/**
 * Created by keyC on 2017/7/21.
 */
!(function(win, doc) {
    function setFontSize() {
        // 获取window 宽度
        // zepto实现 $(window).width()就是这么干的
        var winWidth = window.innerWidth || 375;
        if (winWidth < 800) {
            doc.documentElement.style.fontSize = (winWidth / 750) * 100 + 'px';
        } else {
            doc.documentElement.style.fontSize = '44px';
        }
    }
    // win.onresize = function(){
    //     setFontSize();
    // };
    var evt = 'onorientationchange' in win ? 'orientationchange' : 'resize';
    var timer = null;
    win.addEventListener(evt, function() {
        clearTimeout(timer);
        timer = setTimeout(setFontSize, 300);
    }, false);
    win.addEventListener("pageshow", function(e) {
        if (e.persisted) {
            clearTimeout(timer);
            timer = setTimeout(setFontSize, 300);
        }
    }, false);
    //初始化
    setFontSize();
}(window, document));