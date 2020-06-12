package edge.temperatura.temperatura.models;

import java.util.ArrayList;
import java.util.List;
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
    
    private List<String> alertsId = new ArrayList<>();


    public Trucks(){
        this._id = ObjectId.get();
        this.hostname = "";
        this.alertsId = new ArrayList<>();
        this.env = "";
    }

    public Trucks(String hostname, String env) {
        this._id = ObjectId.get();
        this.hostname = hostname;
        this.env = env;
        this.alertsId = new ArrayList<>();

    }


    public void addAlert(ObjectId alertId){

        alertsId.add(alertId.toString());
    }

    public Iterable<String> getIterableAlertsIds() {
        return alertsId;
    }
    public void clearAlerts(){
        alertsId.clear();
    }
}