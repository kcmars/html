var param = new Object();
var u = navigator.userAgent;
var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
var WEBSITE = "http://aiyunbaoapp.a56999.com" ;
// var WEBSITE = "http://192.168.1.88" ;
function setupWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge) { return callback(WebViewJavascriptBridge); }
    if (window.WVJBCallbacks) { return window.WVJBCallbacks.push(callback); }
    window.WVJBCallbacks = [callback];
    var WVJBIframe = document.createElement('iframe');
    WVJBIframe.style.display = 'none';
    WVJBIframe.src = 'wvjbscheme://__BRIDGE_LOADED__';
    document.documentElement.appendChild(WVJBIframe);
    setTimeout(function() { document.documentElement.removeChild(WVJBIframe) }, 0)
}
setupWebViewJavascriptBridge(function(bridge) {}) ;

function closeWebview() {
    if (isAndroid) {
        AiYunInterface.close()
    } else {
        WebViewJavascriptBridge.callHandler('close', null, function(response) {})
    }
}


function getParams(cb) {
    WebViewJavascriptBridge.callHandler('getParams', null,function(response)  {
        param = response ;
        console.log(param) ;
        cb() ;
    });
}

function openNativeBrowser(url,params) {
    if (typeof (WebViewJavascriptBridge) === 'undefined' && typeof(AiYunInterface) === 'undefined') {
        window.location = url;
    } else {
        if (isAndroid) {
            // var param = "{'order_no':'" + param.order_no + "'}"
            AiYunInterface.openBrowserView(url,JSON.stringify(params)) ;
        } else {
            WebViewJavascriptBridge.callHandler('openBrowserView', {"url":url,"params":params}, function(response) {})
        }
    }
}
function getRequest(callback) {
    if (isAndroid) {
        param = JSON.parse(AiYunInterface.getParams()) ;
        callback() ;
    } else {
        // param.order_no = '15179007065545730541' ;
        callback() ;
        getParams(callback);
    }
}
