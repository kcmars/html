/**
 * Created by zp on 2018/12/20.
 */
var url1 = "/Wap/DVip/marketUser", // 推广用户查询接口
    url2 = "/Wap/DVip/agentMoneyDetail", // 推广奖励查询接口
    url3 = "/Wap/DVip/viewPhone", // 推广奖励查询接口
    url4 = "/Wap/DVip/viewRewardDetail",
    city = "",
    pos = 0,//用户or奖励
    page = 0,//分页
    lookNum1 = 4,//用户列表查看手机号次数
    lookNum2 = 4,//奖励列表查看手机号次数
    phone = null ; //查询的手机号
$(function () {
    getData1(city,page,phone);
    $(".navTab .left").bind("click",function () {
        pos = 0;
        page = 0;
        city = $('#select').val();
        $('.searchNum').val('');
        getData1(city,page,phone);
        $(this).addClass("active").siblings().removeClass("active");
        $(".userBox").css("display","block").siblings(".rewardBox").css("display","none");
    });
    $(".navTab .right").bind("click",function () {
        pos = 1;
        page = 0;
        city = $('#select').val();
        $('.searchNum').val('');
        getData2(city,page,phone);
        $(this).addClass("active").siblings().removeClass("active");
        $(".rewardBox").css("display","block").siblings(".userBox").css("display","none");
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
    //输入手机号查询
    $(".searchIcon").bind("click",function () {
        page = 0;
        var list1 = document.getElementById('userList');
        list1.innerHTML = '';
        var list2 = document.getElementById('rewardList');
        list2.innerHTML = '';
        var numPhone = $('.searchNum').val().trim();
        if(pos == 0){
            getData1(city,page,numPhone);
        }else if(pos == 1){
            getData2(city,page,numPhone);
        }
    });
    $(".searchNum").on('input propertychange',function(){
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
});
//查看电话信息
function lookUserPhone(id ,type) {
    if((type==1&&lookNum1>0)||(type==2&&lookNum2>0)){
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
                    location.href = '/Wap/DVip/login';
                }else if(res.status == 1){
                    if(type==1){
                        $('.puBox1').css('display','block');
                        $('.puBox1 .pu .info .orange').text(res.phone);
                        $('.puBox1 .pu .small b').text(res.remainTimes+'次');
                        lookNum1 = res.remainTimes;
                        if(lookNum1==0){
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
            content: '每月共有4次查看电话<br />本月查看电话次数已用完'
            ,btn: '确定'
        });
    }
}
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
                location.href = '/Wap/DVip/login';
            }else if(res.status == 1){
                lookNum2 = res.remainTimes;//存储查看次数
                var data = res.data ;
                if(lookNum2>0){
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
                location.href = '/Wap/DVip/login';
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
                location.href = '/Wap/DVip/login';
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
        },
        error:function (response) {
            console.log(response) ;
        }
    })
}

