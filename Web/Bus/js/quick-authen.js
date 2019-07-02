/**
 * Created by keyC on 2019/1/17.
 * 快速代注册通道
 */
$(function () {
    getRequest(leaveWords);
    // leaveWords();
});

/**
 * 提交信息
 */
function leaveWords() {
    $(".submit").bind("click",function(){
        var tel = $('.input_tel').val().trim();
        var name = $('.input_name').val().trim();
        var tel_reg = /^1(3|4|5|7|8)[0-9]\d{8}$/;
        var name_reg = /^[\u4e00-\u9fa5]+(·[\u4e00-\u9fa5]+)*$/; // 只允许有中文字符或.
        if (!tel) {
            toastAlertShow("请输入电话号码");
            return;
        } else if (!tel_reg.test(tel)) {
            toastAlertShow("请输入正确的电话号码");
            return;
        }
        if (!name) {
            toastAlertShow("请输入姓名");
            return;
        } else if (!name_reg.test(name)) {
            toastAlertShow("请输入中文名字");
            return;
        }
        loadAlertShow("正在提交...");
        $.ajax({
            type: 'POST',
            url: $.leaveWords,
            data: {
                // user_id: $.user_id,
                user_id: param.user_id,
                phone: tel,
                user_name: name
            },
            success: function (res) {
                console.log("res", res);
                loadAlertHide();
                if (res) {
                    if (res.status == 1) {
                        toastAlertShow("提交成功");
                    } else {
                        toastAlertShow("提交失败");
                    }
                }
            },
            error: function (err) {
                console.log("err", err);
                loadAlertHide();
                window.location.href = "../../Util/html/error.html";
            }
        })
    });
}