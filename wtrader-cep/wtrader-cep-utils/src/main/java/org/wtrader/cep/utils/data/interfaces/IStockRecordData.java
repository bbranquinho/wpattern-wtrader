package org.wtrader.cep.utils.data.interfaces;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.wtrader.cep.utils.data.beans.StockDataBean;
import org.wtrader.cep.utils.data.entities.StockEntity;
import org.wtrader.cep.utils.data.entities.StockRecordEntity;

public interface IStockRecordData {

	public List<StockRecordEntity> findByTradeDateBetweenAndStockOrderByTradeDateAsc(Date unitaryQuotationSince,
			Date businessesWithUp, StockEntity stock);

	public List<Date> groupByTradeDate();

	public List<StockRecordEntity> findByStockOrderByTradeDateAsc(StockEntity stock);

	public List<StockRecordEntity> findByStock(StockEntity stock, PageRequest pageRequest);

	public int countByStock(StockEntity stock);

	public List<StockRecordEntity> findByTradeDateBetweenAndStock(Date initialDate, Date finalDate, StockEntity stock, Sort sort);

	public int countByTradeDate(Date initialDate, Date finalDate);

	public int countByTradeDateAndStock(Date initialDate, Date finalDate, StockEntity stockEntity);

	public List<StockDataBean> findStockDataBetweenAndBeforeTradeDate(String stock, int numDataBefore, int numDataAfter, Date initialDate, Date finalDate);

}
