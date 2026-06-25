# changelogs

## V3.1.5
* new methods `jsonToSafe` and `setByPath` to `JsonUtils`.
* new methods `getSysProps` and `printSysProps` to `SysUtils`.
* remove methods `displaySystemPropsInStdout` and `printSystemProperties` from `EnvUtils`.

## V3.1.4
* removed deprecated method `readInputStream()` from class `BufferedIoUtils`.

## V3.1.3
* new `VersionUtils` class for comparing software versions.

## V3.1.2
* new `NetworkUtils` class.
* add `countToDisplaySize` method to `CalcUtils`.

## v3.0.0
* new `MonthDayUtils` class.
* new `ImageUtils` class.
* new `LocalDateTimeUtils` class.
* new `splitWithoutBlank` to `TextUtils`; 
* new `equalsIgnoreYear()` method to `LocalDateTimeUtils`
* new methods to `CalcUtils`, including `add()`, `subtract()`, `min()`, `max()`, `limitIn()` and `limitInZeroToOne()` for `BigDecimal`
* new `CronUtils`; 
* new `object2JsonSafe` method to `JsonUtils`.
* update dependencies
