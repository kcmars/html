/**
 * Created by zp on 2018/9/29.
 */
$(function () {
   // getRequest(getParams());
    getParams();
});

function getParams() {
    //个人信息
    $(".me-info").bind("click", function () {
        window.location.href = "../html/auth.html"
    });
    //me-list
    $(".me-list li").bind("click", function () {
       let str = $(this).find("span").text();
       switch (str) {
           case "我的账户":
               window.location.href = "../html/account.html";
               break;
           case "我的业务":
               window.location.href = "../html/business.html";
               break;
           case "推荐分享":
               window.location.href = "../html/share.html";
               break;
           case "扫一扫":
               window.location.href = "../html/scan.html";
               break;
           case "设置":
               window.location.href = "../html/setting.html";
               break;
       }
    });
}