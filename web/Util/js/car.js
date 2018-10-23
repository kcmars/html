/**
 * Created by Administrator on 2018/7/11.
 */
var letter = ["A","B","C","D","E","F","G","H","J","K","L","M","N","O","P","Q","R","S","T","W","X","Y","Z"];
var hotModels = [{name:"A"},{name:"B"},{name:"C"},{name:"D"},{name:"E"},{name:"F"},{name:"G"},{name:"H"},
    {name:"J"},{name:"K"},{name:"L"},{name:"M"}];
var allModels = [
    {letter:"A", module: [{name:"A"},{name:"B"},{name:"C"},{name:"D"},{name:"E"},{name:"F"},{name:"G"}]},
    {letter:"B", module: [{name:"A"},{name:"B"},{name:"C"},{name:"D"},{name:"E"}]},
    {letter:"C", module: [{name:"A"},{name:"B"},{name:"C"},{name:"D"},{name:"E"},{name:"F"},{name:"G"},{name:"H"}]},
    {letter:"D", module: [{name:"A"},{name:"B"},{name:"C"},{name:"D"},{name:"E"},{name:"F"},{name:"G"}]},
    {letter:"E", module: [{name:"A"},{name:"B"},{name:"C"}]},
    {letter:"F", module: [{name:"A"},{name:"B"},{name:"C"},{name:"D"},{name:"E"},{name:"F"}]},
    {letter:"G", module: [{name:"A"},{name:"B"},{name:"C"},{name:"D"}]},
    {letter:"H", module: [{name:"A"},{name:"B"},{name:"C"}]},
    {letter:"J", module: [{name:"A"},{name:"B"},{name:"C"},{name:"D"},{name:"E"},{name:"F"},{name:"G"}]},
    {letter:"K", module: [{name:"A"},{name:"B"},{name:"C"},{name:"D"},{name:"E"}]},
    {letter:"L", module: [{name:"A"},{name:"B"},{name:"C"}]},
    {letter:"M", module: [{name:"A"}]},
    {letter:"N", module: [{name:"A"},{name:"B"}]},
    {letter:"O", module: [{name:"A"},{name:"B"},{name:"C"}]},
    {letter:"P", module: [{name:"A"},{name:"B"},{name:"C"},{name:"D"}]},
    {letter:"Q", module: [{name:"A"},{name:"B"},{name:"C"},{name:"D"},{name:"E"}]},
    {letter:"R", module: [{name:"A"},{name:"B"},{name:"C"},{name:"D"},{name:"E"},{name:"F"}]},
    {letter:"S", module: [{name:"A"},{name:"B"},{name:"C"},{name:"D"},{name:"E"},{name:"F"},{name:"G"}]},
    {letter:"T", module: [{name:"A"},{name:"B"},{name:"C"},{name:"D"},{name:"E"},{name:"F"},{name:"G"},{name:"H"},
        {name:"J"},{name:"K"},{name:"L"},{name:"M"}]},
    {letter:"W", module: [{name:"A"},{name:"B"},{name:"C"},{name:"D"},{name:"E"},{name:"F"},{name:"G"},{name:"H"}]},
    {letter:"X", module: [{name:"A"},{name:"B"},{name:"C"},{name:"D"},{name:"E"},{name:"F"},{name:"G"},{name:"H"},
        {name:"J"}]},
    {letter:"Y", module: [{name:"A"},{name:"B"},{name:"C"},{name:"D"},{name:"E"},{name:"F"},{name:"G"},{name:"H"},
        {name:"J"},{name:"K"}]},
    {letter:"Z", module: [{name:"A"},{name:"B"},{name:"C"},{name:"D"},{name:"E"},{name:"F"},{name:"G"},{name:"H"},
        {name:"J"},{name:"K"},{name:"L"}]}
    ];
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
$(function () {

    //热门车型
    var templateHotModels = document.getElementById('template-hot-models-list').innerHTML;
    document.getElementById('hot-models-list').innerHTML = doT.template(templateHotModels)(hotModels);

    //全部车型
    var templateAllModels = document.getElementById('template-all-models-list').innerHTML;
    document.getElementById('all-models-list').innerHTML = doT.template(templateAllModels)(allModels);

    //右侧字母列表
    var templateLetter = document.getElementById('template-letter-list').innerHTML;
    document.getElementById('letter-list').innerHTML = doT.template(templateLetter)(letter);

    //二级菜单
    var templateCarSeries = document.getElementById('template-car-series-list').innerHTML;
    document.getElementById('car-series-list').innerHTML = doT.template(templateCarSeries)(hotModels);

    //三级菜单
    var templateCarColor = document.getElementById('template-car-color-list').innerHTML;
    document.getElementById('car-color-list').innerHTML = doT.template(templateCarColor)(colors);

    //model层字母列表
    var templateLetterModel = document.getElementById('template-letter-model-list').innerHTML;
    document.getElementById('letter-model').innerHTML = doT.template(templateLetterModel)(letter);

    //选择车型弹出二级选择框车系
    $("#hot-models-list li, #all-models-list li > ul > li").bind("click", function () {
        secondLayerShow = true;
        $("#secondLayer").removeClass("second-layer-animation-out").addClass("second-layer-animation-in");
        $("#hot-models-list li, #all-models-list li > ul > li, #car-series-list li").find("span").removeClass("orange-text");
        $(this).find("span").addClass("orange-text");
    });

    //选择车系弹出三级选择框车色
    $("#car-series-list li").bind("click", function () {
        // console.log("li", $(this).index());
        thirdLayerShow = true;
        $("#thirdLayer").removeClass("third-layer-animation-out").addClass("third-layer-animation-in");
        $("#car-series-list li").find("span").removeClass("orange-text");
        $(this).find("span").addClass("orange-text");
    });

    //选择车色清除所有
    $("#car-color-list li").bind("click", function () {
        secondLayerShow = false;
        thirdLayerShow = false;
        $("#hot-models-list li, #all-models-list li > ul > li, #car-series-list li").find("span").removeClass("orange-text");
        $("#thirdLayer").removeClass("third-layer-animation-in").addClass("third-layer-animation-out");
        $("#secondLayer").removeClass("second-layer-animation-in").addClass("second-layer-animation-out");
        let carInfo = {
            brand: "奥迪",
            model: "Q5",
            color: "红色"
        };
        $("#select-model-box").removeClass("plate-model-animation-in").addClass("plate-model-animation-out");
        sessionStorage.setItem("carInfo", JSON.stringify(carInfo));
        $("#car").val(carInfo.brand + " " + carInfo.model + " " + carInfo.color);
    });

    //右边字母导航触摸开始事件
    $("#letter-list li").on('touchstart',function(event) {
        // event.preventDefault();
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
        // window.scrollTo(0, event.touches[0].pageY);
    });

    //右边字母导航触摸移动事件
    $("#letter-list li").on("touchmove", function (event) {
        // event.preventDefault();
        // console.log("mTarget======", event.originalEvent.targetTouches[0]);
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