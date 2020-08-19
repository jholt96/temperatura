/*
Author: Josh Holt
Temperatura Backend
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: 
This is the model for the Trucks Collection in Mongodb.
This model holds the details for each truck that comes in through kafka. 
It has a manual dbref for alerts that are assigned to a particular truck using the alerts id. 
has added methods that allow for clearing alerts list and for adding to the alerts list. 


Uses Lombok to autogenerate getters and setters
*/
package edge.temperatura.temperatura.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "trucks")
public class Trucks {

    @Id
    private ObjectId _id;

    private String hostname;

    private String env;

    public Trucks(){
        this._id = ObjectId.get();
        this.hostname = "";
        this.env = "";
    }

    public Trucks(String hostname, String env) {
        this._id = ObjectId.get();
        this.hostname = hostname;
        this.env = env;
    }
}