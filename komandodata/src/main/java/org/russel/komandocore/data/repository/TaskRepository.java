package org.russel.komandocore.data.repository;

import org.russel.komandocore.data.entity.TaskData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskData, Integer> {
    List<TaskData> findByGroupId(Integer groupId);
    List<TaskData> findAllByAssignedUsers_Id(Integer userId);
    List<TaskData> findAllByGroup_Id(Integer groupId);
}
