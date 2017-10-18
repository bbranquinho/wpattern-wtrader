package org.wtrader.test.cep.data.repositories;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.wtrader.cep.data.repositories.IBenefitStockRepository;
import org.wtrader.cep.utils.data.entities.BenefitStockEntity;
import org.wtrader.test.cep.data.utils.AbstractCepDatabaseTest;

public class BenefitStockRepositoryTest extends AbstractCepDatabaseTest {

	private static final Logger LOGGER = Logger.getLogger(BenefitStockRepositoryTest.class);

	@Inject
	private IBenefitStockRepository benefitStockRepository;

	@Test
	public void testFindAll() {
		List<BenefitStockEntity> benefits = this.benefitStockRepository.findAll();

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(benefits);
		}
	}

}
