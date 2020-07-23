export default function getAuthHeader() {
    const user = localStorage.getItem('user');

    if(user && user.token){
        return 'Bearer ' + user.token;
    }else {
        return {};
    }
}