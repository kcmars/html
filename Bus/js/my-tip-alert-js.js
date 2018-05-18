(function($) {
    $.tip = {
        alert: function(title, content, message, callback, btnText1) {
            if( title == null ) title = 'Alert';
            $.tip._show(title, content, message, null, 'alert', function(result) {
                if( callback ) callback(result);
            }, btnText1);
        },

        confirm: function(title, content, message, callback, btnText1, btnText2, bgColor) {
            if( title == null ) title = 'Confirm';
            $.tip._show(title, content, message, null, 'confirm', function(result) {
                if( callback ) callback(result);
            }, btnText1, btnText2, bgColor);
        },

        _show: function(title, content, msg, value, type, callback, btnText1, btnText2, bgColor) {
            var _html = "";
            _html += '<div id="mt_box"></div><div id="mt_alert"><div id="mt_con"><span id="mt_tit">' + title + '</span>';
            _html += '<div id="mt_center"><div id="mt_content">' + content + '</div>';
            _html += '<div id="mt_msg">' + msg + '</div></div><div id="mt_btn_box">';
            if (type == "alert") {
                _html += '<div id="mt_btn_ok">'+ btnText1 +'</div>';
            }
            if (type == "confirm") {
                _html += '<div id="mt_btn_ok">'+ btnText1 +'</div>';
                _html += '<div id="mt_btn_no">'+ btnText2 +'</div>';
            }
            _html += '</div></div><img id="mt_close_img" src="../img/icon_white_close.png"></div>';

            //必须先将_html添加到body，再设置Css样式
            $("body").append(_html);
            GenerateCss(type, bgColor);
            switch( type ) {
                case 'alert':
                    $("#mt_btn_ok").click( function() {
                        $.tip._hide();
                        callback(true);
                    });
                    $("#mt_btn_ok").focus().keypress( function(e) {
                        if( e.keyCode == 13 || e.keyCode == 27 ) $("#mt_btn_ok").trigger('click');
                    });
                    $("#mt_close_img").click( function() {
                        $.tip._hide();
                        callback(false);
                    });
                    break;
                case 'confirm':
                    $("#mt_btn_ok").click( function() {
                        $.tip._hide();
                        if( callback ) callback(true);
                    });
                    $("#mt_btn_no").click( function() {
                        $.tip._hide();
                        if( callback ) callback(false);
                    });
                    $("#mt_close_img").click( function() {
                        $.tip._hide();
                        if( callback ) callback(false);
                    });
                    $("#mt_btn_no").focus();
                    $("#mt_btn_ok, #mt_btn_no").keypress( function(e) {
                        if( e.keyCode == 13 ) $("#mt_btn_ok").trigger('click');
                        if( e.keyCode == 27 ) $("#mt_btn_no").trigger('click');
                    });
                    break;
            }
        },
        _hide: function() {
            $("#mt_box, #mt_alert, #mt_con").remove();
        }
    };

    // 一个按钮的弹窗样式
    oneBtnAlert = function(title, content, message, callback, btnText1) {
        $.tip.alert(title, content, message, callback, btnText1);
    };

    //两个按钮的弹窗样式
    towBtnAlert = function(title, content, message, callback, btnText1, btnText2, bgColor) {
        $.tip.confirm(title, content, message, callback, btnText1, btnText2, bgColor);
    };

    //生成Css
    var GenerateCss = function (type, bgColor) {

        $("#mt_box").css({ width: '100%', height: '100%', zIndex: '888', position: 'fixed',
            filter: 'Alpha(opacity=60)', backgroundColor: 'black', top: '0', left: '0', opacity: '0.6'
        });

        $("#mt_alert").css({
            width: '100%', height: '100%', zIndex: '889', position: 'fixed', top: '0', left: '0', display: 'flex', flexDirection:'column',
            justifyContent: 'center', alignItems:'center', padding:'0.20rem'
        });

        $("#mt_con").css({ display: 'flex', flexDirection: 'column', zIndex: '889', width: '90%',
            backgroundColor: 'White', borderRadius: '0.23rem'
        });

        $("#mt_tit").css({ textAlign:'center', fontSize: '0.56rem', color: '#1fb1ff', padding: '0.23rem 0.35rem',
            backgroundColor: '#fff', borderRadius: '0.23rem 0.23rem 0 0',
            borderBottom: '0.02rem solid #ccc'
        });

        $("#mt_center").css({ overflow: 'scroll'
        });

        $("#mt_msg").css({
            padding: '0.20rem 0.35rem', fontSize: '0.28rem', color:'#66676f', lineHeight: '0.35rem'
        });

        $("#mt_ico").css({ display: 'block', position: 'absolute', right: '0.23rem', top: '0.21rem',
            border: '0.02rem solid Gray', width: '0.42rem', height: '0.42rem', textAlign: 'center',
            lineHeight: '0.37rem', cursor: 'pointer', borderRadius: '0.28rem', fontFamily: '微软雅黑'
        });

        $("#mt_btn_box").css({ display: 'flex', flexDirection:'row', margin: '0', textAlign: 'center', borderRadius: '0 0 0.23rem 0.23rem' });
        if (type=="alert") {
            $("#mt_btn_ok, #mt_btn_no").css({ display: 'flex', alignItems: 'center', justifyContent: 'center',
                width: '100%', padding: '0.38rem 0', color: 'white', border: 'none', fontSize: '0.47rem', borderRadius: '0 0 0.23rem 0.23rem'});
            $("#mt_btn_ok").css({ backgroundColor: '#1fb1ff' });
        } else {
            $("#mt_btn_ok, #mt_btn_no").css({ display: 'flex', alignItems: 'center', justifyContent: 'center',
                width: '100%', padding: '0.38rem 0', color: 'white', border: 'none', fontSize: '0.37rem' });
            $("#mt_btn_ok").css({ backgroundColor: '#a6a3a3', borderRadius: '0 0 0 0.23rem' });
            $("#mt_btn_no").css({ backgroundColor: bgColor ? bgColor : '#1fb1ff', borderRadius: '0 0 0.23rem 0' });
        }

        $("#mt_close_img").css({ width:'0.70rem', height:'0.70rem', marginTop:'0.60rem'});

        //右上角关闭按钮hover样式
        $("#mt_ico").hover(function () {
            $(this).css({ backgroundColor: 'Red', color: 'White' });
        }, function () {
            $(this).css({ backgroundColor: '#DDD', color: 'black' });
        });

        // var _widht = document.documentElement.clientWidth; //屏幕宽
        // var _height = document.documentElement.clientHeight; //屏幕高
        //
        // var boxWidth = $("#mt_con").width();
        // var boxHeight = $("#mt_con").height();
        //
        // //让提示框居中
        // $("#mt_con").css({ top: (_height - boxHeight) / 2 + "px", left: (_widht - boxWidth) / 2 + "px" });

    }
})(jQuery);
