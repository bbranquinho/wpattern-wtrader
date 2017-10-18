package org.wtrader.cep.utils;

import java.util.Arrays;

public final class RangeNormalization {

	public static final double LOW_DEFAULT = 0.0;

	public static final double HIGH_DEFAULT = 1.0;

	private RangeNormalization() { }

	public static double[][] normalizeColumn(double[][] data) {
		return normalizeColumn(LOW_DEFAULT, HIGH_DEFAULT, data);
	}

	public static double[][] normalizeColumn(double low, double high, double[][] data) {
		if ((data == null) || (data.length <= 0)) {
			return data;
		}

		double[] maxValues = new double[data[0].length];
		double[] minValues = new double[data[0].length];

		Arrays.fill(maxValues, 0, maxValues.length - 1, Double.MIN_VALUE);
		Arrays.fill(minValues, 0, maxValues.length - 1, Double.MAX_VALUE);

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				if (data[i][j] > maxValues[j]) {
					maxValues[j] = data[i][j];
				}

				if (data[i][j] < minValues[j]) {
					minValues[j] = data[i][j];
				}
			}
		}

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				data[i][j] = ((data[i][j] - minValues[j]) / (maxValues[j] - minValues[j])) * (high - low) + low;
			}
		}

		return data;
	}

	public static double[] normalize(double[] data) {
		return normalize(LOW_DEFAULT, HIGH_DEFAULT, data);
	}

	public static double[] normalize(double low, double high, double[] data) {
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		for (int i = 0; i < data.length; i++) {
			if (data[i] < min) {
				min = data[i];
			}

			if (data[i] > max) {
				max = data[i];
			}
		}

		for (int i = 0; i < data.length; i++) {
			data[i] = ((data[i] - min) / (max - min)) * (high - low) + low;
		}

		return data;
	}

}
