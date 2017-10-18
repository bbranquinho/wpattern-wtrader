package org.wtrader.test.cep.data.repositories;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.wtrader.cep.data.repositories.IBenefitTypeRepository;
import org.wtrader.cep.utils.data.entities.BenefitTypeEntity;
import org.wtrader.test.cep.data.utils.AbstractCepDatabaseTest;

public class BenefitTypeRepositoryTest extends AbstractCepDatabaseTest {

	private static final Logger LOGGER = Logger.getLogger(BenefitTypeRepositoryTest.class);

	@Inject
	private IBenefitTypeRepository benefitTypeRepository;

	@Test
	public void testFindAll() {
		List<BenefitTypeEntity> benefits = this.benefitTypeRepository.findAll();

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(benefits);
		}
	}

}
