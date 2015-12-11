package org.wtrader.loader.utils.properties;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.wtrader.loader.utils.ErrorMessages;
import org.wtrader.loader.utils.beans.BaseBean;
import org.wtrader.loader.utils.exceptions.PropertyNotFoundedException;

@Configuration
public class LoaderProperties extends BaseBean implements ILoaderProperties {

	private static final long serialVersionUID = 201401201031L;

	@Value("${crawler.all:#{false}}")
	private boolean crawlerAll;

	@Value("${load.stock.records.path:#{null}}")
	private String stockRecordsPath;

	@Value("${load.index.records.path:#{null}}")
	private String indexRecordsPath;

	@Value("${normalize.update.unitary.quotation.since:false}")
	private boolean normalizeUpdateUnitaryQuotationSince;

	@Value("${normalize.clean.up.company.records:false}")
	private boolean normalizeCleanUpCompanyRecords;

	@Value("${normalize.stock.records:false}")
	private boolean normalizeStockRecords;

	@Value("${normalize.fill.empty.stock.records:false}")
	private boolean normalizeFillEmptyStockRecords;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                 DATABASE BACKUP                                                    //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static final String APPLICATION_DATABASE_NAME_PROPERTY = "application.database.name";

	private static final String APPLICATION_DATABASE_USERNAME_PROPERTY = "application.database.username";

	private static final String APPLICATION_DATABASE_PASSWORD_PROPERTY = "application.database.password";

	private static final String APPLICATION_DATABASE_BACKUP_PROPERTY = "application.database.backup";

	private static final String APPLICATION_DATABASE_BACKUP_MYSQLDUMP_PROPERTY = "application.database.backup.mysqldump";

	private static final String APPLICATION_DATABASE_BACKUP_PATH_PROPERTY = "application.database.backup.path";

	private static final String APPLICATION_DATABASE_BACKUP_FILENAME_PROPERTY = "application.database.backup.filename";

	@Value("${" + APPLICATION_DATABASE_NAME_PROPERTY + "}")
	private String databaseName;

	@Value("${" + APPLICATION_DATABASE_USERNAME_PROPERTY + "}")
	private String databaseUsername;

	@Value("${" + APPLICATION_DATABASE_PASSWORD_PROPERTY + "}")
	private String databasePassword;

	@Value("${" + APPLICATION_DATABASE_BACKUP_PROPERTY + ":false}")
	private boolean databaseBackup;

	@Value("${" + APPLICATION_DATABASE_BACKUP_MYSQLDUMP_PROPERTY + ":#{null}}")
	private String databaseDump;

	@Value("${" + APPLICATION_DATABASE_BACKUP_PATH_PROPERTY + ":#{null}}")
	private String databasePath;

	@Value("${" + APPLICATION_DATABASE_BACKUP_FILENAME_PROPERTY +":#{null}}")
	private String databaseFilename;

	@Override
	public String getDatabaseName() {
		return this.databaseName;
	}

	@Override
	public String getDatabaseUsername() {
		return this.databaseUsername;
	}

	@Override
	public String getDatabasePassword() {
		return this.databasePassword;
	}

	@Override
	public boolean isDatabaseBackup() {
		return this.databaseBackup;
	}

	@Override
	public String getDatabaseDump() {
		return this.databaseDump;
	}

	@Override
	public String getDatabasePath() {
		return this.databasePath;
	}

	@Override
	public String getDatabaseFilename() {
		return this.databaseFilename;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                   CONSTRUCTORS                                                     //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public LoaderProperties() {
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                 PUBLIC METHODS                                                     //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@PostConstruct
	public void init() {
		if (this.databaseBackup) {
			if (this.databaseDump == null) {
				throw new PropertyNotFoundedException(String.format(ErrorMessages.PROPERTY_NOT_FOUNDED,
						APPLICATION_DATABASE_BACKUP_MYSQLDUMP_PROPERTY));
			}

			if (this.databasePath == null) {
				this.databasePath = System.getProperty("user.dir");
			}

			if (this.databaseFilename == null) {
				DateTime dateTime = new DateTime();

				this.databaseFilename = String.format("market_data_backup_%s-%s-%s_%s%s%s_%%s.sql",
						dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), dateTime.getYear(),
						dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), dateTime.getSecondOfMinute());
			}
		}
	}

	@Override
	public boolean isCrawlerAll() {
		return this.crawlerAll;
	}

	public void setCrawlerAll(boolean crawlerAll) {
		this.crawlerAll = crawlerAll;
	}

	@Override
	public String getStockRecordsPath() {
		return this.stockRecordsPath;
	}

	public void setStockRecordsPath(String stockRecordsPath) {
		this.stockRecordsPath = stockRecordsPath;
	}

	@Override
	public String getIndexRecordsPath() {
		return this.indexRecordsPath;
	}

	public void setIndexRecordsPath(String indexRecordsPath) {
		this.indexRecordsPath = indexRecordsPath;
	}

	@Override
	public boolean isNormalizeUpdateUnitaryQuotationSince() {
		return this.normalizeUpdateUnitaryQuotationSince;
	}

	public void setNormalizeUpdateUnitaryQuotationSince(boolean normalizeUpdateUnitaryQuotationSince) {
		this.normalizeUpdateUnitaryQuotationSince = normalizeUpdateUnitaryQuotationSince;
	}

	@Override
	public boolean isNormalizeCleanUpCompanyRecords() {
		return this.normalizeCleanUpCompanyRecords;
	}

	public void setNormalizeCleanUpCompanyRecords(boolean normalizeCleanUpCompanyRecords) {
		this.normalizeCleanUpCompanyRecords = normalizeCleanUpCompanyRecords;
	}

	@Override
	public boolean isNormalizeStockRecords() {
		return this.normalizeStockRecords;
	}

	public void setNormalizeStockRecords(boolean normalizeStockRecords) {
		this.normalizeStockRecords = normalizeStockRecords;
	}

	@Override
	public boolean isNormalizeFillEmptyStockRecords() {
		return this.normalizeFillEmptyStockRecords;
	}

	public void setNormalizeFillEmptyStockRecords(boolean normalizeFillEmptyStockRecords) {
		this.normalizeFillEmptyStockRecords = normalizeFillEmptyStockRecords;
	}

	public void setMysqlDump(String mysqlDump) {
		this.databaseDump = mysqlDump;
	}

}
