package org.wtrader.test.loader.data.utils;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.wtrader.loader.data.utils.LoaderDataContext;

@Configuration
@Import(value = { LoaderDataContext.class })
@ComponentScan(basePackages = { LoaderDataTestContext.BASE_PACKAGE })
public class LoaderDataTestContext {

	static final String BASE_PACKAGE = "org.wtrader.test.loader.data";

	static final String UTILS_BASE_PACKAGE = "org.wtrader.loader.utils.properties";

}
