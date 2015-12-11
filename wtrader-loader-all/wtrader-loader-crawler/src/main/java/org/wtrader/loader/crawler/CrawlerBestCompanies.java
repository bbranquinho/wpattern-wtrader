package org.wtrader.loader.crawler;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.wtrader.loader.crawler.interfaces.ICrawlerBestCompanies;
import org.wtrader.loader.utils.CrawlerUtils;
import org.wtrader.loader.utils.properties.ApplicationProperties;
import org.wtrader.loader.utils.properties.IApplicationProperties;

@Named
public class CrawlerBestCompanies implements ICrawlerBestCompanies {

	private static final Logger LOGGER = Logger.getLogger(CrawlerBestCompanies.class);

	private static final String LINK = "http://www.bmfbovespa.com.br/capitalizacao-bursatil/ResumoBursatilMensal.aspx?idioma=pt-br";

	private static final String TABLE_PATTERN = "<tbody\\b[^>]*>(.*?)</tbody>";

	private static final String LINE_PATTERN = "<tr\\b[^>]*>(.*?)</tr>";

	private static final String COLUMN_PATTERN = "<td\\b[^>]*>(.*?)</td>";

	private static final String REMOVE_START_COLUMN_PATTERN = "<td\\b[^>]*>";

	private static final String REMOVE_END_COLUMN_PATTERN = "</td>";

	@Inject
	private IApplicationProperties applicationProperties;

	public static void main(String[] args) {
		CrawlerBestCompanies crawler = new CrawlerBestCompanies();

		crawler.applicationProperties = new ApplicationProperties();

		try {
			List<String> companies = crawler.retriveCompanies();

			String strMessage = "";

			for (String company : companies) {
				strMessage += company + ", ";
			}

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(strMessage);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	@Override
	public List<String> retriveCompanies() throws Exception {
		StringBuffer response = CrawlerUtils.getRequestContent(LINK, this.applicationProperties.getProxy());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(response.toString());
		}

		// 1. Table.
		List<String> tables = this.regexTable(response.toString());

		if (tables.size() > 1) {
			LOGGER.error("Was founded more than 1 table.");
			System.exit(-1);
		}

		if (tables.size() < 1) {
			LOGGER.error("Not founded the table.");
			System.exit(-1);
		}

		// 2. Lines.
		List<String> lines = this.regexLines(tables.get(0));

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(lines);
		}

		// 3. Companies.
		List<String> companies = this.regexCompanies(lines);

		if (companies != null) {
			String allCompanies = "";

			for (String company : companies) {
				allCompanies += company + " ";
			}

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(allCompanies);
			}
		}

		return companies;
	}

	public void setApplicationProperties(IApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	private List<String> regexCompanies(List<String> lines) {
		List<String> companies = new ArrayList<String>();
		Pattern pattern = Pattern.compile(COLUMN_PATTERN);

		for (String line : lines) {
			Matcher matcher = pattern.matcher(line);
			List<String> columns = new ArrayList<String>();
			boolean ignore = false;

			while (matcher.find()) {
				String column = matcher.group();
				columns.add(column);

				if (column.contains("font-weight")) {
					ignore = true;
					break;
				}
			}

			if (!ignore) {
				companies.add(columns.get(0)
						.replaceAll(REMOVE_START_COLUMN_PATTERN, "")
						.replaceAll(REMOVE_END_COLUMN_PATTERN, "")
						.replaceFirst("\\*", "")
						.replaceAll("\\(N1\\)", "")
						.replaceAll("\\(N2\\)", "")
						.replaceAll("\\(NM\\)", "")
						.toLowerCase()
						.trim());
			}
		}

		return companies;
	}

	private List<String> regexTable(String content) {
		List<String> tables = new ArrayList<String>();
		Pattern pattern = Pattern.compile(TABLE_PATTERN);
		Matcher matcher = pattern.matcher(content);

		while (matcher.find()) {
			tables.add(matcher.group());
		}

		return tables;
	}

	private List<String> regexLines(String content) {
		List<String> lines = new ArrayList<String>();
		Pattern pattern = Pattern.compile(LINE_PATTERN);
		Matcher matcher = pattern.matcher(content);

		while (matcher.find()) {
			lines.add(matcher.group());
		}

		return lines ;
	}

}
