import axios from 'axios';

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
        return JSON.parse(localStorage.getItem('user'));
    }
}

export function getAuthHeader() {
        const user = localStorage.getItem('user');

        if(user && user.token){
            return 'Authorization: Bearer ' + user.token;
        }else {
            return {};
        }
}

export default new AuthService();