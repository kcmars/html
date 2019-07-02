/**
 * Created by keyC on 2018/8/21.
 * 取消订单理由
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
        //     user_id: $.user_id,
        //     identity: "1"
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
                $(".cancel-main").removeClass("none");
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
    let data = {
        reason: reason,
        remark: $("input").val().trim()
    };
    paramToActivity(JSON.stringify(data));
    closeWebview();
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