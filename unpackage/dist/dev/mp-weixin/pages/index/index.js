"use strict";
const common_vendor = require("../../common/vendor.js");
const util_request = require("../../util/request.js");
const store_index = require("../../store/index.js");
require("../../store/lawyer.js");
require("../../store/user.js");
require("../../store/chat.js");
const _sfc_main = {
  data() {
    return {
      avatarUrl: "https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0",
      user: {},
      index: 0,
      lawyers: []
    };
  },
  methods: {
    login() {
      let _this = this;
      common_vendor.index.showLoading({
        title: "自动登录中...",
        mask: true
      });
      common_vendor.index.login({
        success(res) {
          console.log(res.code);
          util_request.postRequest(`/user/login`, res).then((res2) => {
            console.log(res2);
            if (res2.data.code === 200) {
              _this.user = res2.data.data.user;
              common_vendor.index.setStorageSync("user", res2.data.data.user);
              store_index.store.commit("login");
              store_index.store.commit("setUser", res2.data.data.user);
              common_vendor.index.hideLoading();
              common_vendor.index.showToast({
                title: "自动登录成功"
              });
              common_vendor.index.stopPullDownRefresh();
            } else {
              common_vendor.index.showToast({
                icon: "error",
                title: "登录出错"
              });
              common_vendor.index.stopPullDownRefresh();
              store_index.store.commit("logout");
            }
          }).catch((err) => {
            common_vendor.index.showToast({
              icon: "error",
              title: "登录出错"
            });
            common_vendor.index.stopPullDownRefresh();
            store_index.store.commit("logout");
          });
        }
      });
    },
    go() {
      common_vendor.index.navigateToMiniProgram({
        appId: "wx0e6dd14489d23504",
        path: "/pages/jump/jump?colType=3&cusColId=300",
        // extraData: {
        //   'data1': 'test'
        // },
        success(res) {
        }
      });
    },
    onGetPhoneNumber(e) {
      let code = e.detail.code;
      console.log(code);
    },
    onChooseAvatar(e) {
      console.log("图片", e);
      this.avatarUrl = e.detail.avatarUrl;
      util_request.postRequest("/user/setAvatar", {
        avatar: e.detail.avatarUrl,
        userId: this.user.userId
      }).then((res) => {
        if (res.data.code === 200) {
          this.user.avatar = e.detail.avatarUrl;
        }
      });
    },
    handlle(i) {
      this.index = i;
    },
    reName() {
      let _this = this;
      common_vendor.index.showModal({
        title: "昵称",
        editable: true,
        success(e) {
          if (e.confirm) {
            console.log(e.content);
            if (e.content === "") {
              return;
            }
            _this.user.userName = e.content;
            util_request.postRequest("/user/setName", {
              userName: _this.user.userName,
              userId: _this.user.userId
            }).then((res) => {
              common_vendor.index.showToast({
                title: "修改成功"
              });
              store_index.store.commit("setUser", res.data.data.user);
            });
          }
        }
      });
    },
    getLawyers() {
      util_request.getRequest(`/lawyer/getLawyers`).then((res) => {
        if (res.data.code === 200) {
          this.lawyers = res.data.data;
        }
      });
    },
    goLawyer(e) {
      store_index.store.commit("setLawyer", e);
      common_vendor.index.navigateTo({
        url: `/pages/lawyer/lawyer?id=${e.id}`
      });
    },
    goHall() {
      common_vendor.index.navigateToMiniProgram({
        appId: "wx0e6dd14489d23504",
        path: "/pages/jump/jump?colType=3&cusColId=309",
        success(res) {
        }
      });
    }
  },
  onLoad() {
    if (!this.$store.getters.getLoginState) {
      this.login();
    }
    this.getLawyers();
  },
  onPullDownRefresh() {
    store_index.store.commit("logout");
    this.login();
    this.getLawyers();
  },
  onShareAppMessage() {
    return {
      title: "智慧云律"
      // path: '/pages/home/index',不支持自定义页面路径
      // query:id=1,//自定义页面路径中携带的参数，如 path?a=1&b=2 的 “?” 后面部分
      // imageUrl: '/static/imgs/mylogo.png',
    };
  }
};
if (!Array) {
  const _easycom_uni_tag2 = common_vendor.resolveComponent("uni-tag");
  _easycom_uni_tag2();
}
const _easycom_uni_tag = () => "../../uni_modules/uni-tag/components/uni-tag/uni-tag.js";
if (!Math) {
  _easycom_uni_tag();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $data.user.avatar
  }, $data.user.avatar ? {
    b: $data.user.avatar
  } : {
    c: $data.avatarUrl
  }, {
    d: common_vendor.o((...args) => $options.onChooseAvatar && $options.onChooseAvatar(...args)),
    e: common_vendor.t($data.user.userName === "" ? "云律用户" : $data.user.userName),
    f: common_vendor.o((...args) => $options.reName && $options.reName(...args)),
    g: common_vendor.p({
      inverted: true,
      text: "优质用户",
      type: "success",
      ["custom-style"]: " border-color: #2671d3; color: #2671d3;"
    }),
    h: common_vendor.o(($event) => $options.handlle(0)),
    i: common_vendor.n($data.index === 0 ? "select" : ""),
    j: common_vendor.o(($event) => $options.handlle(1)),
    k: common_vendor.n($data.index === 1 ? "select" : ""),
    l: common_vendor.o((...args) => $options.goHall && $options.goHall(...args)),
    m: $data.index === 0
  }, $data.index === 0 ? {
    n: common_vendor.f($data.lawyers, (item, k0, i0) => {
      return {
        a: item.headImg,
        b: common_vendor.t(item.lawName),
        c: common_vendor.t(item.experience),
        d: common_vendor.o(($event) => $options.goLawyer(item))
      };
    })
  } : $data.index === 1 ? {} : {}, {
    o: $data.index === 1
  });
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "G:/Vue/云律小程序/cloud-law/pages/index/index.vue"]]);
_sfc_main.__runtimeHooks = 2;
wx.createPage(MiniProgramPage);
