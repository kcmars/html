/**
 * Created by keyC on 2019/1/24.
 */
$(function () {
    /**
     * 前往快车/顺风车认证界面
     */
    $("#express-btn").bind("click", function () {
        window.location.href = "./expressAuthen.html";
    });
    /**
     * 前往出租车认证界面
     */
    $("#taxi-btn").bind("click", function () {
        window.location.href = "./taxiAuthen.html";
    });
});