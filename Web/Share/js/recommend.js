/**
 * Created by keyC on 2018/7/10.
 * 获取推荐人数
 */
$(function () {
    getRequest(getParams);
    // getParams();
});

/**
 * 获取app传递过来的参数
 */
function getParams() {
    loadAlertShow("正在获取...");
    let params = {
        // user_id: $.user_id,
        user_id: param.user_id,
        grade: 0
    };
    console.log("params", params);
    $.ajax({
        type: 'POST',
        url: $.getRecommendDetail,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res.status == 1){
                let data = res.data;
                $("#invite_total").text(data.invite_total);
                $("#invite_one").text(data.invite_one);
                $("#invite_two").text(data.invite_two);
                $("#invite_three").text(data.invite_three);
                $("#invite_four").text(data.invite_four);
            } else {
                toastAlertShow(res.msg);
            }
        },
        error: function (err) {
            loadAlertHide();
            window.location.href = "../../Util/html/error.html";
            console.log(err);
        }
    });
}
