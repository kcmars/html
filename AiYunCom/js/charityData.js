/**
 * Created by Doris on 2017/8/5.
 */


var url = "/Aiyunbao/WelfareEducation/welfare", // 捐助明细查询接口
    page = 0;//分页

$(function () {
    getData(page);
    $(".charityData .listBox").scrollLock();  // 滚动数据部分时阻止冒泡
});

//加载更多
function loadMore() {
  $('.moreBtn').css("display","inline-block").addClass('loading');
  $('.msg').css("display","none");
  setTimeout(function () {
    getData(page);
  },300);
}
//获取捐助明细列表
function getData(p) {
    $.ajax({
        type: "POST",
        url: url,
        data: {
            page: p
        },
        dataType: "json",
        success: function (res) {
            if(res.status == -1){
                location.href = '/Home/Website/charity';
            }else if(res.status == 1){
                // 捐助者手机号
                var data = res.data;
                console.log(data);

                // 捐助总金额
                if (res.info){
                    $(".navTab .donateNum ").text(res.info.contributionPhone);
                    $('.navTab .donateTotal').text(res.info.donated_money+'元');
                }
                var compiler = doT.template(document.getElementById('list-tmpl1').innerHTML);
                if(p == 0){
                    var list = document.getElementById('donateList');
                    list.innerHTML = '';
                    list.innerHTML = compiler(data);
                } else if (p > 0) {
                    $('#donateList').append(compiler(data));
                }
                if(data.length<10){
                    $('.moreBtn').css("display","none").removeClass('loading');
                    $('.msg').css("display","block");
                    $('.msg').text('没有更多了');
                }else{
                    $('.moreBtn').css("display","inline-block").removeClass('loading');
                    $('.msg').css("display","none");
                }
            }else{
                $('.moreBtn').css("display","none").removeClass('loading');
                $('.msg').css("display","block");
                $('.msg').text(res.msg);
            }
            page++;
        }
    })
}


// 退出登陆
$('.outBtn').click(function () {
    $.post("/Aiyunbao/WelfareEducation/del",function(data) {
        console.log(data);
        if (data.status == 1) {
            window.location.href = "/Home/Website/charity.html";
        }else {
            layer.open({
                content: data.msg
                , btn: '确定'
            })
        }
    })
})
