/**
 * Created by zp on 2018/7/13.
 */
var img1 = "";
var img2 = "";
var img3 = "";
var img4 = "";
var index = -1;
var demoImg = [
    {text: "身份证正面照", img: "../img/id-card-front-demo.jpg"},
    {text: "身份证反面照", img: "../img/id-card-back-demo.jpg"},
    {text: "手持身份证半身照", img: "../img/id-card-hand-demo.jpg"},
    {text: "企业营业执照", img: "../img/enterprise-license-demo.jpg"}
];
$(function () {

    var type = $("input[name='radio']:checked").val();
    if(type == "personal"){
        $("#photo-enterprise-license-box").addClass("none");
    } else {
        $("#photo-enterprise-license-box").removeClass("none");
    }

    $(".authen-type-item").bind("click", function () {
        $(this).find("input").attr("checked", "true");
        var type = $("input[name='radio']:checked").val();
        if(type == "personal"){
            $("#photo-enterprise-license-box").addClass("none");
        } else {
            $("#photo-enterprise-license-box").removeClass("none");
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
    var enterpriseLicenseFlag = true;
    $("#photo-enterprise-license-title").bind("click", function () {
        if(enterpriseLicenseFlag){
            enterpriseLicenseFlag = false;
            $("#photo-enterprise-license").animate({height: 0}, 300);
            $(this).find("i").addClass("img-transform");
        } else {
            enterpriseLicenseFlag = true;
            var $obj = $("#photo-enterprise-license");
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
        submitBusOperator();
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

});

var busOperatorInfo; //大巴车经营者认证信息
var cityInfo; //选择城市信息

function getParams() {
    if(sessionStorage.getItem("busOperatorInfo") != null){
        busOperatorInfo = JSON.parse(sessionStorage.getItem("busOperatorInfo"));
    }
    if(busOperatorInfo) {
        showBusOperatorInfo();
    } else {
        getBusOperatorInfo();
    }
}

//获取司机实名认证信息
function getBusOperatorInfo() {
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
                busOperatorInfo = res.data;
                if(busOperatorInfo){
                    sessionStorage.setItem("busOperatorInfo", JSON.stringify(busOperatorInfo));
                    showBusOperatorInfo();
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
function submitBusOperator() {
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
                    showBusOperatorInfo();
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
function showBusOperatorInfo() {
    if(busOperatorInfo){

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
            } else {
                uploadPicture(3, e.target.result, 3, "A", img4); // 人车照
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