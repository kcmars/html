$(function () {
    //防止键盘把当前输入框给挡住
    $('input').on('click', function () {
        var target = this;
        setTimeout(function(){
            target.scrollIntoViewIfNeeded();
        },400);
    });
});
