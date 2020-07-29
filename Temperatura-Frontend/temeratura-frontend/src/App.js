import React, { useState, useEffect,useCallback } from 'react';
import { BrowserRouter as Router, Switch, Route, Link, Redirect} from "react-router-dom";
import './App.css';
import "bootstrap/dist/css/bootstrap.min.css";
import HomePage from './Components/HomePageComponent';
import Login from './Components/loginComponent';
import PrivateRoute from './PrivateRoute';
import authService from './Services/authService';
import NavBar from './Components/NavBarComponent';
 



function App() {
  const [user, setUser] = useState(null);

  useEffect(()=>{

    var user = authService.getCurrentUser();
    if(user) {
      setUser(user);
    }
    
  },[]);

  const handleLogin = useCallback((user) => {
        setUser(user);
    });

    return (
      <Router>
        <div className="App">
          <NavBar user={user}/>
          <Switch>
            <PrivateRoute exact path= {["/", "/home"]} component={HomePage} user={user}></PrivateRoute> 
            <Route exact path="/login" render={(props) => <Login {...props} handleLogin={handleLogin} user={user}/>}/>
            <PrivateRoute exact path = "/admin" user={user}><h1>Admin</h1> </PrivateRoute>
            <PrivateRoute exact path="/profile" user={user}><h1>Profile</h1> </PrivateRoute>
          </Switch>

        </div>
      </Router>
      );    
}


export default App;