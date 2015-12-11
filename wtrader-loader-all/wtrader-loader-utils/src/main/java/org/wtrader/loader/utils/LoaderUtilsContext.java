package org.wtrader.loader.utils;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { LoaderUtilsContext.BASE_PACKAGE })
public class LoaderUtilsContext {

	static final String BASE_PACKAGE = "org.wtrader.loader.utils";

}
