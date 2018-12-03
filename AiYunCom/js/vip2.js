/**
 * Created by Doris on 2017/8/5.
 */

var url1 = "/Aiyunbao/Vip/userRecom",       // vip 推广用户查询接口
    url2 = "/Aiyunbao/Vip/userBenefits",    // 推广收益查询接口
    url3 = "/Aiyunbao/Vip/viewPhone",       // 查看手机号
    url4 = "/Aiyunbao/Vip/profitDetail",    // 查看vip收益详细细接口
    url5 = "/Aiyunbao/Vip/jurisdiction",    // 查询是否有发布广告的权限或者查询一级推荐用户是否达到20人
    url6 = "/Aiyunbao/Vip/advertisement",   // 发布广告接口
    level = 1,       //查询等级
    pos = 0,         //用户or收益
    page = 0,        //分页
    lookNum1 = 4,   //用户列表查看手机号次数
    lookNum2 = 4,   //收益列表查看手机号次数
    phone = null,   //查询的手机号
    state = -1 ;    //搜索框的伸缩状态
// 监听浏览器可视区域放大缩小，实时获取浏览器宽度
level = 1 ;

var winWidth = $(window).width();
$(window).resize(function() {
    winWidth = $(window).width();
});
//加载更多
function loadMore() {
    $('.moreBtn').css("display","inline-block").addClass('loading');
    $('.msg').css("display","none");
    setTimeout(function () {
        if(pos == 0){
            getData1(page,phone);
        }else if(pos == 1){
            getData2(page,phone);
        }
    },300);
}

// 隐藏/弹出搜索框
function searchHide() {
    if(winWidth >= 769){
        $("#vipData2 .searchInputBox").animate({width:"0"},function () {
            $("#vipData2 .searchInputBox").css("z-index","-1")
        });
    }else if(winWidth < 769){
        $("#vipData2 .searchInputBox").fadeOut().css("z-index","-1");
    }
    state = -1;
}

function searchShow() {
    if(parseInt(winWidth) >= 769){
        $("#vipData2 .searchInputBox").css({"width":"0","z-index":"5","display":"block"}).animate({width:"350px"});
    }else if(winWidth < 769){
        $("#vipData2 .searchInputBox").css("width","400%").fadeIn();
    }
    $("#vipData2 .searchInputBox .tel").val("");
    setTimeout(function () {
        try{
            $(".searchInputBox input").focus();
        } catch(e){}
    },800);
    state = 1;
}

$(function () {
    getData1(page,phone);
    //滑动list内容时阻止冒泡，用了scrollLock插件
    document.getElementById("vipData2").style.height = window.innerHeight + 'px';
    $(".vip .listBox").scrollLock();
    $(".vip .proContent").scrollLock();

    $(".listBox li:not(.notSee) .detailBtn").mouseenter(function () {
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
    // 在数据页点号码查询弹出输入框
    $("#vipData2 .searchBtn").click(function () {
       state = -state;
       if (state === -1) {
           searchHide();
       } else if (state === 1) {
           searchShow();
       }
    });
    // 点击contentWrapper区域隐藏输入框
    $("#vipData2 .wrapperContent").click(function(event){
        event.stopPropagation();
        searchHide();
    });
});

// 切换推广查询Tab
$("#vipData2 .wrapperTab .proSearchBtn").click(function () {
    $(".wrapperContent .proSearchContent").show().siblings(".wrapperContent .proContent").hide();
    searchHide();
});
// 切换查询的推荐用户级数
$(".dataBoxInner .navSubTab .tabList").click(function () {
    level = $(this).index()+1;
    if (level == 1 ) {
        $(".userBox .level1Title").show().siblings(".otherTitle").hide();
    } else if (level !== 1 ) {
        $(".userBox .level1Title").hide().siblings(".otherTitle").show();
    }
    $(this).addClass("active").siblings().removeClass("active");
    // 执行对应级数推荐用户查询
    page = 0;
    if (pos === 0) {
        getData1(page,phone);
    } else if (pos === 1){
        getData2(page,phone);
    }
});

if(undefined){
    console.log(1212112121)
}

//获取推广用户列表
function getData1(p,phone) {
    $.ajax({
        type: "POST",
        url: url1,
        data: {
            grade:level,
            phone:phone,
            page:p
        },
        dataType: "json",
        success: function (res) {
            if(res.status == -1){
                location.href = '/Home/Website/vip';
            }else if(res.status == 1){
               var userLevel = res.userLevel;
                if(userLevel){
                    level = userLevel;
                    if (userLevel == 1 ) {
                        $(".userBox .level1Title").show().siblings(".otherTitle").hide();
                    } else if (userLevel !== 1 ) {
                        $(".userBox .level1Title").hide().siblings(".otherTitle").show();
                    }
                    $(".vipData .dataBoxInner .navSubTab .tabList").eq(userLevel-1).addClass("active").siblings().removeClass("active");
                }
                lookNum1 = res.remainTimes;//存储查看次数
                var data = res.data;
                console.log(data)
                $("#vipData2 .totalProfit").show();
                $("#total1").text( res.count_user +'人');
                for(var i in data ){
                    data[i].lookNum1 = lookNum1;
                }
                if (res.info){
                    $('#vipData2 .navTab .number').text(res.info.users+'人');
                    $('#vipData2 .navTab .profit').text(res.info.total_money+'元');
                }
                if (level == 1) {
                    var compiler = doT.template(document.getElementById('list-tmpl0').innerHTML);
                } else if (level !==1) {
                    compiler = '';
                    compiler = doT.template(document.getElementById('list-tmpl1').innerHTML);
                }
                if(p == 0 ){
                    var list = document.getElementById('userList');
                    list.innerHTML = '';
                    list.innerHTML = compiler(data);
                } else if (p > 0) {
                    $('#userList').append(compiler(data));
                }
                if(data.length < 10){
                    $('.moreBtn').css("display","none").removeClass('loading');
                    $('.msg').css("display","block");
                    $('.msg').text('没有更多了');
                }else {
                    $('.moreBtn').css("display", "inline-block").removeClass('loading');
                    $('.msg').css("display", "none");
                }
            }else{

                $("#total1").text( '0人');
                var list = document.getElementById('userList');
                list.innerHTML = '';
                $('.moreBtn').css("display","none").removeClass('loading');
                $('.msg').css("display","block");
                $('.msg').text(res.msg);
            }
            page++;
        }
    })
}

//获取推广奖励列表
function getData2(p,phone) {
    $.ajax({
        type: "POST",
        url: url2,
        data: {
            grade:level,
            phone:phone,
            page:p
        },
        dataType: "json",
        success: function (res) {
            if(res.status == -1){
                location.href = '/Home/Website/vip';
            }else if(res.status == 1){
                var userLevel = res.userLevel;
                if(userLevel){
                    level = userLevel ;
                    if (userLevel == 1 ) {
                        $(".userBox .level1Title").show().siblings(".otherTitle").hide();
                    } else if (userLevel !== 1 ) {
                        $(".userBox .level1Title").hide().siblings(".otherTitle").show();
                    }
                    $(".vipData .dataBoxInner .navSubTab .tabList").eq(userLevel-1).addClass("active").siblings().removeClass("active");
                }

                $("#total2").text(res.count_money+'元');
                var data = res.data;
                var compiler = doT.template(document.getElementById('list-tmpl2').innerHTML);
                if(p == 0){
                    var list = document.getElementById('rewardList');
                    list.innerHTML = '';
                    list.innerHTML = compiler(data);
                } else if (p > 0) {
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
                $("#total2").text('0元');
                var list = document.getElementById('rewardList');
                list.innerHTML = '';
                $('.moreBtn').css("display","none").removeClass('loading');
                $('.msg').css("display","block");
                $('.msg').text(res.msg);
            }
            page++;
        }
    })
}

//查看收益详细信息
function lookRawrdInfo(id,type) {
    $.ajax({
        type: "POST",
        url: url4,
        data: {
            user_id:id,
            type:type,
            level:level
        },
        dataType: "json",
        success: function (res) {
            if(res.status == -1){
                location.href = '/Home/Website/vip';
            }else if(res.status == 1){
                lookNum2 = res.remainTimes;//存储查看次数
                var data = res.data ;
                if( level == 1){
                    $('.puBox2 .infoContent .infoContentItem .numDetail ').hide();
                }else {
                    $('.puBox2 .infoContent .infoContentItem .numDetail ').show();
                }
                if(lookNum2 > 0){
                    $('.numDetail').removeClass("notSearch");
                }else{
                    $('.numDetail').addClass("notSearch");
                }
                $('.puBox2').css('display','block');
                $('.puBox2 .pu .infoContent .tel').text(data.phone);
                $('.puBox2 .pu .infoContent .remark').text(data.call_time +'日从'+data.start_name +'到'+ data.end_name +'获得'+ data.content);
                $('.puBox2 .pu .infoContent .profitDetail').text(data.trans_money+'元');
                $('.puBox2 .pu .remainTimes').text(res.remainTimes+'次');
                $('.puBox2 .pu .infoContent .userid').text(data.user_id);
            }else {
                layer.open({
                    content: res.msg
                    ,btn: '确定'
                });
            }
        }
    })
}

//查看手机号
function lookUserPhone(id ,type) {
    if((type == 3 && lookNum1 > 0)||(type == 4 && lookNum2 > 0)){
        if(type == 4){
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
                    if(type==3){
                        $('.puBox1').css('display','block');
                        $('.puBox1 .pu .info .tel').text(res.phone);
                        $('.puBox1 .pu .remainTimes').text(res.remainTimes+'次');
                        lookNum1 = res.remainTimes;
                        if(lookNum1 == 0){
                            $('#userList li').addClass('notSee');
                        }
                    }else{
                        $('.puBox1').css('display','none');
                        $('.puBox2 .pu .infoContent .tel').text(res.phone);
                        $('.puBox2 .pu .remainTimes').text(res.remainTimes+'次');
                        lookNum2 = res.remainTimes;
                        if(lookNum2>0){
                            $('.numDetail').removeClass("notSearch");
                        }else{
                            $('.numDetail').addClass("notSearch");
                        }
                    }
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

// 在推广用户和推广收益间切换
$(".navTab .left").bind("click",function () {
    pos = 0;
    page = 0;
    $('.searchBtn .tel').val('');
    getData1(page,phone);
    $(this).addClass("active").siblings().removeClass("active");
    $(".userBox").css("display","block").siblings(".rewardBox").css("display","none");
});

$(".navTab .right").bind("click",function () {
    pos = 1;
    page = 0;
    $('.searchBtn .tel').val('');
    getData2(page,phone);
    $(this).addClass("active").siblings().removeClass("active");
    $(".rewardBox").css("display","block").siblings(".userBox").css("display","none");
});

// 监听输入框
$(".searchBtn .tel").on('input propertychange',function(){
    if($(this).val()==''){
        page = 0;
        var list1 = document.getElementById('userList');
        list1.innerHTML = '';
        var list2 = document.getElementById('rewardList');
        list2.innerHTML = '';
        if(pos == 0){
            getData1(page,phone);
        }else if(pos == 1){
            getData2(page,phone);
        }
    }
});

//输入手机号查询
function searchPhone(phone) {
    if(pos == 0){
        getData1(page,phone);
    }else if(pos == 1){
        getData2(page,phone);
    }
}

$(".searchBtn .tel").click(function (event) {
    event.stopPropagation();
});

//输入手机号查询
$("#searchImg").bind("click",function (event) {
    event.stopPropagation(); //阻止冒泡
    searchHide(); //隐藏搜索框
    page = 0;
    var numPhone = $('.searchBtn .tel').val().trim();
    var reg = /^1(3|4|5|7|8)[0-9]\d{8}$/;
    if (numPhone) {
        if(!reg.test(numPhone)) {
            return;
        } else {
            searchPhone(numPhone);
        }
    }
});

// 切换发布广告Tab
$("#vipData2 .wrapperTab .adBtn").click(function () {
    searchHide();
    $.ajax({
        type: "POST",
        url: url5,
        data: {
            phone: phone
        },
        dataType: "json",
        success: function (res) {
            if (res.status == 0) {
                layer.open({
                    content: res.msg,
                    btn: '确定'
                })
            } else if (res.status == 1) {
                $(".wrapperContent .proSearchContent").hide().siblings(".wrapperContent .proContent").show();
            }
        }
    })
});

// 弹出广告细则弹窗
$("#vipData2 .requirements").click(function () {
    layer.open({
        content: '1.广告内容符合国家法律法规的相关规定<br>2.广告内容不会损害甲方的利益<br>3.广告内容健康、规范',
        btn: '我知道了'
    })
});

// 发布广告
$("#vipData2 #adSubmitBtn").on('click',function () {
    var title = $("#proTitle").val().trim(),
        content = $("#proText").val().trim(),
        obj_type = [];
    $("input[name='adTo']:checked").each(function () {
        obj_type.push($(this).val());
    });

    var str_obj = obj_type.join(",");

    if (!title) {
        layer.open({
            content: '请输入广告标题',
            btn: '我知道了'
        });
        return;
    }
    if (!content) {
        layer.open({
            content: '请输入广告内容',
            btn: '我知道了'
        });
        return;
    }
    if (!str_obj) {
        layer.open({
            content: '请选择发送广告的对象',
            btn: '我知道了'
        });
        return;
    }

    if (title && content && str_obj){
        $.ajax({
            type: "POST",
            url: url6,
            data: {
                title: title,
                content: content,
                user: str_obj
            },
            dataType: "json",
            success: function (res) {
                if(res.status == -1){
                    location.href = '/Home/Website/vip';
                }else if(res.status == 1){
                    layer.open({
                        content:res.msg ,
                        btn: '我知道了'
                    });
                    //   成功后清除状态
                    $("#proTitle").val("");
                    $("#proText").val("");
                    $("#addContent .tipText").text('0/200字');
                    str_obj = '';
                    $("input[name='adTo']").attr("checked",false)
                } else {
                    layer.open({
                        content: res.msg,
                        btn: '我知道了'
                    })
                }
            }
        })
    }
});

// 建议输入框字数显示，达到上限后禁止输入
function checkLength(obj,maxlen) {
    var  getMyLen,MAXLEN;
    MAXLEN = maxlen;
    getMyLen = obj.value.length;
    if(getMyLen>MAXLEN) {
        $(obj).siblings('.tipText').text('最多可输入'+MAXLEN+'字');
        obj.value = obj.value.substr(0,MAXLEN);
    } else {
        $(obj).siblings('.tipText').text(''+getMyLen+'\/'+MAXLEN+'字');
    }
}


// 退出登陆
$('.outBtn').click(function () {
    searchHide();
    $.post("/Aiyunbao/Vip/del",function(data) {
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

