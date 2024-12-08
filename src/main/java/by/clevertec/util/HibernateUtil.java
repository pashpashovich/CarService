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
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;


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
    public static void initializeSearchIndexes() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            SearchSession searchSession = Search.session(session);
            searchSession.indexingPlan()
                    .purge(Review.class, null, null);
            session.flush();
        }
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

}
