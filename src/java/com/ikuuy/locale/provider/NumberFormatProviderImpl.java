package com.ikuuy.locale.provider;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.spi.NumberFormatProvider;
import java.util.Locale;

import com.ikuuy.locale.util.ExtLocalesUtil;

/**
 * An implementation class for {@link NumberFormatProvider} which provides a
 * localized {@link NumberFormat}.
 *
 * @author Yuki Yamada
 *
 */
public class NumberFormatProviderImpl extends NumberFormatProvider {

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
	public NumberFormat getCurrencyInstance(final Locale locale) throws IllegalArgumentException, NullPointerException {
		return getInstance("CurrencyPattern", locale);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NumberFormat getIntegerInstance(final Locale locale) throws IllegalArgumentException, NullPointerException {
		NumberFormat format = getInstance("IntegerPattern", locale);
		format.setParseIntegerOnly(true);
		return format;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NumberFormat getNumberInstance(final Locale locale) throws IllegalArgumentException, NullPointerException {
		return getInstance("NumberPattern", locale);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NumberFormat getPercentInstance(final Locale locale) throws IllegalArgumentException, NullPointerException {
		return getInstance("PercentPattern", locale);
	}

	/**
	 * Returns a new NumberFormat instance for the specified key and locale.
	 *
	 * @param key the property key.
	 * @param locale the desired locale.
	 * @return a number formatter.
	 * @throws IllegalArgumentException if <code>locale</code> isn't available.
	 * @throws NullPointerException if <code>locale</code> is <code>null</code>.
	 */
	protected NumberFormat getInstance(final String key, final Locale locale) throws IllegalArgumentException,
			NullPointerException {
		if (locale == null) {
			throw new NullPointerException("locale:null");
		} else if (!ExtLocalesUtil.isAvailableLocale(locale)) {
			throw new IllegalArgumentException("locale:" + locale.toString());
		}

		DecimalFormat format = new DecimalFormat();
		format.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(locale));

		String pattern = ExtLocalesUtil.getString(key, locale);
		format.applyPattern(pattern);

		return format;
	}
}
