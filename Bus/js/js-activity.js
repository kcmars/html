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

//调用原生app关闭网页
function closeWebview() {
    localStorage.clear();
    if (isAndroid) {
        AiYunInterface.close()
    } else {
        WebViewJavascriptBridge.callHandler('close', null, function(response) {})
    }
}

//刷新原生页面
function refreshApp() {
    if (isAndroid) {
        AiYunInterface.refreshApp()
    } else {
        WebViewJavascriptBridge.callHandler('refreshApp', null, function(response) {})
    }
}

//获取原生传递过来的参数
function getParams(cb) {
    console.log("getParams") ;
    WebViewJavascriptBridge.callHandler('getParams', null,function(response)  {
        param = response ;
        console.log(param) ;
        cb() ;
    });
}

//调用原生app打开网页
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

//调用原生app打开activity
function openActivity(identity, type, params) {
    console.log("openActivity2");
    if (isAndroid) {
        AiYunInterface.toActivity(identity, type, params) ;
    } else {
        WebViewJavascriptBridge.callHandler('toActivity', {"identity": identity, "type":type, "params":params}, function(response) {})
    }
}
// //说明
// identity : 身份： passenger 乘客， bus 大巴车
// type ： 类型： tel 打电话， location 查看位置
// params：参数： 数组，根据前面两个参数判断：
// 1：identity=passenger 时，params=[{}];内容是大巴车班次信息
// 2：identity=bus 时，params=[{}，{}]；第一个内容是乘客信息，第二个是大巴车班次信息
// 3：根据type的值判断跳转到对应的页面

//调用原生app打开扫描二维码activity
function openScanningActivity() {
    if (isAndroid) {
        AiYunInterface.OpenScanning() ;
    } else {
        WebViewJavascriptBridge.callHandler('openScanning', {}, function(response) {})
    }
}

//调用原生app打开支付activity
function openPayActivity(url, order_no) {
    console.log("params");
    if (isAndroid) {
        console.log("openActivity3" + order_no);
        AiYunInterface.openPayActivity(url, order_no) ;
    } else {
        WebViewJavascriptBridge.callHandler('openPayActivity', {"url":url,"order_no":order_no}, function(response) {})
    }
}

//执行函数
function getRequest(callback) {
    console.log("isiOS") ;
    if (isAndroid) {
        console.log("isAndroid") ;
        param = JSON.parse(AiYunInterface.getParams()) ;
        callback() ;
    } else {
        // param.order_no = '15179007065545730541' ;
        // getParams(callback);
        console.log("getParams") ;
        WebViewJavascriptBridge.callHandler('getParams', null,function(response)  {
            param = response ;
            console.log(param) ;
            callback();
        });
    }
}
