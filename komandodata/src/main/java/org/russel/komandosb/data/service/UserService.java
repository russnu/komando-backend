package org.russel.komandosb.data.service;

import org.russel.komandosb.data.model.Group;
import org.russel.komandosb.data.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User get(Integer id);
    User create(User user);
    User update(Integer id, User user);
    void delete(Integer id);

    List<Group> getGroups(Integer userId);
}
