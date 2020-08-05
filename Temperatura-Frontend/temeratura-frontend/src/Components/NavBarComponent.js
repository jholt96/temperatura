import React, { Component } from "react";
import {Link} from "react-router-dom";
import Button from 'react-bootstrap/Button';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import { NavItem } from "react-bootstrap";




export default class NavBar extends Component {

    handleLogout() {
        localStorage.removeItem('user');
        window.location.reload();
    }

    render() {
        var user = localStorage.getItem('user');
        var loggedIn;

        if(user) {
            loggedIn = (
            <Nav.Link onClick={() => {this.handleLogout()}}>Logout</Nav.Link>);
        }else{
            loggedIn = (<Nav.Link id="loginNavBar" to="/login">Login</Nav.Link>);
        }

        return (
            <Navbar fixed="top" bg="dark" expand="lg" variant="dark">
                <Nav>
                    <Navbar.Brand href="/home">Temperatura</Navbar.Brand>
                </Nav>
                <Nav className="mr-auto">
                    <Nav.Link  to="/">Home</Nav.Link>
                    {loggedIn}
                </Nav>

            </Navbar>
            
        );
    }


}