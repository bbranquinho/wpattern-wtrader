package org.wtrader.loader.data.backup;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.loader.utils.interfaces.IDatabase;
import org.wtrader.loader.utils.properties.ILoaderProperties;

@Named
public class Database implements IDatabase {

	private static final Logger LOGGER = Logger.getLogger(Database.class);

	private static final String USER_PARAMETER = "--user=";

	private static final String PASSWORD_PARAMETER = "--password=";

	private static final String RESULT_FILE_PARAMETER = "--result-file=";

	private static final String MYSQL_PROGRAM = "mysql";

	private static final String RESTORE_PARAMETER = "<";

	@Inject
	private ILoaderProperties properties;

	@Override
	public int backup(String step) {
		if (!this.properties.isDatabaseBackup()) {
			return 0;
		}

		File directory = new File(this.properties.getDatabasePath());
		String filename = String.format(this.properties.getDatabaseFilename(), step);

		// When not exist create the directory.
		if(!directory.isDirectory()) {
			new File(this.properties.getDatabasePath()).mkdir();
		}

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("Executing the database backup to phase [%s] with filename [%s] and directory [%s].",
					step, this.properties.getDatabaseFilename(), this.properties.getDatabasePath()));
		}

		try {
			ProcessBuilder processBuilder = new ProcessBuilder(this.properties.getDatabaseDump(),
					USER_PARAMETER + this.properties.getDatabaseUsername(),
					PASSWORD_PARAMETER + this.properties.getDatabasePassword(),
					this.properties.getDatabaseName(), RESULT_FILE_PARAMETER + filename);
			Process process = processBuilder.start();
			int processResult = process.waitFor();
			process.destroy();

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Backup of the database executed.");
			}

			return processResult;
		} catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			return -1;
		}
	}

	@Deprecated
	@Override
	public void restore(String fullFilename) throws Exception {
		// Must be tested;
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(MYSQL_PROGRAM,
					USER_PARAMETER + this.properties.getDatabaseUsername(),
					PASSWORD_PARAMETER + this.properties.getDatabasePassword(),
					this.properties.getDatabaseName(), RESTORE_PARAMETER, fullFilename);
			Process process = processBuilder.start();
			process.waitFor();
			process.destroy();
		} catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

}
