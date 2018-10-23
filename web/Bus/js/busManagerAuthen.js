/**
 * Created by Administrator on 2018/7/13.
 */
var img1 = "";
var img2 = "";
var img3 = "";
var img4 = "";
var index = -1;
var demoImg = [
    {text: "身份证正面照", img: "../img/id-card-front-demo.jpg"},
    {text: "身份证反面照", img: "../img/id-card-back-demo.jpg"},
    {text: "手持身份证正面照", img: "../img/id-card-hand-demo.jpg"},
    {text: "企业营业执照照", img: "../img/enterprise-license-demo.jpg"}
];
$(function () {
    var type = $("input[name='radio']:checked").val();
    if(type == "person"){
        $("#company").addClass("none");
        $("#company-title").removeClass("authen-title").addClass("none");
    } else {
        $("#company").removeClass("none");
        $("#company-title").removeClass("none").addClass("authen-title");
    }

    $(".authen-type-item").bind("click", function () {
        if (flag) {
            $(this).find("input").attr("checked", "true");
            var type = $("input[name='radio']:checked").val();
            if(type == "person"){
                $("#company").addClass("none");
                $("#company-title").removeClass("authen-title").addClass("none");
            } else {
                $("#company").removeClass("none");
                $("#company-title").removeClass("none").addClass("authen-title");
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
    $("#company-title").bind("click", function () {
        if(driverLicenseFlag){
            driverLicenseFlag = false;
            $("#company").animate({height: 0}, 300);
            $(this).find("i").addClass("img-transform");
        } else {
            driverLicenseFlag = true;
            var $obj = $("#company");
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

});

var flag = true; //阻止点击事件
var busManagerInfo; //大巴经营者认证信息

function getParams() {
    if(sessionStorage.getItem("busManagerInfo") != null){
        busManagerInfo = JSON.parse(sessionStorage.getItem("busManagerInfo"));
    }
    if(busManagerInfo) {
        showBusManagerInfo();
    } else {
        getBusManagerInfo();
    }
    //保存
    $("#submit").bind("click", function () {
        submitBusManager();
    });

}

//获取司机实名认证信息
function getBusManagerInfo() {
    let params = {
        // user_id: "4eaf93ae-2cf1-4798-95f6-4ae2516c30c5"
        user_id: param.user_id
    };
    loadAlertShow("获取中...");
    $.ajax({
        type: 'POST',
        url: $.getBusManagerInfo,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res.status == 1){
                busManagerInfo = res.data;
                if(busManagerInfo){
                    sessionStorage.setItem("busManagerInfo", JSON.stringify(busManagerInfo));
                    showBusManagerInfo();
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
function submitBusManager() {
    let params = {
        // user_id: "4eaf93ae-2cf1-4798-95f6-4ae2516c30c5",
        user_id: param.user_id,
        type: $("input[name='radio']:checked").val() == "person" ? "1" : "2",
        name: $("#name").val().trim(),
        ID: $("#id").val().trim(),
        image_id_a: img1,
        image_id_b: img2,
        image_group: img3,
        company_name: $("#company-name").val().trim(),
        unified_social_credit_code: $("#company-code").val().trim(),
        image_enterprise_license: img4
    };
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
    if (params.image_group == "" || params.image_group == null) {
        toastAlertShow("请上传手持身份证正面照");
        return;
    }
    if (params.type != "1") {
        if (params.company_name == "" || params.company_name == null) {
            toastAlertShow("请输入企业名称");
            return;
        }
        if (params.unified_social_credit_code == "" || params.unified_social_credit_code == null) {
            toastAlertShow("请输入企业编号");
            return;
        }
        if (params.image_enterprise_license == "" || params.image_enterprise_license == null) {
            toastAlertShow("请上传营业执照");
            return;
        }
    }
    console.log(params);
    loadAlertShow("提交中...");
    $.ajax({
        type: 'POST',
        url: $.submitBusManager,
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
function showBusManagerInfo() {
    if(busManagerInfo){
        let realNameStatus = busManagerInfo.real_name_status;
        let companyStatus = busManagerInfo.enterprise_license_status;
        let type = busManagerInfo.type;
        if (type == "2") {
            if(realNameStatus > 0 && companyStatus > 0) {
                flag = false;
                $('input:radio').attr('disabled', true);
                $("#submit").removeClass("submit-btn").addClass("none");
            } else {
                flag = true;
                $("#submit").removeClass("none").addClass("submit-btn");
            }
        } else {
            if(realNameStatus > 0) {
                flag = false;
                $('input:radio').attr('disabled', true);
                $("#submit").removeClass("submit-btn").addClass("none");
            } else {
                flag = true;
                $("#submit").removeClass("none").addClass("submit-btn");
            }
        }
        //注册类型
        if (type == 1) { //1自然人
            $("#input2").attr("checked", "true");
            $("#company").addClass("none");
            $("#company-title").removeClass("authen-title").addClass("none");

        } else { //2企业
            $("#input1").attr("checked", "true");
            $("#company").removeClass("none");
            $("#company-title").removeClass("none").addClass("authen-title");
        }
        //实名信息
        if(realNameStatus == 1) { //审核中
            $("#realName #name").val(busManagerInfo.name);
            $("#realName #id").val(busManagerInfo.ID);
            $("#realName input").attr("readonly", true);
            $("#realName #img1").attr("src", $.server2 + busManagerInfo.image_id_a);
            $("#realName #img2").attr("src", $.server2 + busManagerInfo.image_id_b);
            $("#realName #img3").attr("src", $.server2 + busManagerInfo.image_group);
            img1 = busManagerInfo.image_id_a;
            img2 = busManagerInfo.image_id_b;
            img3 = busManagerInfo.image_group;
            $("#realName ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#realName ul li div b").html("审核中");
        } else if (realNameStatus == 2 || realNameStatus == 3) { //成功
            $("#realName #name").val(busManagerInfo.name);
            $("#realName #id").val(busManagerInfo.ID);
            $("#realName input").attr("readonly", true);
            $("#realName #img1").attr("src", $.server2 + busManagerInfo.image_id_a);
            $("#realName #img2").attr("src", $.server2 + busManagerInfo.image_id_b);
            $("#realName #img3").attr("src", $.server2 + busManagerInfo.image_group);
            img1 = busManagerInfo.image_id_a;
            img2 = busManagerInfo.image_id_b;
            img3 = busManagerInfo.image_group;
            $("#realName ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#realName ul li div b").html("审核通过");
        } else if(realNameStatus == 0 || realNameStatus == -1) { //未提交认证 数据无效
            $("#realName input").attr("readonly", false);
            $("#realName ul li div").addClass("none");
            $("#realName ul li div b").html("");
        } else if(realNameStatus == -2) { //失败
            $("#realName #name").val(busManagerInfo.name);
            $("#realName #id").val(busManagerInfo.ID);
            $("#realName input").attr("readonly", false);
            $("#realName #img1").attr("src", $.server2 + busManagerInfo.image_id_a);
            $("#realName #img2").attr("src", $.server2 + busManagerInfo.image_id_b);
            $("#realName #img3").attr("src", $.server2 + busManagerInfo.image_group);
            img1 = busManagerInfo.image_id_a;
            img2 = busManagerInfo.image_id_b;
            img3 = busManagerInfo.image_group;
            $("#realName ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#realName ul li div b").html("审核失败<br/>点击重新上传");
        }
        //企业信息
        if (type == 2) {
            if(companyStatus == 1) { //审核中
                $("#company input").attr("disabled", true);
                $("#company #company-name").val(busManagerInfo.company_name);
                $("#company #company-code").val(busManagerInfo.code);
                $("#company #img4").attr("src", $.server2 + busManagerInfo.image_enterprise_license);
                img4 = busManagerInfo.image_enterprise_license;
                $("#company ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
                $("#company ul li div b").html("审核中");
            } else if (companyStatus == 2 || companyStatus == 3) { //成功
                $("#company input").attr("disabled", true);
                $("#company #company-name").val(busManagerInfo.company_name);
                $("#company #company-code").val(busManagerInfo.code);
                $("#company #img4").attr("src", $.server2 + busManagerInfo.image_enterprise_license);
                img4 = busManagerInfo.image_enterprise_license;
                $("#company ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
                $("#company ul li div b").html("审核通过");
            } else if(companyStatus == 0 || companyStatus == -1) { //未提交认证 数据无效
                $("#company input").attr("disabled", false);
                $("#company ul li div").addClass("none");
                $("#company ul li div b").html("");
            } else if(companyStatus == -2) { //失败
                $("#company input").attr("disabled", false);
                $("#company #company-name").val(busManagerInfo.company_name);
                $("#company #company-code").val(busManagerInfo.code);
                $("#company #img4").attr("src", $.server2 + busManagerInfo.image_enterprise_license);
                img4 = busManagerInfo.image_enterprise_license;
                $("#company ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
                $("#company ul li div b").html("审核失败<br/>点击重新上传");
            }
        }
    }
}

/**
 * 选择上传图片
 * @param index 选取上传下标
 */
function showImg(index) {
    if(busManagerInfo){
        if(index < 2){
            if (busManagerInfo.real_name_status > 0) {
                return;
            }
        }
        if(index == 2){
            if (busManagerInfo.drivers_license_status > 0) {
                return;
            }
        }
    }
    if (!flag){
        return;
    }
    $("#demo-model").removeClass("demo-none").addClass("demo-model");
    $("#demo-model").animate({top: "0", opacity: 1}, 300);
    $("#demo-model > .content > .title").text(demoImg[index].text);
    $("#demo-model > .content > .legend > img").attr("src", demoImg[index].img);
    $("#demo-model > .model").bind("click", function () {
        $("#demo-model").animate({top: "100%", opacity: 0}, 300);
        $("#file").val("");
        $("#file").off("change");
        setTimeout(function () {
            $("#demo-model").removeClass("demo-model").addClass("demo-none");
        }, 300);
    });
    $("#file").on("click", function () {
        $("#demo-model").animate({top: "100%", opacity: 0}, 300);
        setTimeout(function () {
            $("#demo-model").removeClass("demo-model").addClass("demo-none");
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
                uploadPicture(2, e.target.result, 4, "A", img3); //手持身份证正面照
            } else if (index == 3) {
                uploadPicture(3, e.target.result, 6, "A", img4); //企业营业执照照
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
            // user_id: "4eaf93ae-2cf1-4798-95f6-4ae2516c30c5",
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