package by.task.dao.impl;

import by.task.dao.Dao;
import by.task.models.User;
import by.task.util.TestHibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(Lifecycle.PER_CLASS)
class UserDaoTest {
    private SessionFactory sessionFactory;
    private Dao<User, Long> userDao;

    @BeforeAll
    void setup() {
        sessionFactory = TestHibernateUtil.buildSessionFactory();
        userDao = new UserDao(sessionFactory);
    }

    @AfterAll
    void tearDown() {
        sessionFactory.close();
        TestHibernateUtil.shutdown();
    }

    @BeforeEach
    void clearDatabase() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY CASCADE").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldSaveUserAndRetrieveItById_WhenUserIsValid() {
        User user = new User("Test User", "user@test.com", 30);

        userDao.save(user);
        Optional<User> found = userDao.findById(user.getId());

        assertTrue(found.isPresent());
        assertEquals("Test User", found.get().getName());
        assertEquals("user@test.com", found.get().getEmail());
        assertEquals(30, found.get().getAge());
        assertNotNull(found.get().getCreatedAt());
    }

    @Test
    void shouldReturnAllUsers_WhenMultipleUsersExist() {
        User user1 = new User("User1", "user1@test.com", 20);
        User user2 = new User("User2", "user2@test.com", 30);

        userDao.save(user1);
        userDao.save(user2);

        List<User> users = userDao.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void shouldUpdateUserDetails_WhenNewValuesAreValid() {
        User user = new User("Original", "original@test.com", 20);
        userDao.save(user);

        user.setName("Updated");
        user.setEmail("update@test.com");
        user.setAge(25);

        userDao.update(user);

        Optional<User> updated = userDao.findById(user.getId());
        assertTrue(updated.isPresent());
        assertEquals("Updated", updated.get().getName());
        assertEquals("update@test.com", updated.get().getEmail());
        assertEquals(25, updated.get().getAge());
    }

    @Test
    void shouldDeleteUser_WhenUserExists() {
        User user = new User("ToDelete", "delete@test.com", 40);
        userDao.save(user);

        assertTrue(userDao.findById(user.getId()).isPresent());
        userDao.delete(user);

        Optional<User> deleted = userDao.findById(user.getId());
        assertFalse(deleted.isPresent());
    }

    @Test
    void shouldReturnEmptyOptional_WhenUserNotFoundById() {
        Optional<User> result = userDao.findById(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSetCreatedAtAutomatically_WhenNewUserIsSaved() {
        LocalDateTime beforeTest = LocalDateTime.now().minusSeconds(1);
        User user = new User("Timestamp", "timestamp@test.com", 35);

        userDao.save(user);
        Optional<User> found = userDao.findById(user.getId());

        assertTrue(found.isPresent());
        assertNotNull(found.get().getCreatedAt());
        assertTrue(found.get().getCreatedAt().isAfter(beforeTest));
    }
}