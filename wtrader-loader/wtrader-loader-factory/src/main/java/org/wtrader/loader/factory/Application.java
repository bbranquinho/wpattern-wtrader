package org.wtrader.loader.factory;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.interfaces.IStockData;
import org.wtrader.loader.factory.interfaces.IApplication;
import org.wtrader.loader.utils.interfaces.ICrawlerLoader;
import org.wtrader.loader.utils.interfaces.IDataNormalization;
import org.wtrader.loader.utils.interfaces.IDatabase;
import org.wtrader.loader.utils.interfaces.IIndexDataLoader;
import org.wtrader.loader.utils.interfaces.IStockDataLoader;
import org.wtrader.loader.utils.properties.ILoaderProperties;

@Named
public class Application implements IApplication {

	private static final Logger LOGGER = Logger.getLogger(Application.class);

	@Inject
	private IStockDataLoader stockDataLoader;

	@Inject
	private IIndexDataLoader indexDataLoader;

	@Inject
	private ICrawlerLoader crawlerLoader;

	@Inject
	private IStockData stockData;

	@Inject
	private IDataNormalization dataNormalization;

	@Inject
	private ILoaderProperties dataLoaderProperties;

	@Inject
	private IDatabase database;

	@Override
	public void startApplication(String[] args) throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("Market data loader started with properties [%s].", this.dataLoaderProperties));
		}

		if (this.dataLoaderProperties.isCrawlerAll()) {
			this.crawlerLoader.load();
			this.database.backup("crawler");
		}

		try {
			if (this.dataLoaderProperties.getStockRecordsPath() != null) {
				File pathOfFiles = new File(this.dataLoaderProperties.getStockRecordsPath());

				for (File file : pathOfFiles.listFiles()) {
					if (file.isFile()) {
						if (LOGGER.isInfoEnabled()) {
							LOGGER.info(String.format("Loading the file [%s].", file.getAbsolutePath()));
						}

						this.stockDataLoader.loadFile(file.getAbsolutePath(), this.stockData.findAllNames());
					}
				}

				this.database.backup("load-stock-records");
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}

		try {
			if (this.dataLoaderProperties.getIndexRecordsPath() != null) {
				File pathOfFiles = new File(this.dataLoaderProperties.getIndexRecordsPath());

				for (File file : pathOfFiles.listFiles()) {
					if (file.isFile()) {
						if (LOGGER.isInfoEnabled()) {
							LOGGER.info(String.format("Loading the file [%s].", file.getAbsolutePath()));
						}

						this.indexDataLoader.loadFile(file.getAbsolutePath());
					}
				}

				this.database.backup("load-index-records");
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}

		this.dataNormalization.normalize();
	}

}
