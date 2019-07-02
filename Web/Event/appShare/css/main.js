/**
 * Created by Administrator on 2015/9/10.
 */
var captcha_img = $('#captcha-container').find('img')
var verifyimg = captcha_img.attr("src");
captcha_img.attr('title', '点击刷新');
$('#captcha-container').click(function() {
	if (verifyimg.indexOf('?') > 0) {
		captcha_img.attr("src", verifyimg + '&random=' + Math.random());
	} else {
		captcha_img.attr("src",
				verifyimg.replace(/\?.*$/, '') + '?' + Math.random());
	}
});

// 返回顶部

$("#return_top").click(function() {
	$(window).scrodllTop(0);
	// $(window).animate({scrollTop:'0'},200);
});

// 注册和下载页面隐藏显示
$("#registered_button").click(function() {
	$("#formtag_cont").css({
		display : "none"
	});
	$("#myform").css({
		display : "block"
	})
	window.scrollTo(100,300);
});

$(".registered_button").click(function() {
	$("#formtag_cont").css({
		display : "none"
	});
	$("#myform").css({
		display : "block"
	})
	window.scrollTo(100,300);
});
$("#download_button").click(function() {
	$("#apptag_cont").css({
		display : 'none'
	});
	$(".app").css({
		display : 'block'
	})
});
$("#return_formHome").click(function() {
	$("#formtag_cont").css({
		display : "block"
	});
	$("#myform").css({
		display : "none"
	})
});
$("#return_appHome").click(function() {
	$("#apptag_cont").css({
		display : 'block'
	});
	$(".app").css({
		display : 'none'
	})
});
// 返回按钮颜色改变
$("#return_formHome").hover(function() {
	$(this).css({
		backgroundColor : "#253A5F"
	});
	$("#form_icon", $(this)).css({
		borderColor : "white"
	});
}, function() {
	$(this).css({
		backgroundColor : "transparent"
	});
	$("#form_icon", $(this)).css({
		borderColor : "#253A5F"
	});
});

$("#return_appHome").hover(function() {
	$(this).css({
		backgroundColor : "#008C24"
	});
	$("#app_icon", $(this)).css({
		borderColor : "white"
	});
}, function() {
	$(this).css({
		backgroundColor : "transparent"
	});
	$("#app_icon", $(this)).css({
		borderColor : "#008C24"
	});
});

$("#yz").click(
		function() {
			$("#pwd1").val($("#pwd").val());
			var tel = $('.tel').val();
			$.post("/index.php?s=/Home/Member/is_mobile.html", $('#myform')
					.serialize(), function(data) {
				if (data.status == 0) {
					if (data.msg.indexOf('已注册') > 0) {
						var d = dialog({
							title : '欢迎回来，老会员',
							content : data.msg,
							okValue : '立即下载',
							ok : function() {
								window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.seventc.aiyun.activity";
							},
							cancelValue : '稍后',
							cancel : function() {
							}
						});
						d.showModal();
					} else {
						var d = dialog({
							title : '提示',
							content : data.msg
						});
						d.showModal();
						if(data.msg.indexOf('形验证码错误') > 0){
							$('#captcha-container').click();
						}
					}
				} else {
					var validCode = true;
					var time = 60;
					var code = $(this);
					if (validCode) {
						validCode = false;
						var t = setInterval(function() {
							$(".getCode").addClass("getCode1");
							time--;
							$(".getCode").html(time + "秒后重新获取");
							if (time == 0) {
								clearInterval(t);
								$(".getCode").html("重新获取");
								validCode = true;
								$(".getCode").removeClass("getCode1");
							}
						}, 1000)
					}
				}

			}, "json");
		});
// 显示隐藏div
$("#sub")
		.click(
				function() {
					$("#pwd1").val($("#pwd").val());
					$
							.ajax({
								cache : true,
								type : "POST",
								url : '/index.php?s=home/member/newRegisterUser',
								data : $('#myform').serialize(),// 你的formid
								async : false,
								dataType : 'json',
								error : function(request) {
									alert("请检查网络");
								},
								success : function(data) {
									if (data.status == 1) {
										var d = dialog({
											title : '恭喜：注册成功',
											content : '立即下载 爱运【公路运输宝】APP',
											okValue : '立即下载',
											ok : function() {
												window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.seventc.aiyun.activity";
											},
											cancelValue : '稍后',
											cancel : function() {
											}
										});
										d.showModal();
										$("#formtag_cont").css({
											display : 'none'
										});
										$("#formtag_success").css({
											display : 'block'
										});
										$("#myform").css({
											display : 'none'
										});
										$(".app-container").css({
											display : 'block'
										})
									} else {
										var d = dialog({
											title : '提示',
											content : data.msg
										});
										d.showModal();
									}
								}
							});
				});
$("#display").click(function() {
	$("#alert_container").css({
		display : "none"
	})
});

// $(function () {
// //获取短信验证码
// var validCode=true;
// $(".getCode").click (function () {
// var time=60;
// var code=$(this);
// if (validCode) {
// validCode=false;
// var t=setInterval(function () {
// code.addClass("getCode1");
// time--;
// code.html(time+"秒后重新获取");
// if (time==0) {
// clearInterval(t);
// code.html("重新获取");
// validCode=true;
// code.removeClass("getCode1");
//
// }
// },1000)
// }
// })
// });

