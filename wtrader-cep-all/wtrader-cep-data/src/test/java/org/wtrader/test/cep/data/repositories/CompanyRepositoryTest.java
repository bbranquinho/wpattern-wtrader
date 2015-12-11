package org.wtrader.test.cep.data.repositories;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.wtrader.cep.data.repositories.ICompanyRepository;
import org.wtrader.cep.utils.data.entities.CompanyEntity;
import org.wtrader.test.cep.data.utils.AbstractCepDatabaseTest;

public class CompanyRepositoryTest extends AbstractCepDatabaseTest {

	private static final Logger LOGGER = Logger.getLogger(CompanyRepositoryTest.class);

	@Inject
	private ICompanyRepository companyRepository;

	@Test
	public void testFindAll() {
		List<CompanyEntity> companies = this.companyRepository.findAll();

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(companies);
		}
	}

}
