package org.wtrader.cep.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtrader.cep.utils.data.entities.BenefitTypeEntity;

public interface IBenefitTypeRepository extends JpaRepository<BenefitTypeEntity, Long> {

}
