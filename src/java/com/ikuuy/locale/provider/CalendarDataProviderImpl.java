package com.ikuuy.locale.provider;

import java.util.Locale;
import java.util.spi.CalendarDataProvider;

import com.ikuuy.locale.util.ExtLocalesUtil;

/**
 * An implementation class for {@link CalendarDataProvider} which provides
 * locale-dependent {@link Calendar} parameters.
 *
 * @author Yuki Yamada
 *
 */
public class CalendarDataProviderImpl extends CalendarDataProvider {

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
	public int getFirstDayOfWeek(final Locale locale) throws NullPointerException {
		if (locale == null) {
			throw new NullPointerException("locale:null");
		}

		return ExtLocalesUtil.getInt("FirstDayOfWeek", locale);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMinimalDaysInFirstWeek(final Locale locale) throws NullPointerException {
		if (locale == null) {
			throw new NullPointerException("locale:null");
		}

		return ExtLocalesUtil.getInt("MinimalDaysInFirstWeek", locale);
	}
}
