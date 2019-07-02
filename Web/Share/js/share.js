/**
 * Created by keyC on 2018/7/9.
 * 推荐与分享
 */
var qrsrc = "";//我的分享二维码
var wxShareData;//分享微信信息
var pyqShareData;//分享朋友圈信息
var qqShareData;//分享qq信息
var zoneShareData;//分享空间信息

//生成二维码工具
var qrcode = new QRCode("qr_code", {
    width: 128,
    height: 128,
    colorDark : "#000000",
    colorLight : "#ffffff",
    correctLevel : QRCode.CorrectLevel.H
});

$(function () {
    var u = navigator.userAgent;
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
    var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
    if(isiOS) {
        $("#share-img").attr("src","../img/share_ios_img.jpg");
        $("#ios-rules").removeClass("none");
        $("#android-rules").addClass("none");
    } else {
        $("#share-img").attr("src","../img/share_android_img.jpg");
        $("#android-rules").removeClass("none");
        $("#ios-rules").addClass("none");
    }

    getRequest(getParams);
    // getParams();
});

/**
 * 获取app传递过来的参数
 */
function getParams() {
    getShareContent();
    $("ul li").bind("click", function () {
        var index = $(this).index();
        switch (index){
            case 0:
                if(wxShareData){
                    showShareDemo(0, wxShareData);
                } else {
                    getShareContent();
                }
                break;
            case 1:
                if(pyqShareData){
                    showShareDemo(1, pyqShareData);
                } else {
                    getShareContent();
                }
                break;
            case 2:
                if(qqShareData){
                    showShareDemo(2, qqShareData);
                } else {
                    getShareContent();
                }
                break;
            case 3:
                if(zoneShareData){
                    showShareDemo(3, zoneShareData);
                } else {
                    getShareContent();
                }
                break;
            case 4:
                if(qrsrc == ""){
                    getShareContent();
                } else {
                    showShareQR();
                }
                break;
            case 5:
                window.location.href = "./card.html";
                break;
            default:break;
        }
    });
}
/**
 * 获取我的分享信息
 */
function getShareContent() {
    let params = {
        // city_code: "028",
        // user_id: $.user_id,
        // identity: "1"
        city_code: param.city_code,
        user_id: param.user_id,
        identity: param.identity
    };
    loadAlertShow("获取中...");
    $.ajax({
        type: 'POST',
        url: $.getShareContent,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res.status == 1){
                let data = res.data;
                for (let i = 0; i < data.length; i++) {
                    if (data[i].type == "1") {
                        wxShareData = data[i];
                    } else if(data[i].type == "2") {
                        pyqShareData = data[i];
                    } else if(data[i].type == "3") {
                        qqShareData = data[i];
                    } else if (data[i].type == "4") {
                        zoneShareData = data[i];
                    } else if (data[i].type == "7") {
                        qrsrc = data[i].url;
                    }
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
 * 显示我的分享二维码
 */
function showShareQR() {
    //设置要生成二维码的链接
    qrcode.clear(); // 清除代码
    qrcode.makeCode(qrsrc);
    $("#demo-model").removeClass("none").addClass("demo-model");
    $("#demo-model").animate({top: "0", opacity: 1}, 300);
    $("#demo-model .content .close").bind("click", function () {
        $("#demo-model").animate({top: "100%", opacity: 0}, 300);
        setTimeout(function () {
            $("#demo-model").removeClass("demo-model").addClass("none");
            $("#demo-model .content .close").unbind("click");
            $("#demo-model .model").unbind("click");
        }, 300);
    });
    $("#demo-model .model").bind("click", function () {
        $("#demo-model").animate({top: "100%", opacity: 0}, 300);
        setTimeout(function () {
            $("#demo-model").removeClass("demo-model").addClass("none");
            $("#demo-model .content .close").unbind("click");
            $("#demo-model .model").unbind("click");
        }, 300);
    });
}

/**
 * 显示分享示例
 * @type {[*]}
 */
let shareDemo = ["../img/img_share_hint_wx.png", "../img/img_share_hint_wxpyq.png", "../img/img_share_hint_qq.png", "../img/img_share_hint_qqzone.png"];
function showShareDemo(index, data) {
    if (data == null) {
        toastAlertShow("未获取到分享信息，请返回重试");
        return;
    }
    if(index <4) {
        $("#share-demo-model").removeClass("none").addClass("share-demo-model");
        $("#share-demo-model .content img").attr("src", shareDemo[index]);
        $("#share-demo-model").animate({top: "0", opacity: 1}, 300);
        $("#share-demo-model .content img").bind("click", function () {
            //data 传给原生
            openShareTo(index, JSON.stringify(data));
            $("#share-demo-model").animate({top: "100%", opacity: 0}, 300);
            setTimeout(function () {
                $("#share-demo-model").removeClass("share-demo-model").addClass("none");
                $("#share-demo-model .content img").unbind("click");
                $("#share-demo-model .model").unbind("click");
            }, 300);
        });
        $("#share-demo-model .model").bind("click", function () {
            $("#share-demo-model").animate({top: "100%", opacity: 0}, 300);
            setTimeout(function () {
                $("#share-demo-model").removeClass("share-demo-model").addClass("none");
                $("#share-demo-model .content img").unbind("click");
                $("#share-demo-model .model").unbind("click");
            }, 300);
        });
    }
}
