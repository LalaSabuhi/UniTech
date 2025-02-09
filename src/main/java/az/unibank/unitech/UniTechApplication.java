package az.unibank.unitech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableWebMvc
@EnableSwagger2
@EnableCaching
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class UniTechApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniTechApplication.class, args);
	}

}
