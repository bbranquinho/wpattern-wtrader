package org.wtrader.loader.utils.beans;

import java.util.Date;
import java.util.List;

public class CompanyBean extends BaseBean {

	private static final long serialVersionUID = 201401031634L;

	private String tradeName;

	private String codeCvm;

	private List<String> stocks;

	private Date unitaryQuotationSince;

	private List<BenefitStockBean> benefitsInStock;

	public String getTradeName() {
		return this.tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public List<String> getStocks() {
		return this.stocks;
	}

	public void setStocks(List<String> stocks) {
		this.stocks = stocks;
	}

	public Date getUnitaryQuotationSince() {
		return this.unitaryQuotationSince;
	}

	public void setUnitaryQuotationSince(Date unitaryQuotationSince) {
		this.unitaryQuotationSince = unitaryQuotationSince;
	}

	public List<BenefitStockBean> getBenefitsInStock() {
		return this.benefitsInStock;
	}

	public void setBenefitsInStock(List<BenefitStockBean> benefitsInStock) {
		this.benefitsInStock = benefitsInStock;
	}

	public String getCodeCvm() {
		return this.codeCvm;
	}

	public void setCodeCvm(String codeCvm) {
		this.codeCvm = codeCvm;
	}

}
