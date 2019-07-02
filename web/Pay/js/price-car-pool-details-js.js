/**
 * Created by keyC on 2018/8/20.
 */
var priceData = []; //价格详情

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
            // user_id: $.user_id,
            // order_no: "I48OCQ2S",
            user_id: param.user_id,
            order_no: param.order_no,
            type: "2"
        },
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res && res.status == 1){
                $(".main").removeClass("none");
                let data = res.data;
                $("#price").text(data.price);
                if(data.price && parseFloat(data.price) != 0.00){
                    let price = {
                        type: 2,
                        text: "一口价(" + data.num +"座)",
                        price: data.price + "元"
                    };
                    priceData.push(price);
                }
                if(data.award && parseFloat(data.award) != 0.00){
                    let extra = {
                        type: 1,
                        text: "奖励金额",
                        price: data.award + "元"
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