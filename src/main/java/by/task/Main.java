package by.task;

import by.task.dao.Dao;
import by.task.dao.impl.UserDao;
import by.task.models.User;
import by.task.services.UserMenuManager;
import by.task.util.HibernateUtil;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Dao<User, Long> userDao = new UserDao();
    private static final UserMenuManager menuManager = new UserMenuManager(scanner, userDao);

    public static void main(String[] args) {
        menuManager.run();
        HibernateUtil.shutdown();
    }
}