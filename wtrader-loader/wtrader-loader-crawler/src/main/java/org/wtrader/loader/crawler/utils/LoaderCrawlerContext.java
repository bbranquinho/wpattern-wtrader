package org.wtrader.loader.crawler.utils;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { LoaderCrawlerContext.BASE_PACKAGE })
public class LoaderCrawlerContext {

	static final String BASE_PACKAGE = "org.wtrader.loader.crawler";

}
