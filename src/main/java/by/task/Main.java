package by.task;

import by.task.dao.Dao;
import by.task.dao.impl.UserDao;
import by.task.models.User;
import by.task.services.UserMenuManager;
import by.task.util.HibernateUtil;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Dao<User, Long> userDao = new UserDao();
        UserMenuManager menuManager = new UserMenuManager(scanner, userDao);

        menuManager.run();
        HibernateUtil.shutdown();
    }
}