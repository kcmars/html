/**
 * Created by keyC on 2018/7/10.
 * 获取推荐人数详情
 */
var mData = [];
var page = 1;

$(function () {
    getRequest(getParams);
    // getParams();
});

/**
 * 获取app传递过来的参数
 */
function getParams() {
    //获取上一个页面传递过来的id
    var id = $.getLocationParams("id");
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

/**
 * 获取推荐用户列表信息
 * @param type
 * @param id
 * @param cb
 * @param flg
 */
function getDataList(type, id, cb, flg){
    if (type == "refresh") {
        mData = [];
        page = 1;
    } else {
        page++;
    }
    var params = {
        // user_id: $.user_id,
        user_id: param.user_id,
        grade: id,
        page: page,
        page_size: 20
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
            cb(flg);
        }
    });
}
