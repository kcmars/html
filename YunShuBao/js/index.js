/**
 * Created by Administrator on 2018/10/6.
 */
$(function () {
    $.ajax({
        type:"get",
        url:"home.html",
        async:true,
        success:function(data){
            $("#content").empty();
            $("#content").html(data);
        }
    });
    // getRequest(getParams);
    getParams();

    /**
     * 切换身份
     */
    $("#change").bind("click", function () {
        $(".change-box").animate({top: 0}, 300);
        $(".change-box .model").bind("click", function () {
            $(".change-box").animate({top: "-100%"}, 300);
            $(".change-box .model, .change-box .box li").unbind("click");
        });
        $(".change-box .box li").bind("click", function () {
            $(".change-box").animate({top: "-100%"}, 300);
            $(".change-box .model, .change-box .box li").unbind("click");
            let index = $(this).index();
            if (index == 0) {
                $("#change span").text("我要发货");
                sessionStorage.setItem("identity", "goods");
            } else if (index == 1) {
                $("#change span").text("我要拉货");
                sessionStorage.setItem("identity", "driver");
            }
        })
    });
    /**
     * 底部导航栏
     */
    $(".footer li").bind("click", function () {
        $(this).addClass("text-color").siblings().removeClass("text-color");
        switch ($(this).index()) {
            case 0:
                $.ajax({
                    type:"get",
                    url:"home.html",
                    async:true,
                    success:function(data){
                        $("#content").empty();
                        $("#content").html(data);
                    }
                });
                $("#change").removeClass("none").addClass("change");
                break;
            case 1:
                $.ajax({
                    type:"get",
                    url:"source.html",
                    async:true,
                    success:function(data){
                        $("#content").empty();
                        $("#content").html(data);
                    }
                });
                $("#change").removeClass("change").addClass("none");
                break;
            case 2:
                $.ajax({
                    type:"get",
                    url:"more.html",
                    async:true,
                    success:function(data){
                        $("#content").empty();
                        $("#content").html(data);
                    }
                });
                $("#change").removeClass("change").addClass("none");
                break;
            case 3:
                $.ajax({
                    type:"get",
                    url:"me.html",
                    async:true,
                    success:function(data){
                        $("#content").empty();
                        $("#content").html(data);
                    }
                });
                $("#change").removeClass("change").addClass("none");
                break;
        }
    });
    /**
     * 发货、发车按钮触摸移动
     */
    $("#move-button").on("touchmove", function (event) {
        let mX = event.originalEvent.targetTouches[0].clientX;
        let mY = event.originalEvent.targetTouches[0].clientY;
        if (mX - 30 > 0 && mY - 30 > 0 && mX < window.screen.width - 30 && mY < window.screen.height - 30) {
            $("#move-button").animate({top: mY - 30, left: mX - 30}, 0);
        }
    });
});

function getParams() {

}