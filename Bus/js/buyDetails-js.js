//    var obj = [
//        {
//            "start_name" : "成都站",
//            "start_latitude" : "30.656327",
//            "start_longitude" : "104.105747",
//            "end_name" : "图腾·印象酒店(体院店)",
//            "end_latitude" : "30.690386",
//            "end_longitude" : "104.041013",
//        },
//        {
//            "start_district": "武侯区",
//            "start_city": "成都市",
//            "start_province": "四川省",
//            "start_address": "一环路西一段19号",
//            "start_name": "成都体育学院",
//            "start_latitude": "30.646981",
//            "start_longitude": "104.044053",
//            "end_district": "武侯区",
//            "end_city": "成都市",
//            "end_address": "致民东路6号附9号",
//            "end_name": "成都市工商行政管理局",
//            "end_latitude": "30.639409",
//            "end_longitude": "104.087207",
//            "add_time": "1508125261",
//            "pass_point": [{"prov":"四川省", "city":"成都市", "dist": "成华区", "name": "新华公园", "lat": "30.690386", "lon": "104.041013"}],
//        }
//    ];
var mDataBus;
var mDataPassenger = [];
var page = 1;
var mInfo = [];

$(function () {
    getRequest(getParams);
       // getParams();
});

//获取app传递过来的参数
function getParams() {
    console.log(param);
    //获取当前车辆线路信息
    loadAlertShow("请稍等...");
       // var params = {
       //     line_id: "314"
       // };
    var params = {
        line_id: param.bus_line_id
    };
    $.ajax({
        type: 'POST',
        url: $.getBusesLineInfo,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if (res.status == 1) {
                mDataBus = res.data;
                $("#bus-start-time").text(mDataBus.setout_time+" 出发");
                $("#bus-start-address").text(mDataBus.start_name);
                $("#bus-end-address").text(mDataBus.end_name);
                $("#bus-plate").text(mDataBus.car_plate);
                if(mDataBus.seats != "") {
                    $("#bus-seats-num").show();
                    $("#bus-eat-num").text(mDataBus.seats + "座");
                } else {
                    $("#bus-seats-num").hide();
                }
                var busInfo = {
                    "start_province": mDataBus.start_province,
                    "start_city": mDataBus.start_city,
                    "start_district": mDataBus.start_district,
                    "start_name": mDataBus.start_name,
                    "end_province": mDataBus.end_province,
                    "end_city": mDataBus.end_city,
                    "end_district": mDataBus.end_district,
                    "end_name": mDataBus.end_name,
                    "start_lat": mDataBus.start_lat,
                    "start_lon": mDataBus.start_lon,
                    "end_lat": mDataBus.end_lat,
                    "end_lon": mDataBus.end_lon,
                    "pass_point": mDataBus.pass_point != "" && mDataBus.pass_point.length > 0 ? mDataBus.pass_point : null
                };
                mInfo.push(busInfo);
            } else {
                toastAlertShow(res.msg);
            }
        },
        error: function (err) {
            console.log(err);
            loadAlertHide();
        }
    });

    //添加下拉刷新和上拉加载功能
    var listloading = new Listloading('#listloading', {
        disableTime: true,  // 是否需要显示时间
        pullUpAction : function(cb){   // 上拉加载更多
            var flg = false;
            getPassengerList("loadMore", cb, flg);
        },
        pullDownAction : function(cb, flg){  // 下拉刷新
            getPassengerList("refresh", cb, flg);
        },
        Realtimetxt: '松开刷新',
        loaderendtxt: '已经到底了',
        iscrollOptions: {
            scrollbars: false   // 显示iscroll滚动条
        }
    });

    //途径点
    $(".pass-point").bind("click",function(){
        $("#pop-content").addClass("pop-content-l-5").removeClass("pop-content-l-33");
        if (mDataBus != null && mDataBus.pass_point != "" &&  mDataBus.pass_point.length > 0) {
            var template=document.getElementById('template_pop-list').innerHTML;
            //传递一个有数据的数组进去
            document.getElementById('pop-list').innerHTML = doT.template(template)(mDataBus.pass_point);
            $(".pop-box").show();
        } else {
            toastAlertShow("该线路暂无途径点信息")
        }
    });

    //随车人员
    $(".flow-user").bind("click",function(){
        $("#pop-content").addClass("pop-content-l-33").removeClass("pop-content-l-5");
        if (mDataBus != null && mDataBus.follow_user != "" &&  mDataBus.follow_user.length > 0) {
            var template=document.getElementById('template_pop-list').innerHTML;
            //传递一个有数据的数组进去
            document.getElementById('pop-list').innerHTML = doT.template(template)(mDataBus.follow_user);
            $(".pop-box").show();
        } else {
            toastAlertShow("该线路暂无随车人员")
        }
    });

    $(".pop-box, #pop-content>img").bind("click",function(){
        $(".pop-box").hide();
    });
}

//获取已购票乘客的车票信息
function getPassengerList(type, cb, flg){
    if (type == "refresh") {
        mDataPassenger = [];
        page = 1;
    } else {
        page++;
    }
    // var params = {
    //     line_id: "314",
    //     page: page
    // };
    var params = {
        line_id: param.bus_line_id,
        page: page
    };
    $.ajax({
        type: 'POST',
        url: $.lineTicket,
        data: params,
        success: function (res) {
            console.log(page);
            console.log(res);
            if (res.status == 1) {
                var data = res.data;
                for (var key in data) {
                    mDataPassenger.push(data[key]);
                }
                //渲染购票的乘客信息列表
                var template = document.getElementById('template-tickets-passenger-list').innerHTML;
                if (type == "refresh") {
                    $("#tickets-passenger-list").empty();
                    $('#tickets-passenger-list').append(doT.template(template)(data));
                } else {
                    if(mDataPassenger.length!=10){
                        flg = true;
                    }
                    $('#tickets-passenger-list').append(doT.template(template)(data));
                }
            } else {
                if (type == "refresh") {
                    $("#tickets-passenger-list").empty();
                } else {
                    flg = true;
                    page--;
                }
            }
            cb(flg);
        },
        error: function (err) {
            console.log(err);
        }
    });
}

//拨打乘客电话
function callPassengerPhone(tel) {
    openActivity("bus", "tel", tel);
}

//查看乘客上车位置
function lookPassengerLocation(i) {
    console.log(i);
    var data = mDataPassenger[i];
    console.log(data);
    var passengerInfo = {
        "start_name" : data.start_name,
        "start_latitude" : data.start_latitude,
        "start_longitude" : data.start_longitude,
        "end_name" : data.end_name,
        "end_latitude" : data.end_latitude,
        "end_longitude" : data.end_longitude
    };
    mInfo.push(passengerInfo);
    openActivity("bus", "location", JSON.stringify(mInfo));
}

//列表按钮点击事件
function btnClick(i) {
    var data = mDataPassenger[i];
    var mHtml = "";
    if (data != null && data != "") {
        switch (data.status) {
            case "2": //待支付,提醒乘客付款
                mHtml += '<div class="my-alert-html bottom-border"><div class="my-alert-html-left">';
                mHtml += '<span>'+$.format(data.setout_time, "MM-dd")+'</span>';
                mHtml += '<span>'+$.format(data.setout_time, "hh:mm")+'</span>';
                mHtml += '</div><div class="my-alert-html-right"><div class="my-alert-html-right-column"><img class="icon" src="../img/icon_start.png"/>';
                mHtml += '<span>'+ data.start_name +'</span>';
                mHtml += '</div><div class="my-alert-html-right-column"><img class="icon" src="../img/icon_end.png"/>';
                mHtml += '<span>'+ data.end_name +'</span>';
                mHtml += '</div><div class="my-alert-html-right-column"><img src="../img/icon-person.png">';
                mHtml += '<span>'+ $.getPassengerCount(data.passenger_count) +'</span>';
                mHtml += '</div></div></div><div class="my-alert-edit-money"><img src="../img/icon_money.png"/>';
                mHtml += '<span>'+ data.price + " 元" +'</span>';
                mHtml += '</div>';
                towBtnAlert('提醒乘客付款', mHtml, '',function(r, str){
                    if(r){
                        //...点确定之后执行的内容
                        //提醒乘客付款
                        remindMessage(data.id, "driver");
                    } else {
                        if (!str) {
                            //放弃售票
                            abandonPayTicket(data.id, "nothing");
                        }
                    }
                }, '已无票', '提醒乘客付款');
                break;

            case "3": //乘客已付款,待检票
                mHtml += '<div class="my-alert-qr-html"><div class="my-alert-qr-content bottom-border"><div class="my-alert-qr-html-left"><div class="my-alert-qr-html-row"><img src="../img/icon_time.png">';
                mHtml += '<span class="text">'+$.format(data.setout_time, "MM-dd hh:mm")+'</span>';
                mHtml += '</div><div class="my-alert-qr-html-row"><img class="icon" src="../img/icon_start.png"/>';
                mHtml += '<span class="text">'+ data.start_name +'</span>';
                mHtml += '</div><div class="my-alert-qr-html-row"><img class="icon" src="../img/icon_end.png"/>';
                mHtml += '<span class="text">'+ data.end_name +'</span>';
                mHtml += '</div><div class="my-alert-qr-html-row"><img src="../img/icon-person.png">';
                mHtml += '<span class="text">'+ $.getPassengerCount(data.passenger_count) +'</span>';
                mHtml += '</div></div><div class="my-alert-qr-html-right"><img src="../img/icon_money.png">';
                mHtml += '<span class="money">'+ data.price + " 元" +'</span>';
                mHtml += '</div></div>';
                mHtml += '</div>';

                towBtnAlert('乘客已付款', mHtml,
                    '1.乘客已支付本次行程费用至爱运宝平台，请在出发时，提醒乘客提供爱运宝为乘客生成的“<span class="blue-color">车票二维码</span>”,' +
                    '待司机或售票员扫码成功，即可完成扫码验票上车。（扫码验票也可在司机界面中的“我-扫码验票”点击去扫码）。<br/>' +
                    '2.若司机或售票员遇到验票过程中出现信号不稳定、网络连接不佳等造成无法完成扫码验票时，可与乘客协商，提醒乘客在' +
                    '到达目的地后点击“完成行程”即可，行程费用将会在2小时内转至司机钱包。'
                    ,function(r, str){
                        if(r){
                            //...点确定之后执行的内容
                            //调用原生扫描二维码
                            openScanningActivity();
                        } else {
                            if (!str) {
                                //提醒乘客完成行程
                                remindMessage(data.id, "complete");
                            }
                        }
                    }, '提醒乘客完成行程', '扫码验票<img class="scan" src="../img/icon-scan.png"/>');
                break;

            case "6": //已检票
                mHtml += '<div class="my-alert-check-in-html">恭喜您，已扫码验票成功！</div>';
                oneBtnAlert('已验票', mHtml,
                    '请在送达乘客至目的地后，提醒乘客点击“完成行程”即可完成本次行程，系统会在2小时以内将行程费用转至司机钱包。<br/>' +
                    '若行程结束后，乘客忘记点击“完成行程”，系统将以出发时间为计时的2天后，自动默认为乘客已到达目的地，' +
                    '届时将自动结束本次行程并将行程费用自动转至司机钱包。'
                    ,function(r){
                        if(r){
                            //...点确定之后执行的内容
                            //提醒乘客完成行程
                            remindMessage(data.id, "complete");
                        }
                    }, '提醒乘客完成行程');
                break;
        }
    } else {
        toastAlertShow("数据异常");
    }
}

//提醒乘客
function remindMessage(id, type) {
    loadAlertShow("请稍等...");
    var params = {
        ticket_id: id,
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
function abandonPayTicket(id, type) {
    loadAlertShow("请稍等...");
    var params = {
        id: id,
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
