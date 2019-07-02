/**
 * Created by keyC on 2018/8/20.
 */
var priceDataDriver = []; //价格详情

$(function () {
    //获取价格详情信息
    getRequest(getPriceDetail);
    // getPriceDetail();
});

/**
 * 获取价格详情
 */
function getPriceDetail() {
    loadAlertShow("正在获取");
    $.ajax({
        type: 'POST',
        url: $.getDetailPayInfoOfDriver,
        data: {
            user_id: param.user_id,
            order_no: param.order_no
            // user_id: "ce1c1cd6-432d-4248-91df-2088e55c2615",
            // order_no: "S7DHQAC9"
        },
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res && res.status == 1){
                $(".main").removeClass("none");
                let data = res.data;
                $("#price").text(data.pay_price);
                if(data.price && parseFloat(data.price) != 0.00 && data.order_type == 2){
                    let start_info = {
                        type: 2,
                        text: "计价器计费",
                        price: data.price + "元"
                    };
                    priceDataDriver.push(start_info);
                }
                if(data.start_info && data.start_info.price && parseFloat(data.start_info.price) != 0.00){
                    let start_info = {
                        type: 2,
                        text: "起步价",
                        price: data.start_info.price + "元"
                    };
                    priceDataDriver.push(start_info);
                }
                if(data.distance_info && data.distance_info.price && parseFloat(data.distance_info.price) != 0.00){
                    let distance_info = {
                        type: 2,
                        text: "里程费(" + (data.distance_info.distance/1000).toFixed(2) + "公里)",
                        price: data.distance_info.price + "元"
                    };
                    priceDataDriver.push(distance_info);
                }
                if(data.duration_info && data.duration_info.price && parseFloat(data.duration_info.price) != 0.00){
                    let duration_info = {
                        type: 2,
                        text: "时长费(" + (data.duration_info.duration/60).toFixed(2) + "分钟)",
                        price: data.duration_info.price + "元"
                    };
                    priceDataDriver.push(duration_info);
                }
                if(data.far_info && data.far_info.price && parseFloat(data.far_info.price) != 0.00){
                    let far_info = {
                        type: 2,
                        text: "远途费(" + (data.far_info.distance/1000).toFixed(2) + "公里)",
                        price: data.far_info.price + "元"
                    };
                    priceDataDriver.push(far_info);
                }
                if(data.night_info && data.night_info.price && parseFloat(data.night_info.price) != 0.00){
                    let night_info = {
                        type: 2,
                        text: "夜间费",
                        price: data.night_info.price + "元"
                    };
                    priceDataDriver.push(night_info);
                }
                if(data.parking_fee && parseFloat(data.parking_fee) != 0.00){
                    let parking_fee = {
                        type: 2,
                        text: "停车费",
                        price: data.parking_fee + "元"
                    };
                    priceDataDriver.push(parking_fee);
                }
                if(data.road_bridge_fee && parseFloat(data.road_bridge_fee) != 0.00){
                    let road_bridge_fee = {
                        type: 2,
                        text: "高速费",
                        price: data.road_bridge_fee + "元"
                    };
                    priceDataDriver.push(road_bridge_fee);
                }
                if(data.toll_fee && parseFloat(data.toll_fee) != 0.00){
                    let toll_fee = {
                        type: 2,
                        text: "路桥费",
                        price: data.toll_fee + "元"
                    };
                    priceDataDriver.push(toll_fee);
                }
                if(data.extra && data.extra.discount && parseFloat(data.extra.discount) != 0.00){
                    let discount = {
                        type: 1,
                        text: "优惠金额",
                        price: data.extra.discount + "元"
                    };
                    priceDataDriver.push(discount);
                }
                if(data.extra && data.extra.award && parseFloat(data.extra.award) != 0.00){
                    let extra = {
                        type: 1,
                        text: "奖励金额",
                        price: data.extra.award + "元"
                    };
                    priceDataDriver.push(extra);
                }
                let template = document.getElementById('template-price-detail').innerHTML;
                document.getElementById('price-detail').innerHTML = doT.template( template )( priceDataDriver );
            } else {
                $(".main").removeClass("none");
                toastAlertShow(res.msg);
            }
        },
        error: function (err) {
            console.log(err);
            loadAlertHide();
            window.location.href = "../../Util/html/error.html";
        }
    });
}