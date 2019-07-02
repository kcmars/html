/**
 * Created by keyC on 2019/1/24.
 */
var type = "common"; //默认乘客和司机快车
var linkData = {}; //链接参数
var qrsrc = ""; //二维码地址

$(function () {
    $("#myModal").hide();
    var url = window.location.href;
    if (url.indexOf('?') !== -1) {
        var search = url.substring(url.indexOf('?') + 1);
        var queryArray = search.split('&');
        queryArray.forEach(function (item){
            var itemArray = item.split('=');
            var key = itemArray[0];
            var value = decodeURIComponent(itemArray[1]) ? decodeURIComponent(itemArray[1]) : '';
            linkData[key] = value;
        })
    }
    var ispc = IsPC();
    if (ispc) {
        document.getElementById('sub').innerText = '右键'
    }
    //切换乘客快车司机
    $("#common").bind("click", function () {
        $(this).addClass("active").siblings().removeClass("active");
        type = "common";
        hecheng();
    });
    //切换出租车司机
    $("#taxi").bind("click", function () {
        $(this).addClass("active").siblings().removeClass("active");
        type = "taxi";
        hecheng();
    });
    //切换大巴车司机
    $("#bus").bind("click", function () {
        $(this).addClass("active").siblings().removeClass("active");
        type = "bus";
        hecheng();
    });
    //关闭显示信息
    $(".modal-footer, .close").bind("click", function () {
        $("#myModal").hide();
    });
    //获取名片信息
//        getShareCradInfo();
    //生成二维码
    var qrUrl  = "";
    switch (type) {
        case "common":
            qrUrl= "http://a56999.com/static/Event/appShare/html/recommend_passenger.html?rnd=" + new Date().getTime() + "&type=1";
            break;
        case "taxi":
            qrUrl= "http://a56999.com/static/Event/appShare/html/recommend_driver.html?rnd=" + new Date().getTime() + "&type=2";
            break;
        case "bus":
            qrUrl= "http://a56999.com/static/Event/appShare/html/recommend_contractor.html?rnd=" + new Date().getTime() + "&type=4";
            break;
        default:
            qrUrl= "http://a56999.com/static/Event/appShare/html/recommendLink.html?rnd=" + new Date().getTime() + "&type=1";
            break;
    }
    qrcode(setUrl(linkData, qrUrl));
});

/**
 * 显示名片信息
 */
function showCardInfo() {
    $("#myModal").show();
    $("#user-phone").text("已生成用户" + $.number(linkData.phone) + "专属名片");
    $("#vip-code").text("VIP编码 : " + linkData.tjtel);
}
/**
 * 判断是否是pc端
 * @returns {boolean}
 * @constructor
 */
function IsPC() {
    var userAgentInfo = navigator.userAgent;
    var Agents = ["Android", "iPhone",
        "SymbianOS", "Windows Phone",
        "iPad", "iPod"];
    var flag = true;
    for (var v = 0; v < Agents.length; v++) {
        if (userAgentInfo.indexOf(Agents[v]) > 0) {
            flag = false;
            break;
        }
    }
    return flag;
}

/**
 * 生成二维码
 * @param url
 * @returns {string}
 */
function qrcode(url) {
    jQuery('#qrcodeCanvas').qrcode({
        id: "qrcodeCanvas",
        render: "canvas",
        text: url,
        width: "300",               //二维码的宽度
        height: "300",              //二维码的高度
        background: "#ffffff",       //二维码的后景色
        foreground: "#000000"        //二维码的前景色
    });
    var demo = document.getElementById('qrcodeCanvas');
    qrsrc = demo.childNodes[0].toDataURL("image/jpg", 1);
    hecheng();
}

/**
 * 合成名片
 */
function hecheng() {
    console.log("qrsrc=", qrsrc);
    var data = ["../img/" + type + ".jpg", qrsrc, "../img/logo_no_border.png"];
    var base64 = [];
    draw(data, function () {
        document.getElementById('face').innerHTML = '<img src="' + base64[0] + '">';
        showCardInfo();
    }, base64)
}
/**
 * 画名片
 * @param data
 * @param fn
 * @param base64
 */
function draw(data, fn, base64) {
    var c = document.createElement('canvas'),
        ctx = c.getContext('2d'),
        len = data.length;
    c.width = 1480;
    c.height = 913;
    ctx.rect(0, 0, c.width, c.height);
    ctx.fillStyle = '#fff';
    ctx.fill();
    function drawing(n) {
        if (n < len) {
            var img = new Image;
            img.setAttribute("crossOrigin",'Anonymous'); //解决跨域
            img.src = data[n];
            img.onload = function () {
                switch (n) {
                    case 0:
                        ctx.drawImage(img, 0, 0, 1480, 913);
                        break;
                    case 1:
                        ctx.drawImage(img, 890, 500, 300, 300);
                        break;
                    case 2:
                        ctx.drawImage(img, 990, 595, 100, 100);
                        break;
                }
                drawing(n + 1);//递归
            }
        } else {
            //添加文字
            ctx.font = "300 36px 苹方";
            ctx.fillStyle = '#445963';
            ctx.fillText("VIP编码：" + "cQkkGyttcBkpJmEDe1J4QHtUBV4=", 100, 35);
            //保存生成作品图片
            base64.push(c.toDataURL("image/jpg", 1));
            fn();
        }
    }
    drawing(0);
}
/**
 * 设置跳转链接参数
 * @param url
 */
function setUrl(data, url) {
    if (data != null) {
        if (data.tjtel) {
            url += ("&tjtel=" + data.tjtel);
            if (data.city) {
                url += ("&city=" + data.city);
                if (data.identity) {
                    url += ("&identity=" + data.identity);
                    if (data.award_code) {
                        url += ("&award_code=" + data.award_code);
                    }
                } else if (data.award_code) {
                    url += ("&award_code=" + data.award_code);
                }
            } else if (data.identity) {
                url += ("&identity=" + data.identity);
                if (data.award_code) {
                    url += ("&award_code=" + data.award_code);
                }
            } else if (data.award_code) {
                url += ("&award_code=" + data.award_code);s
            }
        } else if (data.city) {
            url += ("&city=" + data.city);
            if (data.identity) {
                url += ("&identity=" + data.identity);
                if (data.award_code) {
                    url += ("&award_code=" + data.award_code);
                }
            } else if (data.award_code) {
                url += ("&award_code=" + data.award_code);
            }
        } else if (data.identity) {
            url += ("&identity=" + data.identity);
            if (data.award_code) {
                url += ("&award_code=" + data.award_code);
            }
        } else if (data.award_code) {
            url += ("&award_code=" + data.award_code);
        }
    }
    return url;
}