/*
Features to implement: 

get all trucks in db create gauges.  if gets a new truck in the websocket reget all trucks and create all gauges. 
Filter based on favorites or by name. 

one of the divs gets clicked on bring up a page showing the current gauge and the alerts graph. 

If a message has an alert show an alert in the top right saying which truck has an alert and the kind of alert. 
*/
import React, { Component } from "react";
import SockJsClient from 'react-stomp';
import axios from 'axios';
import getAuthHeader from '../Services/authHeader';
import Gauge from '../homePage/gauges';
import Truck from '../Classes/Truck';



const API_URL = "http://localhost:8080/api/v1/trucks";

export default class HomePage extends Component{


    constructor(props) {
        super(props);

        this.state = {
            trucks: [],
            clientConnected: false,
            currMessage: {}
        };

        this.truckMap = new Map();

        this.componentDidMount = this.componentDidMount.bind(this);
        this.onMessage = this.onMessage.bind(this);
        this.newTrucks = this.newTrucks.bind(this);
    }

    componentDidMount() {
        //check if user object exist. if it does then get all trucks. 
        if(this.props.user) {

            var config = { headers: {"Authorization" : getAuthHeader(), "XSRF-TOKEN":"5f4b911c-f8cd-4f89-a73d-00cdf8c13184"}};
            axios.get(API_URL + '/', config ).then((res) =>{
                var newTrucks = [];
                
                res.data.forEach(truck => {
                    newTrucks.push(new Truck(truck));
                    this.truckMap.set(truck.hostname,(newTrucks.length - 1))
                });
                this.setState({trucks:newTrucks})
            },(res) => {
                //console.log(res);
            });
        }
        else {

        }


    }

    onMessage(newMessage) {

        
        if(!newMessage.alert)
        {
            var trucks = this.state.trucks;

            const indexOfTruck = this.truckMap.get(newMessage.hostname);
            if(indexOfTruck !== undefined) {
                trucks[indexOfTruck].currentTemperature = newMessage.temperature;
                trucks[indexOfTruck].currentHumidity = newMessage.humidity;
                trucks[indexOfTruck].temperatureCeilingThreshold = newMessage.temperatureCeilingThreshold; 
                trucks[indexOfTruck].temperatureFloorThreshold= newMessage.temperatureFloorThreshold; 
                trucks[indexOfTruck].humidityCeilingThreshold = newMessage.humidityCeilingThreshold; 
                trucks[indexOfTruck].humidityFloorThreshold = newMessage.humidityFloorThreshold;
                trucks[indexOfTruck].timestamp = newMessage.timestamp;

                this.setState({
                    trucks: trucks
                });
            }            
        }else{
            //alert message
        }







        //if hostname exists in truck list update the truck list with the current temp and humidity and set the state. 
        //console.log(message);
    }

    newTrucks() {

    }

    render() {
        const trucks = this.state.trucks.slice();
        const gauges = trucks.map(truck => {
            return <Gauge truck = {truck} key = {truck.hostname + 'gauges'} truckId={truck.hostname + 'gauges'}/>
        })

        return (
            <div>
                <SockJsClient url={"http://localhost:8080/edge"} topics={["/topic/edge"]}
                onMessage={ this.onMessage } ref={ (client) => { this.clientRef = client }}
                onConnect={ () => { this.setState({ clientConnected: true }) } }
                onDisconnect={ () => { this.setState({ clientConnected: false }) } }
                debug={ false }
                headers={{'Authorization': getAuthHeader()}}/>

                <div>{gauges}</div>
            </div>
        );

    }

}