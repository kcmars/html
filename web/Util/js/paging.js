/**
 * Created by keyC on 2018/8/24.
 */
var mPageNum = 10; //一页10条数据
var mRound = 0; //一共分为几页数据
var mRoundList = []; //当前显示出来的 round
var mRoundListLength = 5; //定义基础展示 round 的个数
var currentItem = 1; //表示当前第几页
var currentIndex = 0; //表示当前选中的 round 的下标
var mCurrentPageData = []; //当前页存放的数据
var data = [
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
    3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
    4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
    5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
    6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
    7, 7, 7, 7, 7, 7
]; //总数据

$(function () {
    //判断数据是否存在
    if (data != null && data.length > 0) {
        //获取数据后，根据当前页面显示的数量将数据分页，比如当前一页显示 mPageNum 条，把页面分成 data.length/10 页，向上取整
        mRound = Math.ceil(data.length / mPageNum);

        //从整个数据中获取当前页要显示的数据，要根据当前显示的第几页去获取数据
        //比如要取第三页的内容，那么就要从整个数据的第 30 条往后取 10 条，如果不够 10 条，就全部取
        changeRound(1, 0);
    } else {
        //没得数据
    }

    //上一页
    $("#btn-pre").bind("click", function () {
        if(currentItem > 0) {
            if (currentItem == 1) { //当前是第一页，则不能在向上一页点击，隐藏当前按钮

            } else { //当前不是第一页，则向前翻页，
                let item = parseInt(currentItem) - 1;
                if (item < Math.ceil(mRoundListLength/2)) {
                    currentIndex--;
                } else if (item >= Math.ceil(mRoundListLength/2) && item <= mRound - Math.ceil(mRoundListLength/2)) {
                    currentIndex = Math.floor(mRoundListLength/2);
                } else {
                    currentIndex--;
                }
                changeRound(item, currentIndex);
            }
        } else {

        }
    });

    //下一页
    $("#btn-next").bind("click", function () {
        if(currentItem <= mRound) {
            if (currentItem == mRound) { //当前是最后页，则不能在向后一页点击，隐藏当前按钮

            } else { //当前不是最后页，则向后翻页，
                let item = parseInt(currentItem) + 1;
                if (item < Math.ceil(mRoundListLength/2)) {
                    currentIndex++;
                } else if (item >= Math.ceil(mRoundListLength/2) && item <= mRound - Math.floor(mRoundListLength/2)) {
                    currentIndex = Math.floor(mRoundListLength/2);
                } else {
                    currentIndex++;
                }
                changeRound(item, currentIndex);
            }
        } else {

        }
    });
});

/**
 * 改变 round 的显示
 * @param index 表示当前显示的第几页
 */
function changeRoundList(index) {
    //先清空掉之前的选项
    mRoundList = [];
    //根据当前 currentItem 的位置去显示当前要显示的 roundlist
    //判断当前 currentItem 在 roundlist 中的位置，一行显示的 mRoundListLength（7）个，那么以 currentItem 为中心，前面三个，后面三个
    if (mRound > mRoundListLength) {
        //如果分页的数量大于基础展示的数量，就展示基础数量个 round
        //判断当前的 round 的位置
        if (index <= Math.ceil(mRoundListLength/2)) {
            //如果当前的 round 的位置小于 mRoundListLength（基础展示数量）的一半，
            // 则从 1 开始展示后面基础展示的数量 mRoundListLength（基础展示数量）
            for (let i = 1; i<= mRound; i++) {
                if (i <= mRoundListLength) {
                    mRoundList.push(i);
                }
            }
        } else if (index > Math.ceil(mRoundListLength/2) && index <= mRound - Math.ceil(mRoundListLength/2)) {
            // 如果当前的 round 的位置大于 mRoundListLength（基础展示数量）的一半，
            // 并且当前位置离 mRound（整个 round 数量）的最后一个要大于等于 mRoundListLength（基础展示数量）的一半，
            // 那就展示当前位置前 mRoundListLength（基础展示数量）的一半的位置到后面的 mRoundListLength（基础展示数量） 个
            for (let i = index - Math.floor(mRoundListLength/2); i<= mRound; i++) {
                if (i < parseInt(index - Math.floor(mRoundListLength/2)) + parseInt(mRoundListLength)) {
                    mRoundList.push(i);
                }
            }
        } else {
            // 如果当前的 round 的位置大于 mRoundListLength（基础展示数量）的一半，
            // 并且当前位置离 mRound（整个 round 数量）的最后一个要小于 mRoundListLength（基础展示数量）的一半，
            // 那就展示 mRound（整个 round 数量）的倒数 mRoundListLength（基础展示数量） 个
            for (let i = mRound - mRoundListLength + 1; i <= mRound; i++) {
                mRoundList.push(i);
            }
            // for (let i = mRound; i > mRound - mRoundListLength; i--) {
            //     mRoundList.push(i);
            //     //从最后一个开始的话，需要倒叙数组
            //     mRoundList.reverse();
            // }
        }
    } else {
        //如果分页的数量不够基础展示的数量，就全部显示
        for (let i = 1; i<= mRound; i++) {
            mRoundList.push(i);
        }
    }
    let template = document.getElementById('template-table-round').innerHTML;
    document.getElementById('table-round').innerHTML = doT.template(template)(mRoundList);
}

/**
 * 改变当前显示的数据
 * @param item 显示第几页的数据
 * @param index 点击的当前 round 的下标
 */
function changeRound(item, index) {
    currentItem = item;
    if (item < Math.ceil(mRoundListLength/2)) {
        currentIndex = index;
    } else if (item >= Math.ceil(mRoundListLength/2) && item <= mRound - Math.floor(mRoundListLength/2)) {
        currentIndex = Math.floor(mRoundListLength/2);
    } else {
        currentIndex = index;
    }
    changeRoundList(item);
    if (item == 1) { //当前是第一页，隐藏上一页按钮
        $("#btn-pre").removeClass("btn").addClass("none");
    } else {
        $("#btn-pre").removeClass("none").addClass("btn");
    }
    if (item == mRound) {//当前是最后一页，隐藏下一页按钮
        $("#btn-next").removeClass("btn").addClass("none");
    } else {
        $("#btn-next").removeClass("none").addClass("btn");
    }
    mCurrentPageData = []; //先清空当前显示内容
    //根据 index 改变 round 列表的选中项
    $(".table-round li").eq(parseInt(currentIndex)).addClass("active").siblings().removeClass("active");
    //从整个数据中获取当前页要显示的数据，要根据当前显示的第几页去获取数据
    //比如要取第三页的内容，那么就要从整个数据的第 30 条往后取 10 条，如果不够 10 条，就全部取
    for (let i in data) {
        if (i < item*10 && i >= (parseInt(item)-1)*10) {
            mCurrentPageData.push(data[i]);
        }
    }
    let template = document.getElementById('template-table-content').innerHTML;
    document.getElementById('table-content').innerHTML = doT.template(template)(mCurrentPageData);
}