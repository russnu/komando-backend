package org.russel.komandosb.data.repository;

import org.russel.komandosb.data.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserData, Integer> {
    Optional<UserData> findByUsername(String username);
}
