import React, { Component } from 'react';
import { BrowserRouter as Router, Switch, Route, Link, Redirect} from "react-router-dom";
import './App.css';
import "bootstrap/dist/css/bootstrap.min.css";
import HomePage from './Components/HomePageComponent';
import Login from './Components/loginComponent';
import PrivateRoute from './PrivateRoute';
import authService from './Services/authService';
import NavBar from './Components/NavBarComponent';

export default class App extends Component{

  constructor(props) {
    super(props);

    this.state= {
      user:null
    };

    this.handleLogin = this.handleLogin.bind(this);
  }

  handleLogin(user){
      this.setState({
        user:user
      });
  }

  render(){

    var login = this.state.user === null ? (<Route exact path="/login" render={(props) => <Login {...props} handleLogin={this.handleLogin} />}/>) : (<Redirect to= "/"/>)

    return (
      <Router>
        <div className="App">
          <NavBar user={this.state.user}/>
          <Switch>
            <PrivateRoute exact path= {["/", "/home"]} component={HomePage} user={this.state.user}></PrivateRoute> 
            {login}
            <PrivateRoute exact path = "/admin" user={this.state.user}><h1>Admin</h1> </PrivateRoute>
            <PrivateRoute exact path="/profile" user={this.state.user}><h1>Profile</h1> </PrivateRoute>
          </Switch>

        </div>
      </Router>
      );    
  }
}


