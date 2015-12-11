package org.wtrader.cep.data.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.wtrader.cep.utils.data.entities.CompanyEntity;
import org.wtrader.cep.utils.data.entities.StockEntity;

public interface IStockRepository extends JpaRepository<StockEntity, Long> {

	public StockEntity findByName(String name);

	public List<StockEntity> findByCompany(CompanyEntity company);

	@Query(value = "SELECT s.name FROM StockEntity s ORDER BY s.name")
	public List<String> findAllNames();

	@Query(value = "SELECT COUNT(*) FROM StockEntity s WHERE s.company = ?1 ORDER BY s.name")
	public int countByCompany(CompanyEntity company);

	@Query(value = "SELECT s.name FROM StockEntity s WHERE (s.emptyRecordsPercentual >= ?1) AND ((SELECT MIN(r.tradeDate) FROM StockRecordEntity r WHERE r.stock = s) <= ?2) ORDER BY s.name")
	public List<String> findByTradeDateAndEmptyRecordsPercentual(double emptyPercentual, Date tradeDate);

}
