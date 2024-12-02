package by.clevertec;

import by.clevertec.dao.CarDAO;
import by.clevertec.dao.CategoryDAO;
import by.clevertec.entity.Car;
import by.clevertec.entity.Category;
import by.clevertec.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Main {
    public static void main(String[] args) {
        CarDAO carDAO = new CarDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        Category category = Category.builder()
                .name("Минивен")
                .build();
        categoryDAO.save(category);
        Car car = Car.builder()
                .brand("Nissan")
                .model("Qashqai")
                .year(2021)
                .price(202)
                .category(category)
                .build();
        carDAO.save(car);
//        Client client = Client.builder()
//                .name("Иван Иванов")
//                .registrationDate(LocalDate.now())
//                .build();
//        client.getContacts().put("phone", "+375291234567");
//        client.getContacts().put("email", "ivanov@example.com");
//        ClientDAO clientDAO = new ClientDAO();
//        CarDAO carDAO = new CarDAO();
//        CategoryDAO categoryDAO = new CategoryDAO();
//        ReviewDAO reviewDAO = new ReviewDAO();
//        clientDAO.save(client);
//        Category category = Category.builder()
//                .name("Минивен")
//                .build();
//        categoryDAO.save(category);
//        Car car = Car.builder()
//                .brand("Nissan")
//                .model("Qashqai")
//                .year(2021)
//                .price(202)
//                .category(category)
//                .build();
//        carDAO.save(car);
//        ReviewService reviewService = new ReviewService(reviewDAO,carDAO,clientDAO);
//        reviewService.addReview(client.getId(),car.getId(),"Классная машинка",9);
//        List<Review> reviews = reviewService.searchReviewsByRating(9);
//        System.out.println(reviews);
//        HibernateUtil.getSessionFactory().close();
    }

}
