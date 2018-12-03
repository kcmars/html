/**
 * Created by zp on 2018/7/13.
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

    // getRequest(getParams());
    getParams();

    //保存
    $("#submit").bind("click", function () {
        submitExpressDriver();
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

    var plateShortList = ["京", "津", "沪", "渝", "黑", "吉", "辽", "甘", "陕", "贵", "云", "川", "晋", "冀", "青",
        "鲁", "豫", "苏", "皖", "浙", "闽", "赣", "湘", "鄂", "粤", "琼", "蒙", "新", "藏", "宁", "桂", "港", "澳"];
    var templatePlateModels = document.getElementById('template-plate-short-list').innerHTML;
    document.getElementById('plate-short-list').innerHTML = doT.template(templatePlateModels)(plateShortList);

    //选择车牌简称
    $("#plate-no-short-box").bind("click", function () {
        $("#plate-short-list li span").removeClass("orange-text");
        $("#plate-model").removeClass("plate-model-animation-out").addClass("plate-model-animation-in");
        $("#plate-short-list li, #plate-model > .model").bind("click", function () {
            $(this).find("span").addClass("orange-text");
            $("#plate-model").removeClass("plate-model-animation-in").addClass("plate-model-animation-out");
            $("#plate-no-short").val(plateShortList[$(this).index()]);
        });
    });

    //添加选择城市的组件
    $("#select-city").bind("click", function () {
        $.ajax({
            type:"get",
            url:"/AiYunBaoWeb/Util/html/citys.html",
            async:true,
            success:function(data){
                $("#select-model-box").empty();
                $("#select-model-box").html(data);
                $("#select-model-box").removeClass("plate-model-animation-out").addClass("plate-model-animation-in");
            }
        });
    });
    //添加选择车辆的组件
    $("#select-car").bind("click", function () {
        $.ajax({
            type:"get",
            url:"/AiYunBaoWeb/Util/html/cars.html",
            async:true,
            success:function(data){
                $("#select-model-box").empty();
                $("#select-model-box").html(data);
                $("#select-model-box").removeClass("plate-model-animation-out").addClass("plate-model-animation-in");
            }
        });
    });

});

var driverInfo; //司机认证信息
var carInfo; //选择车辆信息
var cityInfo; //选择城市信息

function getParams() {
    if(sessionStorage.getItem("cityInfo") != null){
        cityInfo = JSON.parse(sessionStorage.getItem("cityInfo"));
    }
    if(sessionStorage.getItem("carInfo") != null){
        carInfo = JSON.parse(sessionStorage.getItem("carInfo"));
    }
    if(sessionStorage.getItem("driverInfo") != null){
        driverInfo = JSON.parse(sessionStorage.getItem("driverInfo"));
    }
    if(driverInfo) {
        showExpressDriverInfo();
    } else {
        getExpressDriverInfo();
    }
}

//获取司机实名认证信息
function getExpressDriverInfo() {
    let params = {
        user_id: "d7a48d9e-93aa-4733-b59e-fdd5b3d6a980"
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
                toastAlertShow(res.msg);
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
    let params = {
        city_code: "20",
        city_name: "成都市",
        name: $("#name").val(),
        ID: $("#id").val(),
        image_id_a: img1,
        image_id_b: img2,
        image_drivers: img3,
        first_issue: $("#first-issue").val(),
        brand: "宝马",
        model: "Q5",
        color: "白色",
        plate_no_short: $("#plate-no-short").val(),
        plate_no: $("#plate-no").val(),
        vehicle_owner: $("#vehicle-owner").val(),
        register_date: $("#register-date").val(),
        image_driving_a: img4,
        image_driving_b: img5,
        image_group: img6
    };
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
                let data = res.data;
                if( data ){
                    showDriverAuthenInfo();
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
}

/**
 * 显示司机认证信息
 */
function showExpressDriverInfo() {
    if(driverInfo){

    }
    if(carInfo){
        $("#car").val(carInfo.brand + " " + carInfo.model + " " + carInfo.color);
    } else {
        $("#car").val("");
    }
    if(cityInfo){
        $("#city").val(cityInfo.city);
    } else {
        $("#city").val("");
    }
}

/**
 * 选择上传图片
 * @param index 选取上传下标
 */
function showImg(index) {
    $("#demo-model").animate({top: "0", opacity: 1}, 300);
    $("#demo-model > .content > .title").text(demoImg[index].text);
    $("#demo-model > .content > .legend > img").attr("src", demoImg[index].img);
    $("#demo-model > .model").bind("click", function () {
        $("#demo-model").animate({top: "100%", opacity: 0}, 300);
    });
    $("#file").on("click", function () {
        $("#demo-model").animate({top: "100%", opacity: 0}, 300);
    });
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
    let params = {
        user_id: "d7a48d9e-93aa-4733-b59e-fdd5b3d6a980",
        base64: base64,
        type: type,
        extra: extra,
        last_file: last_file
    };
    console.log(params);
    loadAlertShow("正在上传...");
    $.ajax({
        type: 'POST',
        url: $.uploadPicture,
        data: params,
        success: function (res) {
            //console.log(res);
            loadAlertHide();
            if(res.status == 1){
                let path = $.server2 +  res.data;
                switch (index) {
                    case 0:
                        img1 = path;
                        $("#img1").attr("src", base64);
                        break;
                    case 1:
                        img2 = path;
                        $("#img2").attr("src", base64);
                        break;
                    case 2:
                        img3 = path;
                        $("#img3").attr("src", base64);
                        break;
                    case 3:
                        img4 = path;
                        $("#img4").attr("src", base64);
                        break;
                    case 4:
                        img5 = path;
                        $("#img5").attr("src", base64);
                        break;
                    case 5:
                        img6 = path;
                        $("#img6").attr("src", base64);
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
}