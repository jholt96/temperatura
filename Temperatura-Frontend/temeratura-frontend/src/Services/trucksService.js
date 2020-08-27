import axios from 'axios';
import getAuthHeader from './authHeader';

const API_URL = "http://localhost:8080/api/v1/trucks";

class TrucksService {

    getAllTrucks() {
        var config = { headers: {"Authorization" : getAuthHeader()}};
        return axios.get(API_URL + '/', config ).then((res) =>
        {
            return res;
        
        },(error) => {
            console.log(error); 
            return error;
        });
    }

    getOneTruck(hostname) {
        var config = { headers: {"Authorization" : getAuthHeader()}};
        return axios.get(API_URL + '/' + hostname, config ).then((res) => {
            return res;
        },(error)=>{
            return error;
        });

    }
    clearAlerts(hostname) {
        var config = { headers: {"Authorization" : getAuthHeader()}};
        return axios.delete(API_URL + '/' + hostname + "/alerts/clear", config ).then((res) => {
            return res;
        },(error)=>{
            return error;
        });
    }

    getAlerts(hostname) {
        var config = { headers: {"Authorization" : getAuthHeader()}};
        return axios.get(API_URL + '/' + hostname + "/alerts", config ).then((res) => {
            return res;
        },(error)=>{
            return error;
        });
    }
}

export default new TrucksService();