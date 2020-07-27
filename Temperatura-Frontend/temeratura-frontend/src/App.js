import React from 'react';
import { BrowserRouter as Router, Switch, Route} from "react-router-dom";
import './App.css';
import "bootstrap/dist/css/bootstrap.min.css";
import HomePage from './Components/HomePageComponent'
import Login from './Components/loginComponent'
import PrivateRoute from './PrivateRoute'
import authService from './Services/authService';

function App() {
  var user = authService.getCurrentUser();

  return (
    <Router>
      <div className="App">
        <Switch>
          <PrivateRoute exact path= {["/", "/home"]}> <HomePage user = {user}/> </PrivateRoute> 
          <Route exact path="/login" component={Login}></Route>
          <PrivateRoute exact path = "/admin"><h1>Admin</h1> </PrivateRoute>
          <PrivateRoute exact path="/profile"><h1>Profile</h1> </PrivateRoute>
        </Switch>

      </div>
    </Router>
    );
}

export default App;



