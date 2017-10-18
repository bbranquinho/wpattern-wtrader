package org.wtrader.cep.data;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.wtrader.cep.utils.data.entities.StockRecordEntity;
import org.wtrader.cep.utils.data.enums.SortEnum;
import org.wtrader.cep.utils.enums.DataType;

class WPatternPhDDatabaseWrapper {

	public static final double[] parseRecord(DataType dataType, List<StockRecordEntity> records) {
		double[] data = new double[records.size()];
		double value;

		for (int i = 0; i < data.length; i++) {
			switch (dataType) {
			case AVERAGE_PRICE:
				value = records.get(i).getAverageNegotiationPrice().doubleValue();
				break;

			case CLOSING_NEGOTIATION_PRICE:
				value = records.get(i).getClosingNegotiationPrice().doubleValue();
				break;

			case HIGHEST_NEGOTIATION_PRICE:
				value = records.get(i).getHighestNegotiationPrice().doubleValue();
				break;

			case LOWEST_NEGOTIATION_PRICE:
				value = records.get(i).getLowestNegotiationPrice().doubleValue();
				break;

			case START_PRICE:
				value = records.get(i).getStartPrice().doubleValue();
				break;

			case VOLUME:
				value = records.get(i).getTotalVolume().doubleValue();
				break;

			default:
				throw new RuntimeException(String.format("DataType not defined [%s].", dataType));
			}

			data[i] = value;
		}

		return data;
	}

	public static final Sort parseSort(SortEnum sort, String fields) {
		return new Sort((sort == SortEnum.ASCENDING) ? Direction.ASC : Direction.DESC, fields);
	}

}
