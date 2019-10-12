/**
 * Created by zp on 2019/9/29.
 */
$(function () {
   // getRequest(getParams());
    getParams();
});

function getParams() {
    //返回上一页
    $(".back").bind("click", function () {
       window.history.back();
    });
    //更多活动
    $(".head-right").bind("click", function () {
       window.location.href = "../html/moreActivity.html";
    });
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
//获取推荐用户列表信息
function getDataList(type, cb, flg){
    if (type == "refresh") {
        mData = [];
        page = 1;
    } else {
        page++;
    }
    let data = [1,2,3,4,5,56,6,7,7,77,7,7,7];
    var template = document.getElementById('template-coupon-list').innerHTML;
    if (type == "refresh") {
        $("#coupon-list").empty();
        $('#coupon-list').append(doT.template(template)(data));
    } else {
        if(mData.length!=10){
            flg = true;
        }
        $('#coupon-list').append(doT.template(template)(data));
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
    //             var template = document.getElementById('template-coupon-list').innerHTML;
    //             if (type == "refresh") {
    //                 $("#coupon-list").empty();
    //                 $('#coupon-list').append(doT.template(template)(data));
    //             } else {
    //                 if(mData.length!=10){
    //                     flg = true;
    //                 }
    //                 $('#message-list').append(doT.template(template)(data));
    //             }
    //         } else {
    //             if (type == "refresh") {
    //                 $("#coupon-list").empty();
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
 * 查看优惠券详情
 */
function lookShareDetail() {
    window.location.href = "../html/shareDetail.html";
}