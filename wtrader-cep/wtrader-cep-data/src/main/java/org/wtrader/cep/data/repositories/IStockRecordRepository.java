package org.wtrader.cep.data.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.wtrader.cep.utils.data.beans.StockDataBean;
import org.wtrader.cep.utils.data.entities.StockEntity;
import org.wtrader.cep.utils.data.entities.StockRecordEntity;

public interface IStockRecordRepository extends JpaRepository<StockRecordEntity, Long> {

	// GROUP

	@Query(value = "SELECT r.tradeDate FROM StockRecordEntity r GROUP BY r.tradeDate ORDER BY r.tradeDate")
	public List<Date> groupByTradeDate();

	// COUNT

	@Query(value = "SELECT COUNT(r) FROM StockRecordEntity r WHERE (r.stock = ?1)")
	public int countByStock(StockEntity stock);

	@Query(value = "SELECT COUNT(DISTINCT r.tradeDate) FROM StockRecordEntity r WHERE (r.tradeDate >= ?1) AND (r.tradeDate <= ?2)")
	public int countByTradeDate(Date start, Date end);

	@Query(value = "SELECT COUNT(DISTINCT r.tradeDate) FROM StockRecordEntity r WHERE (r.tradeDate >= ?1) AND (r.tradeDate <= ?2) AND (r.stock = ?3)")
	public int countByTradeDateAndStock(Date start, Date end, StockEntity stock);

	// DATES

	@Query(value = "SELECT MIN(r.tradeDate) FROM StockRecordEntity as r")
	public Date findMinDate();

	@Query(value = "SELECT MAX(r.tradeDate) FROM StockRecordEntity as r")
	public Date findMaxDate();

	@Query(value = "SELECT r.tradeDate FROM StockRecordEntity as r WHERE r.stock = ?1 ORDER BY r.tradeDate")
	public List<Date> findAllDatesByStock(StockEntity stock);

	@Query(value = "SELECT DISTINCT r.tradeDate FROM StockRecordEntity as r WHERE (r.tradeDate >= ?1) AND (r.tradeDate <= ?2) ORDER BY r.tradeDate")
	public List<Date> findDatesByTradeDateBetween(Date start, Date end);

	@Query(value = "SELECT r.tradeDate FROM StockRecordEntity as r WHERE (r.stock = ?1) AND (r.tradeDate < ?2) ORDER BY r.tradeDate DESC")
	public List<Date> findDatesByDate(StockEntity stock, Date date, Pageable page);

	// STOCK

	public List<StockRecordEntity> findByStock(StockEntity stock);

	public List<StockRecordEntity> findByStock(StockEntity stock, Sort sort);

	public List<StockRecordEntity> findByStock(StockEntity stock, Pageable pageable);

	public List<StockRecordEntity> findByStockOrderByTradeDateAsc(StockEntity stock);

	public List<StockRecordEntity> findByStockOrderByTradeDateDesc(StockEntity stock);

	public List<StockRecordEntity> findByStockOrderByTradeDateAsc(StockEntity stock, Pageable pageable);

	public List<StockRecordEntity> findByStockAndTradeDate(StockEntity stock, Date tradeDate);

	// BETWEEN
	public List<StockRecordEntity> findByTradeDateBetweenAndStock(Date start, Date end, StockEntity stock);

	public List<StockRecordEntity> findByTradeDateBetweenAndStock(Date start, Date end, StockEntity stock, Sort sort);

	public List<StockRecordEntity> findByTradeDateBetweenAndStockOrderByTradeDateAsc(Date start, Date end, StockEntity stock);

	// GREAT THAN
	public List<StockRecordEntity> findByTradeDateGreaterThanAndStock(Date tradeDate, StockEntity stock);

	public List<StockRecordEntity> findByTradeDateGreaterThanAndStock(Date tradeDate, StockEntity stock, Sort sort);

	public List<StockRecordEntity> findByTradeDateGreaterThanAndStock(Date tradeDate, StockEntity stock, Pageable pageable);

	public List<StockRecordEntity> findByTradeDateGreaterThanAndStockOrderByTradeDateAsc(Date tradeDate, StockEntity stock);

	public List<StockRecordEntity> findByTradeDateGreaterThanAndStockOrderByTradeDateDesc(Date tradeDate, StockEntity stock);

	public List<StockRecordEntity> findByTradeDateGreaterThanAndStockOrderByTradeDateAsc(Date tradeDate, StockEntity stock, Pageable pageable);

	// LESS THAN
	public List<StockRecordEntity> findByTradeDateLessThanAndStock(Date tradeDate, StockEntity stock);

	public List<StockRecordEntity> findByTradeDateLessThanAndStock(Date tradeDate, StockEntity stock, Sort sort);

	public List<StockRecordEntity> findByTradeDateLessThanAndStock(Date tradeDate, StockEntity stock, Pageable pageable);

	public List<StockRecordEntity> findByTradeDateLessThanAndStockOrderByTradeDateAsc(Date tradeDate, StockEntity stock);

	public List<StockRecordEntity> findByTradeDateLessThanAndStockOrderByTradeDateAsc(Date tradeDate, StockEntity stock, Pageable pageable);

	// STOCK DATA
	@Query("SELECT new org.wtrader.cep.utils.data.beans.StockDataBean(s.tradeDate, s.closingNegotiationPrice) FROM StockRecordEntity AS s WHERE (s.stock = ?1) AND (s.tradeDate >= ?2) AND (s.tradeDate <= ?3) ORDER BY s.tradeDate")
	public List<StockDataBean> findStockDataByTradeDateBetween(StockEntity stock, Date initialDate, Date finalDate);

	@Query("SELECT new org.wtrader.cep.utils.data.beans.StockDataBean(s.tradeDate, s.closingNegotiationPrice) FROM StockRecordEntity AS s WHERE (s.stock = ?1) AND (s.tradeDate < ?2) ORDER BY s.tradeDate DESC")
	public List<StockDataBean> findStockDataByTradeDateBefore(StockEntity stock, Date date, Pageable pageable);

	@Query("SELECT new org.wtrader.cep.utils.data.beans.StockDataBean(s.tradeDate, s.closingNegotiationPrice) FROM StockRecordEntity AS s WHERE (s.stock = ?1) AND (s.tradeDate > ?2) ORDER BY s.tradeDate")
	public List<StockDataBean> findStockDataByTradeDateAfter(StockEntity stock, Date initialDate, Pageable pageRequest);

}
