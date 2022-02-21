package Requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dbManagement.DbManagement;
import com.fasterxml.jackson.annotation.JsonProperty;
import user.User;

import java.sql.SQLException;

public class ReqHandler {
    ObjectMapper oMap = new ObjectMapper();
    DbManagement db = new DbManagement();

    public User mapUser(Request req)
    {
        User currentUser = new User();
        try
        {
            currentUser = oMap.readValue(req.getContent(), User.class);
        }
        catch (JsonMappingException e) { e.printStackTrace(); return null; }
        catch (JsonProcessingException e) { System.out.println("SOMETHING WENT WRONG WITH USER\n"); return null; }

        return currentUser;
    }

    public ReqHandler(Request req) throws JsonProcessingException, SQLException {
        switch(req.getMethod())
        {
            case "GET":
            {
                System.out.println("GET FUNKTIONIERT\n");
                break;
            }
            case "POST":
                User r;
                if ("/users".equals(req.getRoute()))
                {
                    if ((r = this.mapUser(req)) != null)
                        db.addUser(r);
                }

                if ("/sessions".equals(req.getRoute()))
                {
                    User currentUser = null;
                    if ((r = this.mapUser(req)) != null)
                        currentUser = db.login(r);

                    if (currentUser != null)
                    {
                        System.out.println("Login successful. Welcome " + currentUser.getUsername());
                    }
                    else
                        System.out.println("Either username or password is wrong.\n");
                }
                break;
            case "PUT":
            {
                System.out.println("PUT WORKS\n");
                break;
            }

            case "DELETE":
            {
                System.out.println("DELETE WORKS\n");
                break;
            }
        }
    }
}
