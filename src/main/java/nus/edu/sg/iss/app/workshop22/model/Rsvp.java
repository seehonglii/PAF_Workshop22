package nus.edu.sg.iss.app.workshop22.model;

import java.io.StringReader;
import java.time.Instant;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

public class Rsvp {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private DateTime confirmationDate;
    private String comments;
    
    public Rsvp() {
    }

    public Rsvp(Integer id, String name, String email, String phone, DateTime confirmationDate, String comments) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.confirmationDate = confirmationDate;
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public DateTime getConfirmationDate() {
        return confirmationDate;
    }
    public void setConfirmationDate(DateTime confirmationDate) {
        this.confirmationDate = confirmationDate;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Rsvp [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", confirmationDate="
                + confirmationDate + ", comments=" + comments + "]";
    }

    public static Rsvp create(SqlRowSet rs) {
        Rsvp rsvp = new Rsvp();

        rsvp.setId(rs.getInt("id"));
        rsvp.setName(rs.getString("name"));
        rsvp.setEmail(rs.getString("email"));
        rsvp.setPhone(rs.getString("phone"));      
        rsvp.setConfirmationDate(new DateTime(DateTime.parse(rs.getString("confirmation_date"))));
        rsvp.setComments(rs.getString("comments"));
        
        return rsvp;
    }

    public JsonValue toJson() {
        return Json.createObjectBuilder().add("id", getId())
                                            .add("name", getName())
                                            .add("email", getEmail())
                                            .add("phone", getPhone())
                                            .add("confirmation_date", getConfirmationDate().toString(DateTimeFormat.forPattern("dd-MM-yyyy")))
                                            .add("comments", getComments())
                                            .build();
    }

    public static Rsvp create(String json) {

        JsonObject jsonObject = Json.createReader(new StringReader(json)).readObject();
        
        String name = jsonObject.getString("name");
        String email = jsonObject.getString("email");
        String phone = jsonObject.getString("phone");
        String confirmation_date = jsonObject.getString("confirmation_date");
        String comments = jsonObject.getString("comments");
                
        Rsvp rsvp = new Rsvp();
        rsvp.setName(name);
        rsvp.setEmail(email);
        rsvp.setPhone(phone);
        rsvp.setConfirmationDate(new DateTime(Instant.parse(confirmation_date)));
        rsvp.setComments(comments);
               
        return rsvp;
        
    }

   
        
}
