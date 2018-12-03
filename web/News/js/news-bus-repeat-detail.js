/**
 * Created by zp on 2018/11/15.
 */
let mData = []; //数据源
let mPage = 1; //分页

$(function () {
    getRequest(getParams);
    // getParams();

    /**
     * 关闭详细弹窗
     */
    $(".puBox .close, .puBox .model").bind("click",function () {
        $(".puBox").addClass("none");
    });
});
/**
 * 获取app传递过来的参数
 */
function getParams() {
    //添加下拉刷新和上拉加载功能
    let listloading = new Listloading('#listloading', {
        disableTime: true,  // 是否需要显示时间
        pullUpAction : function(cb){   // 上拉加载更多
            let flg = false;
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
/**
 * 获取大巴车上传记录详情
 * @param type
 * @param cb
 * @param flg
 */
function getDataList(type, cb, flg){
    if (type == "refresh") {
        mData = [];
        mPage = 1;
    } else {
        mPage++;
    }
    // let params = {
    //     user_id: "0c63c36f-d7da-4b9c-80f1-86397d8e0438",
    //     upload_no: "75f180d4d76aceab352d5ccbe5243281",
    //     id: "12",
    //     page: mPage
    // };
    let params = {
        user_id: param.user_id,
        upload_no: param.upload_no,
        id: param.id,
        page: mPage
    };
    console.log(params);
    $.ajax({
        type: 'POST',
        url: $.getAllUploadedBus,
        data: params,
        success: function (res) {
            console.log(mPage);
            console.log(res);
            if (res.status == 1) {
                let data = res.data;
                for (let key in data) {
                    mData.push(data[key]);
                }
                let template = document.getElementById('template-bus-repeat-list').innerHTML;
                if (type == "refresh") {
                    $("#bus-repeat-list").empty();
                    $('#bus-repeat-list').append(doT.template(template)(data));
                } else {
                    if(mData.length!=10){
                        flg = true;
                    }
                    $('#bus-repeat-list').append(doT.template(template)(data));
                }
            } else {
                if (type == "refresh") {
                    $("#bus-repeat-list").empty();
                } else {
                    flg = true;
                    mPage--;
                }
                toastAlertShow(res.msg);
            }
            cb(flg);
        },
        error: function (err) {
            window.location.href = "../../Util/html/error.html";
            console.log(err);
        }
    });
    cb(flg);
}
/**
 * 查看重复大巴车上传者信息
 * @param bus_id bus_id
 */
function lookDetail(bus_id) {
    let params = {
        // user_id: "0c63c36f-d7da-4b9c-80f1-86397d8e0438",
        user_id: param.user_id,
        bus_no: bus_id
    };
    console.log(params);
    loadAlertShow("正在加载...");
    $.ajax({
        type: 'POST',
        url: $.getBusUploaderInfo,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if (res.status == 1) {
                let data = res.data;
                $('.puBox').removeClass("none");
                $('.puBox .pu .title .title_plate').text("【" + data.plate_number + "】");
                $('.puBox .pu .infoContent .info_he_tel').text(data.phone);
                $('.puBox .pu .infoContent .info_he_time').text($.format(data.add_time, "yyyy-MM-dd hh:mm:ss"));
            } else {
                toastAlertShow(res.msg);
            }
        },
        error: function (err) {
            loadAlertHide();
            console.log(err);
        }
    });
}