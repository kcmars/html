//    var obj = [
//        {
//            "start_name" : "成都站",
//            "start_latitude" : "30.656327",
//            "start_longitude" : "104.105747",
//            "end_name" : "图腾·印象酒店(体院店)",
//            "end_latitude" : "30.690386",
//            "end_longitude" : "104.041013",
//        },
//        {
//            "start_district": "武侯区",
//            "start_city": "成都市",
//            "start_province": "四川省",
//            "start_address": "一环路西一段19号",
//            "start_name": "成都体育学院",
//            "start_latitude": "30.646981",
//            "start_longitude": "104.044053",
//            "end_district": "武侯区",
//            "end_city": "成都市",
//            "end_address": "致民东路6号附9号",
//            "end_name": "成都市工商行政管理局",
//            "end_latitude": "30.639409",
//            "end_longitude": "104.087207",
//            "add_time": "1508125261",
//            "pass_point": [{"prov":"四川省", "city":"成都市", "dist": "成华区", "name": "新华公园", "lat": "30.690386", "lon": "104.041013"}],
//        }
//    ];
$(function () {
    getRequest(getParams);
    // getParams();
});

//获取app传递过来的参数
function getParams() {
    //获取上一个页面传递过来的id
    var id = $.getLocationParams("id");
    console.log("id===", id);
    //添加下拉刷新和上拉加载功能
    var listloading = new Listloading('#listloading', {
        disableTime: true,  // 是否需要显示时间
        pullUpAction : function(cb){   // 上拉加载更多
            var flg = false;
            getDataList("loadMore", id, cb, flg);
        },
        pullDownAction : function(cb, flg){  // 下拉刷新
            getDataList("refresh", id, cb, flg);
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
function getDataList(type, id, cb, flg){
    if (type == "refresh") {
        mData = [];
        page = 1;
    } else {
        page++;
    }
    // var params = {
    //     user_id: "c7126d8a-7359-45cf-9aff-6984b4d7617f",
    //     grade: id,
    //     page: page
    // };
    var params = {
        user_id: param.user_id,
        grade: id,
        page: page
    };
    loadAlertShow("正在加载...");
    $.ajax({
        type: 'POST',
        url: $.getRecommendDetail,
        data: params,
        success: function (res) {
            console.log(page);
            console.log(res);
            loadAlertHide();
            if (res.status == 1) {
                var data = res.data;
                for (var key in data) {
                    mData.push(data[key]);
                }
                //渲染购票的乘客信息列表
                var template = document.getElementById('template-share-recommend-list').innerHTML;
                if (type == "refresh") {
                    $("#share-recommend-list").empty();
                    $('#share-recommend-list').append(doT.template(template)(data));
                } else {
                    if(mData.length!=10){
                        flg = true;
                    }
                    $('#share-recommend-list').append(doT.template(template)(data));
                }
            } else {
                if (type == "refresh") {
                    $("#share-recommend-list").empty();
                } else {
                    flg = true;
                    page--;
                }
                toastAlertShow(res.msg);
            }
            cb(flg);
        },
        error: function (err) {
            loadAlertHide();
            window.location.href = "../../Util/html/error.html";
            console.log(err);
        }
    });
    cb(flg);
}
