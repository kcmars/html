/*
 * APICloud JavaScript Library
 * Copyright (c) 2014 apicloud.com
 */
(function(window) {

	var u = {};
	var isAndroid = (/android/gi).test(navigator.appVersion);
	var uzStorage = function() {
		var ls = window.localStorage;
		if (isAndroid) {
			ls = os.localStorage();
		}
		return ls;
	};
	function parseArguments(url, data, fnSuc, dataType) {
		if ( typeof (data) == 'function') {
			dataType = fnSuc;
			fnSuc = data;
			data = undefined;
		}
		if ( typeof (fnSuc) != 'function') {
			dataType = fnSuc;
			fnSuc = undefined;
		}
		return {
			url : url,
			data : data,
			fnSuc : fnSuc,
			dataType : dataType
		};
	}


	u.trim = function(str) {
		if (String.prototype.trim) {
			return str == null ? "" : String.prototype.trim.call(str);
		} else {
			return str.replace(/(^\s*)|(\s*$)/g, "");
		}
	};
	u.trimAll = function(str) {
		return str.replace(/\s*/g, '');
	};
	u.isElement = function(obj) {
		return !!(obj && obj.nodeType == 1);
	};
	u.isArray = function(obj) {
		if (Array.isArray) {
			return Array.isArray(obj);
		} else {
			return obj instanceof Array;
		}
	};
	u.isEmptyObject = function(obj) {
		if (JSON.stringify(obj) === '{}') {
			return true;
		}
		return false;
	};
	u.addEvt = function(el, name, fn, useCapture) {
		if (!u.isElement(el)) {
			console.warn('$api.addEvt Function need el param, el param must be DOM Element');
			return;
		}
		useCapture = useCapture || false;
		if (el.addEventListener) {
			el.addEventListener(name, fn, useCapture);
		}
	};
	u.rmEvt = function(el, name, fn, useCapture) {
		if (!u.isElement(el)) {
			console.warn('$api.rmEvt Function need el param, el param must be DOM Element');
			return;
		}
		useCapture = useCapture || false;
		if (el.removeEventListener) {
			el.removeEventListener(name, fn, useCapture);
		}
	};
	u.one = function(el, name, fn, useCapture) {
		if (!u.isElement(el)) {
			console.warn('$api.one Function need el param, el param must be DOM Element');
			return;
		}
		useCapture = useCapture || false;
		var that = this;
		var cb = function() {
			fn && fn();
			that.rmEvt(el, name, cb, useCapture);
		};
		that.addEvt(el, name, cb, useCapture);
	};
	u.dom = function(el, selector) {
		if (arguments.length === 1 && typeof arguments[0] == 'string') {
			if (document.querySelector) {
				return document.querySelector(arguments[0]);
			}
		} else if (arguments.length === 2) {
			if (el.querySelector) {
				return el.querySelector(selector);
			}
		}
	};
	u.domAll = function(el, selector) {
		if (arguments.length === 1 && typeof arguments[0] == 'string') {
			if (document.querySelectorAll) {
				return document.querySelectorAll(arguments[0]);
			}
		} else if (arguments.length === 2) {
			if (el.querySelectorAll) {
				return el.querySelectorAll(selector);
			}
		}
	};
	u.byId = function(id) {
		return document.getElementById(id);
	};
	u.first = function(el, selector) {
		if (arguments.length === 1) {
			if (!u.isElement(el)) {
				console.warn('$api.first Function need el param, el param must be DOM Element');
				return;
			}
			return el.children[0];
		}
		if (arguments.length === 2) {
			return this.dom(el, selector + ':first-child');
		}
	};
	u.last = function(el, selector) {
		if (arguments.length === 1) {
			if (!u.isElement(el)) {
				console.warn('$api.last Function need el param, el param must be DOM Element');
				return;
			}
			var children = el.children;
			return children[children.length - 1];
		}
		if (arguments.length === 2) {
			return this.dom(el, selector + ':last-child');
		}
	};
	u.eq = function(el, index) {
		return this.dom(el, ':nth-child(' + index + ')');
	};
	u.not = function(el, selector) {
		return this.domAll(el, ':not(' + selector + ')');
	};
	u.prev = function(el) {
		if (!u.isElement(el)) {
			console.warn('$api.prev Function need el param, el param must be DOM Element');
			return;
		}
		var node = el.previousSibling;
		if (node.nodeType && node.nodeType === 3) {
			node = node.previousSibling;
			return node;
		}
	};
	u.next = function(el) {
		if (!u.isElement(el)) {
			console.warn('$api.next Function need el param, el param must be DOM Element');
			return;
		}
		var node = el.nextSibling;
		if (node.nodeType && node.nodeType === 3) {
			node = node.nextSibling;
			return node;
		}
	};
	u.closest = function(el, selector) {
		if (!u.isElement(el)) {
			console.warn('$api.closest Function need el param, el param must be DOM Element');
			return;
		}
		var doms, targetDom;
		var isSame = function(doms, el) {
			var i = 0, len = doms.length;
			for (i; i < len; i++) {
				if (doms[i].isEqualNode(el)) {
					return doms[i];
				}
			}
			return false;
		};
		var traversal = function(el, selector) {
			doms = u.domAll(el.parentNode, selector);
			targetDom = isSame(doms, el);
			while (!targetDom) {
				el = el.parentNode;
				if (el != null && el.nodeType == el.DOCUMENT_NODE) {
					return false;
				}
				traversal(el, selector);
			}

			return targetDom;
		};

		return traversal(el, selector);
	};
	u.contains = function(parent, el) {
		var mark = false;
		if (el === parent) {
			mark = true;
			return mark;
		} else {
			do {
				el = el.parentNode;
				if (el === parent) {
					mark = true;
					return mark;
				}
			} while(el === document.body || el === document.documentElement);

			return mark;
		}

	};
	u.remove = function(el) {
		if (el && el.parentNode) {
			el.parentNode.removeChild(el);
		}
	};
	u.attr = function(el, name, value) {
		if (!u.isElement(el)) {
			console.warn('$api.attr Function need el param, el param must be DOM Element');
			return;
		}
		if (arguments.length == 2) {
			return el.getAttribute(name);
		} else if (arguments.length == 3) {
			el.setAttribute(name, value);
			return el;
		}
	};
	u.removeAttr = function(el, name) {
		if (!u.isElement(el)) {
			console.warn('$api.removeAttr Function need el param, el param must be DOM Element');
			return;
		}
		if (arguments.length === 2) {
			el.removeAttribute(name);
		}
	};
	u.hasCls = function(el, cls) {
		if (!u.isElement(el)) {
			console.warn('$api.hasCls Function need el param, el param must be DOM Element');
			return;
		}
		if (el.className.indexOf(cls) > -1) {
			return true;
		} else {
			return false;
		}
	};
	u.addCls = function(el, cls) {
		if (!u.isElement(el)) {
			console.warn('$api.addCls Function need el param, el param must be DOM Element');
			return;
		}
		if ('classList' in el) {
			el.classList.add(cls);
		} else {
			var preCls = el.className;
			var newCls = preCls + ' ' + cls;
			el.className = newCls;
		}
		return el;
	};
	u.removeCls = function(el, cls) {
		if (!u.isElement(el)) {
			console.warn('$api.removeCls Function need el param, el param must be DOM Element');
			return;
		}
		if ('classList' in el) {
			el.classList.remove(cls);
		} else {
			var preCls = el.className;
			var newCls = preCls.replace(cls, '');
			el.className = newCls;
		}
		return el;
	};
	u.toggleCls = function(el, cls) {
		if (!u.isElement(el)) {
			console.warn('$api.toggleCls Function need el param, el param must be DOM Element');
			return;
		}
		if ('classList' in el) {
			el.classList.toggle(cls);
		} else {
			if (u.hasCls(el, cls)) {
				u.removeCls(el, cls);
			} else {
				u.addCls(el, cls);
			}
		}
		return el;
	};
	u.val = function(el, val) {
		if (!u.isElement(el)) {
			console.warn('$api.val Function need el param, el param must be DOM Element');
			return;
		}
		if (arguments.length === 1) {
			switch(el.tagName) {
				case 'SELECT':
					var value = el.options[el.selectedIndex].value;
					return value;
					break;
				case 'INPUT':
					return el.value;
					break;
				case 'TEXTAREA':
					return el.value;
					break;
			}
		}
		if (arguments.length === 2) {
			switch(el.tagName) {
				case 'SELECT':
					el.options[el.selectedIndex].value = val;
					return el;
					break;
				case 'INPUT':
					el.value = val;
					return el;
					break;
				case 'TEXTAREA':
					el.value = val;
					return el;
					break;
			}
		}

	};
	u.prepend = function(el, html) {
		if (!u.isElement(el)) {
			console.warn('$api.prepend Function need el param, el param must be DOM Element');
			return;
		}
		el.insertAdjacentHTML('afterbegin', html);
		return el;
	};
	u.append = function(el, html) {
		if (!u.isElement(el)) {
			console.warn('$api.append Function need el param, el param must be DOM Element');
			return;
		}
		el.insertAdjacentHTML('beforeend', html);
		return el;
	};
	u.before = function(el, html) {
		if (!u.isElement(el)) {
			console.warn('$api.before Function need el param, el param must be DOM Element');
			return;
		}
		el.insertAdjacentHTML('beforebegin', html);
		return el;
	};
	u.after = function(el, html) {
		if (!u.isElement(el)) {
			console.warn('$api.after Function need el param, el param must be DOM Element');
			return;
		}
		el.insertAdjacentHTML('afterend', html);
		return el;
	};
	u.html = function(el, html) {
		if (!u.isElement(el)) {
			console.warn('$api.html Function need el param, el param must be DOM Element');
			return;
		}
		if (arguments.length === 1) {
			return el.innerHTML;
		} else if (arguments.length === 2) {
			el.innerHTML = html;
			return el;
		}
	};
	u.text = function(el, txt) {
		if (!u.isElement(el)) {
			console.warn('$api.text Function need el param, el param must be DOM Element');
			return;
		}
		if (arguments.length === 1) {
			return el.textContent;
		} else if (arguments.length === 2) {
			el.textContent = txt;
			return el;
		}
	};
	u.offset = function(el) {
		if (!u.isElement(el)) {
			console.warn('$api.offset Function need el param, el param must be DOM Element');
			return;
		}
		var sl = Math.max(document.documentElement.scrollLeft, document.body.scrollLeft);
		var st = Math.max(document.documentElement.scrollTop, document.body.scrollTop);

		var rect = el.getBoundingClientRect();
		return {
			l : rect.left + sl,
			t : rect.top + st,
			w : el.offsetWidth,
			h : el.offsetHeight
		};
	};
	u.css = function(el, css) {
		if (!u.isElement(el)) {
			console.warn('$api.css Function need el param, el param must be DOM Element');
			return;
		}
		if ( typeof css == 'string' && css.indexOf(':') > 0) {
			el.style && (el.style.cssText += ';' + css);
		}
	};
	u.cssVal = function(el, prop) {
		if (!u.isElement(el)) {
			console.warn('$api.cssVal Function need el param, el param must be DOM Element');
			return;
		}
		if (arguments.length === 2) {
			var computedStyle = window.getComputedStyle(el, null);
			return computedStyle.getPropertyValue(prop);
		}
	};
	u.jsonToStr = function(json) {
		if ( typeof json === 'object') {
			return JSON && JSON.stringify(json);
		}
	};
	u.strToJson = function(str) {
		if ( typeof str === 'string') {
			return JSON && JSON.parse(str);
		}
	};
	u.setStorage = function(key, value) {
		if (arguments.length === 2) {
			var v = value;
			if ( typeof v == 'object') {
				v = JSON.stringify(v);
				v = 'obj-' + v;
			} else {
				v = 'str-' + v;
			}
			var ls = uzStorage();
			if (ls) {
				ls.setItem(key, v);
			}
		}
	};
	u.getStorage = function(key) {
		var ls = uzStorage();
		if (ls) {
			var v = ls.getItem(key);
			if (!v) {
				return;
			}
			if (v.indexOf('obj-') === 0) {
				v = v.slice(4);
				return JSON.parse(v);
			} else if (v.indexOf('str-') === 0) {
				return v.slice(4);
			}
		}
	};
	u.rmStorage = function(key) {
		var ls = uzStorage();
		if (ls && key) {
			ls.removeItem(key);
		}
	};
	u.clearStorage = function() {
		var ls = uzStorage();
		if (ls) {
			ls.clear();
		}
	};

	/*by king*/
	u.fixIos7Bar = function(el) {
		if (!u.isElement(el)) {
			console.warn('$api.fixIos7Bar Function need el param, el param must be DOM Element');
			return;
		}
		var strDM = api.systemType;
		if (strDM == 'ios') {
			var strSV = api.systemVersion;
			var numSV = parseInt(strSV, 10);
			var fullScreen = api.fullScreen;
			var iOS7StatusBarAppearance = api.iOS7StatusBarAppearance;
			if (numSV >= 7 && !fullScreen && iOS7StatusBarAppearance) {
				el.style.paddingTop = '20px';
			}
		}
	};
	u.fixStatusBar = function(el) {
		if (!u.isElement(el)) {
			console.warn('$api.fixStatusBar Function need el param, el param must be DOM Element');
			return;
		}
		var sysType = api.systemType;
		if (sysType == 'ios') {
			u.fixIos7Bar(el);
		} else if (sysType == 'android') {
			var ver = api.systemVersion;
			ver = parseFloat(ver);
			if (ver >= 4.4) {
				el.style.paddingTop = '25px';
			}
		}
	};
	u.toast = function(title, text, time) {
		var opts = {};
		var show = function(opts, time) {
			api.showProgress(opts);
			setTimeout(function() {
				api.hideProgress();
			}, time);
		};
		if (arguments.length === 1) {
			var time = time || 500;
			if ( typeof title === 'number') {
				time = title;
			} else {
				opts.title = title + '';
			}
			show(opts, time);
		} else if (arguments.length === 2) {
			var time = time || 500;
			var text = text;
			if ( typeof text === "number") {
				var tmp = text;
				time = tmp;
				text = null;
			}
			if (title) {
				opts.title = title;
			}
			if (text) {
				opts.text = text;
			}
			show(opts, time);
		}
		if (title) {
			opts.title = title;
		}
		if (text) {
			opts.text = text;
		}
		time = time || 500;
		show(opts, time);
	};
	u.post = function(/*url, data, fnSuc, dataType*/) {
		var argsToJson = parseArguments.apply(null, arguments);
		var json = {};
		var fnSuc = argsToJson.fnSuc;
		argsToJson.url && (json.url = argsToJson.url);
		argsToJson.data && (json.data = argsToJson.data);
		if (argsToJson.dataType) {
			var type = argsToJson.dataType.toLowerCase();
			if (type == 'text' || type == 'json') {
				json.dataType = type;
			}
		} else {
			json.dataType = 'json';
		}
		json.method = 'post';
		json.timeout = 30;
		if (json.data) {
			var token = encryption($api.jsonToStr(json.data.values), 'qweasdzxc123');
			json.data.values['token'] = token;
		}
		api.ajax(json, function(ret, err) {
			api.hideProgress();
			if (ret) {
				if (ret.data != undefined) {
					ret.data = decrypt(ret.data, 'qweasdzxc123');
				}
			}
			fnSuc && fnSuc(ret, err);
		});
	};
	u.get = function(/*url,fnSuc,dataType*/) {
		var argsToJson = parseArguments.apply(null, arguments);
		var json = {};
		var fnSuc = argsToJson.fnSuc;
		argsToJson.url && (json.url = argsToJson.url);
		//argsToJson.data && (json.data = argsToJson.data);
		if (argsToJson.dataType) {
			var type = argsToJson.dataType.toLowerCase();
			if (type == 'text' || type == 'json') {
				json.dataType = type;
			}
		} else {
			json.dataType = 'text';
		}
		json.method = 'get';
		api.ajax(json, function(ret, err) {
			if (ret) {
				fnSuc && fnSuc(ret);
			} else {
				alert(err.msg);
			}
		});
	};
	/*end*/

	window.$api = u;

})(window);

var aiyunUrl = 'http://www.a56999.com/index.php/AiyunApi2';
//var aiyunUrl = 'http://192.168.0.111/aiyuntest/index.php/AiyunApi2';
var mainUrl = 'http://www.a56999.com';
//var mainUrl = 'http://192.168.0.111/aiyuntest';s

//关闭当前win
function closeWin() {
	api.closeWin();
}

//时间转时间戳
function transdate(endTime) {
	var date = new Date();
	date.setFullYear(endTime.substring(0, 4));
	date.setMonth(endTime.substring(5, 7) - 1);
	date.setDate(endTime.substring(8, 10));
	date.setHours(endTime.substring(11, 13));
	date.setMinutes(endTime.substring(14, 16));
	date.setSeconds(endTime.substring(17, 19));
	return Date.parse(date) / 1000;
}

//转换为具体时间
function getTimeDetail(nS) {
	//服务器时间
	var serviceTime = new Date(parseInt(nS) * 1000);
	var systemTime = new Date().getTime();
	var oneDay = 24 * 60 * 60 * 1000;
	var shijiancha = systemTime - serviceTime;
	var year = serviceTime.getFullYear();
	var month = (serviceTime.getMonth() + 1 < 10 ? '0' + (serviceTime.getMonth() + 1) : serviceTime.getMonth() + 1);
	var date = (serviceTime.getDate() < 10 ? '0' + (serviceTime.getDate()) : serviceTime.getDate());
	var hour = (serviceTime.getHours() < 10 ? '0' + (serviceTime.getHours()) : serviceTime.getHours());
	var minute = (serviceTime.getMinutes() < 10 ? '0' + (serviceTime.getMinutes()) : serviceTime.getMinutes());
	if (shijiancha < oneDay && shijiancha > 0) {
		return '今天' + hour + ":" + minute;
	} else if (shijiancha > oneDay && shijiancha < 2 * oneDay) {
		return '昨天' + hour + ":" + minute;
	} else if (shijiancha > 2 * oneDay && shijiancha < 3 * oneDay) {
		return '前天' + hour + ":" + minute;
	} else {
		return year + "-" + month + "-" + date;
	}

}

//时间戳转时间
function getTime(nS) {
	var now = new Date(parseInt(nS) * 1000);
	var year = now.getFullYear();
	var month = (now.getMonth() + 1 < 10 ? '0' + (now.getMonth() + 1) : now.getMonth() + 1);
	var date = (now.getDate() < 10 ? '0' + (now.getDate()) : now.getDate());
	var hour = (now.getHours() < 10 ? '0' + (now.getHours()) : now.getHours());
	var minute = (now.getMinutes() < 10 ? '0' + (now.getMinutes()) : now.getMinutes());
	var second = now.getSeconds();
	return year + "-" + month + "-" + date + "   " + hour + ":" + minute;
}

//时间戳转时间
function getTimeNoHours(nS) {
	var now = new Date(parseInt(nS) * 1000);
	var year = now.getFullYear();
	var month = (now.getMonth() + 1 < 10 ? '0' + (now.getMonth() + 1) : now.getMonth() + 1);
	var date = (now.getDate() < 10 ? '0' + (now.getDate()) : now.getDate());
	return year + "-" + month + "-" + date;
}

var repeatCount = 0;
var timeout = 10;
var mTimer;
function ScrollSmoothly(scrollHeight, repeatTimes) {
	if (repeatTimes > 0) {
		window.scrollBy(0, 50);

	} else {

		clearTimeout(mTimer);
	}
	repeatTimes = repeatTimes - timeout;
	mTimer = setTimeout("ScrollSmoothly('" + (scrollHeight - 50) + "','" + repeatTimes + "')", timeout);
}

//加法函数
function accAdd(arg1, arg2) {
	var r1, r2, m;
	try {
		r1 = arg1.toString().split(".")[1].length;
	} catch (e) {
		r1 = 0;
	}
	try {
		r2 = arg2.toString().split(".")[1].length;
	} catch (e) {
		r2 = 0;
	}
	m = Math.pow(10, Math.max(r1, r2));
	return (arg1 * m + arg2 * m) / m;
}

//给Number类型增加一个add方法，，使用时直接用 .add 即可完成计算。
Number.prototype.add = function(arg) {
	return accAdd(arg, this);
};

//减法函数
function Subtr(arg1, arg2) {
	var r1, r2, m, n;
	try {
		r1 = arg1.toString().split(".")[1].length;
	} catch (e) {
		r1 = 0;
	}
	try {
		r2 = arg2.toString().split(".")[1].length;
	} catch (e) {
		r2 = 0;
	}
	m = Math.pow(10, Math.max(r1, r2));
	//last modify by deeka
	//动态控制精度长度
	n = (r1 >= r2) ? r1 : r2;
	return ((arg1 * m - arg2 * m) / m).toFixed(n);
}

//给Number类型增加一个add方法，，使用时直接用 .sub 即可完成计算。
Number.prototype.sub = function(arg) {
	return Subtr(this, arg);
};

//乘法函数
function accMul(arg1, arg2) {
	var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
	try {
		m += s1.split(".")[1].length;
	} catch (e) {
	}
	try {
		m += s2.split(".")[1].length;
	} catch (e) {
	}
	return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
}

//给Number类型增加一个mul方法，使用时直接用 .mul 即可完成计算。
Number.prototype.mul = function(arg) {
	return accMul(arg, this);
};

//除法函数
function accDiv(arg1, arg2) {
	var t1 = 0, t2 = 0, r1, r2;
	try {
		t1 = arg1.toString().split(".")[1].length;
	} catch (e) {
	}
	try {
		t2 = arg2.toString().split(".")[1].length;
	} catch (e) {
	}
	with (Math) {
		r1 = Number(arg1.toString().replace(".", ""));
		r2 = Number(arg2.toString().replace(".", ""));
		return (r1 / r2) * pow(10, t2 - t1);
	}
}

//给Number类型增加一个div方法，，使用时直接用 .div 即可完成计算。
Number.prototype.div = function(arg) {
	return accDiv(this, arg);
};

//打开待处理页面
function openPending() {
	var text = window.location.href;
	if(typeof($api.getStorage('user_info'))!="undefined"&&$api.getStorage('user_info')!=null){
	 var uid = $api.getStorage('user_info')['id'];
    	if (!(text.indexOf("Pending_win") > 0)) {
    		api.openWin({
    			name : 'Pending_win',
    			url : 'widget://html/aiyunApp/Pending/Pending_win.html',
    			reload : true,
    			animation : {
    				type : "movein", //动画类型（详见动画类型常量）
    				subType : "from_top", //动画子类型（详见动画子类型常量）
    				duration : 300 //动画过渡时间，默认300毫秒
    			}
    		});
    	}
    	if (text.indexOf("Business_win") > 0) {
    		api.closeWin({
    		});
    	}
	}else{
	   api.openWin({
                name : 'Login_win',
                url : 'widget://html/aiyunApp/login/Login_win.html',
                reload : true,
                animation : {
                    type : "movein", //动画类型（详见动画类型常量）
                    subType : "from_top", //动画子类型（详见动画子类型常量）
                    duration : 300 //动画过渡时间，默认300毫秒
                }
            });
	}
}

//获取待处理业务数量
function getPeddingCount() {
	api.addEventListener({
		name : 'PeddingCount'
	}, function(ret, err) {
		$api.html($api.dom(".handle_num"), ret.value.count);
	});
	if ($api.getStorage('user_info')) {
		var uid = $api.getStorage('user_info')['id'];
		$api.post(aiyunUrl + '/Member/getPeddingCount', {
			values : {
				uid : uid,
			}
		}, function(ret, err) {
			if (ret) {
				if (ret.status == 1) {
					api.sendEvent({
						name : 'PeddingCount',
						extra : {
							count : ret.data,
						}
					});
				} else {
				}
			} else {
			}
		}, 'json');
	}

}

function updatePeddingCount() {
	var uid = $api.getStorage('user_info')['id'];
	var text = window.location.href;
	$api.post(aiyunUrl + '/Member/getPeddingCount', {
		values : {
			uid : uid,
		}
	}, function(ret, err) {
		if (ret) {
			if (ret.status == 1) {
				api.sendEvent({
					name : 'PeddingCount',
					extra : {
						count : ret.data,
					}
				});
			} else {
			}
		} else {
		}
	}, 'json');

	api.sendEvent({
		name : 'PeddingList',
	});
}

