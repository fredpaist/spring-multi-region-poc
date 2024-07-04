package ee.fredpaist.multiregion.data_accesors.common.system;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

public enum ConfigurationType {
    PERSON_EMAIL_MANDATORY,
    PERSON_PHONE_MANDATORY;

    @ReadingConverter
    public static class FromValueConverter implements Converter<String, ConfigurationType> {
        public ConfigurationType convert(String source) {
            return ConfigurationType.valueOf(source);
        }
    }

    @WritingConverter
    public static class ToValueConverter implements Converter<ConfigurationType, String> {
        @Override
        public String convert(ConfigurationType source) {
            return source.name();
        }
    }

}
