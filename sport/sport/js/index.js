// 赛事区分
(function(){
    var $btn = $('#star .s_btn span');
    var $show = $('#star .s_show');
    var timer = null;
    index = false;
    $btn.click(function(){
        var i = $(this).index();
        if ( !!i != index )
        {
            clearInterval(timer);
            index = !!i;
            $(this).removeClass('able').siblings().addClass('able');
            $show.stop().animate({
                marginLeft : -i*1920 + 'px'
            },500);
            auto();
        }
    });
    auto();
    function auto(){
        timer = setInterval(function (){
            index = !index;
            var x = index-0;
            $btn.eq(x).removeClass('able').siblings().addClass('able');
            $show.stop().animate({
                marginLeft : -x*1920 + 'px'
            },500);
        },6000);
    };
})();
//全部赛事
function country(obj) {
    var text = $(obj).text();
    var index = $("#country li").index($(obj));
    // var li = document.getElementsByTagName("mLi");//获取页面中所有li
    var li = $('li[name="mLi"]');//获取页面中所有li
    for(var i=0; i<li.length; i++) {
        if (i === index) {
            var div = li[i];//获取第i个li
            var myDiv = document.getElementById("myDiv");
            if(myDiv){
                // console.log(li[i]);
                // console.log(div);
                // console.log(div.parentNode);
                div.parentNode.removeChild(myDiv);
            }
            var mHtml = '<div id="myDiv" class="myDiv">我是新的'+ text +'</div>';
            var id = Math.floor(index/6);
            console.log(id);
            $("#country li").eq(6*id + 5).after(mHtml);
        }
    }
}


function showTag(tagNo){
    for(var i=1; i<=2; i++){
        document.getElementById("div"+i).style.display="none";
        document.getElementById("tag"+i).style.background="#ddffdd";
    }
    var tag=document.getElementById("tag"+tagNo);
    tag.style.background="blue";
    var tagContent=document.getElementById("div"+tagNo);
    tagContent.style.display="block";
}






