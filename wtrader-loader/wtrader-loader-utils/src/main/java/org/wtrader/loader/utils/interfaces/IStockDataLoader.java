package org.wtrader.loader.utils.interfaces;

import java.io.IOException;
import java.util.List;

public interface IStockDataLoader {

	public void loadFile(String fullnamePath, List<String> acceptedStocks) throws IOException;

}
