$(function () {
    $.server = "http://192.168.1.88/";
    // $.server = "http://aiyunbaoapp.a56999.com/";
    var ip = "http://192.168.1.88/AppWeb/";
    // var ip = "http://aiyunbaoapp.a56999.com/AppWeb/";
    //前往支付页面
    $.pay = "http://192.168.1.88:8082/Bus/html/pay.html";
    // $.pay = "http://webapp.a56999.com/Bus/html/pay.html";
    //获取乘客行程信息
    $.getUserOrder = ip + "Ticket/getUserOrder";
    //预订购票
    $.addTicket = ip + "Ticket/addTicket";
    //添加乘客
    $.addPassenger = ip + "UserInfo/addPassenger";
    //获取添加的乘客列表
    $.getPassengerList = ip + "UserInfo/getPassengerList";
    //获取购票的大巴车信息
    $.getTicketInfo = ip + "Ticket/getTicketInfo";
    //提醒司机售票、乘客付款、乘客完成行程
    $.remindMessage = ip + "Ticket/remindMessage";
    //放弃购票或无票
    $.abandonPayTicket = ip + "Ticket/abandonPayTicket";
    //完成行程
    $.completeTicket = ip + "Ticket/completeTicket";
    //退票
    $.returnTicket = ip + "Ticket/returnTicket";
    //获取乘客购票信息
    $.getPassengerTicket = ip + "Ticket/getPassengerTicket";
    //同意购票
    $.sellTicket = ip + "Ticket/sellTicket";
    //获取当前车辆线路信息
    $.getBusesLineInfo = ip + "Ticket/getBusesLineInfo";
    //获取已预订车票的乘客列表
    $.lineTicket = ip + "Ticket/lineTicket";
    //获取账单信息
    $.unifiedOrder = ip + "OrderPay/getPayInfo2";
    //微信下单
    $.toWechatPay = ip + "OrderPay/unifiedOrder2";
    //支付宝下单
    $.toAlipay = ip + "Alipay/toAlipay2";


    //防止键盘把当前输入框给挡住
    $('input').on('click', function () {
        var target = this;
        setTimeout(function(){
            target.scrollIntoViewIfNeeded();
        },400);
    });

    //时间差格式化
    $.timeDifference = function (times) {
        var str = "0";
        if (times > 0) {
            //计算出相差天数
            var days=Math.floor(times/(24*3600));
            //计算出小时数
            var leave1=times%(24*3600);   //计算天数后剩余的毫秒数
            var hours=Math.floor(leave1/(3600));
            //计算相差分钟数
            var leave2=leave1%(3600);   //计算小时数后剩余的毫秒数
            var minutes=Math.floor(leave2/(60));
            //计算相差秒数
            var leave3=leave2%(60); //计算分钟数后剩余的毫秒数
            var seconds=Math.round(leave3);
            if (days > 0) {
                str = days+"天"+hours+"小时"+minutes+"分钟";
            } else {
                str = hours+"小时"+minutes+"分钟";
            }
        }
        return str;

    };

    //时间格式化 new Date().Format("yyyy-MM-dd hh:mm:ss")
    $.format = function(str, format){
        var date = new Date(str*1000);
        var o = {
            "M+" : date.getMonth()+1, //month
            "d+" : date.getDate(), //day
            "h+" : date.getHours(), //hour
            "m+" : date.getMinutes(), //minute
            "s+" : date.getSeconds(), //second
            "q+" : Math.floor((date.getMonth()+3)/3), //quarter
            "S" : date.getMilliseconds() //millisecond
        };
        if(/(y+)/.test(format)) {
            format = format.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
        }
        for(var k in o) {
            if(new RegExp("("+ k +")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
            }
        }
        return format;
    };

    //替换电话号码或者身份证号码  $.phone("13453051970")
    $.number = function (str) {
        if (str != null && str != "") {
            var string = "";
            if (str.length == 11) {
                string = str.substr(0, 3) + '****' + str.substr(7);
            } else if (str.length == 12) {
                string = str.substr(0, 3) + '****' + str.substr(8);
            } else if (str.length == 18) {
                string = str.substr(0, 4) + '**********' + str.substr(14);
            } else {
                string = str;
            }
            return string;
        } else {
            return ""
        }
    };

    //司机身份解析 1 司机， 2 售票员
    $.getEmployeeTypeStatus = function (employee_type) {
        if (employee_type != null && employee_type != "") {
            var type = "";
            switch (employee_type) {
                case "1":
                    type = "司机";
                    break;
                case "2":
                    type = "售票员";
                    break;
            }
            return type;
        } else {
            return ""
        }
    };

    //乘客身份格式解析 1 成人， 2 儿童
    $.getTypeStatus = function (user_type) {
        if (user_type != null && user_type != "") {
            var type = "";
            switch (user_type) {
                case "1":
                    type = "成人票";
                    break;
                case "2":
                    type = "儿童票";
                    break;
            }
            return type;
        } else {
            return ""
        }
    };

    //订单状态解析(1：已预定，2:售票[待支付]，3：支付[待检票]，4：已取消，5：退票中，6：已检票，7：已完成, 8：无票, 9：退票成功, 10：退票失败，-1：已过期)
    $.getOrderStatus = function (order_type) {
        if (order_type != null && order_type != "") {
            var type = "";
            switch (order_type) {
                case "1":
                    type = "已预定";
                    break;
                case "2":
                    type = "待支付";
                    break;
                case "3":
                    type = "待检票";
                    break;
                case "4":
                    type = "已取消";
                    break;
                case "5":
                    type = "退票中";
                    break;
                case "6":
                    type = "已检票";
                    break;
                case "7":
                    type = "已完成";
                    break;
                case "8":
                    type = "无票";
                    break;
                case "9":
                    type = "退票成功";
                    break;
                case "10":
                    type = "退票失败";
                    break;
                case "-1":
                    type = "已过期";
                    break;
            }
            return type;
        } else {
            return ""
        }
    };

    //获取几位成人几位儿童
    $.getPassengerCount = function (json) {
        var str = "";
        if (json != null && json != "") {
            if (json.adult > 0 && json.children > 0) {
                str = json.adult + "位成人、" + json.children + "位儿童"
            } else if (json.adult < 1 && json.children > 0) {
                str = json.children + "位儿童"
            } else if(json.adult > 0 && json.children < 1) {
                str = json.adult + "位成人"
            }
        }
        return str;
    };
});

