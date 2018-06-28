var type = "1"; // 1 成人； 2 儿童
$(function () {
    getRequest(getParams);
//        getParams();
});

//获取app传递过来的参数
function getParams() {
    console.log(param);
    //保存
    $(".add-passenger-btn").bind("click",function(){
        var name = $("#name").val();
        var idNumber = $("#id-number").val();
        if (name == "") {
            toastAlertShow("请输入乘客名字");
            return;
        }
        if (idNumber == "") {
            toastAlertShow("请输入乘客身份证号码");
            return;
        }
//            var params = {
//                user_type: type,
//                name: name,
//                card_id: idNumber,
//                user_id: "TUp6SFVLTzBPME9R"
//            };
        var params = {
            user_type: type,
            name: name,
            card_id: idNumber,
            user_id: param.user_id
        };
        loadAlertShow("正在保存...");
        $.ajax({
            type: 'POST',
            url: $.addPassenger,
            data: params,
            success: function (res) {
                console.log(res);
                console.log(params);
                loadAlertHide();
                if (res.status == 1) {
                    toastAlertShow(res.msg);
                    window.history.back();
                    window.history.reload();
                } else {
                    toastAlertShow(res.msg);
                }
            },
            error: function (err) {
                console.log(err);
                loadAlertHide();
            }
        });
    });
}

//选择票种
function selectType(obj, index) {
    $(obj).addClass("add-passenger-type-selected").siblings().removeClass("add-passenger-type-selected");
    if (index === 1) {
        type = "1";
        $(".add-passenger-tip").hide();
    } else {
        type = "2";
        $(".add-passenger-tip").show();
    }
}
