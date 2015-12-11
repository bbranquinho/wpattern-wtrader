package org.wtrader.cep.utils.data.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.wtrader.cep.utils.data.utils.BaseEntity;


@Entity
@Table(name="tb_stock")
@AttributeOverride(name = "id", column = @Column(name = "id"))
public class StockEntity extends BaseEntity<Long> {

	private static final long serialVersionUID = 201401072228L;

	@Column(name = "name")
	private String name;

	@Column(name = "was_normalized")
	private boolean wasNormalized;

	@Column(name = "empty_records_percentual")
	private Double emptyRecordsPercentual;

	@ManyToOne
	@JoinColumn(name="company_id")
	private CompanyEntity company;

	public StockEntity() {
	}

	@Override
	public void setId(Long id) {
		super.setId(id);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CompanyEntity getCompany() {
		return this.company;
	}

	public void setCompany(CompanyEntity company) {
		this.company = company;
	}

	public boolean getWasNormalized() {
		return this.wasNormalized;
	}

	public void setWasNormalized(boolean wasNormalized) {
		this.wasNormalized = wasNormalized;
	}

	public Double getEmptyRecordsPercentual() {
		return this.emptyRecordsPercentual;
	}

	public void setEmptyRecordsPercentual(Double emptyRecordsPercentual) {
		this.emptyRecordsPercentual = emptyRecordsPercentual;
	}

}
