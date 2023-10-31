"use strict";
const common_vendor = require("../../common/vendor.js");
const util_request = require("../../util/request.js");
const store_index = require("../../store/index.js");
const common_assets = require("../../common/assets.js");
require("../../store/lawyer.js");
require("../../store/user.js");
require("../../store/chat.js");
const _sfc_main = {
  data() {
    return {
      userId: "",
      talkId: "",
      height: 0,
      //发送的消息
      chatMsg: "",
      msgList: [],
      inputHeight: 0,
      windowHeight: 0,
      user: {},
      isFocus: false
    };
  },
  computed: {
    scolleHeight() {
      return this.height;
    },
    thinking() {
      return this.$store.getters.getThingking;
    },
    talk() {
      return this.$store.getters.getTalk;
    }
  },
  methods: {
    handleCopy(text) {
      common_vendor.index.setClipboardData({
        data: text,
        success() {
          common_vendor.index.showToast({
            title: "已复制到剪贴板",
            icon: "none"
          });
        }
      });
    },
    // px转换成rpx
    rpxTopx(px) {
      let deviceWidth = common_vendor.index.getSystemInfoSync().windowWidth;
      let rpx = 750 / deviceWidth * Number(px);
      return Math.floor(rpx);
    },
    // rpx转换成px
    pxTorpx(rpx) {
      let deviceWidth = common_vendor.index.getSystemInfoSync().windowWidth;
      let px = Number(rpx) * deviceWidth / 750;
      return Math.floor(px);
    },
    fixHeight(e) {
      this.inputHeight = e.detail.height;
      this.windowHeight -= e.detail.height;
      setTimeout(() => {
        this.height += 1e4;
      });
      this.isFocus = true;
    },
    restoreHeight() {
      this.inputHeight = 0;
      this.windowHeight = common_vendor.index.getSystemInfoSync().windowHeight - this.pxTorpx(127);
    },
    // 发送消息
    handleSend(e) {
      this.isFocus = true;
      if (this.thinking) {
        common_vendor.index.showToast({
          icon: "error",
          title: "正在思考中..."
        });
        return;
      }
      if (!/^\s*$/.test(this.chatMsg) || !this.chatMsg === "") {
        console.log(!this.chatMsg);
        console.log(!/^\s*$/.test(this.chatMsg));
        console.log(!this.chatMsg === "");
        store_index.store.commit("setThingking", this.talk.talkId, true);
        let obj = {
          talkId: this.talkId,
          content: this.chatMsg,
          initiator: "0"
        };
        this.msgList.push(obj);
        this.msgList.push({
          talkId: this.talkId,
          content: "正在思考中...",
          initiator: "1"
        });
        util_request.postRequest("/talk/sendMsg", obj).then((res) => {
          if (res.data.code === 200) {
            this.msgList.pop();
            this.msgList.push(res.data.data);
            this.height += 1e3;
            store_index.store.commit("setThingking", this.talk.talkId, false);
            store_index.store.commit("talkCountPlus");
            this.getRecommend();
          } else {
            this.msgList.pop();
            common_vendor.index.showToast({
              icon: "error",
              title: "发送失败"
            });
            store_index.store.commit("setThingking", this.talk.talkId, false);
          }
        }).catch(() => {
          this.msgList.pop();
          common_vendor.index.showToast({
            icon: "error",
            title: "发送失败"
          });
          store_index.store.commit("setThingking", this.talk.talkId, false);
        });
        this.chatMsg = "";
        this.height += 1e3;
      } else {
        common_vendor.index.showToast({
          icon: "error",
          title: "不能发送空白消息"
        });
        store_index.store.commit("setThingking", this.talk.talkId, false);
      }
    },
    onScroll(e) {
      console.log(e);
    },
    getData() {
      util_request.getRequest(`/talk/getMsg?talkId=${this.talkId}`).then((res) => {
        console.log(res);
        if (res.data.code === 200) {
          this.msgList = res.data.data;
          this.height += this.msgList.length * 500;
        }
      }).catch((err) => {
        common_vendor.index.showToast({
          icon: "error",
          title: "消息加载失败"
        });
      });
    },
    goLawyer(e) {
      store_index.store.commit("setLawyer", e);
      common_vendor.index.navigateTo({
        url: `/pages/lawyer/lawyer?id=${e.id}`
      });
    },
    //  推荐律师
    getRecommend() {
      if (this.talk.talkCount % 5 != 0) {
        return;
      }
      store_index.store.commit("setThingking", this.talk.talkId, true);
      this.msgList.push({
        talkId: this.talkId,
        content: "正在匹配您的专属律师...",
        initiator: "1"
      });
      util_request.postRequest(`/talk/getRecommend`, {
        "content": "通过对话的上下文，你认为如果推荐一名律师给我，该律师的专业领域，经验资格，案例，费用要求分别是怎么样的",
        "talkId": this.talkId
      }).then((res) => {
        if (res.data.code === 200) {
          this.msgList.pop();
          this.msgList.push(res.data.data.msg);
          store_index.store.commit("talkCountPlus");
          this.height += 1e3;
        }
        store_index.store.commit("setThingking", this.talk.talkId, false);
      }).catch((err) => {
        store_index.store.commit("setThingking", this.talk.talkId, false);
      });
    }
  },
  onLoad(e) {
    this.talkId = e.talkId;
    this.getData();
    common_vendor.index.setNavigationBarTitle({
      title: e.title
    });
    this.windowHeight = common_vendor.index.getSystemInfoSync().windowHeight - this.pxTorpx(127);
    this.user = common_vendor.index.getStorageSync("user");
  },
  onPullDownRefresh() {
    this.getData();
  }
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
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_assets._imports_0,
    b: common_vendor.o(($event) => $options.handleCopy(_ctx.item.content)),
    c: common_vendor.f($data.msgList, (item, index, i0) => {
      return common_vendor.e({
        a: item.initiator === "0"
      }, item.initiator === "0" ? {
        b: common_vendor.t(item.content),
        c: common_vendor.o(($event) => $options.handleCopy(item.content), index),
        d: $data.user.avatar
      } : {}, {
        e: item.initiator === "1"
      }, item.initiator === "1" ? {
        f: common_assets._imports_0,
        g: common_vendor.t(item.content),
        h: common_vendor.o(($event) => $options.handleCopy(item.content), index),
        i: common_vendor.t(item.createTime)
      } : {}, {
        j: item.initiator === "2"
      }, item.initiator === "2" ? {
        k: common_assets._imports_0,
        l: common_vendor.o(($event) => $options.goLawyer(JSON.parse(item.content)), index)
      } : {}, {
        m: item.initiator === "2"
      }, item.initiator === "2" ? {
        n: common_assets._imports_0,
        o: JSON.parse(item.content).headImg,
        p: common_vendor.t(JSON.parse(item.content).lawName),
        q: common_vendor.t(JSON.parse(item.content).area),
        r: common_vendor.t(JSON.parse(item.content).experience),
        s: common_vendor.t(JSON.parse(item.content).caseResults),
        t: common_vendor.o(($event) => $options.goLawyer(JSON.parse(item.content)), index),
        v: common_vendor.t(item.createTime)
      } : {}, {
        w: index
      });
    }),
    d: `${$data.windowHeight}px`,
    e: `${$data.inputHeight}px`,
    f: $options.scolleHeight,
    g: common_vendor.o((...args) => $options.onScroll && $options.onScroll(...args)),
    h: common_vendor.o((...args) => $options.fixHeight && $options.fixHeight(...args)),
    i: common_vendor.o((...args) => $options.restoreHeight && $options.restoreHeight(...args)),
    j: $data.isFocus,
    k: $data.chatMsg,
    l: common_vendor.o(($event) => $data.chatMsg = $event.detail.value),
    m: common_vendor.o((...args) => $options.handleSend && $options.handleSend(...args)),
    n: `${$data.inputHeight}px`
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-0a633310"], ["__file", "G:/Vue/云律小程序/cloud-law/pages/chat/chat.vue"]]);
wx.createPage(MiniProgramPage);
