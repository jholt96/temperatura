Temperatura Project

Node-red Container running on a raspberry pi -> Kafka -> Spring boot backend with Mongodb -> React Frontend


Started as an end to end Demo to showcase Edge Computing and kafka. 

A node-red container is running on a raspberry pi with a temperature sensor attached to it. 

Multiple raspberry pi's are streaming data into a kafka topic and then this spring boot app is consuming those messages. 

It sees which truck is sending in the data, if it is over the threshold, and then sending the relevant data into the react frontend(to be built). Each Raspberry pi can change what its temperature threshold is so that if it is over that threshold for 2 minutes it will send an alert into the frontend. 

It exposes some apis that allows the frontend to see a list of all trucks, get 1 truck, and then sees the historical alerts for a particular truck. 


What is coming next?

An Actual README
