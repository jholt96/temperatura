import React from 'react';
import logo from './logo.svg';
import { BrowserRouter as Router, Link, Route } from "react-router-dom";
import './App.css';
import "bootstrap/dist/css/bootstrap.min.css";


function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
      </header>
      <Route exact path="/"><h1>Home</h1></Route>
      <Route exact path="/admin"><h1>Admin</h1></Route>
    </div>
  );
}

export default App;
