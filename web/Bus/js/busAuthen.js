/**
 * Created by Administrator on 2018/7/13.
 */
let img1 = "";
let index = -1;
let busTypeId = "";
let demoImg = [
    {text: "道路运输许可证", img: "../img/id-card-front-demo.jpg"}
];
$(function () {
    getRequest(getParams);
    // getParams();

    //车牌
    let templatePlateModels = document.getElementById('template-plate-short-list').innerHTML;
    document.getElementById('plate-short-list').innerHTML = doT.template(templatePlateModels)($.plateShortList);

    //车辆颜色
    let templateBusColor = document.getElementById('template-plate-color').innerHTML;
    document.getElementById('bus-color-list').innerHTML = doT.template(templateBusColor)($.color);

    //选择车牌简称
    $("#plate-no-short-box").bind("click", function () {
        if(busInfo){
            if(busInfo.status > 0){
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
    //选择车辆颜色
    $("#plate-color").bind("click", function () {
        if(busInfo){
            if(busInfo.status > 0){
                return;
            }
        }
        if (!flag){
            return;
        }
        $("#bus-color-list li span").removeClass("orange-text");
        $("#bus-color").removeClass("plate-model-animation-out").addClass("plate-model-animation-in");
        $("#bus-color-list li, #bus-color > .model").bind("click", function () {
            $(this).find("span").addClass("orange-text");
            $("#bus-color").removeClass("plate-model-animation-in").addClass("plate-model-animation-out");
            $("#plate-color").val($.color[$(this).index()].name);
        });
    });
    //选择车辆类型
    $("#plate-type").bind("click", function () {
        if(busInfo){
            if(busInfo.status > 0){
                return;
            }
        }
        if (!flag){
            return;
        }
        //获取车辆大类型
        getBusType1();

    });
});
//获取车辆大类型
function getBusType1() {
    let params = {
        // user_id: "57ac9ac4-dfcb-4537-9122-f5a7e2b20813",
        user_id: param.user_id,
        type: "1"
    };
    loadAlertShow("获取中...");
    $.ajax({
        type: 'POST',
        url: $.getBusType,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res.status == 1){
                let data = res.data;
                //车辆类型
                var templateBusType = document.getElementById('template-plate-type').innerHTML;
                document.getElementById('bus-type-list').innerHTML = doT.template(templateBusType)(data);
                $("#bus-type-list li span").removeClass("orange-text");
                $("#bus-type").removeClass("plate-model-animation-out").addClass("plate-model-animation-in");
                $("#bus-type > .model").bind("click", function () {
                    $("#bus-type").removeClass("plate-model-animation-in").addClass("plate-model-animation-out");
                    if (flag3) {
                        $("#bus-type2").removeClass("plate-model-animation-in").addClass("plate-model-animation-out");
                        flag3 = false;
                    }
                });
                $("#bus-type-list li").bind("click", function () {
                    $("#bus-type-list li").find("span").removeClass("orange-text");
                    $(this).find("span").addClass("orange-text");
                    getBusType2(data[$(this).index()].type);
                });
            } else {
                toastAlertShow(res.msg, 2500);
            }
        },
        error: function (err) {
            console.log(err);
            loadAlertHide();
        }
    });
}
//获取车辆小类型
function getBusType2(type) {
    let params = {
        // user_id: "57ac9ac4-dfcb-4537-9122-f5a7e2b20813",
        user_id: param.user_id,
        type: type
    };
    loadAlertShow("获取中...");
    $.ajax({
        type: 'POST',
        url: $.getBusType,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res.status == 1){
                flag3 = true;
                let data = res.data;
                //车辆类型
                var templateBusType2 = document.getElementById('template-plate-type2').innerHTML;
                document.getElementById('bus-type-list2').innerHTML = doT.template(templateBusType2)(data);
                $("#bus-type-list2 li span").removeClass("orange-text");
                $("#bus-type2").removeClass("plate-model-animation-out").addClass("plate-model-animation-in");
                $("#bus-type-list2 li").bind("click", function () {
                    $(this).find("span").addClass("orange-text");
                    $("#bus-type").removeClass("plate-model-animation-in").addClass("plate-model-animation-out");
                    $("#bus-type2").removeClass("plate-model-animation-in").addClass("plate-model-animation-out");
                    $("#plate-type").val(data[$(this).index()].bus_class_name);
                    flag3 = false;
                    busTypeId = data[$(this).index()].bus_type_id;
                    console.log(busTypeId);
                });
            } else {
                toastAlertShow(res.msg, 2500);
            }
        },
        error: function (err) {
            console.log(err);
            loadAlertHide();
        }
    });
}

var flag = true; //阻止点击事件
var flag3 = false; //是否展开了二级页面
var busInfo; //大巴经营者认证信息

function getParams() {
    if(sessionStorage.getItem("busInfo") != null){
        busInfo = JSON.parse(sessionStorage.getItem("busInfo"));
    }
    if(busInfo) {
        showBusInfo();
    } else {
        getBusInfo();
    }
    //保存
    $("#submit").bind("click", function () {
        submitBusInfo();
    });

}

//获取司机实名认证信息
function getBusInfo() {
    if (param.bus_no == null) {
        return;
    }
    let params = {
        // user_id: "57ac9ac4-dfcb-4537-9122-f5a7e2b20813",
        // bus_no: "5"
        user_id: param.user_id,
        bus_no: param.bus_no
    };
    loadAlertShow("获取中...");
    $.ajax({
        type: 'POST',
        url: $.getBusInfo,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res.status == 1){
                busInfo = res.data;
                if(busInfo){
                    sessionStorage.setItem("busInfo", JSON.stringify(busInfo));
                    showBusInfo();
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
function submitBusInfo() {
    // user_id: "57ac9ac4-dfcb-4537-9122-f5a7e2b20813",
    let params = {};
    params.user_id = param.user_id;
    if (param.bus_no != null) {
        params.bus_no = param.bus_no;
    }
    params.image = img1;
    params.name = $("#name").val().trim();
    params.short_no = $("#plate-no-short").val().trim();
    params.plate_no = $("#plate-no").val().trim();
    params.color = $("#plate-color").val().trim();
    params.type = busTypeId;
    params.seats = $("#plate-seats").val().trim();
    console.log(params);
    if (params.image == "" || params.image == null) {
        toastAlertShow("请上传道路运输许可证");
        return;
    }
    if (params.name == "" || params.name == null) {
        toastAlertShow("请输入业主姓名");
        return;
    }
    if (params.short_no == "" || params.short_no == null) {
        toastAlertShow("请选择车牌简称");
        return;
    }
    if (params.plate_no == "" || params.plate_no == null) {
        toastAlertShow("请输入车牌号");
        return;
    }
    if (params.color == "" || params.color == null) {
        toastAlertShow("请选择车辆颜色");
        return;
    }
    if (params.type == "" || params.type == null) {
        toastAlertShow("请选择车辆类型");
        return;
    }
    if (params.seats == "" || params.seats == null) {
        toastAlertShow("请输入荷载人数");
        return;
    }
    loadAlertShow("提交中...");
    $.ajax({
        type: 'POST',
        url: $.submitBusInfo,
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
function showBusInfo() {
    if(busInfo){
        let status = busInfo.status;
        if(status > 0) {
            flag = false;
            $('input:radio').attr('disabled', true);
            $("#submit").removeClass("submit-btn").addClass("none");
        } else {
            flag = true;
            $("#submit").removeClass("none").addClass("submit-btn");
        }
        //实名信息
        if(status == 1) { //审核中
            $("#realName #name").val(busInfo.name);
            $("#realName #plate-no-short").val(busInfo.plate_for_short);
            $("#realName #plate-no").val(busInfo.plate_no);
            $("#realName #plate-color").val(busInfo.bus_color);
            $("#realName #plate-type").val(busInfo.bus_type_name);
            $("#realName #plate-seats").val(busInfo.bus_seat);
            $("#realName input").attr("readonly", true);
            $("#realName #img1").attr("src", $.server2 + busInfo.image);
            img1 = busInfo.image;
            $("#realName ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#realName ul li div b").html("审核中");
        } else if (status == 2 || status == 3) { //成功
            $("#realName #name").val(busInfo.name);
            $("#realName #plate-no-short").val(busInfo.plate_for_short);
            $("#realName #plate-no").val(busInfo.plate_no);
            $("#realName #plate-color").val(busInfo.bus_color);
            $("#realName #plate-type").val(busInfo.bus_type_name);
            $("#realName #plate-seats").val(busInfo.bus_seat);
            $("#realName input").attr("readonly", true);
            $("#realName #img1").attr("src", $.server2 + busInfo.image);
            img1 = busInfo.image;
            $("#realName ul li div").removeClass("none authen-status-over").addClass("authen-status-ing");
            $("#realName ul li div b").html("审核通过");
        } else if(status == 0 || status == -1) { //未提交认证 数据无效
            $("#realName input").attr("readonly", false);
            $("#realName ul li div").addClass("none");
            $("#realName ul li div b").html("");
        } else if(status == -2) { //失败
            $("#realName #name").val(busInfo.name);
            $("#realName #plate-no-short").val(busInfo.plate_for_short);
            $("#realName #plate-no").val(busInfo.plate_no);
            $("#realName #plate-color").val(busInfo.bus_color);
            $("#realName #plate-type").val(busInfo.bus_type_name);
            $("#realName #plate-seats").val(busInfo.bus_seat);
            $("#realName input").attr("readonly", false);
            $("#realName #img1").attr("src", $.server2 + busInfo.image);
            img1 = busInfo.image;
            $("#realName ul li div").removeClass("none authen-status-ing").addClass("authen-status-over");
            $("#realName ul li div b").html("审核失败<br/>点击重新上传");
        }
    }
}

/**
 * 选择上传图片
 * @param index 选取上传下标
 */
function showImg(index) {
    if(busInfo){
        if(index < 2){
            if (busInfo.real_name_status > 0) {
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
                uploadPicture(0, e.target.result, 7, "A", img1);  //大巴车资质照
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
 * @param type 上传的类型1234567 身份证，驾驶证，行驶证，人车合照，上岗证，从业资格证, 大巴车资质照
 * @param extra 正反面 A、B
 * @param last_file 上次回传的文件名（服务器上的路径
 */
function uploadPicture(index, base64, type, extra, last_file) {
    loadAlertShow("正在上传...");
    canvasDataURL(base64, function callback(data) {
        let params = {
            // user_id: "57ac9ac4-dfcb-4537-9122-f5a7e2b20813",
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