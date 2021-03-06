/**
 * Created by Administrator on 2018/6/19.
 */
import React, { Component } from 'react';
import {
    Link
} from 'react-router-dom';
import '../style/App.css';
import Button from './button';
import Toggle from './toggle';

export default class About extends Component {
    //组件的生命周期函数以及执行顺序
    //每个组件都可以设置默认的props，如果是多页开发时，可以为不同的页面打造不同的header效果
    static defaultProps = {

    };
    //访问props，设置当前组件的私有状态值state，或者额外的私有属性
    constructor(props){
        super(props);
        this.state = {
            date: new Date()
        }
    }
    //该方法会在组件首次渲染之前调用，这个是在render方法调用前可修改state的最后一次机会。这个方法很少用到。
    componentWillMount(){

    }
    //这个方法在首次真实的DOM渲染后调用（仅此一次）当我们需要访问真实的DOM时，这个方法就经常用到。
    // 如何访问真实的DOM这里就不想说了。当我们需要请求外部接口数据，一般都在这里处理。
    componentDidMount(){
        this.timer = setInterval(()=>{
            this.tick();
        }, 1000);
    }
    tick = () => {
        this.setState({
            date:new Date()
        })
    };
    btnClick = () => {
        console.log("btnClick");
        this.props.history.goBack();
        window.history.back();
    };
    //渲染
    render() {
        return (
            <div className="App">
                <h2>It is {this.state.date.toLocaleTimeString()}.</h2>
                <Button btnClick={this.btnClick.bind(this)}>
                    <Link to="/about">我是一个按钮，你要是点我，我就给你变颜色</Link>
                </Button>
                <Toggle/>
            </div>
        );
    }
    //当组件的props更新时会依次执行下面的函数
    //每当我们通过父组件更新子组件props时（这个也是唯一途径），这个方法就会被调用。
    componentWillReceiveProps(nextProps){

    }
    //字面意思，是否应该更新组件，默认返回true。当返回false时，后期函数就不会调用，组件不会在次渲染。
    shouldComponentUpdate(nextProps,nextState){
        return true;
    }
    //组件将会更新，props和state改变后必调用。
    componentWillUpdate(){

    }
    //render函数在周期函数实际出现的顺序
    //render(){}
    //这个方法在更新真实的DOM成功后调用，当我们需要访问真实的DOM时，这个方法就也经常用到。
    componentDidUpdate(){

    }
    //销毁阶段，只有一个函数被调用：在组件销毁的时候清除数据、定时器或者监听器等
    componentWillUnmount(){
        clearInterval(this.timer);
    }
}
