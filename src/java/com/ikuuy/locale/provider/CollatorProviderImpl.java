package com.ikuuy.locale.provider;

import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.text.spi.CollatorProvider;
import java.util.Locale;
import java.util.logging.Logger;

import com.ikuuy.locale.util.ExtLocalesUtil;

/**
 * An implementation class for {@link CollatorProvider} which provides a
 * localized {@link Collator}.
 *
 * @author Yuki Yamada
 *
 */
public class CollatorProviderImpl extends CollatorProvider {

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
	public Collator getInstance(final Locale locale) throws IllegalArgumentException, NullPointerException {
		if (locale == null) {
			throw new NullPointerException("locale:null");
		} else if (!ExtLocalesUtil.isAvailableLocale(locale)) {
			throw new IllegalArgumentException("locale:" + locale.toString());
		}

		RuleBasedCollator collator = (RuleBasedCollator) Collator.getInstance(Locale.ENGLISH);
		String rules = collator.getRules() + ExtLocalesUtil.getString("CollatorRules", locale);

		try {
			collator = new RuleBasedCollator(rules);
		} catch (ParseException e) {
			LOG.warning(e.getMessage());
		}

		return collator;
	}
}
