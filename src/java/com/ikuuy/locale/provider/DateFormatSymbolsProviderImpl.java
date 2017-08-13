package com.ikuuy.locale.provider;

import java.text.DateFormatSymbols;
import java.text.spi.DateFormatSymbolsProvider;
import java.util.Locale;

import com.ikuuy.locale.util.ExtLocalesUtil;

/**
 * An implementation class for {@link DateFormatSymbolsProvider} which provides a
 * localized {@link DateFormatSymbols}.
 *
 * @author Yuki Yamada
 *
 */
public class DateFormatSymbolsProviderImpl extends DateFormatSymbolsProvider {

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
	public DateFormatSymbols getInstance(final Locale locale) throws IllegalArgumentException, NullPointerException {
		if (locale == null) {
			throw new NullPointerException("locale:null");
		} else if (!ExtLocalesUtil.isAvailableLocale(locale)) {
			throw new IllegalArgumentException("locale:" + locale.toString());
		}

		DateFormatSymbols symbols = DateFormatSymbols.getInstance(Locale.ROOT);
		symbols.setEras(new String[] {
				ExtLocalesUtil.getString("Eras.BC", locale),
				ExtLocalesUtil.getString("Eras.AD", locale) });
		symbols.setMonths(new String[] {
				ExtLocalesUtil.getString("Months.JAN", locale),
				ExtLocalesUtil.getString("Months.FEB", locale),
				ExtLocalesUtil.getString("Months.MAR", locale),
				ExtLocalesUtil.getString("Months.APR", locale),
				ExtLocalesUtil.getString("Months.MAY", locale),
				ExtLocalesUtil.getString("Months.JUN", locale),
				ExtLocalesUtil.getString("Months.JUL", locale),
				ExtLocalesUtil.getString("Months.AUG", locale),
				ExtLocalesUtil.getString("Months.SEP", locale),
				ExtLocalesUtil.getString("Months.OCT", locale),
				ExtLocalesUtil.getString("Months.NOV", locale),
				ExtLocalesUtil.getString("Months.DEC", locale) });
		symbols.setShortMonths(new String[] {
				ExtLocalesUtil.getString("ShortMonths.JAN", locale),
				ExtLocalesUtil.getString("ShortMonths.FEB", locale),
				ExtLocalesUtil.getString("ShortMonths.MAR", locale),
				ExtLocalesUtil.getString("ShortMonths.APR", locale),
				ExtLocalesUtil.getString("ShortMonths.MAY", locale),
				ExtLocalesUtil.getString("ShortMonths.JUN", locale),
				ExtLocalesUtil.getString("ShortMonths.JUL", locale),
				ExtLocalesUtil.getString("ShortMonths.AUG", locale),
				ExtLocalesUtil.getString("ShortMonths.SEP", locale),
				ExtLocalesUtil.getString("ShortMonths.OCT", locale),
				ExtLocalesUtil.getString("ShortMonths.NOV", locale),
				ExtLocalesUtil.getString("ShortMonths.DEC", locale) });
		symbols.setWeekdays(new String[] {"",
				ExtLocalesUtil.getString("Weekdays.SUN", locale),
				ExtLocalesUtil.getString("Weekdays.MON", locale),
				ExtLocalesUtil.getString("Weekdays.TUE", locale),
				ExtLocalesUtil.getString("Weekdays.WED", locale),
				ExtLocalesUtil.getString("Weekdays.THU", locale),
				ExtLocalesUtil.getString("Weekdays.FRI", locale),
				ExtLocalesUtil.getString("Weekdays.SAT", locale) });
		symbols.setShortWeekdays(new String[] {"",
				ExtLocalesUtil.getString("ShortWeekdays.SUN", locale),
				ExtLocalesUtil.getString("ShortWeekdays.MON", locale),
				ExtLocalesUtil.getString("ShortWeekdays.TUE", locale),
				ExtLocalesUtil.getString("ShortWeekdays.WED", locale),
				ExtLocalesUtil.getString("ShortWeekdays.THU", locale),
				ExtLocalesUtil.getString("ShortWeekdays.FRI", locale),
				ExtLocalesUtil.getString("ShortWeekdays.SAT", locale) });
		symbols.setAmPmStrings(new String[] {
				ExtLocalesUtil.getString("AmPmStrings.AM", locale),
				ExtLocalesUtil.getString("AmPmStrings.PM", locale) });
		symbols.setLocalPatternChars(ExtLocalesUtil.getString(
				"LocalPatternChars", locale));

		String prefix = "ZoneStrings";
		String[][] zoneStrings = symbols.getZoneStrings();
		for (String[] values : zoneStrings) {
			String zoneId = values[0];
			String key = prefix + "." + zoneId + ".STANDARD.LONG";
			if (ExtLocalesUtil.containsKey(key, locale, true)) {
				values[1] = ExtLocalesUtil.getString(key, locale, true);
			}
			key = prefix + "." + zoneId + ".STANDARD.SHORT";
			if (ExtLocalesUtil.containsKey(key, locale, true)) {
				values[2] = ExtLocalesUtil.getString(key, locale, true);
			}
			key = prefix + "." + zoneId + ".DAYLIGHT.LONG";
			if (ExtLocalesUtil.containsKey(key, locale, true)) {
				values[3] = ExtLocalesUtil.getString(key, locale, true);
			}
			key = prefix + "." + zoneId + ".DAYLIGHT.SHORT";
			if (ExtLocalesUtil.containsKey(key, locale, true)) {
				values[4] = ExtLocalesUtil.getString(key, locale, true);
			}
		}
		symbols.setZoneStrings(zoneStrings);

		return symbols;
	}
}
