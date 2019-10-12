/**
 * Created by zp on 2018/10/30.
 */
let ip = "http://192.168.1.88/Aiyunbao/";
// let ip = "http://a56999.com/Aiyunbao/";
let url_addCommodity = ip + "Treasure/addTreasure"; //添加商品
let pic_demo = "../img/picture_fill.png";
let video_demo = "../img/playon_fill.png";
let picData = [{src: pic_demo, type: 1}]; //存储图片的容器
let videoData = [{src: video_demo, type: 1}]; //存储视频的容器
$(function () {
    /**
     * 更新存储图片数组
     */
    updetaPic();
    // /**
    //  * 更新存储视频数组
    //  */
    // updetaVideo();
    $('#video-list li input').on('change', function() {
        addVideo(this);
    });
    /**
     * 添加宝贝按钮
     */
    $(".add-btn").bind("click", function () {
        uploadCommodity();
    });
});
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
// /**
//  * 更新视频数组
//  */
// function updetaVideo() {
//     let videoList = document.getElementById('video-list-tmp').innerHTML;
//     document.getElementById('video-list').innerHTML = doT.template(videoList)(videoData);
//     $("#file-video").on("change", function () {
//         addVideo(this);
//     });
// }
/**
 * 添加视频
 * @param obj
 * @returns {boolean}
 */
function addVideo(obj) {
    var files = obj.files,
        videoURL = null,
        windowURL = window.URL || window.webkitURL;
    if (files && files[0]) {
        videoURL = windowURL.createObjectURL(files[0]);
        $(obj).siblings("video").attr("src", videoURL);
        setTimeout(function() {
            videoScaleImg(obj, function (res) {
                console.log("res=", res);
                $(obj).siblings("img").attr("src", res);
                $(obj).siblings("i").removeClass("none");
                $(obj).siblings("span").removeClass("none");
            });
        }, 500);
    }
    // let files = obj.files;
    // if (files && files[0]) {
    //     let relSize = parseFloat(files[0].size/1024/1024);
    //     console.log(relSize);
    //     if(relSize > 10){ // 大于10mb
    //         console.log("视频过大，请上传小于10M的视频");
    //         return false;
    //     }
    //     let render = new FileReader();
    //     render.readAsDataURL(files[0]);
    //     render.onload = function (e) {
    //         $('#video1').attr("src", e.target.result);
    //         setTimeout(function() {
    //             videoScaleImg(function (res) {
    //                 let videoSrc = {
    //                     src: files[0],
    //                     scale_img: res,
    //                     type: 2
    //                 };
    //                 videoData.push(videoSrc);
    //                 console.log(videoData);
    //                 if (videoData.length > 1) {
    //                     videoData.shift();
    //                 }
    //                 console.log("value", $("#file-video").val());
    //                 files = null;
    //                 render = null;
    //                 $("#file-video").val("");
    //                 $("#file-video").off("change");
    //                 updetaVideo();
    //             });
    //         }, 500);
    //     }
    // }
}
/**
 * 获取视频缩略图
 * @param callback
 */
function videoScaleImg(obj, callback) {
    let video = $(obj).siblings("video")[0];
    // 默认按比例压缩
    let maxWidth = 200;
    let maxHeight = 200;
    let w, h;
    let ww = maxWidth / video.videoWidth;
    let hh = maxHeight / video.videoHeight;
    let rate = (ww < hh) ? ww: hh;
    if (rate <= 1) {
        w = video.videoWidth * rate;
        h = video.videoHeight * rate;
    } else {
        w = video.videoWidth;
        h = video.videoHeight;
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
    ctx.drawImage(video, 0, 0, w, h);
    // quality值越小，所绘制出的图像越模糊
    let src = canvas.toDataURL('image/jpeg', quality);
    console.log("src=", src.length);
    callback(src);
}
/**
 * 删除视频
 */
function deleteVideo(obj) {
    // videoData.remove(index);
    // let videoSrc = {
    //     src: video_demo,
    //     type: 1
    // };
    // videoData.unshift(videoSrc);
    // updetaVideo();
    $(obj).siblings("img").attr("src", "../img/playon_fill.png");
    $(obj).siblings("input").val("");
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
}
/**
 * 关闭播放视频
 */
function cancelBox() {
    $("#play-box").addClass("none");
    $("#play-box video").attr("src", "");
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
    console.log("data", new FormData($("#add-form")[0]));
    $.ajax({
        url: "http://192.168.1.88/Aiyunbao/Treasure/addTreasure",
        type: 'POST',
        cache: false,
        data: new FormData($('#add-form')[0]),
        processData: false,
        contentType: false,
        dataType:"json",
        beforeSend: function(){
        },
        success : function(res) {
            console.log("res", res);
        },
        error: function (err) {
            console.log("err", err);
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
