package org.wtrader.cep.ta.utils;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { CepTaContext.BASE_PACKAGE, CepTaContext.LIBPOMDP_PACKAGE })
public class CepTaContext {

	static final String BASE_PACKAGE = "org.wtrader.cep.ta";

	static final String LIBPOMDP_PACKAGE = "com.tictactec.ta.lib";

}
