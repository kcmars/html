/**
 * Created by zp on 2018/8/20.
 */
let priceData = []; //价格配置详情
$(function () {
    //获取计价规则
    getRequest(getPriceRules);
    // getPriceRules();
});
/**
 * 获取计价规则
 */
function getPriceRules() {
    loadAlertShow("正在获取");
    $.ajax({
        type: 'POST',
        url: $.getPriceConfig,
        data: {
            user_id: param.user_id,
            order_no: param.order_no
            // user_id: "f119347b-5c60-40f5-91e2-f0701d2c7e8c",
            // order_no: "1O0FE3P0"
        },
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res && res.status == 1){
                $(".main-rules").removeClass("none");
                let data = res.data;
                if (data.start_config != null && data.start_config.price != null) {
                    let start_config = {
                        type: 2,
                        text: "起步价",
                        price: data.start_config.price + "元"
                    };
                    priceData.push(start_config);
                }
                if(data.price_per_kilometre != null){
                    let price_per_kilometre = {
                        type: 2,
                        text: "里程费",
                        price: data.price_per_kilometre + "元/公里"
                    };
                    priceData.push(price_per_kilometre);
                }
                if(data.price_per_minute != null){
                    let price_per_minute = {
                        type: 2,
                        text: "时长费",
                        price: data.price_per_minute + "元/分钟"
                    };
                    priceData.push(price_per_minute);
                }
                if(data.far_config != null && data.far_config.price_per_kilometre != null){
                    let far_config = {
                        type: 2,
                        text: "远途费",
                        price: data.far_config.price_per_kilometre + "元/公里"
                    };
                    priceData.push(far_config);
                    if (data.far_config.distance != null) {
                        $("#far-info-text").text("超出 "+ data.far_config.distance + " 公里后，加收远途费");
                    }
                }
                let template = document.getElementById('template-price-detail').innerHTML;
                document.getElementById('price-detail').innerHTML = doT.template( template )( priceData );

                if (data.night_config != null) {
                    if (data.night_config.price_per_minute != null) {
                        $("#night-price").text(data.night_config.price_per_minute + "元/公里");
                    }
                    if (data.night_config.start_time != null && data.night_config.end_time != null) {
                        $("#night-text").text("夜间（" + $.format(data.night_config.start_time, "hh:mm") + "-次日" + $.format(data.night_config.end_time, "hh:mm") + "），加收夜间服务费");
                    }
                }
                if (data.share_config != null && data.share_config.config != null) {
                    if (data.share_config.config.one != null) {
                        $("#share-1").text("1人拼车按爱运快车价格的" + (parseFloat(data.share_config.config.one))+ "%计费");
                    }
                    if (data.share_config.config.two != null) {
                        $("#share-2").text("2人拼车按爱运快车价格的" + (parseFloat(data.share_config.config.two))+ "%计费");
                    }
                    if (data.share_config.config.three != null) {
                        $("#share-3").text("3人拼车按爱运快车价格的" + (parseFloat(data.share_config.config.three))+ "%计费");
                    }
                }
            } else {
                $(".main-rules").removeClass("none");
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