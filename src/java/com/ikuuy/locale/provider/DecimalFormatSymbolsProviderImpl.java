package com.ikuuy.locale.provider;

import java.text.DecimalFormatSymbols;
import java.text.spi.DecimalFormatSymbolsProvider;
import java.util.Locale;

import com.ikuuy.locale.util.ExtLocalesUtil;

/**
 * An implementation class for {@link DecimalFormatSymbolsProvider} which provides
 * a localized {@link DecimalFormatSymbols}.
 *
 * @author Yuki Yamada
 *
 */
public class DecimalFormatSymbolsProviderImpl extends DecimalFormatSymbolsProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Locale[] getAvailableLocales() {
		return ExtLocalesUtil.getAvailableLocales();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DecimalFormatSymbols getInstance(final Locale locale) throws IllegalArgumentException, NullPointerException {
		if (locale == null) {
			throw new NullPointerException("locale:null");
		} else if (!ExtLocalesUtil.isAvailableLocale(locale)) {
			throw new IllegalArgumentException("locale:" + locale.toString());
		}

		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
		symbols.setDecimalSeparator(
				ExtLocalesUtil.getChar("DecimalSeparator", locale));
		symbols.setDigit(
				ExtLocalesUtil.getChar("Digit", locale));
		symbols.setExponentSeparator(
				ExtLocalesUtil.getString("ExponentSeparator", locale));
		symbols.setGroupingSeparator(
				ExtLocalesUtil.getChar("GroupingSeparator", locale));
		symbols.setInfinity(
				ExtLocalesUtil.getString("Infinity", locale));
		symbols.setInternationalCurrencySymbol(
				ExtLocalesUtil.getString("InternationalCurrencySymbol", locale));
		symbols.setCurrencySymbol(
				ExtLocalesUtil.getString("CurrencySymbol", locale));
		symbols.setMinusSign(
				ExtLocalesUtil.getChar("MinusSign", locale));
		symbols.setMonetaryDecimalSeparator(
				ExtLocalesUtil.getChar("MonetaryDecimalSeparator", locale));
		symbols.setNaN(
				ExtLocalesUtil.getString("NaN", locale));
		symbols.setPatternSeparator(
				ExtLocalesUtil.getChar("PatternSeparator", locale));
		symbols.setPercent(
				ExtLocalesUtil.getChar("Percent", locale));
		symbols.setPerMill(
				ExtLocalesUtil.getChar("PerMill", locale));
		symbols.setZeroDigit(
				ExtLocalesUtil.getChar("ZeroDigit", locale));

		return symbols;
	}
}
