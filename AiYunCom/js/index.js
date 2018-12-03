/**
 * Created by Doris on 2017/5/13.
 */
'use strict';

$(function(){
// 下翻时隐藏导航，上翻时显现导航栏
    var winHeight = $(window).height(); // 获取屏幕可视区域高度
    var $beforeScrollTop = $(".bodyBox").scrollTop(); // 获取监听元素滚动前离顶部的距离
    $(".bodyBox").scroll(function () {
        var $afterScrollTop = $(".bodyBox").scrollTop();
        if($afterScrollTop > winHeight - 50){
            $("#header").css({"box-shadow":"0 10px 20px -12px rgba(0, 0, 0, 0.42), 0 3px 20px 0px rgba(0, 0, 0, 0.12), 0 8px 10px -5px rgba(0, 0, 0, 0.2)",
                "background":"#fff"});
            $("#header .tab>a").css("color","rgba(0,0,0,0.8)");
            $("#header .name").css("color","rgba(0,0,0,0.8)");
            $("#header .navBar span").css("background","rgba(0,0,0,0.8)");
            $("#header .tab .underLine").css("border-bottom","2px solid rgba(0,0,0,0.6)");
        } else {
            $("#header").css({"box-shadow":"none","background":"transparent"});
            $("#header .tab>a").css("color","rgba(255,255,255,0.8)");
            $("#header .name").css("color","rgba(255,255,255,0.8)");
            $("#header .navBar span").css("background","#fff");
            $("#header .tab .underLine").css("border-bottom","2px solid rgba(255,255,255,0.8)");
        };
		if ($afterScrollTop > 100) {
            var $delta = $afterScrollTop - $beforeScrollTop;
            if( $delta === 0 ){
                return false;
            } else if ($delta > 0){
                $beforeScrollTop = $afterScrollTop;
                $("#header").addClass("slideDown").removeClass("slideUp");
            } else {
                $beforeScrollTop = $afterScrollTop;
                $("#header").addClass("slideUp").removeClass("slideDown");
            }

		}
    }
    )
    $(".vip").scroll(function () {
            var $afterScrollTop = $(".vip").scrollTop();
            if($afterScrollTop >0){
                $("#header").css({"box-shadow":"0 10px 20px -12px rgba(0, 0, 0, 0.42), 0 3px 20px 0px rgba(0, 0, 0, 0.12), 0 8px 10px -5px rgba(0, 0, 0, 0.2)",
                    "background":"#fff"});
                $("#header .tab>a").css("color","rgba(0,0,0,0.8)");
                $("#header .name").css("color","rgba(0,0,0,0.8)");
                $("#header .navBar span").css("background","rgba(0,0,0,0.8)");
                $("#header .tab .underLine").css("border-bottom","2px solid rgba(0,0,0,0.6)");
            } else {
                $("#header").css({"box-shadow":"none","background":"transparent"});
                $("#header .tab>a").css("color","rgba(255,255,255,0.8)");
                $("#header .name").css("color","rgba(255,255,255,0.8)");
                $("#header .navBar span").css("background","#fff");
                $("#header .tab .underLine").css("border-bottom","2px solid rgba(255,255,255,0.8)");
            };
            if ($afterScrollTop > 0) {
                var $delta = $afterScrollTop - $beforeScrollTop;
                if( $delta === 0 ){
                    return false;
                } else if ($delta > 0){
                    $beforeScrollTop = $afterScrollTop;
                    $("#header").addClass("slideDown").removeClass("slideUp");
                } else {
                    $beforeScrollTop = $afterScrollTop;
                    $("#header").addClass("slideUp").removeClass("slideDown");
                }

            }
        }
    )

 // 移动端隐藏导航
	$("#header .navBarBox").on("mouseover",function () {
		$("#header .navBar").children().css("width","100%");
	}).mouseleave(function () {
		$("#header .navBar :nth-child(2)").css("width","80%");
		$("#header .navBar :nth-child(3)").css("width","80%");
		$("#header .navBar :nth-child(4)").css("width","80%");
	})
	$("#header .navBarBox").click(function () {
        $("#header .tabHide").fadeIn();
	});
	$("#header .navBarHide").click(function () {
        $("#header .tabHide").fadeOut();
	})

	// 爱运宝页面轮播图
　var aiyunbao_pos = 0;
	var $aiyunbao_textList = $(".aiyunbao .section2 .textList>li");
	var $aiyunbao_imgList = $(".aiyunbao .section2 .imgList>picture");
	function aiyunbao_carousel() {
		aiyunbao_pos ++;
		if (aiyunbao_pos === 3) {aiyunbao_pos = 0;}
		$aiyunbao_imgList.eq(aiyunbao_pos).fadeIn("slow").siblings().fadeOut("slow");
		$aiyunbao_textList.eq(aiyunbao_pos).css("opacity","1").siblings().css("opacity","0");
	}
	var aiyunbao_timer = setInterval(aiyunbao_carousel,6000);
	$(".aiyunbao .section2").hover(function () {
		clearInterval(aiyunbao_timer);
	},function () {
		aiyunbao_timer = setInterval(aiyunbao_carousel,6000);
	})



	// 公路运输宝页面轮播实现
  var index = 0;
	var $leftBox = $(".glysb .section3 .boxInner>ul");
	var $rightBox = $(".glysb .section3 .textBox>ul");
	var $btn = $(".glysb .section3 .btnBox");
	var $pill = $(".glysb .section3 .pill");
	var $btnArrow = $(".glysb .section3 .arrow");
	var $btnPrev = $(".glysb .section3 .prev");
	var $btnNext = $(".glysb .section3 .next");
	$btn.children().eq(index).find("span").css("color","#2A87F0")
    $rightBox.children().eq(index).css("display","block").siblings().css("display","none");

	$btn.children().hover(function () {
		if($(this).index() === index){return;} else {
			$btn.children().eq(index).find("span").css("color","#ccc");
			index = $(this).index();
			$leftBox.stop().animate({left:-index*100 + "%"},400);
			$rightBox.children().eq(index).css("display","block").siblings().css("display","none");
			$pill.stop().animate({left:index*25 + "%"},400,function () {
				$btn.children().eq(index).find("span").css("color","#2A87F0");
			});
		}
	})

	$btnNext.click(function () {
		index ++;
		if (index === 4){index = 0;}
		$leftBox.stop().animate({left:-index*100 + "%"},400);
        $rightBox.children().eq(index).css("display","block").siblings().css("display","none");
	})
	$btnPrev.click(function () {
		index --;
		if (index === -1) {index = 3}
		$leftBox.stop().animate({left:-index*100 + "%"},400);
        $rightBox.children().eq(index).css("display","block").siblings().css("display","none");
	})

	function glysb_carsousel() {
		$btn.children().eq(index).find("span").css("color","#ccc");
		index ++;
		if (index === 4){index = 0;}
		$leftBox.stop().animate({left:-index*100 + "%"},400);
        $rightBox.children().eq(index).css("display","block").siblings().css("display","none");
		$pill.stop().animate({left:index*25 + "%"},400,function () {
			$btn.children().eq(index).find("span").css("color","#2A87F0");
		});
	}
	var glysb_timer = setInterval(glysb_carsousel,4000);
	$leftBox.hover(function () {
		clearInterval(glysb_timer);
	},function () {
		glysb_timer = setInterval(glysb_carsousel,4000);
	})
	$rightBox.hover(function () {
		clearInterval(glysb_timer);
	},function () {
		glysb_timer = setInterval(glysb_carsousel,4000);
	});
	$btnArrow.hover(function () {
		clearInterval(glysb_timer);
	},function () {
		glysb_timer = setInterval(glysb_carsousel,4000);
	});
	$btn.hover(function () {
		clearInterval(glysb_timer);
	},function () {
		glysb_timer = setInterval(glysb_carsousel,4000);
	})


	//  判断是否是ios终端则隐藏安卓下载按钮；判断是安卓终端则隐藏苹果下载按钮
    var browser={
        versions:function(){
            var u = navigator.userAgent;
            return {
                mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                android: u.indexOf('Android') > -1 || u.indexOf('Adr') > -1 //android终端
            };
        }()
    };
    if(browser.versions.mobile||browser.versions.android||browser.versions.ios){
        if(browser.versions.ios) {
            $(".downloadShare .downLoadAreaInner .downloadBtn:nth-child(2)").css("display","none");
        } else{
            $(".downloadShare .downLoadAreaInner .downloadBtn:nth-child(1)").css("display","none");
        }
    }

})

