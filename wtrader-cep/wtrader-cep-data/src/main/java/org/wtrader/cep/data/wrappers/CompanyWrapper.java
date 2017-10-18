package org.wtrader.cep.data.wrappers;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.wtrader.cep.data.repositories.ICompanyRepository;
import org.wtrader.cep.utils.data.entities.CompanyEntity;
import org.wtrader.cep.utils.data.interfaces.ICompanyData;

@Named
public class CompanyWrapper implements ICompanyData {

	@Inject
	private ICompanyRepository companyRepository;

	@Override
	public List<CompanyEntity> findAll() {
		return this.companyRepository.findAll();
	}

	@Override
	public List<CompanyEntity> findByUnitaryQuotationSince(Date currentDate) {
		return this.companyRepository.findByUnitaryQuotationSince(currentDate);
	}

	@Override
	public CompanyEntity findByCodeCvm(String codeCvm) {
		return this.companyRepository.findByCodeCvm(codeCvm);
	}

}
