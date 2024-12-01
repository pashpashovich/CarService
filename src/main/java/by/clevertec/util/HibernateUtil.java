package by.clevertec.util;

import by.clevertec.entity.Car;
import by.clevertec.entity.CarShowroom;
import by.clevertec.entity.Category;
import by.clevertec.entity.Client;
import by.clevertec.entity.Review;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


@UtilityClass
public class HibernateUtil {

    @Getter
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Car.class)
                    .addAnnotatedClass(CarShowroom.class)
                    .addAnnotatedClass(Category.class)
                    .addAnnotatedClass(Client.class)
                    .addAnnotatedClass(Review.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

}
