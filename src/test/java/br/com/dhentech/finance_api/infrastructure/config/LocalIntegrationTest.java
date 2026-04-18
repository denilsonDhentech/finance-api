package br.com.dhentech.finance_api.infrastructure.config;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/financial_dhentech_db",
        "spring.datasource.username=administrator",
        "spring.datasource.password=admin",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public @interface LocalIntegrationTest {
}
