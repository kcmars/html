/**
 * Created by Administrator on 2018/8/20.
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
            // user_id: "803659d2-170c-41ab-946d-8cde78cb5a10",
            // order_no: "GVDF1CU3"
        },
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res.status == 1){
                let data = res.data;
                if (data.start_config && data.start_config.price) {
                    let start_config = {
                        type: 2,
                        text: "起步价",
                        price: data.start_config.price + "元"
                    };
                    priceData.push(start_config);
                }
                if(data.price_per_kilometre){
                    let price_per_kilometre = {
                        type: 2,
                        text: "里程费",
                        price: data.price_per_kilometre + "元/公里"
                    };
                    priceData.push(price_per_kilometre);
                }
                if(data.price_per_minute){
                    let price_per_minute = {
                        type: 2,
                        text: "时长费",
                        price: data.price_per_minute + "元/分钟"
                    };
                    priceData.push(price_per_minute);
                }
                if(data.far_config){
                    let far_config = {
                        type: 2,
                        text: "远途费",
                        price: data.far_config.price_per_kilometre + "元/公里"
                    };
                    priceData.push(far_config);
                    $("#far-info-text").text("超出 "+ data.far_config.distance + " 公里后，加收远途费");
                }
                let template = document.getElementById('template-price-detail').innerHTML;
                document.getElementById('price-detail').innerHTML = doT.template( template )( priceData );

                $("#night-price").text(data.night_config.price_per_minute + "元/公里");
                $("#night-text").text("夜间（" + $.format(data.night_config.start_time, "hh:mm") + "-次日" + $.format(data.night_config.end_time, "hh:mm") + "），加收夜间服务费");

                $("#share-1").text("1人拼车按爱运快车价格的" + (parseFloat(data.share_config.config.one).toFixed(2)).slice(2,4)+ "%计费");
                $("#share-2").text("2人拼车按爱运快车价格的" + (parseFloat(data.share_config.config.two).toFixed(2)).slice(2,4)+ "%计费");
                $("#share-3").text("3人拼车按爱运快车价格的" + (parseFloat(data.share_config.config.three).toFixed(2)).slice(2,4)+ "%计费");
            } else {
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