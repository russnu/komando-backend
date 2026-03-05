package org.russel.komandosb.serviceimpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.russel.komandosb.data.entity.UserData;
import org.russel.komandosb.data.model.Group;
import org.russel.komandosb.data.model.User;
import org.russel.komandosb.data.repository.UserRepository;
import org.russel.komandosb.data.service.UserService;
import org.russel.komandosb.data.transform.TransformUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    //===================================================================================================//
    @Override
    public List<User> getAll() {
        try {
            return repository.findAll()
                    .stream()
                    .map(TransformUser::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("ERROR RETRIEVING USERS: \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public User get(Integer id) {
        UserData data = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("USER NOT FOUND."));
        try {
            return TransformUser.toDTO(data);
        } catch (Exception e) {
            throw new RuntimeException("ERROR RETRIEVING USER WITH ID [" + id + "]: \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public User create(User user) {
        try {
            // Hash the password before saving
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            UserData saved = repository.save(TransformUser.toEntity(user));
            return TransformUser.toDTO(saved);
        } catch (Exception e) {
            throw new RuntimeException("ERROR CREATING USER: \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public User update(Integer id, User user) {
        UserData existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("USER WITH ID [\" + id + \"] NOT FOUND."));
        try {
            existing.setFirstName(user.getFirstName());
            existing.setLastName(user.getLastName());
            repository.save(existing);
            return TransformUser.toDTO(existing);
        } catch (Exception e) {
            throw new RuntimeException("ERROR UPDATING USER WITH ID [\" + id + \"]: \n", e);
        }

    }
    //===================================================================================================//
    @Override
    public void delete(Integer id) {
        repository.findById(id).orElseThrow(() -> new EntityNotFoundException("USER NOT FOUND."));
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("ERROR DELETING USER WITH ID [\" + id + \"]: \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public List<Group> getGroups(Integer userId) {
        return List.of();
    }
}
