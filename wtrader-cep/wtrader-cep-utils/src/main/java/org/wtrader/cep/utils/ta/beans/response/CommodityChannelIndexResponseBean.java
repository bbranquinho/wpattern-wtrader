package org.wtrader.cep.utils.ta.beans.response;

import org.wtrader.cep.utils.INormalization;
import org.wtrader.cep.utils.RangeNormalization;
import org.wtrader.cep.utils.beans.BaseBean;

public class CommodityChannelIndexResponseBean extends BaseBean implements INormalization {

	private static final long serialVersionUID = 201401160702L;

	private double[] commodityChannelIndex;

	public CommodityChannelIndexResponseBean() {
	}

	public CommodityChannelIndexResponseBean(double[] commodityChannelIndex) {
		super();
		this.commodityChannelIndex = commodityChannelIndex;
	}

	public double[] getCommodityChannelIndex() {
		return this.commodityChannelIndex;
	}

	public void setCommodityChannelIndex(double[] commodityChannelIndex) {
		this.commodityChannelIndex = commodityChannelIndex;
	}

	@Override
	public void normalize() {
		this.commodityChannelIndex = RangeNormalization.normalize(this.commodityChannelIndex);
	}

	@Override
	public void normalize(double low, double high) {
		this.commodityChannelIndex = RangeNormalization.normalize(low, high, this.commodityChannelIndex);
	}

}
