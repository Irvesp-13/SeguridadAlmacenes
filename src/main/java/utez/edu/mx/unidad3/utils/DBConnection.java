package utez.edu.mx.unidad3.utils;

/*
* @Configuration: Le dice a spring  que va a configurar algo.
* Requiere al menos de un metodo que retorne dicha configuracion.
*
* @Bean: Define al metodo que va a regresar dicha configuracion
* */


import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DBConnection {
    @Value("${db.url}")
    private String DB_URL;

    @Value("${db.password}")
    private String DB_PASSWORD;

    @Value("${db.username}")
    private String DB_USERNAME;

    @Bean
    public DataSource getConnection() {
        try {
            DriverManagerDataSource configuration = new DriverManagerDataSource();
            configuration.setUrl(DB_URL);
            configuration.setPassword(DB_PASSWORD);
            configuration.setUsername(DB_USERNAME);
            configuration.setDriverClassName("com.mysql.cj.jdbc.Driver");

            return configuration;
        } catch (Exception ex) {
            System.out.println("Error de conexión de base de datos");
            ex.printStackTrace();
            return null;
        }
    }
}
