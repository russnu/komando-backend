package org.russel.komandocore.data.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "devices")
public class UserDeviceData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fcmToken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserData user;
}
