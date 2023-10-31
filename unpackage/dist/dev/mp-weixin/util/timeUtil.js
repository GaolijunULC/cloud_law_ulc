"use strict";
function getNow() {
  const currentDate = /* @__PURE__ */ new Date();
  const year = currentDate.getFullYear();
  const month = ("0" + (currentDate.getMonth() + 1)).slice(-2);
  const date = ("0" + currentDate.getDate()).slice(-2);
  const hours = ("0" + currentDate.getHours()).slice(-2);
  const minutes = ("0" + currentDate.getMinutes()).slice(-2);
  const formattedDate = `${year}-${month}-${date} ${hours}:${minutes}`;
  return formattedDate;
}
exports.getNow = getNow;
