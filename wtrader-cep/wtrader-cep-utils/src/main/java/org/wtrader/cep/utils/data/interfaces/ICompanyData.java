package org.wtrader.cep.utils.data.interfaces;

import java.util.Date;
import java.util.List;

import org.wtrader.cep.utils.data.entities.CompanyEntity;

public interface ICompanyData {

	public List<CompanyEntity> findAll();

	public List<CompanyEntity> findByUnitaryQuotationSince(Date currentDate);

	public CompanyEntity findByCodeCvm(String codeCvm);

}
