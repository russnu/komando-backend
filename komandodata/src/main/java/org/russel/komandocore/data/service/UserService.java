package org.russel.komandocore.data.service;

import org.russel.komandocore.data.model.Group;
import org.russel.komandocore.data.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User get(Integer id);
    User create(User user);
    User update(Integer id, User user);
    void delete(Integer id);

    List<Group> getGroups(Integer userId);
}
