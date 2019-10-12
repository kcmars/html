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
    //我的优惠券
    $(".coupon").bind("click", function () {
        window.location.href = "../html/myCoupon.html";
    });
    //更多活动
    $(".activity").bind("click", function () {
        window.location.href = "../html/moreActivity.html";
    });
}