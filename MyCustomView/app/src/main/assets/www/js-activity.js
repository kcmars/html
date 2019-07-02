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
        JSInterface.close()
    } else {
        WebViewJavascriptBridge.callHandler('close', null, function(response) {})
    }
}

//刷新原生页面
function refreshApp() {
    if (isAndroid) {
        JSInterface.refreshApp()
    } else {
        WebViewJavascriptBridge.callHandler('refreshApp', null, function(response) {})
    }
}

//获取原生传递过来的参数
function getParams(cb) {
    WebViewJavascriptBridge.callHandler('getParams', null,function(response)  {
        param = response ;
        console.log(param) ;
        cb() ;
    });
}

//调用原生app打开网页
function openNativeBrowser(url,params) {
    if (typeof (WebViewJavascriptBridge) === 'undefined' && typeof(JSInterface) === 'undefined') {
        window.location = url;
    } else {
        if (isAndroid) {
            // var param = "{'order_no':'" + param.order_no + "'}"
            JSInterface.openBrowserView(url,JSON.stringify(params)) ;
        } else {
            WebViewJavascriptBridge.callHandler('openBrowserView', {"url":url,"params":params}, function(response) {})
        }
    }
}

//调用原生app打开activity
function openActivity(identity, type, params) {
    if (isAndroid) {
        JSInterface.toActivity(identity, type, params) ;
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
        JSInterface.OpenScanning() ;
    } else {
        WebViewJavascriptBridge.callHandler('openScanning', {}, function(response) {})
    }
}

//调用原生app打开支付activity
function openPayActivity(url, order_no) {
    if (isAndroid) {
        JSInterface.openPayActivity(url, order_no) ;
    } else {
        WebViewJavascriptBridge.callHandler('openPayActivity', {"url":url,"order_no":order_no}, function(response) {})
    }
}

//调用原生分享功能
function openShareTo(index, data) {
    if (isAndroid) {
        JSInterface.shareTo(index, data) ;
    } else {
        WebViewJavascriptBridge.callHandler('shareTo', {"index": index,"shareConfig": data}, function(response) {})
    }
}

//传递参数到原生
function paramToActivity(data) {
    if (isAndroid) {
        JSInterface.paramTo(data) ;
    } else {
        WebViewJavascriptBridge.callHandler('paramTo', data, function(response) {})
    }
}

//执行函数
function getRequest(callback) {
    if (isAndroid) {
        param = JSON.parse(JSInterface.getParams()) ;
        callback() ;
    } else {
        // param.order_no = '15179007065545730541' ;
        // getParams(callback);
        WebViewJavascriptBridge.callHandler('getParams', null,function(response)  {
            param = response ;
            console.log(param) ;
            callback();
        });
    }
}
