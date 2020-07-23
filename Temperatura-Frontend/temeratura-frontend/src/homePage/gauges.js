import React, { Component } from "react";
import PropTypes from 'prop-types';
import JustGage from 'justgage'

import Truck from '../Classes/Truck';



export default class Gauge extends Component {

    constructor(props) {
        super(props);
    }

    componentDidMount() {

        this.tempGauge = new JustGage({
          id: this.props.key + 'temperature',
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
            id: this.props.key + 'humidity',
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

    componentWillReceiveProps(nextProps){

        if(this.props.truck.currentTemperature !== nextProps.truck.currentTemperature 
                                        || this.props.truck.temperatureCeilingThreshold !== nextProps.truck.temperatureCeilingThreshold 
                                        || this.props.truck.temperatureFloorThreshold !== nextProps.truck.temperatureFloorThreshold ) {

            this.tempGauge.refresh(nextProps.truck.currentTemperature,nextProps.truck.temperatureCeilingThreshold,nextProps.truck.temperatureFloorThreshold);
        }

        if(this.props.truck.currentHumidity !== nextProps.truck.currentHumidity 
                                        || this.props.truck.humidityCeilingThreshold !== nextProps.truck.humidityCeilingThreshold 
                                        || this.props.truck.humidityFloorThreshold !== nextProps.truck.humidityFloorThreshold ) {

            this.tempGauge.refresh(nextProps.truck.currentHumidity,nextProps.truck.humidityCeilingThreshold,nextProps.truck.humidityFloorThreshold);
        }

    }

    render(){
        return (
        <div id= {this.props.key} style= {{width: "23%", margin: "0 auto", display: "inline-block", border: "solid black 2px"}}>
            <h1>{this.props.truck.hostname}</h1>
            <div id = {this.props.key + 'temperature'}></div>
            <div id = {this.props.key + 'humidity'}></div>
            <p>{this.props.truck.timestamp}</p>
            <p>{this.props.truck.env}</p>
        </div>
        );
    }
}
