(function($) {
    $.pay = {
        alert: function(moneyText, callback) {
            if( moneyText == null ) moneyText = 'Pay';
            $.pay._show(moneyText, 'alert', function(result, payType) {
                if( callback ) callback(result, payType);
            });
        },

        _show: function(moneyText, showType, callback) {
            var _html = "";
            _html += '<div id="mp-box"></div><div id="mp-con">';
            _html +=  '<div id="mp-box-title"><img id="mp-img-close" src="../img/icon_gray_close.png" />';
            _html += '<span id="title">支付</span><span id="title-right"></span></div>';
            _html += '<div id="mp-alert-pay-type"><div id="mp-alert-pay-type-row1">';
            _html += '<img id="img1" src="../img/wechat_pay.png" /><span id="span1">微信支付</span><input id="input1" name="radio" type="radio" checked="checked" value="weixin"/>';
            _html += '</div><div id="mp-alert-pay-type-row2"><img id="img2" src="../img/alipay.png" />';
            _html += '<span id="span2">支付宝支付</span><input id="input2" name="radio" type="radio" value="alipay"/>';
            _html += '</div></div>';
            _html += '<div id="mp-alert-pay-btn">' + moneyText + '</div></div>';

            //必须先将_html添加到body，再设置Css样式
            $("body").append(_html);
            GenerateCss();
            switch( showType ) {
                case 'alert':
                    $("#mp-alert-pay-btn").click( function() {
                        var payType = $("input[name='radio']:checked").val();
                        callback(true, payType);
                    });
                    $("#mp-alert-pay-btn").focus().keypress( function(e) {
                        if( e.keyCode == 13 || e.keyCode == 27 ) $("#mp-alert-pay-btn").trigger('click');
                    });
                    $("#mp-img-close").click( function() {
                        callback(false);
                    });
                    break;
            }
        },
        _hide: function() {
            $("#mp-box, #mp-con").remove();
        }
    };

    // 一个按钮的弹窗样式
    payShowAlert = function(moneyText, callback) {
        $.pay.alert(moneyText, callback);
    };

    // 一个按钮的弹窗样式
    payHideAlert = function() {
        $.pay._hide();
    };

    //生成Css
    var GenerateCss = function () {
        $("#mp-box").css({
            width: '100%', height: '100%', zIndex: '666', position: 'fixed', backgroundColor: '#000000',
            top: '0', left: '0', opacity: '0.1'
        });
        $("#mp-con").css({
            display: 'flex', flexDirection: 'column', zIndex: '667', width: '100%', backgroundColor: '#ffffff',
            bottom: '0', left: '0', position: 'fixed', height: 'auto', justifyContent: 'flex-end'
        });
        $("#mp-box-title").css({
            display: 'flex', flexDirection: 'row', fontSize: '0.40rem', padding: '0.20rem 0.25rem',
            alignItems: 'center', borderBottom: '0.02rem solid #C5C5C5'
        });
        $("#mp-box-title>img").css({
            width: '0.50rem', height: '0.50rem'
        });
        $("#title").css({
            color: '#1fb1ff', flex: '1', textAlign: 'center'
        });
        $("#title-right").css({
            width: '0.50rem', height: '0.50rem'
        });
        $("#mp-alert-pay-type").css({
            display: 'flex', flexDirection: 'column', margin: '0.25rem 0'
        });
        $("#mp-alert-pay-type-row1, #mp-alert-pay-type-row2").css({
            display: 'flex', flexDirection: 'row', padding: '0.25rem 0.45rem', fontSize: '0.35rem',
            alignItems: 'center'
        });
        $("#img1, #img2").css({
            width: '0.45rem', height: '0.45rem'
        });
        $("#span1, #span2").css({
            flex: '1', margin: '0 0.2rem'
        });
        $("#input1, #input2").css({
            width: '0.45rem', height: '0.45rem', color: '#1fb1ff'
        });
        $("#mp-alert-pay-btn").css({
            backgroundColor: '#1fb1ff', fontSize: '0.47rem', color: 'white',
            textAlign: 'center', padding: '0.35rem 0'
        });
    }
})(jQuery);
