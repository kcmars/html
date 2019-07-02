/**
 * Created by keyC on 2019/4/8.
 * 获取支付结果信息
 */
$(function () {
    //获取支付结果信息
    getRequest(getPayResultInfo);
    // getPayResultInfo();
});

/**
 * 获取支付结果信息
 */
function getPayResultInfo() {
    loadAlertShow("正在获取");
    $.ajax({
        type: 'POST',
        url: $.getPayResult,
        // data: {
        //     user_id: $.user_id,
        //     order_no: "IIJ19FG5",
        //     order_type: "4"
        // },
        data: {
            user_id: param.user_id,
            order_no: param.order_no,
            order_type: param.order_type
        },
        dataType: 'json',
        success: function (result) {
            loadAlertHide();
            console.log(result);
            if (result.status == 1) {
                $(".success").removeClass("none").addClass("pay-result-content");
                $(".success span").text(result.msg);
            } else {
                $(".failure").removeClass("none").addClass("pay-result-content");
                $(".failure span").text(result.msg);
            }
        },
        error: function (err) {
            loadAlertHide();
            console.log("err",err);
            window.location.href = "../../Util/html/error.html";
        }
    });
}
