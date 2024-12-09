package by.clevertec.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.yaml.snakeyaml.Yaml;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan("by.clevertec")
@EnableJpaRepositories("by.clevertec.repository")
@EnableTransactionManagement
public class AppConfig {

    private final Map<String, Object> yamlProperties;

    public AppConfig() {
        try (InputStream input = Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream("application.yml"))) {
            Yaml yaml = new Yaml();
            this.yamlProperties = yaml.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось загрузить application.yml", e);
        }
    }

    private Map<String, Object> getNestedMap(String path) {
        String[] keys = path.split("\\.");
        Map<String, Object> currentMap = yamlProperties;
        for (String key : keys) {
            currentMap = (Map<String, Object>) currentMap.get(key);
            if (currentMap == null) {
                throw new IllegalStateException("Не удалось найти путь: " + path);
            }
        }
        return currentMap;
    }

    @Bean
    public DataSource dataSource() {
        Map<String, Object> datasource = getNestedMap("spring.datasource");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName((String) datasource.get("driver-class-name"));
        dataSource.setUrl((String) datasource.get("url"));
        dataSource.setUsername((String) datasource.get("username"));
        dataSource.setPassword((String) datasource.get("password"));
        return dataSource;
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setDataSource(dataSource());
        return liquibase;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        Map<String, Object> jpa = getNestedMap("spring.jpa");

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        Map<String, Object> hibernateProperties = (Map<String, Object>) jpa.get("properties");
        if (hibernateProperties != null) {
            jpaProperties.put("hibernate.dialect",  "org.hibernate.dialect.PostgreSQLDialect");
            jpaProperties.put("hibernate.hbm2ddl.auto", "validate");
        }

        factoryBean.setJpaProperties(jpaProperties);
        factoryBean.setPackagesToScan("by.clevertec.entity");
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }
}
