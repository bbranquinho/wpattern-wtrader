package org.wtrader.cep.data.wrappers;

import javax.inject.Inject;
import javax.inject.Named;

import org.wtrader.cep.data.repositories.IBenefitStockRepository;
import org.wtrader.cep.data.repositories.ICompanyRepository;
import org.wtrader.cep.data.repositories.IIndexRecordRepository;
import org.wtrader.cep.data.repositories.IStockRecordRepository;
import org.wtrader.cep.data.repositories.IStockRepository;
import org.wtrader.cep.utils.data.entities.BenefitStockEntity;
import org.wtrader.cep.utils.data.entities.CompanyEntity;
import org.wtrader.cep.utils.data.entities.IndexRecordEntity;
import org.wtrader.cep.utils.data.entities.StockEntity;
import org.wtrader.cep.utils.data.entities.StockRecordEntity;
import org.wtrader.cep.utils.data.interfaces.IDataStorage;

@Named
public class DataStorageWrapper implements IDataStorage {

	@Inject
	private IBenefitStockRepository benefitStockRepository;

	@Inject
	private ICompanyRepository companyRepository;

	@Inject
	private IStockRepository stockRepository;

	@Inject
	private IStockRecordRepository stockRecordRepository;

	@Inject
	private IIndexRecordRepository indexRecordRepository;

	@Override
	public CompanyEntity saveCompany(CompanyEntity company) {
		return this.companyRepository.save(company);
	}

	@Override
	public StockEntity saveStock(StockEntity stock) {
		return this.stockRepository.save(stock);
	}

	@Override
	public IndexRecordEntity saveIndexRecord(IndexRecordEntity indexRecord) {
		return this.indexRecordRepository.save(indexRecord);
	}

	@Override
	public StockRecordEntity saveStockRecord(StockRecordEntity stockRecord) {
		return this.stockRecordRepository.save(stockRecord);
	}

	@Override
	public void deleteCompany(CompanyEntity company) {
		this.companyRepository.delete(company);
	}

	@Override
	public void deleteBenefitStock(BenefitStockEntity benefit) {
		this.benefitStockRepository.delete(benefit);
	}

	@Override
	public void deleteStock(StockEntity stock) {
		this.stockRepository.delete(stock);
	}

	@Override
	public BenefitStockEntity saveBenefitStock(BenefitStockEntity benefitEntity) {
		return this.benefitStockRepository.save(benefitEntity);
	}

}
