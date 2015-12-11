package org.wtrader.test.cep.factory.utils;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.wtrader.cep.data.utils.CepDataContext;
import org.wtrader.cep.processor.utils.CepProcessorContext;
import org.wtrader.cep.ta.utils.CepTaContext;


@Configuration
@Import({ CepProcessorContext.class, CepDataContext.class, CepTaContext.class })
@ComponentScan(basePackages = { CepFactoryContext.BASE_PACKAGE })
public class CepFactoryContext {

	static final String BASE_PACKAGE = "org.wtrader.test.cep.factory";

}
