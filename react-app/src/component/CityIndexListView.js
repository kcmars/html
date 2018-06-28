/**
 * Created by Administrator on 2018/6/20.
 */
import React, {Component} from 'react';
import "../style/cityListCss.css";

export default class CityIndexListView extends Component {
    constructor(props) {
        super(props);
        this.letter = ["A","B","C","D","E","F","G","H","J","K","L","M","N","O","P","Q","R","S","T","W","X","Y","Z"];
        this.popularBrand = ["A","B","C","D","E","F","G","H","J","K","L","M"];
        this.brandList = ["A","B","C","D","E","F","G","H","J","K","L","M"];
        this.thirdLayerTouchStartX = 0; //三级菜单触摸起点x位置
        this.secondLayerTouchStartX = 0; //二级菜单触摸起点x位置
        this.firstLayerTouchStartX = 0; //一级菜单触摸起点x位置
        this.firstLayerTouchStartY = 0; //一级菜单触摸起点y位置
        this.colors = [{color:"white", text: "白色"},
            {color:"red", text: "红色"},
            {color:"black", text: "黑色"},
            {color:"silver", text: "银色"},
            {color:"champagne", text: "香槟色"},
            {color:"gray", text: "灰色"},
            {color:"blue", text: "蓝色"},
            {color:"yellow", text: "黄色"},
            {color:"green", text: "绿色"},
            {color:"coffee", text: "咖啡色"},
            {color:"orange", text: "橙色"},
            {color:"other", text: "其他"}];
        this.state = {
            handleScroll: false, //滑动第一层页面是否更改其他状态
            letter: "A", //toast显示内容
            historyModelsIndex: -1, //历史选择车型下标
            toastClassName: "none", //toast显示样式
            carHotModelsIndex: -1, //热门车型下标
            carModelsIndex: -1, //列表车型下标
            carSeriesIndex: -1, //列表车系下标
            carColorIndex: -1, //列表车色下标
            secondLayerClassName: "", //二级菜单样式
            thirdLayerClassName: "", //三级菜单样式
            slideLetterClassName: "none", // 滑动字母滚动层样式
            secondLayerShow: false, //第二级菜单是否显示
            thirdLayerShow: false, //第三级菜单是否显示
        };
    }

    componentDidMount() {
        //获取数据
        // this.getData();
        // window.onscroll = () => this.handleScroll();
        this.thirdLayer.addEventListener("touchstart", this.thirdLayerTouchStart, false);
        this.thirdLayer.addEventListener("touchmove", this.thirdLayerTouchMove, false);
        this.secondLayer.addEventListener("touchstart", this.secondLayerTouchStart, false);
        this.secondLayer.addEventListener("touchmove", this.secondLayerTouchMove, false);
        this.firstLayer.addEventListener("touchstart", this.firstLayerTouchStart, false);
        this.firstLayer.addEventListener("touchmove", this.firstLayerTouchMove, false);
        this.letterList.addEventListener("touchstart", this.letterTouchStart, false);
        this.letterList.addEventListener("touchmove", this.letterTouchMove, false);
        this.letterList.addEventListener("touchend", this.letterTouchEnd, false);
    }

    // //获取数据
    // getData = () =>{
    //     fetch('url', {
    //         method: 'POST',
    //         headers: {
    //             'Accept': 'application/json',
    //             'Content-Type': 'application/json',
    //         },
    //         body: JSON.stringify(params)
    //     })
    //         .then((response) => response.json())//把response转为json格式
    //         .then((res)=>{
    //             console.log("res=====", res);
    //         }).catch((err)=>{
    //         console.log("err=====", err);
    //     });
    // };

    //第三层页面触摸开始
    thirdLayerTouchStart = (event) =>{
        // console.log("thirdLayerTouchStart==", event.changedTouches[0].clientX);
        this.thirdLayerTouchStartX = event.changedTouches[0].clientX;
    };

    //第三层页面触摸移动
    thirdLayerTouchMove = (event) => {
        // console.log("thirdLayerTouchMove==", event.changedTouches[0].clientX);
        let mX = event.changedTouches[0].clientX;
        if((mX - this.thirdLayerTouchStartX) > 40){
            this.setState({
                thirdLayerClassName: "third-layer-animation-out",
                thirdLayerShow: false,
            })
        }
    };

    //第二层页面触摸开始
    secondLayerTouchStart = (event) => {
        this.secondLayerTouchStartX = event.changedTouches[0].clientX;
    };

    //第二层页面触摸移动
    secondLayerTouchMove = (event) => {
        // console.log("thirdLayerTouchMove==", event.changedTouches[0].clientX);
        let mX = event.changedTouches[0].clientX;
        if((mX - this.secondLayerTouchStartX) > 40){
            this.setState({
                carHotModelsIndex: -1, //热门车型下标
                carModelsIndex: -1, //列表车型下标
                carSeriesIndex: -1, //列表车系下标
                carColorIndex: -1, //列表车色下标
                historyModelsIndex: -1,
                secondLayerClassName: "second-layer-animation-out",
                thirdLayerClassName: this.state.thirdLayerShow ? "third-layer-animation-out" : "",
                secondLayerShow: false,
                thirdLayerShow: false,
            })
        }
    };

    //第二层页面触摸开始
    firstLayerTouchStart = (event) => {
        this.firstLayerTouchStartX = event.changedTouches[0].clientX;
        this.firstLayerTouchStartY = event.changedTouches[0].clientY;
    };

    //第一层页面滚动监听
    firstLayerTouchMove= (event) => {
        let mX = event.changedTouches[0].clientX;
        let mY = event.changedTouches[0].clientY;
        if ((mX - this.firstLayerTouchStartX) > 30 || Math.abs(mY - this.firstLayerTouchStartY) > 30) {
            if (this.state.handleScroll) {
                this.setState({
                    handleScroll: false,
                    carHotModelsIndex: -1, //热门车型下标
                    carModelsIndex: -1, //列表车型下标
                    carSeriesIndex: -1, //列表车系下标
                    carColorIndex: -1, //列表车色下标
                    historyModelsIndex: -1,
                    secondLayerClassName: this.state.secondLayerShow ? "second-layer-animation-out" : "",
                    thirdLayerClassName: this.state.thirdLayerShow ? "third-layer-animation-out" : "",
                    secondLayerShow: false,
                    thirdLayerShow: false,
                });
            }
        }
    };

    //右边字母导航触摸开始事件
    letterTouchStart = (event) => {
        clearTimeout(this.outTimer);
        let target = event.touches[0].target;
        // console.log("letterTouchStart==", event.targetTouches);
        // console.log("letterTouchStart==", event.changedTouches);
        if (target && target.localName === "li") {
            this.setState({
                letter: target.innerText,
                toastClassName: "toast-box",
                slideLetterClassName: "slide-letter"
            });
            if (this.page.childNodes[target.id]) {
                this.page.childNodes[target.id].scrollIntoView();
            }
        }
        // window.scrollTo(0, event.touches[0].pageY);
    };

    //右边字母导航触摸移到事件
    letterTouchMove = (event) => {
        // event.preventDefault();
        // 判断默认行为是否可以被禁用
        if (event.cancelable) {
            // 判断默认行为是否已经被禁用
            if (!event.defaultPrevented) {
                event.preventDefault();
            }
        }
        //创建鼠标滑动的地点的对象
        let mTarget = document.elementFromPoint(event.changedTouches[0].clientX, event.changedTouches[0].clientY);
        // console.log("mTarget======", mTarget);
        // console.log("mTarget======", mTarget.innerText);
        if (mTarget && mTarget.localName === "li") {
            this.setState({
                letter: mTarget.innerText,
                toastClassName: "toast-box"
            });
            if (this.page.childNodes[mTarget.id]) {
                this.page.childNodes[mTarget.id].scrollIntoView();
            }
        }
    };

    //右边字母导航触摸完成事件
    letterTouchEnd = (event) => {
        this.setState({
            slideLetterClassName: "none"
        });
        this.outTimer = setTimeout(()=>{
            this.setState({
                toastClassName: "none",
            });
        },400);
    };

    //选择车型弹出二级选择框
    openBrand = (type, index, data, event) =>{
        // console.log("data==", data);
        // console.log("event==", event);
        // console.log("选了"+data);
        this.setState({
            handleScroll: true,
            carHotModelsIndex: this.state.historyModelsIndex !== index && type==="hot" ? index : -1,
            carModelsIndex: this.state.historyModelsIndex !== index && type==="list" ? index : -1,
            historyModelsIndex: this.state.historyModelsIndex !== index ? index : -1,
            carSeriesIndex: -1,
            carColorIndex: -1,
            secondLayerClassName: this.state.historyModelsIndex !== index ? "second-layer-animation-in" : this.state.secondLayerShow ? "second-layer-animation-out" : "",
            thirdLayerClassName: this.state.thirdLayerShow ? "third-layer-animation-out" : "",
            secondLayerShow: !this.state.secondLayerShow,
            thirdLayerShow: false,
        });
    };

    //选择车系弹出三级选择框
    openColor = (index, data, event) =>{
        // console.log("data==", data);
        // console.log("event==", event);
        // console.log("选了"+data);
        this.setState({
            carSeriesIndex: index,
            carColorIndex: -1,
            thirdLayerClassName: "third-layer-animation-in",
            thirdLayerShow: true,
        });
    };

    //选择颜色
    selectColor = (index, color) =>{
        this.setState({
            carColorIndex: index
        });
        // console.log("选了"+color);
    };

    render() {
        return (
            <div className="container">
                <div ref={(ref)=>{this.firstLayer = ref}} className="first-layer">
                    <div className="layer-left">
                        <div className="header">
                            <img alt="Smile" src="https://static.didialift.com/pinche/gift/resource/1b08b56e0fc1b3ac46b590c3f112a290-face_icon.png"/>
                            <div className="header-text">
                                <span className="header-name">你好，王建波</span>
                                <span className="header-msg">完善信息成为车主「 顺路接单 分摊油费 」</span>
                            </div>
                        </div>
                        <div className="popular-brand">
                            <div className="text">
                                <span>选择车辆品牌</span>
                                <span>本人或他人车辆均可</span>
                            </div>
                            {/*<!-- 热门车型 -->*/}
                            <ul>
                                {this.popularBrand.map((item, index)=>{
                                    return (
                                        <li key={index} onClick={this.openBrand.bind(this, "hot", index, item)}>
                                            <img alt="Logo" src="https://static.didialift.com/pinche/xiaojushow/logo/dazhong.png"/>
                                            <span className={this.state.carHotModelsIndex === index ? "orange-text" : ""}>
                                                {item}
                                            </span>
                                        </li>
                                    )
                                })}
                            </ul>
                        </div>
                        {/*<!-- 页面按字母排序的列表，显示车型 -->*/}
                        <ul ref={(ref)=>this.page=ref} className={["brand-list"].join(' ')}>
                            {this.letter.map((item, index)=>{
                                return (
                                    <li key={index}>
                                        <span className="list-letter">{item}</span>
                                        <ul className="brand-list-item-list">
                                            {this.brandList.map((item, index)=>{
                                                return (
                                                    <li key={index} onClick={this.openBrand.bind(this, "list", index, item)}>
                                                        <img alt="Logo" src="https://static.didialift.com/pinche/xiaojushow/logo/dazhong.png"/>
                                                        <span className={this.state.carModelsIndex === index ? "orange-text" : ""}>
                                                            {item}
                                                        </span>
                                                    </li>
                                                )
                                            })}
                                        </ul>
                                    </li>
                                )
                            })}
                        </ul>
                    </div>
                    {/*<!-- 右边字母索引 -->*/}
                    <ul ref={(ref)=>{this.letterList = ref}} className={["letter"].join(' ')}>
                        {this.letter.map((item, index)=>{
                            return (
                                <li key={index} id={index}>
                                    {item}
                                </li>
                            )
                        })}
                    </ul>
                </div>
                {/*<!-- 第二级菜单，显示车系 -->*/}
                <div ref={(ref)=>{this.secondLayer = ref}} className={["second-layer",this.state.secondLayerClassName].join(' ')}>
                    <ul className="car-series-item-list">
                        {this.brandList.map((item, index)=>{
                            return (
                                <li key={index} onClick={this.openColor.bind(this,index, item)}>
                                    <div className="car-series-item">
                                        <span className={this.state.carSeriesIndex === index ? "orange-text" : ""}>
                                            {item}
                                        </span>
                                    </div>
                                </li>
                            )
                        })}
                    </ul>
                </div>
                {/*<!-- 第三级菜单，显示颜色 -->*/}
                <div ref={(ref)=>{this.thirdLayer = ref}} className={["third-layer",this.state.thirdLayerClassName].join(' ')}>
                    <ul className="car-color-item-list">
                        {this.colors.map((item, index)=>{
                            return (
                                <li key={index} onClick={this.selectColor.bind(this, index, item.text)}>
                                    <div className="car-color-item">
                                        <div className={["car-color", item.color].join(' ')}></div>
                                        <span className={this.state.carColorIndex === index ? "orange-text" : ""}>
                                            {item.text}
                                        </span>
                                    </div>
                                </li>
                            )
                        })}
                    </ul>
                </div>
                {/*<!-- 点击字母索引时出现的字母滑动层，用于整个屏幕都可以滑动显示字母，全透明 -->*/}
                <ul className={[this.state.slideLetterClassName].join(' ')}>
                    {this.letter.map((item, index)=>{
                        return (
                            <li key={index} id={index}>
                                {item}
                            </li>
                        )
                    })}
                </ul>
                {/*<!-- 中间黑色区域的 toast 字母显示 -->*/}
                <div className={this.state.toastClassName}>
                    <div className="toast">
                        {this.state.letter}
                    </div>
                </div>
            </div>
        )
    }
}