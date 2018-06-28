import React, { Component } from 'react';
import {
    BrowserRouter as Router,
    Route
} from 'react-router-dom';
import '../style/App.css';
import Home from "../component/Home";
import About from "../component/About";
import CarList from "../component/CarList";
import CityIndexListView from "../component/CityIndexListView";

export default class App extends Component {
    updateHandle() {
        console.log('每次router变化之后都会触发')
    }
    //渲染
    render() {
        return (
            <Router history={this.props.history} onUpdate={this.updateHandle.bind(this)}>
                <div>
                    <Route exact path="/" component={Home}/>
                    <Route path="/about" component={About}/>
                    <Route path="/carList" component={CarList}/>
                    <Route path="/cityIndex" component={CityIndexListView}/>
                </div>
            </Router>
        );
    }
}
