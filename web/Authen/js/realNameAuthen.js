/**
 * Created by zp on 2018/7/13.
 */

let passengerInfo; // 乘客认证信息

$(function () {
    getRequest(getParams);
    // getParams();
});

function getParams() {
    if(sessionStorage.getItem("passengerInfo") != null){
        passengerInfo = JSON.parse(sessionStorage.getItem("passengerInfo"));
    }
    if(passengerInfo) {
        $(".authen-main").removeClass("none");
        showPassengerRealNameInfo();
    } else {
        getPassengerRealNameInfo();
    }

    $("#submit").bind("click", function () {
        submitPassengerRealNameInfo();
    });
}

//获取实名认证审核信息
function getPassengerRealNameInfo() {
    let params = {
        // user_id: "6a2f6035-653e-406d-b07a-ae94d46cc42a"
        user_id: param.user_id
    };
    loadAlertShow("获取中...");
    $.ajax({
        type: 'POST',
        url: $.getPassengerRealNameInfo,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res && res.status == 1){
                $(".authen-main").removeClass("none");
                passengerInfo = res.data;
                sessionStorage.setItem("passengerInfo", JSON.stringify(passengerInfo));
                showPassengerRealNameInfo();
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

//显示乘客认证信息
function showPassengerRealNameInfo() {
    if(passengerInfo){
        if (passengerInfo.authentication_status == 0 || passengerInfo.authentication_status == -1) { //未提交认证 数据无效
            $("input").attr("readonly",false);
            $("#submit").removeClass("none").addClass("submit-btn");
        } else if (passengerInfo.authentication_status == 1 || passengerInfo.authentication_status == 2 || passengerInfo.authentication_status == 3 ) { //审核中 成功
            $("#name").val(passengerInfo.name);
            $("#id").val(passengerInfo.ID);
            $("input").attr("readonly",true);
            $("#submit").removeClass("submit-btn").addClass("none");

        } else if (passengerInfo.authentication_status == -2) { //失败
            $("#name").val(passengerInfo.name);
            $("#id").val(passengerInfo.ID);
            $("input").attr("readonly",false);
            $("#submit").removeClass("none").addClass("submit-btn");
        }
    }
}

//提交实名信息
function submitPassengerRealNameInfo() {
    let name = $("#name").val();
    let ID = $("#id").val();
    if (name == "") {
        toastAlertShow("请输入姓名");
        return;
    }
    if (ID == "") {
        toastAlertShow("请输入身份证号");
        return;
    }
    let params = {
        // user_id: "6a2f6035-653e-406d-b07a-ae94d46cc42a",
        user_id: param.user_id,
        name: name,
        ID: ID
    };
    loadAlertShow("提交中...");
    $.ajax({
        type: 'POST',
        url: $.submitPassengerRealNameInfo,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if(res && res.status == 1){
                $("input").attr("readonly",true);
                $("#submit").removeClass("submit-btn").addClass("none");
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


// var index = -1;
// var img1 = "";
// var img2 = "";
// var img3 = "";
// function showImg(index) {
//     var file;
//     if(index == 1){
//         file = $(".file1").get(0).files[0]
//     } else if(index == 2){
//         file = $(".file2").get(0).files[0]
//     } else {
//         file = $(".file3").get(0).files[0]
//     }
//     var render = new FileReader();
//     render.readAsDataURL(file);
//     render.onload = function (e) {
//         console.log(e);
//         console.log(e.target.result);
//         if (index == 1) {
//             img1 = "";
//             $("#img1").get(0).src = e.target.result;
//             img1 = e.target.result;
//         } else if (index == 2) {
//             img2 = "";
//             $("#img2").get(0).src = e.target.result;
//             img2 = e.target.result;
//         } else {
//             img3 = "";
//             $("#img3").get(0).src = e.target.result;
//             img3 = e.target.result;
//         }
//     }
// }
// $("ul li").bind("click", function () {
//     index = $(this).index();
//     console.log("index= ", index);
//     $("#demo-model").animate({top: "0", opacity: 1}, 300);
//     $("#demo-model > .model").bind("click", function () {
//         $("#demo-model").animate({top: "100%", opacity: 0}, 300);
//     });
//     $("#file").on("click", function () {
//         $("#demo-model").animate({top: "100%", opacity: 0}, 300);
//     });
// });

// $("#file").on("change", function () {
//     var file = $(".file").get(0).files[0];
//     var render = new FileReader();
//     render.readAsDataURL(file);
//     render.onload = function (e) {
//         // console.log(e);
//         // console.log(e.target.result);
//         if (index == 0) {
//             console.log("1");
//             img1 = "";
//             $("#img1").get(0).src = e.target.result;
//             img1 = e.target.result;
//         } else if (index == 1) {
//             console.log("2");
//             img2 = "";
//             $("#img2").get(0).src = e.target.result;
//             img2 = e.target.result;
//         } else {
//             console.log("3");
//             img3 = "";
//             $("#img3").get(0).src = e.target.result;
//             img3 = e.target.result;
//         }
//         file = null;
//     }
// });