package org.russel.komandosb.data.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "groupdata")
public class GroupData {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private UserData createdBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "group_users",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "user_id"})
    )
    private Set<UserData> users = new HashSet<>();

    @OneToMany(mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<TaskData> tasks = new HashSet<>();

    @CreationTimestamp
    @JsonFormat(pattern = "MM dd, yyyy - hh:mm a", timezone = "GMT+08:00")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(pattern = "MM dd, yyyy - hh:mm a", timezone = "GMT+08:00")
    private LocalDateTime updatedAt;

}
