/**
 * Created by keyC on 2019/1/17.
 * 大巴车名片快速上传
 */
var img1 = "";
var img2 = "";
var index = 1;

$(function () {
    getRequest(uploadInfo);
    // uploadInfo();
});

/**
 * 获取负责城市
 */
function getCity() {
    $.ajax({
        type: 'POST',
        url: $.getUploaderCity,
        data: {
            user_id: param.user_id
            // user_id: $.user_id
        },
        success: function (res) {
            loadAlertHide();
            console.log("res", res);
            if (res) {
                if(res.status == 1){
                    let data = res.data;
                    if (data != null) {
                        var obj = document.getElementById('select');
                        for (let i in data) {
                            obj.add(new Option(data[i].city, data[i].citycode));
                        }
                    }
                } else {
                    toastAlertShow(res.msg);
                }
            }
        },
        error: function (err) {
            console.log("err", err);
            loadAlertHide();
            window.location.href = "../../Util/html/error.html";
        }
    })
}

/**
 * 上传名片
 */
function uploadInfo() {
    $("#demo-picture li").bind("click", function () {
        $("#model-big-picture").removeClass("none");
        let index = $(this).index();
        if(index == 0){
            $("#big-picture").attr("src","../img/card_face_demo.jpg");
        } else {
            $("#big-picture").attr("src","../img/card_back_demo.jpg");
        }
    });
    $("#model-big-picture .big-bg").bind("click", function () {
        $("#model-big-picture").addClass("none");
    });
    //获取负责城市
    getCity();
    /**
     * 提交名片
     */
    $(".submit").bind("click", function () {
        if (img1 === "") {
            toastAlertShow("请上传名片正面照片");
            return;
        }
        if (img2 === "") {
            toastAlertShow("请上传名片反面照片");
            return;
        }
        var obj = document.getElementById('select');
        var index = obj.selectedIndex; //序号，取当前选中选项的序号
        if (index === -1) {
            toastAlertShow("未选择负责城市，刷新获取城市后再试");
            return;
        }
        var code = obj.options[index].value;
        loadAlertShow("正在提交...");
        let params = {
            user_id: param.user_id,
            // user_id: $.user_id,
            paths: img1 + "," + img2,
            code: code != null ? code : ""
        };
        $.ajax({
            type: 'POST',
            url: $.uploadInfo,
            data: params,
            success: function (res) {
                loadAlertHide();
                console.log("res", res);
                // console.log("res", params);
                if (res) {
                    if(res.status == 1){
                        $("#img1").attr("src","../img/card_face_add.jpg");
                        $("#img2").attr("src","../img/card_back_add.jpg");
                        img1 = "";
                        img2 = "";
                        toastAlertShow("提交成功");
                    } else {
                        toastAlertShow("提交失败");
                    }
                }
            },
            error: function (err) {
                console.log("err", err);
                loadAlertHide();
                window.location.href = "../../Util/html/error.html";
            }
        })
    });
}
/**
 * 选择上传图片
 * @param index 选取上传下标
 */
function showImg(index) {
    index = index;
    var file;
    if(index == 1){
        file = $("#file1").get(0).files[0]
    } else {
        file = $("#file2").get(0).files[0]
    }
    var render = new FileReader();
    render.readAsDataURL(file);
    render.onload = function (e) {
        // console.log(e);
        // console.log(e.target.result);
        if (index == 1) {
            uploadPicture(1, e.target.result, 8, "A", img1);  //名片正面
        } else if (index == 2) {
            uploadPicture(2, e.target.result, 8, "B", img2); //名片反面
        }
    }
}
/**
 * 图片上传
 * @param index 图片id
 * @param base64 图片base64
 * @param type 上传的类型12345678 身份证，驾驶证，行驶证，人车合照，上岗证，从业资格证, 8 名片
 * @param extra 正反面 A、B
 * @param last_file 上次回传的文件名（服务器上的路径
 */
function uploadPicture(index, base64, type, extra, last_file) {
    loadAlertShow("正在上传...");
    canvasDataURL(base64, function callback(data) {
        let params = {
            // user_id: $.user_id,
            user_id: param.user_id,
            base64: data,
            type: type,
            extra: extra,
            last_file: last_file
        };
        console.log(params);
        $.ajax({
            type: 'POST',
            url: $.uploadPicture,
            data: params,
            success: function (res) {
                //console.log(res);
                loadAlertHide();
                if(res && res.status == 1){
                    let path = res.data;
                    switch (index) {
                        case 1:
                            img1 = path;
                            $("#img1").attr("src", data);
                            break;
                        case 2:
                            img2 = path;
                            $("#img2").attr("src", data);
                            break;
                        default: break;
                    }
                } else {
                    toastAlertShow(res.msg);
                }
            },
            error: function (err) {
                console.log(err);
                loadAlertHide();
                window.location.href = "../../Util/html/error.html";
            }
        });
    });
}
/**
 * 压缩图片
 * @param path
 */
function canvasDataURL(path, callback){
    var img = new Image();
    img.src = path;
    img.onload = function(){
        var that = this;
        // 默认按比例压缩
        var w = that.width,
            h = that.height,
            scale = w / h;
        h =  (w / scale);
        var quality = 0.1;  // 默认图片质量为0.7
        //生成canvas
        var canvas = document.createElement('canvas');
        var ctx = canvas.getContext('2d');
        // 创建属性节点
        var anw = document.createAttribute("width");
        anw.nodeValue = w;
        var anh = document.createAttribute("height");
        anh.nodeValue = h;
        canvas.setAttributeNode(anw);
        canvas.setAttributeNode(anh);
        ctx.drawImage(that, 0, 0, w, h);
        console.log(quality);
        // quality值越小，所绘制出的图像越模糊
        let base64 = canvas.toDataURL('image/jpeg', quality);
        callback(base64);
    };
}
