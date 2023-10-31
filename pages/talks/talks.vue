<template>
	<view>
		<view class="top">
			<view class="title">
				Chat AI
			</view>
			<view class="add" @click="addChat()">
				<image style="width: 20px;height: 20px;" src="../../static/icon/add.png" mode="aspectFit"></image>
				<view class="text">
					新的聊天
				</view>
			</view>
		</view>
		<view class="talks_list">
			<uni-swipe-action>
				<view v-for="(item,index) in dataList" :key="item.title" @click="goTalk(item)">
					<uni-swipe-action-item :auto-close="true">
						<view class="talk">
							<view class="talk_title">
								{{item.title}}
							</view>
							<view class="footer">
								<view class="time">
									{{item.updateTime}}
								</view>
								<view class="count">
									{{item.talkCount}}条对话
								</view>
							</view>
						</view>
						<template v-slot:right>
							<view class="slot_button" @click.stop="deleteTalk(item)">
								<image style="width: 40px;height: 40px;" src="../../static/icon/delete.png"
									mode="aspectFit"></image>
							</view>
						</template>
					</uni-swipe-action-item>
				</view>
			</uni-swipe-action>

		</view>
	</view>
</template>

<script>
	import {
		getRequest,
		postRequest
	} from '../../util/request.js'
	import {
		getNow
	} from '../../util/timeUtil.js'
	import store from '@/store/index.js'
	export default {
		data() {
			return {
				dataList: [],
				user: {
					userId: "",
				}
			};
		},
		methods: {
			goTalk(item) {
				store.commit('setTalk', item)
				uni.navigateTo({
					url: `/pages/chat/chat?talkId=${item.talkId}&title=${item.title}`
				})
			},
			deleteTalk(item) {
				console.log(item);
				let _this = this
				uni.showModal({
					title: '确定删除？',
					success: function(res) {
						if (res.confirm) {
							let tmp = {
								"talkId": item.talkId
							}
							postRequest(`/talk/delTalk`, tmp).then((e) => {
								if (e.data.code === 200) {
									uni.showToast({
										title: '删除成功'
									})
									_this.getData()
								} else {
									uni.showToast({
										icon: 'error',
										title: '删除失败'
									})
								}
							}).catch((err) => {
								uni.showToast({
									icon: 'error',
									title: '请求失败'
								})
							})
						} else if (res.cancel) {
							uni.showToast({
								icon: 'error',
								title: '删除失败'
							})
						}
					}
				});
				return
			},
			addChat() {
				let _this = this
				uni.showModal({
					title: '取个名字',
					editable: true,
					success: function(res) {
						if (res.confirm) {
							console.log(res);
							let tmp = {
								"title": res.content,
								"updateTime": getNow(),
								"talkCount": 0,
								"userId": _this.user.userId
							}
							postRequest(`/talk/newTalk`, tmp).then((e) => {
								if (e.data.code === 200) {
									uni.showToast({
										title: '添加成功'
									})
									_this.dataList.push(tmp)
								} else {
									uni.showToast({
										icon: 'error',
										title: '添加失败'
									})
								}
							}).catch((err) => {
								uni.showToast({
									icon: 'error',
									title: '请求失败'
								})
							})
						} else if (res.cancel) {
							uni.showToast({
								icon: 'error',
								title: '新增失败'
							})
						}
					}
				});
				return
			},
			getData() {
				getRequest(`/talk/getTalks?userId=${this.user.userId}`).then((res) => {
					console.log(res);
					if (res.data.code === 200) {
						this.dataList = res.data.data
					}
					uni.stopPullDownRefresh()
				}).catch((err) => {
					uni.stopPullDownRefresh()
				})
			}
		},
		onLoad() {
			this.getData()
			this.user = uni.getStorageSync("user")
		},
		onPullDownRefresh() {
			this.getData()
			this.user = uni.getStorageSync("user")
		},
		onShow() {
			this.getData()
		}
	}
</script>

<style lang="scss">
	page {
		width: 100%;
		background: #1d57a2;
		color: #ffffffff;

		.top {
			width: 100%;
			justify-content: space-between;
			display: flex;
			align-items: center;
			margin: 20px 0;

			.title {
				font-size: 18px;
				font-weight: 600;
				margin-left: 20px;
				text-align: left;
			}

			.add {
				width: 100px;
				height: 30px;
				padding: 5px;
				margin-right: 20px;
				background: #2168bf;
				display: flex;
				border-radius: 15px;
				align-items: center;
				justify-content: space-around;

				.text {
					font-size: 14px;
					font-weight: 500;
					color: #ffffffff;
				}
			}
		}


		.talks_list {
			width: calc(100% - 40px);
			margin: 0 20px;
			padding-bottom: 20px;

			.talk {
				background: #2671d3;
				padding: 20px;
				margin-bottom: 20px;
				border-radius: 15px;
				display: flex;
				flex-direction: column;
				justify-content: space-between;
				height: 60px;
				border: #ffffffff 1px solid;

				.footer {
					display: flex;
					justify-content: space-between;
					align-items: center;
					color: #f7f7f7ff;
				}
			}

			.slot_button {
				display: flex;
				align-items: center;
				justify-content: center;
				height: 100px;
			}
		}
	}
</style>