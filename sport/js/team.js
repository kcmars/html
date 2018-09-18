$(function () {
    getTeamsBasicInfo();
    getTeamsSquad();
    getTransfer();
    getTiming();
});
let mTeamsSquadInfo = [];
//获取球队的基本信息
function getTeamsBasicInfo() {
    $.ajax({
        type: 'GET',
        url: "http://192.168.31.190:8000/teams/395",
        // data:data,
        dataType: "json",
        data: {id: "395"},
        success: function (result) {
            let res=result.data;
            for(var key in res){
                $('.'+key).append(res[key])
            }
        },
        error: function (err) {
            console.log("err", err);
        }
    });
}
//球队阵容
function getTeamsSquad() {
    $.ajax({
        type: 'GET',
        url: "http://192.168.31.190:8000/players",
        dataType: "json",
        data: {team_id: "395"},
        // data: {match_id: param.match_id},
        success: function (result) {
            let res = result.data;
            // console.log("============",res);
            let data0 = [];
            let data1 = [];
            let data2 = [];
            let data3 = [];
            for (let key in res) {
                if (res[key].location === "0") {
                    data0.push(res[key])
                } else  if(res[key].location === "1"){
                    data1.push(res[key])
                } else if (res[key].location === "2") {
                    data2.push(res[key])
                }else if (res[key].location === "3"){
                    data3.push(res[key])
                }
            }
            mTeamsSquadInfo.push(data0);
            mTeamsSquadInfo.push(data1);
            mTeamsSquadInfo.push(data2);
            mTeamsSquadInfo.push(data3);
            showTitleTipData(0);
        },
        error: function (err) {
            console.log("err", err);
        }
    });
}

//showTitleTipData
function showTitleTipData(index) {
    // console.log("----", index);
    let res = mTeamsSquadInfo[index];
    let template=document.getElementById('template-squad-data-list').innerHTML;
    //传递一个有数据的数组进去
    document.getElementById('squad-data').innerHTML = doT.template( template )( res );
}


//球队转会信息(转入和转出)
function getTransfer(){
    $.ajax({
        type: 'GET',
        url: "http://192.168.31.190:8000/transfer/team",
        dataType: "json",
        data: {team_id: "1145"},
        // data: {match_id: param.match_id},
        success: function (result) {
            let res=result.data;
            // console.log("-======",res);
            let data0 = [];
            let data1 = [];
            for(let key in res){
                if(res[key].transfer_type === "1"){
                    data0.push(res[key]);
                }else if(res[key].transfer_type === "2"){
                    data1.push(res[key]);
                }
            }
            let template1 = document.getElementById('template-into-info-list').innerHTML;
            //传递一个有数据的数组进去
            document.getElementById('into-info').innerHTML = doT.template( template1 )( data0 );
            let swiper1 = new Swiper('.swiper-container', {
                slidesPerView: 3,
                spaceBetween : 25,
                loop: true,
                navigation: {
                    nextEl: '.swiper-button-next',
                    prevEl: '.swiper-button-prev',
                },
            });
            let template2 = document.getElementById('template-out-info-list').innerHTML;
            //传递一个有数据的数组进去
            document.getElementById('out-info').innerHTML = doT.template( template2 )( data1 );
            let swiper2 = new Swiper('.swiper-container', {
                slidesPerView: 3,
                spaceBetween : 25,
                loop: true,
                navigation: {
                    nextEl: '.swiper-button-next',
                    prevEl: '.swiper-button-prev',
                },
            });
        },
        error: function (err) {
            console.log("err", err);
        }
    });
}

//赛程赛果
function getTiming() {
    let res = [1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1];
    let template=document.getElementById('template-timing-data-list').innerHTML;
    //传递一个有数据的数组进去
    document.getElementById('timing-data').innerHTML = doT.template( template )( res );
    // $.ajax({
    //     type: 'GET',
    //     url: "http://192.168.31.190:8000/schedule",
    //     dataType: "json",
    //     data: {team_id: "395",match_season:"2018"},
    //     // data: {match_id: param.match_id},
    //     success: function (result) {
    //         let res=result.data;
    //         // console.log(res)
    //         let template=document.getElementById('template-timing-data-list').innerHTML;
    //         //传递一个有数据的数组进去
    //         document.getElementById('timing-data').innerHTML = doT.template( template )( res );
    //     },
    //     error: function (err) {
    //         console.log("err", err);
    //     }
    // });
}