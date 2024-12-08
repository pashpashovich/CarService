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
import by.clevertec.service.CarService;
import by.clevertec.service.ClientService;
import by.clevertec.service.ReviewService;
import by.clevertec.util.HibernateUtil;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        //Проверка работы методов
        CarDAO carDao = new CarDAO();
        ClientDAO clientDAO = new ClientDAO();
        ReviewDAO reviewDAO = new ReviewDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        CarShowroomDAO showroomDAO = new CarShowroomDAO();
        CarService carService = new CarService(carDao);
        ReviewService reviewService = new ReviewService(reviewDAO);
        addSomeCarsToShowroom(carService);
        System.out.println("Добавленные автомобили: ");
        System.out.println(carDao.findAll());
        System.out.println("Автомобили марки Nissan:\n" + carService.findCarsByFilters("Nissan", null, null, null, null));
        buyCar(carDao);
        addReview(reviewService, clientDAO, carDao);
        System.out.println("Сущности проекта:");
        System.out.println(carDao.findAll() + "\n" + reviewDAO.findAll() + "\n" + categoryDAO.findAll() + "\n" + showroomDAO.findAll() + "\n" + clientDAO.findAll());
        System.out.println("Отзыв со словом классная");
        System.out.println(reviewService.searchReviews("Классная"));
        System.out.println("Все мащины в порядке убывания цены: ");
        System.out.println(carService.findAllSortedByPrice("Desc"));
        System.out.println("Поиск машин с пагинацией: ");
        System.out.println(carService.findCarsWithPagination(1, 1));
        System.out.println("Поиск машин с использованием Entity graph: ");
        System.out.println(carService.findCarsByShowroomWithEntityGraph(1L));
        System.out.println("Поиск машин с использованием JOIN FETCH: ");
        System.out.println(carService.findCarsByShowroomWithJoinFetch(1L));
        System.out.println("Удаление всех сущностей: ");
        reviewDAO.delete(reviewDAO.findById(1L));
        carDao.delete(carDao.findById(1L));
        categoryDAO.delete(categoryDAO.findById(1L));
        clientDAO.delete(clientDAO.findById(1L));
        showroomDAO.delete(showroomDAO.findById(1L));
        HibernateUtil.getSessionFactory().close();
    }

    private static void addSomeCarsToShowroom(CarService carService) {
        CategoryDAO categoryDAO = new CategoryDAO();
        Category category = Category.builder()
                .name("Внедорожник")
                .build();
        categoryDAO.save(category);
        Car car = Car.builder()
                .brand("Nissan")
                .model("Qashqai")
                .year(2021)
                .price(20200)
                .category(category)
                .build();
        Car car2 = Car.builder()
                .brand("AUDI")
                .model("Q5")
                .year(2015)
                .price(10765)
                .category(category)
                .build();
        carService.addCar(car);
        carService.addCar(car2);
        CarShowroom carShowroom = CarShowroom.builder()
                .name("Главный")
                .address("Минск")
                .build();
        CarShowroomDAO carShowroomDAO = new CarShowroomDAO();
        carShowroomDAO.save(carShowroom);
        carService.assignCarToShowroom(car, carShowroom);
        carService.assignCarToShowroom(car2, carShowroom);
    }

    private static void buyCar(CarDAO carDAO) {
        Client client = Client.builder()
                .name("Иван Иванов")
                .registrationDate(LocalDate.now())
                .build();
        client.getContacts().put("phone", "+375291234567");
        client.getContacts().put("email", "ivanov@example.com");
        Car car = carDAO.findById(1L);
        ClientService.buyCar(client, car);
    }

    private static void addReview(ReviewService reviewService, ClientDAO clientDAO, CarDAO carDAO) {
        Client client = clientDAO.findById(1L);
        Car car = carDAO.findById(1L);
        reviewService.addReview(client, car, "Классная машина", 10);
        System.out.println("Отзыв со словом Классная:");
        System.out.println(reviewService.searchReviewsByText("Классная"));
        System.out.println("Отзыв с отметкой 9:");
        System.out.println(reviewService.searchReviewsByRating(9));
    }
}
