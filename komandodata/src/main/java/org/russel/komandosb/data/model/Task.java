package org.russel.komandosb.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.russel.komandosb.data.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Task {
    private Integer id;
    private String title;
    private String description;
    private Status status;
    private List<User> assignedUsers;
    private User createdBy;
    private Group group;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
