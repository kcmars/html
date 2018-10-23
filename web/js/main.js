$(function () {
    /**
     * 大巴车售票功能接口
     */
    // $.server = "http://192.168.1.88/";
    $.server = "http://aiyunbaoapp.a56999.com/";
    // var ip = "http://192.168.1.88/AppWeb/";
    var ip = "http://aiyunbaoapp.a56999.com/AppWeb/";
    //前往支付页面
    // $.pay = "http://192.168.1.88:8082/Bus/html/pay.html";
    $.pay = "http://webapp.a56999.com/Bus/html/pay.html";
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
    //微信下单
    $.toWechatPay = ip + "OrderPay/unifiedOrder2";
    //支付宝下单
    $.toAlipay = ip + "Alipay/toAlipay2";

    /**
     * 分享接口地址
     */
    $.server1 = "http://bb4b3408.ngrok.io/";
    $.server2 = "http://118.190.203.67:9501/";
    var ip1 = $.server2 + "Web/";
    //获取我的分享二维码
    $.getShareQR = ip1 + "Share/getQRCode";
    //获取我的分享信息
    $.getShareContent = ip1 + "Share/getShareContent";
    //获取我的推荐人数
    $.getRecommendDetail = ip1 + "Share/getRecommendDetail";

    /**
     * 认证接口地址
     */
    //上传认证图片
    $.uploadPicture = ip1 + "Authentication/upload";
    //获取乘客实名认证
    $.getPassengerRealNameInfo = ip1 + "Authentication/getPassengerRealNameInfo";
    //提交乘客实名认证
    $.submitPassengerRealNameInfo = ip1 + "Authentication/passenger";
    //获取司机快车认证信息
    $.getExpressDriverInfo = ip1 + "Authentication/getExpressDriverInfo";
    //提交司机快车认证
    $.submitExpressDriver = ip1 + "Authentication/expressDriver";
    //获取司机快车认证信息
    $.getTaxiDriverInfo = ip1 + "Authentication/getTaxiDriverInfo";
    //提交司机快车认证
    $.submitTaxiDriver = ip1 + "Authentication/taxiDriver";
    //获取大巴车管理者认证
    $.getBusManagerInfo = ip1 + "Authentication/getBusManagerInfo";
    //提交大巴车管理者认证
    $.submitBusManager = ip1 + "Authentication/busManager";
    //获取大巴车认证
    $.getBusInfo = ip1 + "Driver/getBusInfo";
    //提交大巴车认证
    $.submitBusInfo = ip1 + "Driver/addBus";
    //获取大巴车类型
    $.getBusType = ip1 + "Common/getBusType";
    //获取大巴车雇员认证
    $.getBusEmployees = ip1 + "Driver/getEmployeeInfo";
    //获取大巴车雇员信息
    $.getEmployeesInfo = ip1 + "Driver/getBusEmployeeInfo";
    //提交大巴车雇员类型
    $.submitBusEmployees = ip1 + "Driver/addEmployee";

    /**
     * 支付相关接口
     */
    //获取账单信息
    $.getPayInfo = ip1 + "Pay/getPayInfo";
    //下单
    $.unifiedOrder = ip1 + "Pay/unifiedOrder";
    //获取预估价格详情
    $.getDetailEstimateInfo = ip1 + "Pay/getDetailEstimateInfo";
    //乘客端获取行程账单详情
    $.getDetailPayInfo = ip1 + "Pay/getDetailPayInfo";
    //司机端获取行程账单详情
    $.getDetailPayInfoOfDriver = ip1 + "Pay/getDetailPayInfoOfDriver";
    //获取价格配置详情
    $.getPriceConfig = ip1 + "Pay/getPriceConfig";
    //获取钱包消息详情
    $.getPropertyDetail = ip1 + "Common/getPropertyDetail";


    /**
     * 评价相关接口
     */
    //获取评价信息
    $.getRatingInfo = ip1 + "Judge/getRatingInfo";
    //提交评价信息
    $.submitRating = ip1 + "Judge/submitRating";
    //获取取消理由
    $.getCancelReason = ip1 + "Common/getCancelReason";
    //提交取消理由
    $.submitReason = ip1 + "Common/submitReason";

    /**
     * 地址相关接口
     */
    //获取省
    $.getProvinces = ip1 + "Common/getProvinces";
    //获取城市
    $.getCities = ip1 + "Common/getCities";
    //获取区县
    $.getAds = ip1 + "Common/getAds";

    $.plateShortList = ["京", "津", "沪", "渝", "黑", "吉", "辽", "甘", "陕", "贵", "云", "川", "晋", "冀", "青",
        "鲁", "豫", "苏", "皖", "浙", "闽", "赣", "湘", "鄂", "粤", "琼", "蒙", "新", "藏", "宁", "桂", "港", "澳"];
    $.color = [{color:"white", name: "白色"},
        {color:"red", name: "红色"},
        {color:"black", name: "黑色"},
        {color:"silver", name: "银色"},
        {color:"champagne", name: "香槟色"},
        {color:"gray", name: "灰色"},
        {color:"blue", name: "蓝色"},
        {color:"yellow", name: "黄色"},
        {color:"green", name: "绿色"},
        {color:"coffee", name: "咖啡色"},
        {color:"orange", name: "橙色"},
        {color:"other", name: "其他"}];

    //防止键盘把当前输入框给挡住
    $('input').on('click', function () {
        var target = this;
        setTimeout(function(){
            target.scrollIntoViewIfNeeded();
        },400);
    });

    $.getLocationParams = function (key) {
        let reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
        let r = encodeURI(window.location.search).substr(1).match(reg);
        if (r != null) {
            return decodeURI(unescape(r[2]));
        }
        return null;
    };

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

    //补全两位小数
    $.formatTwoDecimal = function (number) {
        if (isNaN(parseFloat(number))) {
            return false;
        }
        let mNumber = Math.round(number * 100) / 100;
        let s_x = mNumber.toString();
        let pos_decimal = s_x.indexOf('.');
        if (pos_decimal < 0) {
            pos_decimal = s_x.length;
            s_x += '.';
        }
        while (s_x.length <= pos_decimal + 2) {
            s_x += '0';
        }
        return s_x;
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

    //消息类型格式解析
    $.getNewsType = function (type) {
        if (type != null && type != "") {
            var str = "";
            switch (type) {
                case 1:
                    str = "首单奖励";
                    break;
                case 2:
                    str = "共享收益";
                    break;
                case 3:
                    str = "承包商分红奖励";
                    break;
                case 4:
                    str = "系统抽成";
                    break;
                case 5:
                    str = "行程收益";
                    break;
                case 6:
                    str = "提现";
                    break;
            }
            return str;
        } else {
            return ""
        }
    };
    //消息状态格式解析
    $.getNewsStatus = function (status) {
        let str = "";
        switch (status) {
            case 0:
                str = "审核中";
                break;
            case 1:
                str = "提现成功";
                break;
            case -1:
                str = "提现失败";
                break;
        }
        return str;
    };

    //获取提现类型格式解析
    $.getWealthChannel = function (channel) {
        let str = "";
        switch (channel) {
            case "1":
                str = "微信";
                break;
            case "2":
                str = "支付宝";
                break;
            case "3":
                str = "农业银行";
                break;
            case "4":
                str = "建设银行";
                break;
            case "5":
                str = "平安银行";
                break;
            case "6":
                str = "招商银行";
                break;
            case "7":
                str = "中国银行";
                break;
            case "8":
                str = "工商银行";
                break;
            case "9":
                str = "中国邮政储蓄银行";
                break;
        }
        return str;
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

    //判断提交的参数是否完整
    $.detectionParam = function (json) {
        let str = "";
        if(json != null && json != ""){
            for (let key in json) {
                if(json[key] == ""){
                    str = $.authenInfoDetection(key);
                    break;
                }
            }
        }
        return str;
    };

    //认证信息提交检查
    $.authenInfoDetection = function (text) {
        let str = "";
        switch (text) {
            case "pro_code":
                str = "请选择注册省份";
                break;
            case "pro_name":
                str = "请选择注册省份";
                break;
            case "city_code":
                str = "请选择注册城市";
                break;
            case "city_name":
                str = "请选择注册城市";
                break;
            case "ad_code":
                str = "请选择注册城区";
                break;
            case "ad_name":
                str = "请选择注册城区";
                break;
            case "company_name":
                str = "请输入公司全称";
                break;
            case "name":
                str = "请输入姓名";
                break;
            case "ID":
                str = "请输入身份证号码";
                break;
            case "image_id_a":
                str = "请上传身份证正面照";
                break;
            case "image_id_b":
                str = "请上传身份证背面照";
                break;
            case "image_drivers":
                str = "请上传驾驶证照";
                break;
            case "first_issue":
                str = "请选择首次领驾驶证日期";
                break;
            case "brand":
                str = "请选择注册车辆";
                break;
            case "model":
                str = "请选择注册车辆";
                break;
            case "color":
                str = "请选择注册车辆颜色";
                break;
            case "plate_no_short":
                str = "请选择注册车辆车牌简称";
                break;
            case "plate_no":
                str = "请输入注册车辆车牌号码";
                break;
            case "vehicle_owner":
                str = "请输入注册车辆所有人姓名";
                break;
            case "register_date":
                str = "请选择注册车辆行驶证注册日期";
                break;
            case "image_driving_a":
                str = "请上传行驶证正本照";
                break;
            case "image_driving_b":
                str = "请上传行驶证副本照";
                break;
            case "image_group":
                str = "请上传照片";
                break;
            case "qualification_no":
                str = "请输入服务证编号";
                break;
            case "image_qualification":
                str = "请上传服务证照";
                break;
            case "image":
                str = "请上传照片";
                break;
            case "short_no":
                str = "请选择车牌简称";
                break;
            case "seats":
                str = "请填写核载人数";
                break;
            case "type":
                str = "请填写核载人数";
                break;
            case "unified_social_credit_code":
                str = "请填写企业代码";
                break;
            case "image_enterprise_license":
                str = "请上传企业营业执照照";
                break;
            case "phone":
                str = "请填写联系电话";
                break;
            default: break;
        }
        return str;
    }
});

