/**
 * Created by zp on 2018/10/17.
 */

var  url1 = "/Aiyunbao/RepeatCar/carRecord",  // 获取数据
    url2 = "/Aiyunbao/RepeatCar/carDetail",  // 单条信息
    url3 = "/Aiyunbao/RepeatCar/del";  // 退出
var  mPage = 0; //分页
var  mPlate = "" ; //查询的车牌号
var  state = -1;  //搜索框的伸缩状态 -1表示隐藏，1表示弹出
var  mType = 1; //1 成功数据, 2 重复数据
// 监听浏览器可视区域放大缩小，实时获取浏览器宽度
var winWidth = $(window).width();
$(window).resize(function() {
    winWidth = $(window).width();
});
//加载更多
function loadMore() {
    $('.moreBtn').css("display","inline-block").addClass('loading');
    $('.msg').css("display","none");
    setTimeout(function () {
        mPage++;
        if(mType == 1){
            getData1(mType, mPage, mPlate);
        }else if(mType == 2){
            getData2(mType, mPage, mPlate);
        }
    },300);
}

// 隐藏/弹出搜索框
function searchHide() {
    if(winWidth >= 769){
        $(".searchInputBox").animate({width:"0"},function () {
            $(".searchInputBox").css("z-index","-1")
        });
    }else if(winWidth < 769){
        $(".searchInputBox").fadeOut().css("z-index","-1");
    }
    state = -1;
}
function searchShow() {
    if(parseInt(winWidth) >= 769){
        $(".searchInputBox").css({"width":"0","z-index":"5","display":"block"}).animate({width:"350px"});
    }else if(winWidth < 769){

        $(".searchInputBox").css("width","300%").fadeIn();
    }
    $(".vip .searchInputBox .plate").val("");
    setTimeout(function () {
        try{
            $(".searchInputBox input").focus();
        } catch(e){}
    },800);
    state = 1;
}
$(function () {
    getData1(mType, mPage, mPlate);
    //滑动list内容时阻止冒泡，用了scrollLock插件
    $(".vip .listBox").scrollLock();

    $(".listBox .detailBtn").mouseenter(function () {
        $(this).css("background","#C3C4C5");
    }).mouseleave(function () {
        $(this).css("background","#D9DADB");
    });
    $(".userBox #userList .detailBtn").bind("click",function () {
        $(".puBox1").css("display","block").siblings(".puBox2").css("display","none");

    });
    $(".rewardBox #rewardList .detailBtn").bind("click",function () {
        $(".puBox2").css("display","block").siblings(".puBox1").css("display","none");

    });

    $(".puBox .close").bind("click",function () {
        $(".puBox").css("display","none");
    });
    // 在数据页点查询弹出输入框
    $(".searchBtn").click(function () {
        state = -state;
        if (state === -1) {
            searchHide();
        } else if (state === 1) {
            searchShow();
        }
    });
    // 点击contentWrapper区域隐藏输入框
    $(".wrapperContent").click(function(event){
        event.stopPropagation();
        searchHide();
    });

});
$(".navTabBox .left_btn").bind("click",function () {
    mType = 1;
    mPage = 0;
    mPlate = "";
    $('.searchBtn .plate').val('');
    getData1(mType, mPage, mPlate);
    $(this).addClass("active").siblings().removeClass("active");
    $(".userBox").css("display","block").siblings(".rewardBox").css("display","none");
});

$(".navTabBox .right_btn").bind("click",function () {
    mType = 2;
    mPage = 0;
    mPlate = "";
    $('.searchBtn .plate').val('');
    getData2(mType, mPage, mPlate);
    $(this).addClass("active").siblings().removeClass("active");
    $(".rewardBox").css("display","block").siblings(".userBox").css("display","none");
});

//获取车辆推荐成功列表
function getData1(mType, mPage, mPlate) {
    $.ajax({
        type: "POST",
        url: url1,
        data: {
            type: mType,
            page: mPage,
            plate: mPlate
        },
        dataType: "json",
        success: function (res) {
            console.log("res==", mType + "---" +  mPage + "---" + mPlate);
            console.log("res==", res);
            if(res.status == -1){
                location.href = '/Home/Website/vip';
            }else if(res.status == 1){
                let data = res.list.list;
                if(data !== null && data.length > 0){
                    if(mPage == 0){
                        var compiler = doT.template(document.getElementById('list-tmpl1').innerHTML);
                        var list = document.getElementById('userList');
                        list.innerHTML = '';
                        list.innerHTML = compiler(data);
                    } else {
                        var compiler = doT.template(document.getElementById('list-tmpl1').innerHTML);
                        $('#userList').append(compiler(data));
                    }
                    $('.userList_num').text(res.list.con + "辆");
                    $('.rewardList_num').text(res.list.reCon + "辆");
                    if(data.length<10){
                        $('.moreBtn').css("display","none").removeClass('loading');
                        $('.msg').css("display","block");
                        $('.msg').text('没有更多了');
                    }else{
                        $('.moreBtn').css("display","inline-block").removeClass('loading');
                        $('.msg').css("display","none");
                    }
                } else {
                    $('.moreBtn').css("display","none").removeClass('loading');
                    $('.msg').css("display","block");
                    $('.msg').text('没有更多了');
                }
            }else{
                $('.moreBtn').css("display","none").removeClass('loading');
                $('.msg').css("display","block");
                $('.msg').text(res.msg);
            }
        }
    })
}

//获取车辆推荐重复列表
function getData2(mType, mPage, mPlate) {
    $.ajax({
        type: "POST",
        url: url1,
        data: {
            type: mType,
            page: mPage,
            plate: mPlate
        },
        dataType: "json",
        success: function (res) {
            console.log("res===", mType + "---" +  mPage + "---" + mPlate);
            console.log("res===", res);
            if(res.status == -1){
                location.href = '/Home/Website/vip';
            }else if(res.status == 1){
                let data = res.list.list;
                if(data !== null && data.length > 0){
                    if (mPage == 0) {
                        var compiler = doT.template(document.getElementById('list-tmpl2').innerHTML);
                        var list = document.getElementById('rewardList');
                        list.innerHTML = '';
                        list.innerHTML = compiler(data);
                    } else {
                        var compiler = doT.template(document.getElementById('list-tmpl2').innerHTML);
                        $('#rewardList').append(compiler(data));
                    }
                    $('.userList_num').text(res.list.con + "辆");
                    $('.rewardList_num').text(res.list.reCon + "辆");
                    if(data.length<10){
                        $('.moreBtn').css("display","none").removeClass('loading');
                        $('.msg').css("display","block");
                        $('.msg').text('没有更多了');
                    }else{
                        $('.moreBtn').css("display","inline-block").removeClass('loading');
                        $('.msg').css("display","none");
                    }
                } else {
                    $('.moreBtn').css("display","none").removeClass('loading');
                    $('.msg').css("display","block");
                    $('.msg').text('没有更多了');
                }
            }else{
                $('.moreBtn').css("display","none").removeClass('loading');
                $('.msg').css("display","block");
                $('.msg').text(res.msg);
            }
        }
    })
}
//输入车牌号查询
$("#searchImg").bind("click",function (event) {
    event.stopPropagation();
    searchHide();
    mPage = 0;
    var list1 = document.getElementById('userList');
    list1.innerHTML = '';
    var list2 = document.getElementById('rewardList');
    list2.innerHTML = '';
    mPlate = $('.searchBtn .plate').val().trim();
    if(mType == 1){
        getData1(mType, mPage, mPlate);
    }else if(mType == 2){
        getData2(mType, mPage, mPlate);
    }
});
$(".searchBtn .plate").click(function (event) {
    event.stopPropagation();
});
// 监听输入框
$(".searchBtn .plate").on('input propertychange',function(){
    if($(this).val()==''){
        mPage = 0;
        var list1 = document.getElementById('userList');
        list1.innerHTML = '';
        var list2 = document.getElementById('rewardList');
        list2.innerHTML = '';
        if(mType == 1){
            getData1(mType, mPage, mPlate);
        }else if(mType == 2){
            getData2(mType, mPage, mPlate);
        }
    }
});
// 切换tabBtn时，隐藏输入框
$(".wrapperTab .tabItem").not(".searchBtn").click(function () {
    searchHide();
});

//查看重复信息
function lookRepeatPlateInfo(id) {
    $.ajax({
        type: "POST",
        url: url2,
        data: {
            id: id,
            type: 2
        },
        dataType: "json",
        success: function (res) {
            console.log("res2==", res);
            if(res.status == -1){
                location.href = '/Home/Website/vip';
            }else if(res.status == 1){
                let data = res.info ;
                $('.puBox2').css('display','block');
                $('.puBox2 .pu .title .title_plate').text("【" + data.car + "】");
                $('.puBox2 .pu .infoContent .info_my_tel').text(data.phone);
                $('.puBox2 .pu .infoContent .info_my_time').text(data.repeat_time);
                $('.puBox2 .pu .infoContent .info_he_tel').text(data.user);
                $('.puBox2 .pu .infoContent .info_he_time').text(data.verify_time);
            }else{
                layer.open({
                    content: '暂无信息'
                    ,btn: '确定'
                });
            }
        }
    })
}

// 退出登陆
$('.outBtn').click(function () {
    $.post(url3,function(data) {
        if (data.status == 1) {
            window.location.href = "/Home/Website/vip.html";
        }else {
            layer.open({
                content: data.msg
                , btn: '确定'
            })
        }
    })
});
