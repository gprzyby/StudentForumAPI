package edu.internet_engineering.student_forum_api;

import edu.internet_engineering.student_forum_api.model.security.JWTFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "edu.internet_engineering.student_forum_api.model.repo")
public class StudentForumApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentForumApiApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean(){
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new JWTFilter());
		filterRegistrationBean.setUrlPatterns(Arrays.asList("/categories/*", "/threads/*", "/subjects/*"));
		return filterRegistrationBean;
	}
}
