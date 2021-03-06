package com.ikuuy.locale.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	/**
	 * A set of all locales for the resource bundles which contain time zone names.
	 */
	private static final Set<Locale> TZ_NAME_LOCALES = new HashSet<Locale>();

	/**
	 * A compiled representation of a regular expression for substitution.
	 */
	private static final Pattern SUBST_PATTERN = Pattern.compile("\\$\\{([^\\}]+)\\}");

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
		initAvailableLocales(props, "locales", LOCALES);
		initAvailableLocales(props, "TimeZoneName.locales", TZ_NAME_LOCALES);
	}

	/**
	 * Initialize the set of all locales for the resource bundles.
	 *
	 * @param props a property list from the locale config.
	 * @param key the key for the desired set of locales.
	 * @param localeSet the desired set of locales.
	 */
	private static void initAvailableLocales(final Properties props, final String key, final Set<Locale> localeSet) {
		String value = props.getProperty(key);
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
					localeSet.add(locale);
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
					localeSet.add(builder.build());
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
	 * Returns an array of all locales for the resource bundles which contain time
	 * zone names.
	 *
	 * @return an array of locales.
	 */
	public static Locale[] getAvailableTimeZoneNameLocales() {
		return TZ_NAME_LOCALES.toArray(new Locale[TZ_NAME_LOCALES.size()]);
	}

	/**
	 * Determines whether the given <code>locale</code> is one of the
	 * <code>availableLocales<code>.
	 *
	 * @param locale possible locale.
	 * @param availableLocales an array of all available locales.
	 * @return <code>true</code> if the given <code>locale</code> is one of the
	 *     <code>availableLocales<code>; <code>false</code> otherwise.
	 */
	public static boolean isAvailableLocale(final Locale locale, final Locale... availableLocales) {
		boolean isAvailable = false;

		Control control = Control.getNoFallbackControl(Control.FORMAT_DEFAULT);
		List<Locale> candidateLocales = control.getCandidateLocales("", locale);
		for (Locale targetLocale : candidateLocales) {
			if (Arrays.asList(availableLocales).contains(targetLocale)) {
				isAvailable = true;
				break;
			}
		}

		return isAvailable;
	}

	/**
	 * Returns a string for the given <code>key</code> from the resource bundle.
	 *
	 * @param key the key for the desired string.
	 * @param locale the desired locale.
	 * @return the string for the given <code>key</code> and <code>locale</code>.
     * @throws MissingResourceException if no object for the given key can be found.
	 */
	public static String getString(final String key, final Locale locale) throws MissingResourceException {
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
	 * Returns a string for the given <code>key</code> from the resource bundle.
	 *
	 * @param key the key for the desired string.
	 * @param locale the desired locale.
	 * @param substitute a flag whether substitution for <code>${key}</code> within
	 *     the string is enabled.
	 * @return the string for the given <code>key</code> and <code>locale</code>.
	 * @throws MissingResourceException if no object for the given <code>key</code> or
	 *     any key to be substituted can be found.
	 */
	public static String getString(final String key, final Locale locale, final boolean substitute)
			throws MissingResourceException {
		String value = null;

		if (!substitute) {
			value = getString(key, locale);
		} else if (key != null) {
			ResourceBundle bundle = getBundle(locale);
			if (bundle != null) {
				StringBuffer buf = new StringBuffer();
				String srcValue = bundle.getString(key);
				Matcher matcher = SUBST_PATTERN.matcher(srcValue);
				while (matcher.find()) {
					String subKey = matcher.group(1);
					String replacement = bundle.getString(subKey);
					matcher.appendReplacement(buf, Matcher.quoteReplacement(replacement));
				}
				matcher.appendTail(buf);
				value = buf.toString();
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
     * @throws MissingResourceException if no object for the given key can be found.
	 */
	public static char getChar(final String key, final Locale locale) throws MissingResourceException {
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
     * @throws MissingResourceException if no object for the given key can be found.
	 */
	public static int getInt(final String key, final Locale locale) throws MissingResourceException {
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
	 * Determines whether the given <code>key</code> is contained in the resource
	 * bundle.
	 *
	 * @param key possible key.
	 * @param locale the desired locale.
	 * @param substitute a flag whether substitution for <code>${key}</code> within
	 *     the string is enabled.
	 * @return <code>true</code> if the given <code>key</code> and the keys to be
	 *     substituted within the string are contained in the resource bundle;
	 *     <code>false</code> otherwise.
	 */
	public static boolean containsKey(final String key, final Locale locale, final boolean substitute) {
		boolean contain = false;

		if (!substitute) {
			contain = containsKey(key, locale);
		} else if (key != null) {
			ResourceBundle bundle = getBundle(locale);
			if (bundle != null && bundle.containsKey(key)) {
				contain = true;
				String srcValue = bundle.getString(key);
				Matcher matcher = SUBST_PATTERN.matcher(srcValue);
				while (matcher.find()) {
					String subKey = matcher.group(1);
					if (!bundle.containsKey(subKey)) {
						contain = false;
						break;
					}
				}
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
