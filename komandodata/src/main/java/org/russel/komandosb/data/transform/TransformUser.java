package org.russel.komandosb.data.transform;

import org.russel.komandosb.data.entity.UserData;
import org.russel.komandosb.data.model.User;

public class TransformUser {
    public static User toDTO(UserData data) {
        if (data == null) return null;
        User user = new User();
        user.setId(data.getId());
        user.setFirstName(data.getFirstName());
        user.setLastName(data.getLastName());
        user.setUsername(data.getUsername());
        return user;
    }

    public static UserData toEntity(User dto) {
        if (dto == null) return null;
        UserData userData = new UserData();
        userData.setId(dto.getId());
        userData.setFirstName(dto.getFirstName());
        userData.setLastName(dto.getLastName());
        userData.setUsername(dto.getUsername());
        userData.setPassword(dto.getPassword());
        return userData;
    }
}
