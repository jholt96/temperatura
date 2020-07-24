import React from 'react';
import logo from './logo.svg';
import { BrowserRouter as Router, Switch, Link, Route, Redirect} from "react-router-dom";
import './App.css';
import "bootstrap/dist/css/bootstrap.min.css";
import HomePage from './Components/HomePageComponent'
import Login from './Components/loginComponent'
import PrivateRoute from './PrivateRoute'

function App() {
  var user = {token:"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzaWtqaXNoQGxpdmUuY29tIiwiaWF0IjoxNTk1NjAyMDU0LCJleHAiOjE1OTU2ODg0NTR9.HTYuZxBzRxJ0BpLZWHLss7lwxZEeg6QCDobVIOmxG9ItHciSdK9L5qa2mqUF3hzexltxjiRBeSZHIR8KfYY_1A",type:"Bearer",username:"sikjish@live.com",roles:["ROLE_VIEWER","ROLE_ADMIN"]}
  localStorage.setItem('user',JSON.stringify(user));

  return (
    <Router>
      <div className="App">
        <Switch>
          <PrivateRoute exact path= {["/", "/home"]}> <HomePage user = {user}/> </PrivateRoute> 
          <PrivateRoute exact path="/login"><Login/></PrivateRoute>
          <PrivateRoute exact path = "/admin"> <HomePage user = {user} /> </PrivateRoute>
          <PrivateRoute exact path="/profile"> <HomePage user = {user}/> </PrivateRoute>
        </Switch>

      </div>
    </Router>
    );
}

export default App;



