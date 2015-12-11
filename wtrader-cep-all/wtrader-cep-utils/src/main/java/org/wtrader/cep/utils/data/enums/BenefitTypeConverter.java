package org.wtrader.cep.utils.data.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BenefitTypeConverter implements AttributeConverter<BenefitType, String> {

	@Override
	public String convertToDatabaseColumn(BenefitType benefitType) {
		return benefitType.getName();
	}

	@Override
	public BenefitType convertToEntityAttribute(String name) {
		return BenefitType.parser(name);
	}

}
