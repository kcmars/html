(function($) {
    $.toast = {
        alert: function(msg) {
            $.toast._show(msg);
        },
        _show: function(msg) {
            let _html = "";
            _html += '<div id="mt_box"></div><div id="mt_alert"><div id="mt_content">';
            _html += '<span id="mt_msg">' + msg + '</span>';
            _html += '</div></div></div>';

            //必须先将_html添加到body，再设置Css样式
            $("body").append(_html);
            GenerateCss();
        },
        _hide: function() {
            $("#mt_box, #mt_alert").remove();
        }
    };

    // 显示加载中提示的弹窗样式
    $.toastAlertShow = function(title, time) {
        $.toast.alert(title);
        setTimeout(function () {
            $.toast._hide();
        }, time ? time : 1000);
    };

    //生成Css
    let GenerateCss = function () {

        $("#mt_box").css({ width: '100%', height: '100%', zIndex: '99999', position: 'fixed',
            filter: 'Alpha(opacity=10)', backgroundColor: '#66676f', top: '0', left: '0', opacity: '0.0'
        });

        $("#mt_alert").css({
            width: '80%', height: '100%', zIndex: '999999', position: 'fixed', top: '0', left: '0',
            display: 'flex', flexDirection:'column', justifyContent: 'flex-end', alignItems:'center', marginLeft: '10%'
        });

        $("#mt_content").css({
            zIndex: '999999', backgroundColor: '#333333', borderRadius: '5px', marginBottom: '50px',
            display: 'flex', flexDirection:'column', justifyContent: 'center', alignItems:'center'
        });

        $("#mt_msg").css({
            textAlign:'center', fontSize: '14px', color: '#fff', padding: '5px 10px', lineHeight: '20px'
        });
    }
})(jQuery);
