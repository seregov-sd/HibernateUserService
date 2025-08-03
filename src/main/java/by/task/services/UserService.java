package by.task.services;

import by.task.dao.Dao;
import by.task.dao.impl.UserDao;
import by.task.models.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final Dao<User, Long> userDao = new UserDao();

    public void saveUser(User user) {
        userDao.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void updateUser(User user) {
        userDao.update(user);
    }

    public void deleteUser(User user) {
        userDao.delete(user);
    }
}