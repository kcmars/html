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
                let data = res.data;
                if (data.come_from == 1) {
                    $(".type").text("支出");
                } else {
                    $(".type").text("收入");
                }
                $(".value").text($.formatTwoDecimal(data.value));
                let wealthDetail = [];
                if (data.type != null) {
                    let type = {
                        text:"类型",
                        content: $.getNewsType(data.type)
                    };
                    wealthDetail.push(type);
                }
                if (data.status != null) {
                    let status = {
                        text:"状态",
                        content: $.getNewsStatus(data.status)
                    };
                    wealthDetail.push(status);
                }
                if (data.extra != null) {
                    let extra = data.extra;
                    if (extra.channel != null) {
                        let channel = {
                            text:"提现账户类型",
                            content: $.getWealthChannel(extra.channel)
                        };
                        wealthDetail.push(channel);
                    }
                    if (extra.name != null) {
                        let name = {
                            text:"提现账户姓名",
                            content: extra.name
                        };
                        wealthDetail.push(name);
                    }
                    if (extra.account != null) {
                        let account = {
                            text:"提现账户账号",
                            content: extra.account
                        };
                        wealthDetail.push(account);
                    }
                }
                if (data.add_time != null) {
                    let add_time = {
                        text:"时间",
                        content: $.format(data.add_time, "yyyy-MM-dd hh:mm:ss")
                    };
                    wealthDetail.push(add_time);
                }
                if (data.id != null) {
                    let id = {
                        text:"编号",
                        content: data.id
                    };
                    wealthDetail.push(id);
                }
                if (data.balance != null) {
                    let balance = {
                        text:"剩余金额",
                        content: $.formatTwoDecimal(data.balance)
                    };
                    wealthDetail.push(balance);
                }
                if (data.remarks != null) {
                    let remarks = {
                        text:"备注",
                        content: data.remarks
                    };
                    wealthDetail.push(remarks);
                }
                let template = document.getElementById('template-wealth-detail-list').innerHTML;
                document.getElementById('wealth-detail').innerHTML = doT.template( template )( wealthDetail );
            } else {
                toastAlertShow(res.msg);
            }
        },
        error: function (err) {
            console.log(err);
            loadAlertHide();
            window.location.href = "../../Util/html/error.html";
        }
    });
}