/**
 * Created by zp on 2018/11/12.
 */
let timer = null; //定时器
let timerClick = true; //定时器开关

$(function () {
    /**
     * 查看密码
     */
    $(".password-eys").bind("click",function () {
        if(!$(this).hasClass("active")){
            $(this).siblings("input").prop("type","text");
            $(this).addClass("active");
        } else {
            $(this).siblings("input").prop("type","password");
            $(this).removeClass("active");
        }
    });
    /**
     * 切换显示验证码登录
     */
    $(".btn-change-code-login").bind("click",function () {
        $(".password-login").addClass("none");
        $(".code-login").removeClass("none");
        $(".register").addClass("none");
        $(".change-password").addClass("none");
        if (timer != null) {
            timerClick = true;
            clearInterval(timer);
            timer = null;
        }
        $(".code-box").removeClass("timer");
        $(".code-box").text("获取验证码");
    });
    /**
     * 切换显示密码登录
     */
    $(".btn-change-password-login, .btn-register-login").bind("click",function () {
        $(".password-login").removeClass("none");
        $(".code-login").addClass("none");
        $(".register").addClass("none");
        $(".change-password").addClass("none");
    });
    /**
     * 切换显示注册账户
     */
    $(".btn-register").bind("click",function () {
        $(".password-login").addClass("none");
        $(".code-login").addClass("none");
        $(".register").removeClass("none");
        $(".change-password").addClass("none");
        if (timer != null) {
            timerClick = true;
            clearInterval(timer);
            timer = null;
        }
        $(".code-box").removeClass("timer");
        $(".code-box").text("获取验证码");
    });
    /**
     * 切换显示修改密码
     */
    $(".forget-password").bind("click",function () {
        $(".password-login").addClass("none");
        $(".code-login").addClass("none");
        $(".register").addClass("none");
        $(".change-password").removeClass("none");
        if (timer != null) {
            timerClick = true;
            clearInterval(timer);
            timer = null;
        }
        $(".code-box").removeClass("timer");
        $(".code-box").text("获取验证码");
    });
    /**
     * 获取短信验证码
     */
    $(".code-box").bind("click", function () {
        if (timerClick) {
            timerClick = false;
            $(".code-box").addClass("timer");
            $(".code-box").text("60s");
            timer = setInterval(function () {
                let time = parseInt($(".code-box").text());
                if (time > 0) {
                    time--;
                    console.log(time);
                    $(".code-box").text(time + "s");
                } else {
                    if (timer != null) {
                        timerClick = true;
                        clearInterval(timer);
                        timer = null;
                    }
                    $(".code-box").removeClass("timer");
                    $(".code-box").text("重新发送");
                }
            }, 1000);
        }
    });
});