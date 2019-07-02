/**
 * Created by Administrator on 2019/6/20.
 */
$(function(){
    $("#jOrgChart").html("012312322");
    getRequest(getParams);

    //调用android程序中的方法，并传递参数
//        var name = document.getElementById("name_input").value;
//        window.AndroidWebView.showInfoFromJs(name);
    //数据返回
//    $.ajax({
//        url: "./test.json",
//        type: 'GET',
//        dataType: 'JSON',
//        data: {action: 'org_select'},
//        success: function(result){
//            console.log("test=", result);
//            $("#jOrgChart").html(result);
//            var showlist = $("<ul id='org' style='display:none'></ul>");
//            showall(result.data, showlist);
//            $("#jOrgChart").append(showlist);
//            $("#org").jOrgChart( {
//                chartElement : '#jOrgChart',//指定在某个dom生成jorgchart
//                dragAndDrop : false //设置是否可拖动
//            });
//            alert("333");
//        }, error: function (err) {
//             console.log("err=", );
//             $("#jOrgChart").html(err);
//         }
//    });
});

//在android代码中调用此方法
function getParams(){
     $("#jOrgChart").html("00000");
     $("#jOrgChart").html(param.param);
     var showlist = $("<ul id='org' >2222</ul>");
     showall(param.data, showlist);
     $("#jOrgChart").append(showlist);
//     $("#jOrgChart").html(showlist);
     $("#org").jOrgChart( {
         chartElement : '#jOrgChart',//指定在某个dom生成jorgchart
         dragAndDrop : false //设置是否可拖动
     });
}

function showall(menu_list, parent) {
    $.each(menu_list, function(index, val) {
        if(val.childrens.length > 0){

            var li = $("<li></li>");
            li.append("<a>"+val.name+"</a>").append("<ul></ul>").appendTo(parent);
            //递归显示
//            showall(val.childrens, $(li).children().eq(1));
        }else{
            $("<li></li>").append("<a>"+val.name+"</a>").appendTo(parent);
        }
    });

}