package org.wtrader.cep.processor.utils;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackages = { CepProcessorContext.BASE_PACKAGE })
public class CepProcessorContext {

	static final String BASE_PACKAGE = "org.wtrader.cep.processor";

}
