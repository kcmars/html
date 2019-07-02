/**
 * Created by keyC on 2017/5/13.
 */
'use strict';

function hideForm() {
    //location.href = "./recommendLink.html";
    window.history.go(-1);
}

//是否可以点击注册
var isRegister = false;

var data = {};
var token = "";

$(function () {
    var url = window.location.href;
    if (url.indexOf('?') !== -1) {
        var search = url.substring(url.indexOf('?') + 1);
        var queryArray = search.split('&');
        queryArray.forEach(function (item){
            var itemArray = item.split('=');
            var key = itemArray[0];
            var value = decodeURIComponent(itemArray[1]) ? decodeURIComponent(itemArray[1]) : '';
            data[key] = value;
        })
    }
    // $('body').height($('body')[0].clientHeight);
    oneBtnAlert("爱运温馨提示",
        "",
        "新用户免费注册成功后，如若不能及时下载安装APP，可在下次进入此推荐链接中，输入您已注册的手机号码，即可为您提供下载地址，轻松下载APP。",
        function (res) {
            if (res) {}
        },
        "知道了,现在去注册"
    );
    // toastAlertShow("新用户免费注册成功后，如若不能及时下载安装APP，可在下次进入此推荐链接中，输入您已注册的手机号码，即可为您提供下载地址，轻松下载APP。", 5000);

    /**
     * 检测手机号输入
     */
    $("#phoneCheck").bind("input", function () {
        var phone = $(this).val().trim();
        if (phone.length == 11) {
            //获取图形验证码
            checkPhone();
        }
    });

    /**
     * 检测短信验证码
     */
    $("#messageCode").bind("input", function () {
        var sms_code = $(this).val().trim();
        if (sms_code.length == 4) {
            //检测短信验证码
            checkSmsCode();
        }
    });

    /**
     * 检测图片验证码
     */
    $("#imgCode").bind("input", function () {
        var img_code = $(this).val().trim();
        if (img_code.length == 4) {
            //检测图片验证码
            checkImgCode();
        }
    });

    /**
     * 检查手机号是否注册
     */
    function checkPhone(phone) {
        loadAlertShow("正在检测...");
        let params = {
            phone: $("#phoneCheck").val().trim()
        };
        $.ajax({
            type: 'POST',
            url: $.getShareLinkCheckPhone,
            data: params,
            success: function (res) {
                console.log(res);
                loadAlertHide();
                if (res) {
                    if (res.status == 1) {
                        if (res.ext == 1) {  //已注册，前往下载
                            $(".registered .registered_phone").text(phone);
                            $(".registered .registered_text").text("该手机号已注册过“爱运宝”，可直接下载安装登录“爱运宝”APP，如忘记密码或修改密码可在APP登录页面通过【忘记密码】操作找回密码或修改密码");
                            $(".content").hide().siblings(".registered").show();
                            toastAlertShow(res.msg);

                        } else if (res.ext == 2){ // 输入短信验证码
                            $(".sms_code").removeClass("none");
                            $(".img_code").addClass("none");
                            time($(".codeBtn"), 60);

                        } else if (res.ext == 3) {  //输入图片验证码
                            $(".img_code").removeClass("none");
                            $(".sms_code").addClass("none");
                            $("#img-code").attr("src", res.data);
                        }
                    } else {
                        toastAlertShow(res.msg);
                    }
                }
            },
            error: function (err) {
                loadAlertHide();
                console.log(err);
            }
        });
    }

    /**
     * 检测短信验证码
     */
    function checkSmsCode() {
        loadAlertShow("正在检测...");
        let params = {
            phone: $("#phoneCheck").val().trim(),
            sms_code: $("#messageCode").val().trim(),
            event: "register"
        };
        $.ajax({
            type: 'POST',
            url: $.getShareLinkCheckSmsCode,
            data: params,
            success: function (res) {
                console.log(res);
                loadAlertHide();
                if (res) {
                    if (res.status == 1) {
                        if (res.ext == 1) {
                            token = res.data;
                            $(".sms_code").addClass("none");
                            $(".img_code").addClass("none");
                            $(".pws_code").removeClass("none");
                        } else {
                            toastAlertShow(res.msg);
                        }
                    } else {
                        toastAlertShow(res.msg);
                    }
                }
            },
            error: function (err) {
                loadAlertHide();
                console.log(err);
            }
        });
    }

    /**
     * 检测图形验证码
     */
    function checkImgCode() {
        loadAlertShow("正在检测...");
        let params = {
            phone: $("#phoneCheck").val().trim(),
            img_code: $("#imgCode").val().trim(),
            event: "register"
        };
        $.ajax({
            type: 'POST',
            url: $.getShareLinkCheckImgCode,
            data: params,
            success: function (res) {
                console.log(res);
                console.log("params=", params);
                loadAlertHide();
                if (res) {
                    if (res.status == 1) {
                        if (res.ext == 2) {
                            $(".sms_code").removeClass("none");
                            $(".img_code").addClass("none");
                            time($(".codeBtn"), 60);
                        } else {
                            toastAlertShow(res.msg);
                        }
                    } else {
                        getShareLinkImgCode();
                        toastAlertShow(res.msg);
                    }
                }
            },
            error: function (err) {
                loadAlertHide();
                console.log(err);
            }
        });
    }

    /**
     * 检测密码输入
     */
    $("#passwordCheck").bind("input", function () {
        var pwd = $(this).val().trim();
        if (pwd.length >= 6) {
            isRegister = true;
            $(".submitBtn").addClass("submitBtnActive");
        } else {
            isRegister = false;
            $(".submitBtn").removeClass("submitBtnActive");
        }
    });
});

$(".registerFormBox .close").bind("click",function () {
    hideForm();
});

/**
 * 注册协议
 */
function toggleCheck() {
    if($(".registerFormBox .protocol .check").hasClass("checkNo")){
        $(".registerFormBox .protocol .check").removeClass("checkNo");
    } else {
        $(".registerFormBox .protocol .check").addClass("checkNo");
    }
}

/**
 * 获取短信验证码
 * @param obj
 * @param t
 * @returns {boolean}
 */
function getSmsCode(obj, t) {
    loadAlertShow("正在获取...");
    let params = {
        event: "register"
    };
    $.ajax({
        type: 'POST',
        url: $.getShareLinkSmsCode,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if (res) {
                if (res.status == 1) {
                    if (res.ext == 1) {
                        time(obj, 60);
                    } else {
                        toastAlertShow(res.msg);
                    }
                } else {
                    toastAlertShow(res.msg);
                }
            }
        },
        error: function (err) {
            loadAlertHide();
            console.log(err);
        }
    });
}

/**
 * 定时器
 * @param obj
 * @param t
 */
function  time(obj,t){
    if (t === 0) {
        $(obj).removeAttr("disabled");
        $(obj).removeClass("disable").addClass("able");
        $(obj).text("获取验证码");
    } else {
        $(obj).attr("disabled", "true");
        $(obj).text(t + "秒后重试");
        $(obj).removeClass("able").addClass("disable");
        t--;
        setTimeout(function () {
            time(obj, t);
        }, 1000);
    }
}

/**
 * 提交注册
 */
$(".registerFormBox .submitBtn").bind("click",function () {
    if (!isRegister) {
        return;
    }
    var pwd = $("#passwordCheck").val(),
        tjtel = (data != null && data.tjtel != null) ? data.tjtel : "", // 推荐人电话
        city = (data != null && data.city != null) ? data.city : "",
        identity = (data != null && data.identity != null) ? data.identity : "", // 推荐身份: 1 乘客， 2 司机
        type = (data != null && data.type != null) ? data.type : ""; // 推荐类型： 1 快车， 2 出租车，4 大巴车

    if(pwd==""){
        toastAlertShow("请输入密码");
        return;
    }
    loadAlertShow("正在提交...");
    let params = {
        pwd: pwd,
        __token__: token,
        event: "register",
        city: city,
        identity: identity,
        type: type
    };
    if (tjtel != "") {
        params.recommend_tel = tjtel;
    }
    $.ajax({
        type: 'POST',
        url: $.getShareLinkRegister,
        data: params,
        success: function (res) {
            console.log("params=", params);
            console.log("res=", res);
            loadAlertHide();
            if (res) {
                if (res.status == 1) {
                    if (res.ext == 1) {
                        $(".registered .registered_phone").text($("#phoneCheck").val().trim());
                        $(".registered .registered_text").text("恭喜您，已成功注册“爱运宝”");
                        $(".content").hide().siblings(".registered").show();
                        toastAlertShow(res.msg);
                    } else {
                        toastAlertShow(res.msg);
                    }
                } else {
                    toastAlertShow(res.msg);
                }
            }
        },
        error: function (err) {
            console.log(err);
            loadAlertHide();
            window.location.href = "../../../Util/html/error.html";
        }
    });
});

/**
 * 显示隐藏密码
 */
$(".registerFormBox .inputBox .icon-eye").bind("click",function () {
    if(!$(this).hasClass("open")){
        $(this).siblings("#passwordCheck").prop("type","text");
        $(this).addClass("open");
    } else {
        $(this).siblings("#passwordCheck").prop("type","password");
        $(this).removeClass("open");
    }
});

/**
 * 获取图片验证码
 */
function getShareLinkImgCode() {
    loadAlertShow("正在获取...");
    let params = {
        phone: $("#phoneCheck").val().trim(),
        event: "register"
    };
    $.ajax({
        type: 'POST',
        url: $.getShareLinkImgCode,
        data: params,
        success: function (res) {
            loadAlertHide();
            console.log(res);
            if (res) {
                if (res.status == 1) {
                    if (res.ext) {
                        $("#img-code").attr("src", res.data);
                    } else {
                        toastAlertShow(res.msg);
                    }
                } else {
                    toastAlertShow(res.msg);
                }
            }
        },
        error: function (err) {
            loadAlertHide();
            console.log(err);
        }
    });
}
$('#captcha-container').bind("click", function () {
    getShareLinkImgCode();
});

/**
 * 判断手机型号跳转下载
 * @type {string}
 */
var u = navigator.userAgent;
var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
function phone() {
    console.log("isiOS=", isiOS);
    if (isWeiXin()) {
        if (isiOS) {
            window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.a56999.aiyun';//苹果下载地址
        } else {
            window.location.href = "https://sj.qq.com/myapp/detail.htm?apkName=com.a56999.aiyun"; //应用宝下载地址
            // window.location.href = "./warmPrompt.html";
        }
    } else {
        if (isiOS) {
            window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.a56999.aiyun';//苹果下载地址
        } else {
            window.location.href = "https://sj.qq.com/myapp/detail.htm?apkName=com.a56999.aiyun"; //应用宝下载地址
            // window.location.href = "./warmPrompt.html";
        }
    }
}

function isWeiXin() {
    var ua = window.navigator.userAgent.toLowerCase();
    if (ua.match(/MicroMessenger/i) == 'micromessenger' || ua.match(/QQ/i) == "qq") {
        return true;
    } else {
        return false;
    }
}