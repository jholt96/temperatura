import axios from 'axios';

import jwt_decode from 'jwt-decode';

const API_URL = "http://localhost:8080/management/api/v1/users";

class AuthService {
    login(username,password){
        return axios.post(API_URL + '/login',
        {
            'username': username, 'password':password
        }).then(response => {
            if(response.data.token){
                localStorage.setItem("user",JSON.stringify(response.data));
            }
            return response.data;
        });
    }

    logout() {
        localStorage.removeItem('user');
    }

    getCurrentUser() {
        
        var user = JSON.parse(localStorage.getItem('user'));
        var returnVal = null;


        if(user){
            var tokenDecoded = jwt_decode(user.token.slice(6));
            var current_time = Date.now() / 1000;

            if(tokenDecoded.exp < current_time) {
                console.log("token is expired");
                returnVal = null;
            }else{
                returnVal = user;
            }
        }
        else{
            returnVal = null;
        }

        return returnVal;
    }
}


export default new AuthService();