# Extended Locales Library

## Project Purpose

Some languages are not fully supported in Java. e.g., DateFormat, NumberFormat, and locale names are not. This project is in order to support them by implementing the locale sensitive service provider which is offered in the Java Extension Mechanism.


----------------------------------------------------------------------------------------------------
## Usage

Download [ext-locales-x.x.jar](https://github.com/ikuuy/ext-locales/releases).

### Java 9

It is needed to specify the following options to run your program.
```
java -p <path of ext-locales-x.x.jar> -Djava.locale.providers=CLDR,COMPAT,SPI <your mainclass>
```

### Java 8 or earlier

It is needed to store ext-locales-x.x.jar into the proper location.

For Tomcat 6 or later, the target location is $CATALINA_HOME/endorsed.  
For Tomcat 5.x, the target location is $CATALINA_HOME/common/endorsed.  
Additionally, $JAVA_HOME/jre/lib/endorsed can be the target instead of the above locations.  
Please manually create the 'endorsed' directory and copy it there.


----------------------------------------------------------------------------------------------------
## Remarks

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

2. ext-locales-x.x.jar works on Java 8 or 9. If you use Java 6 or 7, please manually build it with Maven.


----------------------------------------------------------------------------------------------------
## Adding more locales

If you'd like to add another unsupported locale, please follow the steps below.

1. Add the locale string into 'locales=' within src/resources/ExtLocales.config
2. Create the resource bundle file as src/resources/ExtLocales_XX.properties
3. Localize the content of the resource bundle.
4. Build and store it into the proper location.
