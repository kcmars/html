$(function () {
    getRequest(getParams);
       // getParams();
});

//获取app传递过来的参数
function getParams() {
    //保存
    $("#phone").on("input",function(){
        var phone = $(this).val();
        if (phone.length == 11) {
            // var params = {
            //     phone: phone,
            //     user_id: $.user_id
            // };
            var params = {
                phone: phone,
                user_id: param.user_id
            };
            loadAlertShow("正在保存...");
            $.ajax({
                type: 'POST',
                url: $.checkUserExist,
                data: params,
                success: function (res) {
                    console.log(res);
                    loadAlertHide();
                    if (res.status == 1) {
                        toastAlertShow(res.msg);
                        window.location.href = "./busEmployeesAuthen.html?token=" + res.data;
                    } else {
                        toastAlertShow(res.msg, 1500);
                        $("#phone").val("");
                    }
                },
                error: function (err) {
                    console.log(err);
                    loadAlertHide();
                    window.location.href = "../../Util/html/error.html";
                }
            });
        }
    });
}
