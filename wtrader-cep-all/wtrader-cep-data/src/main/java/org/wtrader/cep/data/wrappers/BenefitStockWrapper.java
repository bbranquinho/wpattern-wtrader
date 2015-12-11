package org.wtrader.cep.data.wrappers;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.wtrader.cep.data.repositories.IBenefitStockRepository;
import org.wtrader.cep.utils.data.entities.BenefitStockEntity;
import org.wtrader.cep.utils.data.entities.CompanyEntity;
import org.wtrader.cep.utils.data.interfaces.IBenefitStockData;

@Named
public class BenefitStockWrapper implements IBenefitStockData {

	@Inject
	private IBenefitStockRepository benefitStockRepository;

	@Override
	public List<BenefitStockEntity> findByCompany(CompanyEntity company) {
		return this.benefitStockRepository.findByCompany(company);
	}

	@Override
	public List<BenefitStockEntity> findByCompanyOrderByBusinessesWithUpAsc(CompanyEntity company) {
		return this.benefitStockRepository.findByCompanyOrderByBusinessesWithUpAsc(company);
	}

	@Override
	public BenefitStockEntity findByDeliberateAndCompany(Date deliberate, CompanyEntity company) {
		return this.benefitStockRepository.findByDeliberateAndCompany(deliberate, company);
	}

}
