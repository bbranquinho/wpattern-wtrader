package org.wtrader.cep.data.utils;

import java.beans.PropertyVetoException;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.mchange.v2.c3p0.ComboPooledDataSource;


@Configuration
@ComponentScan(basePackages = { CepDataContext.BASE_PACKAGE })
@EnableJpaRepositories(basePackages = { CepDataContext.REPOSITORY_BASE_PACKAGE })
public class CepDataContext {

	private static final Logger LOGGER = Logger.getLogger(CepDataContext.class);

	static final String BASE_PACKAGE = "org.wtrader.cep.data";

	static final String REPOSITORY_BASE_PACKAGE = "org.wtrader.cep.data.repositories";

	static final String ENTITY_BASE_PACKAGE = "org.wtrader.cep.utils.data.entities";

	@Inject
	private DataProperties properties;

	public CepDataContext() {
	}
	
	@Bean(name = "placeHolderConfigurer")
	public static PropertySourcesPlaceholderConfigurer getPlaceHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean(name = "transactionManager")
	public JpaTransactionManager getTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();

		EntityManagerFactory ent = this.getEntityManagerFactory();

		transactionManager.setEntityManagerFactory(ent);
		transactionManager.setJpaDialect(this.getJpaDialect());

		return transactionManager;
	}

	@Bean(name = "jpaDialect")
	public JpaDialect getJpaDialect() {
		return new HibernateJpaDialect();
	}

	@Bean(name = "entityManagerFactory")
	public EntityManagerFactory getEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactory.setPackagesToScan(ENTITY_BASE_PACKAGE);
		entityManagerFactory.setDataSource(this.getDataSource());
		entityManagerFactory.setJpaVendorAdapter(this.getJpaVendorAdapter());
		entityManagerFactory.afterPropertiesSet();

		return entityManagerFactory.getObject();
	}

	@Bean(name = "jpaVendorAdapter")
	public JpaVendorAdapter getJpaVendorAdapter() {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

		jpaVendorAdapter.setShowSql(this.properties.getDatabaseShowSql());
		jpaVendorAdapter.setGenerateDdl(true);

		return jpaVendorAdapter;
	}

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();

		try {
			dataSource.setDriverClass(this.properties.getDatabaseDriver());
		} catch (PropertyVetoException e) {
			LOGGER.error(e.getMessage(), e);
			System.exit(-1);
		}

		dataSource.setJdbcUrl(this.properties.getDatabaseUrl());
		dataSource.setUser(this.properties.getDatabaseUser());
		dataSource.setPassword(this.properties.getDatabasePassword());
		dataSource.setMinPoolSize(this.properties.getMinPoolSize());
		dataSource.setMaxPoolSize(this.properties.getMaxPoolSize());
		dataSource.setMaxIdleTime(this.properties.getMaxIdleTime());

		return dataSource;
	}

}
