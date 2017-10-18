package org.wtrader.test.loader.data.database.restore;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.wtrader.loader.utils.interfaces.IDatabase;
import org.wtrader.test.loader.data.AbstractLoaderDataTest;

public class DatabaseRestoreTest extends AbstractLoaderDataTest {

	private static final Logger LOGGER = Logger.getLogger(DatabaseRestoreTest.class);

	private static final String FULL_FILENAME = "D:\\Doutorado\\executables\\loader\\7_market_data_backup_4-30-2014_73358_normalize-stock-records.sql";

	@Inject
	private IDatabase database;

	@Test
	public void restoreDatabase() {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("Restoring the database [%s].", FULL_FILENAME));
		}

		try {
			this.database.restore(FULL_FILENAME);
		} catch(Exception e) {
			this.logAndFail(e.getMessage(), e, LOGGER);
		}
	}

}
