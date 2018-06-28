/**
 * Created by Administrator on 2018/6/12.
 */
import React from 'react';
import '../style/button.css';

export default class Button extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            btnColor: false
        };
        // this.buttonClick = this.buttonClick.bind(this);
    }

    //阻止默认行为不能直接返回false，需要使用e.preventDefault();
    //属性函数写法，不这样写的话，用this调用该方法试，需要在constructor中绑定this，或者采用箭头函数写法，否在this是undefined
    buttonClick = () => {
        this.setState({
            btnColor: !this.state.btnColor
        });
        this.props.btnClick();
    };

    render(){
        return(
            <span className={["button", this.state.btnColor ? "buttonColor" : null].join(' ')} onClick={this.buttonClick}>
                {this.props.children && this.props.children}
            </span>
        )
    }
}