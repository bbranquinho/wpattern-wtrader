package org.wtrader.loader.factory.main;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.wtrader.loader.factory.interfaces.IApplication;
import org.wtrader.loader.factory.utils.LoaderFactoryContext;

abstract class AbstractMain {

	private static final Logger LOGGER = Logger.getLogger(AbstractMain.class);

	protected ApplicationContext context;

	protected IApplication factoryDistribution;

	private String[] args;

	protected AbstractMain(String[] args) {
		this.args = args;
	}

	public void mainAbstract(String... contexts) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("WTrader Loader started.");
		}
		try {
			this.context = new AnnotationConfigApplicationContext(LoaderFactoryContext.class);

			this.factoryDistribution = this.context.getBean(IApplication.class);

			this.factoryDistribution.startApplication(this.args);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
