package edge.temperatura.temperatura.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "trucks" )
public class Trucks {

    @Id
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter    
    private String hostname;

    @Getter
    @Setter   
    private String env;
    
    @Getter
    @Setter
    private Map<String,Date> alerts = new HashMap<>();

    public Trucks(){}

    public Trucks(String id, String hostname, String env){
        this.id = id;
        this.hostname = hostname;
        this.env = env;
    }

    
}