"use strict";
const common_vendor = require("../../common/vendor.js");
const util_request = require("../../util/request.js");
const util_timeUtil = require("../../util/timeUtil.js");
const store_index = require("../../store/index.js");
require("../../store/lawyer.js");
require("../../store/user.js");
require("../../store/chat.js");
const _sfc_main = {
  data() {
    return {
      dataList: [],
      user: {
        userId: ""
      }
    };
  },
  methods: {
    goTalk(item) {
      store_index.store.commit("setTalk", item);
      common_vendor.index.navigateTo({
        url: `/pages/chat/chat?talkId=${item.talkId}&title=${item.title}`
      });
    },
    deleteTalk(item) {
      console.log(item);
      let _this = this;
      common_vendor.index.showModal({
        title: "确定删除？",
        success: function(res) {
          if (res.confirm) {
            let tmp = {
              "talkId": item.talkId
            };
            util_request.postRequest(`/talk/delTalk`, tmp).then((e) => {
              if (e.data.code === 200) {
                common_vendor.index.showToast({
                  title: "删除成功"
                });
                _this.getData();
              } else {
                common_vendor.index.showToast({
                  icon: "error",
                  title: "删除失败"
                });
              }
            }).catch((err) => {
              common_vendor.index.showToast({
                icon: "error",
                title: "请求失败"
              });
            });
          } else if (res.cancel) {
            common_vendor.index.showToast({
              icon: "error",
              title: "删除失败"
            });
          }
        }
      });
      return;
    },
    addChat() {
      let _this = this;
      common_vendor.index.showModal({
        title: "取个名字",
        editable: true,
        success: function(res) {
          if (res.confirm) {
            console.log(res);
            let tmp = {
              "title": res.content,
              "updateTime": util_timeUtil.getNow(),
              "talkCount": 0,
              "userId": _this.user.userId
            };
            util_request.postRequest(`/talk/newTalk`, tmp).then((e) => {
              if (e.data.code === 200) {
                common_vendor.index.showToast({
                  title: "添加成功"
                });
                _this.dataList.push(tmp);
              } else {
                common_vendor.index.showToast({
                  icon: "error",
                  title: "添加失败"
                });
              }
            }).catch((err) => {
              common_vendor.index.showToast({
                icon: "error",
                title: "请求失败"
              });
            });
          } else if (res.cancel) {
            common_vendor.index.showToast({
              icon: "error",
              title: "新增失败"
            });
          }
        }
      });
      return;
    },
    getData() {
      util_request.getRequest(`/talk/getTalks?userId=${this.user.userId}`).then((res) => {
        console.log(res);
        if (res.data.code === 200) {
          this.dataList = res.data.data;
        }
        common_vendor.index.stopPullDownRefresh();
      }).catch((err) => {
        common_vendor.index.stopPullDownRefresh();
      });
    }
  },
  onLoad() {
    this.getData();
    this.user = common_vendor.index.getStorageSync("user");
  },
  onPullDownRefresh() {
    this.getData();
    this.user = common_vendor.index.getStorageSync("user");
  },
  onShow() {
    this.getData();
  }
};
if (!Array) {
  const _easycom_uni_swipe_action_item2 = common_vendor.resolveComponent("uni-swipe-action-item");
  const _easycom_uni_swipe_action2 = common_vendor.resolveComponent("uni-swipe-action");
  (_easycom_uni_swipe_action_item2 + _easycom_uni_swipe_action2)();
}
const _easycom_uni_swipe_action_item = () => "../../uni_modules/uni-swipe-action/components/uni-swipe-action-item/uni-swipe-action-item.js";
const _easycom_uni_swipe_action = () => "../../uni_modules/uni-swipe-action/components/uni-swipe-action/uni-swipe-action.js";
if (!Math) {
  (_easycom_uni_swipe_action_item + _easycom_uni_swipe_action)();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.o(($event) => $options.addChat()),
    b: common_vendor.f($data.dataList, (item, index, i0) => {
      return {
        a: common_vendor.t(item.title),
        b: common_vendor.t(item.updateTime),
        c: common_vendor.t(item.talkCount),
        d: common_vendor.o(($event) => $options.deleteTalk(item), item.title),
        e: "0eec80a2-1-" + i0 + ",0eec80a2-0",
        f: item.title,
        g: common_vendor.o(($event) => $options.goTalk(item), item.title)
      };
    }),
    c: common_vendor.p({
      ["auto-close"]: true
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "G:/Vue/云律小程序/cloud-law/pages/talks/talks.vue"]]);
wx.createPage(MiniProgramPage);
