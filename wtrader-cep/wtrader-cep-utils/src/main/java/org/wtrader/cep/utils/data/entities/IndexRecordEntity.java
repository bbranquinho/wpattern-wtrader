package org.wtrader.cep.utils.data.entities;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.wtrader.cep.utils.data.utils.BaseEntity;


@Entity
@Table(name="tb_index_record")
@AttributeOverride(name = "id", column = @Column(name = "id"))
public class IndexRecordEntity extends BaseEntity<Long> implements Cloneable {

	private static final long serialVersionUID = 201401072216L;

	@Temporal(TemporalType.DATE)
	@Column(name="trade_date")
	private Date tradeDate;

	@Column(name="open")
	private Double open;

	@Column(name="high")
	private Double high;

	@Column(name="low")
	private Double low;

	@Column(name="close")
	private Double close;

	@Column(name="volume")
	private Long volume;

	@ManyToOne
	@JoinColumn(name="stock_id")
	private StockEntity stock;

	public IndexRecordEntity() {
	}

	@Override
	public void setId(Long id) {
		super.setId(id);
	}

	@Override
	public IndexRecordEntity clone() {
		try {
			IndexRecordEntity clone = (IndexRecordEntity) super.clone();

			if (this.tradeDate != null) {
				clone.tradeDate = (Date) this.tradeDate.clone();
			}

			if (this.open != null) {
				clone.open = new Double(this.open);
			}

			if (this.high != null) {
				clone.high = new Double(this.high);
			}

			if (this.low != null) {
				clone.low = new Double(this.low);
			}

			if (this.close != null) {
				clone.close = new Double(this.close);
			}

			if (this.volume != null) {
				clone.volume = new Long(this.volume);
			}

			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e.getMessage(), e);
		}
	}

	public Date getTradeDate() {
		return this.tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public Double getOpen() {
		return this.open;
	}

	public void setOpen(Double open) {
		this.open = open;
	}

	public Double getHigh() {
		return this.high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public Double getLow() {
		return this.low;
	}

	public void setLow(Double low) {
		this.low = low;
	}

	public Double getClose() {
		return this.close;
	}

	public void setClose(Double close) {
		this.close = close;
	}

	public Long getVolume() {
		return this.volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

	public StockEntity getStock() {
		return this.stock;
	}

	public void setStock(StockEntity stock) {
		this.stock = stock;
	}

}
