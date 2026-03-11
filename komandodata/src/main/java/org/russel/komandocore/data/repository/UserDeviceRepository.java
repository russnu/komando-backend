package org.russel.komandocore.data.repository;

import org.russel.komandocore.data.entity.UserData;
import org.russel.komandocore.data.entity.UserDeviceData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDeviceData, Integer> {
    Optional<UserDeviceData> findByFcmToken(String token);
    List<UserDeviceData> findByUser(UserData user);
    List<UserDeviceData> findAllByUserIdIn(Collection<Integer> userIds);
}
