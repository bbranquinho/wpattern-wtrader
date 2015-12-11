package org.wtrader.loader.crawler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.cep.utils.data.entities.BenefitStockEntity;
import org.wtrader.cep.utils.data.entities.BenefitTypeEntity;
import org.wtrader.cep.utils.data.entities.CompanyEntity;
import org.wtrader.cep.utils.data.entities.StockEntity;
import org.wtrader.cep.utils.data.enums.BenefitType;
import org.wtrader.cep.utils.data.interfaces.IBenefitStockData;
import org.wtrader.cep.utils.data.interfaces.IBenefitTypeData;
import org.wtrader.cep.utils.data.interfaces.ICompanyData;
import org.wtrader.cep.utils.data.interfaces.IDataStorage;
import org.wtrader.cep.utils.data.interfaces.IStockData;
import org.wtrader.loader.crawler.interfaces.ICrawlerAllCompanies;
import org.wtrader.loader.crawler.interfaces.ICrawlerEventosCorporativos;
import org.wtrader.loader.utils.beans.BenefitStockBean;
import org.wtrader.loader.utils.beans.CompanyBean;
import org.wtrader.loader.utils.interfaces.ICrawlerLoader;


@Named
public class CrawlerLoader implements ICrawlerLoader {

	private static final Logger LOGGER = Logger.getLogger(CrawlerLoader.class);

	@Inject
	private IBenefitStockData benefitStockData;

	@Inject
	private IBenefitTypeData benefitTypeData;

	@Inject
	private ICompanyData companyData;

	@Inject
	private IStockData stockData;

	@Inject
	private IDataStorage dataStorage;

	@Inject
	private ICrawlerAllCompanies crawlerAllCompanies;

	@Inject
	private ICrawlerEventosCorporativos crawlerEventosCorporativos;

	private Map<BenefitType, BenefitTypeEntity> benefitTypes;

	public CrawlerLoader() {
	}

	@Override
	public void load() {
		this.loadBenefitsTypes();
		List<String[]> companies = null;
		CompanyBean companyBean;

		try {
			companies = this.crawlerAllCompanies.retrieveAllCompanies();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		if (companies == null) {
			LOGGER.error("Problem to retrieve all companies.");
			return;
		}

		for (String[] company : companies) {
			try {
				companyBean = this.crawlerEventosCorporativos.retrieveCompany(company[0], company[1]);

				if (companyBean != null) {
					this.saveCompany(companyBean);
				} else {
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info(String.format("Company not founded to codigoCvm [%s] and companyName [%s].",
								company[0], company[1]));
					}
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	private void loadBenefitsTypes() {
		if (this.benefitTypes == null) {
			this.benefitTypes = new HashMap<BenefitType, BenefitTypeEntity>();

			for (BenefitTypeEntity benefit : this.benefitTypeData.findAll()) {
				this.benefitTypes.put(benefit.getType(), benefit);
			}
		}
	}

	private void saveCompany(CompanyBean company) {
		if ((company.getStocks() == null) || company.getStocks().isEmpty()) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(String.format("Company without shares on market [%s].", company));
			}
			return;
		}

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("Saving the company [%s].", company));
		}

		CompanyEntity companyEntity = this.saveAndGetCompanyEntity(company);
		this.saveStocks(companyEntity, company);
		this.saveBenefits(companyEntity, company);
	}

	private void saveStocks(CompanyEntity companyEntity, CompanyBean companyBean) {
		for (String stockName : companyBean.getStocks()) {
			if ((stockName == null) || stockName.isEmpty()) {
				LOGGER.warn(String.format("Stock name is empty [%s].", companyBean));
				continue;
			}

			StockEntity stockEntity = this.stockData.findByName(stockName);

			if (stockEntity == null) {
				stockEntity = new StockEntity();

				stockEntity.setCompany(companyEntity);
				stockEntity.setName(stockName);
				stockEntity.setWasNormalized(false);

				stockEntity = this.dataStorage.saveStock(stockEntity);

				if (LOGGER.isInfoEnabled()) {
					LOGGER.info(String.format("Stock saved [%s].", stockEntity));
				}
			} else {
				if (stockEntity.getCompany().getId() != companyEntity.getId()) {
					LOGGER.error(String.format("Stock [%s] associated with different companies [%s][%s].",
							stockName, stockEntity.getCompany().getId(), companyEntity.getId()));
				}
			}
		}
	}

	private void saveBenefits(CompanyEntity companyEntity, CompanyBean company) {
		if (company.getBenefitsInStock() == null)  {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("Company [%s] without benefits in stock.", company));
			}
			return;
		}

		for (BenefitStockBean benefit : company.getBenefitsInStock()) {
			BenefitStockEntity benefitEntity = this.benefitStockData.findByDeliberateAndCompany(benefit.getDeliberate(), companyEntity);

			if (benefitEntity != null) {
				continue;
			}

			BenefitTypeEntity benefitType = this.getBenefitType(benefit.getName());

			if (benefitType == null) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info(String.format("Benefit in Stock [%s] not recognized.", benefit));
				}
				continue;
			}

			benefitEntity = new BenefitStockEntity();

			benefitEntity.setBusinessesWithUp(benefit.getBusinessesWithUp());
			benefitEntity.setCompany(companyEntity);
			benefitEntity.setDeliberate(benefit.getDeliberate());
			benefitEntity.setFactor(benefit.getFactor());
			benefitEntity.setSharesInCredit(benefit.getSharesInCredit());
			benefitEntity.setBenefitType(benefitType);

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(String.format("Saving a new benefit [%s].", benefitEntity));
			}

			benefitEntity = this.dataStorage.saveBenefitStock(benefitEntity);

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(String.format("Benefit in stock saved [%s].", benefitEntity));
			}
		}
	}

	private BenefitTypeEntity getBenefitType(String name) {
		BenefitType benefitType = BenefitType.parser(name);

		if (benefitType == null) {
			if (name.startsWith("b")) {
				benefitType = BenefitType.BONIFICATION;
			} if (name.startsWith("c")) {
				benefitType = BenefitType.FISSION;
			} else if (name.startsWith("d")) {
				benefitType = BenefitType.SPLIT;
			} else if (name.startsWith("g")) {
				benefitType = BenefitType.INPLIT;
			} else if (name.startsWith("r")) {
				benefitType = BenefitType.RETURN_CAPITAL;
			}
		}

		if (benefitType == null) {
			return null;
		}

		return this.benefitTypes.get(benefitType);
	}

	private CompanyEntity saveAndGetCompanyEntity(CompanyBean company) {
		CompanyEntity companyEntity = this.companyData.findByCodeCvm(company.getCodeCvm());

		if (companyEntity == null) {
			companyEntity = new CompanyEntity();
			companyEntity.setName(company.getTradeName().toLowerCase());
			companyEntity.setCodeCvm(company.getCodeCvm());
			companyEntity.setUnitaryQuotationSince(company.getUnitaryQuotationSince());

			companyEntity = this.dataStorage.saveCompany(companyEntity);
		} else {
			boolean update = false;

			if (!companyEntity.getName().toLowerCase().equals(company.getTradeName())) {
				LOGGER.error(String.format("Code cvm associated with different companies [%s][%s].",
						companyEntity.getName(), company.getTradeName()));
				return companyEntity;
			}

			if (companyEntity.getUnitaryQuotationSince().compareTo(company.getUnitaryQuotationSince()) != 0) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info(String.format("Updating the record Unitary Quotation Since [%s] of company [%s].",
							company.getUnitaryQuotationSince(), companyEntity.getName()));
				}

				update = true;

				companyEntity.setUnitaryQuotationSince(company.getUnitaryQuotationSince());
			}

			if (update) {
				companyEntity = this.dataStorage.saveCompany(companyEntity);
			}
		}

		return companyEntity;
	}

}
