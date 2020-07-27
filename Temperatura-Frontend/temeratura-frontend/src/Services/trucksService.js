import axios from 'axios';
import getAuthHeader from './Services/authHeader';
import Truck from '../Classes/Truck';


class TrucksService {

    getAllTrucks() {
        var config = { headers: {"Authorization" : getAuthHeader()}};
        axios.get(API_URL + '/', config ).then((res) =>{
            var newTrucks = [];
            
            res.data.forEach(truck => {
                newTrucks.push(new Truck(truck));
                this.truckMap.set(truck.hostname,(newTrucks.length - 1))
            });
            this.setState({trucks:newTrucks})
        },(res) => {
            console.log("error");
        });
    }

    getOneTruck(hostname) {
        var config = { headers: {"Authorization" : getAuthHeader()}};
        axios.get(API_URL + '/' + hostname, config ).then((res) =>{
            var newTruck = res.data;  
            this.setState({trucks:newTrucks})
        },(res) => {
            console.log("error");
        });

    }
    getAlerts(hostname) {

    }
}