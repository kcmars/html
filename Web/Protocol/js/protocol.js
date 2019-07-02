/**
 * Created by keyC on 2019/5/28.
 */
$(function () {

    $('.toTop').hide();        //隐藏go to top按钮

    /**
     * 监听页面滚动事件
     */
    $(document).scroll(function() {
        //当window的scrolltop距离大于1时，go to
        var scroH = $(document).scrollTop();  //滚动高度
        var viewH = $(window).height();  //可见高度
        var contentH = $(document).height();  //内容高度
        if(scroH > 100){
            $('.toTop').fadeIn();
        }else{
            $('.toTop').fadeOut();
        }
    });

    /**
     * 绑定回到顶部事件
     */
    $(".toTop").bind("click", function () {
        $('html ,body').animate({scrollTop: 0}, 300);
        return false;
    });
});