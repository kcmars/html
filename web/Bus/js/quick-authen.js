/**
 * Created by zp on 2018/7/23.
 */
$(function () {
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
            url: 'http://aiyunbaoapp.a56999.com/AppWeb/Vip/mailbox',
            data: {phone: tel, name: name},
            success: function (data) {
                loadAlertHide();
                toastAlertShow(data.msg);
            },
            error: function (err) {
                loadAlertHide();
                window.location.href = "../../Util/html/error.html";
            }
        })
    });
});