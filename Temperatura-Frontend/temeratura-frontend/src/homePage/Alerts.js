import React, { useState, useEffect } from 'react';
import trucksService from "../Services/trucksService";
import { Redirect } from "react-router-dom";
import CanvasJSReact from '../assets/canvasjs.react';
import { Modal } from 'react-bootstrap';
import Button from 'react-bootstrap/Button';


var CanvasJSChart = CanvasJSReact.CanvasJSChart;

function Alerts(props) {

    const [alerts,setAlerts] = useState(null);

    useEffect(() => {

        trucksService.getAlerts(props.hostname).then(res => {

            if(res.status === 200) {
                var newAlerts = [];

                res.data.forEach(alert =>{

                    newAlerts.push(alert);

                });

                setAlerts(newAlerts);
            }
            else{
                console.log(res.status);
            }

        });

    },[]);

    const handleClick = (() => {
        props.onClick();
    })

    const handleClearAlerts = (() => {

        trucksService.clearAlerts(props.hostname).then(res => {
            if(res.status === 200){
                setAlerts([]);
                handleClick();
            }else{
                alert("Not authenticated to clear alerts for this device!");
            }
        });

        
    });


    const options = {
        animationEnabled: true,
        exportEnabled: true,
        theme: "light1",
        title:{
            text: props.hostname + " Alerts"
        },
        axisY: {
            title: "Temperature",
            suffix: " °F"
        },
        axisX: {
            title: "Timestamp",
            valueFormatString: "MM-DD: hh-mm K"
        },
        data: [{
            type: "line",
            toolTipContent: "Alert: {x}: {y} °F",
            dataPoints: []
        }]
    }
    if(alerts != null){
        alerts.forEach(alert => {
            options.data[0].dataPoints.push(
                { x:new Date(alert.timestamp) , y:  alert.temperature},
            );

        });        
    }


    return (
       <Modal show={true} onHide={handleClick}>
            <Modal.Body>
                <CanvasJSChart options={options}/>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" style={{float:"left",textAlign:"left"}} onClick={handleClearAlerts}>
                    Clear Alerts
                </Button>
                <Button variant="primary" onClick={handleClick}>
                    Close
                </Button>


        </Modal.Footer>       
       </Modal>

    );

};


export default Alerts;