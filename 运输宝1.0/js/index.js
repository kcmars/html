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
});

function getParams() {
    $("ul li").bind("click", function () {
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