/**
 * Created by keyC on 2018/7/13.
 * 出租车认证信息
 */
var img1 = "";
var img2 = "";
var img3 = "";
var img4 = "";
var img5 = "";
var img6 = "";
var img7 = "";
var index = -1;
var demoImg = [
    {text: "身份证正面照", img: "../img/id-card-front-demo.jpg"},
    {text: "身份证反面照", img: "../img/id-card-back-demo.jpg"},
    {text: "驾驶证照", img: "../img/driver-license-front-demo.jpg"},
    {text: "行驶证正本照", img: "../img/driving-license-front-demo.jpg"},
    {text: "行驶证副本照", img: "../img/driving-license-back-demo.jpg"},
    {text: "人车合照", img: "../img//person-car-demo.jpg"},
    {text: "服务证照", img: "../img/taxi-qualification-demo.jpg"}
];
var flag1 = true; //阻止点击事件
var taxiDriverInfo; //司机认证信息
var taxiCityInfo = {   //选择城市信息
    province: "",
    pro_code: "",
    city: "",
    city_code: "",
    dist: "",
    ad_code: ""
};

$(function () {
    var realNameFlag = false;
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
    var driverLicenseFlag = false;
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
    var drivingLicenseFlag = false;
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
    var personCarFlag = false;
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
    var taxiFlag = false;
    $("#taxi-title").bind("click", function () {
        if(taxiFlag){
            taxiFlag = false;
            $("#taxi").animate({height: 0}, 300);
            $(this).find("i").addClass("img-transform");
        } else {
            taxiFlag = true;
            var $obj = $("#taxi");
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
    // $(".authen-main").removeClass("none");

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
    //车牌简称
    var templatePlateShort = document.getElementById('template-plate-short-list').innerHTML;
    document.getElementById('plate-short-list').innerHTML = doT.template(templatePlateShort)($.plateShortList);
    //车牌字母
    var templatePlateLetter = document.getElementById('template-plate-letter-list').innerHTML;
    document.getElementById('plate-letter-list').innerHTML = doT.template(templatePlateLetter)($.plateletterList);
    //选择车牌简称
    $("#plate-no-short-box").bind("click", function () {
        if(taxiDriverInfo){
            if(taxiDriverInfo.driving_license_status > 0 && taxiDriverInfo.driving_license_status != 2){
                return;
            }
        }
        if (!flag1){
            return;
        }
        $("#plate-short-list li span").removeClass("orange-text");
        $("#plate-model").removeClass("plate-model-animation-out").addClass("plate-model-animation-in");
        $("#plate-model > .model").bind("click", function () {
            $("#plate-model").removeClass("plate-model-animation-in").addClass("plate-model-animation-out");
            $("#plate-model > .model").unbind("click");
            $("#plate-short-list li").unbind("click");
            $("#plate-letter-list li").unbind("click");
        });
        $("#plate-short-list li").bind("click", function () {
            $(this).addClass("orange-text").siblings().removeClass("orange-text");
            $("#plate-letter-list li").removeClass("orange-text");
            let short = $.plateShortList[$(this).index()];
            $("#plate-letter-list").removeClass("plate-letter-animation-out").addClass("plate-letter-animation-in");
            $("#plate-letter-list li").bind("click", function () {
                $(this).addClass("orange-text");
                $("#plate-letter-list").removeClass("plate-letter-animation-in").addClass("plate-letter-animation-out");
                $("#plate-model").removeClass("plate-model-animation-in").addClass("plate-model-animation-out");
                $("#plate-no-short").val(short + $.plateletterList[$(this).index()]);
                $("#plate-model > .model").unbind("click");
                $("#plate-short-list li").unbind("click");
                $("#plate-letter-list li").unbind("click");
            });
        });
    });
    //添加选择城市的组件
    $("#select-city").bind("click", function () {
        if (taxiDriverInfo) {
            if ((taxiDriverInfo.province != null && taxiDriverInfo.province != "") || (taxiDriverInfo.city != null && taxiDriverInfo.city != "") || (taxiDriverInfo.dist != null && taxiDriverInfo.dist != "")) {
                return;
            }
        }
        if (!flag1){
            return;
        }
        $.ajax({
            type:"get",
            url:"citys.html",
            async:true,
            success:function(data){
                $("#select-model-box").removeClass("plate-model-animation-out").addClass("plate-model-animation-in");
                $("#type").val("taxi");
                $("#select-model-box").empty();
                $("#select-model-box").html(data);
            }
        });
    });
});

/**
 * 获取参数
 */
function getParams() {
    getTaxiDriverInfo();
    //保存
    $("#submit").bind("click", function () {
        submitTaxiDriver();
    });
}

/**
 * 获取司机实名认证信息
 */
function getTaxiDriverInfo() {
    let params = {};
    params.user_id = param.user_id;
    // params.user_id = $.user_id;
    if (param.id != null) {
        params.id = param.id;
    }
    loadAlertShow("获取中...");
    $.ajax({
        type: 'POST',
        url: $.getTaxiDriverInfo,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res && res.status == 1){
                $(".authen-main").removeClass("none");
                taxiDriverInfo = res.data;
                if(taxiDriverInfo) {
                    showTaxiDriverInfo();
                }
            } else {
                $(".authen-main").removeClass("none");
                toastAlertShow(res.msg, 2500);
            }
        },
        error: function (err) {
            console.log(err);
            loadAlertHide();
            // window.location.href = "../../Util/html/error.html";
        }
    });
}
/**
 * 提交司机实名认证信息
 */
function submitTaxiDriver() {
    if(sessionStorage.getItem("taxiCityInfo") != null) {
        taxiCityInfo = JSON.parse(sessionStorage.getItem("taxiCityInfo"));
    }
    let params = {
        // user_id: $.user_id,
        user_id: param.user_id,
        pro_code: taxiCityInfo.pro_code,
        pro_name: taxiCityInfo.province,
        city_code: taxiCityInfo.city_code,
        city_name: taxiCityInfo.city,
        ad_code: taxiCityInfo.ad_code,
        ad_name: taxiCityInfo.dist,
        company_name: $("#company-name").val().trim(),
        name: $("#name").val().trim(),
        ID: $("#id").val().trim(),
        image_id_a: img1,
        image_id_b: img2,
        image_drivers: img3,
        first_issue: $("#first-issue").val().trim(),
        plate_no_short: $("#plate-no-short").val().trim().substring(0,1),
        plate_no_alpha: $("#plate-no-short").val().trim().substring(1,2),
        plate_no: $("#plate-no").val().trim(),
        vehicle_owner: $("#vehicle-owner").val().trim(),
        register_date: $("#register-date").val().trim(),
        image_driving_a: img4,
        image_driving_b: img5,
        image_group: img6,
        qualification_no: $("#qualification-no").val().trim(),
        image_qualification: img7
    };
    let text = $.detectionParam(params);
    if(text != ""){
        toastAlertShow(text);
        return;
    }
    loadAlertShow("提交中...");
    console.log("params", params);
    $.ajax({
        type: 'POST',
        url: $.submitTaxiDriver,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res && res.status == 1){
                flag1 = false;
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
            // window.location.href = "../../Util/html/error.html";
        }
    });
}

/**
 * 显示司机认证信息
 */
function showTaxiDriverInfo() {
    if(taxiDriverInfo){
        let status = taxiDriverInfo.status;
        let taxiCompanyStatus = taxiDriverInfo.taxi_company_status;
        let realNameStatus = taxiDriverInfo.real_name_status;
        let driversLicenseStatus = taxiDriverInfo.drivers_license_status;
        let drivingLicenseStatus = taxiDriverInfo.driving_license_status;
        let photoGroupStatus = taxiDriverInfo.photo_group_status;
        let taxiQualificationStatus = taxiDriverInfo.taxi_qualification_status;
        if (taxiDriverInfo.province != null || taxiDriverInfo.city != null || taxiDriverInfo.dist) {
            taxiCityInfo = {   //选择城市信息
                province: taxiDriverInfo.province,
                pro_code: taxiDriverInfo.pro_code,
                city: taxiDriverInfo.city,
                city_code: taxiDriverInfo.city_code,
                dist: taxiDriverInfo.dist,
                ad_code: taxiDriverInfo.ad_code
            };
            $("#city").val((taxiCityInfo.province != null ? taxiCityInfo.province : "") + "  " + (taxiCityInfo.city != null ? taxiCityInfo.city : "") + "  " + (taxiCityInfo.dist != null ? taxiCityInfo.dist : ""));
            sessionStorage.setItem("taxiCityInfo", JSON.stringify(taxiCityInfo));
        }
        if(status != 2 && taxiCompanyStatus > 0 && realNameStatus > 0 && driversLicenseStatus > 0 && drivingLicenseStatus > 0 && photoGroupStatus >0 && taxiQualificationStatus > 0) {
            flag1 = false;
            $("#submit").removeClass("submit-btn").addClass("none");
        } else {
            flag1 = true;
            $("#submit").removeClass("none").addClass("submit-btn");
        }
        //公司信息
        if(taxiCompanyStatus == 1){ //审核中
            $("#company-name").val(taxiDriverInfo.company_name != null ? taxiDriverInfo.company_name : "");
            $("#company-name").attr("readonly", true);
        } else if (taxiCompanyStatus == 2) { //需完善
            $("#company-name").val(taxiDriverInfo.company_name != null ? taxiDriverInfo.company_name : "");
            $("#company-name").attr("readonly", false);
        } else if (taxiCompanyStatus == 3) { //已认证
            $("#company-name").val(taxiDriverInfo.company_name != null ? taxiDriverInfo.company_name : "");
            $("#company-name").attr("readonly", true);
        } else if (taxiCompanyStatus == 0 || taxiCompanyStatus == -1) { //未认证
            $("#company-name").attr("readonly", false);
        } else if (taxiCompanyStatus == -2) { //审核失败
            $("#company-name").val(taxiDriverInfo.company_name != null ? taxiDriverInfo.company_name : "");
            $("#company-name").attr("readonly", false);
        }
        //实名信息
        if(realNameStatus == 1) { //审核中
            $("#realName #name").val(taxiDriverInfo.name != null ? taxiDriverInfo.name : "");
            $("#realName #id").val(taxiDriverInfo.ID != null ? taxiDriverInfo.ID : "");
            $("#realName input").attr("readonly", true);
            $("#realName #img1").attr("src", taxiDriverInfo.image_id_a != null ? ($.server2 + taxiDriverInfo.image_id_a) : "");
            $("#realName #img2").attr("src", taxiDriverInfo.image_id_b != null ? ($.server2 + taxiDriverInfo.image_id_b) : "");
            img1 = taxiDriverInfo.image_id_a != null ? taxiDriverInfo.image_id_a : "";
            img2 = taxiDriverInfo.image_id_b != null ? taxiDriverInfo.image_id_b : "";
            $("#realName ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#realName ul li div b").html("审核中");
        } else if (realNameStatus == 2) { //需完善
            $("#realName #name").val(taxiDriverInfo.name != null ? taxiDriverInfo.name : "");
            $("#realName #id").val(taxiDriverInfo.ID != null ? taxiDriverInfo.ID : "");
            $("#realName input").attr("readonly", false);
            $("#realName #img1").attr("src", taxiDriverInfo.image_id_a != null ? ($.server2 + taxiDriverInfo.image_id_a) : "");
            $("#realName #img2").attr("src", taxiDriverInfo.image_id_b != null ? ($.server2 + taxiDriverInfo.image_id_b) : "");
            img1 = taxiDriverInfo.image_id_a != null ? taxiDriverInfo.image_id_a : "";
            img2 = taxiDriverInfo.image_id_b != null ? taxiDriverInfo.image_id_b : "";
            $("#realName ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#realName ul li div b").html("请完善资料");
        } else if (realNameStatus == 3) { //成功
            $("#realName #name").val(taxiDriverInfo.name != null ? taxiDriverInfo.name : "");
            $("#realName #id").val(taxiDriverInfo.ID != null ? taxiDriverInfo.ID : "");
            $("#realName input").attr("readonly", true);
            $("#realName #img1").attr("src", taxiDriverInfo.image_id_a != null ? ($.server2 + taxiDriverInfo.image_id_a) : "");
            $("#realName #img2").attr("src", taxiDriverInfo.image_id_b != null ? ($.server2 + taxiDriverInfo.image_id_b) : "");
            img1 = taxiDriverInfo.image_id_a != null ? taxiDriverInfo.image_id_a : "";
            img2 = taxiDriverInfo.image_id_b != null ? taxiDriverInfo.image_id_b : "";
            $("#realName ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#realName ul li div b").html("审核通过");
        } else if(realNameStatus == 0 || realNameStatus == -1) { //未提交认证 数据无效
            $("#realName input").attr("readonly", false);
            $("#realName ul li div").addClass("none");
            $("#realName ul li div b").html("");
        } else if(realNameStatus == -2) { //失败
            $("#realName #name").val(taxiDriverInfo.name != null ? taxiDriverInfo.name : "");
            $("#realName #id").val(taxiDriverInfo.ID != null ? taxiDriverInfo.ID : "");
            $("#realName input").attr("readonly", false);
            $("#realName #img1").attr("src", taxiDriverInfo.image_id_a != null ? ($.server2 + taxiDriverInfo.image_id_a) : "");
            $("#realName #img2").attr("src", taxiDriverInfo.image_id_b != null ? ($.server2 + taxiDriverInfo.image_id_b) : "");
            img1 = taxiDriverInfo.image_id_a != null ? taxiDriverInfo.image_id_a : "";
            img2 = taxiDriverInfo.image_id_b != null ? taxiDriverInfo.image_id_b : "";
            $("#realName ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#realName ul li div b").html("审核失败<br/>点击重新上传");
        }
        //驾驶证信息
        if(driversLicenseStatus == 1) { //审核中
            $("#driver-license input").attr("disabled", true);
            $("#driver-license #first-issue").val(taxiDriverInfo.first_issue != null ? taxiDriverInfo.first_issue : "");
            $("#driver-license #img3").attr("src", taxiDriverInfo.image_drivers != null ? ($.server2 + taxiDriverInfo.image_drivers) : "");
            img3 = taxiDriverInfo.image_drivers != null ? taxiDriverInfo.image_drivers : "";
            $("#driver-license ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#driver-license ul li div b").html("审核中");
        } else if (driversLicenseStatus == 2) { //需完善
            $("#driver-license input").attr("disabled", false);
            $("#driver-license #first-issue").val(taxiDriverInfo.first_issue != null ? taxiDriverInfo.first_issue : "");
            $("#driver-license #img3").attr("src", taxiDriverInfo.image_drivers != null ? ($.server2 + taxiDriverInfo.image_drivers) : "");
            img3 = taxiDriverInfo.image_drivers != null ? taxiDriverInfo.image_drivers : "";
            $("#driver-license ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#driver-license ul li div b").html("请完善资料");
        } else if (driversLicenseStatus == 3) { //成功
            $("#driver-license input").attr("disabled", true);
            $("#driver-license #first-issue").val(taxiDriverInfo.first_issue != null ? taxiDriverInfo.first_issue : "");
            $("#driver-license #img3").attr("src", taxiDriverInfo.image_drivers != null ? ($.server2 + taxiDriverInfo.image_drivers) : "");
            img3 = taxiDriverInfo.image_drivers != null ? taxiDriverInfo.image_drivers : "";
            $("#driver-license ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#driver-license ul li div b").html("审核通过");
        } else if(driversLicenseStatus == 0 || driversLicenseStatus == -1) { //未提交认证 数据无效
            $("#driver-license input").attr("disabled", false);
            $("#driver-license ul li div").addClass("none");
            $("#driver-license ul li div b").html("");
        } else if(driversLicenseStatus == -2) { //失败
            $("#driver-license input").attr("disabled", false);
            $("#driver-license #first-issue").val(taxiDriverInfo.first_issue != null ? taxiDriverInfo.first_issue : "");
            $("#driver-license #img3").attr("src", taxiDriverInfo.image_drivers != null ? ($.server2 + taxiDriverInfo.image_drivers) : "");
            img3 = taxiDriverInfo.image_drivers != null ? taxiDriverInfo.image_drivers : "";
            $("#driver-license ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#driver-license ul li div b").html("审核失败<br/>点击重新上传");
        }
        //行驶证信息
        if(drivingLicenseStatus == 1) { //审核中
            $("#driving-license input").attr("disabled", true);
            $("#driving-license #car").val((taxiDriverInfo.brand != null ? taxiDriverInfo.brand : "") + " " + (taxiDriverInfo.model != null ? taxiDriverInfo.model : "") + " " + (taxiDriverInfo.color != null ? taxiDriverInfo.color : ""));
            $("#driving-license #plate-no-short").val((taxiDriverInfo.plate_no_short != null ? taxiDriverInfo.plate_no_short : "") + (taxiDriverInfo.plate_no_alpha != null ? taxiDriverInfo.plate_no_alpha : ""));
            $("#driving-license #plate-no").val(taxiDriverInfo.plate_no != null ? taxiDriverInfo.plate_no : "");
            $("#driving-license #vehicle-owner").val(taxiDriverInfo.vehicle_owner != null ? taxiDriverInfo.vehicle_owner : "");
            $("#driving-license #register-date").val(taxiDriverInfo.register_date != null ? taxiDriverInfo.register_date : "");
            $("#driving-license #plate-no").attr("readonly", true);
            $("#driving-license #vehicle-owner").attr("readonly", true);
            $("#driving-license #img4").attr("src", taxiDriverInfo.image_driving_a != null ? ($.server2 + taxiDriverInfo.image_driving_a) : "");
            $("#driving-license #img5").attr("src", taxiDriverInfo.image_driving_b != null ? ($.server2 + taxiDriverInfo.image_driving_b) : "");
            img4 = taxiDriverInfo.image_driving_a != null ? taxiDriverInfo.image_driving_a : "";
            img5 = taxiDriverInfo.image_driving_b != null ? taxiDriverInfo.image_driving_b : "";
            $("#driving-license ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#driving-license ul li div b").html("审核中");
        } else if (drivingLicenseStatus == 2) { //需完善
            $("#driving-license input").attr("disabled", false);
            $("#driving-license #car").val((taxiDriverInfo.brand != null ? taxiDriverInfo.brand : "") + " " + (taxiDriverInfo.model != null ? taxiDriverInfo.model : "") + " " + (taxiDriverInfo.color != null ? taxiDriverInfo.color : ""));
            $("#driving-license #plate-no-short").val((taxiDriverInfo.plate_no_short != null ? taxiDriverInfo.plate_no_short : "") + (taxiDriverInfo.plate_no_alpha != null ? taxiDriverInfo.plate_no_alpha : ""));
            $("#driving-license #plate-no").val(taxiDriverInfo.plate_no != null ? taxiDriverInfo.plate_no : "");
            $("#driving-license #vehicle-owner").val(taxiDriverInfo.vehicle_owner != null ? taxiDriverInfo.vehicle_owner : "");
            $("#driving-license #register-date").val(taxiDriverInfo.register_date != null ? taxiDriverInfo.register_date : "");
            $("#driving-license #plate-no").attr("readonly", false);
            $("#driving-license #vehicle-owner").attr("readonly", false);
            $("#driving-license #img4").attr("src", taxiDriverInfo.image_driving_a != null ? ($.server2 + taxiDriverInfo.image_driving_a) : "");
            $("#driving-license #img5").attr("src", taxiDriverInfo.image_driving_b != null ? ($.server2 + taxiDriverInfo.image_driving_b) : "");
            img4 = taxiDriverInfo.image_driving_a != null ? taxiDriverInfo.image_driving_a : "";
            img5 = taxiDriverInfo.image_driving_b != null ? taxiDriverInfo.image_driving_b : "";
            $("#driving-license ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#driving-license ul li div b").html("请完善资料");
        } else if (drivingLicenseStatus == 3) { //成功
            $("#driving-license input").attr("disabled", true);
            $("#driving-license #car").val((taxiDriverInfo.brand != null ? taxiDriverInfo.brand : "") + " " + (taxiDriverInfo.model != null ? taxiDriverInfo.model : "") + " " + (taxiDriverInfo.color != null ? taxiDriverInfo.color : ""));
            $("#driving-license #plate-no-short").val((taxiDriverInfo.plate_no_short != null ? taxiDriverInfo.plate_no_short : "") + (taxiDriverInfo.plate_no_alpha != null ? taxiDriverInfo.plate_no_alpha : ""));
            $("#driving-license #plate-no").val(taxiDriverInfo.plate_no != null ? taxiDriverInfo.plate_no : "");
            $("#driving-license #vehicle-owner").val(taxiDriverInfo.vehicle_owner != null ? taxiDriverInfo.vehicle_owner : "");
            $("#driving-license #register-date").val(taxiDriverInfo.register_date != null ? taxiDriverInfo.register_date : "");
            $("#driving-license #plate-no").attr("readonly", true);
            $("#driving-license #vehicle-owner").attr("readonly", true);
            $("#driving-license #img4").attr("src", taxiDriverInfo.image_driving_a != null ? ($.server2 + taxiDriverInfo.image_driving_a) : "");
            $("#driving-license #img5").attr("src", taxiDriverInfo.image_driving_b != null ? ($.server2 + taxiDriverInfo.image_driving_b) : "");
            img4 = taxiDriverInfo.image_driving_a != null ? taxiDriverInfo.image_driving_a : "";
            img5 = taxiDriverInfo.image_driving_b != null ? taxiDriverInfo.image_driving_b : "";
            $("#driving-license ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#driving-license ul li div b").html("审核通过");
        } else if(drivingLicenseStatus == 0 || drivingLicenseStatus == -1) { //未提交认证 数据无效
            $("#driving-license input").attr("disabled", false);
            $("#driving-license #plate-no").attr("readonly", false);
            $("#driving-license #vehicle-owner").attr("readonly", false);
            $("#driving-license ul li div").addClass("none");
            $("#driving-license ul li div b").html("");
        } else if(drivingLicenseStatus == -2) { //失败
            $("#driving-license input").attr("disabled", false);
            $("#driving-license #car").val((taxiDriverInfo.brand != null ? taxiDriverInfo.brand : "") + " " + (taxiDriverInfo.model != null ? taxiDriverInfo.model : "") + " " + (taxiDriverInfo.color != null ? taxiDriverInfo.color : ""));
            $("#driving-license #plate-no-short").val((taxiDriverInfo.plate_no_short != null ? taxiDriverInfo.plate_no_short : "") + (taxiDriverInfo.plate_no_alpha != null ? taxiDriverInfo.plate_no_alpha : ""));
            $("#driving-license #plate-no").val(taxiDriverInfo.plate_no != null ? taxiDriverInfo.plate_no : "");
            $("#driving-license #vehicle-owner").val(taxiDriverInfo.vehicle_owner != null ? taxiDriverInfo.vehicle_owner : "");
            $("#driving-license #register-date").val(taxiDriverInfo.register_date != null ? taxiDriverInfo.register_date : "");
            $("#driving-license #plate-no").attr("readonly", false);
            $("#driving-license #vehicle-owner").attr("readonly", false);
            $("#driving-license #img4").attr("src", taxiDriverInfo.image_driving_a != null ? ($.server2 + taxiDriverInfo.image_driving_a) : "");
            $("#driving-license #img5").attr("src", taxiDriverInfo.image_driving_b != null ? ($.server2 + taxiDriverInfo.image_driving_b) : "");
            img4 = taxiDriverInfo.image_driving_a != null ? taxiDriverInfo.image_driving_a : "";
            img5 = taxiDriverInfo.image_driving_b != null ? taxiDriverInfo.image_driving_b : "";
            $("#driving-license ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#driving-license ul li div b").html("审核失败<br/>点击重新上传");
        }
        //人车信息
        if(photoGroupStatus == 1) { //审核中
            $("#photo-person-car-box #img6").attr("src", taxiDriverInfo.image_group != null ? ($.server2 + taxiDriverInfo.image_group) : "");
            img6 = taxiDriverInfo.image_group != null ? taxiDriverInfo.image_group : "";
            $("#photo-person-car-box ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#photo-person-car-box ul li div b").html("审核中");
        } else if (photoGroupStatus == 2) { //需完善
            $("#photo-person-car-box #img6").attr("src", taxiDriverInfo.image_group != null ? ($.server2 + taxiDriverInfo.image_group) : "");
            img6 = taxiDriverInfo.image_group != null ? taxiDriverInfo.image_group : "";
            $("#photo-person-car-box ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#photo-person-car-box ul li div b").html("请完善资料");
        } else if (photoGroupStatus == 3) { //成功
            $("#photo-person-car-box #img6").attr("src", taxiDriverInfo.image_group != null ? ($.server2 + taxiDriverInfo.image_group) : "");
            img6 = taxiDriverInfo.image_group != null ? taxiDriverInfo.image_group : "";
            $("#photo-person-car-box ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#photo-person-car-box ul li div b").html("审核通过");
        } else if(photoGroupStatus == 0 || photoGroupStatus == -1) { //未提交认证 数据无效
            $("#photo-person-car-box ul li div").addClass("none");
            $("#photo-person-car-box ul li div b").html("");
        } else if(photoGroupStatus == -2) { //失败
            $("#photo-person-car-box #img6").attr("src", taxiDriverInfo.image_group != null ? ($.server2 + taxiDriverInfo.image_group) : "");
            img6 = taxiDriverInfo.image_group != null ? taxiDriverInfo.image_group : "";
            $("#photo-person-car-box ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#photo-person-car-box ul li div b").html("审核失败<br/>点击重新上传");
        }
        //服务证认证
        if(taxiQualificationStatus == 1){ // 审核中
            $("#taxi-box #qualification-no").val(taxiDriverInfo.taxi_qualification_no != null ? taxiDriverInfo.taxi_qualification_no : "");
            $("#taxi-box input").attr("readonly", true);
            $("#taxi-box #img7").attr("src", taxiDriverInfo.image_taxi_qualification != null ? ($.server2 + taxiDriverInfo.image_taxi_qualification) : "");
            img7 = taxiDriverInfo.image_taxi_qualification != null ? taxiDriverInfo.image_taxi_qualification : "";
            $("#taxi-box ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#taxi-box ul li div b").html("审核中");
        } else if (taxiQualificationStatus == 2) { //需完善
            $("#taxi-box #qualification-no").val(taxiDriverInfo.taxi_qualification_no != null ? taxiDriverInfo.taxi_qualification_no : "");
            $("#taxi-box input").attr("readonly", false);
            $("#taxi-box #img7").attr("src", taxiDriverInfo.image_taxi_qualification != null ? ($.server2 + taxiDriverInfo.image_taxi_qualification) : "");
            img7 = taxiDriverInfo.image_taxi_qualification != null ? taxiDriverInfo.image_taxi_qualification : "";
            $("#taxi-box ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#taxi-box ul li div b").html("请完善资料");
        } else if (taxiQualificationStatus == 3) { //成功
            $("#taxi-box #qualification-no").val(taxiDriverInfo.taxi_qualification_no != null ? taxiDriverInfo.taxi_qualification_no : "");
            $("#taxi-box input").attr("readonly", true);
            $("#taxi-box #img7").attr("src", taxiDriverInfo.image_taxi_qualification != null ? ($.server2 + taxiDriverInfo.image_taxi_qualification) : "");
            img7 = taxiDriverInfo.image_taxi_qualification != null ? taxiDriverInfo.image_taxi_qualification : "";
            $("#taxi-box ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#taxi-box ul li div b").html("审核通过");
        } else if (taxiQualificationStatus == 0 || taxiQualificationStatus == -1) { //未提交认证 数据无效
            $("#taxi-box input").attr("readonly", false);
            $("#taxi-box ul li div").addClass("none");
            $("#taxi-box ul li div b").html("");
        } else if (taxiQualificationStatus == -2) { //失败
            $("#taxi-box #qualification-no").val(taxiDriverInfo.taxi_qualification_no != null ? taxiDriverInfo.taxi_qualification_no : "");
            $("#taxi-box input").attr("readonly", false);
            $("#taxi-box #img7").attr("src", taxiDriverInfo.image_taxi_qualification != null ? ($.server2 + taxiDriverInfo.image_taxi_qualification) : "");
            img7 = taxiDriverInfo.image_taxi_qualification != null ? taxiDriverInfo.image_taxi_qualification : "";
            $("#taxi-box ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#taxi-box ul li div b").html("审核失败<br/>点击重新上传");
        }
    }
}

/**
 * 选择上传图片
 * @param index 选取上传下标
 */
function showImg(index) {
    if(taxiDriverInfo){
        if(index < 2){
            if (taxiDriverInfo.real_name_status > 0 && taxiDriverInfo.real_name_status != 2) {
                return;
            }
        }
        if(index == 2){
            if (taxiDriverInfo.drivers_license_status > 0 && taxiDriverInfo.drivers_license_status != 2) {
                return;
            }
        }
        if(index == 3 || index == 4){
            if (taxiDriverInfo.driving_license_status > 0 && taxiDriverInfo.driving_license_status != 2) {
                return;
            }
        }
        if(index == 5){
            if (taxiDriverInfo.photo_group_status > 0 && taxiDriverInfo.photo_group_status != 2) {
                return;
            }
        }
        if(index > 5){
            if (taxiDriverInfo.taxi_qualification_status > 0 && taxiDriverInfo.taxi_qualification_status != 2) {
                return;
            }
        }
    }
    if (!flag1){
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
            $("#demo-model > .model").unbind("click");
        }, 300);
    });
    $("#file").on("click", function () {
        $("#demo-model").animate({top: "100%", opacity: 0}, 300);
        setTimeout(function () {
            $("#demo-model").removeClass("demo-model").addClass("none");
            $("#demo-model > .model").unbind("click");
        }, 300);
    });
    $("#file").val("");
    $("#file").off("change");
    $("#file").on("change", function () {
        var file = $(".file").get(0).files[0];
        var render = new FileReader();
        render.readAsDataURL(file);
        render.onload = function (e) {
            // console.log(e);
            //console.log(e.target.result);
            if (index == 0) {
                uploadPicture(0, e.target.result, 1, "A", img1);    //身份证正面
            } else if (index == 1) {
                uploadPicture(1, e.target.result, 1, "B", img2);    //身份证反面
            } else if (index == 2) {
                uploadPicture(2, e.target.result, 2, "A", img3);    //驾驶证
            } else if (index == 3) {
                uploadPicture(3, e.target.result, 3, "A", img4);    //行驶证正面
            } else if (index == 4) {
                uploadPicture(4, e.target.result, 3, "B", img5);    //行驶证反面
            } else if (index == 5) {
                uploadPicture(5, e.target.result, 4, "A", img6);    // 人车照
            } else {
                uploadPicture(6, e.target.result, 5, "A", img7);    // 服务证照
            }
            file = null;
            $("#file").val("");
            $("#file").off("change");
        };
    });
}
/**
 * 图片上传
 * @param index 图片id
 * @param base64 图片base64
 * @param type 上传的类型123456 身份证，驾驶证，行驶证，人车合照，服务证照
 * @param extra 正反面 A、B
 * @param last_file 上次回传的文件名（服务器上的路径）
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
                console.log(res);
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
                        case 6:
                            img7 = path;
                            $("#img7").attr("src", data);
                            $("#img7-status").removeClass("authen-status-ing authen-status-over").addClass("none");
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
                // window.location.href = "../../Util/html/error.html";
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