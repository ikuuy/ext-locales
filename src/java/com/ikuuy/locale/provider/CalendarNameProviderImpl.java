package com.ikuuy.locale.provider;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.spi.CalendarNameProvider;

import com.ikuuy.locale.util.ExtLocalesUtil;

/**
 * An implementation class for {@link CalendarNameProvider} which provides
 * localized names for {@link Calendar}.
 *
 * @author Yuki Yamada
 *
 */
public class CalendarNameProviderImpl extends CalendarNameProvider {

	/**
	 * A global logger.
	 */
	private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

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
	public String getDisplayName(final String calendarType, final int field, final int value, final int style,
			final Locale locale) throws IllegalArgumentException, NullPointerException {
		if (locale == null) {
			throw new NullPointerException("locale:null");
		}

		String displayName = null;
		String key = generateKey(calendarType, field, value, style);
		if (ExtLocalesUtil.containsKey(key, locale)) {
			displayName = ExtLocalesUtil.getString(key, locale);
		} else {
			displayName = getDisplayNameFromDateFormatSymbols(calendarType, field, value, style, locale);
		}

		return displayName;
	}

	/**
	 * Returns a key for the resource bundle.
	 *
	 * @param calendarType the calendar type.
	 * @param field the <code>Calendar</code> field index.
	 * @param value the value of the <code>Calendar field</code>.
	 * @param style the string representation style.
	 * @return a key for the resource bundle.
	 * @throws IllegalArgumentException if <code>field</code> or <code>style</code> is 
	 *         invalid.
	 */
	protected String generateKey(final String calendarType, final int field, final int value, final int style)
			throws IllegalArgumentException {
		if (field < 0 || field >= Calendar.FIELD_COUNT) {
			throw new IllegalArgumentException("field:" + field);
		}

		StringBuffer key = new StringBuffer(40);

		key.append("Calendar." + calendarType + ".");
		switch (field) {
		case Calendar.ERA:
			key.append("Eras");
			break;
		case Calendar.YEAR:
			key.append("Years");
			break;
		case Calendar.MONTH:
			key.append("Months");
			break;
		case Calendar.DAY_OF_WEEK:
			key.append("Weekdays");
			break;
		case Calendar.AM_PM:
			key.append("AmPmStrings");
			break;
		default:
			break;
		}
		key.append("." + value + ".");
		switch (style) {
		case Calendar.SHORT:
			key.append("SHORT");
			break;
		case Calendar.LONG:
			key.append("LONG");
			break;
		case Calendar.NARROW_FORMAT:
			key.append("NARROW_FORMAT");
			break;
		case Calendar.SHORT_STANDALONE:
			key.append("SHORT_STANDALONE");
			break;
		case Calendar.LONG_STANDALONE:
			key.append("LONG_STANDALONE");
			break;
		case Calendar.NARROW_STANDALONE:
			key.append("NARROW_STANDALONE");
			break;
		default:
			throw new IllegalArgumentException("style:" + style);
		}

		return key.toString();
	}

	/**
	 * Returns the string representation (display name) of the calendar
	 * <code>field value</code> in the given <code>style</code> and
	 * <code>locale</code> from {@link DateFormatSymbols}. If no string
	 * representation is applicable, <code>null</code> is returned.
	 *
	 * @param calendarType the calendar type.
	 * @param field the <code>Calendar</code> field index.
	 * @param value the value of the <code>Calendar field</code>.
	 * @param style the string representation style.
	 * @param locale the desired locale.
	 * @return the string representation of the <code>field value</code>.
	 */
	protected String getDisplayNameFromDateFormatSymbols(final String calendarType, final int field, final int value,
			final int style, final Locale locale) {
		String displayName = null;

		DateFormatSymbols symbols = DateFormatSymbols.getInstance(locale);
		String[] symbolsArray = null;
		switch (field) {
		case Calendar.ERA:
			if ("gregory".equals(calendarType)) {
				symbolsArray = symbols.getEras();
			}
			break;
		case Calendar.MONTH:
			if ((style & Calendar.LONG) == 0) {
				symbolsArray = symbols.getShortMonths();
			} else {
				symbolsArray = symbols.getMonths();
			}
			break;
		case Calendar.DAY_OF_WEEK:
			if ((style & Calendar.LONG) == 0) {
				symbolsArray = symbols.getShortWeekdays();
			} else {
				symbolsArray = symbols.getWeekdays();
			}
			break;
		case Calendar.AM_PM:
			symbolsArray = symbols.getAmPmStrings();
			break;
		default:
			break;
		}

		if (symbolsArray != null && 0 <= value && value < symbolsArray.length) {
			displayName = symbolsArray[value];
			if ((style & Calendar.NARROW_FORMAT) != 0) {
				Pattern pattern = Pattern.compile("^(\\d+).*");
				Matcher matcher = pattern.matcher(displayName);
				if (matcher.find()) {
					// Use the first digit(s).
					displayName = matcher.group(1);
				} else if (!displayName.isEmpty()) {
					// Use the first character.
					displayName = displayName.substring(0, 1);
				}
			}
		}

		return displayName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Integer> getDisplayNames(final String calendarType, final int field, final int style,
			final Locale locale) throws NullPointerException {
		if (locale == null) {
			throw new NullPointerException("locale:null");
		}

		Map<String, Integer> displayNames = null;

		int minimum = 1;
		int maximum = 1;
		if (field == Calendar.ERA || field == Calendar.MONTH || field == Calendar.DAY_OF_WEEK
				|| field == Calendar.AM_PM) {
			// Get the minimum and maximum values for the calendar field.
			Calendar calendar;
			if ("buddhist".equals(calendarType)) {
				calendar = Calendar.getInstance(new Locale("th", "TH", "TH"));
			} else if ("japanese".equals(calendarType)) {
				calendar = Calendar.getInstance(new Locale("ja", "JP", "JP"));
			} else {
				calendar = Calendar.getInstance(Locale.ENGLISH);
			}
			minimum = calendar.getMinimum(field);
			maximum = calendar.getMaximum(field);
		}

		if (style == Calendar.ALL_STYLES) {
			for (int specificStyle : new int[] { Calendar.SHORT, Calendar.LONG, Calendar.NARROW_FORMAT,
					Calendar.SHORT_STANDALONE, Calendar.LONG_STANDALONE, Calendar.NARROW_STANDALONE }) {
				Map<String, Integer> map = getDisplayNames(calendarType, field, minimum, maximum, specificStyle, locale);
				if (displayNames == null) {
					displayNames = map;
				} else if (map != null) {
					displayNames.putAll(map);
				}
			}
		} else {
			displayNames = getDisplayNames(calendarType, field, minimum, maximum, style, locale);
		}

		return displayNames;
	}

	/**
     * Returns a <code>Map</code> containing all string representations (display
     * names) of the <code>Calendar field</code> in the given <code>style</code>
     * and <code>locale</code> and their corresponding field values between
     * <code>minimum</code> and <code>maximum</code>.
	 *
	 * @param calendarType the calendar type.
	 * @param field the <code>Calendar</code> field index.
	 * @param minimum the minimum value for the given <code>Calendar field</code>.
	 * @param maximum the maximum value for the given <code>Calendar field</code>.
	 * @param style the string representation style.
	 * @param locale the desired locale.
	 * @return a <code>Map</code> containing all display names of <code>field</code>.
	 * @throws NullPointerException if <code>locale</code> is <code>null</code>.
	 */
	protected Map<String, Integer> getDisplayNames(final String calendarType, final int field, final int minimum,
			final int maximum, final int style, final Locale locale) throws NullPointerException {
		Map<String, Integer> displayNames = null;

		for (int value = minimum; value <= maximum; value++) {
			String displayName = null;
			try {
				displayName = getDisplayName(calendarType, field, value, style, locale);
			} catch (IllegalArgumentException e) {
				LOG.warning(e.getMessage());
			}
			if (displayName != null) {
				if (displayNames == null) {
					displayNames = new HashMap<String, Integer>();
				} else if (displayNames.containsKey(displayName)) {
					// Returns null if the name is not unique.
					displayNames = null;
					break;
				}
				displayNames.put(displayName, value);
			}
		}

		return displayNames;
	}
}
