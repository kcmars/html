/**
 * Created by Administrator on 2018/7/13.
 */
var img1 = "";
var img2 = "";
var img3 = "";
var img4 = "";
var img5 = "";
var img6 = "";
var index = -1;
var demoImg = [
    {text: "身份证正面照", img: "../img/id-card-front-demo.jpg"},
    {text: "身份证反面照", img: "../img/id-card-back-demo.jpg"},
    {text: "驾驶证照", img: "../img/driver-license-front-demo.jpg"},
    {text: "行驶证正本照", img: "../img/driving-license-front-demo.jpg"},
    {text: "行驶证副本照", img: "../img/driving-license-back-demo.jpg"},
    {text: "人车合照", img: "../img/work-license-demo.jpg"}
];
$(function () {
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
            $("#driver-license").animate({height: 0}, 300);
            $(this).find("i").addClass("img-transform");
        } else {
            driverLicenseFlag = true;
            var $obj = $("#driver-license");
            var currentHeight = $obj.css("height");
            $obj.css("height","auto");
            var animateHeight = $obj.css("height");
            $obj.css("height", currentHeight);
            $obj.animate({height: animateHeight}, 300);
            $(this).find("i").removeClass("img-transform");
        }
    });
    var drivingLicenseFlag = true;
    $("#driving-title").bind("click", function () {
        if(drivingLicenseFlag){
            drivingLicenseFlag = false;
            $("#driving-license").animate({height: 0}, 300);
            $(this).find("i").addClass("img-transform");
        } else {
            drivingLicenseFlag = true;
            var $obj = $("#driving-license");
            var currentHeight = $obj.css("height");
            $obj.css("height","auto");
            var animateHeight = $obj.css("height");
            $obj.css("height", currentHeight);
            $obj.animate({height: animateHeight}, 300);
            $(this).find("i").removeClass("img-transform");
        }
    });
    var personCarFlag = true;
    $("#photo-person-car-title").bind("click", function () {
        if(personCarFlag){
            personCarFlag = false;
            $("#photo-person-car").animate({height: 0}, 300);
            $(this).find("i").addClass("img-transform");
        } else {
            personCarFlag = true;
            var $obj = $("#photo-person-car");
            var currentHeight = $obj.css("height");
            $obj.css("height","auto");
            var animateHeight = $obj.css("height");
            $obj.css("height", currentHeight);
            $obj.animate({height: animateHeight}, 300);
            $(this).find("i").removeClass("img-transform");
        }
    });

    getRequest(getParams);
    // getParams();

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
    var templatePlateModels = document.getElementById('template-plate-short-list').innerHTML;
    document.getElementById('plate-short-list').innerHTML = doT.template(templatePlateModels)($.plateShortList);
    //选择车牌简称
    $("#plate-no-short-box").bind("click", function () {
        if(driverInfo){
            if(driverInfo.driving_license_status > 0){
                return;
            }
        }
        if (!flag){
            return;
        }
        $("#plate-short-list li span").removeClass("orange-text");
        $("#plate-model").removeClass("plate-model-animation-out").addClass("plate-model-animation-in");
        $("#plate-short-list li, #plate-model > .model").bind("click", function () {
            $(this).find("span").addClass("orange-text");
            $("#plate-model").removeClass("plate-model-animation-in").addClass("plate-model-animation-out");
            $("#plate-no-short").val($.plateShortList[$(this).index()]);
        });
    });

    //添加选择城市的组件
    $("#select-city").bind("click", function () {
        if (!flag){
            return;
        }
        $.ajax({
            type:"get",
            url: "citys.html",
            async:true,
            success:function(data){
                $("#type").val("express");
                $("#select-model-box").empty();
                $("#select-model-box").html(data);
                $("#select-model-box").removeClass("plate-model-animation-out").addClass("plate-model-animation-in");
            }
        });
    });
    //添加选择车辆的组件
    $("#select-car").bind("click", function () {
        if(driverInfo){
            if(driverInfo.driving_license_status > 0){
                return;
            }
        }
        if (!flag){
            return;
        }
        $.ajax({
            type:"get",
            url:"cars.html",
            async:true,
            success:function(data){
                $("#select-model-box").empty();
                $("#select-model-box").html(data);
                $("#select-model-box").removeClass("plate-model-animation-out").addClass("plate-model-animation-in");
            }
        });
    });

});

var flag = true; //阻止点击事件
var driverInfo; //司机认证信息
var carInfo = {  //选择车辆信息
    brand: "奥迪",
    model: "A3",
    color: "银白色"
};
let cityInfo = {   //选择城市信息
    city: "",
    city_code: ""
};

function getParams() {
    if(sessionStorage.getItem("driverInfo") != null){
        driverInfo = JSON.parse(sessionStorage.getItem("driverInfo"));
    }
    if(driverInfo) {
        showExpressDriverInfo();
    } else {
        getExpressDriverInfo();
    }

    //保存
    $("#submit").bind("click", function () {
        submitExpressDriver();
    });

}

//获取司机实名认证信息
function getExpressDriverInfo() {
    let params = {
        // user_id: "6a2f6035-653e-406d-b07a-ae94d46cc42a"
        user_id: param.user_id
    };
    loadAlertShow("获取中...");
    $.ajax({
        type: 'POST',
        url: $.getExpressDriverInfo,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res.status == 1){
                driverInfo = res.data;
                if(driverInfo){
                    sessionStorage.setItem("driverInfo", JSON.stringify(driverInfo));
                    showExpressDriverInfo();
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

//提交司机实名认证信息
function submitExpressDriver() {
    if(sessionStorage.getItem("cityInfo") != null) {
        cityInfo = JSON.parse(sessionStorage.getItem("cityInfo"));
    }
    if(sessionStorage.getItem("carInfo") != null) {
        carInfo = JSON.parse(sessionStorage.getItem("carInfo"));
    }
    let params = {
        // user_id: "6a2f6035-653e-406d-b07a-ae94d46cc42a",
        user_id: param.user_id,
        city_code: cityInfo.city_code,
        city_name: cityInfo.city,
        name: $("#name").val(),
        ID: $("#id").val(),
        image_id_a: img1,
        image_id_b: img2,
        image_drivers: img3,
        first_issue: $("#first-issue").val(),
        brand: carInfo.brand,
        model: carInfo.model,
        color: carInfo.color,
        plate_no_short: $("#plate-no-short").val(),
        plate_no: $("#plate-no").val(),
        vehicle_owner: $("#vehicle-owner").val(),
        register_date: $("#register-date").val(),
        image_driving_a: img4,
        image_driving_b: img5,
        image_group: img6
    };
    console.log(params);
    let text = $.detectionParam(params);
    if(text != ""){
        toastAlertShow(text);
        return;
    }
    loadAlertShow("提交中...");
    $.ajax({
        type: 'POST',
        url: $.submitExpressDriver,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res.status == 1){
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
function showExpressDriverInfo() {
    if(driverInfo){
        let realNameStatus = driverInfo.real_name_status;
        let driversLicenseStatus = driverInfo.drivers_license_status;
        let drivingLicenseStatus = driverInfo.driving_license_status;
        let photoGroupStatus = driverInfo.photo_group_status;

        cityInfo = {   //选择城市信息
            city: driverInfo.city,
            city_code: driverInfo.city_code
        };
        $("#city").val(cityInfo.city);
        sessionStorage.setItem("cityInfo", JSON.stringify(cityInfo));

        carInfo = {   //选择车辆信息
            brand: driverInfo.brand,
            model: driverInfo.model,
            color: driverInfo.color
        };
        $("#car").val(carInfo.brand + " " + carInfo.model + " " + carInfo.color);
        sessionStorage.setItem("carInfo", JSON.stringify(carInfo));

        if(realNameStatus > 0 && driversLicenseStatus > 0 && drivingLicenseStatus > 0 && photoGroupStatus >0) {
            flag = false;
            $("#submit").removeClass("submit-btn").addClass("none");
        } else {
            flag = true;
            $("#submit").removeClass("none").addClass("submit-btn");
        }
        //实名信息
        if(realNameStatus == 1) { //审核中
            $("#realName #name").val(driverInfo.name);
            $("#realName #id").val(driverInfo.ID);
            $("#realName input").attr("readonly", true);
            $("#realName #img1").attr("src", $.server2 + driverInfo.image_id_a);
            $("#realName #img2").attr("src", $.server2 + driverInfo.image_id_b);
            img1 = driverInfo.image_id_a;
            img2 = driverInfo.image_id_b;
            $("#realName ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#realName ul li div b").html("审核中");
        } else if (realNameStatus == 2 || realNameStatus == 3) { //成功
            $("#realName #name").val(driverInfo.name);
            $("#realName #id").val(driverInfo.ID);
            $("#realName input").attr("readonly", true);
            $("#realName #img1").attr("src", $.server2 + driverInfo.image_id_a);
            $("#realName #img2").attr("src", $.server2 + driverInfo.image_id_b);
            img1 = driverInfo.image_id_a;
            img2 = driverInfo.image_id_b;
            $("#realName ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#realName ul li div b").html("审核通过");
        } else if(realNameStatus == 0 || realNameStatus == -1) { //未提交认证 数据无效
            $("#realName input").attr("readonly", false);
            $("#realName ul li div").addClass("none");
            $("#realName ul li div b").html("");
        } else if(realNameStatus == -2) { //失败
            $("#realName #name").val(driverInfo.name);
            $("#realName #id").val(driverInfo.ID);
            $("#realName input").attr("readonly", false);
            $("#realName #img1").attr("src", $.server2 + driverInfo.image_id_a);
            $("#realName #img2").attr("src", $.server2 + driverInfo.image_id_b);
            img1 = driverInfo.image_id_a;
            img2 = driverInfo.image_id_b;
            $("#realName ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#realName ul li div b").html("审核失败<br/>点击重新上传");
        }
        //驾驶证信息
        if(driversLicenseStatus == 1) { //审核中
            $("#driver-license input").attr("disabled", true);
            $("#driver-license #first-issue").val(driverInfo.first_issue);
            $("#driver-license #img3").attr("src", $.server2 + driverInfo.image_drivers);
            img3 = driverInfo.image_drivers;
            $("#driver-license ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#driver-license ul li div b").html("审核中");
        } else if (driversLicenseStatus == 2 || driversLicenseStatus == 3) { //成功
            $("#driver-license input").attr("disabled", true);
            $("#driver-license #first-issue").val(driverInfo.first_issue);
            $("#driver-license #img3").attr("src", $.server2 + driverInfo.image_drivers);
            img3 = driverInfo.image_drivers;
            $("#driver-license ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#driver-license ul li div b").html("审核通过");
        } else if(driversLicenseStatus == 0 || driversLicenseStatus == -1) { //未提交认证 数据无效
            $("#driver-license input").attr("disabled", false);
            $("#driver-license ul li div").addClass("none");
            $("#driver-license ul li div b").html("");
        } else if(driversLicenseStatus == -2) { //失败
            $("#driver-license input").attr("disabled", false);
            $("#driver-license #first-issue").val(driverInfo.first_issue);
            $("#driver-license #img3").attr("src", $.server2 + driverInfo.image_drivers);
            img3 = driverInfo.image_drivers;
            $("#driver-license ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#driver-license ul li div b").html("审核失败<br/>点击重新上传");
        }
        //行驶证信息
        if(drivingLicenseStatus == 1) { //审核中
            $("#driving-license input").attr("disabled", true);
            $("#driving-license #car").val(driverInfo.brand + " " + driverInfo.model + " " + driverInfo.color);
            $("#driving-license #plate-no-short").val(driverInfo.plate_no_short);
            $("#driving-license #plate-no").val(driverInfo.plate_no);
            $("#driving-license #vehicle-owner").val(driverInfo.vehicle_owner);
            $("#driving-license #register-date").val(driverInfo.register_date);
            $("#driving-license input").attr("readonly", true);
            $("#driving-license #img4").attr("src", $.server2 + driverInfo.image_driving_a);
            $("#driving-license #img5").attr("src", $.server2 + driverInfo.image_driving_b);
            img4 = driverInfo.image_driving_a;
            img5 = driverInfo.image_driving_b;
            $("#driving-license ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#driving-license ul li div b").html("审核中");
        } else if (drivingLicenseStatus == 2 || drivingLicenseStatus == 3) { //成功
            $("#driving-license input").attr("disabled", true);
            $("#driving-license #car").val(driverInfo.brand + " " + driverInfo.model + " " + driverInfo.color);
            $("#driving-license #plate-no-short").val(driverInfo.plate_no_short);
            $("#driving-license #plate-no").val(driverInfo.plate_no);
            $("#driving-license #vehicle-owner").val(driverInfo.vehicle_owner);
            $("#driving-license #register-date").val(driverInfo.register_date);
            $("#driving-license input").attr("readonly", true);
            $("#driving-license #img4").attr("src", $.server2 + driverInfo.image_driving_a);
            $("#driving-license #img5").attr("src", $.server2 + driverInfo.image_driving_b);
            img4 = driverInfo.image_driving_a;
            img5 = driverInfo.image_driving_b;
            $("#driving-license ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#driving-license ul li div b").html("审核通过");
        } else if(drivingLicenseStatus == 0 || drivingLicenseStatus == -1) { //未提交认证 数据无效
            $("#driving-license input").attr("disabled", false);
            $("#driving-license input").attr("readonly", false);
            $("#driving-license ul li div").addClass("none");
            $("#driving-license ul li div b").html("");
        } else if(drivingLicenseStatus == -2) { //失败
            $("#driving-license input").attr("disabled", false);
            $("#driving-license #car").val(driverInfo.brand + " " + driverInfo.model + " " + driverInfo.color);
            $("#driving-license #plate-no-short").val(driverInfo.plate_no_short);
            $("#driving-license #plate-no").val(driverInfo.plate_no);
            $("#driving-license #vehicle-owner").val(driverInfo.vehicle_owner);
            $("#driving-license #register-date").val(driverInfo.register_date);
            $("#driving-license input").attr("readonly", false);
            $("#driving-license #img4").attr("src", $.server2 + driverInfo.image_driving_a);
            $("#driving-license #img5").attr("src", $.server2 + driverInfo.image_driving_b);
            img4 = driverInfo.image_driving_a;
            img5 = driverInfo.image_driving_b;
            $("#driving-license ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#driving-license ul li div b").html("审核失败<br/>点击重新上传");
        }
        //人车信息
        if(photoGroupStatus == 1) { //审核中
            $("#photo-person-car-box #img6").attr("src", $.server2 + driverInfo.image_group);
            img6 = driverInfo.image_group;
            $("#photo-person-car-box ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#photo-person-car-box ul li div b").html("审核中");
        } else if (photoGroupStatus == 2 || photoGroupStatus == 3) { //成功
            $("#photo-person-car-box #img6").attr("src", $.server2 + driverInfo.image_group);
            img6 = driverInfo.image_group;
            $("#photo-person-car-box ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#photo-person-car-box ul li div b").html("审核通过");
        } else if(photoGroupStatus == 0 || photoGroupStatus == -1) { //未提交认证 数据无效
            $("#photo-person-car-box ul li div").addClass("none");
            $("#photo-person-car-box ul li div b").html("");
        } else if(photoGroupStatus == -2) { //失败
            $("#photo-person-car-box #img6").attr("src", $.server2 + driverInfo.image_group);
            img6 = driverInfo.image_group;
            $("#photo-person-car-box ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#photo-person-car-box ul li div b").html("审核失败<br/>点击重新上传");
        }
    }
}

/**
 * 选择上传图片
 * @param index 选取上传下标
 */
function showImg(index) {
    if(driverInfo){
        if(index < 2){
            if (driverInfo.real_name_status > 0) {
                return;
            }
        }
        if(index == 2){
            if (driverInfo.drivers_license_status > 0) {
                return;
            }
        }
        if(index == 3 || index == 4){
            if (driverInfo.driving_license_status > 0) {
                return;
            }
        }
        if(index > 4){
            if (driverInfo.photo_group_status > 0) {
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
                uploadPicture(2, e.target.result, 2, "A", img3); //驾驶证
            } else if (index == 3) {
                uploadPicture(3, e.target.result, 2, "A", img4); //行驶证正面
            } else if (index == 4) {
                uploadPicture(4, e.target.result, 3, "B", img5); //行驶证反面
            } else {
                uploadPicture(5, e.target.result, 3, "A", img6); // 人车照
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
            // user_id: "6a2f6035-653e-406d-b07a-ae94d46cc42a",
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
                if(res.status == 1){
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
                        case 3:
                            img4 = path;
                            $("#img4").attr("src", data);
                            $("#img4-status").removeClass("authen-status-ing authen-status-over").addClass("none");
                            break;
                        case 4:
                            img5 = path;
                            $("#img5").attr("src", data);
                            $("#img5-status").removeClass("authen-status-ing authen-status-over").addClass("none");
                            break;
                        case 5:
                            img6 = path;
                            $("#img6").attr("src", data);
                            $("#img6-status").removeClass("authen-status-ing authen-status-over").addClass("none");
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