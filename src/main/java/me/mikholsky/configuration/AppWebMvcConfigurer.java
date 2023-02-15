package me.mikholsky.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.time.Duration;
import java.util.Objects;

@Configuration
@EnableWebMvc
@ComponentScan("me.mikholsky")
@PropertySource({"classpath:application.properties"})
public class AppWebMvcConfigurer implements WebMvcConfigurer {
	private ApplicationContext applicationContext;
	private Environment env;

	@Autowired
	public void setEnv(Environment env) {
		this.env = env;
	}

	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		var templateResolver = new SpringResourceTemplateResolver();

		templateResolver.setApplicationContext(this.applicationContext);
		templateResolver.setPrefix(Objects.requireNonNull(env.getProperty("templateResolver.prefix")));
		templateResolver.setSuffix(Objects.requireNonNull(env.getProperty("templateResolver.suffix")));
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCacheable(
			Boolean.parseBoolean(Objects.requireNonNull(env.getProperty("templateResolver.isCacheable"))));

		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		var templateEngine = new SpringTemplateEngine();

		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setEnableSpringELCompiler(true);

		return templateEngine;
	}

	@Bean
	public ThymeleafViewResolver viewResolver() {
		var viewResolver = new ThymeleafViewResolver();

		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setOrder(1);

		return viewResolver;
	}
}

