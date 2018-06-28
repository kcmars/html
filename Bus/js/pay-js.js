$(function () {
    //获取账单信息
    getRequest(getPayInfo);
       // getPayInfo();
});

//获取账单信息
function getPayInfo() {
    console.log($.unifiedOrder);
    $.ajax({
        type: 'POST',
        url: $.unifiedOrder,
           // data: {order_no: "15278201843176296830"},
        data: {order_no: param.order_no},
        dataType: 'json',
        success: function (result) {
            console.log(result);
            if (result.status == 1) {
                var data = result.data;
                if (data != null) {
                    $(".mp-alert-pay-btn").text("确认支付" + data.pay_money + "元");
                }
            }
        },
        error: function (err) {
            console.log("err",err);
        }
    });

    //支付
    $(".mp-alert-pay-btn").bind("click",function(){
        var payType = $("input[name='radio']:checked").val();
        pay(payType, param.order_no);
    });

    //放弃支付
    $(".mp-img-close").bind("click",function(){
        towBtnAlert("爱运温馨提示", "", "确定要放弃本次支付吗？", function (res) {
            if (res) {
                closeWebview();
                window.location.reload();
            }
        }, "取消", "确定")
    });
}

//支付
function pay(payType, order_no) {
    if (payType == 'weixin') {
        $.ajax({
            type: 'POST',
            url: $.toWechatPay,
            data: {order_no: order_no},
            dataType: 'json',
            success: function (result) {
                console.log(result);
                if(result.status == 0){//统一下单失败

                }else{
                    var res = result.data;
                    console.log(res);
                    var turnUrl = 'aiyunpays://wap/pay?' ;
                    var param = 'appid='+res.appid ;
                    param += '&partnerid='+res.partnerid ;
                    param += '&prepayid='+res.prepayid ;
                    param += '&package='+res.package ;
                    param += '&noncestr='+res.noncestr ;
                    param += '&timestamp='+res.timestamp ;
                    param += '&sign='+res.sign ;
                    var return_url = "/AppWeb/OrderPay/payResult?out_trade_no=" + res.out_trade_no;
                    param += "&return_url=" + return_url ;
                    window.location = turnUrl + param ;
                }
            },
            error: function (err) {
                console.log(err);
            }
        });
    }else if (payType === 'alipay') {
        $.ajax({
            type: 'POST',
            url: $.toAlipay,
            data: {order_no: order_no},
            dataType: 'json',
            success: function (result) {
                console.log(result);
                if (result.status == 0) {//

                } else {
                    var res = result.data;
                    console.log(res);
                    window.location = res.payurl ;
                }
            },
            error: function (err) {
                console.log(err);
            }
        });
    }
}