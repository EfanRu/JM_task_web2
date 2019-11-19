package service;

import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class UserService {
    private static final UserService instance = new UserService();

    /* хранилище данных */
    private Map<Long, User> dataBase = Collections.synchronizedMap(new HashMap<>());
    /* счетчик id */
    private AtomicLong maxId = new AtomicLong(0);
    /* список авторизованных пользователей */
    private Map<Long, User> authMap = Collections.synchronizedMap(new HashMap<>());

    private UserService() {}

    public static UserService getInstance() {
        return instance == null ? new UserService() : instance;
    }

    public List<User> getAllUsers() {
        return new LinkedList<>(dataBase.values());
    }

    public User getUserById(Long id) {
        return dataBase.get(id);
    }

    public boolean addUser(User user) {
        if (isExistsThisUser(user)) {
            return false;
        }
        //Mb conflicts if user.id set maxId in something else place
        user.setId(maxId.incrementAndGet());
        dataBase.put(user.getId(), user);
        return true;
    }

    public void deleteAllUser() {
        dataBase.clear();
        maxId = new AtomicLong(0);
    }

    public boolean isExistsThisUser(User user) {
        for (User u : getAllUsers()) {
            if (u.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

    public List<User> getAllAuth() {
        return new LinkedList<>(authMap.values());
    }

    public boolean authUser(User user) {
        if (isUserAuthById(user.getId())) {
            return false;
        }
        authMap.put(user.getId(), user);
        return true;
    }

    public void logoutAllUsers() {
        authMap.clear();
    }

    public boolean isUserAuthById(Long id) {
        return authMap.containsKey(id);
    }
}
