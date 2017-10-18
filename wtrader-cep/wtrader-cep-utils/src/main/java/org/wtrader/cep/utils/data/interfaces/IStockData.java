package org.wtrader.cep.utils.data.interfaces;

import java.util.Date;
import java.util.List;

import org.wtrader.cep.utils.data.entities.CompanyEntity;
import org.wtrader.cep.utils.data.entities.StockEntity;

public interface IStockData {

	public List<StockEntity> findAll();

	public StockEntity findByName(String stockName);

	public List<StockEntity> findByCompany(CompanyEntity company);

	public int countByCompany(CompanyEntity company);

	public List<String> findAllNames();

	public List<String> findStocksByTradeDateAndEmptyPercentual(double emptyPercentual, Date tradeDate);

}
