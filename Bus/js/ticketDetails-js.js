//    var obj = [{
//        "bus_type": "",
//        "plate": null,
//        "bus_tel": [{
//            "tel": "134530051970"
//        },{
//            "tel": "028-85341778"
//        },{
//            "tel": "0282-85341778"
//        }],
//        "allow_num": "",
//        "setout_time": "15:10",
//        "start_province": "四川省",
//        "start_city": "成都市",
//        "start_district": "武侯区",
//        "start_name": "石羊场车站",
//        "end_province": "四川省",
//        "end_city": "眉山市",
//        "end_district": "",
//        "end_name": "眉山市",
//        "start_lat": "30.585780",
//        "start_lon": "104.032170",
//        "end_lat": "30.048320",
//        "end_lon": "103.831790",
//        "pass_point": null
//    }];
var mDataInfo;
var mInfo = [];
var flag = false;

$(function () {
    getRequest(getParams);
       // getParams();
});

//获取传递的参数
function getParams() {
    loadAlertShow("加载中...");
    //获取购票的大巴车信息AppWeb/Ticket/getTicketInfo
    //    var params = {
    //        order_no: "15278383815915314999"
    //    };
    var params = {
        order_no: param.order_no
    };
    $.ajax({
        type: 'POST',
        url: $.getTicketInfo,
        data: params,
        success: function (res) {
            console.log(params);
            console.log(res);
            loadAlertHide();
            if (res.status == 1) {
                mDataInfo = res.data;
                $("#passenger-time").text($.format(mDataInfo.setout_time, "MM-dd hh:mm")+" 出发");
                $("#passenger-start-address").text(mDataInfo.start_name);
                $("#passenger-end-address").text(mDataInfo.end_name);
                $("#bus-type").text(mDataInfo.car_type);
                $("#bus-time").text(mDataInfo.l_setout_time);
                $("#bus-start-address").text(mDataInfo.l_start_name);
                $("#bus-end-address").text(mDataInfo.l_end_name);
                $("#bus-status-btn").text($.getOrderStatus(mDataInfo.t_status));
                if (mDataInfo.line_pint != "" && mDataInfo.line_pint.length > 0) {
                    $(".pass-point").show();
                    var obj = mDataInfo.line_pint;
                    var template=document.getElementById('template-pass-point-list').innerHTML;
                    //传递一个有数据的数组进去
                    document.getElementById('pass-point').innerHTML = doT.template( template )( obj );
                    if (flag) {
                        $("#pass-point").show();
                    } else {
                        $("#pass-point").hide();
                    }
                }
                var busList = {
                    "bus_type": mDataInfo.car_type,
                    "plate": mDataInfo.car_plate,
                    "bus_tel": mDataInfo.bus_tel,
                    "start_province": mDataInfo.l_start_province,
                    "start_city": mDataInfo.l_start_city,
                    "start_district": mDataInfo.l_start_district,
                    "start_name": mDataInfo.l_start_name,
                    "end_province": mDataInfo.l_end_province,
                    "end_city": mDataInfo.l_end_city,
                    "end_district": mDataInfo.l_end_district,
                    "end_name": mDataInfo.l_end_name,
                    "start_lat": mDataInfo.l_start_lat,
                    "start_lon": mDataInfo.l_start_lon,
                    "end_lat": mDataInfo.l_end_lat,
                    "end_lon": mDataInfo.l_end_lon,
                    "pass_point": mDataInfo.line_pint != "" && mDataInfo.line_pint.length > 0 ? mDataInfo.line_pint : null
                };
                mInfo.push(busList);
            } else {
                toastAlertShow(res.msg);
            }
        },
        error: function (err) {
            console.log(err);
            loadAlertHide();
        }
    });

    //拨打电话
    $("#phone").bind("click",function(){
        if (mInfo.length > 0) {
            openActivity("passenger", "tel", JSON.stringify(mInfo));
        } else {
            toastAlertShow("数据丢失，无法拨打电话")
        }
    });

    //查看定位
    $("#location").bind("click",function(){
        if (mInfo.length > 0) {
            openActivity("passenger", "location", JSON.stringify(mInfo));
        } else {
            toastAlertShow("数据丢失，无法查看定位")
        }
    });

    //按钮
    $(".item-footer-right-btn").bind("click",function(){
        var mHtml = "";
        switch (mDataInfo.t_status) {
            case "1"://已预定
                mHtml += '<div class="my-alert-html bottom-border"><div class="my-alert-html-left">';
                mHtml += '<span>'+$.format(mDataInfo.setout_time, "MM-dd")+'</span>';
                mHtml += '<span>'+mDataInfo.l_setout_time+'</span>';
                mHtml += '</div><div class="my-alert-html-right"><div class="my-alert-html-right-column"><img src="../img/icon_start.png" class="icon"/>';
                mHtml += '<span class="text">'+mDataInfo.l_start_name+'</span>';
                mHtml += '</div><div class="my-alert-html-right-column"><img src="../img/icon_end.png" class="icon"/>';
                mHtml += '<span class="text">'+mDataInfo.l_end_name+'</span>';
                mHtml += '</div><div class="my-alert-html-right-column"><img src="../img/icon_bus.png">';
                mHtml += '<span class="text">'+mDataInfo.car_type+'</span>';
                mHtml += '</div></div></div>';
                towBtnAlert('已预订', mHtml, '车票已预订成功，正在等待司机或售票员确认，可主动联系司机或售票员，请求确认售票',function(r, str){
                    if(r){
                        //...点确定之后执行的内容
                        //提醒司机售票
                        remindMessage(mDataInfo.t_id, "passenger");
                    } else {
                        if (!str) {
                            //放弃购票
                            abandonPayTicket(mDataInfo.t_id, "abandon");
                        }
                    }
                }, '放弃购票', '提醒售票');
                break;

            case "2"://待支付
                //购票时间
                var timeSell = parseInt(mDataInfo.t_sell_time) + 1800;
                //发车时间
                var timeStart = parseInt(mDataInfo.set_out_time);
                //当前时间
                var timeNow = new Date().getTime()/1000;
                //购票剩余时间
                var time = timeSell - parseInt(timeNow);
                //距离上车时间
                var timeDistance = timeStart - parseInt(timeNow);
                var timer = setInterval(function () {
                    if (time > 0) {
                        time -= 1;
                        document.getElementById("timer").innerHTML = $.format(time-1, "mm:ss");
                    } else {
                        clearInterval(timer);
                        document.getElementById("timer").innerHTML = "已超时";
                    }
                }, 1000);

                //支付
                mHtml += '<div class="my-alert-html bottom-border"><div class="my-alert-html-pay-left"><img src="../img/icon_money.png">';
                mHtml += '<span class="money">'+ mDataInfo.t_price +'</span>';
                mHtml += '</div><div class="my-alert-html-pay-right"><span>支付计时：</span>';
                mHtml += '<span id="timer" class="timer">--:--</span>';
                mHtml += '</div></div>';

                if (time > 0) {
                    towBtnAlert('去支付', mHtml,
                        '1.司机或售票员已同意本次购票，请在提示支付时间内完成支付，否则系统会自动取消本次购票。<br/>' +
                        '2.距离行程发车时间还剩大约<span class="blue-color">'+$.timeDifference(timeDistance)+'</span>，' +
                        '请确保有足够的时间抵达上车地点，并及时验票、上车等，以免耽误您的行程'
                        ,function(r, str){
                            if(r){
                                //...点确定之后执行的内容
                                //去支付
                                openPayActivity($.pay, mDataInfo.order_no);
                            } else {
                                if(!str) {
                                    //放弃购票
                                    abandonPayTicket(mDataInfo.t_id, "abandon");
                                }
                            }
                            clearInterval(timer);
                        }, '放弃购票', '去支付', '#FF8C00');
                } else {
                    oneBtnAlert('支付超时', mHtml,
                        '1.司机或售票员已同意本次购票，请在提示支付时间内完成支付，否则系统会自动取消本次购票。<br/>' +
                        '2.距离行程发车时间还剩大约<span class="blue-color">'+$.timeDifference(timeDistance)+'</span>，' +
                        '请确保有足够的时间抵达上车地点，并及时验票、上车等，以免耽误您的行程'
                        ,function(r){
                            if(r){
                                //...点确定之后执行的内容
                                //放弃购票
                                abandonPayTicket(mDataInfo.t_id, "abandon");
                            }
                        }, '放弃购票');
                }
                break;

            case "3"://待检票
                mHtml += '<div class="my-alert-qr-html"><div class="my-alert-qr-content bottom-border"><div class="my-alert-qr-html-left"><div class="my-alert-qr-html-row"><img src="../img/icon_time.png">';
                mHtml += '<span class="text">'+$.format(mDataInfo.setout_time, "MM-dd hh:mm")+'</span>';
                mHtml += '</div><div class="my-alert-qr-html-row"><img src="../img/icon_start.png" class="icon"/>';
                mHtml += '<span class="text">'+mDataInfo.start_name+'</span>';
                mHtml += '</div><div class="my-alert-qr-html-row"><img src="../img/icon_end.png" class="icon"/>';
                mHtml += '<span class="text">'+mDataInfo.end_name+'</span>';
                mHtml += '</div><div class="my-alert-qr-html-row"><img src="../img/icon-person.png">';
                mHtml += '<span class="text">'+ $.getPassengerCount(mDataInfo.passenger_count) +'</span>';
                mHtml += '</div></div><div class="my-alert-qr-html-right"><img src="../img/icon_money.png">';
                mHtml += '<span class="money">'+ mDataInfo.t_price +" 元"+'</span>';
                mHtml += '</div></div>';
                mHtml += '<img class="my-qr" src="'+ $.server + mDataInfo.t_ticket_code +'"></div>';

                towBtnAlert('已付款', mHtml, '1.司机或售票员已同意本次购票，请在提示支付时间内完成支付，否则系统会自动取消本次购票。<br/>' +
                    '2.距离行程发车时间还剩大约<span class="blue-color">6小时30分</span>，请确保有足够的时间抵达上车地点，并及时验票、上车等，以免耽误您的行程'
                    ,function(r, str){
                        if(r){
                            //...点确定之后执行的内容
                            //完成行程
                            completeTicket(mDataInfo.t_id);
                        } else {
                            if (!str) {
                                //退票
                                returnTicket(mDataInfo.t_id);
                            }
                        }
                    }, '退票', '完成行程');
                break;

            case "6"://已检票
                mHtml += '<div class="my-alert-check-in-html">恭喜您，已扫码验票成功！</div>';
                oneBtnAlert('已验票', mHtml,
                    '请在到达目的地后，点击“完成行程”即可完成本次行程（完成行程也可在乘客界面中点击“我-行程-客运大巴”' +
                    '页面中的“完成”即可完成本次行程）<br/>'+
                    '若行程结束后，乘客忘记点击“完成行程”，系统将以出发时间为计时的2天后，自动默认为乘客已到达目的地。届时将结束本次行程。'
                    ,function(r){
                        if(r){
                            //...点确定之后执行的内容
                            //完成行程
                            completeTicket(mDataInfo.t_id);
                        }
                    }, '完成行程');
                break;
        }
    });

    //查看途径点
    $(".pass-point").bind("click",function(){
        if (flag) {
            $("#pass-point").hide();
            flag = false;
        } else {
            $("#pass-point").show();
            flag = true;
        }
        // if ($("#pass-point").is(":visible") === false) {
        //     $("#pass-point").show();
        //     console.log("true");
        // } else {
        //     $("#pass-point").hide();
        //     console.log("false");
        // }
    });

    //查看乘客
    $(".passenger").bind("click",function(){
        $("#pop-content").addClass("pop-content-l-5").removeClass("pop-content-l-33");
        var obj = mDataInfo.ride_user_name;
        var template=document.getElementById('template_pop-list').innerHTML;
        //传递一个有数据的数组进去
        document.getElementById('pop-list').innerHTML = doT.template( template )( obj );
        $(".pop-box").show();
    });

    //查看联系电话
    $(".phone").bind("click",function(){
        $("#pop-content").addClass("pop-content-l-33").removeClass("pop-content-l-5");
        var obj = [];
        var phone = {
            phone: mDataInfo.t_phone
        };
        obj.push(phone);
        var template=document.getElementById('template_pop-list').innerHTML;
        //传递一个有数据的数组进去
        document.getElementById('pop-list').innerHTML = doT.template( template )( obj );
        $(".pop-box").show();
    });

    //关闭pop层
    $(".pop-box, #pop-content>img").bind("click",function(){
        $(".pop-box").hide();
    });
}

//提醒
function remindMessage(t_id, type) {
    loadAlertShow("请稍等...");
    var params = {
        ticket_id: t_id,
        remind_type: type
    };
    $.ajax({
        type: 'POST',
        url: $.remindMessage,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if (res.status == 1) {
                toastAlertShow(res.msg);
            } else {
                toastAlertShow(res.msg);
            }
        },
        error: function (err) {
            console.log(err);
            loadAlertHide();
        }
    });
}

//放弃购票或无票
function abandonPayTicket(t_id, type) {
    loadAlertShow("请稍等...");
    var params = {
        id: t_id,
        type: type
    };
    $.ajax({
        type: 'POST',
        url: $.abandonPayTicket,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if (res.status == 1) {
                toastAlertShow(res.msg);
                window.location.reload();
                refreshApp();
            } else {
                toastAlertShow(res.msg);
            }
        },
        error: function (err) {
            console.log(err);
            loadAlertHide();
        }
    });
}

//完成行程
function completeTicket(id) {
    loadAlertShow("请稍等...");
    var params = {
        ticket_id: id
    };
    $.ajax({
        type: 'POST',
        url: $.completeTicket,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if (res.status == 1) {
                toastAlertShow(res.msg);
                window.location.reload();
                refreshApp();
            } else {
                toastAlertShow(res.msg);
            }
        },
        error: function (err) {
            console.log(err);
            loadAlertHide();
        }
    });
}

//退票
function returnTicket(id) {
    loadAlertShow("请稍等...");
    var params = {
        ticket_id: id
    };
    $.ajax({
        type: 'POST',
        url: $.returnTicket,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if (res.status == 1) {
                toastAlertShow(res.msg);
                window.location.reload();
                refreshApp();
            } else {
                toastAlertShow(res.msg);
            }
        },
        error: function (err) {
            console.log(err);
            loadAlertHide();
        }
    });
}
