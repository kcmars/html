/**
 * Created by Doris on 2017/8/5.
 */

var  url1 = "/Aiyunbao/DVip/marketUser",  // 推广用户查询接口
    url2 = "/Aiyunbao/DVip/agentMoneyDetail",  // 推广奖励查询接口
    url3 = "/Aiyunbao/DVip/viewPhone",  // 查看手机号
    url4 = "/Aiyunbao/DVip/viewRewardDetail", // 查看奖励明细接口
    city = "",
    pos = 0, //用户or奖励
    page = 0, //分页
    lookNum1 = 4, //用户列表查看手机号次数
    lookNum2 = 4, //奖励列表查看手机号次数
    phone = null ; //查询的手机号
    state = -1;  //搜索框的伸缩状态 -1表示隐藏，1表示弹出

// 监听浏览器可视区域放大缩小，实时获取浏览器宽度
var winWidth = $(window).width();
$(window).resize(function() {
    winWidth = $(window).width();
});
//加载更多
function loadMore() {
    $('.moreBtn').css("display","inline-block").addClass('loading');
    $('.msg').css("display","none");
    city = $('#select').val();
    setTimeout(function () {
        if(pos == 0){
            getData1(city,page,phone);
        }else if(pos == 1){
            getData2(city,page,phone);
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
    $(".vip .searchInputBox .tel").val("");
    setTimeout(function () {
        try{
            $(".searchInputBox input").focus();
        } catch(e){}
    },800);
    state = 1;
}
$(function () {
    getData1(city,page,phone);
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
$(".navTab .left").bind("click",function () {
    pos = 0;
    page = 0;
    city = $('#select').val();
    $('.searchBtn .tel').val('');
    getData1(city,page,phone);
    $(this).addClass("active").siblings().removeClass("active");
    $(".userBox").css("display","block").siblings(".rewardBox").css("display","none");
});

$(".navTab .right").bind("click",function () {
    pos = 1;
    page = 0;
    city = $('#select').val();
    $('.searchBtn .tel').val('');
    getData2(city,page,phone);
    $(this).addClass("active").siblings().removeClass("active");
    $(".rewardBox").css("display","block").siblings(".userBox").css("display","none");
});

//获取推广用户列表
function getData1(city,p,phone) {
    $.ajax({
        type: "POST",
        url: url1,
        data: {
            market_id:city,
            phone:phone,
            page:p
        },
        dataType: "json",
        success: function (res) {
            if(res.status == -1){
                location.href = '/Home/Website/vip';
            }else if(res.status == 1){
                lookNum1 = res.remainTimes;//存储查看次数
                var data = res.market_user;
                for(var i in data ){
                    data[i].lookNum1 = lookNum1;
                }
                if(p==0){
                    var compiler = doT.template(document.getElementById('list-tmpl1').innerHTML);
                    var list = document.getElementById('userList');
                    list.innerHTML = '';
                    list.innerHTML = compiler(data);
                    if(res.city_list){
                        $('#select').empty();
                        for(var i in res.city_list){
                            $('#select').append('<option name="'+res.city_list[i].city_name+'" value="'+res.city_list[i].market_id+'">'+res.city_list[i].city_name+'</option>');
                        }
                    }
                } else if (p > 0) {
                    var compiler = doT.template(document.getElementById('list-tmpl1').innerHTML);
                    $('#userList').append(compiler(data));
                }
                if(data.length<10){
                    $('.moreBtn').css("display","none").removeClass('loading');
                    $('.msg').css("display","block");
                    $('.msg').text('没有更多了');
                }else{
                    $('.moreBtn').css("display","inline-block").removeClass('loading');
                    $('.msg').css("display","none");
                }
            }else{
                $('.moreBtn').css("display","none").removeClass('loading');
                $('.msg').css("display","block");
                $('.msg').text(res.msg);
            }
            page++;
            //res.city_list
        }
    })
}

//获取推广奖励列表
function getData2(city,p,phone) {
    $.ajax({
        type: "POST",
        url: url2,
        data: {
            market_id:city,
            phone:phone,
            page:p
        },
        dataType: "json",
        success: function (res) {
            if(res.status == -1){
                location.href = '/Home/Website/vip';
            }else if(res.status == 1){
                var data = res.data;
                if(p==0){
                    var compiler = doT.template(document.getElementById('list-tmpl2').innerHTML);
                    var list = document.getElementById('rewardList');
                    list.innerHTML = '';
                    list.innerHTML = compiler(data);
                    if(res.city_list){
                        for(var i in res.city_list){
                            $('#select').append('<option name="'+res.city_list[i].city_name+'" value="'+res.city_list[i].market_id+'">'+res.city_list[i].city_name+'</option>');
                        }
                    }
                } else if (p > 0) {
                    var compiler = doT.template(document.getElementById('list-tmpl2').innerHTML);
                    $('#rewardList').append(compiler(data));
                }
                if(data.length<10){
                    $('.moreBtn').css("display","none").removeClass('loading');
                    $('.msg').css("display","block");
                    $('.msg').text('没有更多了');
                }else{
                    $('.moreBtn').css("display","inline-block").removeClass('loading');
                    $('.msg').css("display","none");
                }
            }else{
                $('.moreBtn').css("display","none").removeClass('loading');
                $('.msg').css("display","block");
                $('.msg').text(res.msg);
            }
            if(res.agentMoney){
                $('#totalMoney .left span').text(res.agentMoney.agent_money+'元');
                $('#totalMoney .middle span').text(res.agentMoney.used_money+'元');
                $('#totalMoney .right span').text(res.agentMoney.remain_money+'元');
            }
            page++;
        }
    })
}
//输入手机号查询
$("#searchImg").bind("click",function (event) {
    event.stopPropagation();
    searchHide();
    page = 0;
    var list1 = document.getElementById('userList');
    list1.innerHTML = '';
    var list2 = document.getElementById('rewardList');
    list2.innerHTML = '';
    var numPhone = $('.searchBtn .tel').val().trim();
    if(pos == 0){
        getData1(city,page,numPhone);
    }else if(pos == 1){
        getData2(city,page,numPhone);
    }
});
$(".searchBtn .tel").click(function (event) {
    event.stopPropagation();
})
// 监听输入框
$(".searchBtn .tel").on('input propertychange',function(){
    if($(this).val()==''){
        page = 0;
        var list1 = document.getElementById('userList');
        list1.innerHTML = '';
        var list2 = document.getElementById('rewardList');
        list2.innerHTML = '';
        if(pos == 0){
            getData1(city,page,phone);
        }else if(pos == 1){
            getData2(city,page,phone);
        }
    }
});
// 切换tabBtn时，隐藏输入框
$(".wrapperTab .tabItem").not(".searchBtn").click(function () {
    searchHide();
});

//查看奖励信息
function lookRawrdInfo(id,type) {
    $.ajax({
        type: "POST",
        url: url4,
        data: {
            detail_id:id,
            type:type
        },
        dataType: "json",
        success: function (res) {
            if(res.status == -1){
                location.href = '/Home/Website/vip';
            }else if(res.status == 1){
                lookNum2 = res.remainTimes;//存储查看次数
                var data = res.data ;
                if(lookNum2 > 0){
                    $('.numDetail').removeClass("notSearch");
                }else{
                    $('.numDetail').addClass("notSearch");
                }
                $('.puBox2').css('display','block');
                $('.puBox2 .pu .infoContent .tel').text(data.phone);
                $('.puBox2 .pu .textCenter b').text(res.remainTimes+'次');
                $('.puBox2 .pu .infoContent .rewardTime').text(data.add_time);
                $('.puBox2 .pu .infoContent .remark').text(data.award_remark);
                $('.puBox2 .pu .infoContent .userid').text(data.user_id);
            }else{
            }
        }
    })
}
//查看电话信息
function lookUserPhone(id ,type) {
    if((type == 1 && lookNum1 > 0)||(type == 2 && lookNum2 > 0)){
        if(type==2){
            id = $('.userid').text();
        }
        $.ajax({
            type: "POST",
            url: url3,
            data: {
                user_id:id,
                type:type
            },
            dataType: "json",
            success: function (res) {
                if(res.status == -1){
                    location.href = '/Home/Website/vip';
                }else if(res.status == 1){
                    if(type==1){
                        $('.puBox1').css('display','block');
                        $('.puBox1 .pu .info .tel').text(res.phone);
                        $('.puBox1 .pu .small b').text(res.remainTimes+'次');
                        lookNum1 = res.remainTimes;
                        if(lookNum1 == 0){
                            $('#userList li').addClass('notSee');
                        }
                    }else{
                        $('.puBox1').css('display','none');
                        $('.puBox2 .pu .infoContent .tel').text(res.phone);
                        $('.puBox2 .pu .textCenter b').text(res.remainTimes+'次');
                        lookNum2 = res.remainTimes;
                        if(lookNum2>0){
                            $('.numDetail').removeClass("notSearch");
                        }else{
                            $('.numDetail').addClass("notSearch");
                        }
                    }
                }else{
                }
            }
        })
    }else{
        layer.open({
            content: '您的当月查看次数已用完'
            ,btn: '确定'
        });
        return;
    }
}

//选择地址
function cityRank() {
    city = $('#select').val();
    var list1 = document.getElementById('userList');
    list1.innerHTML = '';
    var list2 = document.getElementById('rewardList');
    list2.innerHTML = '';
    page = 0;
    if(pos == 0){
        getData1(city,page,phone);
    }else if(pos == 1){
        getData2(city,page,phone);
    }
}

// 退出登陆
$('.outBtn').click(function () {
    $.post("/Aiyunbao/DVip/del",function(data) {
        if (data.status == 1) {
            window.location.href = "/Home/Website/vip.html";
        }else {
            layer.open({
                content: data.msg
                , btn: '确定'
            })
        }
    })
})
