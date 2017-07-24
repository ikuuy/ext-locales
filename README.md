# Extended Locales Library

## Project Purpose:

Some languages are not fully supported in Java. e.g., DateFormat, NumberFormat, and locale names are not. This project is in order to support them by implementing the locale sensitive service provider which is offered in the Java Extension Mechanism.


----------------------------------------------------------------------------------------------------
## Usage:

It is needed to store [ext-locales-x.x.jar](https://github.com/ikuuy/ext-locales/releases) into the proper location.

For Tomcat 6 or later, the target location is $CATALINA_HOME/endorsed.  
For Tomcat 5.x, the target location is $CATALINA_HOME/common/endorsed.  
Additionally, $JAVA_HOME/jre/lib/endorsed can be the target instead of the above locations.  
Please manually create the 'endorsed' directory and copy it there.


----------------------------------------------------------------------------------------------------
## Remarks:

1. In order to enable the extension of the service provider, please use
```
DateFormatSymbols.getInstance(locale);
DecimalFormatSymbols.getInstance(locale);
```
instead of  
```
new DateFormatSymbols(locale);
new DecimalFormatSymbols(locale);
```
in your code, because the latters do not support the extension.  

2. Java 6 or later is required because the locale sensitive service provider has been introduced in Java 6.


----------------------------------------------------------------------------------------------------
## Adding more locales:

If you'd like to add another unsupported locale, please follow the steps below.

1. Add the locale string into 'locales=' within src/resources/ExtLocales.config
2. Create the resource bundle file as src/resources/ExtLocales_XX.properties
3. Localize the content of the resource bundle.
4. Build and store it into the proper location.

However, before trying the above steps, please consider to simply specify **-Djava.locale.providers=JRE,CLDR,SPI** (or CLDR,JRE,SPI) into JAVA_OPTS and check whether the desired locale is actually supported by using the CLDR's data. The default behaviour equals to -Djava.locale.providers=JRE,SPI.  
https://docs.oracle.com/javase/8/docs/technotes/guides/intl/enhancements.8.html
