package nus.edu.sg.iss.app.workshop22.repository;

import static nus.edu.sg.iss.app.workshop22.repository.DBQueries.*;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import nus.edu.sg.iss.app.workshop22.model.Rsvp;

@Repository
public class RsvpRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /*
     * Fetch all rsvp
     */

    public List<Rsvp> getAllRsvp(){
        List<Rsvp> rsvps = new ArrayList<Rsvp>();
        SqlRowSet rs = null;
        rs = jdbcTemplate.queryForRowSet(SELECT_ALL_RSVP);

        while (rs.next()){
            rsvps.add(Rsvp.create(rs));
        }
        return rsvps;
    }

    public List<Rsvp> getRsvpByName(String name) {
        List<Rsvp> rsvp = new ArrayList<Rsvp>();
        SqlRowSet rs = null;
        rs = jdbcTemplate.queryForRowSet(SELECT_RSVP_BY_NAME, new Object[] {"%" + name + "%"});

        while (rs.next()){
            rsvp.add(Rsvp.create(rs));
        }
        return rsvp;
    }

    private Rsvp getRsvpByEmail(String email) {
        List<Rsvp> rsvpList = new ArrayList<Rsvp>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_RSVP_BY_EMAIL, email);

        while (rs.next()){
            rsvpList.add(Rsvp.create(rs));
        }
        return rsvpList.get(0);
    }

    public Rsvp createRsvp(Rsvp rsvp){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Rsvp existingRsvp = getRsvpByEmail(rsvp.getEmail());
        
        if(Objects.isNull(existingRsvp)){
            //insert record
            jdbcTemplate.update(conn->{
                        PreparedStatement statement = conn.prepareStatement
                        (INSERT_NEW_RSVP, Statement.RETURN_GENERATED_KEYS);
                        statement.setString(1, rsvp.getName());
                        statement.setString(2, rsvp.getEmail());
                        statement.setString(3, rsvp.getPhone());
                        statement.setTimestamp(4, new Timestamp(rsvp.getConfirmationDate().toDateTime().getMillis()));
                        statement.setString(5,rsvp.getComments());
                        return statement;
                        }, keyHolder);

            BigInteger primaryKey = (BigInteger) keyHolder.getKey();

            rsvp.setId(primaryKey.intValue());
            
        } else{ //update existing record 

            existingRsvp.setName(rsvp.getName());
            existingRsvp.setPhone(rsvp.getPhone());
            existingRsvp.setConfirmationDate(rsvp.getConfirmationDate());
            existingRsvp.setComments(rsvp.getComments());
            boolean isUpdated = updateRsvp(existingRsvp);

            if(isUpdated){
                rsvp.setId(existingRsvp.getId());
            }
        }
        return rsvp;
    }

    public boolean updateRsvp(Rsvp existingRsvp){
        return jdbcTemplate.update(UPDATE_RSVP_BY_EMAIL,
                            existingRsvp.getName(),
                            existingRsvp.getPhone(),
                            new Timestamp(existingRsvp.getConfirmationDate().toDateTime().getMillis()),
                            existingRsvp.getComments(),
                            existingRsvp.getEmail())>0;   
    }

    public Long getTatolRsvpCount(){
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(TOTAL_RSVP_COUNT);
        return (Long) rows.get(0).get("total_count");
    }

}
