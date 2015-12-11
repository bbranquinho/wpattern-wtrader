package org.wtrader.loader.crawler;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.wtrader.loader.crawler.interfaces.ICrawlerCodigoCvm;
import org.wtrader.loader.utils.properties.ApplicationProperties;

public class CrawlerCodigoCvm implements ICrawlerCodigoCvm {

	private static final Logger LOGGER = Logger.getLogger(CrawlerCodigoCvm.class);

	private static final String[][] REPLACES = new String[][] {
		{ "ambev", "ambev s/a" },
		{ "ccr s/a", "ccr sa" },
		{ "bmf bovespa", "bmfbovespa" }
	};

	private CrawlerAllCompanies crawlerAllCompanies;

	public CrawlerCodigoCvm() {
		this.crawlerAllCompanies = new CrawlerAllCompanies();
	}

	public static void main(String[] args) {
		ApplicationProperties applicationProperties = new ApplicationProperties();

		CrawlerBestCompanies crawlerCompanies = new CrawlerBestCompanies();
		CrawlerCodigoCvm codigoCvm = new CrawlerCodigoCvm();

		crawlerCompanies.setApplicationProperties(applicationProperties);
		codigoCvm.crawlerAllCompanies.setApplicationProperties(applicationProperties);

		try {
			List<String[]> codesAndCompany = codigoCvm.retrieveCodigoCvm(crawlerCompanies.retriveCompanies());

			if (codesAndCompany != null) {
				for (String[] codeComp : codesAndCompany) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(String.format("%s => %s", codeComp[0], codeComp[1]));
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	@Override
	public List<String[]> retrieveCodigoCvm(List<String> companies) throws IOException {
		List<String[]> codes = new ArrayList<String[]>();
		List<String[]> allCodesAndCompany = this.crawlerAllCompanies.retrieveAllCompanies();

		boolean founded;

		for (String company : companies) {
			company = company.toLowerCase().trim();

			for (int i = 0; i < REPLACES.length; i++) {
				if (REPLACES[i][0].equals(company)) {
					company = REPLACES[i][1];
				}
			}

			founded = false;

			for (String[] codeCompany : allCodesAndCompany) {
				if (codeCompany[1].equals(company.trim().toLowerCase())) {
					if (founded) {
						LOGGER.error(String.format("Founded more than one company [%s - %s].", codeCompany[1].trim().toLowerCase(),
								company.trim().toLowerCase()));
					}

					founded = true;
					codes.add(codeCompany);
				}
			}

			if (!founded) {
				LOGGER.error(String.format("Not founded the \"codigoCvm\" to Company [%s].", company));
			}
		}

		return codes;
	}

}
