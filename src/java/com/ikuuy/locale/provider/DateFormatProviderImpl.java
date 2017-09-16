package com.ikuuy.locale.provider;

import java.text.DateFormat;
import java.text.MessageFormat;
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
		DateFormat dateFormat = null;

		String format = getDateTimeFormatString("DateFormat", style, locale);
		if (format != null) {
			dateFormat = new SimpleDateFormat(format, locale);
		}

		return dateFormat;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DateFormat getDateTimeInstance(final int dateStyle, final int timeStyle, final Locale locale)
			throws IllegalArgumentException, NullPointerException {
		DateFormat dateFormat = null;

		String format = getDateTimeFormatString("DateTimeFormat", dateStyle, locale);
		if (format != null) {
			String pattern = MessageFormat.format(format, getDateTimeFormatString("DateFormat", dateStyle, locale),
					getDateTimeFormatString("TimeFormat", timeStyle, locale));
			dateFormat = new SimpleDateFormat(pattern, locale);
		}

		return dateFormat;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DateFormat getTimeInstance(final int style, final Locale locale) throws IllegalArgumentException,
			NullPointerException {
		DateFormat dateFormat = null;

		String format = getDateTimeFormatString("TimeFormat", style, locale);
		if (format != null) {
			dateFormat = new SimpleDateFormat(format, locale);
		}

		return dateFormat;
	}

	/**
	 * Returns a date/time pattern with the given formatting style for the specified
	 * locale.
	 *
	 * @param prefix the prefix for the property key.
	 * @param style the given date/time formatting style.
	 * @param locale the desired locale.
	 * @return a date/time pattern.
	 * @throws IllegalArgumentException if <code>style</code> is invalid, or if
	 *     <code>locale</code> isn't available.
	 * @throws NullPointerException if <code>locale</code> is <code>null</code>.
	 */
	protected String getDateTimeFormatString(final String prefix, final int style, final Locale locale)
			throws IllegalArgumentException, NullPointerException {
		if (locale == null) {
			throw new NullPointerException("locale:null");
		} else if (!ExtLocalesUtil.isAvailableLocale(locale, getAvailableLocales())) {
			throw new IllegalArgumentException("locale:" + locale.toString());
		}

		String key;
		switch (style) {
		case DateFormat.SHORT:
			key = prefix + ".SHORT";
			break;
		case DateFormat.MEDIUM:
			key = prefix + ".MEDIUM";
			break;
		case DateFormat.LONG:
			key = prefix + ".LONG";
			break;
		case DateFormat.FULL:
			key = prefix + ".FULL";
			break;
		default:
			throw new IllegalArgumentException("style:" + style);
		}

		return ExtLocalesUtil.getString(key, locale);
	}
}
