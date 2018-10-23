var mDataInfo;
$(function () {
    getRequest(getParams);
//        getParams();
});

//获取app传递过来的参数
function getParams() {
    console.log(param);
    loadAlertShow("加载中...");
    //获取乘客购票信息
//        var params = {
//            ticket_id: "12"
//        };
    var params = {
        ticket_id: param.ticket_id
    };
    $.ajax({
        type: 'POST',
        url: $.getPassengerTicket,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if (res.status == 1) {
                mDataInfo = res.data;
                var order = mDataInfo.order;
                $("#passenger-start-time").text($.format(order.setout_time, "MM-dd hh:mm")+" 出发");
                $("#passenger-start-address").text(order.start_name);
                $("#passenger-end-address").text(order.end_name);
                $("#passenger-phone").text(mDataInfo.phone);
                //渲染购票的乘客列表
                var template = document.getElementById('template-passenger-tickets').innerHTML;
                //传递一个有数据的数组进去
                document.getElementById('passenger-tickets-list').innerHTML = doT.template(template)(mDataInfo.passengerUser);
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

    //同意购票
    $(".buy-tickets-btn-right").bind("click",function(){
        var money = $("#money").val();
        if (money == "") {
            toastAlertShow("请输入车票费用");
            return;
        }
//            var params = {
//                price: money,
//                ticket_id: "12"
//            };
        var params = {
            price: money,
            ticket_id: param.ticket_id
        };
        loadAlertShow("请稍等...");
        $.ajax({
            type: 'POST',
            url: $.sellTicket,
            data: params,
            success: function (res) {
                console.log(res);
                loadAlertHide();
                if (res.status == 1) {
                    location.href = './buyDetails.html';
                    refreshApp();
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
    });

    //无票
    $(".buy-tickets-btn-left").bind("click",function(){
//            var params = {
//                price: money,
//                ticket_id: "12"
//            };
        var params = {
            ticket_id: param.ticket_id
        };
        loadAlertShow("请稍等...");
        $.ajax({
            type: 'POST',
            url: $.abandonPayTicket,
            data: params,
            success: function (res) {
                console.log(res);
                loadAlertHide();
                if (res.status == 1) {
                    closeWebview();
                    if (window.history.length > 0) {
                        window.history.reload();
                        refreshApp();
                    }
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
    });
}
