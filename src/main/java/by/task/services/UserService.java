package by.task.services;

import by.task.dao.Dao;
import by.task.dao.impl.UserDao;
import by.task.models.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final Dao<User, Long> userDao = new UserDao();

    public void saveUser(User user) {
        validateUser(user);
        userDao.save(user);
    }

    public Optional<User> getUserById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Некорректный ID пользователя");
        }
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            System.out.println("В системе пока нет пользователей");
        }
        return users;
    }

    public void updateUser(User user) {
        validateUser(user);
        if (userNotExist(user.getId())) {
            throw new IllegalArgumentException("Пользователь с ID " + user.getId() + " не найден");
        }
        userDao.update(user);
    }

    public void deleteUser(User user) {
        if (userNotExist(user.getId())) {
            throw new IllegalArgumentException("Пользователь с ID " + user.getId() + " не найден");
        }
        userDao.delete(user);
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не может быть null");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя обязательно");
        }
    }

    private boolean userNotExist(Long id) {
        return id == null || userDao.findById(id).isEmpty();
    }
}