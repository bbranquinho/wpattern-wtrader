package org.wtrader.cep.data.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:data.properties")
public class DataProperties {

	@Value("${database.driver}")
	private String databaseDriver;

	@Value("${database.url}")
	private String databaseUrl;

	@Value("${database.user}")
	private String databaseUser;

	@Value("${database.password}")
	private String databasePassword;

	@Value("${database.show_sql:false}")
	private boolean databaseShowSql;

	@Value("${database.min_pool_size:2}")
	private int minPoolSize;

	@Value("${database.max_pool_size:5}")
	private int maxPoolSize;

	@Value("${database.max_idle_time:60}")
	private int maxIdleTime;

	public DataProperties() {
	}

	public String getDatabaseDriver() {
		return this.databaseDriver;
	}

	public String getDatabaseUrl() {
		return this.databaseUrl;
	}

	public String getDatabaseUser() {
		return this.databaseUser;
	}

	public String getDatabasePassword() {
		return this.databasePassword;
	}

	public boolean getDatabaseShowSql() {
		return this.databaseShowSql;
	}

	public int getMinPoolSize() {
		return this.minPoolSize;
	}

	public int getMaxPoolSize() {
		return this.maxPoolSize;
	}

	public int getMaxIdleTime() {
		return this.maxIdleTime;
	}

}
