"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {};
  },
  onLoad() {
    common_vendor.index.navigateToMiniProgram({
      appId: "wx0e6dd14489d23504",
      path: "/pages/jump/jump?colType=3&cusColId=309",
      success(res) {
      }
    });
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {};
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "G:/Vue/云律小程序/cloud-law/pages/hall/hall.vue"]]);
wx.createPage(MiniProgramPage);
