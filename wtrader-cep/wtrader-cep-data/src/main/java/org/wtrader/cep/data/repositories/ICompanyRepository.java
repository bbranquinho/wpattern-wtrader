package org.wtrader.cep.data.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.wtrader.cep.utils.data.entities.CompanyEntity;

public interface ICompanyRepository extends JpaRepository<CompanyEntity, Long> {

	public CompanyEntity findByCodeCvm(String codeCvm);

	public List<CompanyEntity> findByUnitaryQuotationSince(Date unitaryQuotationSince);

	@Query(value = "SELECT c FROM CompanyEntity AS c WHERE (c.unitaryQuotationSince IS NOT NULL) AND (c.unitaryQuotationSince > ?1)")
	public List<CompanyEntity> findCompaniesByUnitaryQuotationSince(Date unitaryQuotationSince);

}
