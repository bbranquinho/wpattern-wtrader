package org.wtrader.loader.crawler.interfaces;


import java.text.ParseException;

import org.wtrader.loader.utils.beans.CompanyBean;


public interface ICrawlerEventosCorporativos {

	public CompanyBean retrieveCompany(String codigoCvm, String companyName) throws ParseException;

}
