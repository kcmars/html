/**
 * Created by zp on 2019/9/24.
 */
$(function () {
    // getRequest(getParams);
    getParams();
    //返回上一页
    $(".back").bind("click", function () {
        window.history.back();
    });
    //添加银行卡
    $(".head-right").bind("click", function () {
        window.location.href = "../html/addAccount.html";
    });

});

//获取app传递过来的参数
function getParams() {
    //添加下拉刷新和上拉加载功能
    var listloading = new Listloading('#listloading', {
        disableTime: true,  // 是否需要显示时间
        pullUpAction : function(cb){   // 上拉加载更多
            var flg = false;
            getDataList("loadMore", cb, flg);
        },
        pullDownAction : function(cb, flg){  // 下拉刷新
            getDataList("refresh", cb, flg);
        },
        Realtimetxt: '松开刷新',
        loaderendtxt: '已经到底了',
        iscrollOptions: {
            scrollbars: false   // 显示iscroll滚动条
        }
    });
}

let mData = [];
let page = 1;
//获取消息列表
function getDataList(type, cb, flg){
    if (type == "refresh") {
        mData = [];
        page = 1;
    } else {
        page++;
    }
    let data = [1,2,3,4,5,56,6,7,7,77,7,7,7];
    var template = document.getElementById('template-bank-list').innerHTML;
    if (type == "refresh") {
        $("#bank-list").empty();
        $('#bank-list').append(doT.template(template)(data));
    } else {
        if(mData.length!=10){
            flg = true;
        }
        $('#bank-list').append(doT.template(template)(data));
    }
    // var params = {
    //     user_id: "c7126d8a-7359-45cf-9aff-6984b4d7617f",
    //     grade: id,
    //     page: page
    // };
    // loadAlertShow("正在加载...");
    // $.ajax({
    //     type: 'POST',
    //     url: $.getRecommendDetail,
    //     data: params,
    //     success: function (res) {
    //         console.log(page);
    //         console.log(res);
    //         loadAlertHide();
    //         if (res.status == 1) {
    //             var data = res.data;
    //             for (var key in data) {
    //                 mData.push(data[key]);
    //             }
    //             var template = document.getElementById('template-bank-list').innerHTML;
    //             if (type == "refresh") {
    //                 $("#bank-list").empty();
    //                 $('#bank-list').append(doT.template(template)(data));
    //             } else {
    //                 if(mData.length!=10){
    //                     flg = true;
    //                 }
    //                 $('#bank-list').append(doT.template(template)(data));
    //             }
    //         } else {
    //             if (type == "refresh") {
    //                 $("#bank-list").empty();
    //             } else {
    //                 flg = true;
    //                 page--;
    //             }
    //             toastAlertShow(res.msg);
    //         }
    //         cb(flg);
    //     },
    //     error: function (err) {
    //         loadAlertHide();
    //         window.location.href = "../../Util/html/error.html";
    //         console.log(err);
    //     }
    // });
    cb(flg);
}

/**
 * 查看消息详情
 * @param type 消息类型
 */
function lookDetail(type) {
    switch (type) {
        case 0:
            break;

        case 1:
            window.location.href = "../html/shareDetail.html";
            break;
    }
}