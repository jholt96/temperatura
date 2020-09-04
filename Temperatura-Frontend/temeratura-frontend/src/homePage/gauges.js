import React, { Component } from "react";
import JustGage from 'justgage';
import Alerts from './Alerts.js';
import Button from 'react-bootstrap/Button';


import '../css/gauges.css'




export default class Gauge extends Component {

    constructor(props) {
        super(props);

        this.state = {
            popupToggle: false
        };

        this.onGaugeClick = this.onGaugeClick.bind(this);

        const refTime = React.createRef();
    }

    componentDidMount() {

        this.tempGauge = new JustGage({
          id: this.props.truckId + 'temperature',
          label: 'Temperature',
          min: 0,
          max: 100,
          counter: true,
          value: 0,
          decimals : 2,
          levelColorsGradient: false,
          labelFontColor: '#000000',
          relativeGaugeSize: true
        });

        this.humidityGauge = new JustGage({
            id: this.props.truckId + 'humidity',
            label: 'Humidity',
            min: 0,
            max: 100,
            counter: true,
            value: 0,
            decimals : 2,
            levelColorsGradient: false,
            labelFontColor: '#000000',
            relativeGaugeSize: true
          });

    }
    
    componentWillUnmount() {
        this.tempGauge.destroy();
        this.humidityGauge.destroy();
    }

    componentDidUpdate(prevProps){
            this.tempGauge.refresh(this.props.truck.currentTemperature);

            this.humidityGauge.refresh(this.props.truck.currentHumidity);
    }


    onGaugeClick() {

        this.setState({
            popupToggle: !this.state.popupToggle
        });

    }

    render(){
        var date = null;

        if(this.props.truck.timestamp === ""){
            date = new Date();
        }else{

            date = new Date(this.props.truck.timestamp);
        }
        return (
        <div className = "gaugeDiv" id= {this.props.truckId}>
            {this.state.popupToggle ? <Alerts hostname={this.props.truck.hostname} onClick={this.onGaugeClick}/>: null}

            <div>
                <h1 className = "gaugeTitle">{this.props.truck.hostname}</h1>
            </div>
            <div id = {this.props.truckId + 'temperature'}></div>
            <div id = {this.props.truckId + 'humidity'}></div>
            <div style={{display:"inline"}}>
            <p ref={this.refTime} className = "gaugeTime">{date.toLocaleString()}  </p> 
            <p className = "gaugeEnv"> ENV: {this.props.truck.env} </p>   
            <Button variant="primary" style={{ display: 'inherit', float:'right'}} onClick={this.onGaugeClick}> Alerts </Button>           
            </div>

        </div>
        );
    }
}
