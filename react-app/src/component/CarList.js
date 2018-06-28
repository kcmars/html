/**
 * Created by Administrator on 2018/6/20.
 */
import React, { Component } from 'react';
import "../style/car.css";

export default class CarList extends Component {
    constructor() {
        super();
        this.state = {
            className: 'hidden'
        }
    }
    handleScroll() {
        if (document.documentElement.scrollTop > 430) {
            this.setState({
                className: 'show'
            })
        }
    }
    componentDidMount() {
        window.onscroll = () => this.handleScroll()
    }
    render() {
        return(
            <div className="page1">
                <div className="header-title">
                    <h1>Title</h1>
                </div>
                <div className="about-wrapper">
                    <div className="about-text">
                        <div className={this.state.className}>
                            <h3>Title</h3>
                            <p>This is a text that will appear.</p>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}
