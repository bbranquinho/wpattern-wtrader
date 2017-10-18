package org.wtrader.cep.data.wrappers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.wtrader.cep.data.repositories.IIndexRecordRepository;
import org.wtrader.cep.utils.data.entities.IndexRecordEntity;
import org.wtrader.cep.utils.data.entities.StockEntity;
import org.wtrader.cep.utils.data.interfaces.IIndexRecordData;

@Named
public class IndexRecordWrapper implements IIndexRecordData {

	@Inject
	private IIndexRecordRepository indexRecordRepository;

	@Override
	public long countByStock(StockEntity stock) {
		return this.indexRecordRepository.countByStock(stock);
	}

	@Override
	public List<IndexRecordEntity> findByStockOrderByTradeDateAsc(StockEntity stock) {
		return this.indexRecordRepository.findByStockOrderByTradeDateAsc(stock);
	}

}
