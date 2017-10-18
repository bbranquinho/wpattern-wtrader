package org.wtrader.loader.data.utils;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { LoaderDataContext.BASE_PACKAGE })
public class LoaderDataContext {

	static final String BASE_PACKAGE = "org.wtrader.loader.data";

}
