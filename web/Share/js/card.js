/**
 * Created by keyC on 2018/7/10.
 * 名片分享
 */
var wxShareData; //分享到微信信息
var qqShareData; //分享到qq信息

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
/**
 * 获取参数
 */
function getParams() {
    getShareContent();
    $("ul li").bind("click", function () {
        var index = $(this).index();
        if(index == 0){
            if(wxShareData){
                showShareDemo(0, wxShareData)
            } else {
                getShareContent();
            }
        } else if(index == 1){
            if (qqShareData){
                showShareDemo(1, qqShareData)
            } else {
                getShareContent();
            }
        }
    });
}

/**
 * 获取我的分享信息
 * @param index 分享对应的type 1 微信， 2 朋友圈， 3 qq， 4 空间
 */
function getShareContent() {
    let params = {
        // user_id: $.user_id,
        // city_code: "028",
        // identity: "1",
        user_id: param.user_id,
        city_code: param.city_code,
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
                    if (data[i].type == "5") {
                        wxShareData = data[i];
                    } else if(data[i].type == "6") {
                        qqShareData = data[i];
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
 * 显示分享示例
 * @type {[*]} 示意图
 */
var shareDemo = ["../img/img_share_hint_wx.png", "../img/img_share_hint_qq.png"];
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
            openShareTo(index == 0 ? index : 2, JSON.stringify(data));
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