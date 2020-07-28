import React, { Component } from "react";
import {Link} from "react-router-dom";
import Button from 'react-bootstrap/Button';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import { NavItem } from "react-bootstrap";




export default class NavBar extends Component {

    handleLogout(){
        localStorage.removeItem('user');
        window.location.reload();
    }

    render() {
        var user = localStorage.getItem('user');
        var loggedIn;

        if(user) {
            loggedIn = (
            <Nav.Link style={{float:"right"}} onClick={() => {this.handleLogout()}}>Logout</Nav.Link>);
        }else{
            loggedIn = (<Nav.Link style={{float:"right"}} to="/login">Login</Nav.Link>);
        }

        return (
            <Navbar  bg="dark" expand="lg" variant="dark">
                <Navbar.Brand href="/home">Temperatura</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />  
                <Link to="/">Home</Link>
                <Nav className="justify-content-end">{loggedIn}</Nav>
            </Navbar>
            
        );
    }


}