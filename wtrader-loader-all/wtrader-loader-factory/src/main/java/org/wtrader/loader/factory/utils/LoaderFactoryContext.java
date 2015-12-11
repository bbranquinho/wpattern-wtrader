package org.wtrader.loader.factory.utils;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.wtrader.cep.data.utils.CepDataContext;
import org.wtrader.loader.crawler.utils.LoaderCrawlerContext;
import org.wtrader.loader.data.utils.LoaderDataContext;
import org.wtrader.loader.utils.LoaderUtilsContext;

@Configuration
@Import(value = { CepDataContext.class, LoaderCrawlerContext.class, LoaderDataContext.class, LoaderUtilsContext.class })
@ComponentScan(basePackages = { LoaderFactoryContext.BASE_PACKAGE })
public class LoaderFactoryContext {

	static final String BASE_PACKAGE = "org.wtrader.loader.factory";

}
