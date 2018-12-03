/**
 * Created by Administrator on 2018/9/10.
 */
$(function () {
   getRequest(getParams);
    // getParams();
});

/**
 * 获取参数
 */
function getParams() {
    let params = {
        // user_id: "38b8781d-5a56-47b8-98ca-a6fc9b71d8a8",
        // id: "2018091098501019"
        user_id: param.user_id,
        id: param.id
    };
    loadAlertShow("获取中...");
    $.ajax({
        type: 'POST',
        url: $.getPropertyDetail,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res.status == 1){

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
}