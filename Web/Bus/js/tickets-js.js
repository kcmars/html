var select_passenger_list;
$(function () {
    getRequest(getParams);
//        getParams();
});

//获取app传递过来的参数
function getParams() {
    console.log(param);
    loadAlertShow("加载中...");
    var params = {
        order_no: param.order_no   //购票订单号
    };
    console.log(params);
    $.ajax({
        type: 'POST',
        url: $.getUserOrder,
        data: params,
        contentType : "application/json" ,
        traditional:true,
        success: function (res) {
            console.log(res);
            console.log(params);
            loadAlertHide();
            if (res.status == 1) {
                var data = res.data;
                if (data) {
                    $("#passenger-time").text($.format(data.setout_time, "MM-dd hh:mm")+" 出发");
                    $("#passenger-start-address").text(data.start_name);
                    $("#passenger-end-address").text(data.end_name);
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


    //渲染选择的乘客列表
    var template=document.getElementById('template_passenger').innerHTML;
    select_passenger_list = localStorage.getItem("select_passenger_list");
    if (select_passenger_list != null){
        select_passenger_list = JSON.parse(select_passenger_list);
    }
    //传递一个有数据的数组进去
    document.getElementById('passenger-list').innerHTML = doT.template( template )( select_passenger_list );
    //显示完毕清除内容
    localStorage.clear();

    //添加乘客
    $(".tickets-content-add-id").bind("click",function(){
        location.href = './selectPassenger.html';
    });

    //预订购票
    $(".tickets-btn").bind("click",function(){
        var phone = $("#phone").val();
        var ride_user_id = [];
//            var noteInfo = $("#note-info").val();
        if (select_passenger_list == null || select_passenger_list.length < 1) {
            toastAlertShow("请至少添加一位乘客");
            return;
        } else {
            for (var i = 0; i<select_passenger_list.length; i++) {
                ride_user_id.push(select_passenger_list[i].id);
            }
        }
        if (phone == "") {
            toastAlertShow("请输入联系电话");
            return;
        }
        loadAlertShow("正在提交...");
//            var params = {
//                user_id: "TUp6SFVLTzBPME9R",    //购票人员id
//                order_no: "15275618271708608100",   //购票订单号
//                bus_line_id: "236",    //车辆线路id
//                ride_user_id: JSON.stringify(ride_user_id),   //乘车人员id
//                phone: phone,      //联系电话
//                remarks: ""   //行程备注
//            };
        var params = {
            user_id: param.user_id,    //购票人员id
            order_no: param.order_no,   //购票订单号
            bus_line_id: param.bus_line_id,    //车辆线路id
            ride_user_id: JSON.stringify(ride_user_id),   //乘车人员id
            phone: phone,      //联系电话
            remarks: ""   //行程备注
        };
        console.log(params);
        $.ajax({
            type: 'POST',
            url: $.addTicket,
            data: params,
            contentType : "application/json" ,
            traditional:true,
            success: function (res) {
                console.log(res);
                console.log(params);
                loadAlertHide();
                if (res.status == 1) {
                    location.href = './ticketDetails.html';
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

    //详情
    $(".tickets-head-info-detail").bind("click",function(){
        location.href = './refundInstructions.html';
    });
}

//删除选择的乘客
function remove(index) {
    select_passenger_list.splice(index, 1);
    localStorage.setItem("select_passenger_list", JSON.stringify(select_passenger_list));
    var template=document.getElementById('template_passenger').innerHTML;
    //传递一个有数据的数组进去
    document.getElementById('passenger-list').innerHTML = doT.template( template )( select_passenger_list );
}
