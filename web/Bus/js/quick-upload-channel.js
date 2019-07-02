/**
 * Created by keyC on 2019/1/17.
 * 快速上传名片通道
 */
$(function () {

    getRequest(getCardUploadStatus);
		// getCardUploadStatus();
});

/**
 * 获取参数
 */
function getCardUploadStatus() {
    $("#demo-picture li").bind("click", function () {
        $("#model-big-picture").removeClass("none");
        let index = $(this).index();
        if(index == 0){
            $("#big-picture").attr("src","../img/card_face_demo.jpg");
        } else {
            $("#big-picture").attr("src","../img/card_back_demo.jpg");
        }
    });
    $("#model-big-picture .big-bg").bind("click", function () {
        $("#model-big-picture").addClass("none");
    });

    $.ajax({
        type: 'POST',
        url: $.getUploaderStatus,
        data: {
            // user_id: $.user_id
            user_id: param.user_id
        },
        success: function (res) {
            console.log("res==", res);
            if (res) {
                if(res.status == 1){
                    $("#card-box").removeClass("none");
                    $("#demo-picture").removeClass("none").addClass("demo-picture");
                } else {
                    $("#card-box").addClass("none");
                    $("#demo-picture").addClass("none").removeClass("demo-picture");
                }
            }
        },
        error: function (err) {
            console.log("err==", err);
            // window.location.href = "../../Util/html/error.html";
        }
    })
}