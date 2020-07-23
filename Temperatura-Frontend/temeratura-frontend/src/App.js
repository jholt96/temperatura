import React from 'react';
import logo from './logo.svg';
import { BrowserRouter as Router, Link, Route } from "react-router-dom";
import './App.css';
import "bootstrap/dist/css/bootstrap.min.css";
import HomePage from './Components/HomePageComponent'


function App() {
  var user = {token:"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzaWtqaXNoQGxpdmUuY29tIiwiaWF0IjoxNTk1NTI2NjI2LCJleHAiOjE1OTU2MTMwMjZ9.jnnCN6gmrsRjFDuT77QkpVAXIrgXAoVkrWccnXIP0Lf20RRVpcbCA4vIHOfMX_FVXXlCsgjkOgxDMGTTYAv1jw",type:"Bearer",username:"sikjish@live.com",roles:["ROLE_VIEWER","ROLE_ADMIN"]}
  localStorage.setItem('user',JSON.stringify(user));

return (
    <div className="App">
      <HomePage user={user}/>
      {//<Route exact path="/"><h1>Home</h1></Route>
      //<Route exact path="/admin"><h1>Admin</h1></Route>
      }

    </div>
  );
}

export default App;
