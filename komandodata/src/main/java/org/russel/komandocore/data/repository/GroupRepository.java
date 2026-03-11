package org.russel.komandocore.data.repository;

import org.russel.komandocore.data.entity.GroupData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<GroupData, Integer> {
    @Query("""
            SELECT g.id,
               g.name,
               COUNT(DISTINCT u.id),
               COUNT(DISTINCT t.id)
            FROM GroupData g
            LEFT JOIN g.users u
            LEFT JOIN TaskData t ON t.group.id = g.id
            GROUP BY g.id, g.name""")
    List<Object[]> findAllWithCounts();

    @Query("""
        SELECT g.id,
               g.name,
               g.createdBy.id,
               g.createdBy.firstName,
               g.createdBy.lastName,
               COUNT(DISTINCT u2.id),
               COUNT(DISTINCT t.id)
        FROM GroupData g
        JOIN g.users u
        LEFT JOIN g.users u2
        LEFT JOIN TaskData t ON t.group.id = g.id
        WHERE u.id = :userId
        GROUP BY g.id, g.name, g.createdBy.id, g.createdBy.firstName, g.createdBy.lastName
       """)
    List<Object[]> findByUserIdWithCounts(@Param("userId") Integer userId);
}
