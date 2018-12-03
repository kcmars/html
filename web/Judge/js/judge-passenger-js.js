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
    loadAlertShow("加载中...");
    $.ajax({
        type: 'POST',
        url: $.getRatingInfo,
        // data: {
        //     user_id: "ff3110c0-8683-4a1c-a770-5dec8b675059",
        //     order_no: "AK3TF05S",
        //     identity: "2"
        // },
        data: {
            user_id: param.user_id,
            order_no: param.order_no,
            identity: "2"
        },
        dataType: 'json',
        success: function (res) {
            console.log(res);
            loadAlertHide();
            result = res;
            if (res && res.status == 1) { //已评价
                $(".judge-main").removeClass("none");
                let data = res.data;
                $(".submit, input").addClass("none");
                $(".status").text(getStatue(data.pay_status));
                $("#money").text(data.price);
                $(".text").text("已评价乘客");
                $("#phone").text("尾号" + data.phone);
                if(data.portrait != null){
                    $("#portrait").attr("src", $.server1 + data.portrait);
                }
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
                $(".judge-main").removeClass("none");
                let data = res.data;
                $(".submit, input").removeClass("none");
                $(".status").text(getStatue(data.pay_status));
                $("#money").text(data.price);
                $(".text").text("请评价乘客");
                $("#phone").text("尾号" + data.phone);
                if(data.portrait != null){
                    $("#portrait").attr("src", $.server1 + data.portrait);
                }
                $(".judge-hint").text("指出不足");
                let content = data.content;
                if(content.length > 0){
                    for (let i in content) {
                        if(content[i].type == 1){
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
                    $(".judge-hint").text("完美乘客，无可挑剔");
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

    //继续听单
    $("#go-on-btn").bind("click",function(){
        continueOrder(param.user_id);
        // continueOrder();
    });

    //暂停听单
    $("#stop-btn").bind("click",function(){
        stopOrder(param.user_id);
        // stopOrder();
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
        // user_id: "ff3110c0-8683-4a1c-a770-5dec8b675059",
        // order_no: "AK3TF05S",
        user_id: user_id,
        order_no: order_no,
        identity: "2",
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
                $(".text").text("已评价乘客");
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

                //是否是从行程结束点击过来，从行程详情点击过来则不显示
                if (param.status == "ing") {
                    //评价成功，显示是否继续听单
                    $("#continue-btn").removeClass("none").addClass("continue-btn");
                }
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
 * 继续接单
 * @param user_id
 */
function continueOrder(user_id) {
    loadAlertShow("正在提交");
    $.ajax({
        type: 'POST',
        url: $.submitRating,
        data: {user_id: user_id},
        dataType: 'json',
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res.status == 1){
                toastAlertShow(res.msg);
                closeWebview();
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
 * 暂停接单
 * @param user_id
 */
function stopOrder(user_id) {
    loadAlertShow("正在提交");
    $.ajax({
        type: 'POST',
        url: $.submitRating,
        data: {user_id: user_id},
        dataType: 'json',
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res.status == 1){
                toastAlertShow(res.msg);
                closeWebview();
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

/**
 * 获取信息状态
 * @param status
 * @returns {string}
 */
function getStatue(status) {
    let str = "";
    switch (status) {
        case 5:
            str = "待收款";
            break;
        case 6:
            str = "已收款";
            break;
        default:break;
    }
    return str;
}