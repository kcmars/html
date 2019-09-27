/**
 * Created by zp on 2018/10/30.
 */
let url_login = "http://a56999.com/Aiyunbao/Treasure/submitLogin"; //登录
let url_logout = "http://a56999.com/Aiyunbao/Treasure/del"; //退出登录
let url_getCommodityList = "http://a56999.com/Aiyunbao/Treasure/treasures"; //获取商品列表
let url_getCommodityDetail = "http://a56999.com/Aiyunbao/Treasure/detail"; //获取商品详情
let url_getCommodityClassify = "http://a56999.com/Aiyunbao/Treasure/treasuresType"; //获取商品分类
let url_addCommodityClassify = "http://a56999.com/Aiyunbao/Treasure/addType"; //添加商品分类
let url_addCommodity = "http://a56999.com/Aiyunbao/Treasure/addTreasure"; //添加商品
let url_deleteCommodity = "http://a56999.com/Aiyunbao/Treasure/treasureDel"; //删除商品
let url_getContact = "http://a56999.com/Aiyunbao/Treasure/contacts"; //获取卖家联系方式
let user_id = null; //用户信息
let mPage = 0; //分页
let mType = "全部"; //当前类别
let isShow = false; //是否显示分类
let picData = [{src: "../img/picture_fill.png", type: 1}]; //存储图片的容器
let videoData = [{src: "../img/playon_fill.png", type: 1}]; //存储视频的容器
let classifyData = []; //存储所有类别的容器(不包含全部)
$(function () {
    /**
     * 获取用户登录信息
     */
    if(sessionStorage.getItem("user_id") != null) {
        user_id = sessionStorage.getItem("user_id");
        $(".home span").text("卖家宝贝");
        $(".seller span").text("添加宝贝");
        $(".outBtn span").text("退出登录");
    } else {
        $(".home span").text("首页");
        $(".seller span").text("卖家入口");
        $(".outBtn span").text("联系卖家");
    }
    /**
     * 切换tab
     */
    $(".wrapperTab .tabItem").bind("click",function () {
        $(this).addClass("active").siblings().removeClass("active");
        let index = $(this).index();
        if(index == 0){ //买家页面
            $(".content-home").removeClass("none");
            $(".content-login").addClass("none");
            $(".content-add").addClass("none");
            $(".content-contact").addClass("none");
            mPage = 0;
            getAllCommodity();
            getClassifyList();
        } else if(index == 1){ //卖家入口，添加商品
            if(user_id) {
                getClassifyList();
                $(".content-home").addClass("none");
                $(".content-login").addClass("none");
                $(".content-add").removeClass("none");
                $(".content-contact").addClass("none");
            } else {
                $(".content-home").addClass("none");
                $(".content-login").removeClass("none");
                $(".content-add").addClass("none");
                $(".content-contact").addClass("none");
            }
        } else if(index == 2) { //联系,退出登录
            if(user_id) {
                getOut();
            } else {
                /**
                 * 获取卖家联系方式
                 */
                getContact();
            }
        }
    });
    /**
     * 获取分类classify-list
     */
    getClassifyList();
    /**
     * 获取商品列表
     */
    getAllCommodity();

    /**
     * 关闭详细信息弹窗
     */
    $(".puBoxCommodity .overLay").bind("click",function () {
        $(".puBoxCommodity").css("display","none");
    });
    /**
     * 登录页面
     * 登录页面验证（电话号码、密码、图形验证码），在不合法时出现tip提示信息
     */
    $("#telInput").bind("blur",function () {
        let reg = /^1(3|4|5|7|8)[0-9]\d{8}$/;
        let num = $("#telInput").val().trim();
        if(num){
            if(!reg.test(num)){
                $(this).siblings(".status").removeClass("tick");
                $(this).siblings(".tip").css("display","block");
            }else if(reg.test(num)){
                $(this).siblings(".tip").css("display","none");
                $(this).siblings(".status").addClass("tick");
            }
        }else{
            $(this).val("");
            $(this).siblings(".tip").css("display","none");
            $(this).siblings(".status").removeClass("tick");
        }
    });
    $("#pswInput").bind("blur",function () {
        if($(this).val().trim()){
            $(this).siblings(".tip").css("display","none");
        }
    });
    $("#captchaInput").bind("blur",function () {
        if($(this).val().trim()){
            $(this).siblings(".tip").css("display","none");
        }
    });
    $(".inputBox .eye").bind("click",function () {
        if(!$(this).hasClass("open")){
            $(this).siblings("#pswInput").prop("type","text");
            $(this).addClass("open");
        } else {
            $(this).siblings("#pswInput").prop("type","password");
            $(this).removeClass("open");
        }
    });
    /**
     * 刷新验证码
     */
    $(".captchaImgBox").click(function () {
        $("#captchaImg").attr("src","http://a56999.com/Aiyunbao/Treasure/getVerifyCode?"+Math.random());
    });
    /**
     * 登录按钮
     */
    $(".submitBtn").bind("click",function () {
        login();
    });

    /**
     * 添加宝贝页面
     */
    $("#type").bind("click", function () {
        if(isShow) {
            isShow = false;
            $(".type").addClass("none");
        } else {
            isShow = true;
            let commodityList2 = document.getElementById('classify-list2-tmp').innerHTML;
            document.getElementById('classify-list2').innerHTML = doT.template(commodityList2)(classifyData);
            $(".type").removeClass("none");
        }
    });
    /**
     * 更新存储图片数组
     */
    updetaPic();
    /**
     * 更新存储视频数组
     */
    updetaVideo();
    /**
     * 添加宝贝按钮
     */
    $(".add-btn").bind("click", function () {
        uploadCommodity();
    });

    /**
     * 联系人页面
     */
    $(".content-contact .model").bind("click", function () {
        $(".content-contact").addClass("none");
    });
});

/**
 * 获取卖家联系方式
 */
function getContact() {
    let postData = {};
    $.ajax({
        type: "POST",
        url: url_getContact,
        data: postData,
        dataType: "json",
        success: function (res) {
            if (res) {
                console.log("getContact", res);
                if (res.status == 1){
                    let data = res.list[0];
                    if (data) {
                        $(".content-contact").removeClass("none");
                        $("#fixed-phone").text(data.fixed_phone);
                        $("#mobile-phone").text(data.phone);
                        $("#weixin-phone").text(data.wei_xin);
                    }
                } else {
                    layer.open({
                        content: res.msg,
                        btn: '确定'
                    });
                }
            }
        }
    });
}
/**
 * 添加分类
 */
function addClassify() {
    $("#type").attr("readonly", false);
    $("#type").focus();
    $("#add-class").removeClass("none");
    $("#add-class").bind("click", function () {
        addClassifyList();
        $(".type").addClass("none");
    });
}
/**
 * 添加分类
 */
function addClassifyList() {
    let type = $("#type").val().trim();
    if (type == "") {
        layer.open({
            content: '请输入商品分类'
            ,btn: '确定'
        });
        return;
    }
    let param = {
        type: type
    };
    $.ajax({
        type: "POST",
        url: url_addCommodityClassify,
        data: param,
        dataType: "json",
        success: function (res) {
            if(res) {
                console.log("addClassifyList", res);
                console.log("addClassifyList-param", param);
                if (res.status == 1){
                    $("#type").attr("readonly", true);
                    $("#add-class").addClass("none");
                    classifyData = res.list;
                    let commodityList2 = document.getElementById('classify-list2-tmp').innerHTML;
                    document.getElementById('classify-list2').innerHTML = doT.template(commodityList2)(classifyData);
                    $("#add-class").unbind("click");
                } else {

                }
            }
        }
    })
}
/**
 * 选择分类
 */
function setClassify(classify, obj) {
    $(obj).addClass("active").siblings().removeClass("active");
    $("#type").val(classify);
    isShow = false;
    $(".type").addClass("none");
    $("#type").attr("readonly", true);
    $("#add-class").addClass("none");
}
/**
 * 获取分类列表
 */
function getClassifyList() {
    let param = {};
    $.ajax({
        type: "POST",
        url: url_getCommodityClassify,
        data: param,
        dataType: "json",
        success: function (res) {
            if (res) {
                console.log("getClassifyList", res);
                if (res.status == 1){
                   classifyData = res.list;
                   let classifyList = document.getElementById('classify-list-tmp').innerHTML;
                   document.getElementById('classify-list').innerHTML = doT.template(classifyList)(classifyData);
                   $("#classify-list li").bind("click", function () {
                       $(this).addClass("active").siblings().removeClass("active");
                       mType = $(this)[0].innerHTML;
                       mPage = 0;
                       getAllCommodity();
                   });
               } else {

               }
            }
        }
    });
}

/**
 * 获取宝贝列表commodity-list
 */
function getAllCommodity() {
    $('#loadBtn').addClass('none');
    $('#loadImg').removeClass('none');
    $('#loadMsg').addClass('none');
    let param = {
        page: mPage,
        type: mType == "全部" ? "" : mType
    };
    console.log("param", param);
    $.ajax({
        type: "POST",
        url: url_getCommodityList,
        data: param,
        dataType: "json",
        success: function (res) {
            if (res) {
                console.log("getAllCommodity=", res);
                if (res.status == 1){
                    let data = res.list;
                    let commodityListTmp = document.getElementById('commodity-list-tmp').innerHTML;
                    if (mPage == 0) {
                        $("#commodity-list").empty();
                        $('#commodity-list').append(doT.template(commodityListTmp)(data));
                    } else {
                        $('#commodity-list').append(doT.template(commodityListTmp)(data));
                    }
                    if(data.length < 12){
                        $('#loadBtn').addClass('none');
                        $('#loadImg').addClass('none');
                        $('#loadMsg').addClass('none');
                        // $('#loadMsg').text('没有更多了');
                    }else {
                        $('#loadBtn').removeClass('none');
                        $('#loadImg').addClass('none');
                        $('#loadMsg').addClass('none');
                    }
                    mPage++;
                } else {
                    if (mPage == 0) {
                        $("#commodity-list").empty();
                    } else {
                        mPage--;
                    }
                    $('#loadBtn').addClass('none');
                    $('#loadImg').addClass('none');
                    $('#loadMsg').removeClass('none');
                    $('#loadMsg').text(res.msg);
                }
            }
        }
    });
}

/**
 * 添加图片
 */
function addPic() {
    let file = $("#file-pic").get(0).files[0];
    let render = new FileReader();
    render.readAsDataURL(file);
    render.onload = function (e) {
        let picSrc = {
            src: e.target.result,
            type: 2
        };
        picData.push(picSrc);
        if (picData.length > 6) {
            picData.shift();
        }
        file = null;
        render = null;
        $("#file-pic").val("");
        $("#file-pic").off("change");
        updetaPic();
    }
}
/**
 * 删除图片
 */
function deletePic(index) {
    console.log(index);
    picData.remove(index);
    if (picData.length > 0 && picData[0].type == 2) {
        let picSrc = {
            src: "../img/picture_fill.png",
            type: 1
        };
        picData.unshift(picSrc);
    }
    updetaPic();
}
/**
 * 更新图片数组
 */
function updetaPic() {
    let picList = document.getElementById('pic-list-tmp').innerHTML;
    document.getElementById('pic-list').innerHTML = doT.template(picList)(picData);
    $("#file-pic").on("change", function () {
        addPic();
    });
}
/**
 * 更新视频数组
 */
function updetaVideo() {
    let videoList = document.getElementById('video-list-tmp').innerHTML;
    document.getElementById('video-list').innerHTML = doT.template(videoList)(videoData);
    $("#file-video").on("change", function () {
        addVideo();
    });
}
/**
 * 添加视频
 */
function addVideo() {
    let file = $("#file-video").get(0).files[0];
    let relSize = parseInt(file.size/1024/1024);
    if(relSize > 10){ // 大于10mb
        layer.open({
            content: '视频过大，请上传小于10M的视频'
            ,btn: '确定'
        });
        return false;
    }
    let render = new FileReader();
    render.readAsDataURL(file);
    render.onload = function (e) {
        let videoSrc = {
            src: e.target.result,
            type: 2
        };
        videoData.push(videoSrc);
        if (videoData.length > 2) {
            videoData.shift();
        }
        file = null;
        render = null;
        $("#file-video").val("");
        $("#file-video").off("change");
        updetaVideo();
    }
}
/**
 * 删除视频
 */
function deleteVideo(index) {
    videoData.remove(index);
    if (videoData.length > 0 && videoData[0].type == 2) {
        let videoSrc = {
            src: "../img/playon_fill.png",
            type: 1
        };
        videoData.unshift(videoSrc);
    }
    updetaVideo();
}

/**
 * 删除数组指定的元素
 */
Array.prototype.remove = function(index) {
    for(let i=0; i<this.length; i++) {
        if(i == index) {
            this.splice(i, 1);
            break;
        }
    }
};
/**
 * 上传商品
 */
function uploadCommodity() {
    let type = $("#type").val().trim();
    if(type == ''){
        layer.open({
            content: '请选择商品分类'
            ,btn: '确定'
        });
        return;
    }
    let code = $("#code").val().trim();
    if(code == ''){
        layer.open({
            content: '请输入商品编号'
            ,btn: '确定'
        });
        return;
    }
    let money = $("#money").val().trim();
    let name = $("#name").val().trim();
    if(name == ''){
        layer.open({
            content: '请输入商品名称或描述'
            ,btn: '确定'
        });
        return;
    }
    if (picData.length < 1) {
        layer.open({
            content: '请至少上传一张商品图片'
            ,btn: '确定'
        });
        return;
    }
    let layerLoad = layer.load({type: 2});
    let param = {
        type: type,
        price: money,
        content: name,
        number: code,
        file: picData,
        visual: videoData
    };
    $.ajax({
        type: "POST",
        url: url_addCommodity,
        data: param,
        dataType: "json",
        success: function (res) {
            if (res) {
                console.log("uploadCommodity", res);
                console.log("uploadCommodity-param", param);
                if (res.status == 1){
                    $("#type").val("");
                    $("#code").val("");
                    $("#money").val("");
                    $("#name").val("");
                    picData = [{src: "../img/picture_fill.png", type: 1}];
                    videoData = [{src: "../img/playon_fill.png", type: 1}];
                    updetaPic();
                    updetaVideo();
                    layer.open({
                        content: res.msg,
                        btn: '确定'
                    });
                } else {
                    layer.open({
                        content: res.msg,
                        btn: '确定'
                    });
                }
            }
            layer.close(layerLoad);
        }
    })
}

/**
 * 查看商品详细
 */
function lookDetail(id) {
    let param = {
        id: id
    };
    $.ajax({
        type: "POST",
        url: url_getCommodityDetail,
        data: param,
        dataType: "json",
        success: function (res) {
            if(res) {
                console.log("lookDetail=", res);
                if (res.status == 1){
                    let data = res.data;
                    let picArray = [];
                    if (data.img && data.img.length > 0) {
                        for(let i = 0; i<data.img.length; i++){
                            let pic = {
                                src: data.img[i].img,
                                type: 1
                            };
                            picArray.push(pic);
                        }
                    }
                    if (data.screen && data.screen.length > 0) {
                        for(let i = 0; i<data.screen.length; i++){
                            let pic = {
                                src: data.screen[i].screen,
                                type: 2
                            };
                            picArray.push(pic);
                        }
                    }
                    let swiper = document.getElementById('swiper-list-tmp').innerHTML;
                    document.getElementById('swiper-list').innerHTML = doT.template(swiper)(picArray);
                    let mySwiper = new Swiper ('.swiper-container', {
                        loop: false, //是否循环
                        navigation: { //左右箭头
                            nextEl: '.swiper-button-next',
                            prevEl: '.swiper-button-prev',
                        },
                        pagination: { // 如果需要分页器
                            el: '.swiper-pagination'
                        }
                    });
                    $(".puBoxCommodity").css('display','block');
                    $("#pu-type").text(data.type);
                    $("#pu-code").text(data.number);
                    $("#pu-price").text(data.price);
                    $("#pu-content").text(data.content);
                    if(user_id){
                        $(".delete-commodity").removeClass("none");
                        $(".delete-commodity").bind("click", function () {
                            deleteCommodity(data.id);
                        })
                    } else {
                        $(".delete-commodity").addClass("none");
                    }
                } else {
                    layer.open({
                        content: res.msg,
                        btn: '确定'
                    });
                }
            }
        }
    });
}
/**
 * 删除宝贝
 */
function deleteCommodity(id) {
    let param = {
        id: id
    };
    $.ajax({
        type: "POST",
        url: url_deleteCommodity,
        data: param,
        dataType: "json",
        success: function (res) {
            console.log("deleteCommodity", res);
            if (res.status == 1){
                $(".puBoxCommodity").css('display','none');
                mPage = 0;
                getAllCommodity();
            }
            layer.open({
                content: res.msg,
                btn: '确定'
            });
        }
    });
}

/**
 * 登录
 */
function login() {
    let password = $("#pswInput").val().trim(),
        num = $("#telInput").val().trim(),
        captcha = $("#captchaInput").val().trim();
    if(!num){
        $(".telInputBox .tip").css("display","block");
    }
    if(num == ''){
        layer.open({
            content: '请输入手机号'
            ,btn: '确定'
        });
        return;
    }
    if(!password){
        $(".pswInputBox .tip").css("display","block");
    }
    if(password == ''){
        layer.open({
            content: '请输入密码'
            ,btn: '确定'
        });
        return;
    }
    if(!captcha){
        $(".captchaInputBox .tip").css("display","block");
    }
    if(captcha == ''){
        layer.open({
            content: '请输入验证码'
            ,btn: '确定'
        });
        return;
    }
    let param = {
        'phone': num,
        'password': password,
        'code': captcha,
    };
    $.ajax({
        type: "POST",
        url: url_login,
        data: param,
        dataType: "json",
        success: function (res) {
            if (res) {
                console.log("login", res);
                if (res.status == 1) {
                    user_id = res.user_id;
                    sessionStorage.setItem("user_id", res.user_id);
                    $(".home span").text("卖家宝贝");
                    $(".seller span").text("添加宝贝");
                    $(".outBtn span").text("退出登录");
                    $(".content-login").addClass("none");
                    $(".content-add").removeClass("none");
                }else{
                    layer.open({
                        content: res.msg,
                        btn: '确定',
                        end:function () {
                            $("#captchaImg").attr("src","http://a56999.com/Aiyunbao/Treasure/getVerifyCode?"+Math.random());
                        }
                    });
                }
            }
        }
    });
}
/**
 * 退出登录
 */
function getOut() {
    $.ajax({
        type: "POST",
        url: url_logout,
        data: {},
        dataType: "json",
        success: function (res) {
            if(res){
                console.log("getOut", res);
                if(res.status == 1) {
                    user_id = null;
                    sessionStorage.clear();
                    $(".content-home").addClass("none");
                    $(".content-login").removeClass("none");
                    $(".content-add").addClass("none");
                    $(".content-contact").addClass("none");
                    $(".home span").text("首页");
                    $(".seller span").text("卖家入口");
                    $(".outBtn span").text("联系卖家");
                } else {
                    layer.open({
                        content: res.msg,
                        btn: '确定'
                    })
                }
            } else {
                console.log("res", res);
            }
        },
        error: function (error) {
            console.log("error", error);
        }
    });
}
/**
 * 补全两位小数
 * @param number
 * @returns {*}
 */
function formatTwoDecimal(number) {
    if (isNaN(parseFloat(number))) {
        return false;
    }
    let mNumber = Math.round(number * 100) / 100;
    let s_x = mNumber.toString();
    let pos_decimal = s_x.indexOf('.');
    if (pos_decimal < 0) {
        pos_decimal = s_x.length;
        s_x += '.';
    }
    while (s_x.length <= pos_decimal + 2) {
        s_x += '0';
    }
    return s_x;
}
