/**
 * Created by Administrator on 2018/8/20.
 */
let priceData = []; //价格详情
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
        url: $.getDetailEstimateInfo,
        data: {
            user_id: param.user_id,
            order_no: param.order_no,
            type: "1"
            // user_id: "803659d2-170c-41ab-946d-8cde78cb5a10",
            // order_no: "EADNCFC9"
        },
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res && res.status == 1){
                $(".main").removeClass("none");
                let data = res.data;
                $("#price").text(data.price);
                if(data.distance_info && parseFloat(data.distance_info.price) != 0.00){
                    let distance_info = {
                        type: 2,
                        text: "里程费(" + (data.distance_info.distance/1000).toFixed(2) + "公里)",
                        price: data.distance_info.price + "元"
                    };
                    priceData.push(distance_info);
                }
                if(data.duration_info && parseFloat(data.duration_info.price) != 0.00){
                    let duration_info = {
                        type: 2,
                        text: "时长费(" + (data.duration_info.duration/60).toFixed(2) + "分钟)",
                        price: data.duration_info.price + "元"
                    };
                    priceData.push(duration_info);
                }
                if(data.far_info && parseFloat(data.far_info.price) != 0.00){
                    let far_info = {
                        type: 2,
                        text: "远途费(" + (data.far_info.distance/1000).toFixed(2) + "公里)",
                        price: data.far_info.price + "元"
                    };
                    priceData.push(far_info);
                }
                if(data.night_info && parseFloat(data.night_info.price) != 0.00){
                    let night_info = {
                        type: 2,
                        text: "夜间费",
                        price: data.night_info.price + "元"
                    };
                    priceData.push(night_info);
                }
                if(data.extra && parseFloat(data.extra.discount) != 0.00){
                    let discount = {
                        type: 1,
                        text: "优惠金额",
                        price: data.extra.discount + "元"
                    };
                    priceData.push(discount);
                }
                if(data.extra && parseFloat(data.extra.award) != 0.00){
                    let extra = {
                        type: 1,
                        text: "奖励金额",
                        price: data.extra.award + "元"
                    };
                    priceData.push(extra);
                }
                let template = document.getElementById('template-price-detail').innerHTML;
                document.getElementById('price-detail').innerHTML = doT.template( template )( priceData );
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