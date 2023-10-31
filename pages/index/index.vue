<template>
	<view class="container">
		<view class="card">
			<button open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
				<image v-if="user.avatar" style="width: 80px;height: 80px;" :src="user.avatar" mode="aspectFill">
				</image>
				<image v-else style="width: 80px;height: 80px;" :src="avatarUrl" mode="aspectFill"></image>
			</button>
			<view class="content">
				<view class="name" @click="reName">
					{{user.userName === '' ? '云律用户' : user.userName}}
				</view>
				<view class="mod">
					<uni-tag :inverted="true" text="优质用户" type="success"
						custom-style=" border-color: #2671d3; color: #2671d3;" />
				</view>
			</view>
		</view>
		<view class="laws">
			<view class="titles">
				<view @click="handlle(0)" class="title" :class="index === 0 ? 'select':''">
					精英律师
				</view>
				<view @click="handlle(1)" class="title" :class="index === 1 ? 'select':''">
					小智
				</view>
				<view @click="goHall" class="title">
					普法讲堂
				</view>
			</view>
			<view class="laws_list" v-if="index === 0">
				<view class="law" v-for="item in lawyers" @click="goLawyer(item)">
					<view class="main">
						<image style="width: 60px;height: 80px;" :src="item.headImg" mode="aspectFill">
						</image>
						<view class="content">
							<view class="name">
								{{item.lawName}}
							</view>
							<view class="about">
								{{item.experience}}
							</view>
						</view>
					</view>
					<view class="more">
						更多
					</view>
				</view>
			</view>
			<view class="boot" v-else-if="index === 1">
				<image src="http://47.109.45.24/images/chat_boot.png" mode="aspectFit"></image>
			</view>
		</view>
		<!-- <button type="default" @click="login">登录</button>
		<button open-type="getPhoneNumber" @getphonenumber="onGetPhoneNumber">唤起授权</button>
		<view class="">
			<button class="avatar-wrapper" open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
				<image class="avatar" :src="avatarUrl" mode="aspectFit"></image>
			</button>
			<input type="nickname" class="weui-input" placeholder="请输入昵称" />
			<open-data type="userNickName"></open-data>
			<open-data type="userAvatarUrl"></open-data>
			<open-data type="userGender"></open-data>
			<open-data type="groupName"></open-data>
			<open-data type="userCity"></open-data>
			<open-data type="userProvince"></open-data>
			<open-data type="userCountry"></open-data>
			<open-data type="userLanguage"></open-data>
		</view>
		<button @click="go">跳转小程序</button> -->

	</view>
</template>

<script>
	import {
		postRequest,
		getRequest
	} from '../../util/request.js'
	import store from '@/store/index.js'
	export default {
		data() {
			return {
				avatarUrl: 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0',
				user: {},
				index: 0,
				lawyers: []
			}
		},
		methods: {
			login() {
				let _this = this
				uni.showLoading({
					title: '自动登录中...',
					mask: true
				})
				uni.login({
					success(res) {
						console.log(res.code)
						// 将 code 发送到服务端进行校验和业务处理
						postRequest(`/user/login`, res).then((res) => {
							console.log(res);
							if (res.data.code === 200) {
								_this.user = res.data.data.user
								uni.setStorageSync("user", res.data.data.user)
								store.commit('login')
								store.commit('setUser', res.data.data.user)
								uni.hideLoading()
								uni.showToast({
									title: '自动登录成功'
								})
								uni.stopPullDownRefresh()
							} else {
								uni.showToast({
									icon: 'error',
									title: '登录出错'
								})
								uni.stopPullDownRefresh()
								store.commit('logout')
							}
						}).catch((err) => {
							uni.showToast({
								icon: 'error',
								title: '登录出错'
							})
							uni.stopPullDownRefresh()
							store.commit('logout')
						})
					}
				});
			},
			go() {
				uni.navigateToMiniProgram({
					appId: 'wx0e6dd14489d23504',
					path: '/pages/jump/jump?colType=3&cusColId=300',
					// extraData: {
					//   'data1': 'test'
					// },
					success(res) {
						// 打开成功
					}
				})
			},
			onGetPhoneNumber(e) {
				let code = e.detail.code;
				console.log(code);
				if (code) {
					// 需要在这里获取手机号，但是需要先获取到 code，然后通过后台获取解密后的手机号
				}
			},
			onChooseAvatar(e) {
				console.log("图片", e);
				this.avatarUrl = e.detail.avatarUrl
				postRequest("/user/setAvatar", {
					avatar: e.detail.avatarUrl,
					userId: this.user.userId
				}).then((res) => {
					if (res.data.code === 200) {
						this.user.avatar = e.detail.avatarUrl
					}
				})
			},
			handlle(i) {
				this.index = i
			},
			reName() {
				let _this = this
				uni.showModal({
					title: "昵称",
					editable: true,
					success(e) {
						if (e.confirm) {
							console.log(e.content);
							if (e.content === "") {
								return
							}
							_this.user.userName = e.content
							postRequest("/user/setName", {
								userName: _this.user.userName,
								userId: _this.user.userId
							}).then((res) => {
								uni.showToast({
									title: '修改成功'
								})
								store.commit('setUser', res.data.data.user)
							})
						}

					}
				})

			},
			getLawyers() {
				getRequest(`/lawyer/getLawyers`).then((res) => {
					if (res.data.code === 200) {
						this.lawyers = res.data.data
					}
				})
			},
			goLawyer(e) {
				store.commit('setLawyer', e)
				uni.navigateTo({
					url: `/pages/lawyer/lawyer?id=${e.id}`
				})
			},
			goHall() {
				uni.navigateToMiniProgram({
					appId: 'wx0e6dd14489d23504',
					path: '/pages/jump/jump?colType=3&cusColId=309',
					success(res) {
						// 打开成功
					}
				})
			}
		},
		onLoad() {
			if (!this.$store.getters.getLoginState) {
				this.login()
			}
			this.getLawyers()
		},
		onPullDownRefresh() {
			store.commit('logout')
			this.login()
			this.getLawyers()
		},
		onShareAppMessage() {
			return {
				title: '智慧云律',
				// path: '/pages/home/index',不支持自定义页面路径
				// query:id=1,//自定义页面路径中携带的参数，如 path?a=1&b=2 的 “?” 后面部分
				// imageUrl: '/static/imgs/mylogo.png',
			}
		}
	}
</script>

<style lang="scss">
	page {
		background-image: linear-gradient(to top, #2671d3 0%, #1d57a2 1000%);
	}

	.container {
		padding: 20px;
		font-size: 14px;
		line-height: 24px;

		.card {
			display: flex;
			background: #fff;
			border-radius: 15px;
			padding: 20px 15px;

			button {
				/* 去除按钮默认样式 */
				background: transparent;
				border: none;
				color: inherit;
				outline: none;
				cursor: pointer;
				padding: 0;
				margin: 0;
				height: auto;
				border-radius: 0;
				display: flex;
				align-items: center;

				image {
					align-self: center;
				}
			}


			.content {
				display: flex;
				flex-direction: column;
				justify-content: center;
				font-size: 12px;
				margin-left: 30px;
				color: #7a7a7a;

				.name {
					font-size: 18px;
					color: #000000;
					font-weight: 600;
					margin: 10px 0;
				}

			}
		}




		.laws {

			.titles {
				margin: 20px 0;
				display: flex;

				.title {
					width: 80px;
					margin-right: 10px;
					background: rgba(255, 255, 255, 0.8);
					color: #484848;
					border-radius: 15px;
					font-size: 14px;
					font-weight: 600;
					text-align: center;
					padding: 5px 0;
				}

				.select {
					background: #fff;
					color: #000000;
				}
			}

			.laws_list {
				background: #fff;
				border-radius: 15px;
				padding: 20px 0px;
				font-size: 14px;

				.law {
					display: flex;
					align-items: center;
					justify-content: space-between;

					.main {
						display: flex;
						align-items: center;
						padding: 0 10px;

						image {
							padding: 10px;
						}

						.name {
							font-size: 15px;
						}

						.about {
							color: #7a7a7a;
						}
					}

					.more {
						color: #2671d3;
						padding: 0 6px;
						margin-right: 10px;
						border: 1px solid #2671d3;
						border-radius: 15px;
						min-width: 30px;
						text-align: center;
					}
				}
			}

			.boot {
				border-radius: 15px;
				background: #fff;
				margin-top: 20px;
				display: flex;
				align-items: center;
				justify-content: center;
			}
		}
	}
</style>