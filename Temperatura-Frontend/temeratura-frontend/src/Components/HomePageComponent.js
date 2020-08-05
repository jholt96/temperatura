/*
Features to implement: 

get all trucks in db create gauges.  if gets a new truck in the websocket reget all trucks and create all gauges. 
Filter based on favorites or by name. 

one of the divs gets clicked on bring up a page showing the current gauge and the alerts graph. 

If a message has an alert show an alert in the top right saying which truck has an alert and the kind of alert. 
*/
import React, { Component } from "react";
import SockJsClient from 'react-stomp';
import getAuthHeader from '../Services/authHeader';
import Gauge from '../homePage/gauges';
import Truck from '../Classes/Truck';
import { Redirect } from "react-router-dom";
import authService from "../Services/authService";
import trucksService from "../Services/trucksService"

const WSURL = "http://localhost:8080/edge";
const TOPIC = "/topic/edge";

export default class HomePage extends Component{

    _isMounted = false;

    constructor(props) {
        super(props);

        this.state = {
            trucks: [],
            clientConnected: false,
            redirect: false
        };

        this.truckMap = new Map();
        this.clientRef = null;
        this.socket = null;
        this.stompClient = null;

        this.componentDidMount = this.componentDidMount.bind(this);
        this.onMessage = this.onMessage.bind(this);
        this.onConnect = this.onConnect.bind(this);
        this.onDisconnect = this.onConnect.bind(this);


    }

    componentWillUnmount() {
        this._isMounted = false;
        //this.clientRef.disconnect();
      }
    componentDidMount() {
        //check if user object exist. if it does then get all trucks. 
        this._isMounted = true;

        var user = authService.getCurrentUser();
        if(user) {

            trucksService.getAllTrucks().then((res) =>{
                if(res.status == 200){

                    var newTrucks = [];
                    
                    res.data.forEach(truck => {
                        newTrucks.push(new Truck(truck));
                        this.truckMap.set(truck.hostname,(newTrucks.length - 1))
                    });
                    if(this._isMounted){
                        this.setState({trucks:newTrucks,redirect:false})    
                    }
                }else{
                    console.log(res.status);
                }
  
            },(error) => {
                if(this._isMounted){
                    this.setState({
                        redirect: true
                    });
                }
                if(this.state.clientConnected){

                    //this.clientRef.disconnect();
                }
    
            });
        }
        else {
            console.log('Not Authenticated. Redirecting to login');
            if(this._isMounted){
                this.setState({
                    redirect: true
                });
            }

            if(this.state.clientConnected){

                //this.clientRef.disconnect();
            }

        }

    }

    handleError(error) {

        console.log(error);
        if(this._isMounted){
            this.setState({
                redirect: true
            })                    
        }
    }

    onConnect(){
        if(this._isMounted){
            this.setState({clientConnected:true});
        }
    }
    onDisconnect() {
        if(this._isMounted){
            this.setState({ clientConnected: false });
        }
    }


    onMessage(newMessage) {

        if(this.state.clientConnected){
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
        }


        //if hostname exists in truck list update the truck list with the current temp and humidity and set the state. 
        //console.log(message);
    }



    render() {
        const trucks = this.state.trucks.slice();
        const gauges = trucks.map(truck => {
            return <Gauge truck = {truck} key = {truck.hostname + 'gauges'} truckId={truck.hostname + 'gauges'}/>
        })
        var sockjsJsx = null;
        if(this._isMounted){
            sockjsJsx= (
                <SockJsClient url={"http://localhost:8080/edge"} 
                    topics={["/topic/edge"]}
                    onMessage={ this.onMessage } 
                    ref={ (client) => { this.clientRef = client }}
                    onConnect={ this.onConnect }
                    onDisconnect={this.onDisconnect}
                    autoReconnect={false}
                    debug={ false }
                    onConnectFailure={(error) => {this.handleError(error);}}
                    headers={{'Authorization': getAuthHeader()}}
                />
            );
        }else{
            sockjsJsx = <div></div>;
        }

        return (
            !this.state.redirect ? 
            <div>
                {sockjsJsx}
                <div>{gauges}</div>
            </div> :
            <Redirect  to={{pathname: "/login"}}/>
        );

    }
}