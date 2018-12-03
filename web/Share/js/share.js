/**
 * Created by Administrator on 2018/7/9.
 */
let qrsrc = ""; //我的分享二维码
let wxShareData;
let pyqShareData;
let qqShareData;
let zoneShareData;

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
function getParams() {
    $("ul li").bind("click", function () {
        var index = $(this).index();
        switch (index){
            case 0:
                if(wxShareData){
                    showShareDemo(0, wxShareData);
                } else {
                    getShareContent(1)
                }
                break;
            case 1:
                if(pyqShareData){
                    showShareDemo(1, pyqShareData);
                } else {
                    getShareContent(2)
                }
                break;
            case 2:
                if(qqShareData){
                    showShareDemo(2, qqShareData);
                } else {
                    getShareContent(3)
                }
                break;
            case 3:
                if(zoneShareData){
                    showShareDemo(3, zoneShareData);
                } else {
                    getShareContent(4)
                }
                break;
            case 4:
                if(qrsrc == ""){
                    getShareQR();
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
//获取我的分享二维码
function getShareQR() {
    let params = {
        // user_id: "67b3934b-dfbd-4439-b6bd-ab2c92649cba"
        user_id: param.user_id
    };
    loadAlertShow("获取中...");
    $.ajax({
        type: 'POST',
        url: $.getShareQR,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res.status == 1){
                qrsrc = $.server1 + res.data;
                showShareQR();
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
//显示我的分享二维码
function showShareQR() {
    $("#demo-model").removeClass("none").addClass("demo-model");
    $("#demo-model .content .qr").attr("src", qrsrc);
    $("#demo-model").animate({top: "0", opacity: 1}, 300);
    $("#demo-model .content .close").bind("click", function () {
        $("#demo-model").animate({top: "100%", opacity: 0}, 300);
        setTimeout(function () {
            $("#demo-model").removeClass("demo-model").addClass("none");
        }, 300);
    });
    $("#demo-model .model").bind("click", function () {
        $("#demo-model").animate({top: "100%", opacity: 0}, 300);
        setTimeout(function () {
            $("#demo-model").removeClass("demo-model").addClass("none");
        }, 300);
    });
}
//获取我的分享信息
function getShareContent(index) {
    let params = {
        // city_code: "028",
        // user_id: "67b3934b-dfbd-4439-b6bd-ab2c92649cba",
        city_code: param.city_code,
        user_id: param.user_id,
        share_type: index
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
                if (index == 1) {
                    wxShareData = data;
                } else if(index == 2){
                    pyqShareData = data;
                } else if(index ==3){
                    qqShareData = data;
                } else {
                    zoneShareData = data;
                }
                showShareDemo(parseInt(index)-1, data);
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
//显示分享示例
let shareDemo = ["../img/img_share_hint_wx.png", "../img/img_share_hint_wxpyq.png", "../img/img_share_hint_qq.png", "../img/img_share_hint_qqzone.png"];
function showShareDemo(index, data) {
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
            }, 300);
        });
        $("#share-demo-model .model").bind("click", function () {
            $("#share-demo-model").animate({top: "100%", opacity: 0}, 300);
            setTimeout(function () {
                $("#share-demo-model").removeClass("share-demo-model").addClass("none");
            }, 300);
        });
    }
}
