/**
 * Created by zp on 2019/9/29.
 */
$(function () {
   // getRequest(getParams());
    getParams();
});

function getParams() {
    //返回上一页
    $(".back").bind("click", function () {
       window.history.back();
    });
    //分享详情
    $(".head-right").bind("click", function () {
        window.location.href = "../html/shareList.html";
    });
    //分享按钮
    $(".share-btn li").bind("click", function () {
        let str = $(this).find("span").text();
        switch (str) {
            case "微信":
                toastAlertShow("分享到微信");
                break;
            case "朋友圈":
                toastAlertShow("分享到朋友圈");
                break;
            case "QQ":
                toastAlertShow("分享到QQ");
                break;
            case "空间":
                toastAlertShow("分享到空间");
                break;
        }
    });
}