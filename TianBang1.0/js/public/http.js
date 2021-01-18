/**
 * 网络资源js
 * ip、链接、接口地址、资源地址、静态变量
 */
$(function () {

    $.srcStatic = "http://localhost:63342/TianBang1.0/html/";

    /**
     * 防止键盘把当前输入框给挡住
     */
    $('input').on('click', function () {
        let target = this;
        setTimeout(function(){
            target.scrollIntoViewIfNeeded();
        },400);
    });


});

/**
 * 返回上一页
 */
function goBack() {
    window.history.back();
}