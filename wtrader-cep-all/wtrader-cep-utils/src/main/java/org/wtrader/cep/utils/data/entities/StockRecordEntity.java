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
@Table(name="tb_stock_record")
@AttributeOverride(name = "id", column = @Column(name = "id"))
public class StockRecordEntity extends BaseEntity<Long> implements Cloneable {

	private static final long serialVersionUID = 201401072216L;

	@Column(name="average_negotiation_price")
	private Double averageNegotiationPrice;

	@Column(name="closing_negotiation_price")
	private Double closingNegotiationPrice;

	@Column(name="highest_buy_offer_price")
	private Double highestBuyOfferPrice;

	@Column(name="highest_negotiation_price")
	private Double highestNegotiationPrice;

	@Column(name="lowest_negotiation_price")
	private Double lowestNegotiationPrice;

	@Column(name="lowest_sell_offer_price")
	private Double lowestSellOfferPrice;

	@Column(name="start_price")
	private Double startPrice;

	@Column(name="total_business")
	private Long totalBusiness;

	@Column(name="total_negotiation")
	private Long totalNegotiation;

	@Column(name="total_volume")
	private Double totalVolume;

	@Temporal(TemporalType.DATE)
	@Column(name="trade_date")
	private Date tradeDate;

	@ManyToOne
	@JoinColumn(name="stock_id")
	private StockEntity stock;

	public StockRecordEntity() {
	}

	@Override
	public void setId(Long id) {
		super.setId(id);
	}

	@Override
	public StockRecordEntity clone() {
		try {
			StockRecordEntity clone = (StockRecordEntity) super.clone();

			if (this.averageNegotiationPrice != null) {
				clone.averageNegotiationPrice = new Double(this.averageNegotiationPrice);
			}

			if (this.closingNegotiationPrice != null) {
				clone.closingNegotiationPrice = new Double(this.closingNegotiationPrice);
			}

			if (this.highestBuyOfferPrice != null) {
				clone.highestBuyOfferPrice = new Double(this.highestBuyOfferPrice);
			}

			if (this.highestNegotiationPrice != null) {
				clone.highestNegotiationPrice = new Double(this.highestNegotiationPrice);
			}

			if (this.lowestNegotiationPrice != null) {
				clone.lowestNegotiationPrice = new Double(this.lowestNegotiationPrice);
			}

			if (this.lowestSellOfferPrice != null) {
				clone.lowestSellOfferPrice = new Double(this.lowestSellOfferPrice);
			}

			if (this.startPrice != null) {
				clone.startPrice = new Double(this.startPrice);
			}

			if (this.totalBusiness != null) {
				clone.totalBusiness = new Long(this.totalBusiness);
			}

			if (this.totalNegotiation != null) {
				clone.totalNegotiation = new Long(this.totalNegotiation);
			}

			if (this.totalVolume != null) {
				clone.totalVolume = new Double(this.totalVolume);
			}

			if (this.tradeDate != null) {
				clone.tradeDate = (Date) this.tradeDate.clone();
			}

			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e.getMessage(), e);
		}
	}

	public Double getAverageNegotiationPrice() {
		return this.averageNegotiationPrice;
	}

	public void setAverageNegotiationPrice(Double averageNegotiationPrice) {
		this.averageNegotiationPrice = averageNegotiationPrice;
	}

	public Double getClosingNegotiationPrice() {
		return this.closingNegotiationPrice;
	}

	public void setClosingNegotiationPrice(Double closingNegotiationPrice) {
		this.closingNegotiationPrice = closingNegotiationPrice;
	}

	public Double getHighestBuyOfferPrice() {
		return this.highestBuyOfferPrice;
	}

	public void setHighestBuyOfferPrice(Double highestBuyOfferPrice) {
		this.highestBuyOfferPrice = highestBuyOfferPrice;
	}

	public Double getHighestNegotiationPrice() {
		return this.highestNegotiationPrice;
	}

	public void setHighestNegotiationPrice(Double highestNegotiationPrice) {
		this.highestNegotiationPrice = highestNegotiationPrice;
	}

	public Double getLowestNegotiationPrice() {
		return this.lowestNegotiationPrice;
	}

	public void setLowestNegotiationPrice(Double lowestNegotiationPrice) {
		this.lowestNegotiationPrice = lowestNegotiationPrice;
	}

	public Double getLowestSellOfferPrice() {
		return this.lowestSellOfferPrice;
	}

	public void setLowestSellOfferPrice(Double lowestSellOfferPrice) {
		this.lowestSellOfferPrice = lowestSellOfferPrice;
	}

	public Double getStartPrice() {
		return this.startPrice;
	}

	public void setStartPrice(Double startPrice) {
		this.startPrice = startPrice;
	}

	public Long getTotalBusiness() {
		return this.totalBusiness;
	}

	public void setTotalBusiness(Long totalBusiness) {
		this.totalBusiness = totalBusiness;
	}

	public Long getTotalNegotiation() {
		return this.totalNegotiation;
	}

	public void setTotalNegotiation(Long totalNegotiation) {
		this.totalNegotiation = totalNegotiation;
	}

	public Double getTotalVolume() {
		return this.totalVolume;
	}

	public void setTotalVolume(Double totalVolume) {
		this.totalVolume = totalVolume;
	}

	public Date getTradeDate() {
		return this.tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public StockEntity getStock() {
		return this.stock;
	}

	public void setStock(StockEntity stock) {
		this.stock = stock;
	}

}
