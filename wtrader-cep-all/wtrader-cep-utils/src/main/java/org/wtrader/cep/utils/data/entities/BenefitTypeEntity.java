package org.wtrader.cep.utils.data.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.wtrader.cep.utils.data.enums.BenefitType;
import org.wtrader.cep.utils.data.enums.BenefitTypeConverter;
import org.wtrader.cep.utils.data.utils.BaseEntity;


@Entity
@Table(name="tb_benefit_type")
@AttributeOverride(name = "id", column = @Column(name = "id"))
public class BenefitTypeEntity extends BaseEntity<Long> {

	private static final long serialVersionUID = 201401072234L;

	@Column(name = "name")
	@Convert(converter = BenefitTypeConverter.class)
	private BenefitType type;

	public BenefitTypeEntity() {
	}

	public BenefitType getType() {
		return this.type;
	}

	public void setType(BenefitType type) {
		this.type = type;
	}

}
