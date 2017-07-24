package com.ikuuy.locale.provider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.spi.DateFormatProvider;
import java.util.Locale;

import com.ikuuy.locale.util.ExtLocalesUtil;

/**
 * An implementation class for {@link DateFormatProvider} which provides a
 * localized {@link DateFormat}.
 *
 * @author Yuki Yamada
 *
 */
public class DateFormatProviderImpl extends DateFormatProvider {

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
	public DateFormat getDateInstance(final int style, final Locale locale) throws IllegalArgumentException,
			NullPointerException {
		return new SimpleDateFormat(getDateFormatString(style, locale), locale);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DateFormat getDateTimeInstance(final int dateStyle, final int timeStyle, final Locale locale)
			throws IllegalArgumentException, NullPointerException {
		return new SimpleDateFormat(getDateFormatString(dateStyle, locale) + " "
				+ getTimeFormatString(timeStyle, locale), locale);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DateFormat getTimeInstance(final int style, final Locale locale) throws IllegalArgumentException,
			NullPointerException {
		DateFormat dateFormat = null;

		String format = getTimeFormatString(style, locale);
		if (format != null) {
			dateFormat = new SimpleDateFormat(format, locale);
		}

		return dateFormat;
	}

	/**
	 * Returns a date pattern with the given formatting style for the specified
	 * locale.
	 *
	 * @param style the given date formatting style.
	 * @param locale the desired locale.
	 * @return a date pattern.
	 * @throws IllegalArgumentException if <code>style</code> is invalid, or if
	 *     <code>locale</code> isn't available.
	 * @throws NullPointerException if <code>locale</code> is <code>null</code>.
	 */
	protected String getDateFormatString(final int style, final Locale locale) throws IllegalArgumentException,
			NullPointerException {
		if (locale == null) {
			throw new NullPointerException("locale:null");
		} else if (!ExtLocalesUtil.isAvailableLocale(locale)) {
			throw new IllegalArgumentException("locale:" + locale.toString());
		}

		String key;
		switch (style) {
		case DateFormat.SHORT:
			key = "DateFormat.SHORT";
			break;
		case DateFormat.MEDIUM:
			key = "DateFormat.MEDIUM";
			break;
		case DateFormat.LONG:
			key = "DateFormat.LONG";
			break;
		case DateFormat.FULL:
			key = "DateFormat.FULL";
			break;
		default:
			throw new IllegalArgumentException("style:" + style);
		}

		return ExtLocalesUtil.getString(key, locale);
	}

	/**
	 * Returns a time pattern with the given formatting style for the specified
	 * locale.
	 *
	 * @param style the given time formatting style.
	 * @param locale the desired locale.
	 * @return a time pattern.
	 * @throws IllegalArgumentException if <code>style</code> is invalid, or if
	 *     <code>locale</code> isn't available.
	 * @throws NullPointerException if <code>locale</code> is <code>null</code>.
	 */
	protected String getTimeFormatString(final int style, final Locale locale) throws IllegalArgumentException,
			NullPointerException {
		if (locale == null) {
			throw new NullPointerException("locale:null");
		} else if (!ExtLocalesUtil.isAvailableLocale(locale)) {
			throw new IllegalArgumentException("locale:" + locale.toString());
		}

		String key;
		switch (style) {
		case DateFormat.SHORT:
			key = "TimeFormat.SHORT";
			break;
		case DateFormat.MEDIUM:
			key = "TimeFormat.MEDIUM";
			break;
		case DateFormat.LONG:
			key = "TimeFormat.LONG";
			break;
		case DateFormat.FULL:
			key = "TimeFormat.FULL";
			break;
		default:
			throw new IllegalArgumentException("style:" + style);
		}

		return ExtLocalesUtil.getString(key, locale);
	}
}
