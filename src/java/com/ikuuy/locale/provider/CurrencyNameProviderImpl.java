package com.ikuuy.locale.provider;

import java.util.Locale;
import java.util.spi.CurrencyNameProvider;

import com.ikuuy.locale.util.ExtLocalesUtil;

/**
 * An implementation class for {@link CurrencyNameProvider} which provides
 * localized names for {@link Currency}.
 *
 * @author Yuki Yamada
 *
 */
public class CurrencyNameProviderImpl extends CurrencyNameProvider {

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
	public String getSymbol(final String currencyCode, final Locale locale) throws IllegalArgumentException,
			NullPointerException {
		if (currencyCode == null) {
			throw new NullPointerException("currencyCode:null");
		} else if (!currencyCode.matches("^[A-Z]{3}$")) {
			// The currency code string should be in the form of three upper-case letters.
			throw new IllegalArgumentException("currencyCode:" + currencyCode);
		} else if (locale == null) {
			throw new NullPointerException("locale:null");
		} else if (!ExtLocalesUtil.isAvailableLocale(locale)) {
			throw new IllegalArgumentException("locale:" + locale.toString());
		}

		String symbol = null;
		String key = "Currency." + currencyCode;

		if (ExtLocalesUtil.containsKey(key, locale)) {
			symbol = ExtLocalesUtil.getString(key, locale);
		} else {
			String intlSymbol = ExtLocalesUtil.getString("InternationalCurrencySymbol", locale);
			if (currencyCode.equals(intlSymbol)) {
				symbol = ExtLocalesUtil.getString("CurrencySymbol", locale);
			}
		}

		return symbol;
	}
}
