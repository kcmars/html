/**
 * Created by zp on 2018/8/27.
 */
$(function () {
    //刷新页面
    $(".refresh").bind("click", function () {
        window.history.back();
    });
    //检测网络
    $(".network").bind("click", function () {
        if(window.navigator.onLine){
            toastAlertShow("网络正常，请稍后再次刷新页面");
            console.log('网络正常！');
        }else{
            toastAlertShow("网络中断，请检查网络后再次刷新页面");
            console.log('网络中断！');
        }
    });
});