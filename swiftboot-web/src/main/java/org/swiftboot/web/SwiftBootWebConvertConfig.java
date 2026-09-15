package org.swiftboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.MonthDayDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.YearMonthDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.MonthDaySerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.swiftboot.web.config.SwiftBootWebConfigBean;
import org.swiftboot.web.format.*;
import org.swiftboot.web.util.TimezoneUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Config conversion at web tier.
 *
 * @since 3.0
 */
@Configuration
public class SwiftBootWebConvertConfig implements WebMvcConfigurer {

    @Resource
    private SwiftBootWebConfigBean configBean;

    /**
     * for HTTP URL Query String / Form Data
     *
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
//        registry.addFormatterForFieldType(LocalDateTime.class, new DynamicTimeZoneLocalDateTimeFormatter(configBean.getFormatPatternLocalDateTime()));
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
//        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalDateTime()));
//        registrar.setDateFormatter(DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalDate()));
//        registrar.setTimeFormatter(DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalTime()));
//        registrar.registerFormatters(registry);
        //  YearMonth and MonthDay have to do it manually.
        registry.addConverter(new LocalDateTimeConverter());
        registry.addConverter(new LocalDateConverter());
        registry.addConverter(new LocalTimeConverter());
        registry.addConverter(new InstantConverter());
        registry.addConverter(new OffsetDateTimeConverter());
        registry.addConverter(new YearMonthConverter());
        registry.addConverter(new MonthDayConverter());
    }

    /**
     * for application/json
     *
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // Serializer
        javaTimeModule.addSerializer(LocalDateTime.class, new DynamicTimeZoneLocalDateTimeSerializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalDateTime())));
//        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalDateTime())));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalDate())));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalTime())));
        javaTimeModule.addSerializer(Instant.class, new DynamicTimeZoneInstantSerializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternInstant())));
        javaTimeModule.addSerializer(OffsetDateTime.class, new DynamicTimeZoneOffsetDateTimeSerializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternOffsetDateTime())));
        javaTimeModule.addSerializer(YearMonth.class, new YearMonthSerializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternYearMonth())));
        javaTimeModule.addSerializer(MonthDay.class, new MonthDaySerializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternMonthDay())));
        // Deserializer
        javaTimeModule.addDeserializer(LocalDateTime.class, new DynamicTimeZoneLocalDateTimeDeserializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalDateTime())));
//        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalDateTime())));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalDate())));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalTime())));
        javaTimeModule.addDeserializer(Instant.class, new DynamicTimeZoneInstantDeserializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternInstant())));
        javaTimeModule.addDeserializer(OffsetDateTime.class, new DynamicTimeZoneOffsetDateTimeDeserializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternOffsetDateTime())));
        javaTimeModule.addDeserializer(YearMonth.class, new YearMonthDeserializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternYearMonth())));
        javaTimeModule.addDeserializer(MonthDay.class, new MonthDayDeserializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternMonthDay())));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(configBean.getFormatPatternLocalDateTime()));
        objectMapper.registerModule(javaTimeModule);

        // for converting BigDecimal to String and vice versa.
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(BigDecimal.class, new BigDecimalPlainSerializer());
        simpleModule.addDeserializer(BigDecimal.class, new BigDecimalPlainDeserializer());
        objectMapper.registerModule(simpleModule);

        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Swagger V3 requires ByteArrayHttpMessageConverter before MappingJackson2HttpMessageConverter
        converters.add(0, new ByteArrayHttpMessageConverter());
        converters.add(1, mappingJackson2HttpMessageConverter());
    }

    private class LocalDateTimeConverter implements Converter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(String source) {
            ZoneId zoneId = TimezoneUtils.zoneIdFromCurrentLocale();
            return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalDateTime()))
                    .atZone(zoneId)
                    .withZoneSameInstant(ZoneOffset.UTC)
                    .toLocalDateTime();
        }
    }

    private class LocalDateConverter implements Converter<String, LocalDate> {
        @Override
        public LocalDate convert(String source) {
//            ZoneId zoneId = TimezoneUtils.zoneIdFromCurrentLocale();
            return LocalDate.parse(source, DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalDate()));
        }
    }

    private class LocalTimeConverter implements Converter<String, LocalTime> {
        @Override
        public LocalTime convert(String source) {
//            ZoneId zoneId = TimezoneUtils.zoneIdFromCurrentLocale();
            return LocalTime.parse(source, DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalTime()));
        }
    }

    private class InstantConverter implements Converter<String, Instant> {
        @Override
        public Instant convert(String source) {
            ZoneId zoneId = TimezoneUtils.zoneIdFromCurrentLocale();
            return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(configBean.getFormatPatternInstant()))
                    .atZone(zoneId)
                    .toInstant();
        }
    }

    private class OffsetDateTimeConverter implements Converter<String, OffsetDateTime> {
        @Override
        public OffsetDateTime convert(String source) {
            ZoneId zoneId = TimezoneUtils.zoneIdFromCurrentLocale();
            return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(configBean.getFormatPatternInstant()))
                    .atZone(zoneId).toOffsetDateTime();
        }
    }

    private class YearMonthConverter implements Converter<String, YearMonth> {
        @Override
        public YearMonth convert(String source) {
            return YearMonth.parse(source.trim(),
                    DateTimeFormatter.ofPattern(configBean.getFormatPatternYearMonth()));
        }
    }

    private class MonthDayConverter implements Converter<String, MonthDay> {
        @Override
        public MonthDay convert(String source) {
            return MonthDay.parse(source.trim(),
                    DateTimeFormatter.ofPattern(configBean.getFormatPatternMonthDay()));
        }
    }

}
