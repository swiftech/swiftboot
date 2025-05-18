package org.swiftboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.swiftboot.web.config.SwiftBootWebConfigBean;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Config conversion at web tier.
 * @since 3.0
 */
@Configuration
public class SwiftBootWebConvertConfig implements WebMvcConfigurer {

    @Resource
    private SwiftBootWebConfigBean configBean;

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalDateTime())));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalDate())));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalTime())));
        javaTimeModule.addSerializer(YearMonth.class, new YearMonthSerializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternYearMonth())));
        javaTimeModule.addSerializer(MonthDay.class, new MonthDaySerializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternMonthDay())));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalDateTime())));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalDate())));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternLocalTime())));
        javaTimeModule.addDeserializer(YearMonth.class, new YearMonthDeserializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternYearMonth())));
        javaTimeModule.addDeserializer(MonthDay.class, new MonthDayDeserializer(DateTimeFormatter.ofPattern(configBean.getFormatPatternMonthDay())));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(configBean.getFormatPatternLocalDateTime()));
        objectMapper.registerModule(javaTimeModule);
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Swagger V3 requires ByteArrayHttpMessageConverter before MappingJackson2HttpMessageConverter
        converters.add(0, new ByteArrayHttpMessageConverter());
        converters.add(1, mappingJackson2HttpMessageConverter());
    }
}
