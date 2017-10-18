package org.wtrader.cep.data.wrappers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.wtrader.cep.data.repositories.IBenefitTypeRepository;
import org.wtrader.cep.utils.data.entities.BenefitTypeEntity;
import org.wtrader.cep.utils.data.interfaces.IBenefitTypeData;

@Named
public class BenefitTypeData implements IBenefitTypeData {

	@Inject
	private IBenefitTypeRepository benefitTypeRepository;

	@Override
	public List<BenefitTypeEntity> findAll() {
		return this.benefitTypeRepository.findAll();
	}

}
