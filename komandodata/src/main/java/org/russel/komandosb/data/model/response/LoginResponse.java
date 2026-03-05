package org.russel.komandosb.data.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private Integer userId;
    private String fullname;
    private String username;
    private String message;
}
