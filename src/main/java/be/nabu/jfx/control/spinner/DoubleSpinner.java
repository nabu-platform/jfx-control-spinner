/*
* Copyright (C) 2015 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

package be.nabu.jfx.control.spinner;

import java.text.DecimalFormat;
import java.text.ParseException;

import javafx.util.StringConverter;

public class DoubleSpinner extends Spinner<Double> {

	private double increment = 1;
	private Double max, min;
	
	private StringConverter<Double> converter;
	private DoubleValueChanger valueChanger;
	private boolean wrapValues;
	
	public DoubleSpinner(Alignment alignment) {
		super(alignment);
	}
	
	@Override
	public DoubleValueChanger getValueChanger() {
		if (valueChanger == null)
			valueChanger = new DoubleValueChanger();
		return valueChanger;
	}
	@Override
	public StringConverter<Double> getConverter() {
		if (converter == null)
			setPrecision(0);
		return converter;
	}
	
	public void setFormat(String format) {
		converter = new DecimalConverter(new DecimalFormat(format));
	}
	public void setFormat(DecimalFormat formatter) {
		converter = new DecimalConverter(formatter);
	}
	public void setPrecision(int precision) {
		converter = new DecimalConverter(buildFormatter(precision));
	}
	
	public double getIncrement() {
		return increment;
	}

	public void setIncrement(double increment) {
		this.increment = increment;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}
	
	public boolean isWrapValues() {
		return wrapValues;
	}

	public void setWrapValues(boolean wrapValues) {
		this.wrapValues = wrapValues;
	}

	private class DecimalConverter extends StringConverter<Double> {
		
		private DecimalFormat formatter;
		
		public DecimalConverter(DecimalFormat formatter) {
			this.formatter = formatter;
		}
		
		@Override
		public Double fromString(String arg0) {
			if (arg0 == null)
				return null;
			else if (arg0.isEmpty())
				return null;
			arg0 = arg0.replace('.', formatter.getDecimalFormatSymbols().getDecimalSeparator());
			arg0 = arg0.replace(',', formatter.getDecimalFormatSymbols().getDecimalSeparator());
			try {
				double value = formatter.parse(arg0).doubleValue();
				if ((max != null && value > max) || (min != null && value < min)) {
					throw new IllegalArgumentException("Out of bounds");
				}
				else {
					return value;
				}
			}
			catch (ParseException e) {
				throw new IllegalArgumentException(e);
			}
		}

		@Override
		public String toString(Double arg0) {
			return arg0 == null ? null : formatter.format(arg0);
		}
	}
	private DecimalFormat buildFormatter(int precision) {
		String format = "0";
		if (precision > 0) {
			format += ".";
			for (int i = 0; i < precision; i++)
				format += "0";
		}
		return new DecimalFormat(format);
	}
	
	private class DoubleValueChanger implements ValueChanger<Double> {
		
		@Override
		public Double increment(Double currentValue) {
			double newValue = currentValue == null ? increment : currentValue + increment;
			if (max != null && newValue > max) {
				if (wrapValues) {
					newValue = min == null ? (double) Long.MIN_VALUE : min;
				}
				return max == null ? currentValue : max;
			}
			return newValue;
		}

		@Override
		public Double decrement(Double currentValue) {
			double newValue = currentValue == null ? 0 - increment : currentValue - increment;
			if (min != null && newValue < min) {
				if (wrapValues) {
					newValue = max == null ? (double) Long.MAX_VALUE : max;
				}
				return min == null ? currentValue : min;
			}
			return newValue;
		}
	}
}
