$(function () {
    //获取账单信息
    getRequest(getPayInfo);
       // getPayInfo();
});

//获取账单信息
function getPayInfo() {
    loadAlertShow("正在获取");
    $.ajax({
        type: 'POST',
        url: $.getPayInfo,
           // data: {user_id: "ff3110c0-8683-4a1c-a770-5dec8b675059", order_no: "AK3TF05S"},
        data: {
            user_id: param.user_id,
            order_no: param.order_no
        },
        dataType: 'json',
        success: function (result) {
            loadAlertHide();
            console.log(result);
            if (result.status == 1) {
                var data = result.data;
                if (data != null) {
                    $("#price").text(data.price + " 元");
                    if (data.discount_money != 0) {
                        $("#discount-price").text(data.discount_money + " 元");
                        $("#discount-price-div").removeClass("none").addClass("flex-row discount-money-row");
                    } else {
                        $("#discount-price-div").removeClass("flex-row discount-money-row").addClass("none");
                    }
                    $(".mp-alert-pay-btn").text("确认支付 " + data.pay_price + " 元");
                }
            } else if (result.status == 3) {
                window.location.href = result;
            } else {
                toastAlertShow(result.msg);
            }
        },
        error: function (err) {
            loadAlertHide();
            console.log("err",err);
            window.location.href = "../../Util/html/error.html";
        }
    });

    //支付
    $(".mp-alert-pay-btn").bind("click",function(){
        var payType = $("input[name='radio']:checked").val();
        pay(payType, param.order_no, param.user_id);
    });

    //放弃支付
    $(".mp-img-close").bind("click",function(){
        // towBtnAlert("爱运温馨提示", "", "确定要放弃本次支付吗？", function (res) {
        //     if (res) {
        //         closeWebview();
        //         // window.location.reload();
        //     }
        // }, "取消", "确定")
        closeWebview();
    });
}

/**
 * 点击支付
 * @param payType 支付类型 1 微信， 2 支付宝
 * @param order_no 订单号
 * @param user_id 用户id
 */
function pay(payType, order_no, user_id) {
    loadAlertShow("正在提交");
    $.ajax({
        type: 'POST',
        url: $.unifiedOrder,
        data: {
            // user_id: user_id,
            // order_no: order_no,
            user_id: "474b4062-8a08-4c44-8113-188106c1c45b",
            order_no: "AK3TF05S",
            channel: payType
        },
        dataType: 'json',
        success: function (result) {
            loadAlertHide();
            console.log(result);
            if(result.status == 1){// 下单成功
                var res = result.data;
                console.log(res);
                window.location = res ;
            } else if (result.status == 5) {//已经支付过
                window.location.href = result;
            } else {//统一下单失败
                toastAlertShow(result.msg);
            }
        },
        error: function (err) {
            loadAlertHide();
            console.log(err);
            window.location.href = "../../Util/html/error.html";
        }
    });
}