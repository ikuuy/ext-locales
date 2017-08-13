package com.ikuuy.locale.provider;

import java.util.Locale;
import java.util.TimeZone;
import java.util.spi.TimeZoneNameProvider;

import com.ikuuy.locale.util.ExtLocalesUtil;

/**
 * An implementation class for {@link TimeZoneNameProvider} which provides
 * localized names for {@link TimeZone}.
 *
 * @author Yuki Yamada
 *
 */
public class TimeZoneNameProviderImpl extends TimeZoneNameProvider {

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
	public String getDisplayName(final String ID, final boolean daylight, final int style, final Locale locale)
			throws IllegalArgumentException, NullPointerException {
		if (ID == null) {
			throw new NullPointerException("ID:null");
		} else if (locale == null) {
			throw new NullPointerException("locale:null");
		} else if (!ExtLocalesUtil.isAvailableLocale(locale)) {
			throw new IllegalArgumentException("locale:" + locale.toString());
		}

		String displayTimeZone = null;
		StringBuffer key = new StringBuffer(40);
		key.append("ZoneStrings." + ID + ".");
		if (daylight) {
			key.append("DAYLIGHT.");
		} else {
			key.append("STANDARD.");
		}
		if (style == TimeZone.SHORT) {
			key.append("SHORT");
		} else if (style == TimeZone.LONG) {
			key.append("LONG");
		} else {
			throw new IllegalArgumentException("style:" + style);
		}

		if (ExtLocalesUtil.containsKey(key.toString(), locale, true)) {
			displayTimeZone = ExtLocalesUtil.getString(key.toString(), locale, true);
		}

		return displayTimeZone;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getGenericDisplayName(final String ID, final int style, final Locale locale)
			throws IllegalArgumentException, NullPointerException {
		if (ID == null) {
			throw new NullPointerException("ID:null");
		} else if (locale == null) {
			throw new NullPointerException("locale:null");
		} else if (!ExtLocalesUtil.isAvailableLocale(locale)) {
			throw new IllegalArgumentException("locale:" + locale.toString());
		}

		String displayTimeZone = null;
		StringBuffer key = new StringBuffer(40);
		key.append("ZoneStrings." + ID + ".GENERIC.");
		if (style == TimeZone.SHORT) {
			key.append("SHORT");
		} else if (style == TimeZone.LONG) {
			key.append("LONG");
		} else {
			throw new IllegalArgumentException("style:" + style);
		}

		if (ExtLocalesUtil.containsKey(key.toString(), locale, true)) {
			displayTimeZone = ExtLocalesUtil.getString(key.toString(), locale, true);
		}

		return displayTimeZone;
	}
}
