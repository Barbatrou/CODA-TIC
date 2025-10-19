package coda.tic.cobaye_api.repository;

import coda.tic.cobaye_api.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {
    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(generateId());
        }
        users.put(user.getId(), user);
        return user;
    }

    public void deleteById(Integer id) {
        users.remove(id);
    }

    public boolean existsById(Integer id) {
        return users.containsKey(id);
    }

    private Integer generateId() {
        return users.keySet().stream()
                .max(Integer::compareTo)
                .map(id -> id + 1)
                .orElse(1);
    }
}
