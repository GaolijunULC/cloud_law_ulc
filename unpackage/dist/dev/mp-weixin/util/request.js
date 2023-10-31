"use strict";
const common_vendor = require("../common/vendor.js");
var serverUrl = "http://47.109.45.24:5254/api";
let token = common_vendor.index.getStorageSync("access_token") || "";
let header = {
  "content-type": "application/json",
  "Authorization": "Bearer " + token
};
function postRequest(url, data) {
  token = common_vendor.index.getStorageSync("access_token") || "";
  header = {
    "content-type": "application/json",
    "Authorization": "Bearer " + token
  };
  return new Promise((resolve, reject) => {
    common_vendor.index.request({
      url: serverUrl + url,
      data,
      header,
      method: "POST",
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res);
        } else {
          reject(res);
        }
      },
      fail: (res) => {
        reject(res);
      }
    });
  });
}
function getRequest(url) {
  token = common_vendor.index.getStorageSync("access_token") || "";
  header = {
    "Authorization": "Bearer " + token,
    "Access-Control-Allow-Origin": "*"
  };
  return new Promise((resolve, reject) => {
    common_vendor.index.request({
      url: serverUrl + url,
      header,
      method: "GET",
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res);
        } else {
          reject(res);
        }
      },
      fail: (res) => {
        reject(res);
      }
    });
  });
}
exports.getRequest = getRequest;
exports.postRequest = postRequest;
