import { useReducer } from "react";

export default function getAuthHeader() {
    const user = JSON.parse(localStorage.getItem('user'));



    if(user && user.token){
        return 'Bearer ' + user.token;
    }else {
        return {};
    }
}