$(function () {
    getSelectTeams();
    getSchedule();
    getCupData();
    getCompetition();
    getRelatedNews ();
    getIntegral();
});
let result;
//获取select中的球队信息
function getSelectTeams() {
    // console.log("xxx",$.getTeams)
    $.ajax({
        type: 'GET',
        url: "http://192.168.31.190:8000/teams",
        // data:data,
        dataType: "json",
        data: {match_id: "2"},
        // data: {match_id: param.match_id},
        success: function (result) {
            let res=result.data;
            let selTeams=document.getElementById("sel-team");
            // $("#sel-team").appendChild(new Option("1","bbbbbbbbb"));
           res.map((item, index)=> {
               selTeams.options.add(new Option(item.name, index));
               // console.log(item.name)
           });
        },
        error: function (err) {
            console.log("err", err);
        }
    });
}
//获取第几轮比赛的表格数据
function getSchedule(){
    $.ajax({
        type: 'GET',
        // /match/schedule?match_id=1&match_season=2017-2018
        url: "http://192.168.31.190:8000/match/schedule",
        dataType: "json",
        data: {match_id: "19",match_season:"2018"},
        success: function (res) {
            result = res.data;
            // console.log("bbb====",res.data);
            // away_tid game_id home_tid rounds run_head scores
            let run_head = result.run_head;
            // console.log("run_head===", run_head);
            if(run_head && run_head.length > 0){
                let template1=document.getElementById('template-game-num-table-list').innerHTML;
                //传递一个有数据的数组进去
                document.getElementById('game-num').innerHTML = doT.template( template1 )( run_head );
            }
            $("#game-num").find("li").eq(0).addClass("current-active").siblings().removeClass("current-active");
            showDataTable(res.data.current_run);

        },
        error: function (err) {
            console.log("err", err);
        }
    });
}
//显示title列表
function showDataTableHeadList(index) {
    $("#game-num").find("li").eq(index).addClass("current-active").siblings().removeClass("current-active");
    //显示对应的数据
    showDataTable(index);
}
//几轮表格数据待用
function showDataTable(index) {
    // console.log(index);
    // console.log("result===",result.rounds);
    let data = [];//先建一个数组
    for (let i = 0; i< result.rounds.length; i++) {//然后循环，填充数据
        if(parseInt(result.rounds[i]) === parseInt(index) ){
            // console.log("111index==", index);
            let obj = {}; //建一个obj对象，填充数据，然后push到data里面
            obj.away_tid = result.away_tid[i];
            obj.away_tn = result.away_tn[i];
            obj.game_id = result.game_id[i];
            obj.home_tid = result.home_tid[i];
            obj.home_tn = result.home_tn[i];
            obj.rounds = result.rounds[i];
            obj.scores = result.scores[i];
            obj.time = result.time[i];
            obj.rounds = result.rounds[i];
            // obj.run_head = result.run_head[i];
            data.push(obj);
        }
    }
    let template1=document.getElementById('template-super-league-table-list').innerHTML;
    //传递一个有数据的数组进去
    document.getElementById('super-league-table').innerHTML = doT.template( template1 )( data );

}
//杯赛数据统计
function getCupData(){
    $.ajax({
        type: 'GET',
        url: "http://192.168.31.190:8000/match/standstat",
        dataType: "json",
        data: {match_id: "1",match_season:"2018"},
        // data: {match_id: param.match_id},
        // away_agv_goal: "1.11"away_defensive_max_lose_num: 1away_defensive_max_team: "塞尔维亚白鹰,乌克兰联,"
        // away_defensive_min_lose_num: 8away_defensive_min_team: "汉密尔顿城,"away_goal: "20"away_goal_max_num: 6
        // away_goal_max_team: "FC沃尔库塔,乌克兰联,"away_goal_min_num: 0away_goal_min_team: "CSC密西沙加,布兰特福德银河,斯卡伯勒,"
        // away_sper: "27.78"away_win_num: 5defensive_max_lose_num: 2defensive_max_team: "塞尔维亚白鹰,"defensive_min_lose_num: 11
        // defensive_min_team: "汉密尔顿城防守最差球队,"draw_num: 5draw_sper: "27.78"finish_num: 18finish_sper: "25.35"
        // goal_max_num: 15goal_max_team: "FC沃尔库塔,"goal_min_num: 1goal_min_team: "CSC密西沙加,皇家密西沙加,"
        // home_agv_goal: "1.78"home_defensive_max_lose_num: 0home_defensive_max_team: "皇家密西沙加,"
        // home_defensive_min_lose_num: 4home_defensive_min_team: "布兰特福德银河,斯卡伯勒,"
        // home_goal: "32"home_goal_max_num: 9home_goal_max_team: "FC沃尔库塔,"home_goal_min_num: 0
        // home_goal_min_team: "皇家密西沙加,"home_win_num: 8
        // home_win_sper: "44.44"id: "5b2f70bcf334d845ed796369"last_update: "2018-06-25 17:45:05"
        // match_id: 1match_season: "2018"total_avg_goal: "2.89"total_goal: 52unfinish_num: 53unfinish_sper: "74.65"
        success: function (result) {
            let res=result.data;
            // console.log("xxxxx",res);
            for(var key in res){
                $('.'+key).append(res[key])
            }
            // //获胜场数
            // $('.home_win_num').append(res.home_win_num)
            // $('.home_win_sper').append(res.home_win_sper + "%")
            // $('.draw_num').append(res.draw_num)
            // $('.draw_sper').append(res.draw_sper + "%")
            // $('.away_win_num').append(res.away_win_num)
            // $('.away_sper').append(res.away_sper + "%")
            // //已赛场数
            // $('.finish_num').append(res.finish_num + "场")
            // //主场客场进球数
            // $('.total_goal').append(res.total_goal)
            // $('.total_avg_goal').append("平均" + res.total_avg_goal + "/场")
            // $('.home_goal').append(res.home_goal)
            // $('.home_agv_goal').append("平均" + res.home_agv_goal + "/场")
            // $('.away_goal').append(res.away_goal)
            // $('.away_agv_goal').append("平均" + res.away_agv_goal + "/场")
            // //最佳最弱防守
            // $('.defensive_min_team').append(res.defensive_min_team)
            // $('.defensive_min_lose_num').append(res.defensive_min_lose_num + '球')
            // $('.home_defensive_min_team').append(res.home_defensive_min_team)
            // $('.home_defensive_min_lose_num').append(res.home_defensive_min_lose_num + '球')
            // $('.away_defensive_min_team').append(res.away_defensive_min_team)
            // $('.away_defensive_min_lose_num').append(res.away_defensive_min_lose_num + '球')
            // $('.defensive_max_team').append(res.defensive_max_team)
            // $('.defensive_max_lose_num').append(res.defensive_max_lose_num + '球')
            // $('.home_defensive_max_team').append(res.home_defensive_max_team)
            // $('.home_defensive_max_lose_num').append(res.home_defensive_max_lose_num + '球')
            // $('.away_defensive_max_team').append(res.away_defensive_max_team)
            // $('.away_defensive_max_lose_num').append(res.away_defensive_max_lose_num + '球')
            // //最佳最弱攻击
            // $('.goal_max_team').append(res.goal_max_team)
            // $('.goal_max_num').append(res.goal_max_num + '球')
            // $('.home_goal_max_team').append(res.home_goal_max_team)
            // $('.home_goal_max_num').append(res.home_goal_max_num + '球')
            // $('.away_goal_max_team').append(res.away_goal_max_team)
            // $('.away_goal_max_num').append(res.away_goal_max_num + '球')
            // $('.goal_min_team').append(res.goal_min_team)
            // $('.goal_min_num').append(res.goal_min_num + '球')
            // $('.home_goal_min_team').append(res.home_goal_min_team)
            // $('.home_goal_min_num').append(res.home_goal_min_num + '球')
            // $('.away_goal_min_team').append(res.away_goal_min_team)
            // $('.away_goal_min_num').append(res.away_goal_min_num + '球')
        },
        error: function (err) {
            console.log("err", err);
        }
    });
}
//英超赛制
function getCompetition (){
    $.ajax({
        type: 'GET',
        // /match/schedule?match_id=1&match_season=2017-2018
        url: "http://192.168.31.190:8000/match/profile",
        dataType: "json",
        data: {match_id: "9"},
        // data: {match_id: param.match_id},
        success: function (result) {
            let res=result.data;
            // console.log("xxxxx",res.profile);
            $('#profile').append(res.profile)
        },
        error: function (err) {
            console.log("err", err);
        }
    });
}
//相关新闻
function getRelatedNews(){
    // $.ajax({
    //     type: 'GET',
    //     // /match/schedule?match_id=1&match_season=2017-2018
    //     url: "http://192.168.31.190:8000/match/profile",
    //     dataType: "json",
    //     data: {match_id: "9"},
    //     // data: {match_id: param.match_id},
    //     success: function (result) {
    //         let res=result.data;
    //         // console.log("xxxxx",res.profile);
    //     },
    //     error: function (err) {
    //         console.log("err", err);
    //     }
    // });
}



//联赛积分
function getIntegral(){
    $.ajax({
        type: 'GET',
        url: "http://192.168.31.190:8000/match/standing",
        dataType: "json",
        data: {match_id: "19",match_season:"2018"},
        // data: {match_id: param.match_id},
        success: function (result) {
            let res=result.data;
            console.log("xxxxx",res);
            let template1=document.getElementById('template-all-integral-table-list').innerHTML;
            //传递一个有数据的数组进去
            document.getElementById('all-integral').innerHTML = doT.template( template1 )(res[0]);
            let template2=document.getElementById('template-home-integral-table-list').innerHTML;
            //传递一个有数据的数组进去
            document.getElementById('home-integral').innerHTML = doT.template( template2 )(res[1]);
            let template3=document.getElementById('template-away-integral-table-list').innerHTML;
            //传递一个有数据的数组进去
            document.getElementById('away-integral').innerHTML = doT.template( template3 )(res[2]);
        },
        error: function (err) {
            console.log("err", err);
        }
    });
}