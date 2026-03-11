package org.russel.komandocore.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskNotification {
    private String title;
    private String body;
    private String topic;
    private List<String> tokens;
    private Map<String, String> data;

}
