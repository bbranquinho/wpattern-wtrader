package org.wtrader.test.cep.data.repositories;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.wtrader.cep.data.repositories.IIndexRecordRepository;
import org.wtrader.cep.data.repositories.IStockRepository;
import org.wtrader.cep.utils.data.entities.IndexRecordEntity;
import org.wtrader.test.cep.data.utils.AbstractCepDatabaseTest;

public class IndexRecordTest extends AbstractCepDatabaseTest {

	private static final Logger LOGGER = Logger.getLogger(IndexRecordTest.class);

	@Inject
	private IIndexRecordRepository indexRecordRepository;

	@Inject
	private IStockRepository stockRepository;

	@Test
	public void testFindAll() {
		List<IndexRecordEntity> indexRecords = this.indexRecordRepository.findAll();

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(indexRecords);
		}
	}

	@Test
	public void testCount() {
		long numRecords = this.indexRecordRepository.countByStock(this.stockRepository.findByName("^bvsp"));

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("Number of records [%s].", numRecords));
		}
	}

}
