/**
 * Created by zp on 2018/7/23.
 */
$(function () {
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
    $(".submit").bind("click", function () {
        if (img1 === "") {
            toastAlertShow("请上传名片正面照片");
            return;
        }
        if (img2 === "") {
            toastAlertShow("请上传名片反面照片");
            return;
        }
        loadAlertShow("正在提交...");
        $.ajax({
            type: 'POST',
//                url: 'http://192.168.1.88/AppWeb/Vip/imgUpload',
            url: 'http://aiyunbaoapp.a56999.com/AppWeb/Vip/imgUpload',
            data: {user_id: param.user_id, img_positive: img1.replace(/data.*?base64,/,''), img_back: img2.replace(/data.*?base64,/,'')},
//                data: {user_id: 'TUp6SFVLTzBPME9R', img_positive: img1.replace(/data.*?base64,/,''), img_back: img2.replace(/data.*?base64,/,'')},
            success: function (res) {
                loadAlertHide();
                if(res.status == 1){
                    $("#img1").attr("src","../img/card_face_add.jpg");
                    $("#img2").attr("src","../img/card_back_add.jpg");
                    img1 = "";
                    img2 = "";
                    toastAlertShow(res.data.msg);
                } else {
                    toastAlertShow(res.msg);
                }
            },
            error: function (err) {
                loadAlertHide();
                window.location.href = "../../Util/html/error.html";
            }
        })
    });
});
let img1 = "";
let img2 = "";
let index = 1;
function showImg(index) {
    index = index;
    var file;
    if(index == 1){
        file = $("#file1").get(0).files[0]
    } else {
        file = $("#file2").get(0).files[0]
    }
    var render = new FileReader();
    render.readAsDataURL(file);
    render.onload = function (e) {
        // console.log(e);
        // console.log(e.target.result);
        if (index == 1) {
            img1 = "";
            $("#img1").get(0).src = e.target.result;
            img1 = e.target.result;
        } else {
            img2 = "";
            $("#img2").get(0).src = e.target.result;
            img2 = e.target.result;
        }
    }
}
