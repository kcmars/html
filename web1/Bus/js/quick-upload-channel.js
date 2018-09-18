/**
 * Created by Administrator on 2018/7/23.
 */
$(function () {
    $(".demo-picture li").bind("click", function () {
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

    // getRequest(getCardUploadStatus);
		getCardUploadStatus();
});

function getCardUploadStatus() {
    $.ajax({
        type: 'POST',
        url: 'http://aiyunbaoapp.a56999.com/AppWeb/Vip/uploadStatus',
        // data: {user_id: param.user_id},
           data: {user_id: 'TUp6SFVLTzBPME9R'},
        success: function (res) {
            // console.log("res==", res);
            if(res.status == 1){
                $("#card-box").removeClass("none");
                $("#demo-picture").removeClass("none").addClass("demo-picture");
            } else {
                $("#card-box").addClass("none");
                $("#demo-picture").addClass("none").removeClass("demo-picture");
            }
        },
        error: function (err) {
            window.location.href = "../../Util/html/error.html";
        }
    })
}