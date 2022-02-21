package user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @JsonProperty(value = "Username")
    private String username;
    @JsonProperty(value = "Password")
    private String password;


}


