package org.wtrader.test.cep.processor.utils;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackages = { CepProcessorTestContext.BASE_PACKAGE })
public class CepProcessorTestContext {

	static final String BASE_PACKAGE = "org.wtrader.test.cep.processor";

}
