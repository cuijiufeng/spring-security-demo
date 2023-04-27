package io.inferiority.demo.springsecurity.config.multidb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @Class: DynamicDataSource
 * @Date: 2023/4/27 9:31
 * @author: cuijiufeng
 */
//@Component
@Deprecated
public class DynamicDataSource extends AbstractRoutingDataSource {
	@Autowired
	private Environment environment;

	@PostConstruct
	public void init() {
		Binder propertyBinder = new Binder(ConfigurationPropertySources.get(environment));
		BindResult<DataSourceProperties> bindResult = propertyBinder.bind("spring.datasource", DataSourceProperties.class);
		setDefaultTargetDataSource(bindResult.get().initializeDataSourceBuilder().build());

		Map<Object, Object> targetDataSources = new HashMap<>();
		for (String dbNo : environment.getProperty("custom.datasource.names").split(",")) {
			BindResult<DataSourceProperties> result = propertyBinder.bind("custom.datasource." + dbNo, DataSourceProperties.class);
			targetDataSources.put(dbNo, result.get().initializeDataSourceBuilder().build());
		}
		setTargetDataSources(targetDataSources);
	}

	@Override
	protected Object determineCurrentLookupKey() {
		return DynamicDataSourceContextHolder.getDataSourceType();
	}
}