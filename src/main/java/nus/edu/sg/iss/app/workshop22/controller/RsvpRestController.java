package nus.edu.sg.iss.app.workshop22.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import nus.edu.sg.iss.app.workshop22.model.Rsvp;
import nus.edu.sg.iss.app.workshop22.repository.RsvpRepo;

@RestController
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class RsvpRestController {

    @Autowired
    RsvpRepo repo;
    
    /*
     * fetch all rsvp
     */
    @GetMapping("/rsvps")
    public ResponseEntity<String> getAllRsvps(){
        
        List<Rsvp> rsvps = repo.getAllRsvp();

        //convert the list into Json. since it is a list, convert into Json array
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for(Rsvp r : rsvps) {
            arrayBuilder.add(r.toJson());
        }
        JsonArray result = arrayBuilder.build();

        return ResponseEntity.status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(result.toString());
    }

    /*
     * fetch rsvp by name 
     */
    @GetMapping("/rsvp")
    public ResponseEntity<String> getRsvpByName(@RequestParam String name){
        
        List<Rsvp> rsvp = repo.getRsvpByName(name);

        //convert the list into Json. since it is a list, convert into Json array
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for(Rsvp r : rsvp) {
            arrayBuilder.add(r.toJson());
        }
        JsonArray result = arrayBuilder.build();

        //to handle 404 error
        if(rsvp.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body("{'error_code' :" + HttpStatus.NOT_FOUND +"}");
        }

        return ResponseEntity.status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(result.toString());
    }

    @PostMapping(path = "/rsvp", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> insertUpdatedRsvp(@RequestBody String json){

        Rsvp rsvp = null;
        JsonObject jsonObject = null;
        try{
            rsvp = Rsvp.create(json);
        } catch(Exception e){
            e.printStackTrace();
            jsonObject = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return ResponseEntity.badRequest().body(jsonObject.toString());
        }

        Rsvp result = repo.createRsvp(rsvp);

        jsonObject = Json.createObjectBuilder()
                                    .add("rsvpID", result.getId())
                                    .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(jsonObject.toString());
    }

    @PutMapping(path="/rsvp", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> putRsvp(@RequestBody String json){
        Rsvp rsvp = null;
        boolean rsvpResult = false;
        JsonObject resp;
        try{
            rsvp = Rsvp.create(json);
        } catch(Exception e){
            e.printStackTrace();
            resp = Json.createObjectBuilder()
                        .add("error: ", e.getMessage())
                        .build();
            return ResponseEntity.badRequest().body(resp.toString());
        }
        rsvpResult = repo.updateRsvp(rsvp);
        resp = Json.createObjectBuilder()
                    .add("updated", rsvpResult)
                    .build();

        return ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(resp.toString());
    }

    @GetMapping(path="/rsvps/count")
    public ResponseEntity<String> getTotalRsvpCounts(){
        JsonObject resp;
        Long total_rsvps = repo.getTatolRsvpCount();

        resp = Json.createObjectBuilder().add("total_count", total_rsvps)
                            .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(resp.toString());
    }

}
