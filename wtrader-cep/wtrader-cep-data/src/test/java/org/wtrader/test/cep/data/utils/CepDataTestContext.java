package org.wtrader.test.cep.data.utils;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.wtrader.cep.data.utils.CepDataContext;

@Configuration
@Import(value = { CepDataContext.class })
@ComponentScan(basePackages = { CepDataTestContext.BASE_TEST_PACKAGE })
public class CepDataTestContext {

	static final String BASE_TEST_PACKAGE = "org.wtrader.test.cep.data";

}
