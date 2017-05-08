package org.gift.it;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import java.io.File;

@SpringBootApplication(exclude = {JacksonAutoConfiguration.class, JmxAutoConfiguration.class,
		WebSocketAutoConfiguration.class})
public class JustGiftItApplication {

	@Value("${multipart.location}/gif/")
	private String gifLocation;

	public static void main(String[] args) {
		SpringApplication.run(JustGiftItApplication.class, args);
	}

	@PostConstruct
	private void init() {
		File gifFolder = new File(gifLocation);
		if (!gifFolder.exists()) {
			gifFolder.mkdir();
		}
	}

	@Bean
	public FilterRegistrationBean deRegisterHiddenHttMethodFilter(HiddenHttpMethodFilter filter) {
		FilterRegistrationBean bean = new FilterRegistrationBean(filter);
		bean.setEnabled(false);
		return bean;
	}

	@Bean
	public FilterRegistrationBean deRegisterHttpPutFormFilter(HttpPutFormContentFilter filter) {
		FilterRegistrationBean bean = new FilterRegistrationBean(filter);
		bean.setEnabled(false);
		return bean;
	}

	@Bean
	public FilterRegistrationBean deRegisterRquestContextFilter(RequestContextFilter filter) {
		FilterRegistrationBean bean = new FilterRegistrationBean(filter);
		bean.setEnabled(false);
		return bean;
	}

	@Bean
	public WebMvcConfigurer webMvcConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/gif/**")
						.addResourceLocations("file:" + gifLocation);
				super.addResourceHandlers(registry);
			}
		};
	}
}
