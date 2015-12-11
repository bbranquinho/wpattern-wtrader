package org.wtrader.cep.utils.data.interfaces;

import org.wtrader.cep.utils.data.entities.BenefitStockEntity;
import org.wtrader.cep.utils.data.entities.CompanyEntity;
import org.wtrader.cep.utils.data.entities.IndexRecordEntity;
import org.wtrader.cep.utils.data.entities.StockEntity;
import org.wtrader.cep.utils.data.entities.StockRecordEntity;

public interface IDataStorage {

	public CompanyEntity saveCompany(CompanyEntity company);

	public StockEntity saveStock(StockEntity stock);

	public IndexRecordEntity saveIndexRecord(IndexRecordEntity indexRecord);

	public StockRecordEntity saveStockRecord(StockRecordEntity stockRecord);

	public BenefitStockEntity saveBenefitStock(BenefitStockEntity benefitEntity);

	public void deleteCompany(CompanyEntity company);

	public void deleteBenefitStock(BenefitStockEntity benefit);

	public void deleteStock(StockEntity stock);

}
