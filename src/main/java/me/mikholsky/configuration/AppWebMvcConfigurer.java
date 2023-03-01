package me.mikholsky.configuration;

import org.hibernate.cfg.Environment;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.lang.NonNull;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ComponentScan("me.mikholsky")
@PropertySource({"classpath:application.properties"})
public class AppWebMvcConfigurer implements WebMvcConfigurer, EnvironmentAware, ApplicationContextAware {
	private ApplicationContext applicationContext;
	private org.springframework.core.env.Environment env;

	@Override
	public void setEnvironment(org.springframework.core.env.Environment environment) {
		this.env = environment;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private Properties hibernateProperties() {
		var properties = new Properties();

		properties.setProperty(
			Environment.DRIVER,
			Objects.requireNonNull(env.getProperty("hibernate.dialect")));

		properties.setProperty(
			Environment.SHOW_SQL,
			Objects.requireNonNull(env.getProperty("hibernate.show_sql"))
		);

		properties.setProperty(
			Environment.FORMAT_SQL,
			Objects.requireNonNull(env.getProperty("hibernate.format_sql"))
		);

		properties.setProperty(
			Environment.POOL_SIZE,
			Objects.requireNonNull(env.getProperty("hibernate.pool_size"))
		);

//		properties.setProperty(
//			Environment.CURRENT_SESSION_CONTEXT_CLASS,
//			Objects.requireNonNull(env.getProperty("hibernate.current_session_context_class"))
//		);

		return properties;
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

	@Bean
	public DataSource dataSource() {
		var dataSource = new DriverManagerDataSource();

		dataSource.setUrl(Objects.requireNonNull(env.getProperty("jdbc.url")));
		dataSource.setUsername(Objects.requireNonNull(env.getProperty("jdbc.username")));
		dataSource.setPassword(Objects.requireNonNull(env.getProperty("jdbc.password")));
		dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("jdbc.driver_class")));

		return dataSource;
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		var localSessionFactoryBean = new LocalSessionFactoryBean();

		localSessionFactoryBean.setHibernateProperties(hibernateProperties());

		localSessionFactoryBean.setPackagesToScan(Objects.requireNonNull(env.getProperty("datasource.packages_to_scan")));

		localSessionFactoryBean.setDataSource(dataSource());

		return localSessionFactoryBean;
	}

	@Bean
	public HibernateTransactionManager transactionManager() {
		return new HibernateTransactionManager(Objects.requireNonNull(sessionFactory().getObject()));
	}
}

