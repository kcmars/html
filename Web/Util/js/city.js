/**
 * Created by keyC on 2018/7/11.
 */
var mProvinces = []; //所有省
var mCities = []; //省下面的城市
var mAds = []; //城市下面的区县
var province = ""; //选择的省
var pro_code = ""; //选择的省code
var city = ""; //选择的市
var city_code = ""; //选择的市code
var dist = ""; //选择的区
var ad_code = ""; //选择的区code
var thirdLayerTouchStartX = 0; //三级菜单触摸起点x位置
var secondLayerTouchStartX = 0; //二级菜单触摸起点x位置
var firstLayerTouchStartX = 0; //一级菜单触摸起点x位置
var firstLayerTouchStartY = 0; //一级菜单触摸起点y位置
var secondLayerShow = false; //二级页面是否显示
var thirdLayerShow = false; //三级页面是否显示

$(function () {
    //获取省数据
    getRequest(getProvinces);
    // getProvinces();

    //第三层页面触摸开始
    $("#thirdLayer").on("touchstart", function (event) {
        thirdLayerTouchStartX = event.originalEvent.targetTouches[0].clientX;
    });

    //第三层页面触摸移动
    $("#thirdLayer").on("touchmove", function (event) {
        let mX = event.originalEvent.targetTouches[0].clientX;
        if ((mX - thirdLayerTouchStartX) > 40) {
            $("#city-list li").find("span").removeClass("orange-text");
            $("#thirdLayer").removeClass("third-layer-animation-in").addClass("third-layer-animation-out");
        }
    });

    //第二层页面触摸开始
    $("#secondLayer").on("touchstart", function (event) {
        secondLayerTouchStartX = event.originalEvent.targetTouches[0].clientX;
    });

    //第二层页面触摸移动
    $("#secondLayer").on("touchmove", function (event) {
        let mX = event.originalEvent.targetTouches[0].clientX;
        if((mX - secondLayerTouchStartX) > 40){
            $("#province-list li, #city-list li").find("span").removeClass("orange-text");
            $("#secondLayer").removeClass("second-layer-animation-in").addClass("second-layer-animation-out");
            if(thirdLayerShow){
                thirdLayerShow = false;
                $("#thirdLayer").removeClass("third-layer-animation-in").addClass("third-layer-animation-out");
            }
        }
    });

    //第一层页面触摸开始
    $("#firstLayer").on("touchstart", function (event) {
        firstLayerTouchStartX = event.originalEvent.targetTouches[0].clientX;
        firstLayerTouchStartY = event.originalEvent.targetTouches[0].clientY;
    });

    //第一层页面滚动监听
    $("#firstLayer").on("touchmove", function (event) {
        let mX = event.originalEvent.targetTouches[0].clientX;
        let mY = event.originalEvent.targetTouches[0].clientY;
        if ((mX - firstLayerTouchStartX) > 80) {
            if(secondLayerShow) {
                secondLayerShow = false;
                $("#province-list li, #city-list li").find("span").removeClass("orange-text");
                $("#secondLayer").removeClass("second-layer-animation-in").addClass("second-layer-animation-out");
                if(thirdLayerShow){
                    secondLayerShow = false;
                    $("#thirdLayer").removeClass("third-layer-animation-in").addClass("third-layer-animation-out");
                }
            }
            $("#select-model-box").removeClass("plate-model-animation-in").addClass("plate-model-animation-out");

        } else if ((mX - firstLayerTouchStartX) > 30 || Math.abs(mY - firstLayerTouchStartY) > 30) {
            if(secondLayerShow) {
                secondLayerShow = false;
                $("#province-list li, #city-list li").find("span").removeClass("orange-text");
                $("#secondLayer").removeClass("second-layer-animation-in").addClass("second-layer-animation-out");
                if(thirdLayerShow){
                    secondLayerShow = false;
                    $("#thirdLayer").removeClass("third-layer-animation-in").addClass("third-layer-animation-out");
                }
            }
        }
    });
});
/**
 * 获取省
 */
function getProvinces() {
    $.ajax({
        type: 'POST',
        url: $.getProvinces,
        data: {
            user_id: param.user_id
            // user_id: $.user_id
        },
        success: function (res) {
            console.log(res);
            if(res.status == 1){
                mProvinces = [];
                if (res.data != null && res.data.length > 0) {
                    mProvinces = res.data;
                    let template = document.getElementById('template-province-list').innerHTML;
                    document.getElementById('province-list').innerHTML = doT.template(template)(mProvinces);
                } else {
                    toastAlertShow("暂无数据");
                }
            } else {
                toastAlertShow(res.msg);
            }
        },
        error: function (err) {
            console.log(err);
            // window.location.href = "../../Util/html/error.html";
        }
    });
}
/**
 * 获取城市
 * @param obj
 * @param pro
 * @param code
 */
function getCities(obj, pro, code) {
    $(obj).addClass("orange-text").siblings().removeClass("orange-text");
    province = pro;
    pro_code = code;
    $.ajax({
        type: 'POST',
        url: $.getCities,
        data: {
            user_id: param.user_id,
            pro_code: code
            // user_id: $.user_id,
            // pro_code: code
        },
        success: function (res) {
            console.log(res);
            if(res.status == 1){
                mCities = [];
                if (res.data != null && res.data.length > 0) {
                    secondLayerShow = true;
                    $("#secondLayer").removeClass("second-layer-animation-out").addClass("second-layer-animation-in");
                    if(thirdLayerShow){
                        thirdLayerShow = false;
                        $("#thirdLayer").removeClass("third-layer-animation-in").addClass("third-layer-animation-out");
                    }
                    mCities = res.data;
                    let templateCarSeries = document.getElementById('template-city-list').innerHTML;
                    document.getElementById('city-list').innerHTML = doT.template(templateCarSeries)(mCities);
                } else {
                    secondLayerShow = false;
                    $("#province-list li, #city-list li").find("span").removeClass("orange-text");
                    $("#secondLayer").removeClass("second-layer-animation-in").addClass("second-layer-animation-out");
                    let cityInfo = {   //选择城市信息
                        province: pro,
                        pro_code: code,
                        city: pro,
                        city_code: code
                    };
                    sessionStorage.setItem("cityInfo", JSON.stringify(cityInfo));
                    $("#select-model-box").removeClass("plate-model-animation-in").addClass("plate-model-animation-out");
                    $("#city").val(cityInfo.province);
                }
            } else {
                toastAlertShow(res.msg);
            }
        },
        error: function (err) {
            console.log(err);
            // window.location.href = "../../Util/html/error.html";
        }
    });
}
/**
 * 获取区县
 * @param obj
 * @param c
 * @param code
 */
function getAds(obj, c, code) {
    let type = $("#type").val();
    if(type == "express"){
        secondLayerShow = false;
        $("#province-list li, #city-list li").find("span").removeClass("orange-text");
        $("#secondLayer").removeClass("second-layer-animation-in").addClass("second-layer-animation-out");
        let cityInfo = {   //选择城市信息
            province: province,
            pro_code: pro_code,
            city: c,
            city_code: code
        };
        sessionStorage.setItem("cityInfo", JSON.stringify(cityInfo));
        $("#select-model-box").removeClass("plate-model-animation-in").addClass("plate-model-animation-out");
        $("#city").val(cityInfo.province + "-" + cityInfo.city);
    } else {
        city = c;
        city_code = code;
        $.ajax({
            type: 'POST',
            url: $.getAds,
            data: {
                user_id: param.user_id,
                city_code: code
                // user_id: $.user_id,
                // city_code: code
            },
            success: function (res) {
                console.log(res);
                if(res.status == 1){
                    mAds = [];
                    if (res.data != null && res.data.length > 0) {
                        thirdLayerShow = true;
                        $("#thirdLayer").removeClass("third-layer-animation-out").addClass("third-layer-animation-in");
                        $(obj).addClass("orange-text").siblings().removeClass("orange-text");
                        mAds = res.data;
                        let templateCarColor = document.getElementById('template-ad-list').innerHTML;
                        document.getElementById('ad-list').innerHTML = doT.template(templateCarColor)(mAds);
                    } else {
                        secondLayerShow = false;
                        $("#province-list li, #city-list li").find("span").removeClass("orange-text");
                        $("#secondLayer").removeClass("second-layer-animation-in").addClass("second-layer-animation-out");
                        let cityInfo = {   //选择城市信息
                            province: province,
                            pro_code: pro_code,
                            city: c,
                            city_code: code
                        };
                        sessionStorage.setItem("cityInfo", JSON.stringify(cityInfo));
                        $("#select-model-box").removeClass("plate-model-animation-in").addClass("plate-model-animation-out");
                        $("#city").val(cityInfo.province + "-" + cityInfo.city);
                    }
                } else {
                    toastAlertShow(res.msg);
                }
            },
            error: function (err) {
                console.log(err);
                // window.location.href = "../../Util/html/error.html";
            }
        });
    }
}
/**
 * 选择好区县
 * @param obj
 * @param dist
 * @param ad_code
 */
function selectAds(obj, dist, ad_code) {
    secondLayerShow = false;
    thirdLayerShow = false;
    $("#province-list li, #city-list li").find("span").removeClass("orange-text");
    $("#thirdLayer").removeClass("third-layer-animation-in").addClass("third-layer-animation-out");
    $("#secondLayer").removeClass("second-layer-animation-in").addClass("second-layer-animation-out");
    let cityInfo = {   //选择城市信息
        province: province,
        pro_code: pro_code,
        city: city,
        city_code: city_code,
        dist: dist,
        ad_code: ad_code
    };
    sessionStorage.setItem("taxiCityInfo", JSON.stringify(cityInfo));
    $("#select-model-box").removeClass("plate-model-animation-in").addClass("plate-model-animation-out");
    $("#city").val(cityInfo.province + "-" + cityInfo.city + "-" + cityInfo.dist);
}