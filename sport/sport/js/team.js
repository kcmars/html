$(function () {
    getTeamsBasicInfo();
    getTeamsSquad();
    getTransfer();
});
//获取球队的基本信息
function getTeamsBasicInfo() {
    $.ajax({
        type: 'GET',
        url: "http://192.168.31.190:8000/teams/395",
        // data:data,
        dataType: "json",
        data: {id: "1145"},
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
        data: {team_id: "1145"},
        // data: {match_id: param.match_id},
        success: function (result) {
            let res=result.data;
            let template=document.getElementById('template-squad-data-list').innerHTML;
            //传递一个有数据的数组进去
            document.getElementById('squad-data').innerHTML = doT.template( template )( res );
        },
        error: function (err) {
            console.log("err", err);
        }
    });
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
            for(var key in res){
                if(res[key].transfer_type==1){
                        let intoData  = res;
                        let template=document.getElementById('template-into-info-list').innerHTML;
                        //传递一个有数据的数组进去
                        document.getElementById('into-info').innerHTML = doT.template( template )( res );
                    var swiper = new Swiper('.swiper-container', {
                        slidesPerView: 3,
                        spaceBetween : 15,
                        loop: true,
                        navigation: {
                            nextEl: '.swiper-button-next',
                            prevEl: '.swiper-button-prev',
                        },
                    });
                }else if(res[key].transfer_type==2){
                        let outData  = res;
                        console.log("res===",res)
                        let template=document.getElementById('template-out-info-list').innerHTML;
                        //传递一个有数据的数组进去
                        document.getElementById('out-info').innerHTML = doT.template( template )( res );
                    var swiper = new Swiper('.swiper-container', {
                        slidesPerView: 3,
                        spaceBetween : 15,
                        loop: true,
                        navigation: {
                            nextEl: '.swiper-button-next',
                            prevEl: '.swiper-button-prev',
                        },
                    });
            }
            // console.log(res[0].transfer_type)

            }
            // res.map((item, index)=> {
            //     console.log(item,index)
            //     if(item.transfer_type==1){
            //         let intoData  = res;
            //         let template=document.getElementById('template-into-info-list').innerHTML;
            //         //传递一个有数据的数组进去
            //         document.getElementById('into-info').innerHTML = doT.template( template )( res );
            //     }else if(item.transfer_type==2){
            //         let outData  = res;
            //         let template=document.getElementById('template-out-info-list').innerHTML;
            //         //传递一个有数据的数组进去
            //         document.getElementById('out-info').innerHTML = doT.template( template )( res );
            //     }
            // });

        },
        error: function (err) {
            console.log("err", err);
        }
    });
}