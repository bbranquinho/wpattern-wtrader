package org.wtrader.cep.utils.data.interfaces;

import java.util.Date;
import java.util.List;

import org.wtrader.cep.utils.data.entities.BenefitStockEntity;
import org.wtrader.cep.utils.data.entities.CompanyEntity;

public interface IBenefitStockData {

	public List<BenefitStockEntity> findByCompany(CompanyEntity company);

	public List<BenefitStockEntity> findByCompanyOrderByBusinessesWithUpAsc(CompanyEntity company);

	public BenefitStockEntity findByDeliberateAndCompany(Date deliberate, CompanyEntity company);

}
