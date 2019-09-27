/**
 * Created by zp on 2018/10/6.
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
});

function getParams() {
    //身份切换
    let roleFlag = true;
    $(".head-left").bind("click", function () {
        if (roleFlag) {
            roleFlag = false;
            if ($.role == 1) {
                $(".role").text("司机");
            } else {
                $(".role").text("货主");
            }
            $(".role-box").removeClass("none");
        } else {
            roleFlag = true;
            $(".role-box").addClass("none");
        }
    });
    //关闭身份选择下的model
    $(".model").bind("touchstart", function () {
        roleFlag = true;
        $(".role-box").addClass("none");
    });
    $(".role").bind("click", function () {
        roleFlag = true;
        if ($.role == 1) {
            $.role = 2;
            $(".head-left span").text("司机");
        } else {
            $.role = 1;
            $(".head-left span").text("货主");
        }
        $(".role-box").addClass("none");
    });
    //消息
    $(".head-right").bind("click", function () {
        $(".role-box").addClass("none");
        window.location.href = "../html/message.html";
    });
    //点击运业务
    $("#btn-yun").bind("click", function () {
       if ($.role == 1) {
            window.location.href = "../html/goods.html";
       } else {
            window.location.href = "../html/business.html";
       }
    });
    $(".footer li").bind("click", function () {
        roleFlag = true;
        $(".role-box").addClass("none");
        $(this).addClass("active").siblings().removeClass("active");
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
                break;
        }
    });
}