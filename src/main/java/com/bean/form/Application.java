package com.bean.form;

import com.bean.form.dao.BookDAO;
import com.bean.form.dao.PersonDAO;
//import com.bean.form.dao.impl.BookDAOImpl;
//import com.bean.form.dao.impl.PersonDAOImpl;
import com.bean.form.exceptions.GlobalExceptionHandler;
import com.bean.form.service.HistoryService;
import com.bean.form.service.impl.BookServiceImpl;
import com.bean.form.service.impl.HistoryServiceImpl;
import com.bean.form.service.impl.PersonServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Locale;

import static springfox.documentation.builders.PathSelectors.regex;

@EnableSwagger2
@ImportResource("spring_mvc_config.xml")
@SpringBootApplication
@ComponentScan(basePackages = {"com.bean.form.controller"},basePackageClasses = {BookServiceImpl.class, BookDAO.class, PersonServiceImpl.class, PersonDAO.class, HistoryService.class})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public TaskExecutor controllerPool() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() + 1);
		executor.setQueueCapacity(25);
		return executor;
	}

	@Bean
    public AspectLogger aspectLogger(){
	    return new AspectLogger();
    }

    @Bean
	public GlobalExceptionHandler globalExceptionHandler(){
    	return new GlobalExceptionHandler();
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
		sessionLocaleResolver.setDefaultLocale(Locale.US);
		return sessionLocaleResolver;
	}

	@Bean
	public Docket postApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Library")
				.apiInfo(apiInfo())
				.select().paths(regex("/.*")).build()
				.enableUrlTemplating(false);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Spring REST Sample with Swagger")
				.description("Spring REST Sample with Swagger")
				.contact("https://github.com/igorek9191/Library-project")
				.version("1.0")
				.build();
	}
}
