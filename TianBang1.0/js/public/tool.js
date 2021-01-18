/**
 * 工具类js
 * 格式转换、单位换算、通用方法
 */
$(function () {

    /**
     * 获取浏览器地址的参数
     * @param key 要获取的参数名
     * @return {string|null} 返回参数值
     */
    $.getLocationParams = function (key) {
        let reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
        let r = encodeURI(window.location.search).substr(1).match(reg);
        if (r != null) {
            return decodeURI(unescape(r[2]));
        }
        return null;
    };

    /**
     * 时间戳格式化时间格式
     * @param time 时间戳（秒）
     * @param format 时间格式（"yyyy-MM-dd hh:mm:ss"）
     * @return {*} 时间格式
     */
    $.formatDate = function(time, format){
        let date = new Date(time * 1000);
        format = format || "yyyy-MM-dd hh:mm:ss";
        let o = {
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
        for(let k in o) {
            if(new RegExp("("+ k +")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length === 1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
            }
        }
        return format;
    };

    /**
     * 时间差格式化
     * @param times 时间距离（秒）
     * @return {string} 时间差值格式（*天*时*分*秒）
     */
    $.timeDifference = function (times) {
        let str = "0";
        if (times > 0) {
            //计算出相差天数
            let days = Math.floor(times/(24*3600));
            //计算出小时数
            let leave1 = times%(24*3600);   //计算天数后剩余的毫秒数
            let hours = Math.floor(leave1/(3600));
            //计算相差分钟数
            let leave2 = leave1%(3600);   //计算小时数后剩余的毫秒数
            let minutes = Math.floor(leave2/(60));
            //计算相差秒数
            let leave3 = leave2%(60); //计算分钟数后剩余的毫秒数
            let seconds = Math.round(leave3);
            if (days > 0) {
                str = days+"天"+hours+"小时"+minutes+"分钟";
            } else {
                str = hours+"小时"+minutes+"分钟";
            }
        }
        return str;
    };

    /**
     * 字符隐藏替换
     * @param str 目标字符串（电话号码、身份证号码、姓名等）
     * @return {string|*}
     */
    $.number = function (str) {
        if (str != null && str !== "") {
            let string = "";
            if (str.length < 7) {
                string = '*' + str.substr(6);
            } else if (str.length === 11) {
                string = str.substr(0, 3) + '****' + str.substr(7);
            } else if (str.length === 12) {
                string = str.substr(0, 3) + '****' + str.substr(8);
            } else if (str.length === 18) {
                string = str.substr(0, 4) + '**********' + str.substr(14);
            } else {
                string = str;
            }
            return string;
        } else {
            return ""
        }
    };

    /**
     * 补全两位小数
     * @param number 目标数值
     * @return {string|boolean}
     */
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

    /**
     * 转货币单位
     * @param number 目标数据
     * @param places 小数位数
     * @param symbol 钱币符号
     * @param thousand 分割符号
     * @param decimal 小数点符号
     * @returns {string}
     */
    $.formatMoney = function (number, places, symbol, thousand, decimal) {
        number = number || 0;
        places = !isNaN(places = Math.abs(places)) ? places : 2;
        symbol = symbol !== undefined ? symbol : "";
        thousand = thousand || ",";
        decimal = decimal || ".";
        let negative = number < 0 ? "-" : "";
        let  i = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "";
        let j = i.length > 3 ? i.length % 3 : 0;
        return symbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (places ? decimal + Math.abs(number - i).toFixed(places).slice(2) : "");
    };
});