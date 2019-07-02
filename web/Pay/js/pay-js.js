/**
 * Created by keyC on 2018/8/20.
 * 获取支付详情，并选择支付方式发起付款
 */
var priceInfoList = []; //价格详情

$(function () {
    //获取账单信息
    getRequest(getPayInfo);
    // getPayInfo();
});

/**
 * 获取账单信息
 */
function getPayInfo() {
    loadAlertShow("正在获取");
    $.ajax({
        type: 'POST',
        url: $.getPayInfo,
        data: {
            // user_id: $.user_id,
            // order_no: "NV8LKEDQ",
            // order_type: "4"
            user_id: param.user_id,
            order_no: param.order_no,
            order_type: param.order_type
        },
        dataType: 'json',
        success: function (result) {
            loadAlertHide();
            console.log(result);
            if (result.status == 1) {
                switch (result.ext) {
                    case 1:
                        var data = result.data;
                        if (data != null) {
                            if(data.price && parseFloat(data.price) != 0.00){
                                let start_info = {
                                    type: 2,
                                    text: "行程费用",
                                    price: data.price + "元"
                                };
                                priceInfoList.push(start_info);
                            }
                            if(data.discount_money && parseFloat(data.discount_money) != 0.00){
                                let discount_money = {
                                    type: 1,
                                    text: "优惠金额",
                                    price: data.discount_money + "元"
                                };
                                priceInfoList.push(discount_money);
                            }
                            if(data.order_reward && parseFloat(data.order_reward) != 0.00){
                                let order_reward = {
                                    type: 1,
                                    text: "奖励金额",
                                    price: data.order_reward + "元"
                                };
                                priceInfoList.push(order_reward);
                            }
                            if(data.parking_fee && parseFloat(data.parking_fee) != 0.00){
                                let parking_fee = {
                                    type: 2,
                                    text: "停车费",
                                    price: data.parking_fee + "元"
                                };
                                priceInfoList.push(parking_fee);
                            }
                            if(data.road_bridge_fee && parseFloat(data.road_bridge_fee) != 0.00){
                                let road_bridge_fee = {
                                    type: 2,
                                    text: "高速费",
                                    price: data.road_bridge_fee + "元"
                                };
                                priceInfoList.push(road_bridge_fee);
                            }
                            if(data.toll_fee && parseFloat(data.toll_fee) != 0.00){
                                let toll_fee = {
                                    type: 2,
                                    text: "路桥费",
                                    price: data.toll_fee + "元"
                                };
                                priceInfoList.push(toll_fee);
                            }
                            let template = document.getElementById('template-price-info-list').innerHTML;
                            document.getElementById('price-info-list').innerHTML = doT.template( template )( priceInfoList );
                            $(".mp-alert-pay-btn").text("确认支付 " + data.pay_price + " 元");
                        }
                        break;

                    case 2:
                        toastAlertShow(result.msg);
                        break;
                }
            } else if (result.status == 3) {
                window.location.href = result.data;
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
        if (priceInfoList && priceInfoList.length > 0) {
            pay(payType, param.order_no, param.user_id,  param.order_type);
        } else {
            toastAlertShow("未获取到价格信息，无法发起支付");
        }
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
function pay(payType, order_no, user_id, order_type) {
    loadAlertShow("正在提交");
    $.ajax({
        type: 'POST',
        url: $.unifiedOrder,
        data: {
            user_id: user_id,
            order_no: order_no,
            order_type: order_type,
            channel: payType
            // user_id: $.user_id,
            // order_no: "NV8LKEDQ",
            // channel: payType,
            // order_type: "4"
        },
        dataType: 'json',
        success: function (result) {
            loadAlertHide();
            console.log(result);
            if(result.status == 1){
                switch (result.ext) {
                    case 1: // 下单成功
                        var res = result.data;
                        window.location = res ;
                        break;

                    case 2:
                        toastAlertShow(result.msg);
                        break
                }
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