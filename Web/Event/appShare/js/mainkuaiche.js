/**
 * Created by keyC on 2015/9/10.
 */
$(function () {
    getShareLinkImgCode();
    $('#captcha-container').bind("click", function () {
        getShareLinkImgCode();
    })
});

/**
 * 获取图形验证码
 */
function getShareLinkImgCode() {
    let params = {};
    console.log("getShareLinkImgCode", "getShareLinkImgCode");
    $.ajax({
        type: 'POST',
        url: $.getShareLinkImgCode,
        data: params,
        success: function (res) {
            console.log("getShareLinkImgCode=", res);
            if (res) {
                if (res.status == 1) {
                    $("#img-code").attr("src", res.data);
                }
            }
        },
        error: function (err) {
            console.log(err);
        }
    });
}

var u = navigator.userAgent;
var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; // android终端
var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); // ios终端
$("#Click_box").click(function () {
        var d = dialog({
            title: '提示',
            content: '<h3>请填写你正要注册或已<br>经注册过的手机号码：<h3><br /><input type="text" id="readytel" >',
            okValue: '确定',
            ok: function () {
                var readytel = $('#readytel').val();
                var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(16[0-9]{1})|(17[0-9]{1})|(18[0-9]{1})|(19[0-9]{1}))+\d{8})$/;
                if(readytel){
                    if(!(myreg.test(readytel))){
                        var d = dialog({
                            title: '信息提示！',
                            content: '不是有效的11位手机号码',
                            ok: function () {
                            }
                        });
                        d.showModal();
                        return false;
                    }
                }
                $.post("/index.php/AppApiV1/Member/checkPhone2",
                   'phone=' + readytel,
                   function (data) {
                        if (data.status==0) {
                            var d = dialog({
                                title: '信息提示！',
                                content: '该手机号码已注册，您可直接下载安装登录"爱云宝"APP，如忘记密码可在APP登录页面通过【忘记密码】操作找回密码',
                                ok: function () {
                                    phone();
                                }
                            });
                            d.showModal();

                        }else {
                           $('#tel').val(readytel);
                           $api.addCls($api.byId('invite_login'), 'hideleft');
                           $api.addCls($api.byId('inviteword_box'), 'showlift');
                           $api.removeCls($api.byId('invite_login'), 'showlift');
                           $api.addEvt($api.byId('login_bt'), 'click', function () {
                               $api.addCls($api.byId('invite_login'), 'hideleft');
                               $api.addCls($api.byId('inviteword_box'), 'showlift');
                               $api.removeCls($api.byId('invite_login'), 'showlift');
                           });
                           $api.addEvt($api.byId('invite_changeOth'), 'click', function () {
                               $api.removeCls($api.byId('inviteword_box'), 'showlift');
                               $api.removeCls($api.byId('invite_login'), 'hideleft');
                               $api.addCls($api.byId('invite_login'), 'showlift');
                               $api.addCls($api.byId('inviteword_box'), 'hideleft');
                           })
                        }
                    }, "json");
            },
        });
        d.showModal();
        return false;
});
// 返回顶部
$("#return_top").click(function () {
    $(window).scrodllTop(0);
    // $(window).animate({scrollTop:'0'},200);
});

// 注册和下载页面隐藏显示
$("#registered_button").click(function () {
    $("#formtag_cont").css({
        display: "none"
    });
    $("#myform").css({
        display: "block"
    })
});

$(".registered_button").click(function () {
    $("#formtag_cont").css({
        display: "none"
    });
    $("#myform").css({
        display: "block"
    })
});
$("#download_button").click(function () {
    $("#apptag_cont").css({
        display: 'none'
    });
    $(".app").css({
        display: 'block'
    })
});
$("#return_formHome").click(function () {
    $("#formtag_cont").css({
        display: "block"
    });
    $("#myform").css({
        display: "none"
    })
});
$("#return_appHome").click(function () {
    $("#apptag_cont").css({
        display: 'block'
    });
    $(".app").css({
        display: 'none'
    })
});
// 返回按钮颜色改变
$("#return_formHome").hover(function () {
    $(this).css({
        backgroundColor: "#253A5F"
    });
    $("#form_icon", $(this)).css({
        borderColor: "white"
    });
}, function () {
    $(this).css({
        backgroundColor: "transparent"
    });
    $("#form_icon", $(this)).css({
        borderColor: "#253A5F"
    });
});

$("#return_appHome").hover(function () {
    $(this).css({
        backgroundColor: "#008C24"
    });
    $("#app_icon", $(this)).css({
        borderColor: "white"
    });
}, function () {
    $(this).css({
        backgroundColor: "transparent"
    });
    $("#app_icon", $(this)).css({
        borderColor: "#008C24"
    });
});
//获取短信验证码
$("#yz").click(function () {
            var code = $('#code').val();
            var tel = $('#tel').val();
            var pwd = $("#pwd").val();
            var wenzi = document.getElementById('yz').innerHTML;
            wenzi = wenzi.substr(-6);
            if (tel==""||tel.length!=11){
                $('#tel').focus();
                var d = dialog({
                    title: '信息提示！',
                    content: '请输入有效11位手机号码',
                    ok: function () {
                    }
                });
                d.showModal();
                return false;
            }
            if (pwd==""||pwd.length<6){
                $('#pwd').focus();
                var d = dialog({
                    title: '信息提示！',
                    content: '请输入6至20位密码',
                    ok: function () {
                    }
                });
                d.showModal();
                return false;
            }
            if (code==""){
                $('#code').focus();
                var d = dialog({
                    title: '信息提示！',
                    content: '图形验证码不能为空',
                    ok: function () {
                    }
                });
                d.showModal();
                return false;
            }
            if(wenzi=="秒后重新获取"){
                return false;
            }
            $.post("/index.php/Open/Static/check_verify.html",
                {
                    'code':code,
                    'phone':tel
                },
                function (data) {
                    if (data.status == 1) {
                        if (data.msg.indexOf('已注册') > 0) {
                            var d = dialog({
                                title: '提示',
                                content: data.msg
                            });
                            d.showModal();
                            $('#captcha-container').click();
                        } else {
                            var validCode = true;
                            var time = 60;
                            var code = $(this);
                            if (validCode) {
                                validCode = false;
                                var t = setInterval(function () {
                                    $("#yz").addClass(
                                        "getCode1");
                                    time--;
                                    $("#yz").html(
                                        time + "秒后重新获取");
                                    if (time == 0) {
                                        clearInterval(t);
                                        $("#yz").html("重新获取");
                                        validCode = true;
                                        $("#yz").removeClass(
                                            "getCode1");
                                    }
                                }, 1000)
                            }
                            var d = dialog({
                                title: '信息提示！',
                                content: '短信验证码已发送',
                                ok: function () {
                                }
                            });
                            d.showModal();
                        }
                    } else {
                        var d = dialog({
                            title: '信息提示！',
                            content: data.msg,
                            ok: function () {
                            }
                        });
                        d.showModal();
                        $('#captcha-container').click();
                    }
                }, "json");
        });
// 显示隐藏div
$("#sub").click(function () {
    var tel = $('#tel').val();
    var pwd = $("#pwd").val();
    var smscode = $("#smscode").val();
    var recommend_phone = $("#recommend_phone").val();
    if (tel==""||tel.length!=11){
        $('#tel').focus();
        var d = dialog({
            title: '信息提示！',
            content: '请输入有效11位手机号码',
            ok: function () {
            }
        });
        d.showModal();
        return false;
    }
    if (pwd==""||pwd.length<6){
        $('#pwd').focus();
        var d = dialog({
            title: '信息提示！',
            content: '请输入6至20位密码',
            ok: function () {
            }
        });
        d.showModal();
        return false;
    }
    if (smscode==""||smscode.length!=4){
        $("#smscode").focus();
        var d = dialog({
            title: '信息提示！',
            content: '请输入正确验证码',
            ok: function () {
            }
        });
        d.showModal();
        return false;
    }
    $.post("/index.php/AppApiV1/Member/checkVerificationSMS.html",
        {
            'code':smscode
        },
        function (data) {
            if(data.status==1){
                $.post("/index.php/AppApiV1/Member/register.html",
                    {
                        'phone' : tel,
                        'password' : pwd,
                        'recommend_phone' : recommend_phone
                    },
                    function (data) {
                        console.log(JSON.stringify(data))
                        if(data.status==1){
                            var d = dialog({
                                title: '信息提示！',
                                content: data.msg,
                                ok: function () {
                                    phone();
                                }
                            });
                            d.showModal();
                        }else{
                            var d = dialog({
                                title: '信息提示！',
                                content: data.msg,
                                ok: function () {
                                }
                            });
                            d.showModal();
                        }
                    },'json');
            }else{
                var d = dialog({
                    title: '信息提示！',
                    content: data.msg,
                    ok: function () {
                    }
                });
                d.showModal();
            }
        },'json');
});
$("#display").click(function () {
    $("#alert_container").css({
        display: "none"
    })
});
//判断手机型号跳转下载
function phone() {
    if (isWeiXin()) {
        if (isiOS) {
            window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.a56999.aiyun';//苹果下载地址
        } else {
            window.location.href = "./warmPrompt.html";
        }
    } else {
        if (isiOS) {
            window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.a56999.aiyun';//苹果下载地址
        } else {
            window.location.href = "./download.html";
        }
    }
}

function isWeiXinOrQQOrNot() {
    var ua = window.navigator.userAgent.toLowerCase();
    if (ua.match(/MicroMessenger/i) == 'micromessenger') {
        return 1;
    } else if( ua.match(/QQ/i) == "qq"){
        return 2;
    }else {
        return 0;
    }
}
