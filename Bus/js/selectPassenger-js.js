//存储已被添加的乘客
var passenger_list = [];
$(function () {
    getRequest(getParams);
//        getParams();
});

//获取app传递过来的参数，并获取添加的乘客列表
function getParams() {
    console.log(param);
//        var params = {
//            user_id: "TUp6SFVLTzBPME9R"
//        };
    var params = {
        user_id: param.user_id
    };
    $.ajax({
        type: 'POST',
        url: $.getPassengerList,
        data: params,
        success: function (res) {
            console.log(res);
            console.log(params);
            loadAlertHide();
            if (res.status == 1) {
                var data = res.data;
                passenger_list = data;
                //渲染可以选择的乘客列表
                var template=document.getElementById('template_1').innerHTML;
                //传递一个有数据的数组进去
                document.getElementById('showInfo').innerHTML = doT.template( template )( data );
            } else {
                toastAlertShow(res.msg);
            }
        },
        error: function (err) {
            console.log(err);
            loadAlertHide();
        }
    });

    //添加新的乘客
    $(".select-passenger-head-add").bind("click",function(){
        location.href = './addPassenger.html';
    });

    //保存选中的乘客
    $(".select-passenger-btn").bind("click",function(){
        var select_passenger_list = [];
        var select_passenger = document.getElementsByName('select_passenger');
        for (var i = 0; i<select_passenger.length; i++) {
            if(select_passenger[i].checked) {
                select_passenger_list.push(passenger_list[i]);
            }
        }
        if (select_passenger_list.length > 0) {
            localStorage.setItem("select_passenger_list", JSON.stringify(select_passenger_list));
        }
        //返回上一页并刷新上一页
        window.history.back();
        window.history.reload();
    });
}
