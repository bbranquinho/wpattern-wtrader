package org.wtrader.loader.utils.beans;

import java.util.Date;


public class StockRecordBean extends BaseBean {

	private static final long serialVersionUID = 201303270724L;

	private Date tradeDate;

	private Long bdi;

	private String stockName;

	private Double startPrice;

	private Double highestNegotiationPrice;

	private Double lowestNegotiationPrice;

	private Double averageNegotiationPrice;

	private Double closingNegotiationPrice;

	private Double highestBuyOfferPrice;

	private Double lowestSellOfferPrice;

	private Long totalBusiness;

	private Long totalNegotiation;

	private Double totalVolume;

	public String getStockName() {
		return this.stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public Long getBdi() {
		return this.bdi;
	}

	public void setBdi(Long bdi) {
		this.bdi = bdi;
	}

	public Double getStartPrice() {
		return this.startPrice;
	}

	public void setStartPrice(Double startPrice) {
		this.startPrice = startPrice;
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

	public Double getLowestSellOfferPrice() {
		return this.lowestSellOfferPrice;
	}

	public void setLowestSellOfferPrice(Double lowestSellOfferPrice) {
		this.lowestSellOfferPrice = lowestSellOfferPrice;
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

}
