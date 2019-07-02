/**
 * Created by Doris on 2017/5/13.
 */
'use strict';

// 整屏滑动
$(function(){
	var container = document.getElementById('container');
	var pages = document.querySelectorAll('.page');
	var slip = Slip(container, 'y').webapp(pages);
	var index,$activePage;
    $('#container .page').eq(0).addClass('active').siblings().addClass('notactive');
	// 轮播渐隐渐出
	var size = $(".slideBox ul li").size();
	var i = 0;
	$(".slideBox ul li").eq(0).show().siblings().hide();
	function slide() {
		i++;
		if(i == size){i = 0;}
		$(".slideBox ul li").eq(i).fadeIn().siblings().fadeOut();
	}
	setInterval(slide,4000);

	// 滑屏时为新页面添加active类，从而触发动画；为上一屏增加notactive类，实现渐隐效果也避免了画面跳动
	slip.end(function () {
		index = slip.page;
		$activePage = $('#container .page').eq(index);
    $activePage.removeClass('notactive').addClass('active').siblings().removeClass('active').addClass('notactive');
  });


	$(".page1 .bannerBox .slideBox").click(function () {
		$(".puBox").fadeIn();
    });

	$(".puBox .close").click(function () {
        $(".puBox").fadeOut();
        showForm() ;
    })
});
