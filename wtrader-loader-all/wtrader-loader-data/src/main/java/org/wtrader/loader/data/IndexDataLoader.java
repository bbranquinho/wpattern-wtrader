package org.wtrader.loader.data;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.entities.CompanyEntity;
import org.wtrader.cep.utils.data.entities.IndexRecordEntity;
import org.wtrader.cep.utils.data.entities.StockEntity;
import org.wtrader.cep.utils.data.interfaces.IDataStorage;
import org.wtrader.loader.utils.interfaces.IIndexDataLoader;

@Named
public class IndexDataLoader implements IIndexDataLoader {

	private final Logger LOGGER = Logger.getLogger(IndexDataLoader.class);

	private static final String FILE_FORMAT = "UTF-8";

	private static final String DELIMITER = "\t";

	private static final String DATE_PATTERN = "dd/MM/yyyy";

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);

	private int codeCvm;

	@Inject
	private IDataStorage dataStorage;

	public IndexDataLoader() {
		this.codeCvm = 0;
	}

	@Override
	public void loadFile(String fullnamePath) throws IOException {
		if (this.LOGGER.isInfoEnabled()) {
			this.LOGGER.info(String.format("Load data from file name path [%s].", fullnamePath));
		}

		LineIterator lineIterator = FileUtils.lineIterator(new File(fullnamePath), FILE_FORMAT);
		StockEntity stockEntity = null;

		if (lineIterator.hasNext()) {
			stockEntity = this.processStockLine(lineIterator.next().toLowerCase().trim());
		}

		while (lineIterator.hasNext()) {
			final String line = lineIterator.next().toLowerCase().trim();

			try {
				this.processIndexLine(line, stockEntity);
			} catch (Exception e) {
				this.LOGGER.error(String.format("Problem to parse or save the record [%s].", line));
				this.LOGGER.error(e.getMessage(), e);
			}
		}
	}

	private StockEntity processStockLine(String line) {
		StringTokenizer tokenizer = new StringTokenizer(line, DELIMITER);

		// Company
		CompanyEntity companyEntity = new CompanyEntity();

		companyEntity.setName(tokenizer.nextToken().trim());
		companyEntity.setCodeCvm(String.format("c%s:%s", this.codeCvm++, System.currentTimeMillis()));

		companyEntity = this.dataStorage.saveCompany(companyEntity);

		// Stock
		StockEntity stockEntity = new StockEntity();

		stockEntity.setName(tokenizer.nextToken().trim());
		stockEntity.setCompany(companyEntity);
		stockEntity.setWasNormalized(false);

		stockEntity = this.dataStorage.saveStock(stockEntity);

		return stockEntity;
	}

	private void processIndexLine(String line, StockEntity stockEntity) throws ParseException {
		IndexRecordEntity newRecord = new IndexRecordEntity();
		StringTokenizer tokenizer = new StringTokenizer(line, DELIMITER);
		String nextToken;
		int count = 0;

		while (tokenizer.hasMoreTokens()) {
			nextToken = tokenizer.nextToken().trim();

			switch (++count) {
			case 1:
				newRecord.setTradeDate(SIMPLE_DATE_FORMAT.parse(nextToken));
				break;

			case 2:
				newRecord.setOpen(Double.parseDouble(nextToken));
				break;

			case 3:
				newRecord.setHigh(Double.parseDouble(nextToken));
				break;

			case 4:
				newRecord.setLow(Double.parseDouble(nextToken));
				break;

			case 5:
				newRecord.setClose(Double.parseDouble(nextToken));
				break;

			case 6:
				newRecord.setVolume(Long.parseLong(nextToken));
				break;

			default:
				break;
			}
		}

		newRecord.setStock(stockEntity);

		this.dataStorage.saveIndexRecord(newRecord);
	}

}
