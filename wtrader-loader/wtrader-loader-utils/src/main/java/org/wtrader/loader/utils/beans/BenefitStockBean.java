package org.wtrader.loader.utils.beans;

import java.util.Date;

public class BenefitStockBean extends BaseBean {

	private static final long serialVersionUID = 201401060840L;

	private String name;

	private Date deliberate;

	private Date businessesWithUp;

	private Double factor;

	private Date sharesInCredit;

	public BenefitStockBean() {
	}

	public BenefitStockBean(String name, Date deliberate, Date businessesWithUp, double factor, Date sharesInCredit) {
		super();
		this.name = name;
		this.deliberate = deliberate;
		this.businessesWithUp = businessesWithUp;
		this.factor = factor;
		this.sharesInCredit = sharesInCredit;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDeliberate() {
		return this.deliberate;
	}

	public void setDeliberate(Date deliberate) {
		this.deliberate = deliberate;
	}

	public Date getBusinessesWithUp() {
		return this.businessesWithUp;
	}

	public void setBusinessesWithUp(Date businessesWithUp) {
		this.businessesWithUp = businessesWithUp;
	}

	public Double getFactor() {
		return this.factor;
	}

	public void setFactor(Double factor) {
		this.factor = factor;
	}

	public Date getSharesInCredit() {
		return this.sharesInCredit;
	}

	public void setSharesInCredit(Date sharesInCredit) {
		this.sharesInCredit = sharesInCredit;
	}

}
