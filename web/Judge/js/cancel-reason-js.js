/**
 * Created by zp on 2018/8/21.
 */
$(function () {
    getRequest(getCancelReason);
    // getCancelReason();
});

/**
 * 获取取消理由
 */
function getCancelReason() {
    loadAlertShow("加载中...");
    $.ajax({
        type: 'POST',
        url: $.getCancelReason,
        // data: {
        //     user_id: "803659d2-170c-41ab-946d-8cde78cb5a10",
        //     identity: "2"
        // },
        data: {
            user_id: param.user_id,
            identity: param.identity
        },
        dataType: 'json',
        success: function (res) {
            console.log(res);
            loadAlertHide();
            result = res;
            if (res && res.status == 1) {
                $(".cancel-main").removeClass("none");
                let resultReason = res.data;
                let template = document.getElementById('template-cancel-reason-list').innerHTML;
                document.getElementById('cancel-reason').innerHTML = doT.template(template)(resultReason);
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

    // //取消
    // $("#close").bind("click",function(){
    //     closeWebview();
    // });

    //提交取消理由
    $(".submit").bind("click",function(){
        submitCancelReason(param.user_id, param.order_no);
        // submitCancelReason();
    });
}

/**
 * 提交取消理由
 */
function submitCancelReason(user_id, order_no) {
    loadAlertShow("加载中...");
    let reason = 0;
    $("#cancel-reason li").each(function () {
        if($(this).hasClass("active")){
            reason = $(this).context.id;
        }
    });
    $.ajax({
        type: 'POST',
        url: $.submitReason,
        // data: {
        //     user_id: "767ef565-80fa-4f73-b42e-51226dad7e42",
        //     order_no: "1TRL6P7E",
        //     reason: "1",
        //     remark: "1"
        // },
        data: {
            user_id: user_id,
            order_no: order_no,
            reason: reason,
            remark: $("input").val().trim()
        },
        dataType: 'json',
        success: function (res) {
            console.log(res);
            loadAlertHide();
            result = res;
            if (res.status == 1) { //已评价
                toastAlertShow(res.msg);
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
}
/**
 * 选择取消内容
 * @param obj
 * @param index
 */
function setReason(obj, index) {
    // if($(obj).hasClass("active")){
    //     $(obj).removeClass("active");
    // } else {
    //     $(obj).addClass("active").siblings().removeClass("active");
    // }
    $(obj).addClass("active").siblings().removeClass("active");
    if($("#cancel-reason li").hasClass("active")){
        $(".submit").attr("disabled", false);
        $(".submit").removeClass("active-btn");
    } else {
        $(".submit").attr("disabled", true);
        $(".submit").addClass("active-btn");
    }
}