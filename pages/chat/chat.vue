<template>
	<view class="chatPage">
		<scroll-view :style="{height: `${windowHeight}px`,marginBottom:`${inputHeight}px`}" id="scrollview"
			scroll-y="true" :scroll-top="scolleHeight" :scroll-with-animation="true" class="scroll-view"
			@scroll="onScroll">
			<!-- 聊天主体 -->
			<view id="msglistview" class="chat-body">
				<!-- 聊天记录 -->
				<view>
					<view class="item Ai">
						<!-- 头像 -->
						<view class="avatar">
							<image style="width: 50px;height: 50px;" src="@/static/img/yunlv_logo.png"
								mode="aspectFill"></image>
						</view>
						<!-- 文字内容 -->
						<view class="chat">
							<view class="content left" @longpress="handleCopy(item.content)">
								您好我是您的专属资深法律顾问，我叫小智，您可以问我任何法律相关问题
							</view>
						</view>
					</view>
				</view>
				<view v-for="(item,index) in msgList" :key="index">
					<!-- 自己发的消息 -->
					<view class="item self" v-if="item.initiator === '0'">
						<!-- 文字内容 -->
						<view class="content right" @longpress="handleCopy(item.content)">
							{{item.content}}
						</view>
						<!-- 头像 -->
						<view class="avatar">
							<image style="width: 50px;height: 50px;" :src="user.avatar" mode="aspectFill">
							</image>
							<!-- <open-data type="userAvatarUrl"></open-data> -->
						</view>
					</view>
					<!-- 机器人发的消息 -->
					<view class="item Ai" v-if="item.initiator === '1'">
						<!-- 头像 -->
						<view class="avatar">
							<image style="width: 50px;height: 50px;" src="@/static/img/yunlv_logo.png"
								mode="aspectFill"></image>
						</view>
						<!-- 文字内容 -->
						<view class="chat">
							<view class="content left" @longpress="handleCopy(item.content)">
								{{item.content}}
							</view>
							<view class="time">
								{{item.createTime}}
							</view>
						</view>
					</view>

					<!-- 推荐的律师 -->

					<view class="item Ai" v-if="item.initiator === '2'">
						<view class="avatar">
							<image style="width: 50px;height: 50px;" src="@/static/img/yunlv_logo.png"
								mode="aspectFill"></image>
						</view>
						<view class="chat">
							<view class="content left">
								小智根据和您的对话，为你推荐如下律师
								<view class="link" @click="goLawyer(JSON.parse(item.content))">
									点击去查看
								</view>
							</view>
						</view>
					</view>

					<view class="item Ai" v-if="item.initiator === '2'">
						<view class="avatar">
							<image style="width: 50px;height: 50px;" src="@/static/img/yunlv_logo.png"
								mode="aspectFill"></image>
						</view>
						<view class="chat">
							<view class="content left" @click="goLawyer(JSON.parse(item.content))">
								<view class="recommend">
									<view class="icon">
										<image style="width: 60px;height: 80px;" :src="JSON.parse(item.content).headImg"
											mode="aspectFill"></image>
									</view>
									<view class="about">
										<view class="t">
											{{JSON.parse(item.content).lawName}}
										</view>
										<view class="h">
											{{JSON.parse(item.content).area}}
										</view>
									</view>
								</view>
								<view class="">
									{{JSON.parse(item.content).experience}}。
									{{JSON.parse(item.content).caseResults}}。
								</view>
							</view>
							<view class="time">
								{{item.createTime}}
							</view>
						</view>
					</view>


				</view>
			</view>
		</scroll-view>
		<!-- 底部消息发送栏 -->
		<!-- 用来占位，防止聊天消息被发送框遮挡 -->
		<view class="chat-bottom">
			<view class="send-msg" :style="{bottom: `${inputHeight}px`}">
				<view class="uni-textarea">
					<textarea v-model="chatMsg" maxlength="300" :adjust-position="false" :show-confirm-bar="false"
						:auto-height="true" @focus="fixHeight" @blur="restoreHeight" :hold-keyboard="true"
						:focus="isFocus"></textarea>
				</view>
				<button @click="handleSend" class="send-btn">发送</button>
			</view>
		</view>
	</view>
</template>
<script>
	import {
		getRequest,
		postRequest
	} from '../../util/request.js'
	import store from '@/store/index.js'
	export default {
		data() {
			return {
				userId: '',
				talkId: "",
				height: 0,
				//发送的消息
				chatMsg: "",
				msgList: [],
				inputHeight: 0,
				windowHeight: 0,
				user: {},
				isFocus: false
			}
		},
		computed: {
			scolleHeight() {
				return this.height
			},
			thinking() {
				return this.$store.getters.getThingking
			},
			talk() {
				return this.$store.getters.getTalk
			}
		},
		methods: {
			handleCopy(text) {
				uni.setClipboardData({
					data: text,
					success() {
						uni.showToast({
							title: '已复制到剪贴板',
							icon: 'none'
						});
					}
				});
			},
			// px转换成rpx
			rpxTopx(px) {
				let deviceWidth = uni.getSystemInfoSync().windowWidth
				let rpx = (750 / deviceWidth) * Number(px)
				return Math.floor(rpx)
			},
			// rpx转换成px
			pxTorpx(rpx) {
				let deviceWidth = uni.getSystemInfoSync().windowWidth
				let px = Number(rpx) * deviceWidth / 750
				return Math.floor(px)
			},
			fixHeight(e) {
				this.inputHeight = e.detail.height
				this.windowHeight -= e.detail.height
				setTimeout(() => {
					this.height += 10000
				})
				this.isFocus = true
			},
			restoreHeight() {
				this.inputHeight = 0
				this.windowHeight = uni.getSystemInfoSync().windowHeight - this.pxTorpx(127)
			},
			// 发送消息
			handleSend(e) {
				this.isFocus = true
				if (this.thinking) {
					uni.showToast({
						icon: 'error',
						title: '正在思考中...'
					})
					return
				}
				//如果消息不为空
				if (!(/^\s*$/.test(this.chatMsg)) || !this.chatMsg === "") {
					console.log(!this.chatMsg);
					console.log(!(/^\s*$/.test(this.chatMsg)));
					console.log(!this.chatMsg === "");
					store.commit('setThingking', this.talk.talkId, true)
					let obj = {
						talkId: this.talkId,
						content: this.chatMsg,
						initiator: '0'
					}
					this.msgList.push(obj);
					this.msgList.push({
						talkId: this.talkId,
						content: "正在思考中...",
						initiator: '1'
					});
					// 发送请求
					postRequest("/talk/sendMsg", obj).then((res) => {
						if (res.data.code === 200) {
							this.msgList.pop()
							this.msgList.push(res.data.data);
							this.height += 1000
							store.commit('setThingking', this.talk.talkId, false)
							store.commit('talkCountPlus')
							this.getRecommend()
						} else {
							this.msgList.pop()
							uni.showToast({
								icon: 'error',
								title: '发送失败'
							})
							store.commit('setThingking', this.talk.talkId, false)
						}
					}).catch(() => {
						this.msgList.pop()
						uni.showToast({
							icon: 'error',
							title: '发送失败'
						})
						store.commit('setThingking', this.talk.talkId, false)
					})
					this.chatMsg = '';
					this.height += 1000
				} else {
					uni.showToast({
						icon: 'error',
						title: '不能发送空白消息'
					})
					store.commit('setThingking', this.talk.talkId, false)
				}
			},
			onScroll(e) {
				console.log(e);
			},
			getData() {
				getRequest(`/talk/getMsg?talkId=${this.talkId}`).then((res) => {
					console.log(res);
					if (res.data.code === 200) {
						this.msgList = res.data.data
						this.height += this.msgList.length * 500
					}
				}).catch((err) => {
					uni.showToast({
						icon: 'error',
						title: '消息加载失败'
					})
				})
			},
			goLawyer(e) {
				store.commit('setLawyer', e)
				uni.navigateTo({
					url: `/pages/lawyer/lawyer?id=${e.id}`
				})
			},
			//  推荐律师
			getRecommend() {
				// console.log(this.talk);
				// console.log(this.talk.talkCount % 3 != 0);
				if (this.talk.talkCount % 5 != 0) {
					return
				}
				store.commit('setThingking', this.talk.talkId, true)
				this.msgList.push({
					talkId: this.talkId,
					content: "正在匹配您的专属律师...",
					initiator: '1'
				});
				// 发送请求
				postRequest(`/talk/getRecommend`, {
					"content": "通过对话的上下文，你认为如果推荐一名律师给我，该律师的专业领域，经验资格，案例，费用要求分别是怎么样的",
					"talkId": this.talkId
				}).then((res) => {
					if (res.data.code === 200) {
						this.msgList.pop()
						this.msgList.push(res.data.data.msg);
						store.commit('talkCountPlus')
						this.height += 1000
					}
					store.commit('setThingking', this.talk.talkId, false)
				}).catch((err) => {
					store.commit('setThingking', this.talk.talkId, false)
				})

			}
		},
		onLoad(e) {
			this.talkId = e.talkId
			this.getData()
			uni.setNavigationBarTitle({
				title: e.title
			});
			this.windowHeight = uni.getSystemInfoSync().windowHeight - this.pxTorpx(127)
			this.user = uni.getStorageSync("user")
			// uni.onUnload(() => {
			// 	uni.showModal({
			// 		title: '确认退出',
			// 		content: '确定要退出页面吗？',
			// 		success: (res) => {
			// 			if (res.confirm) {
			// 				// 用户点击确定，执行退出逻辑
			// 				uni.navigateBack();
			// 			} else if (res.cancel) {
			// 				// 用户点击取消，可以不做任何操作，或者执行其他操作
			// 			}
			// 		}
			// 	});
			// })
		},
		onPullDownRefresh() {
			this.getData()
		},
		// onUnload() {
		// 	// uni.showModal({
		// 	//   title: '确认退出',
		// 	//   content: '确定要退出页面吗？',
		// 	//   success: (res) => {
		// 	//     if (res.confirm) {
		// 	//       // 用户点击确定，执行退出逻辑
		// 	//       uni.navigateBack();
		// 	//     } else if (res.cancel) {
		// 	//       // 用户点击取消，可以不做任何操作，或者执行其他操作
		// 	//     }
		// 	//   }
		// 	// });
		// }
	}
</script>
<style lang="scss" scoped>
	$chatContentbgc: #2671d3;
	$sendBtnbgc: #4F7DF5;

	view,
	button,
	text,
	input,
	textarea {
		margin: 0;
		padding: 0;
		box-sizing: border-box;
	}

	/* 聊天消息 */
	.chatPage {
		overflow: hidden;

		.scroll-view {
			::-webkit-scrollbar {
				display: none;
				width: 0 !important;
				height: 0 !important;
				-webkit-appearance: none;
				background: transparent;
				color: transparent;
			}

			// background-color: orange;
			background-color: #F6F6F6;

			.chat-body {
				display: flex;
				flex-direction: column;
				padding-top: 23rpx;
				// background-color:skyblue;

				.self {
					justify-content: flex-end;
				}

				.item {
					display: flex;
					padding: 23rpx 30rpx;
					// background-color: greenyellow;

					.chat {
						.time {
							font-size: 12px;
							color: #666666;
							margin: 5rpx 20rpx;
							padding: 5rpx 20rpx;
						}

						.recommend {
							display: flex;
							align-items: center;
							justify-content: space-around;

							.t {
								font-size: 22px;
							}

							.h {
								font-size: 16px;
							}

							.icon {
								margin: 10px 5px;
							}
						}
					}

					.right {
						background-color: $chatContentbgc;
						color: #FFFFFF !important;
					}

					.left {
						background-color: #FFFFFF;
					}

					// 聊天消息的三角形
					.right::after {
						position: absolute;
						display: inline-block;
						content: '';
						width: 0;
						height: 0;
						left: 100%;
						top: 10px;
						border: 12rpx solid transparent;
						border-left: 12rpx solid $chatContentbgc;
					}

					.left::after {
						position: absolute;
						display: inline-block;
						content: '';
						width: 0;
						height: 0;
						top: 10px;
						right: 100%;
						border: 12rpx solid transparent;
						border-right: 12rpx solid #FFFFFF;
					}

					.content {
						position: relative;
						max-width: 486rpx;
						border-radius: 8rpx;
						word-wrap: break-word;
						padding: 24rpx 24rpx;
						margin: 0 24rpx;
						border-radius: 5px;
						font-size: 32rpx;
						font-family: PingFang SC;
						font-weight: 500;
						color: #333333;
						line-height: 50rpx;

						.link {
							color: #2671d3;
							display: inline-block;
							text-decoration: #2671d3;
						}
					}

					.avatar {
						display: flex;
						justify-content: center;
						width: 78rpx;
						height: 78rpx;
						background: transparent;
						border-radius: 8rpx;
						overflow: hidden;

						image {
							align-self: center;
						}

					}
				}
			}
		}

		/* 底部聊天发送栏 */
		.chat-bottom {
			width: 100%;
			height: 127rpx;
			background: #F4F5F7;

			.send-msg {
				display: flex;
				align-items: flex-end;
				padding: 16rpx 30rpx;
				width: 100%;
				min-height: 127rpx;
				position: fixed;
				background: #EDEDED;
				display: flex;
				align-items: center;

				.send-btn {
					display: flex;
					align-items: center;
					justify-content: center;
					margin-bottom: 20rpx;
					margin-left: 25rpx;
					width: 128rpx;
					height: 75rpx;
					background: $sendBtnbgc;
					border-radius: 8rpx;
					font-size: 28rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #FFFFFF;
					line-height: 28rpx;
				}

				.uni-textarea {
					margin-bottom: 20rpx;
					background: #FFFFFF;
					padding: 15rpx 8rpx;
					border-radius: 8rpx;

					textarea {
						width: 537rpx;
						min-height: 43rpx;
						max-height: 500rpx;
						font-size: 35rpx;
						font-family: PingFang SC;
						color: #333333;
						line-height: 36rpx;
					}
				}

			}



		}

	}
</style>