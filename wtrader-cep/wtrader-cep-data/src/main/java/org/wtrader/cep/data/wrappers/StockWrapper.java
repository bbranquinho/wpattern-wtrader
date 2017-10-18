package org.wtrader.cep.data.wrappers;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.wtrader.cep.data.repositories.IStockRepository;
import org.wtrader.cep.utils.data.entities.CompanyEntity;
import org.wtrader.cep.utils.data.entities.StockEntity;
import org.wtrader.cep.utils.data.interfaces.IStockData;

@Named
public class StockWrapper implements IStockData {

	@Inject
	private IStockRepository stockRepository;

	@Override
	public List<StockEntity> findAll() {
		return this.stockRepository.findAll();
	}

	@Override
	public StockEntity findByName(String stockName) {
		return this.stockRepository.findByName(stockName);
	}

	@Override
	public List<StockEntity> findByCompany(CompanyEntity company) {
		return this.stockRepository.findByCompany(company);
	}

	@Override
	public int countByCompany(CompanyEntity company) {
		return this.stockRepository.countByCompany(company);
	}

	@Override
	public List<String> findAllNames() {
		return this.stockRepository.findAllNames();
	}

	@Override
	public List<String> findStocksByTradeDateAndEmptyPercentual(double emptyPercentual, Date tradeDate) {
		return this.stockRepository.findByTradeDateAndEmptyRecordsPercentual(emptyPercentual, tradeDate);
	}

}
