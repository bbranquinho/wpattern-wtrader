package org.wtrader.cep.utils.planner.interfaces;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort.Direction;
import org.wtrader.cep.utils.data.entities.StockRecordEntity;

public interface IPortfolioStockData {

	public List<StockRecordEntity> findClosingPricesByDateAndStock(Date initialDate, Date finalDate, String stockName, Direction direction);

}
