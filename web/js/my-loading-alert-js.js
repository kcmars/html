
(function($) {
    $.loading = {
        alert: function(msg) {
            $.loading._show(msg);
        },
        _show: function(msg) {
            var _html = "";
            _html += '<div id="ml_box"></div><div id="ml_alert"><div id="ml_content">';
            _html += '<img id="ml_img" src="../img/load.gif"/>';
            _html += '<span id="ml_msg">' + msg + '</span>';
            _html += '</div></div></div>';

            //必须先将_html添加到body，再设置Css样式
            $("body").append(_html);
            GenerateCss();
        },
        _hide: function() {
            $("#ml_box, #ml_alert").remove();
        }
    };

    // 显示加载中提示的弹窗样式
    loadAlertShow = function(title) {
        $.loading.alert(title);
    };

     // 关闭
    loadAlertHide = function() {
        $.loading._hide();
    };

    //生成Css
    var GenerateCss = function () {

        $("#ml_box").css({ width: '100%', height: '100%', zIndex: '99999', position: 'fixed',
            filter: 'Alpha(opacity=60)', backgroundColor: '#66676f', top: '0', left: '0', opacity: '0.6'
        });

        $("#ml_alert").css({
            width: '100%', height: '100%', zIndex: '999999', position: 'fixed', top: '0', left: '0',
            display: 'flex', flexDirection:'column', justifyContent: 'center', alignItems:'center'
        });

        $("#ml_content").css({
            width: '2.11rem', height: '2.11rem', zIndex: '999999', backgroundColor: '#66676f', borderRadius: '0.23rem',
            display: 'flex', flexDirection:'column', justifyContent: 'center', alignItems:'center'
        });

        $("#ml_img").css({ width:'0.70rem', height:'0.70rem'});

        $("#ml_msg").css({ textAlign:'center', fontSize: '0.33rem', color: '#fff', marginTop:'0.23rem'});
    }
})(jQuery);
