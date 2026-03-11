package org.russel.komandocore.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Group {
    private Integer id;
    private String name;
    private User createdBy;
    private List<User> users;
    private List<Task> tasks;
    private Integer userCount;
    private Integer taskCount;
}
