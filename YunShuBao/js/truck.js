/**
 * Created by Administrator on 2018/10/26.
 */
$(function () {
    let data = [];
    $.ajax({
        url: '../json/truck.json',
        async: false,
        success: function (res) {
            data = res;
        }
    });
    let templatePlateModels = document.getElementById('template-truck-list').innerHTML;
    let list =  document.getElementById('truck-list');
    list.innerHTML = doT.template(templatePlateModels)(data);
});