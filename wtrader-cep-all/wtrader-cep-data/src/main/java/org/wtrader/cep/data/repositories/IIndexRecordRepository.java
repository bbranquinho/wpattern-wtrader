package org.wtrader.cep.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.wtrader.cep.utils.data.entities.IndexRecordEntity;
import org.wtrader.cep.utils.data.entities.StockEntity;

public interface IIndexRecordRepository extends JpaRepository<IndexRecordEntity, Long> {

	@Query(value = "SELECT COUNT(*) FROM IndexRecordEntity as i WHERE (i.stock = ?1)")
	public long countByStock(StockEntity stock);

	public List<IndexRecordEntity> findByStockOrderByTradeDateAsc(StockEntity stock);

}
