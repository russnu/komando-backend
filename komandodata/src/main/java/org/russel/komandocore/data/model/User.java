package org.russel.komandocore.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    public String getFullName() {
        return (firstName + " " + lastName).trim();
    }
}
