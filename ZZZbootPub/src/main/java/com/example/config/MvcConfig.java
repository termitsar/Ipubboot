package com.example.config;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.webflow.view.AjaxThymeleafViewResolver;
import org.thymeleaf.spring4.webflow.view.FlowAjaxThymeleafView;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import com.example.service.OrderFlowHandler;

@Configuration
@ComponentScan("com.example")
@EntityScan(basePackages = "com.example.model")
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class MvcConfig extends WebMvcConfigurerAdapter {
    
	@Autowired
	private ApplicationContext applicationContext;
	
    @Autowired
    private SpringTemplateEngine springTemplateEngine;
	
    @Resource
    private Environment env;
    
//	@Bean
//	public HibernateEntityManagerFactory sessionFactory(HibernateEntityManagerFactory hemf) {
//	
//	    return hemf;
//	}
//	
//	
//	@Autowired
//	   @Bean
//	    public PlatformTransactionManager transactionManager(HibernateEntityManagerFactory hemf){
//	        return new JpaTransactionManager(hemf);
//	    }
//	
//	@Autowired
//    @Bean
//    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
//        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//        transactionManager.setSessionFactory(sessionFactory);
//        return transactionManager;
//    }

  
	
	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		DriverManagerDataSource  dataSource = new DriverManagerDataSource ();
//		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//		dataSource.setUrl("jdbc:mysql://vipnet-2:3306/ipub");
//		dataSource.setUsername("root");
//		dataSource.setPassword("g771373");
		dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.password"));

		return dataSource;
	}

	private Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
	//properties.put("hbm2ddl.auto", "create");	
		return properties;
	}

	@Autowired
	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource) {
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
		sessionBuilder.addProperties(getHibernateProperties());
		sessionBuilder.scanPackages("com.example.model");

		return sessionBuilder.buildSessionFactory();
	}

	@Autowired
	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);

		return transactionManager;
	}
    
    
    
	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
			"classpath:/META-INF/resources/", "classpath:/resources/",
			"classpath:/static/", "classpath:/public/" };
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (!registry.hasMappingForPattern("/classresource/**")) {
			registry.addResourceHandler("/classresource/**").addResourceLocations(
					"classpath:/META-INF/web-resources/");
		}
		
		if (!registry.hasMappingForPattern("/resources/**")) {
			registry.addResourceHandler("/resources/**").addResourceLocations("file:///"+env.getProperty("path.pictures.short"));
		}
		
		if (!registry.hasMappingForPattern("/**")) {
			registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
		}
	}
	
	
	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	     
	    mailSender.setUsername("rosno64@gmail.com");
	    mailSender.setPassword("banzay111");
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
	    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	    CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
	    characterEncodingFilter.setForceEncoding(true);
	    characterEncodingFilter.setEncoding("UTF-8");
	    registrationBean.setFilter(characterEncodingFilter);
	    return registrationBean;
	}
	
	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}

	@Bean
	public AjaxThymeleafViewResolver tilesViewResolver() {
		AjaxThymeleafViewResolver viewResolver = new AjaxThymeleafViewResolver();
		viewResolver.setViewClass(FlowAjaxThymeleafView.class);
		viewResolver.setTemplateEngine(springTemplateEngine);
		viewResolver.setContentType("text/html; charset=UTF-8");
		return viewResolver;
	}
	
	@Bean(name="ordering")
	public OrderFlowHandler OrderFlowHandler() {
		return new OrderFlowHandler();
	}
	

	
	public static final String MAX_FILE_SIZE = "2MB";
	public static final String MAX_REQUEST_SIZE = "10MB";
	public static final String FILE_SIZE_THRESHOLD = "10MB";
	@Bean
	MultipartConfigElement multipartConfigElement() {

	    MultipartConfigFactory  factory = new MultipartConfigFactory();
	    factory.setMaxFileSize(MAX_FILE_SIZE);
	    factory.setMaxRequestSize(MAX_REQUEST_SIZE);
	    factory.setFileSizeThreshold(FILE_SIZE_THRESHOLD);
	    return factory.createMultipartConfig();
	}

	@Bean
	public StandardServletMultipartResolver multipartResolver() {
	    return new StandardServletMultipartResolver();
	}
	
	
}
