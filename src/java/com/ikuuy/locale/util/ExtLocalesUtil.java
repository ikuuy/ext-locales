package com.ikuuy.locale.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Logger;

/**
 * An utility class to get a value from the resource bundles.
 *
 * @author Yuki Yamada
 *
 */
public final class ExtLocalesUtil {

	/**
	 * A global logger.
	 */
	private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * A set of all locales for the resource bundles.
	 */
	private static final Set<Locale> LOCALES = new HashSet<Locale>();

	private ExtLocalesUtil() {
	}

	static {
		// Load the locale config.
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream inStream = loader.getResourceAsStream("ExtLocales.config");

		Properties props = new Properties();
		if (inStream != null) {
			try {
				props.load(inStream);
			} catch (IOException e) {
				LOG.warning(e.getMessage());
			} finally {
				try {
					inStream.close();
				} catch (IOException e) {
					LOG.warning(e.getMessage());
				}
			}
		}

		String value = props.getProperty("locales");
		if (value != null) {
			Locale.Builder builder = new Locale.Builder();
			String[] locales = value.split(",");
			for (String localeStr : locales) {
				Locale locale = null;
				String beforeSharp;
				String afterSharp = null;
				if (localeStr.contains("_#")) {
					beforeSharp = localeStr.trim().split("_#", 2)[0];
					afterSharp = localeStr.trim().split("_#", 2)[1];
				} else {
					beforeSharp = localeStr.trim();
				}
				String[] params = beforeSharp.split("_", 3);
				switch (params.length) {
				case 1:
					// ll
					locale = new Locale(params[0]);
					break;
				case 2:
					// ll_RR
					// _RR
					locale = new Locale(params[0], params[1]);
					break;
				case 3:
					// ll__vvv
					// ll_RR_vvv
					// _RR_vvv
					locale = new Locale(params[0], params[1], params[2]);
					break;
				default:
					continue;
				}

				if (afterSharp == null) {
					LOCALES.add(locale);
				} else {
					String script = null;
					String extension = null;
					if (afterSharp.matches("^[A-Z][a-z]{3}..+")) {
						// #Ssss_e-eeee
						script = afterSharp.substring(0, 4);
						extension = afterSharp.substring(5);
					} else if (afterSharp.matches("^[A-Z][a-z]{3}$")) {
						// #Ssss
						script = afterSharp;
					} else {
						// #e-eeee
						extension = afterSharp;
					}

					builder.clear();
					builder.setLocale(locale);
					if (script != null) {
						builder.setScript(script);
					}
					if (extension != null && extension.matches("^[0-9a-zA-Z]-.+")) {
						builder.setExtension(extension.charAt(0), extension.substring(2));
					}
					LOCALES.add(builder.build());
				}
			}
		}
	}

	/**
	 * Returns an array of all locales for the resource bundles.
	 *
	 * @return an array of locales.
	 */
	public static Locale[] getAvailableLocales() {
		return LOCALES.toArray(new Locale[LOCALES.size()]);
	}

	/**
	 * Determines whether the given <code>locale</code> is available
	 * for the resource bundles.
	 *
	 * @param locale possible locale.
	 * @return <code>true</code> if the given <code>locale</code> is available for the
	 *     resource bundles; <code>false</code> otherwise.
	 */
	public static boolean isAvailableLocale(final Locale locale) {
		boolean isAvailable = false;

		if (getBundle(locale) != null) {
			isAvailable = true;
		}

		return isAvailable;
	}

	/**
	 * Returns a string for the given <code>key</code> from the resource bundle.
	 *
	 * @param key the key for the desired string.
	 * @param locale the desired locale.
	 * @return the string for the given <code>key</code> and <code>locale</code>.
	 */
	public static String getString(final String key, final Locale locale) {
		String value = null;

		if (key != null) {
			ResourceBundle bundle = getBundle(locale);
			if (bundle != null) {
				value = bundle.getString(key);
			}
		}

		return value;
	}

	/**
	 * Returns a <code>char</code> value for the given <code>key</code> from the
	 * resource bundle.
	 *
	 * @param key the key for the desired <code>char</code> value.
	 * @param locale the desired locale.
	 * @return the <code>char</code> value for the given <code>key</code> and
	 *     <code>locale</code>.
	 */
	public static char getChar(final String key, final Locale locale) {
		char value = 0;

		String str = getString(key, locale);
		if (str != null && !str.isEmpty()) {
			value = str.charAt(0);
		}

		return value;
	}

	/**
	 * Returns a <code>int</code> value for the given <code>key</code> from the
	 * resource bundle.
	 *
	 * @param key the key for the desired <code>int</code> value.
	 * @param locale the desired locale.
	 * @return the <code>int</code> value for the given <code>key</code> and
	 *     <code>locale</code>.
	 */
	public static int getInt(final String key, final Locale locale) {
		int value = 0;

		String str = getString(key, locale);
		if (str != null && !str.isEmpty()) {
			try {
				value = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				LOG.warning(e.getMessage());
			}
		}

		return value;
	}

	/**
	 * Determines whether the given <code>key</code> is contained in the resource
	 * bundle.
	 *
	 * @param key possible key.
	 * @param locale the desired locale.
	 * @return <code>true</code> if the given <code>key</code> is contained in the
	 *     resource bundle; <code>false</code> otherwise.
	 */
	public static boolean containsKey(final String key, final Locale locale) {
		boolean contain = false;

		if (key != null) {
			ResourceBundle bundle = getBundle(locale);
			if (bundle != null) {
				contain = bundle.containsKey(key);
			}
		}

		return contain;
	}

	/**
	 * Returns an enumeration of the keys.
	 *
	 * @param locale the desired locale.
	 * @return an <code>Enumeration</code> of the keys contained in this
	 *         <code>ResourceBundle</code> and its parent bundles.
	 */
	public static Enumeration<String> getKeys(final Locale locale) {
		Enumeration<String> keys = null;

		ResourceBundle bundle = getBundle(locale);
		if (bundle != null) {
			keys = bundle.getKeys();
		}

		return keys;
	}

	/**
	 * Get a resource bundle using the specified locale.
	 *
	 * @param locale the desired locale.
	 * @return the resource bundle for the given locale, or <code>null</code> if the
	 *     resource bundle could not be found.
	 */
	private static ResourceBundle getBundle(final Locale locale) {
		ResourceBundle bundle = null;

		if (locale != null) {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();

			try {
				bundle = ResourceBundle.getBundle("ExtLocales", locale, loader);
			} catch (MissingResourceException e) {
				LOG.warning(e.getMessage());
			}
		}

		return bundle;
	}
}
