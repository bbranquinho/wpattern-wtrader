package org.wtrader.loader.crawler.interfaces;


import java.io.IOException;
import java.util.List;

public interface ICrawlerAllCompanies {

	public List<String[]> retrieveAllCompanies() throws IOException;

}
