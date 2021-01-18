/**
 * Created by lovo_bdk on 15-12-17.
 */
!(function(win, doc) {
    function setFontSize() {
        // 获取window 宽度
        // zepto实现 $(window).width()就是这么干的
        let winWidth = window.innerWidth || 375;
        console.log("当前移动设备屏幕宽度："+winWidth);
        doc.documentElement.style.fontSize = (winWidth / 750) * 100 + 'px';
    }
    // win.onresize = function(){
    //     setFontSize();
    // };
    let evt = 'onorientationchange' in win ? 'orientationchange' : 'resize';
    let timer = null;
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