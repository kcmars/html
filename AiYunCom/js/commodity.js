/**
 * Created by zp on 2018/10/30.
 */
let ip = "http://192.168.1.88/Aiyunbao/";
// let ip = "http://a56999.com/Aiyunbao/";
let url_login = ip + "Treasure/submitLogin"; //登录
let url_logout = ip + "Treasure/del"; //退出登录
let url_getCommodityList = ip + "Treasure/treasures"; //获取商品列表
let url_getCommodityDetail = ip + "Treasure/detail"; //获取商品详情
let url_getCommodityClassify = ip + "Treasure/treasuresType"; //获取商品分类
let url_addCommodityClassify = ip + "Treasure/addType"; //添加商品分类
let url_addCommodity = ip + "Treasure/addTreasure"; //添加商品
let url_deleteCommodity = ip + "Treasure/treasureDel"; //删除商品
let url_getContact = ip + "Treasure/contacts"; //获取卖家联系方式
let url_register = ip + "User/register"; //注册账户
let url_checkPhone = ip + "User/checkPhone"; //检测手机号是否注册
let url_get_sms_code = ip + "User/getVerifyCode"; //获取短信验证码
let url_collection = ip + "Collection/addCollection"; //收藏
let url_cancelCollection = ip + "Collection/collectionDel"; //取消收藏
let url_getCollectList = ip + "Collection/getList"; //获取收藏的宝贝列表
let user_id = null; //用户信息
let user_type = null; //用户类型
let list_type = 1; //列表类型 1 首页的数据， 2 收藏的数据
// let mPage = 0; //分页
let mType = "全部"; //当前类别
let isShow = false; //是否显示分类
let pic_demo = "../img/picture_fill.png";
let video_demo = "../img/playon_fill.png";
let picData = [{src: pic_demo, type: 1}]; //存储图片的容器
let classifyData = []; //存储所有类别的容器(不包含全部)
let mDataInfo = null; //当前查看的商品详情
let commodity_id = null; //当前查看的商品id
let mySwiper = null;
let timer = null; //定时器
let timerClick = true; //定时器开关
let checkPhone = true; //检查手机号是是否注册
/**
 * 分页
 * @type {number}
 */
let mPageNum = 10; //一页10条数据
let mRound = 0; //一共分为几页数据
let mRoundList = []; //当前显示出来的 round
let mRoundListLength = 5; //定义基础展示 round 的个数
let currentItem = 1; //表示当前第几页
let currentIndex = 0; //表示当前选中的 round 的下标
let mCurrentPageData = []; //当前页存放的数据
let mSelectIndex = 0; //当前选中的round项
$(function () {
    mySwiper = new Swiper ('.swiper-container', {
        loop: false, //是否循环
        navigation: { //左右箭头
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },
        pagination: { // 如果需要分页器
            el: '.swiper-pagination'
        }
    });
    /**
     * 获取用户登录信息
     */
    if(sessionStorage.getItem("user_id") != null && sessionStorage.getItem("user_type") != null) {
        user_id = sessionStorage.getItem("user_id");
        user_type = sessionStorage.getItem("user_type");
        if (user_type == 1) { //卖家
            $(".contact").addClass("none");
            $(".outBtn").removeClass("none");
            $(".home span").text("卖家宝贝");
            $(".seller span").text("添加宝贝");
            $(".outBtn span").text("退出登录");
        } else { //买家
            $(".contact").removeClass("none");
            $(".outBtn").removeClass("none");
            $(".home span").text("首页");
            $(".seller span").text("我的收藏");
            $(".contact span").text("联系卖家");
            $(".outBtn span").text("退出登录");
        }
    } else {
        $(".outBtn").addClass("none");
        $(".contact").removeClass("none");
        $(".home span").text("首页");
        $(".seller span").text("登录入口");
        $(".contact span").text("联系卖家");
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
            $(".content-register").addClass("none");
            // mPage = 0;
            // getAllCommodity();
            $("#min-price").val("");
            $("#max-price").val("");
            $("#content").val("");
            mType = "全部";
            list_type = 1;
            if (user_id != null && user_type != null) {
                if (user_type == 1) { //卖家
                    $(".contact").addClass("none");
                    $(".outBtn").removeClass("none");
                    $(".home span").text("卖家宝贝");
                    $(".seller span").text("添加宝贝");
                    $(".outBtn span").text("退出登录");
                } else if (user_type == 2) { //买家
                    $(".contact").removeClass("none");
                    $(".outBtn").removeClass("none");
                    $(".home span").text("首页");
                    $(".seller span").text("我的收藏");
                    $(".contact span").text("联系卖家");
                    $(".outBtn span").text("退出登录");
                }
            } else {
                $(".outBtn").addClass("none");
                $(".contact").removeClass("none");
                $(".home span").text("首页");
                $(".seller span").text("登录入口");
                $(".contact span").text("联系卖家");
            }
            changeRound(1, 0);
            getClassifyList();

        } else if(index == 1){ //买家入口，买家注册，登录入口，卖家添加商品
            if(user_id != null) {
                if (user_type == 2) { //登录为买家， 显示收藏
                    $(".content-home").removeClass("none");
                    $(".content-login").addClass("none");
                    $(".content-add").addClass("none");
                    $(".content-contact").addClass("none");
                    $(".content-register").addClass("none");
                    $("#min-price").val("");
                    $("#max-price").val("");
                    $("#content").val("");
                    mType = "全部";
                    list_type = 2;
                    changeRound(1, 0);
                    getClassifyList();
                } else {  //登录为卖家，显示添加
                    commodity_id = null;
                    getClassifyList();
                    $("#commodity-id").val("");
                    $("#type").val("");
                    $("#code").val("");
                    $("#money").val("");
                    $("#remarks").val("");
                    $("#name").val("");
                    picData = [{src: pic_demo, type: 1}];
                    updetaPic();
                    $("#video-list li").find("img").attr("src", video_demo);
                    $("#video-list li").find("input").val("");
                    $("#video-list li").find("video").attr("src", "");
                    $("#video-list li").find("video").attr("poster", "");
                    $("#video-list li").find(".scale-img").val("");
                    $("#video-list li").find("i").addClass("none");
                    $("#video-list li").find("span").addClass("none");
                    $(".content-home").addClass("none");
                    $(".content-login").addClass("none");
                    $(".content-add").removeClass("none");
                    $(".content-contact").addClass("none");
                    $(".content-register").addClass("none");
                }
            } else {
                $(".outBtn").addClass("none");
                $(".contact").removeClass("none");
                $(".home span").text("首页");
                $(".seller span").text("登录入口");
                $(".contact span").text("联系卖家");
                $(".content-home").addClass("none");
                $(".content-login").removeClass("none");
                $(".content-add").addClass("none");
                $(".content-contact").addClass("none");
                $(".content-register").addClass("none");
            }
        } else if(index == 2) { //联系卖家, 卖家退出登录
            if(user_id) {
                if (user_type == 1) { //卖家
                    getOut();
                } else {
                    /**
                     * 获取卖家联系方式
                     */
                    getContact();
                }
            } else {
                /**
                 * 获取卖家联系方式
                 */
                getContact();
            }
        } else if (index == 3) { //买家退出登录
            if(user_id) {
                getOut();
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
    // getAllCommodity();
    changeRound(1, 0);
    /**
     * 按价格搜索商品
     */
    $("#price-btn").bind("click", function () {
        // mPage = 0;
        // getAllCommodity();
        changeRound(1, 0);
    });
    /**
     * 按输入内容搜索商品
     */
    $("#content-btn").bind("click", function () {
        // mPage = 0;
        // getAllCommodity();
        changeRound(1, 0);
    });
    /**
     * 关闭详细信息弹窗
     */
    $(".puBoxCommodity .overLay").bind("click",function () {
        $(".puBoxCommodity").css("display","none");
        $(".delete-commodity .delete").unbind("click");
        $(".delete-commodity .change").unbind("click");
        $("#detail-collection").unbind("click");
        mDataInfo = null;
    });
    /**
     * 登录页面
     * 登录页面验证（电话号码、密码、图形验证码），在不合法时出现tip提示信息
     */
    $("#telInput, #register-phone, #register-share-phone").bind("blur",function () {
        let reg = /^1(3|4|5|7|8)[0-9]\d{8}$/;
        let num = $(this).val().trim();
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
    $("#pswInput, #register-password").bind("blur",function () {
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
            $(this).siblings().prop("type","text");
            $(this).addClass("open");
        } else {
            $(this).siblings().prop("type","password");
            $(this).removeClass("open");
        }
    });
    /**
     * 检查手机号是否注册
     */
    $("#telInput").on('input',function(e){
        let value = $(this).val().trim();
        let reg = /^1(3|4|5|7|8)[0-9]\d{8}$/;
        if (value.length == 11 && checkPhone) {
            if(!reg.test(value)){
                $(this).siblings(".status").removeClass("tick");
                $(this).siblings(".tip").css("display","block");
                return;
            }
            document.activeElement.blur();
            checkPhone = false;
            let layerLoad = layer.load({type: 2});
            let param = {
                phone: value,
            };
            $.ajax({
                type: "POST",
                url: url_checkPhone,
                data: param,
                dataType: "json",
                success: function (res) {
                    console.log("checkPhone", res);
                    layer.close(layerLoad);
                    if (res) {
                        if (res.status == 1) {
                            layer.open({
                                content: res.msg,
                                btn: '去注册',
                                yes: function () {
                                    layer.closeAll();
                                    checkPhone = true;
                                    $(".home span").text("首页");
                                    $(".seller span").text("注册账户");
                                    $(".outBtn span").text("联系卖家");
                                    $(".content-login").addClass("none");
                                    $(".content-add").addClass("none");
                                    $(".content-register").removeClass("none");
                                },
                                cancel: function () {
                                    layer.closeAll();
                                    checkPhone = true;
                                }
                            });
                        } else {
                            checkPhone = true;
                        }
                    }
                },
                error: function (err) {
                    layer.close(layerLoad);
                    console.log(err);
                    checkPhone = true;
                }
            });
        } else {
            $(this).siblings(".tip").css("display","none");
            $(this).siblings(".status").addClass("tick");
        }
    });
    /**
     * 获取短信验证码
     */
    $("#code-btn").bind("click", function () {
        if (timerClick) {
            let phone = $("#register-phone").val().trim();
            if(!phone){
                $(".telInputBox .tip").css("display","block");
            }
            if(phone == ""){
                layer.open({
                    content: '请输入手机号',
                    btn: '确定'
                });
                return;
            }
            if (timer != null) {
                clearInterval(timer);
                timer = null;
            }
            timerClick = false;
            let layerLoad = layer.load({type: 2});
            let param = {
                phone: phone,
            };
            $.ajax({
                type: "POST",
                url: url_get_sms_code,
                data: param,
                dataType: "json",
                success: function (res) {
                    console.log("get_sms_code", res);
                    layer.close(layerLoad);
                    if (res) {
                        if (res.status == 1) {
                            $("#code-btn").addClass("active");
                            $("#code-btn").text("60s");
                            timer = setInterval(function () {
                                let time = parseInt($("#code-btn").text());
                                if (time > 0) {
                                    time--;
                                    $("#code-btn").text(time + "s");
                                } else {
                                    if (timer != null) {
                                        $("#code-btn").removeClass("active");
                                        timerClick = true;
                                        clearInterval(timer);
                                        timer = null;
                                    }
                                    $("#code-btn").text("重新发送");
                                }
                            }, 1000);
                        } else {
                            timerClick = true;
                        }
                        layer.open({
                            content: res.msg,
                            btn: '确定',
                        });
                    }
                },
                error: function (err) {
                    layer.close(layerLoad);
                    timerClick = true;
                    console.log(err);
                }
            });
        }
    });
    /**
     * 注册按钮
     */
    $("#registerBtn").bind("click",function () {
        register();
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
    $("#submitBtn").bind("click",function () {
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
    $('#video-list li input').on('change', function() {
        addVideo(this);
    });
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

    // //判断数据是否存在
    // if (data != null && data.length > 0) {
    //     //获取数据后，根据当前页面显示的数量将数据分页，比如当前一页显示 mPageNum 条，把页面分成 data.length/10 页，向上取整
    //     mRound = Math.ceil(data.length / mPageNum);
    //     //从整个数据中获取当前页要显示的数据，要根据当前显示的第几页去获取数据
    //     //比如要取第三页的内容，那么就要从整个数据的第 30 条往后取 10 条，如果不够 10 条，就全部取
    //     changeRound(1, 0);
    // } else {
    //     //没得数据
    // }
    /**
     * 上一页
     */
    $("#btn-pre").bind("click", function () {
        if(currentItem > 0) {
            if (currentItem == 1) { //当前是第一页，则不能在向上一页点击，隐藏当前按钮

            } else { //当前不是第一页，则向前翻页，
                let item = parseInt(currentItem) - 1;
                if (item < Math.ceil(mRoundListLength/2)) {
                    currentIndex--;
                } else if (item >= Math.ceil(mRoundListLength/2) && item <= mRound - Math.ceil(mRoundListLength/2)) {
                    currentIndex = Math.floor(mRoundListLength/2);
                } else {
                    currentIndex--;
                }
                changeRound(item, currentIndex);
            }
        } else {

        }
    });
    /**
     * 下一页
     */
    $("#btn-next").bind("click", function () {
        if(currentItem <= mRound) {
            if (currentItem == mRound) { //当前是最后页，则不能在向后一页点击，隐藏当前按钮

            } else { //当前不是最后页，则向后翻页，
                let item = parseInt(currentItem) + 1;
                if (item < Math.ceil(mRoundListLength/2)) {
                    currentIndex++;
                } else if (item >= Math.ceil(mRoundListLength/2) && item <= mRound - Math.floor(mRoundListLength/2)) {
                    currentIndex = Math.floor(mRoundListLength/2);
                } else {
                    currentIndex++;
                }
                changeRound(item, currentIndex);
            }
        } else {

        }
    });
    /**
     * 首页
     */
    $("#btn-first").bind("click", function () {
        changeRound(1, 0);
    });
    /**
     * 尾页
     */
    $("#btn-last").bind("click", function () {
        changeRound(mRound, mRoundListLength - 1);
    });
    /**
     * 获取分页宝贝列表
     */
    $(".commodity-box #select").bind("change", function () {
        // console.log($(this).val());
        mSelectIndex = $(this).val() - 1;
        $("#select option").eq(mSelectIndex).attr('selected', true).siblings().attr('selected', false);
        changeRound(mSelectIndex + 1, 0);
    });
});

/**
 * 获取卖家联系方式
 */
function getContact() {
    let layerLoad = layer.load({type: 2});
    let postData = {};
    $.ajax({
        type: "POST",
        url: url_getContact,
        data: postData,
        dataType: "json",
        success: function (res) {
            console.log("getContact", res);
            layer.close(layerLoad);
            if (res) {
                if (res.status == 1){
                    let data = res.list[0];
                    if (data) {
                        $(".content-contact").removeClass("none");
                        if (data.seller_name != null && data.seller_name != "") {
                            $("#seller-name").removeClass("none").addClass("item");
                            $("#seller-name .seller-name").text(data.seller_name);
                        } else {
                            $("#seller-name").removeClass("item").addClass("none");
                        }
                        if (data.fixed_phone != null && data.fixed_phone != "") {
                            $("#fixed-phone").removeClass("none").addClass("item");
                            $("#fixed-phone .fixed-phone").text(data.fixed_phone);
                        } else {
                            $("#fixed-phone").removeClass("item").addClass("none");
                        }
                        if (data.phone != null && data.phone != "") {
                            $("#mobile-phone").removeClass("none").addClass("item");
                            $("#mobile-phone .mobile-phone").text(data.phone);
                        } else {
                            $("#mobile-phone").removeClass("item").addClass("none");
                        }
                        if (data.wei_xin != null && data.wei_xin != "") {
                            $("#weixin-phone").removeClass("none").addClass("item");
                            $("#weixin-phone .weixin-phone").text(data.wei_xin);
                        } else {
                            $("#weixin-phone").removeClass("item").addClass("none");
                        }
                        if (data.address != null && data.address != "") {
                            $("#address").removeClass("none").addClass("item");
                            $("#address .address").text(data.address);
                        } else {
                            $("#address").removeClass("item").addClass("none");
                        }
                    }
                } else {
                    layer.open({
                        content: res.msg,
                        btn: '确定'
                    });
                }
            }
        },
        error: function (err) {
            layer.close(layerLoad);
            console.log(err);
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
    let layerLoad = layer.load({type: 2});
    let param = {
        type: type
    };
    console.log("param", param);
    $.ajax({
        type: "POST",
        url: url_addCommodityClassify,
        data: param,
        dataType: "json",
        success: function (res) {
            layer.close(layerLoad);
            console.log("addClassifyList", res);
            if(res) {
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
        },
        error: function (err) {
            layer.close(layerLoad);
            console.log(err);
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
                document.getElementById('classify-list').innerHTML = "";
                if (res.status == 1){
                   classifyData = res.list;
                   let classifyList = document.getElementById('classify-list-tmp').innerHTML;
                   document.getElementById('classify-list').innerHTML = doT.template(classifyList)(classifyData);
                    $("#classify-list li").bind("click", function () {
                        $(this).addClass("active").siblings().removeClass("active");
                        mType = $(this)[0].innerHTML;
                        // mPage = 0;
                        // getAllCommodity();
                        changeRound(1, 0);
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
// function getAllCommodity() {
//     // if (mPage == 0) {
//     //     $("#commodity-list").empty();
//     // }
//     // $('#loadBtn').addClass('none');
//     // $('#loadImg').removeClass('none');
//     // $('#loadMsg').addClass('none');
//     let price_min = $("#min-price").val().trim();
//     let price_max = $("#max-price").val().trim();
//     let content = $("#content").val().trim();
//     if (price_min != "" && price_max != "" && parseInt(price_max) < parseInt(price_min)) {
//         layer.open({
//             content: '最高价格不得低于最低价格'
//             ,btn: '确定'
//         });
//         return false;
//     }
//     let param = {
//         page: mPage,
//         type: mType == "全部" ? "" : mType,
//         price_min: price_min,
//         price_max: price_max,
//         content: content
//     };
//     console.log("param", param);
//     $.ajax({
//         type: "POST",
//         url: url_getCommodityList,
//         data: param,
//         dataType: "json",
//         success: function (res) {
//             if (res) {
//                 console.log("getAllCommodity=", res);
//                 if (res.status == 1){
//                     mCurrentPageData = res.list;
//                     // let data = res.list;
//                     // let commodityListTmp = document.getElementById('commodity-list-tmp').innerHTML;
//                     // $('#commodity-list').innerHTML = doT.template(commodityListTmp)(data);
//                     // if (mPage == 0) {
//                     //     $("#commodity-list").empty();
//                     //     $('#commodity-list').append(doT.template(commodityListTmp)(data));
//                     // } else {
//                     //     $('#commodity-list').append(doT.template(commodityListTmp)(data));
//                     // }
//                     // mRound = res.countPage;
//                     // if(data.length < 12){
//                     //     $('#loadBtn').addClass('none');
//                     //     $('#loadImg').addClass('none');
//                     //     $('#loadMsg').addClass('none');
//                     //     $('#loadMsg').text('没有更多了');
//                     // }else {
//                     //     $('#loadBtn').removeClass('none');
//                     //     $('#loadImg').addClass('none');
//                     //     $('#loadMsg').addClass('none');
//                     // }
//                     // mPage++;
//                 } else {
//                     // if (mPage == 0) {
//                     //     $("#commodity-list").empty();
//                     // } else {
//                     //     mPage--;
//                     // }
//                     // $('#loadBtn').addClass('none');
//                     // $('#loadImg').addClass('none');
//                     // $('#loadMsg').removeClass('none');
//                     // $('#loadMsg').text(res.msg);
//                 }
//             }
//         }
//     });
//  }

/**
 * 改变 round 的显示
 * @param index 表示当前显示的第几页
 */
function changeRoundList(index) {
    // console.log("index=", index);
    //先清空掉之前的选项
    mRoundList = [];
    //根据当前 currentItem 的位置去显示当前要显示的 roundlist
    //判断当前 currentItem 在 roundlist 中的位置，一行显示的 mRoundListLength（5）个，那么以 currentItem 为中心，前面三个，后面三个
    if (mRound > mRoundListLength) {
        //如果分页的数量大于基础展示的数量，就展示基础数量个 round
        //判断当前的 round 的位置
        if (index <= Math.ceil(mRoundListLength/2)) {
            //如果当前的 round 的位置小于 mRoundListLength（基础展示数量）的一半，
            // 则从 1 开始展示后面基础展示的数量 mRoundListLength（基础展示数量）
            for (let i = 1; i<= mRound; i++) {
                if (i <= mRoundListLength) {
                    mRoundList.push(i);
                }
            }
        } else if (index > Math.ceil(mRoundListLength/2) && index <= mRound - Math.ceil(mRoundListLength/2)) {
            // 如果当前的 round 的位置大于 mRoundListLength（基础展示数量）的一半，
            // 并且当前位置离 mRound（整个 round 数量）的最后一个要大于等于 mRoundListLength（基础展示数量）的一半，
            // 那就展示当前位置前 mRoundListLength（基础展示数量）的一半的位置到后面的 mRoundListLength（基础展示数量） 个
            for (let i = index - Math.floor(mRoundListLength/2); i<= mRound; i++) {
                if (i < parseInt(index - Math.floor(mRoundListLength/2)) + parseInt(mRoundListLength)) {
                    mRoundList.push(i);
                }
            }
        } else {
            // 如果当前的 round 的位置大于 mRoundListLength（基础展示数量）的一半，
            // 并且当前位置离 mRound（整个 round 数量）的最后一个要小于 mRoundListLength（基础展示数量）的一半，
            // 那就展示 mRound（整个 round 数量）的倒数 mRoundListLength（基础展示数量） 个
            for (let i = mRound - mRoundListLength + 1; i <= mRound; i++) {
                mRoundList.push(i);
            }
            // for (let i = mRound; i > mRound - mRoundListLength; i--) {
            //     mRoundList.push(i);
            //     //从最后一个开始的话，需要倒叙数组
            //     mRoundList.reverse();
            // }
        }
    } else {
        //如果分页的数量不够基础展示的数量，就全部显示
        for (let i = 1; i<= mRound; i++) {
            mRoundList.push(i);
        }
    }
    let template = document.getElementById('template-table-round').innerHTML;
    document.getElementById('table-round').innerHTML = doT.template(template)(mRoundList);
}

/**
 * 改变当前显示的数据
 * @param item 显示第几页的数据
 * @param index 点击的当前 round 的下标
 */
function changeRound(item, index) {
    let price_min = $("#min-price").val().trim();
    let price_max = $("#max-price").val().trim();
    let content = $("#content").val().trim();
    if (price_min != "" && price_max != "" && parseInt(price_max) < parseInt(price_min)) {
        layer.open({
            content: '最高价格不得低于最低价格',
            btn: '确定'
        });
        return false;
    }
    let param = {
        user_id: user_id,
        page: item - 1,
        type: mType == "全部" ? "" : mType,
        price_min: price_min,
        price_max: price_max,
        content: content
    };
    console.log("param", param);
    let layerLoad = layer.load({type: 2});
    $.ajax({
        type: "POST",
        url: list_type == 1 ? url_getCommodityList : url_getCollectList,
        data: param,
        dataType: "json",
        success: function (res) {
            layer.close(layerLoad);
            console.log("getAllCommodity=", res);
            if (res) {
                document.getElementById('commodity-list').innerHTML = "";
                if (res.status == 1){
                    mCurrentPageData = res.list;
                    mRound = res.countPage;
                    currentItem = item;
                    let template = document.getElementById('commodity-list-tmp').innerHTML;
                    document.getElementById('commodity-list').innerHTML = doT.template(template)(mCurrentPageData);
                    $(".commodity-box").scrollTop(0);
                    if (item == 1 && mCurrentPageData.length < 12) { //如果是第一页，并且第一页的数量不足12条，则后续没有分页，则隐藏分页栏
                        $("#table-btn").removeClass("table-btn").addClass("none");
                        $("#table-select").removeClass("table-select").addClass("none");
                    } else {
                        $("#table-btn").removeClass("none").addClass("table-btn");
                        $("#table-select").removeClass("none").addClass("table-select");
                        $("#select").empty();
                        for (let i = 1; i <= mRound; i++) {
                            $("#select").append("<option value='"+ i + "'>" + i + "</option>");//新增
                        }
                        $("#select option").eq(mSelectIndex).attr("selected", true);//选中第一个
                        if (item < Math.ceil(mRoundListLength/2)) {
                            currentIndex = item - 1;
                        } else if (item >= Math.ceil(mRoundListLength/2) && item <= mRound - Math.floor(mRoundListLength/2)) {
                            currentIndex = Math.floor(mRoundListLength/2);
                        } else {
                            currentIndex = mRoundListLength - (mRound - item) - 1;
                        }
                        changeRoundList(item);
                        if (item == 1) { //当前是第一页，隐藏上一页按钮
                            $("#btn-pre, #btn-first").removeClass("btn").addClass("none");
                        } else {
                            $("#btn-pre, #btn-first").removeClass("none").addClass("btn");
                        }
                        if (item == mRound) {//当前是最后一页，隐藏下一页按钮
                            $("#btn-next, #btn-last").removeClass("btn").addClass("none");
                        } else {
                            $("#btn-next, #btn-last").removeClass("none").addClass("btn");
                        }
                        // mCurrentPageData = []; //先清空当前显示内容
                        //根据 index 改变 round 列表的选中项
                        $(".table-round li").eq(parseInt(currentIndex)).addClass("active").siblings().removeClass("active");
                        // //从整个数据中获取当前页要显示的数据，要根据当前显示的第几页去获取数据
                        // //比如要取第三页的内容，那么就要从整个数据的第 30 条往后取 mPageNum 条，如果不够 mPageNum 条，就全部取
                        // for (let i in data) {
                        //     if (i < item*mPageNum && i >= (parseInt(item)-1)*mPageNum) {
                        //         mCurrentPageData.push(data[i]);
                        //     }
                        // }
                    }
                } else {
                    if (item == 1) { //一条数据都没有，则隐藏分页栏
                        $("#table-btn").removeClass("table-btn").addClass("none");
                        $("#table-select").removeClass("table-select").addClass("none");
                    }
                    layer.open({
                        content: res.msg,
                        btn: '确定'
                    });
                }
            }
        },
        error: function (err) {
            layer.close(layerLoad);
            console.log("err", err);
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
        canvasDataURL(e.target.result, function (res) {
            let picSrc = {
                src: res,
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
        });
    }
}
/**
 * 删除图片
 */
function deletePic(index) {
    picData.remove(index);
    if (picData.length > 0 && picData[0].type == 2) {
        let picSrc = {
            src: pic_demo,
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
 * 添加视频
 * @param obj
 * @returns {boolean}
 */
function addVideo(obj) {
    let files = obj.files,
        videoURL = null,
        windowURL = window.URL || window.webkitURL;
    if (files && files[0]) {
        videoURL = windowURL.createObjectURL(files[0]);
        $(obj).siblings("video").attr("src", videoURL);
        let video = $(obj).siblings("video")[0];
        console.log("video=", video);
        setTimeout(function() {
            videoScaleImg(video, function (res) {
                console.log("res=", res);
                $(obj).siblings("img").attr("src", res);
                $(obj).siblings("video").attr("poster", res);
                $(obj).siblings(".scale-img").val(res);
                $(obj).siblings("i").removeClass("none");
                $(obj).siblings("span").removeClass("none");
            });
        }, 500);
    }
}
/**
 * 获取视频缩略图
 * @param callback
 */
function videoScaleImg(obj, callback) {
    // 默认按比例压缩
    let maxWidth = 200;
    let maxHeight = 200;
    let w, h;
    let ww = maxWidth / obj.videoWidth;
    let hh = maxHeight / obj.videoHeight;
    let rate = (ww < hh) ? ww: hh;
    if (rate <= 1) {
        w = obj.videoWidth * rate;
        h = obj.videoHeight * rate;
    } else {
        w = obj.videoWidth;
        h = obj.videoHeight;
    }
    let quality = 0.5;  // 默认图片质量为0.7
    //生成canvas
    let canvas = document.createElement('canvas');
    let ctx = canvas.getContext('2d');
    // 创建属性节点
    let anw = document.createAttribute("width");
    anw.nodeValue = w;
    let anh = document.createAttribute("height");
    anh.nodeValue = h;
    canvas.setAttributeNode(anw);
    canvas.setAttributeNode(anh);
    ctx.drawImage(obj, 0, 0, w, h);
    // quality值越小，所绘制出的图像越模糊
    let src = canvas.toDataURL('image/jpeg', quality);
    console.log("src=", src.length);
    callback(src);
}
/**
 * 删除视频
 */
function deleteVideo(obj) {
    $(obj).siblings("img").attr("src", video_demo);
    $(obj).siblings("input").val("");
    $(obj).siblings("video").attr("src", "");
    $(obj).siblings("video").attr("poster", "");
    $(obj).siblings(".scale-img").val("");
    $(obj).siblings("i").addClass("none");
    $(obj).siblings("span").addClass("none");
}
/**
 * 播放视频
 */
function playVideo(obj) {
    console.log($(obj).siblings("video")[0].src);
    $("#play-box").removeClass("none");
    $("#play-box video").attr("src", $(obj).siblings("video")[0].src);
    $("#play-box video").attr("poster", $(obj).siblings("video")[0].poster);
}
/**
 * 关闭播放视频
 */
function cancelBox() {
    $("#play-box").addClass("none");
    $("#play-box video").attr("src", "");
    $("#play-box video").attr("poster", "");
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
    let name = $("#name").val().trim();
    if(name == ''){
        layer.open({
            content: '请输入商品名称或描述'
            ,btn: '确定'
        });
        return;
    }
    if (picData[picData.length - 1].type == 1 && $(".scale-img").val() == "") {
        layer.open({
            content: '请至少上传一张商品图片或视频'
            ,btn: '确定'
        });
        return;
    }
    let layerLoad = layer.load({type: 2});
    $.ajax({
        url: url_addCommodity,
        type: 'POST',
        cache: false,
        data: new FormData($('#add-form')[0]),
        processData: false,
        contentType: false,
        dataType:"json",
        beforeSend: function(){
        },
        success : function(res) {
            layer.close(layerLoad);
            console.log("res", res);
            if (res) {
                if (res.status == 1){
                    $("#commodity-id").val("");
                    $("#type").val("");
                    $("#code").val("");
                    $("#money").val("");
                    $("#remarks").val("");
                    $("#name").val("");
                    picData = [{src: pic_demo, type: 1}];
                    updetaPic();
                    $("#video-list li").find("img").attr("src", video_demo);
                    $("#video-list li").find("input").val("");
                    $("#video-list li").find("video").attr("src", "");
                    $("#video-list li").find("video").attr("poster", "");
                    $("#video-list li").find(".scale-img").val("");
                    $("#video-list li").find("i").addClass("none");
                    $("#video-list li").find("span").addClass("none");
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
        },
        error: function (err) {
            layer.close(layerLoad);
            console.log("err", err);
            layer.open({
                content: "上传失败，访问异常",
                btn: '确定'
            });
        }
    });
}
/**
 * 查看商品详细
 */
function lookDetail(id) {
    let layerLoad = layer.load({type: 2});
    let param = {
        user_id: user_id,
        id: id
    };
    $.ajax({
        type: "POST",
        url: url_getCommodityDetail,
        data: param,
        dataType: "json",
        success: function (res) {
            layer.close(layerLoad);
            console.log("lookDetail=", res);
            if(res) {
                if (res.status == 1){
                    $(".puBoxCommodity").css('display','block');
                    mDataInfo = res.data;
                    let picArray = [];
                    if (mDataInfo.img && mDataInfo.img.length > 0) {
                        for(let i = 0; i<mDataInfo.img.length; i++){
                            let pic = {
                                src: mDataInfo.img[i].src,
                                type: 1
                            };
                            picArray.push(pic);
                        }
                    }
                    if (mDataInfo.screen && mDataInfo.screen.length > 0) {
                        for(let i = 0; i<mDataInfo.screen.length; i++){
                            let pic = {
                                src: mDataInfo.screen[i].src,
                                scale_img: mDataInfo.screen[i].scale_img,
                                type: 2
                            };
                            picArray.push(pic);
                        }
                    }
                    let swiper = document.getElementById('swiper-list-tmp').innerHTML;
                    document.getElementById('swiper-list').innerHTML = doT.template(swiper)(picArray);
                    mySwiper.updateSize();
                    mySwiper.updateSlides();
                    mySwiper.update();
                    mySwiper.updateProgress();
                    mySwiper.slideTo(0, 10, false);
                    $("#pu-type").text(mDataInfo.type);
                    $("#pu-code").text(mDataInfo.number);
                    $("#pu-price").text(mDataInfo.remarks + formatPrice(mDataInfo.price));
                    $("#pu-content").text(mDataInfo.content);
                    if (user_id != null) {
                        if (user_type == 2) { //买家显示收藏
                            $("#detail-collection").removeClass("none").addClass("item");
                            if (mDataInfo.collection == 1) { //未收藏
                                $("#detail-collection span").text("收藏");
                                $("#detail-collection i").removeClass("active");
                            } else { //已收藏
                                $("#detail-collection span").text("取消收藏");
                                $("#detail-collection i").addClass("active");
                            }
                        } else { // 卖家不显示收藏
                            $("#detail-collection").removeClass("item").addClass("none");
                        }
                    } else {
                        $("#detail-collection").removeClass("none").addClass("item");
                        $("#detail-collection span").text("收藏");
                        $("#detail-collection i").removeClass("active");
                    }
                    $("#detail-collection").bind("click", function () {
                        collectionClick(this);
                    });
                    // console.log(user_id + "--" + user_type);
                    if(user_type != null && user_type == 1){
                        $("#delete-commodity").removeClass("none").addClass("delete-commodity");
                        $("#delete-commodity .delete").bind("click", function () {
                            deleteCommodity(mDataInfo.id);
                        });
                        $("#delete-commodity .change").bind("click", function () {
                            commodity_id = mDataInfo.id;
                            changeCommodity(mDataInfo);
                        });
                    } else {
                        $("#delete-commodity").removeClass("delete-commodity").addClass("none");
                    }
                } else {
                    layer.open({
                        content: res.msg,
                        btn: '确定'
                    });
                }
            }
        },
        error: function (err) {
            layer.close(layerLoad);
            console.log(err);
        }
    });
}
/**
 * 收藏按钮
 * @param obj
 */
function collectionClick(obj) {
    if (user_id != null) { //已登录
        if (mDataInfo.collection == 0) { //已收藏，取消收藏
            cancelCollection(obj);
        } else { //未收藏，点击收藏
            collection(obj);
        }
    } else { //未登录，显示登录
        layer.open({
            content: "抱歉，收藏功能需要登录后才能使用",
            btn: '确定',
            yes: function () {
                layer.closeAll();
                $(".wrapperTab .tabItem").eq(1).addClass("active").siblings().removeClass("active");
                $(".puBoxCommodity").css("display","none");
                $(".delete-commodity .delete").unbind("click");
                $(".delete-commodity .change").unbind("click");
                $("#detail-collection").unbind("click");
                $(".content-home").addClass("none");
                $(".content-login").removeClass("none");
                $(".content-add").addClass("none");
                $(".content-contact").addClass("none");
                $(".content-register").addClass("none");
            },
            cancel: function () {
                layer.closeAll();
            }
        });
    }
}
/**
 * 点击收藏
 * @param obj
 */
function collection(obj) {
    let layerLoad = layer.load({type: 2});
    let param = {
        user_id: user_id,
        id: mDataInfo.id
    };
    console.log("param", param);
    $.ajax({
        type: "POST",
        url: url_collection,
        data: param,
        dataType: "json",
        success: function (res) {
            layer.close(layerLoad);
            console.log("collection", res);
            if (res.status == 1){
                $(obj).find("span").text("取消收藏");
                $(obj).find("i").addClass("active");
                // mPage = 0;
                // getAllCommodity();
                mDataInfo.collection = 0;
                changeRound(currentItem, currentIndex);
            }
            layer.open({
                content: res.msg,
                btn: '确定'
            });
        },
        error: function (err) {
            layer.close(layerLoad);
            console.log(err);
        }
    });
}
/**
 * 取消收藏
 * @param obj
 */
function cancelCollection(obj) {
    let layerLoad = layer.load({type: 2});
    let param = {
        user_id: user_id,
        id: mDataInfo.id
    };
    console.log("param", param);
    $.ajax({
        type: "POST",
        url: url_cancelCollection,
        data: param,
        dataType: "json",
        success: function (res) {
            layer.close(layerLoad);
            console.log("cancelCollection", res);
            if (res.status == 1){
                $(".puBoxCommodity").css('display','none');
                $(".delete-commodity .delete").unbind("click");
                $(".delete-commodity .change").unbind("click");
                $("#detail-collection").unbind("click");
                mDataInfo = null;
                $(obj).find("span").text("收藏");
                $(obj).find("i").removeClass("active");
                // mPage = 0;
                // getAllCommodity();
                changeRound(currentItem, currentIndex);
            }
            layer.open({
                content: res.msg,
                btn: '确定'
            });
        },
        error: function (err) {
            layer.close(layerLoad);
            console.log(err);
        }
    });
}
/**
 * 删除宝贝
 * @param id
 */
function deleteCommodity(id) {
    let layerLoad = layer.load({type: 2});
    let param = {
        id: id
    };
    console.log("param", param);
    $.ajax({
        type: "POST",
        url: url_deleteCommodity,
        data: param,
        dataType: "json",
        success: function (res) {
            layer.close(layerLoad);
            console.log("deleteCommodity", res);
            if (res.status == 1){
                $(".puBoxCommodity").css('display','none');
                $(".delete-commodity .delete").unbind("click");
                $(".delete-commodity .change").unbind("click");
                $("#detail-collection").unbind("click");
                mDataInfo = null;
                // mPage = 0;
                // getAllCommodity();
                changeRound(currentItem, currentIndex);
            }
            layer.open({
                content: res.msg,
                btn: '确定'
            });
        },
        error: function (err) {
            layer.close(layerLoad);
            console.log(err);
        }
    });
}
/**
 * 修改宝贝
 * @param data
 */
function changeCommodity(data) {
    if (data) {
        $(".puBoxCommodity").css("display","none");
        $(".delete-commodity .delete").unbind("click");
        $(".delete-commodity .change").unbind("click");
        $("#detail-collection").unbind("click");
        mDataInfo = null;
        $(".wrapperTab .tabItem").eq(1).addClass("active").siblings().removeClass("active");
        getClassifyList();
        $("#commodity-id").val(data.id);
        $(".content-home").addClass("none");
        $(".content-login").addClass("none");
        $(".content-add").removeClass("none");
        $(".content-contact").addClass("none");
        $(".content-register").addClass("none");
        $("#type").val(data.type);
        $("#code").val(data.number);
        let price = parseFloat(data.price);
        if (price >= 100000000) {
            $("#money").val(price/100000000);
            $(".select").val("亿元");
        } else if (price < 100000000 && price >= 10000) {
            $("#money").val(price/10000);
            $(".select").val("万元");
        } else {
            $("#money").val(price);
            $(".select").val("元");
        }
        $("#remarks").val(data.remarks);
        $("#name").val(data.content);
        if (data.img != null) {
            picData = data.img;
            if (data.img.length < 6) {
                let picSrc = {
                    src: pic_demo,
                    type: 1
                };
                picData.unshift(picSrc);
            }
        } else {
            picData = [{src: pic_demo, type: 1}];
        }
        if (data.screen != null) {
            $("#video-list li img").attr("src", data.screen[0].scale_img);
            $("#video-list li video").attr("src", data.screen[0].src);
            $("#video-list li video").attr("poster", data.screen[0].scale_img);
            $("#video-list li .scale-img").val(data.screen[0].scale_img);
            $("#video-list li i").removeClass("none");
            $("#video-list li span").removeClass("none");
        } else {
            $("#video-list li").find("img").attr("src", video_demo);
            $("#video-list li").find("input").val("");
            $("#video-list li").find("video").attr("src", "");
            $("#video-list li").find("video").attr("poster", "");
            $("#video-list li").find(".scale-img").val("");
            $("#video-list li").find("i").addClass("none");
            $("#video-list li").find("span").addClass("none");
        }
        updetaPic();
    }
}
/**
 * 登录
 */
function login() {
    let password = $("#pswInput").val().trim();
    let phone = $("#telInput").val().trim();
    let captcha = $("#captchaInput").val().trim();
    if(!phone){
        $(".telInputBox .tip").css("display","block");
    }
    if(phone == ''){
        layer.open({
            content: '请输入手机号',
            btn: '确定'
        });
        return;
    }
    if(!password){
        $(".pswInputBox .tip").css("display","block");
    }
    if(password == ''){
        layer.open({
            content: '请输入密码',
            btn: '确定'
        });
        return;
    }
    if(!captcha){
        $(".captchaInputBox .tip").css("display","block");
    }
    if(captcha == ''){
        layer.open({
            content: '请输入验证码',
            btn: '确定'
        });
        return;
    }
    let layerLoad = layer.load({type: 2});
    let param = {
        phone: phone,
        password: password,
        code: captcha
    };
    $.ajax({
        type: "POST",
        url: url_login,
        data: param,
        dataType: "json",
        success: function (res) {
            layer.close(layerLoad);
            console.log("login", res);
            if (res) {
                if (res.status == 1) {
                    user_id = res.user_id;
                    user_type = res.identity;
                    sessionStorage.setItem("user_id", res.user_id);
                    sessionStorage.setItem("user_type", res.identity);
                    if (user_type == 2) { //买家
                        list_type = 1;
                        $(".wrapperTab .tabItem").eq(0).addClass("active").siblings().removeClass("active");
                        $(".home span").text("首页");
                        $(".seller span").text("我的收藏");
                        $(".contact span").text("联系卖家");
                        $(".outBtn span").text("退出登录");
                        $(".content-home").removeClass("none");
                        $(".content-login").addClass("none");
                        $(".content-add").addClass("none");
                        $(".content-register").addClass("none");
                        $(".outBtn").removeClass("none");
                        $(".contact").removeClass("none");
                    } else {
                        $(".home span").text("卖家宝贝");
                        $(".seller span").text("添加宝贝");
                        $(".outBtn span").text("退出登录");
                        $(".content-home").addClass("none");
                        $(".content-login").addClass("none");
                        $(".content-add").removeClass("none");
                        $(".content-register").addClass("none");
                        $(".outBtn").removeClass("none");
                        $(".contact").addClass("none");
                    }
                } else if (res.status == -1) {
                    layer.open({
                        content: "该手机号还未注册爱运平台账户",
                        btn: '去注册',
                        yes: function () {
                            layer.closeAll();
                            $(".home span").text("首页");
                            $(".seller span").text("注册账户");
                            $(".outBtn span").text("联系卖家");
                            $(".content-login").addClass("none");
                            $(".content-add").addClass("none");
                            $(".content-register").removeClass("none");
                        },
                        cancel: function () {
                            layer.closeAll();
                        }
                    });
                } else {
                    layer.open({
                        content: res.msg,
                        btn: '确定',
                        end:function () {
                            $("#captchaImg").attr("src","http://a56999.com/Aiyunbao/Treasure/getVerifyCode?"+Math.random());
                        }
                    });
                }
            }
        },
        error: function (err) {
            layer.close(layerLoad);
            console.log(err);
        }
    });
}
/**
 * 注册
 */
function register() {
    let phone = $("#register-phone").val().trim();
    let code = $("#register-code").val().trim();
    let password = $("#register-password").val().trim();
    let sharePhone = $("#register-share-phone").val().trim();
    if(!phone){
        $(".telInputBox .tip").css("display","block");
    }
    if(phone == ""){
        layer.open({
            content: '请输入手机号',
            btn: '确定'
        });
        return;
    }
    if(!code){
        $(".captchaInputBox .tip").css("display","block");
    }
    if(code == ''){
        layer.open({
            content: '请输入短信验证码',
            btn: '确定'
        });
        return;
    }
    if(!password){
        $(".pswInputBox .tip").css("display","block");
    }
    if(password == ''){
        layer.open({
            content: '请输入密码',
            btn: '确定'
        });
        return;
    }
    let layerLoad = layer.load({type: 2});
    let param = {
        phone: phone,
        password: password,
        verifyCode: code,
        recPhone: sharePhone
    };
    $.ajax({
        type: "POST",
        url: url_register,
        data: param,
        dataType: "json",
        success: function (res) {
            layer.close(layerLoad);
            console.log("register", res);
            if (res) {
                if (res.status == 1) {
                    $(".home span").text("首页");
                    $(".seller span").text("登录");
                    $(".outBtn span").text("联系卖家");
                    $(".content-login").removeClass("none");
                    $(".content-home").addClass("none");
                    $(".content-add").addClass("none");
                    $(".content-register").addClass("none");
                }else{
                    layer.open({
                        content: res.msg,
                        btn: '确定',
                    });
                }
            }
        },
        error: function (err) {
            layer.close(layerLoad);
            console.log(err);
        }
    });
}
/**
 * 退出登录
 */
function getOut() {
    let layerLoad = layer.load({type: 2});
    $.ajax({
        type: "POST",
        url: url_logout,
        data: {},
        dataType: "json",
        success: function (res) {
            layer.close(layerLoad);
            console.log("getOut", res);
            if(res){
                if(res.status == 1) {
                    user_id = null;
                    user_type = null;
                    list_type = 1;
                    sessionStorage.clear();
                    $(".wrapperTab .tabItem").eq(1).addClass("active").siblings().removeClass("active");
                    $(".content-home").addClass("none");
                    $(".content-login").removeClass("none");
                    $(".content-add").addClass("none");
                    $(".content-contact").addClass("none");
                    $(".content-register").addClass("none");
                    $(".home span").text("首页");
                    $(".seller span").text("登录入口");
                    $(".contact span").text("联系卖家");
                    $(".contact").removeClass("none");
                    $(".outBtn").addClass("none");
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
        error: function (err) {
            layer.close(layerLoad);
            console.log(err);
        }
    });
}
/**
 * 压缩图片
 * @param path
 */
function canvasDataURL(path, callback){
    let img = new Image();
    img.src = path;
    img.onload = function(){
        let that = this;
        // 默认按比例压缩
        let w = that.width,
            h = that.height,
            scale = w / h;
        h =  (w / scale);
        let quality = 0.1;  // 默认图片质量为0.7
        //生成canvas
        let canvas = document.createElement('canvas');
        let ctx = canvas.getContext('2d');
        // 创建属性节点
        let anw = document.createAttribute("width");
        anw.nodeValue = w;
        let anh = document.createAttribute("height");
        anh.nodeValue = h;
        canvas.setAttributeNode(anw);
        canvas.setAttributeNode(anh);
        ctx.drawImage(that, 0, 0, w, h);
        // console.log(quality);
        // quality值越小，所绘制出的图像越模糊
        let base64 = canvas.toDataURL('image/jpeg', quality);
        callback(base64);
    };
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
/**
 * 转货币单位
 * @param number
 * @param places
 * @param symbol
 * @param thousand
 * @param decimal
 * @returns {string}
 */
function formatMoney(number, places, symbol, thousand, decimal) {
    number = number || 0;
    places = !isNaN(places = Math.abs(places)) ? places : 2;
    symbol = symbol !== undefined ? symbol : "";
    thousand = thousand || ",";
    decimal = decimal || ".";
    let negative = number < 0 ? "-" : "";
    let  i = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "";
    let j = i.length > 3 ? i.length % 3 : 0;
    return symbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (places ? decimal + Math.abs(number - i).toFixed(places).slice(2) : "");
}
/**
 * 数字转价格
 * @param price
 */
function formatPrice(price) {
    if (price != null) {
        if (parseFloat(price) >= 100000000) {
            return parseFloat(price)/100000000 + "亿元";
        } else if (parseFloat(price) < 100000000 && parseFloat(price) >= 10000) {
            return parseFloat(price)/10000 + "万元";
        } else {
            return parseFloat(price) + "元";
        }
    }
    return price;
}
