package org.wtrader.test.loader.factory.utils;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.wtrader.loader.factory.utils.LoaderFactoryContext;

@Configuration
@Import(value = { LoaderFactoryContext.class })
@ComponentScan(basePackages = { LoaderFactoryTestContext.BASE_PACKAGE })
public class LoaderFactoryTestContext {

	static final String BASE_PACKAGE = "org.wtrader.test.loader.factory";

}
