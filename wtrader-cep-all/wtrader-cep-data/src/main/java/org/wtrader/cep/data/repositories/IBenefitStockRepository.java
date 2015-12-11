package org.wtrader.cep.data.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtrader.cep.utils.data.entities.BenefitStockEntity;
import org.wtrader.cep.utils.data.entities.CompanyEntity;

public interface IBenefitStockRepository extends JpaRepository<BenefitStockEntity, Long> {

	public BenefitStockEntity findByDeliberateAndCompany(Date deliberate, CompanyEntity company);

	public List<BenefitStockEntity> findByCompany(CompanyEntity company);

	public List<BenefitStockEntity> findByCompanyOrderByBusinessesWithUpAsc(CompanyEntity company);

}
