package org.wtrader.cep.utils.data.entities;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.wtrader.cep.utils.data.utils.BaseEntity;


@Entity
@Table(name="tb_company")
@AttributeOverride(name = "id", column = @Column(name = "id"))
public class CompanyEntity extends BaseEntity<Long> {

	private static final long serialVersionUID = 201401072232L;

	@Column(name = "name")
	private String name;

	@Column(name = "code_cvm")
	private String codeCvm;

	@Temporal(TemporalType.DATE)
	@Column(name="unitary_quotation_since")
	private Date unitaryQuotationSince;

	public CompanyEntity() {
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

	public String getCodeCvm() {
		return this.codeCvm;
	}

	public void setCodeCvm(String codeCvm) {
		this.codeCvm = codeCvm;
	}

	public Date getUnitaryQuotationSince() {
		return this.unitaryQuotationSince;
	}

	public void setUnitaryQuotationSince(Date unitaryQuotationSince) {
		this.unitaryQuotationSince = unitaryQuotationSince;
	}

}
