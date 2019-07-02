/**
 * Created by keyC on 2017/7/21.
 */
var telFlag = false;
var filechooser = document.getElementById('inputImg');
var html = '';

$(function () {
    // 电话号码输入框实时验证
    $('#inputTel').bind('blur',function () {
        var reg = /^1(3|4|5|7|8)\d{9}$/;
        var num = $(this).val().trim();
        if(num){
            if(reg.test(num)){
                $(this).siblings('.status').css('display','inline-block');
                $(this).siblings('.tip').css('display','none');
                telFlag = true;
            }else {
                telFlag = false;
                $(this).siblings('.tip').css('display','block');
                $(this).siblings('.status').css('display','none');
            }
        }else {
            telFlag = false;
            $(this).siblings('.status').css('display','none');
            $(this).siblings('.tip').css('display','none');
        }

    })
});

/**
 * 建议输入框字数显示，达到上限后禁止输入
 * @param obj
 * @param maxlen
 */
function checkLength(obj,maxlen) {
    var  getMyLen,MAXLEN;
    MAXLEN = maxlen;
    getMyLen = obj.value.length;
    if(getMyLen>MAXLEN) {
        $(obj).siblings('.tipText').text('最多可输入'+MAXLEN+'字');
        obj.value = obj.value.substr(0,MAXLEN);
    } else {
        $(obj).siblings('.tipText').text(''+getMyLen+'\/'+MAXLEN+'字');
    }
}

/**
 * 操作DOM删除图片
 * @param obj
 */
function uploadDelete(obj) {
    var deletePar = obj.parentNode;
    document.getElementById('previewBox').removeChild(deletePar);
    html = document.getElementById('previewBox').innerHTML;
}

/**
 * 获取图片,展示图片
 * @type {Element}
 */
filechooser.onchange = function() {
    //var imgLength = document.getElementById('previewBox').childNodes.length;
    var imgLength = $('#previewBox>.uploadList').length;
    console.log(imgLength)
    if(imgLength > 3){
        layer.open({
            content: '最多可上传4张图片',
            btn: '我知道了'
        });
        return;
    }
    var file = this.files[0];
    // 接受 jpeg, jpg, png, gif 类型的图片
    if (!/\/(?:jpeg|jpg|png|gif)/i.test(file.type)) return;
    var reader = new FileReader();
    reader.onload = function() {
        var result = this.result;
        var img = new Image();
        img.onload = function() {
            var compressedDataUrl = compress(img, file.type);
            toPreviewer(file,compressedDataUrl);
            img = null;
        };
        img.src = result;
    };
    reader.readAsDataURL(file);
};
function toPreviewer(file,dataUrl) {
    html = html + '<div class="uploadList">' +
        '<img src="' + dataUrl + '" class="uploadImg" /><br/>'+
        '<span class="imgName">' + file.name + '</span><br/>' +
        '<a onclick="uploadDelete(this)" class="uploadDelete" title="删除">删除</a>'+
        '</div>';
    $('#previewBox').html(html);
    $('#inputImg').val('');
}

/**
 * 压缩图片，返回图片的base64的url
 * @param img
 * @param fileType
 * @returns {string}
 */
function compress(img, fileType) {
    var canvas = document.createElement("canvas");
    var ctx = canvas.getContext('2d');
    const MAX_HEIGHT = 200;
    // 宽度等比例缩放 *=
    if (img.height > img.width && img.height > MAX_HEIGHT) {
        img.width *= MAX_HEIGHT / img.height;
        img.height = MAX_HEIGHT;
    }else if(img.width > img.height && img.width > MAX_HEIGHT){
        img.height *= MAX_HEIGHT / img.width;
        img.width = MAX_HEIGHT;
    }else if(img.width == img.height && img.width > MAX_HEIGHT){
        img.width = MAX_HEIGHT;
        img.height = MAX_HEIGHT;
    }

    canvas.width = img.width;
    canvas.height = img.height;
    ctx.fillStyle = "#fff";
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    ctx.drawImage(img, 0, 0, img.width, img.height);

    // 压缩
    var base64data = canvas.toDataURL();
    canvas = ctx = null;
    return base64data;
}

/**
 * 点击提交按钮事件
 */
$('.submitBtn').bind('click',function () {
    var tel = $('#inputTel').val();
    var sex = $('input:radio[name="sex"]:checked').val();
    var year = $('#selectYear').val();
    var matterText = $('#textareaMatter').val();
    var matterImg = [], imgUrl = null;
    if($('#previewBox .uploadList').length > 0 ){
        $('#previewBox .uploadList').each(function () {
            imgUrl = $(this).children('.uploadImg').attr('src');
            matterImg.push(imgUrl);
        })
    }
    var opinion = $('#opinion').val();
    // 判断手机号
    if(!telFlag){
        layer.open({
            content: '请输入正确的手机号码',
            btn: '我知道了'
        });
        return;
    }
    // 问题输入框、图片框、意见框至少有一个不为空才能提交
    if(!matterText && matterImg.length ==0 && !opinion){
        layer.open({
            content: '请输入需要反馈的问题或者建议',
            btn: '我知道了'
        });
        return;
    }else {
        $.post("/Wap/Feedback/addFeedback.html",
            {
                tel: tel,
                sex: sex,
                year: year,
                matterText: matterText,
                matterImg: matterImg,
                opinion: opinion
            },
            function (data) {
                if (data.status == 1) {
                    layer.open({
                        content: '保存成功，谢谢您提出的意见',
                        btn: '确认',
                        end:function (){
                            location.reload() ;
                        }
                    })
                } else {
                    layer.open({
                        content: data.msg,
                        btn: '确认'
                    })
                }
            }, 'json');
    }
});



