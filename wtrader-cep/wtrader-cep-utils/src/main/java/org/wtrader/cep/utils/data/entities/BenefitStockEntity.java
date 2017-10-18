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

import org.wtrader.cep.utils.data.enums.BenefitType;
import org.wtrader.cep.utils.data.utils.BaseEntity;


@Entity
@Table(name="tb_benefit_stock")
@AttributeOverride(name = "id", column = @Column(name = "id"))
public class BenefitStockEntity extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	@Column(name = "businesses_with_up")
	private Date businessesWithUp;

	@Temporal(TemporalType.DATE)
	private Date deliberate;

	@Column(name = "factor")
	private Double factor;

	@Temporal(TemporalType.DATE)
	@Column(name = "shares_in_credit")
	private Date sharesInCredit;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private CompanyEntity company;

	@ManyToOne
	@JoinColumn(name = "benefit_type_id")
	private BenefitTypeEntity benefitType;

	public BenefitStockEntity() {
	}

	@Override
	public void setId(Long id) {
		super.setId(id);
	}

	public Date getBusinessesWithUp() {
		return this.businessesWithUp;
	}

	public void setBusinessesWithUp(Date businessesWithUp) {
		this.businessesWithUp = businessesWithUp;
	}

	public Date getDeliberate() {
		return this.deliberate;
	}

	public void setDeliberate(Date deliberate) {
		this.deliberate = deliberate;
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

	public CompanyEntity getCompany() {
		return this.company;
	}

	public void setCompany(CompanyEntity company) {
		this.company = company;
	}

	public BenefitTypeEntity getBenefitType() {
		return this.benefitType;
	}

	public BenefitType getType() {
		if (this.benefitType == null) {
			return null;
		}

		return this.benefitType.getType();
	}

	public void setBenefitType(BenefitTypeEntity benefitType) {
		this.benefitType = benefitType;
	}

}
