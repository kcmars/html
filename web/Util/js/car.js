/**
 * Created by keyC on 2018/7/11.
 */
var colors = [{color:"white", name: "白色"},
    {color:"red", name: "红色"},
    {color:"black", name: "黑色"},
    {color:"silver", name: "银色"},
    {color:"champagne", name: "香槟色"},
    {color:"gray", name: "灰色"},
    {color:"blue", name: "蓝色"},
    {color:"yellow", name: "黄色"},
    {color:"green", name: "绿色"},
    {color:"coffee", name: "咖啡色"},
    {color:"orange", name: "橙色"},
    {color:"other", name: "其他"}];
var thirdLayerTouchStartX = 0; //三级菜单触摸起点x位置
var secondLayerTouchStartX = 0; //二级菜单触摸起点x位置
var firstLayerTouchStartX = 0; //一级菜单触摸起点x位置
var firstLayerTouchStartY = 0; //一级菜单触摸起点y位置
var secondLayerShow = false; //二级页面是否显示
var thirdLayerShow = false; //三级页面是否显示
var letter = []; //车辆首字母
var hotModels = []; //热门车型
var allModels = []; //所有车辆
var carSeries = []; //车辆对应系列
var mBrand = ""; //车辆类型
var mModel = ""; //车辆系列
var mColor = ""; //车辆颜色
var hot = ["一汽", "东风", "丰田", "别克", "起亚", "哈弗", "大众", "雪佛兰", "本田", "标致", "现代", "福特"];

$(function () {

    getRequest(getParams);
    // getParams();

    //第三层页面触摸开始
    $("#thirdLayer").on("touchstart", function (event) {
        thirdLayerTouchStartX = event.originalEvent.targetTouches[0].clientX;
    });

    //第三层页面触摸移动
    $("#thirdLayer").on("touchmove", function (event) {
        let mX = event.originalEvent.targetTouches[0].clientX;
        if ((mX - thirdLayerTouchStartX) > 40) {
            $("#car-series-list li").find("span").removeClass("orange-text");
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
            $("#hot-models-list li, #all-models-list li > ul > li, #car-series-list li").find("span").removeClass("orange-text");
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
        if ((mX - firstLayerTouchStartX) > 80){
            if(secondLayerShow) {
                secondLayerShow = false;
                $("#hot-models-list li, #all-models-list li > ul > li, #car-series-list li").find("span").removeClass("orange-text");
                $("#secondLayer").removeClass("second-layer-animation-in").addClass("second-layer-animation-out");
                if (thirdLayerShow) {
                    secondLayerShow = false;
                    $("#thirdLayer").removeClass("third-layer-animation-in").addClass("third-layer-animation-out");
                }
            }
            $("#select-model-box").removeClass("plate-model-animation-in").addClass("plate-model-animation-out");

        }else if ((mX - firstLayerTouchStartX) > 30 || Math.abs(mY - firstLayerTouchStartY) > 30) {
            if(secondLayerShow) {
                secondLayerShow = false;
                $("#hot-models-list li, #all-models-list li > ul > li, #car-series-list li").find("span").removeClass("orange-text");
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
 * 获取车辆
 */
function getParams() {
    let params = {
        brand: ""
    };
    loadAlertShow("获取中...");
    $.ajax({
        type: 'POST',
        url: $.getAllCarType,
        data: params,
        success: function (res) {
            allModels = [];
            hotModels = [];
            console.log("getAllCarType=", res);
            loadAlertHide();
            if (res) {
                if(res.status == 1){
                    let data = res.data;
                    if (data != null && data.length > 0) {
                        letter.pushNoRepeat(data);
                        console.log(letter);
                        //右侧字母列表
                        let templateLetter = document.getElementById('template-letter-list').innerHTML;
                        document.getElementById('letter-list').innerHTML = doT.template(templateLetter)(letter);
                        //model层字母列表
                        let templateLetterModel = document.getElementById('template-letter-model-list').innerHTML;
                        document.getElementById('letter-model').innerHTML = doT.template(templateLetterModel)(letter);
                        for (let i = 0; i<letter.length; i++) {
                            let modules = [];
                            for (let j = 0; j<data.length; j++) {
                                if (data[j].alpha == letter[i]) {
                                    let module = {
                                        src: $.carLogo + data[j].logo,
                                        name: data[j].brand
                                    };
                                    modules.push(module);
                                }
                            }
                            let allModel = {
                                letter: letter[i],
                                module: modules
                            };
                            allModels.push(allModel);
                        }
                        for (let j = 0; j<data.length; j++) {
                            if (isHotModel(data[j].brand)) {
                                let hotModel = {
                                    src: $.carLogo + data[j].logo,
                                    name: data[j].brand
                                };
                                hotModels.push(hotModel);
                            }
                        }
                        //全部车型
                        let templateAllModels = document.getElementById('template-all-models-list').innerHTML;
                        document.getElementById('all-models-list').innerHTML = doT.template(templateAllModels)(allModels);
                        //热门车型
                        let templateHotModels = document.getElementById('template-hot-models-list').innerHTML;
                        document.getElementById('hot-models-list').innerHTML = doT.template(templateHotModels)(hotModels);
                        //字母导航触摸监听
                        touchLetter();
                    }
                } else {
                    toastAlertShow(res.msg);
                }
            }
        },
        error: function (err) {
            console.log("getAllCarType=", err);
            loadAlertHide();
            // window.location.href = "../../Util/html/error.html";
        }
    });
}

/**
 * 获取车辆系列
 */
function getCarModel(brand, obj) {
    console.log(brand);
    mBrand = brand;
    let params = {
        brand: mBrand
    };
    loadAlertShow("获取中...");
    $.ajax({
        type: 'POST',
        url: $.getAllCarType,
        data: params,
        success: function (res) {
            console.log(res);
            loadAlertHide();
            if (res) {
                if(res.status == 1){
                    carSeries = res.data;
                    if (carSeries != null && carSeries.length > 0) {
                        let templateCarSeries = document.getElementById('template-car-series-list').innerHTML;
                        document.getElementById('car-series-list').innerHTML = doT.template(templateCarSeries)(carSeries);
                        secondLayerShow = true;
                        $("#secondLayer").removeClass("second-layer-animation-out").addClass("second-layer-animation-in");
                        $("#hot-models-list li, #all-models-list li > ul > li, #car-series-list li").find("span").removeClass("orange-text");
                        $(obj).find("span").addClass("orange-text");
                    }
                } else {
                    toastAlertShow(res.msg);
                }
            }
        },
        error: function (err) {
            console.log(err);
            loadAlertHide();
        }
    });
}
/**
 * 获取车辆颜色
 */
function getCarColor(model, obj) {
    mModel = model.replace(mBrand, "").trim();
    let templateCarColor = document.getElementById('template-car-color-list').innerHTML;
    document.getElementById('car-color-list').innerHTML = doT.template(templateCarColor)(colors);
    thirdLayerShow = true;
    $("#thirdLayer").removeClass("third-layer-animation-out").addClass("third-layer-animation-in");
    $("#car-series-list li").find("span").removeClass("orange-text");
    $(obj).find("span").addClass("orange-text");
}
/**
 * 选取车辆颜色
 */
function setCarColor(color, obj) {
    mColor = color;
    secondLayerShow = false;
    thirdLayerShow = false;
    $("#hot-models-list li, #all-models-list li > ul > li, #car-series-list li").find("span").removeClass("orange-text");
    $("#thirdLayer").removeClass("third-layer-animation-in").addClass("third-layer-animation-out");
    $("#secondLayer").removeClass("second-layer-animation-in").addClass("second-layer-animation-out");
    let carInfo = {
        brand: mBrand,
        model: mModel,
        color: mColor
    };
    $("#select-model-box").removeClass("plate-model-animation-in").addClass("plate-model-animation-out");
    sessionStorage.setItem("carInfo", JSON.stringify(carInfo));
    $("#car").val(carInfo.brand + " " + carInfo.model + " " + carInfo.color);
}
/**
 * 去重插入数组，并排序
 * @param data
 */
Array.prototype.pushNoRepeat = function(data){
    for(let i = 0; i < data.length; i++){
        let ele = data[i].alpha;
        if(this.indexOf(ele) == -1){
            this.push(ele);
        }
    }
    this.sort();
};
/**
 * 字母触摸事件
 */
function touchLetter() {
    //右边字母导航触摸开始事件
    $("#letter-list li").on('touchstart',function(event) {
        // Event.preventDefault();
        clearTimeout(this.timer);
        let target = event.currentTarget;
        if (target && target.localName == "li") {
            $("#toast").removeClass("none").addClass("toast-box");
            $("#letter-model").removeClass("none").addClass("letter-model");
            $("#toast-letter").text(target.innerText);
            if ($("#all-models-list>li").eq(target.id)) {
                $("#all-models-list>li").eq(target.id).get(0).scrollIntoView();
            }
        }
    });
    //右边字母导航触摸移动事件
    $("#letter-list li").on("touchmove", function (event) {
        // Event.preventDefault();
        // console.log("mTarget======", Event.originalEvent.targetTouches[0]);
        //创建鼠标滑动的地点的对象
        let mTarget = document.elementFromPoint(event.originalEvent.targetTouches[0].clientX, event.originalEvent.targetTouches[0].clientY);
        // console.log("target", $(mTarget).index());
        if (mTarget && mTarget.localName == "li") {
            $("#toast").removeClass("none").addClass("toast-box");
            $("#toast-letter").text(mTarget.innerText);
            if ($("#all-models-list>li").eq($(mTarget).index())) {
                $("#all-models-list>li").eq($(mTarget).index()).get(0).scrollIntoView();
            }
        }
    });
    //右边字母导航触摸完成事件
    $("#letter-list li").on("touchend", function (event) {
        $("#letter-model").removeClass("letter-model").addClass("none");
        this.timer = setTimeout(function () {
            $("#toast").removeClass("toast-box").addClass("none");
        }, 400)
    });
}
/**
 * 选取热门车型
 * @returns {boolean}
 */
function isHotModel(brand) {
    return hot.exist(brand);
}
/**
 * 检查数组中是否存在某值
 * @param data
 */
Array.prototype.exist = function(data){
    if(this.indexOf(data) == -1){
        return false;
    } else {
        return true;
    }
};

