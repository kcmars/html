/**
 * Created by Administrator on 2019/6/20.
 */
$(function(win, doc){
    //数据返回
    $.ajax({
        url: "../json/test.json",
        type: 'GET',
        dataType: 'JSON',
        data: {action: 'org_select'},
        success: function(result){
            console.log("test=", result);
            var showlist = $("<ul id='org' style='display:none'></ul>");
            showall(result.data, showlist);
            $("#jOrgChart").append(showlist);
            $("#org").jOrgChart( {
                chartElement : '#jOrgChart',//指定在某个dom生成jorgchart
                dragAndDrop : false //设置是否可拖动
            });
            var width1 = $(window).width();
            var width2 = $(document).width();
            console.log("width1", width1);
            console.log("width2", width2);

            var height1 = $(window).height();
            var height2 = $(document).height();
            console.log("height1", height1);
            console.log("height2", height2);
            if (width1 >= width2 && height1 >= height2) {

            } else if (width1 < width2 && height1 >= height2) {
                let scale = width1/width2;
                console.log("scale", scale);
                $("body").attr('style', '-webkit-transform: scale('+ scale +') translate(-50%,-50%);' +
                    '-moz-transform: scale('+ scale +') translate(-50%,-50%);' +
                    '-o-transform: scale('+ scale +') translate(-50%,-50%);' +
                    'transform: scale('+ scale +') translate(-50%,-50%);' +
                    'transform-origin: 0 0;' +
                    'position: absolute;' +
                    'left: 50%;' +
                    'top: 50%;');
            } else if (width1 >= width2 && height1 < height2) {
                let scale = height1/height2;
                console.log("scale", scale);
                $("body").attr('style', '-webkit-transform: scale('+ scale +') translate(-50%,-50%);' +
                    '-moz-transform: scale('+ scale +') translate(-50%,-50%);' +
                    '-o-transform: scale('+ scale +') translate(-50%,-50%);' +
                    'transform: scale('+ scale +') translate(-50%,-50%);' +
                    'transform-origin: 0 0;' +
                    'position: absolute;' +
                    'left: 50%;' +
                    'top: 50%;');
            } else {
                let scale1 = width1/width2;
                let scale2 = height1/height2;
                let scale = scale1 > scale2 ? scale2 : scale1;
                console.log("scale", scale);
                $("body").attr('style', '-webkit-transform: scale('+ scale +') translate(-50%,-50%);' +
                    '-moz-transform: scale('+ scale +') translate(-50%,-50%);' +
                    '-o-transform: scale('+ scale +') translate(-50%,-50%);' +
                    'transform: scale('+ scale +') translate(-50%,-50%);' +
                    'transform-origin: 0 0;' +
                    'position: absolute;' +
                    'left: 50%;' +
                    'top: 50%;');
            }
        }, error: function (err) {
            console.log("err=", err);
        }
    });
}(window, document));

function showall(menu_list, parent) {
    $.each(menu_list, function(index, val) {
        if(val.childrens.length > 0){
            var li = $("<li></li>");
            // li.append("<a><img src='../img/family_avatar.png'>" + val.name+"</a>").append("<ul></ul>").appendTo(parent);
            if (val.name1 != "") {
                li.append("<div><a><img src='" + (val.head && val.head != "" ? val.head : "../img/family_avatar.png") + "'><span>" + val.name + "</span><span>" + val.name + "</span><input value='" +
                    val.id
                    + "'></a><span class='line3'></span><a><img src='" +  (val.head && val.head != "" ? val.head : "../img/family_avatar.png") + "'><span>" + val.name1 + "</span><span>" + val.name + "</span><input value='" +
                    val.id
                    + "'></a></div>").append("<ul></ul>").appendTo(parent);
                // li.append("<div><a><img src='../img/family_avatar.png'><span>" + val.name + "</span><input value='" + JSON.stringify(val) + "'></a><a><img src='../img/family_avatar.png'><span>" + val.name1+"</span><input value='" + JSON.stringify(val) + "'></a></div>").append("<ul></ul>").appendTo(parent);
            } else {
                li.append("<div><a><img src='../img/family_avatar.png'><span>" + val.name + "</span><input value='" + JSON.stringify(val) + "'></a></div>").append("<ul></ul>").appendTo(parent);
            }
            //递归显示
            showall(val.childrens, $(li).children().eq(1));
        }else{
            if (val.name1 != "") {
                $("<li></li>").append("<div><a><img src='../img/family_avatar.png'><span>" + val.name + "</span><input value='" + JSON.stringify(val) + "'></a><span class='line3'></span><a><img src='../img/family_avatar.png'><span>" + val.name1+"</span><input value='" + JSON.stringify(val) + "'></a></div>").appendTo(parent);
            } else {
                $("<li></li>").append("<div><a><img src='../img/family_avatar.png'><span>" + val.name + "</span><input value='" + JSON.stringify(val) + "'></a></div>").appendTo(parent);
            }
        }
    });

}