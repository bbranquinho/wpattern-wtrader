package org.wtrader.loader.utils.properties;

public interface ILoaderProperties {

	public abstract String getDatabaseName();

	public abstract String getDatabaseUsername();

	public abstract String getDatabasePassword();

	public abstract boolean isDatabaseBackup();

	public abstract String getDatabaseDump();

	public abstract String getDatabasePath();

	public abstract String getDatabaseFilename();

	public abstract boolean isCrawlerAll();

	public abstract String getStockRecordsPath();

	public abstract String getIndexRecordsPath();

	public abstract boolean isNormalizeUpdateUnitaryQuotationSince();

	public abstract boolean isNormalizeCleanUpCompanyRecords();

	public abstract boolean isNormalizeStockRecords();

	public abstract boolean isNormalizeFillEmptyStockRecords();

}