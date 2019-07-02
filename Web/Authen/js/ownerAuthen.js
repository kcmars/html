/**
 * Created by keyC on 2018/7/13.
 */
var img1 = "";
var img2 = "";
var img3 = "";
var img4 = "";
var img5 = "";
var img6 = "";
var img7 = "";
var img8 = "";
var img9 = "";
var index = -1;
var demoImg = [
    {text: "身份证正面照", img: "../img/id-card-front-demo.jpg"},
    {text: "身份证反面照", img: "../img/id-card-back-demo.jpg"},
    {text: "手持身份证半身照", img: "../img/id-card-hand-demo.jpg"},
    {text: "驾驶证正本照", img: "../img/driver-license-front-demo.jpg"},
    {text: "驾驶证副本照", img: "../img/driver-license-back-demo.jpg"},
    {text: "行驶证正本照", img: "../img/driving-license-front-demo.jpg"},
    {text: "行驶证副本照", img: "../img/driving-license-back-demo.jpg"},
    {text: "上岗证正面照", img: "../img/work-license-demo.jpg"},
    {text: "从业资格证信息照", img: "../img/taxi-qualification-demo.jpg"}
];
$(function () {
    var type = $("input[name='radio']:checked").val();
    if(type == "express"){
        $("#taxi-box").addClass("none");
    } else {
        $("#taxi-box").removeClass("none");
    }

    $(".authen-type-item").bind("click", function () {
        $(this).find("input").attr("checked", "true");
        var type = $("input[name='radio']:checked").val();
        if(type == "express"){
            $("#taxi-box").addClass("none");
        } else {
            $("#taxi-box").removeClass("none");
        }
    });

    var realNameFlag = false;
    $("#realName-title").bind("click", function () {
        if(realNameFlag){
            realNameFlag = false;
            $("#realName").animate({height: 0}, 300);
            $(this).find("i").removeClass("img-transform");
        } else {
            realNameFlag = true;
            var $obj = $("#realName");
            var currentHeight = $obj.css("height");
            $obj.css("height","auto");
            var animateHeight = $obj.css("height");
            $obj.css("height", currentHeight);
            $obj.animate({height: animateHeight}, 300);
            $(this).find("i").addClass("img-transform");
        }
    });
    var driverLicenseFlag = false;
    $("#driver-title").bind("click", function () {
        if(driverLicenseFlag){
            driverLicenseFlag = false;
            $("#driver-license").animate({height: 0}, 300);
            $(this).find("i").removeClass("img-transform");
        } else {
            driverLicenseFlag = true;
            var $obj = $("#driver-license");
            var currentHeight = $obj.css("height");
            $obj.css("height","auto");
            var animateHeight = $obj.css("height");
            $obj.css("height", currentHeight);
            $obj.animate({height: animateHeight}, 300);
            $(this).find("i").addClass("img-transform");
        }
    });
    var drivingLicenseFlag = false;
    $("#driving-title").bind("click", function () {
        if(drivingLicenseFlag){
            drivingLicenseFlag = false;
            $("#driving-license").animate({height: 0}, 300);
            $(this).find("i").removeClass("img-transform");
        } else {
            drivingLicenseFlag = true;
            var $obj = $("#driving-license");
            var currentHeight = $obj.css("height");
            $obj.css("height","auto");
            var animateHeight = $obj.css("height");
            $obj.css("height", currentHeight);
            $obj.animate({height: animateHeight}, 300);
            $(this).find("i").addClass("img-transform");
        }
    });
    var personCarFlag = false;
    $("#photo-person-car-title").bind("click", function () {
        if(personCarFlag){
            personCarFlag = false;
            $("#photo-person-car").animate({height: 0}, 300);
            $(this).find("i").removeClass("img-transform");
        } else {
            personCarFlag = true;
            var $obj = $("#photo-person-car");
            var currentHeight = $obj.css("height");
            $obj.css("height","auto");
            var animateHeight = $obj.css("height");
            $obj.css("height", currentHeight);
            $obj.animate({height: animateHeight}, 300);
            $(this).find("i").addClass("img-transform");
        }
    });
    var taxiFlag = false;
    $("#taxi-title").bind("click", function () {
        if(taxiFlag){
            taxiFlag = false;
            $("#taxi").animate({height: 0}, 300);
            $(this).find("i").removeClass("img-transform");
        } else {
            taxiFlag = true;
            var $obj = $("#taxi");
            var currentHeight = $obj.css("height");
            $obj.css("height","auto");
            var animateHeight = $obj.css("height");
            $obj.css("height", currentHeight);
            $obj.animate({height: animateHeight}, 300);
            $(this).find("i").addClass("img-transform");
        }
    });

    // getRequest(getParams());
    getParams();

});

function getParams() {
    getExpressDriverInfo();
}

/**
 * 图片上传
 * @param index 图片id
 * @param base64 图片base64
 * @param type 上传的类型123456 身份证，驾驶证，行驶证，人车合照，上岗证，从业资格证
 * @param extra 正反面 A、B
 * @param last_file 上次回传的文件名（服务器上的路径）
 */
function uploadPicture(index, base64, type, extra, last_file) {
    let params = {
        user_id: "d7a48d9e-93aa-4733-b59e-fdd5b3d6a980",
        base64: base64,
        type: type,
        extra: extra,
        last_file: last_file
    };
    loadAlertShow("正在上传...");
    $.ajax({
        type: 'POST',
        url: $.uploadPicture,
        data: params,
        success: function (res) {
            console.log(res);
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
                    case 6:
                        img7 = path;
                        $("#img7").attr("src", base64);
                        break;
                    case 7:
                        img8 = path;
                        $("#img8").attr("src", base64);
                        break;
                    case 8:
                        img9 = path;
                        $("#img9").attr("src", base64);
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
            toastAlertShow(err.msg);
        }
    });
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
                let data = res.data;

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
        user_id: "d7a48d9e-93aa-4733-b59e-fdd5b3d6a980",
        name: name,
        ID: id,
        image_a: image_a,
        image_b: image_b
    };
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

function showImg(index) {
    console.log("index= ", index);
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
        console.log("index= ", index);
        var file = $(".file").get(0).files[0];
        var render = new FileReader();
        render.readAsDataURL(file);
        render.onload = function (e) {
            // console.log(e);
            console.log(e.target.result);
            if (index == 0) {
                uploadPicture(0, e.target.result, 1, "A", img1);
            } else if (index == 1) {
                uploadPicture(1, e.target.result, 1, "B", img2);
            } else if (index == 2) {
                uploadPicture(2, e.target.result, 2, "A", img3);
            } else if (index == 3) {
                uploadPicture(3, e.target.result, 2, "B", img4);
            } else if (index == 4) {
                uploadPicture(4, e.target.result, 3, "A", img5);
            } else if (index == 5) {
                uploadPicture(5, e.target.result, 3, "B", img6);
            } else if (index == 6) {
                uploadPicture(6, e.target.result, 4, "A", img7);
            } else if (index == 7) {
                uploadPicture(7, e.target.result, 5, "A", img8);
            } else {
                uploadPicture(8, e.target.result, 6, "A", img9);
            }
            file = null;
            $("#file").val("");
            $("#file").off("change")
        }
    });
}
