package org.wtrader.cep.utils.data.interfaces;

import java.util.List;

import org.wtrader.cep.utils.data.entities.IndexRecordEntity;
import org.wtrader.cep.utils.data.entities.StockEntity;

public interface IIndexRecordData {

	public long countByStock(StockEntity stock);

	public List<IndexRecordEntity> findByStockOrderByTradeDateAsc(StockEntity stock);

}
