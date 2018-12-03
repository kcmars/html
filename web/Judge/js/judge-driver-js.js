/**
 * Created by zp on 2018/8/20.
 */
$(function () {
    //获取评价信息
    getRequest(getRatingInfo);
    // getRatingInfo();
});
let result; //内容
let resultGood = []; //好的评价
let resultBad = []; //不好的评价
let starIndex = 1; //星星选择个数
/**
 * 获取评价信息
 */
function getRatingInfo() {
    let params = {
        // user_id: "9d398738-2105-4410-8f08-17ba207bce67",
        // order_no: "JJSHRLVV",
        // identity: "1"
        user_id: param.user_id,
        order_no: param.order_no,
        identity: "1"
    };
    console.log("params", params);
    loadAlertShow("加载中...");
    $.ajax({
        type: 'POST',
        url: $.getRatingInfo,
        data: params,
        dataType: 'json',
        success: function (res) {
            console.log(res);
            loadAlertHide();
            result = res;
            if (res && res.status == 1) { //已评价
                $("#judge-main").removeClass("none");
                let data = res.data;
                $(".submit, input").addClass("none");
                $(".text").text("已评价司机");
                $(".list-star li").each(function () {
                    if($(this).index() < data.star){
                        $(this).addClass("active");
                    } else {
                        $(this).removeClass("active");
                    }
                });
                $(".judge-hint").text(data.remark);
                let template = document.getElementById('template-judge-content-list').innerHTML;
                document.getElementById('list-content').innerHTML = doT.template(template)(data.content);

            } else if(res.status == 2) { //未评价
                $("#judge-main").removeClass("none");
                let data = res.data;
                $(".submit, input").removeClass("none");
                $(".text").text("请评价司机");
                $(".judge-hint").text("指出不足");
                let content = data.content;
                if (content.length > 0) {
                    for (let i in content) {
                        if (content[i].type == 1) {
                            resultGood.push(content[i]);
                        } else {
                            resultBad.push(content[i]);
                        }
                    }
                    $(".submit").attr("disabled", true);
                    $(".submit").addClass("active-btn");
                    let template = document.getElementById('template-judge-content-list').innerHTML;
                    document.getElementById('list-content').innerHTML = doT.template(template)(resultBad);
                }
            } else if (res.status == 3) { //没有获取到支付结果
                window.location.href = "../../Util/html/payRequest.html";
            } else {
                toastAlertShow(res.msg)
            }
        },
        error: function (err) {
            console.log("err",err);
            loadAlertHide();
            window.location.href = "../../Util/html/error.html";
        }
    });

    //选择星星
    $(".list-star li").bind("click",function(){
        if(!(result && result.status == 1)){
            starIndex = $(this).index() + 1;
            $(".list-star li").each(function () {
               if($(this).index() < starIndex){
                   $(this).addClass("active");
               } else {
                   $(this).removeClass("active");
               }
            });
            let template = document.getElementById('template-judge-content-list').innerHTML;
            if(starIndex == 5){
                // $(".submit").attr("disabled", false);
                // $(".submit").removeClass("active-btn");
                if(resultGood){
                    $(".judge-hint").text("完美司机，无可挑剔");
                    document.getElementById('list-content').innerHTML = doT.template(template)(resultGood);
                }
            } else {
                // $(".submit").attr("disabled", true);
                // $(".submit").addClass("active-btn");
                if(resultBad){
                    $(".judge-hint").text("指出不足");
                    document.getElementById('list-content').innerHTML = doT.template(template)(resultBad);
                }
            }
        }
    });

    //提交评价
    $(".submit").bind("click",function(){
        submitRating(param.user_id, param.order_no);
        // submitRating();
    });
}

/**
 * 提交评价
 * @param user_id
 * @param order_no
 */
function submitRating(user_id, order_no) {
    let content = "";
    $(".list-content li").each(function () {
        if($(this).hasClass("active")){
            content += $(this).context.id;
            content += ",";
        }
    });
    content = content.substring(0, content.length-1);
    let param = {
        // user_id: "9d398738-2105-4410-8f08-17ba207bce67",
        // order_no: "JJSHRLVV",
        user_id: user_id,
        order_no: order_no,
        identity: "1",
        star: starIndex,
        remark: $("input").val().trim(),
        content: content
    };
    console.log(param);
    loadAlertShow("正在提交");
    $.ajax({
        type: 'POST',
        url: $.submitRating,
        data: param,
        dataType: 'json',
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res.status == 1){
                result = {status:1};
                $(".submit, input").addClass("none");
                $(".text").text("已评价司机");
                $(".list-star li").each(function () {
                    if($(this).index() < starIndex){
                        $(this).addClass("active");
                    } else {
                        $(this).removeClass("active");
                    }
                });
                $(".judge-hint").text($("input").val().trim());
                let contentJudge = [];
                if(starIndex == 5){
                    $(".list-content li").each(function () {
                        if($(this).hasClass("active")){
                            for(let i = 0; i<resultGood.length; i++){
                               if(resultGood[i].id == parseInt($(this).context.id)){
                                   contentJudge.push(resultGood[i])
                               }
                            }
                        }
                    });
                } else {
                    $(".list-content li").each(function () {
                        if($(this).hasClass("active")){
                            for(let i = 0; i<resultBad.length; i++){
                                if(resultBad[i].id == parseInt($(this).context.id)){
                                    contentJudge.push(resultBad[i])
                                }
                            }
                        }
                    });
                }
                let template = document.getElementById('template-judge-content-list').innerHTML;
                document.getElementById('list-content').innerHTML = doT.template(template)(contentJudge);
            }else{
                toastAlertShow(res.msg)
            }
        },
        error: function (err) {
            loadAlertHide();
            console.log(err);
            window.location.href = "../../Util/html/error.html";
        }
    });
}

/**
 * 选择评价内容
 * @param obj
 * @param index
 */
function setJudgeItem(obj, index) {
    if(!(result && result.status == 1)){
        if($(obj).hasClass("active")){
            $(obj).removeClass("active");
        } else {
            $(obj).addClass("active");
        }
        if($("#list-content li").hasClass("active")){
            $(".submit").attr("disabled", false);
            $(".submit").removeClass("active-btn");
        } else {
            $(".submit").attr("disabled", true);
            $(".submit").addClass("active-btn");
        }
    }
}
