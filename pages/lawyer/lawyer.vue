<template>
	<view>
		<view class="bg"></view>

		<view class="content">
			<view class="card">
				<view class="container">
					<view class="avatar" @click="seeImg" :style="{ backgroundImage: 'url(' + lawyer.headImg + ')' }">
					</view>
				</view>
				<view class="name">
					{{lawyer.lawName}}律师
				</view>
				<view class="msgs">
					<view class="msg">
						<view class="describe">
							{{lawyer.serviceNum}}人
						</view>
						<view class="title">
							服务人数
						</view>
					</view>
					<view class="line"></view>
					<view class="msg">
						<view class="describe">
							{{lawyer.area}}
						</view>
						<view class="title">
							擅长领域
						</view>
					</view>
				</view>
			</view>
			<uni-section title="律师信息" type="line" padding>
				<view class="intros">
					<view class="intro">
						<view class="title">经验资格：</view>
						<view class="text">{{lawyer.experience}}\n</view>
					</view>
					<view class="intro">
						<view class="title">案例成果：</view>
						<view class="text">{{lawyer.caseResults}}\n</view>
					</view>
					<view class="intro">
						<view class="title">费用结构：</view>
						<view class="text">{{lawyer.cost}}\n</view>
					</view>
				</view>
			</uni-section>
			<uni-section title="经典案例" type="line" padding>
				<view style="color: #2981eb;" @click="goPage">
					前往查看经典案例
				</view>
			</uni-section>
		</view>
		<view class="footer">
			<view class="container">
				<view class="avatar" :style="{ backgroundImage: 'url(' + lawyer.headImg + ')' }"></view>
			</view>
			<view class="content">
				<view class="title" @click="callPhone">
					您是否需要咨询{{lawyer.lawName}}律师
				</view>
				<view class="phone" @click="callPhone">
					<image style="width: 20px;height: 20px;" src="../../static/icon/phone.png" mode="aspectFit"></image>
					<view class="text">
						电话联系
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import store from '@/store/index.js'
	import {
		getRequest
	} from '@/util/request.js'
	export default {
		data() {
			return {

			};
		},
		computed: {
			lawyer() {
				return this.$store.getters.getLawyer
			}
		},
		onLoad(e) {
			console.log(this.lawyer);
			console.log(e);
			if (this.lawyer === null) {
				this.getLawyers(e.id)
			}
		},
		methods: {
			callPhone() {
				uni.makePhoneCall({
					phoneNumber: this.lawyer.phone
				});
			},
			goPage() {
				uni.navigateToMiniProgram({
					appId: 'wx0e6dd14489d23504',
					path: '/pages/jump/jump?colType=3&cusColId=314',
					success(res) {
						// 打开成功
					}
				})
			},
			getLawyers(id) {
				getRequest(`/lawyer/getLawyers`).then((res) => {
					if (res.data.code === 200) {
						// console.log(res.data.data);
						let tmp = res.data.data.find((e) => {
							return e.id === Number(id)
						})
						store.commit('setLawyer', tmp)
						// console.log(tmp);
					}
				})
			},
			seeImg() {
				uni.previewImage({
					urls: [this.lawyer.headImg],
					longPressActions: {
						itemList: ['发送给朋友', '保存图片', '收藏'],
						success: function(data) {
							console.log('选中了第' + (data.tapIndex + 1) + '个按钮,第' + (data.index + 1) + '张图片');
						},
						fail: function(err) {
							console.log(err.errMsg);
						}
					}
				});
			}
		},
		onShareAppMessage() {
			return {
				title: `${this.lawyer.lawName}律师`,
				path: `/pages/lawyer/lawyer?id=${this.lawyer.id}`,
				// query: id = this.lawyer.id, //自定义页面路径中携带的参数，如 path?a=1&b=2 的 “?” 后面部分
				imageUrl: `${this.lawyer.headImg}`,
			}
		}
	}
</script>

<style lang="scss" scoped>
	.bg {
		position: absolute;
		top: 0;
		width: 100%;
		height: 120px;
		background: #2570d0;
		z-index: -1;
	}

	.content {
		.card {
			height: 160px;
			margin: 70px 20px 20px 20px;
			background: #fff;
			display: flex;
			flex-direction: column;
			align-items: center;
			border-radius: 5px;
			padding-bottom: 5px;

			.container {
				width: 100px;
				height: 100px;
				display: flex;
				justify-content: center;
				align-items: center;
				margin-top: -50px;

				.avatar {
					width: 100%;
					height: 100%;
					background-size: cover;
					background-repeat: no-repeat;
					background-position: top;
					border-radius: 50%;
				}
			}

			.name {
				font-size: 22px;
				font-weight: 600;
				padding: 10px;
			}

			.msgs {
				width: 80%;
				border-top: 1px solid rgba(0, 0, 0, 0.1);
				display: flex;
				justify-content: space-evenly;
				align-items: center;

				.line {
					width: 1px;
					height: 40px;
					background: rgba(0, 0, 0, 0.1);
				}

				.msg {
					display: flex;
					flex-direction: column;
					align-items: center;

					.describe {
						font-size: 18px;
						line-height: 30px;
					}

					.title {
						font-size: 14px;
						color: rgba(0, 0, 0, 0.7);
					}
				}
			}
		}


		.intros {
			display: flex;
			flex-direction: column;

			.intro {
				display: flex;

				.title {
					min-width: 70px;
				}
			}
		}
	}

	.footer {
		position: fixed;
		bottom: 0;
		height: 100px;
		width: 100%;
		box-shadow: 0 -1px 17px 1px rgba(0, 0, 0, 0.1);
		display: flex;
		align-items: center;
		justify-content: space-evenly;
		padding-bottom: 20px;

		.container {
			width: 60px;
			height: 60px;
			display: flex;
			justify-content: center;
			align-items: center;

			.avatar {
				width: 100%;
				height: 100%;
				background-size: cover;
				background-repeat: no-repeat;
				background-position: top;
				border-radius: 50%;
			}
		}

		.content {
			width: 60%;

			.title {
				font-size: 15px;
				margin-bottom: 10px;
			}

			.phone {
				width: 120px;
				height: 30px;
				background: #2570d0;
				color: #fff;
				border-radius: 50px;
				display: flex;
				align-items: center;
				justify-content: space-evenly;
			}
		}
	}
</style>