package org.wtrader.cep.utils.interfaces;

import org.wtrader.cep.utils.annotations.TechnicalAnalysis;
import org.wtrader.cep.utils.ta.beans.request.CommodityChannelIndexRequestBean;
import org.wtrader.cep.utils.ta.beans.response.CommodityChannelIndexResponseBean;

@TechnicalAnalysis(name = "Commodity Channel Index")
public interface ICommodityChannelIndex {

	public CommodityChannelIndexResponseBean calculate(CommodityChannelIndexRequestBean requestBean);

}
