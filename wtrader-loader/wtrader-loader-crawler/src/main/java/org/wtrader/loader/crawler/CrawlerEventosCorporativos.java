package org.wtrader.loader.crawler;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.loader.crawler.interfaces.ICrawlerEventosCorporativos;
import org.wtrader.loader.utils.CrawlerUtils;
import org.wtrader.loader.utils.beans.BenefitStockBean;
import org.wtrader.loader.utils.beans.CompanyBean;
import org.wtrader.loader.utils.properties.ApplicationProperties;
import org.wtrader.loader.utils.properties.IApplicationProperties;

@Named
public class CrawlerEventosCorporativos implements ICrawlerEventosCorporativos {

	private static final Logger LOGGER = Logger.getLogger(CrawlerEventosCorporativos.class);

	private static final String LINK_EVENTS = "http://www.bmfbovespa.com.br/cias-listadas/empresas-listadas/ResumoEventosCorporativos.aspx?codigoCvm=%s&tab=3&idioma=pt-br";

	private static final String LINK_COMPANY_PRE = "http://www.bmfbovespa.com.br/cias-listadas/empresas-listadas/ResumoEmpresaPrincipal.aspx?codigoCvm=%s&idioma=pt-br";

	private static final String LINK_COMPANY_POS = "http://www.bmfbovespa.com.br/pt-br/mercados/acoes/empresas/ExecutaAcaoConsultaInfoEmp.asp?CodCVM=%s&ViewDoc=%s";

	private static final String TABLE_PATTERN = "<table\\b[^>]*>(.*?)</table>";

	private static final String TBODY_PATTERN = "<tbody\\b[^>]*>(.*?)</tbody>";

	private static final String LINE_PATTERN = "<tr\\b[^>]*>(.*?)</tr>";

	private static final String COLUMN_PATTERN = "<td\\b[^>]*>(.*?)</td>";

	private static final String VIEW_DOC_PATTERN = "ViewDoc=(.*?)#";

	private static final String NOME_PREGAO_PATTERN = "<td\\b[^>]*>(.*Nome de Preg.*?)</tr>";

	private static final String TRADE_NAME_PATTERN = "\"dado\">(.*?)<";

	private static final String STOCKS_PATTERN = "verificacotacao\\('.*?'";

	private static final String UNITARY_QUOTATION_SINCE_PATTERN = "lblCotacaoUnitariaValor.*?<";

	private static final String VALUE_PATTERN = ">(.*?)<";

	private static final String FACTOR_VALUE_PATTERN = ">([^<][0-9\\.\\,\\\\\\/]+.*?)<";

	private static final String INTEGER_PATTERN = "[0-9]+";

	private static final String DATE_FORMAT = "dd/MM/yyyy";

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT);

	@Inject
	private IApplicationProperties applicationProperties;

	public static void main(String[] args) {
		ApplicationProperties applicationProperties = new ApplicationProperties();

		List<CompanyBean> allCompanies = new ArrayList<CompanyBean>();
		CrawlerBestCompanies crawlerCompanies = new CrawlerBestCompanies();
		CrawlerEventosCorporativos eventosCorporativos = new CrawlerEventosCorporativos();
		CrawlerAllCompanies crawlerAllCompanies =new CrawlerAllCompanies();
		//Scanner scanner = new Scanner(System.in);
		CompanyBean companyBean;

		crawlerCompanies.setApplicationProperties(applicationProperties);
		crawlerAllCompanies.setApplicationProperties(applicationProperties);
		eventosCorporativos.applicationProperties = applicationProperties;

		try {
			List<String[]> codesAndCompany;

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("All companies [0] or best companies [1].");
			}

			//if (scanner.nextInt() == 0) {
			//codesAndCompany = new CrawlerCodigoCvm().retrieveCodigoCvm(crawlerCompanies.retriveCompanies());
			//} else {
			codesAndCompany = crawlerAllCompanies.retrieveAllCompanies();
			//}

			for (String[] cvmAndCompany : codesAndCompany) {
				companyBean = eventosCorporativos.retrieveCompany(cvmAndCompany[0], cvmAndCompany[1]);

				if (companyBean != null) {
					allCompanies.add(companyBean);
				} else {
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info(String.format("Company not founded to cvm [%s] and company [%s].", cvmAndCompany[0], cvmAndCompany[1]));
					}
				}
			}

			for (CompanyBean company : allCompanies) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info(company);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Override
	public CompanyBean retrieveCompany(String codigoCvm, String companyName) throws ParseException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("Retrieving Company and Events data of \"codigoCvm\" [%s].", codigoCvm));
		}

		CompanyBean companyBean = new CompanyBean();

		String response = CrawlerUtils.getRequestContent(String.format(LINK_COMPANY_PRE, codigoCvm), this.applicationProperties.getProxy()).toString();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Response of \"pre company\" [%s].", response));
		}

		Integer viewDoc = this.getIntField(response, VIEW_DOC_PATTERN);

		if (viewDoc == null) {
			LOGGER.warn(String.format("Field(s) [viewDoc = %s] not founded.", viewDoc));
			return null;
		}

		response = CrawlerUtils.getRequestContent(String.format(LINK_COMPANY_POS, codigoCvm, viewDoc), this.applicationProperties.getProxy()).toString();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Response of \"pos company\" [%s].", response));
		}

		Pattern pattern = Pattern.compile(NOME_PREGAO_PATTERN);
		Matcher matcher = pattern.matcher(response);

		if (!matcher.find()) {
			LOGGER.error(String.format("\"Nome de Pregão\" using the regex [%s] not founded in [%s].", NOME_PREGAO_PATTERN, response));
			return null;
		}

		// Trade name
		pattern = Pattern.compile(TRADE_NAME_PATTERN, Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(matcher.group());

		if (!matcher.find()) {
			LOGGER.error(String.format("\"Trade Name\" using the regex [%s] not founded in [%s].", TRADE_NAME_PATTERN, response));
			return null;
		}

		companyBean.setTradeName(matcher.group().toLowerCase().replaceAll("<", "").replaceAll("\"dado\">", "").trim());

		// Codigo Cvm
		companyBean.setCodeCvm(codigoCvm);

		// Stocks
		pattern = Pattern.compile(STOCKS_PATTERN, Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(response);

		companyBean.setStocks(new ArrayList<String>());

		while (matcher.find()) {
			String stockName = matcher.group().toLowerCase().replaceAll("\'", "").replaceAll("verificacotacao\\(", "").trim();

			if (!stockName.trim().isEmpty() && !companyBean.getStocks().contains(stockName)) {
				companyBean.getStocks().add(stockName);
			}
		}

		// Eventos corporativos
		response = CrawlerUtils.getRequestContent(String.format(LINK_EVENTS, codigoCvm), this.applicationProperties.getProxy()).toString();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Response of \"eventos corporativos\" [%s].", response));
		}

		// Unitary quotation since.
		pattern = Pattern.compile(UNITARY_QUOTATION_SINCE_PATTERN, Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(response);

		if (!matcher.find()) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(String.format("Not exist quotation events to codigo svm [%s] and company name [%s].", codigoCvm, companyName));
			}

			return companyBean;
		}

		pattern = Pattern.compile(VALUE_PATTERN);
		matcher = pattern.matcher(matcher.group());

		if (!matcher.find()) {
			LOGGER.error(String.format("Not founded a \"unitary quotation since\" in [%s].", response));
			return null;
		}

		Date unitaryQuotationSince = this.parseDate(matcher.group().replaceAll(">", "").replaceAll("<", "").trim());

		companyBean.setUnitaryQuotationSince(unitaryQuotationSince);

		// Benefits in stock
		companyBean.setBenefitsInStock(this.retrieveBenefitsInStock(response));

		return companyBean;
	}

	private List<BenefitStockBean> retrieveBenefitsInStock(String content) throws ParseException {
		List<BenefitStockBean> benefits = new ArrayList<BenefitStockBean>();

		Pattern pattern = Pattern.compile(TABLE_PATTERN);
		Matcher matcher = pattern.matcher(content);
		BenefitStockBean benefit;
		String table;

		// Benefit in stocks
		while (matcher.find()) {
			table = matcher.group().toLowerCase();

			if (table.contains("proventos em a")) {
				Pattern benefitPattern = Pattern.compile(TBODY_PATTERN);
				Matcher benefitMatcher = benefitPattern.matcher(table);

				if (!benefitMatcher.find()) {
					LOGGER.error(String.format("Not founded the tag \"tbody\" in benefit in stocks [%s].", table));
					return benefits;
				}

				table = benefitMatcher.group();

				benefitPattern = Pattern.compile(LINE_PATTERN, Pattern.CASE_INSENSITIVE);
				benefitMatcher = benefitPattern.matcher(table);

				while (benefitMatcher.find()) {
					benefit = this.processLineBenefit(benefitMatcher.group());

					if (benefit != null) {
						benefits.add(benefit);
					}
				}
			}
		}

		return benefits;
	}

	private BenefitStockBean processLineBenefit(String column) throws ParseException {
		BenefitStockBean benefit = new BenefitStockBean();
		Pattern pattern = Pattern.compile(COLUMN_PATTERN, Pattern.CASE_INSENSITIVE);
		Pattern valuePattern = Pattern.compile(VALUE_PATTERN, Pattern.CASE_INSENSITIVE);
		Pattern factorValuePattern = Pattern.compile(FACTOR_VALUE_PATTERN, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(column);
		Matcher valueMatcher;
		int matcherIndex = 0;
		String value;

		while (matcher.find()) {
			valueMatcher = (matcherIndex == 3) ? factorValuePattern.matcher(matcher.group()) : valuePattern.matcher(matcher.group());

			if (valueMatcher.find()) {
				value = valueMatcher.group().replaceAll("<", "").replaceAll(">", "").trim();

				switch (matcherIndex) {
				case 0:
					// Name
					benefit.setName(value);
					break;

				case 1:
					// Deliberate
					benefit.setDeliberate(this.parseDate(value));
					break;

				case 2:
					// Businesses with up
					benefit.setBusinessesWithUp(this.parseDate(value));
					break;

				case 3:
					// Factor
					try {
						if (value.contains("/") || value.contains("\\")) {
							String[] values = value.replaceAll("\\.", "").replaceAll(",", ".").split("/|\\\\");
							double factor = Double.parseDouble(values[0]);

							for (int i = 1; i < values.length; i++) {
								factor /= Double.parseDouble(values[i]);
							}

							benefit.setFactor(factor);
						} else if (value.contains(",") && value.contains(".")) {
							benefit.setFactor(Double.parseDouble(value.replaceAll("\\.", "").replaceAll(",", ".")));
						} else {
							benefit.setFactor(Double.parseDouble(value.replaceAll(",", ".")));
						}
					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
						LOGGER.error(String.format("Problem to parse the factor [%s] to column [%s].", value, column));
					}

					if (LOGGER.isInfoEnabled()) {
						LOGGER.info(String.format("Factor [%s] to benefit [%s] was converted to [%s].", value, benefit.getName(),
								benefit.getFactor()));
					}
					break;

				case 4:
					// Shares in credit
					benefit.setSharesInCredit(this.parseDate(value));
					break;
				}

				matcherIndex++;
			}
		}

		return benefit;
	}

	private Date parseDate(String value) {
		try {
			return SIMPLE_DATE_FORMAT.parse(value);
		} catch (ParseException e) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(e.getMessage());
			}
			return null;
		}
	}

	private Integer getIntField(String content, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);

		if (!matcher.find()) {
			LOGGER.error(String.format("Field [%s] not founded to content [%s].", regex, content));
			return null;
		}

		content = matcher.group();

		Pattern intPattern = Pattern.compile(INTEGER_PATTERN);
		Matcher intMatcher = intPattern.matcher(content);

		if (!intMatcher.find()) {
			LOGGER.error(String.format("Integer field [%s] not founded to content [%s].", INTEGER_PATTERN, content));
			return null;
		}

		return Integer.parseInt(intMatcher.group());
	}

}
