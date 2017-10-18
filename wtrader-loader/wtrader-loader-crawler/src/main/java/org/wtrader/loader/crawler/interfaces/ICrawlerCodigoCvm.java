package org.wtrader.loader.crawler.interfaces;


import java.io.IOException;
import java.util.List;

public interface ICrawlerCodigoCvm {

	public List<String[]> retrieveCodigoCvm(List<String> companies) throws IOException;

}
