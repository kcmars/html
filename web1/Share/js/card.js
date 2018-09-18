/**
 * Created by Administrator on 2018/7/10.
 */
let wxShareData;
let qqShareData;
$(function () {
    var u = navigator.userAgent;
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
    var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端

    if(isiOS) {
        $("#ios-rules").removeClass("none");
        $("#android-rules").addClass("none");
    } else {
        $("#android-rules").removeClass("none");
        $("#ios-rules").addClass("none");
    }

    getRequest(getParams);
    // getParams();
});
function getParams() {
    $("ul li").bind("click", function () {
        var index = $(this).index();
        if(index == 0){
            if(wxShareData){
                showShareDemo(0, wxShareData)
            } else {
                getShareContent(5);
            }
        } else if(index == 1){
            if (qqShareData){
                showShareDemo(2, qqShareData)
            } else {
                getShareContent(6);
            }
        }
    });
}

//获取我的分享信息
function getShareContent(index) {
    let params = {
        city_code: param.city_code,
        // user_id: "c7126d8a-7359-45cf-9aff-6984b4d7617f",
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
                if (index == 5){
                    wxShareData = data;
                } else {
                    qqShareData = data;
                }
                showShareDemo(parseInt(index)-5, data);
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
let shareDemo = ["../img/img_share_hint_wx.png", "../img/img_share_hint_qq.png"];
function showShareDemo(index, data) {
    if(index <2) {
        $("#share-demo-model").removeClass("none").addClass("share-demo-model");
        $("#share-demo-model .content img").attr("src", shareDemo[index]);
        $("#share-demo-model").animate({top: "0", opacity: 1}, 300);
        $("#share-demo-model .content img").bind("click", function () {
            //data 传给原生
            openShareTo(index == 0 ? index : 2, data);
            $("#share-demo-model").animate({top: "100%", opacity: 0}, 300);
            setTimeout(function () {
                $("#share-demo-model").removeClass("share-demo-model").addClass("none");
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