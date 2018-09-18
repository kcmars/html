// 赛事区分轮播
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
//全部赛事点击切换
// function country(obj) {
//     var text = $(obj).text();
//     var index = $("#country li").index($(obj));
//     // var li = document.getElementsByTagName("mLi");//获取页面中所有li
//     var li = $('li[name="mLi"]');//获取页面中所有li
//     for(var i=0; i<li.length; i++) {
//         if (i === index) {
//             var div = li[i];//获取第i个li
//             var myDiv = document.getElementById("myDiv");
//             if(myDiv){
//                 // console.log(li[i]);
//                 // console.log(div);
//                 // console.log(div.parentNode);
//                 div.parentNode.removeChild(myDiv);
//             }
//             var mHtml = '<div id="myDiv" class="myDiv">我是新的'+ text +'</div>';
//             var id = Math.floor(index/6);
//             console.log(id);
//             $("#country li").eq(6*id + 5).after(mHtml);
//         }
//     }
// }
$(function () {
    getAllCompetition ();
    getAllTitle ();
});
var gameAllListNameData;
//全部赛事title
function getAllTitle (){
    $.ajax({
        type: 'GET',
        // /match/schedule?match_id=1&match_season=2017-2018
        url: "http://192.168.31.190:8000/match/sort",
        dataType: "json",
        data: {game_type: "1"},
        // data: {match_id: param.match_id},
        success: function (result) {
            let res=result.data;
            let template=document.getElementById('template-game-all-list').innerHTML;
            //传递一个有数据的数组进去
            document.getElementById('game-all-list').innerHTML = doT.template( template )( res );
        },
        error: function (err) {
            console.log("err", err);
        }
    });
}
//全部赛事数据获取
//全部赛事数据
let allEventsDatas;
//国家赛事列表
let countryList = [];
//杯赛列表
let cupList = [];
//点击内容
function getAllCompetition (){
    $.ajax({
        type: 'GET',
        // /match/schedule?match_id=1&match_season=2017-2018
        url: "http://192.168.31.190:8000/match.json",
        // url: "res.json",
        dataType: "json",
        data: {sortid: "1"},
        // data: {match_id: param.match_id},
        success: function (result) {
            console.log("result==",result.data);
            allEventsDatas = result.data;
            //首先是上面得res，这个是所有的数据，然后，我们下面要显示两个列表，左边列表现实的事 res 得每一项下面得area_list下，type为2得 数据。右边事1得数据
            // for (let i in allEventsDatas) {
            showAllEventsData(0); //默认显示第0条的
        },
        error: function (err) {
            console.log("err", err);
        }
    });
}
//新建一个方法，来渲染赛事数据，参数 index 表示 该显示那个下面的赛事
function showAllEventsData(index) {
    $("#game-all-list").find("li").eq(index).addClass("changeColor").siblings().removeClass("changeColor");
    //点击切换的时候，数组要重新赋值，所以把数组清空掉
    cupList = [];
    countryList = [];
    //最后一个不是全不显示嘛，就要单独判断一下
    if (allEventsDatas && allEventsDatas.length > 0) {
        //这哈就对了，类型应该不是int
        if (index == 4) {
            //最后一个单独显示。显示全部的话，就把右边那个隐藏掉
            $(".right").addClass("none");
            var areaList = allEventsDatas[index].area_list; //获取点击的对应选项的数据
            for (let j in areaList) {
                countryList.push(areaList[j]);
            }
            let template1=document.getElementById('template-game-all-name-list').innerHTML;
            //传递一个有数据的数组进去
            document.getElementById('game-all-name-list').innerHTML = doT.template( template1)(countryList);
        } else {
            $(".right").removeClass("none");
            //allEventsDatas 得每一项下面得area_list下，type为2得 数据
            let areaList = allEventsDatas[index].area_list; //获取点击的对应选项的数据
            //然后来循环areaList，取里面得type对应得值
            for (let j in areaList) {
                let type = areaList[j].type;
                if (type === 1) {
                    cupList.push(areaList[j]); //1就把当前项放到杯赛中去
                } else if (type === 2) {
                    countryList.push(areaList[j]); //2就把当前项放到国家中去
                }
            }
            let template2=document.getElementById('template-game-all-name-cup-list').innerHTML;//找到M模板
            //传递一个有数据的数组进去，
            document.getElementById('game-all-name-cup-list').innerHTML = doT.template( template2 )(cupList);
            let template1=document.getElementById('template-game-all-name-list').innerHTML;
            //传递一个有数据的数组进去
            document.getElementById('game-all-name-list').innerHTML = doT.template( template1)(countryList);
            showCountryItemList(0)
            showCupItemList(0)
        }
    }
}
function showCountryItemList(index){//显示国家对应项的下面的list
    if(countryList && countryList.length > 0){
        let countryMatchList = countryList[index].match_list;
        let n = 4; //表示一行显示几个
        let li = $('li[name="mLiCountry"]');//获取当前ul中所有li
        let last = Math.ceil(li.length/n); //获取行数
        for(let i=0; i<li.length; i++) {
            if (i === parseInt(index)) {
                let div = li[i];//获取第i个li
                let myDiv = document.getElementById("myDiv");
                if(myDiv){
                    div.parentNode.removeChild(myDiv);
                }
                let mHtml = '<div id="myDiv" class="myDiv">';
                //这里循环，把matchList里面的name全部显示出来
                for (let k in countryMatchList){
                    let mSpan = '<a>' + countryMatchList[k].name + '</a>';
                    mHtml += mSpan;
                }
                mHtml += '</div>';
                let id = Math.floor(index / n);
                //如果是最后一行，则直接加在末尾，如果不是，则加在当前行的后面
                if ((id + 1) === last) {
                    $("#game-all-name-list").append(mHtml);
                } else {
                    $("#game-all-name-list li").eq(n*id + (n - 1)).after(mHtml);
                }
            }
        }
    }
}
function showCupItemList(index) {//显示杯赛下面对应的国家的list
    // console.log("111111",index)
    if(cupList && cupList.length > 0){
        let cupMatchList = cupList[index].match_list;
        console.log(cupList[index].match_list)
        let n = 3; //表示一行显示几个
        let li = $('li[name="mLiCup"]');//获取当前ul中所有li
        let last = Math.ceil(li.length/n); //获取行数
        for(let i=0; i<li.length; i++) {
            if (i === parseInt(index)) {
                let div = li[i];//获取第i个li
                let myDiv = document.getElementById("myDiv1");
                if(myDiv){
                    div.parentNode.removeChild(myDiv);
                }
                let mHtml = '<div id="myDiv1" class="myDiv">';
                //这里循环，把matchList里面的name全部显示出来
                for (let k in cupMatchList){
                    let mSpan = '<a>' + cupMatchList[k].name + '</a>';
                    mHtml += mSpan;
                }
                mHtml += '</div>';
                let id = Math.floor(index / n);
                //如果是最后一行，则直接加在末尾，如果不是，则加在当前行的后面
                if ((id + 1) === last) {
                    $("#game-all-name-cup-list").append(mHtml);
                } else {
                    $("#game-all-name-cup-list li").eq(n*id + (n - 1)).after(mHtml);
                }
            }
        }
    }
}
