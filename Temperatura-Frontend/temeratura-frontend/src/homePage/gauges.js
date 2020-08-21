import React, { Component } from "react";
import PropTypes from 'prop-types';
import JustGage from 'justgage';

import '../css/gauges.css'




export default class Gauge extends Component {

    componentDidMount() {

        this.tempGauge = new JustGage({
          id: this.props.truckId + 'temperature',
          label: 'Temperature',
          min: 0,
          max: 0,
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
            min: 20,
            max: 80,
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
            this.tempGauge.refresh(this.props.truck.currentTemperature,this.props.truck.temperatureCeilingThreshold,this.props.truck.temperatureFloorThreshold);

            this.humidityGauge.refresh(this.props.truck.currentHumidity,this.props.truck.humidityCeilingThreshold,this.props.truck.humidityFloorThreshold);
    }

    render(){
        return (
        <div className = "gaugeDiv" id= {this.props.truckId}>
            <h1 className = "gaugeTitle">{this.props.truck.hostname}</h1>
            <div id = {this.props.truckId + 'temperature'}></div>
            <div id = {this.props.truckId + 'humidity'}></div>
            <p className = "gaugeTime">{this.props.truck.timestamp}</p>
            <p>{this.props.truck.env}</p>
        </div>
        );
    }
}
