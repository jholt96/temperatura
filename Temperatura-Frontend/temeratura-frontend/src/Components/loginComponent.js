import React, { Component } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import { BrowserRouter as useHistory, useLocation} from "react-router-dom";
import '../css/login.css';


import AuthService  from '../Services/authService'



const required = value => {
    if (!value) {
      return (
        <div className="alert alert-danger" role="alert">
          This field is required!
        </div>
      );
    }
  };


export default class Login extends Component {

    constructor(props) {
        super(props);

        this.state = {
            username: "",
            password: "",
            loading: false,
            message: ""
        };
        this.onChangeUsername = this.onChangeUsername.bind(this);
        this.onChangePassword = this.onChangePassword.bind(this);
        this.login = this.login.bind(this);
    }

    componentDidMount() {
      if(this.props.user !== null) {
          this.props.history.push('/');
      }
    }
    componentDidUpdate(){
      if(this.props.user !== null) {
        this.props.history.push('/');
    }
    }

    onChangeUsername(e) {
        this.setState({
          username: e.target.value
        });
      }
    
      onChangePassword(e) {
        this.setState({
          password: e.target.value
        });
      }
    
      login() {
        this.setState({
          message: "",
          loading: true
        });
        AuthService.login(this.state.username,this.state.password).then((res) => {
          console.log(res);
          
          this.props.handleLogin(res);
          this.props.history.push('/');

        },error => {
            this.setState({
              message: "",
              loading: false
            });
        });
      }

      render() {
        return (
          <div className="loginForm">
            <h1>Login</h1>
            <Form>
              <div className="usernameForm">
                <label htmlFor="username">Username</label>
                <Input
                  type="text"
                  className="form-control"
                  name="username"
                  value={this.state.username}
                  onChange={this.onChangeUsername}
                  validations={[required]}
                />
              </div>

              <div className="passwordForm">
                <label htmlFor="password">Password</label>
                <Input
                  type="password"
                  className="form-control"
                  name="password"
                  value={this.state.password}
                  onChange={this.onChangePassword}
                  validations={[required]}
                />
              </div>

              <div className="loginButton">
                <button
                  className="btn btn-primary btn-block"
                  onClick={this.login}
                  disabled={this.state.loading}
                >
                  {this.state.loading && (
                    <span className="spinner-border spinner-border-sm"></span>
                  )}
                  <span>Login</span>
                </button>
              </div>
            </Form>
          </div>
        );
      }
}