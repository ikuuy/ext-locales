package com.ikuuy.locale.provider;

import java.util.Locale;
import java.util.spi.LocaleNameProvider;

import com.ikuuy.locale.util.ExtLocalesUtil;

/**
 * An implementation class for {@link LocaleNameProvider} which provides localized
 * names for {@link Locale}.
 *
 * @author Yuki Yamada
 *
 */
public class LocaleNameProviderImpl extends LocaleNameProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Locale[] getAvailableLocales() {
		Locale[] locales = ExtLocalesUtil.getAvailableLocales();
		Locale[] availableLocales = new Locale[locales.length + 1];
		availableLocales[0] = Locale.ROOT;
		System.arraycopy(locales, 0, availableLocales, 1, locales.length);
		return availableLocales;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplayCountry(final String countryCode, final Locale locale) throws IllegalArgumentException,
			NullPointerException {
		if (countryCode == null) {
			throw new NullPointerException("countryCode:null");
		} else if (!countryCode.matches("^[A-Z]{2}$") && !countryCode.matches("^[0-9]{3}$")) {
			// The country code string should be in the form of two upper-case letters
			// or three digit letters.
			throw new IllegalArgumentException("countryCode:" + countryCode);
		} else if (locale == null) {
			throw new NullPointerException("locale:null");
		} else if (!ExtLocalesUtil.isAvailableLocale(locale)) {
			throw new IllegalArgumentException("locale:" + locale.toString());
		}

		String displayCountry = null;
		String key = "Country." + countryCode;

		if (ExtLocalesUtil.containsKey(key, locale)) {
			displayCountry = ExtLocalesUtil.getString(key, locale);
		}

		return displayCountry;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplayLanguage(final String languageCode, final Locale locale) throws IllegalArgumentException,
			NullPointerException {
		if (languageCode == null) {
			throw new NullPointerException("languageCode:null");
		} else if (!languageCode.matches("^[a-z]{2,8}$")) {
			// The language code string should be in the form of
			// two to eight lower-case letters.
			throw new IllegalArgumentException("languageCode:" + languageCode);
		} else if (locale == null) {
			throw new NullPointerException("locale:null");
		} else if (!ExtLocalesUtil.isAvailableLocale(locale)) {
			throw new IllegalArgumentException("locale:" + locale.toString());
		}

		String displayLanguage = null;
		String key = "Language." + languageCode;

		if (ExtLocalesUtil.containsKey(key, locale)) {
			displayLanguage = ExtLocalesUtil.getString(key, locale);
		}

		return displayLanguage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplayScript(final String scriptCode, final Locale locale) throws IllegalArgumentException,
			NullPointerException {
		if (scriptCode == null) {
			throw new NullPointerException("scriptCode:null");
		} else if (!scriptCode.matches("^[A-Z][a-z]{3}$")) {
			// The script code string should be in the form of four title case letters.
			throw new IllegalArgumentException("scriptCode:" + scriptCode);
		} else if (locale == null) {
			throw new NullPointerException("locale:null");
		} else if (!ExtLocalesUtil.isAvailableLocale(locale)) {
			throw new IllegalArgumentException("locale:" + locale.toString());
		}

		String displayScript = null;
		String key = "Script." + scriptCode;

		if (ExtLocalesUtil.containsKey(key, locale)) {
			displayScript = ExtLocalesUtil.getString(key, locale);
		}

		return displayScript;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplayVariant(final String variant, final Locale locale) throws IllegalArgumentException,
			NullPointerException {
		if (variant == null) {
			throw new NullPointerException("variant:null");
		} else if (locale == null) {
			throw new NullPointerException("locale:null");
		} else if (!ExtLocalesUtil.isAvailableLocale(locale)) {
			throw new IllegalArgumentException("locale:" + locale.toString());
		}

		String displayVariant = null;
		String key = "Variant." + variant;

		if (ExtLocalesUtil.containsKey(key, locale)) {
			displayVariant = ExtLocalesUtil.getString(key, locale);
		}

		return displayVariant;
	}
}
