package by.clevertec;

import by.clevertec.dao.CarDAO;
import by.clevertec.dao.CarShowroomDAO;
import by.clevertec.dao.CategoryDAO;
import by.clevertec.dao.ClientDAO;
import by.clevertec.dao.ReviewDAO;
import by.clevertec.entity.Car;
import by.clevertec.entity.CarShowroom;
import by.clevertec.entity.Category;
import by.clevertec.entity.Client;
import by.clevertec.entity.Review;
import by.clevertec.service.CarService;
import by.clevertec.service.ReviewService;
import by.clevertec.util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        CarDAO carDAO = new CarDAO();
        ClientDAO clientDAO = new ClientDAO();
        CarShowroomDAO showroomDAO = new CarShowroomDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        ReviewDAO reviewDAO = new ReviewDAO();
        CarService carService = new CarService(carDAO, showroomDAO);
        ReviewService reviewService = new ReviewService(reviewDAO, carDAO, clientDAO);

        try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory()) {
            System.out.println("=== Начало тестов ===");

            // Создание объектов
            CarShowroom showroom = CarShowroom.builder()
                    .name("Автосалон №1")
                    .address("г. Минск, пр-т Независимости, 1")
                    .build();
            showroomDAO.save(showroom);

            Category sedanCategory = Category.builder()
                    .name("Седан")
                    .build();
            categoryDAO.save(sedanCategory);

            Category miniCategory = Category.builder()
                    .name("Мини")
                    .build();
            categoryDAO.save(miniCategory);

            Car car = Car.builder()
                    .model("Camry")
                    .brand("Toyota")
                    .year(2021)
                    .price(35000)
                    .category(sedanCategory)
                    .showroom(showroom)
                    .build();
            carDAO.save(car);
            Car car2 = Car.builder()
                    .model("Yarys")
                    .brand("Toyota")
                    .year(2021)
                    .price(20000)
                    .category(miniCategory)
                    .showroom(showroom)
                    .build();
            carDAO.save(car2);
            Client client = Client.builder()
                    .name("Иван Иванов")
                    .registrationDate(LocalDate.now())
                    .build();
            client.getContacts().put("phone", "+375291234567");
            client.getContacts().put("email", "ivanov@example.com");
            clientDAO.save(client);
            client.getCars().add(car);
            car.setShowroom(null);
            clientDAO.update(client);
            carDAO.update(car);

            // Создание отзыва
            Review review = Review.builder()
                    .text("Отличный автомобиль! Классный")
                    .rating(5)
                    .client(client)
                    .car(car)
                    .build();
            reviewDAO.save(review);
            System.out.println(carService.findCarsByFilters("Toyota", 2021, null, 10000D, 300000D));
            System.out.println(carService.findAllSortedByPrice("ASC"));
            System.out.println(carService.findCarsWithPagination(1, 2));
            System.out.println(reviewService.searchReviews("ывсап"));

//
//            System.out.println("=== Данные об автосалонах ===");
//            showroomDAO.findAll().forEach(System.out::println);
//
//            System.out.println("=== Данные о клиентах ===");
//            clientDAO.findAll().forEach(System.out::println);
//
//            System.out.println("=== Данные об автомобилях ===");
//            carDAO.findAll().forEach(System.out::println);
//
//            System.out.println("=== Данные об отзывах ===");
//            reviewDAO.findAll().forEach(System.out::println);
//
//            // Удаление в правильном порядке
//            System.out.println("=== Удаление объектов ===");
//            // Сначала удаляем отзывы, так как они зависят от машины и клиента
//            reviewDAO.delete(review);
//            // Удаляем машину после удаления отзыва
//            carDAO.delete(car);
//            // Удаляем категорию, так как она может быть зависима от машины (если настроен каскад)
//            categoryDAO.delete(sedanCategory);
//            clientDAO.delete(client);
//            showroomDAO.delete(showroom);
//
//            System.out.println("=== Удаление завершено ===");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.getSessionFactory().close();
        }
    }
}
