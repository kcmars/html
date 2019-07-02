/**
 * Created by keyC on 2018/7/13.
 */
var img1 = "";
var img2 = "";
var img3 = "";
var img4 = "";
var index = -1;
var img_id_card_front = "../img/id-card-front-demo.jpg";//身份证正面照
var img_id_card_back = "../img/id-card-back-demo.jpg";//身份证反面照
var img_driver_license = "../img/driver-license-front-demo.jpg";//驾驶证正本照
var demoImg = [
    {text: "身份证正面照", img: img_id_card_front},
    {text: "身份证反面照", img: img_id_card_back},
    {text: "驾驶证正本照", img: img_driver_license}
];
var flag = true; //阻止点击事件
var busEmployeesInfo; //大巴经营者认证信息
var data = {}; //检查手机号是否被添加的token

$(function () {
    var url = window.location.href;
    if (url.indexOf('?') !== -1) {
        var search = url.substring(url.indexOf('?') + 1);
        var queryArray = search.split('&');
        queryArray.forEach(function (item){
            var itemArray = item.split('=');
            var key = itemArray[0];
            var value = decodeURIComponent(itemArray[1]) ? decodeURIComponent(itemArray[1]) : '';
            data[key] = value;
        })
    }

    var type = $("input[name='radio']:checked").val();
    if(type == "person"){
        $("#driver").addClass("none");
        $("#driver-title").removeClass("authen-title").addClass("none");
    } else {
        $("#driver").removeClass("none");
        $("#driver-title").removeClass("none").addClass("authen-title");
    }

    $(".authen-type-item").bind("click", function () {
        if (flag) {
            $(this).find("input").attr("checked", "true");
            var type = $("input[name='radio']:checked").val();
            if(type == "person"){
                $("#driver").addClass("none");
                $("#driver-title").removeClass("authen-title").addClass("none");
            } else {
                $("#driver").removeClass("none");
                $("#driver-title").removeClass("none").addClass("authen-title");
            }
        } else {
            toastAlertShow("已提交审核，不能继续修改", 1000);
        }
    });

    var realNameFlag = true;
    $("#realName-title").bind("click", function () {
        if(realNameFlag){
            realNameFlag = false;
            $("#realName").animate({height: 0}, 300);
            $(this).find("i").addClass("img-transform");
        } else {
            realNameFlag = true;
            var $obj = $("#realName");
            var currentHeight = $obj.css("height");
            $obj.css("height","auto");
            var animateHeight = $obj.css("height");
            $obj.css("height", currentHeight);
            $obj.animate({height: animateHeight}, 300);
            $(this).find("i").removeClass("img-transform");
        }
    });
    var driverLicenseFlag = true;
    $("#driver-title").bind("click", function () {
        if(driverLicenseFlag){
            driverLicenseFlag = false;
            $("#driver").animate({height: 0}, 300);
            $(this).find("i").addClass("img-transform");
        } else {
            driverLicenseFlag = true;
            var $obj = $("#driver");
            var currentHeight = $obj.css("height");
            $obj.css("height","auto");
            var animateHeight = $obj.css("height");
            $obj.css("height", currentHeight);
            $obj.animate({height: animateHeight}, 300);
            $(this).find("i").removeClass("img-transform");
        }
    });
    /**
     * 初始化传入参数说明
     * @param  {[date]}      [设置日期]
     * @param  {[theme]}     [主题设置：目前白蓝两种主题，默认白色主题]
     * @param  {[onSelect]}  [日期选中后回调函数 参数date为选中日期]
     */
    date.init(
        {
            date : $.format(new Date().getTime()/1000, "yyyy-MM-dd"),
            // date : '2018-07-25',
            theme : 'blue',
            onSelect : function(date){
                console.log(date);
            }}
    );
    getRequest(getParams);
    // getParams();

});

/**
 * 获取参数
 */
function getParams() {
    if(sessionStorage.getItem("busEmployeesInfo") != null){
        busEmployeesInfo = JSON.parse(sessionStorage.getItem("busEmployeesInfo"));
    }
    if(busEmployeesInfo) {
        showBusEmployeesInfo();
    } else {
        getBusEmployeesInfo();
    }
    // getBusEmployeesInfo();
    //保存
    $("#submit").bind("click", function () {
        submitBusEmployeesInfo();
    });
}
/**
 * 获取雇员实名认证信息getEmployeesInfo
 */
function getBusEmployeesInfo() {
    let params = {};
    let url = "";
    if (param.type == "selfInfo") {
        // params.user_id = $.user_id;
        params.user_id = param.user_id;
        url = $.getEmployeesInfo;
    } else {
        if (param.employee_id == null) {
            return;
        }
        if (param.id != null) {
            params.id = param.id;
        }
        // params.user_id = $.user_id;
        // params.type = "1";
        // params.employee_id = "118";
        params.user_id = param.user_id;
        params.type = param.type;
        params.employee_id = param.employee_id;
        url = $.getBusEmployees;
    }
    loadAlertShow("获取中...");
    console.log(params);
    $.ajax({
        type: 'POST',
        url: url,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res && res.status == 1){
                busEmployeesInfo = res.data;
                if(busEmployeesInfo){
                    sessionStorage.setItem("busEmployeesInfo", JSON.stringify(busEmployeesInfo));
                    showBusEmployeesInfo();
                }
            } else {
                toastAlertShow(res.msg, 2500);
            }
        },
        error: function (err) {
            console.log(err);
            loadAlertHide();
            window.location.href = "../../Util/html/error.html";
        }
    });
}

/**
 * 提交司机实名认证信息
 */
function submitBusEmployeesInfo() {
    let params = {};
    params.user_id = param.user_id;
    // params.user_id = $.user_id;
    let url = $.submitBusEmployees;
    if (param.employee_id != null) {
        params.employee_id = param.employee_id;
        url = $.updateEmployeeInfo;
    }
    if (data != null && data.token != null) {
        params.__token__ = data.token;
    }
    params.type = $("input[name='radio']:checked").val() == "driver" ? "1" : "2";
    params.name = $("#name").val().trim();
    params.ID = $("#id").val().trim();
    params.image_id_a = img1;
    params.image_id_b = img2;
    params.image_drivers = img3;
    params.first_issue = $("#first-issue").val().trim();
    console.log(params);
    if (params.name == "" || params.name == null) {
        toastAlertShow("请输入姓名");
        return;
    }
    if (params.ID == "" || params.ID == null) {
        toastAlertShow("请输入身份证号码");
        return;
    }
    if (params.image_id_a == "" || params.image_id_a == null) {
        toastAlertShow("请上传身份证正面照");
        return;
    }
    if (params.image_id_b == "" || params.image_id_b == null) {
        toastAlertShow("请上传身份证背面照");
        return;
    }
    if (params.type == "1") {
        if (params.image_drivers == "" || params.image_drivers == null) {
            toastAlertShow("请上传驾驶证正本照");
            return;
        }
        if (params.first_issue == "" || params.first_issue == null) {
            toastAlertShow("请选择初次领证日期");
            return;
        }
    }
    loadAlertShow("提交中...");
    $.ajax({
        type: 'POST',
        url: url,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res && res.status == 1){
                flag = false;
                $("#submit").removeClass("submit-btn").addClass("none");
                $("input").attr("readonly", true);
                $("input").attr("disabled", true);
                $("ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
                $("ul li div b").text("审核中");
                toastAlertShow(res.msg, 2500);
            } else {
                toastAlertShow(res.msg, 2000);
            }
        },
        error: function (err) {
            console.log(err);
            loadAlertHide();
            window.location.href = "../../Util/html/error.html";
        }
    });
}

/**
 * 显示司机认证信息
 */
function showBusEmployeesInfo() {
    if(busEmployeesInfo){
        let status = busEmployeesInfo.status;
        let realNameStatus = busEmployeesInfo.real_name_status;
        let driverStatus = busEmployeesInfo.drivers_license_status;
        let type = busEmployeesInfo.employee_type;
        if (type == "1") {
            if(realNameStatus > 0 && driverStatus > 0 && status != 2) {
                flag = false;
                $('input:radio').attr('disabled', true);
                $("#submit").removeClass("submit-btn").addClass("none");
            } else {
                flag = true;
                $('input:radio').attr('disabled', false);
                $("#submit").removeClass("none").addClass("submit-btn");
            }
        } else {
            if(realNameStatus > 0 && status != 2) {
                flag = false;
                $('input:radio').attr('disabled', true);
                $("#submit").removeClass("submit-btn").addClass("none");
            } else {
                flag = true;
                $('input:radio').attr('disabled', false);
                $("#submit").removeClass("none").addClass("submit-btn");
            }
        }
        //注册类型
        if (type == "1") { //1司机
            $("#input1").attr("checked", "true");
            $("#driver").removeClass("none");
            $("#driver-title").removeClass("none").addClass("authen-title");
        } else { //2售票员
            $("#input2").attr("checked", "true");
            $("#driver").addClass("none");
            $("#driver-title").removeClass("authen-title").addClass("none");
        }
        //实名信息
        if(realNameStatus == 1) { //审核中
            $("#realName #name").val(busEmployeesInfo.name);
            $("#realName #id").val(busEmployeesInfo.ID);
            $("#realName #phone").val(busEmployeesInfo.phone);
            $("#realName input").attr("readonly", true);
            $("#realName #img1").attr("src", $.server2 + busEmployeesInfo.image_id_a);
            $("#realName #img2").attr("src", $.server2 + busEmployeesInfo.image_id_b);
            img1 = busEmployeesInfo.image_id_a;
            img2 = busEmployeesInfo.image_id_b;
            $("#realName ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#realName ul li div b").html("审核中");

        } else if (realNameStatus == 2) { //待完善
            $("#realName #name").val(busEmployeesInfo.name != null ? busEmployeesInfo.name : "");
            $("#realName #id").val(busEmployeesInfo.ID != null ? busEmployeesInfo.ID : "");
            $("#realName #phone").val(busEmployeesInfo.phone);
            $("#realName input").attr("readonly", false);
            $("#realName #phone").attr("readonly", true);
            $("#realName #img1").attr("src", busEmployeesInfo.image_id_a != null && busEmployeesInfo.image_id_a != "" ? $.server2 + busEmployeesInfo.image_id_a : img_id_card_front);
            $("#realName #img2").attr("src", busEmployeesInfo.image_id_b != null && busEmployeesInfo.image_id_b != "" ? $.server2 + busEmployeesInfo.image_id_b : img_id_card_back);
            if (busEmployeesInfo.image_id_a != null && busEmployeesInfo.image_id_a != "") {
                img1 = busEmployeesInfo.image_id_a;
            }
            if (busEmployeesInfo.image_id_b != null && busEmployeesInfo.image_id_b != "") {
                img2 = busEmployeesInfo.image_id_b;
            }
            $("#realName ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#realName ul li div b").html("请完善资料");

        } else if ( realNameStatus == 3) { //成功
            $("#realName #name").val(busEmployeesInfo.name);
            $("#realName #id").val(busEmployeesInfo.ID);
            $("#realName #phone").val(busEmployeesInfo.phone);
            $("#realName input").attr("readonly", true);
            $("#realName #img1").attr("src", $.server2 + busEmployeesInfo.image_id_a);
            $("#realName #img2").attr("src", $.server2 + busEmployeesInfo.image_id_b);
            img1 = busEmployeesInfo.image_id_a;
            img2 = busEmployeesInfo.image_id_b;
            $("#realName ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#realName ul li div b").html("审核通过");

        } else if(realNameStatus == 0 || realNameStatus == -1) { //未提交认证 数据无效
            $("#realName input").attr("readonly", false);
            $("#realName ul li div").addClass("none");
            $("#realName ul li div b").html("");

        } else if(realNameStatus == -2) { //失败
            $("#realName #name").val(busEmployeesInfo.name);
            $("#realName #id").val(busEmployeesInfo.ID);
            $("#realName #phone").val(busEmployeesInfo.phone);
            $("#realName input").attr("readonly", false);
            $("#realName #phone").attr("readonly", true);
            $("#realName #img1").attr("src", $.server2 + busEmployeesInfo.image_id_a);
            $("#realName #img2").attr("src", $.server2 + busEmployeesInfo.image_id_b);
            img1 = busEmployeesInfo.image_id_a;
            img2 = busEmployeesInfo.image_id_b;
            $("#realName ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#realName ul li div b").html("审核失败<br/>点击重新上传");
        }
        //驾驶证信息
        if (type == "1") {
            if(driverStatus == 1) { //审核中
                $("#driver input").attr("readonly", true);
                $("#driver input").attr("disabled", true);
                $("#driver #first-issue").val(busEmployeesInfo.first_issue);
                $("#driver #img3").attr("src", $.server2 + busEmployeesInfo.image_drivers);
                img3 = busEmployeesInfo.image_drivers;
                $("#driver ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
                $("#driver ul li div b").html("审核中");
            } else if (driverStatus == 2) { //待完善
                $("#driver input").attr("disabled", false);
                $("#driver input").attr("readonly", false);
                $("#driver #first-issue").val(busEmployeesInfo.first_issue != null ? busEmployeesInfo.first_issue : "");
                $("#driver #img3").attr("src", busEmployeesInfo.image_drivers != null && busEmployeesInfo.image_drivers != "" ? $.server2 + busEmployeesInfo.image_drivers : img_driver_license);
                if (busEmployeesInfo.image_drivers != null && busEmployeesInfo.image_drivers != "") {
                    img3 = busEmployeesInfo.image_drivers;
                }
                $("#driver ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
                $("#driver ul li div b").html("请完善资料");

            } else if (driverStatus == 3) { //成功
                $("#driver input").attr("disabled", true);
                $("#driver input").attr("readonly", true);
                $("#driver #first-issue").val(busEmployeesInfo.first_issue);
                $("#driver #img3").attr("src", $.server2 + busEmployeesInfo.image_drivers);
                img3 = busEmployeesInfo.image_drivers;
                $("#driver ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
                $("#driver ul li div b").html("审核通过");

            } else if(driverStatus == 0 || driverStatus == -1) { //未提交认证 数据无效
                $("#driver input").attr("disabled", false);
                $("#driver input").attr("readonly", false);
                $("#driver ul li div").addClass("none");
                $("#driver ul li div b").html("");
            } else if(driverStatus == -2) { //失败
                $("#driver input").attr("readonly", false);
                $("#driver input").attr("disabled", false);
                $("#driver #first-issue").val(busEmployeesInfo.first_issue);
                $("#driver #img3").attr("src", $.server2 + busEmployeesInfo.image_drivers);
                img3 = busEmployeesInfo.image_drivers;
                $("#driver ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
                $("#driver ul li div b").html("审核失败<br/>点击重新上传");
            }
        }
    }
}

/**
 * 选择上传图片
 * @param index 选取上传下标
 */
function showImg(index) {
    if(busEmployeesInfo){
        if(index < 2){
            if (busEmployeesInfo.real_name_status > 0 && busEmployeesInfo.real_name_status != 2) {
                return;
            }
        }
        if(index == 2){
            if (busEmployeesInfo.drivers_license_status > 0 && busEmployeesInfo.drivers_license_status != 2) {
                return;
            }
        }
    }
    if (!flag){
        return;
    }
    $("#demo-model").removeClass("none").addClass("demo-model");
    $("#demo-model").animate({top: "0", opacity: 1}, 300);
    $("#demo-model > .content > .title").text(demoImg[index].text);
    $("#demo-model > .content > .legend > img").attr("src", demoImg[index].img);
    $("#demo-model > .model").bind("click", function () {
        $("#demo-model").animate({top: "100%", opacity: 0}, 300);
        $("#file").val("");
        $("#file").off("change");
        setTimeout(function () {
            $("#demo-model").removeClass("demo-model").addClass("none");
        }, 300);
    });
    $("#file").on("click", function () {
        $("#demo-model").animate({top: "100%", opacity: 0}, 300);
        setTimeout(function () {
            $("#demo-model").removeClass("demo-model").addClass("none");
        }, 300);
    });
    $("#file").val("");
    $("#file").off("change");
    $("#file").on("change", function () {
        //console.log("index= ", index);
        var file = $(".file").get(0).files[0];
        var render = new FileReader();
        render.readAsDataURL(file);
        render.onload = function (e) {
            // console.log(e);
            //console.log(e.target.result);
            if (index == 0) {
                uploadPicture(0, e.target.result, 1, "A", img1);  //身份证正面
            } else if (index == 1) {
                uploadPicture(1, e.target.result, 1, "B", img2); //身份证反面
            } else if (index == 2) {
                uploadPicture(2, e.target.result, 2, "A", img3); //驾驶证照
            }
            file = null;
            $("#file").val("");
            $("#file").off("change");
        }
    });
}

/**
 * 图片上传
 * @param index 图片id
 * @param base64 图片base64
 * @param type 上传的类型123456 身份证，驾驶证，行驶证，人车合照，上岗证，从业资格证
 * @param extra 正反面 A、B
 * @param last_file 上次回传的文件名（服务器上的路径
 */
function uploadPicture(index, base64, type, extra, last_file) {
    loadAlertShow("正在上传...");
    canvasDataURL(base64, function callback(data) {
        let params = {
            // user_id: $.user_id,
            user_id: param.user_id,
            base64: data,
            type: type,
            extra: extra,
            last_file: last_file
        };
        console.log(params);
        $.ajax({
            type: 'POST',
            url: $.uploadPicture,
            data: params,
            success: function (res) {
                //console.log(res);
                loadAlertHide();
                if(res && res.status == 1){
                    let path = res.data;
                    switch (index) {
                        case 0:
                            img1 = path;
                            $("#img1").attr("src", data);
                            $("#img1-status").removeClass("authen-status-ing authen-status-over").addClass("none");
                            break;
                        case 1:
                            img2 = path;
                            $("#img2").attr("src", data);
                            $("#img2-status").removeClass("authen-status-ing authen-status-over").addClass("none");
                            break;
                        case 2:
                            img3 = path;
                            $("#img3").attr("src", data);
                            $("#img3-status").removeClass("authen-status-ing authen-status-over").addClass("none");
                            break;
                        default: break;
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

/**
 * 压缩图片
 * @param path
 */
function canvasDataURL(path, callback){
    var img = new Image();
    img.src = path;
    img.onload = function(){
        var that = this;
        // 默认按比例压缩
        var w = that.width,
            h = that.height,
            scale = w / h;
        h =  (w / scale);
        var quality = 0.1;  // 默认图片质量为0.7
        //生成canvas
        var canvas = document.createElement('canvas');
        var ctx = canvas.getContext('2d');
        // 创建属性节点
        var anw = document.createAttribute("width");
        anw.nodeValue = w;
        var anh = document.createAttribute("height");
        anh.nodeValue = h;
        canvas.setAttributeNode(anw);
        canvas.setAttributeNode(anh);
        ctx.drawImage(that, 0, 0, w, h);
        console.log(quality);
        // quality值越小，所绘制出的图像越模糊
        let base64 = canvas.toDataURL('image/jpeg', quality);
        callback(base64);
    };
}