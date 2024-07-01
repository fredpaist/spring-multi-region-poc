package ee.fredpaist.multiregion.configuration.regional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.ArrayList;
import java.util.List;

@AutoConfigureBefore(MongoAutoConfiguration.class)
@Configuration
@ConditionalOnProperty(value = "mongodb.multi.enabled", havingValue = "true")
public class RegionMongoAutoConfiguration {

    @Bean("regionDatabaseConfig")
    @ConditionalOnMissingBean
    public MultiRegionalMongoConfig multiRegionalMongoConfig(MultiRegionalMongoProperties multiTenantMongoProperties) {
        return new MultiRegionalMongoConfig(multiTenantMongoProperties);
    }

    @Bean("regionDatabaseFactory")
    @ConditionalOnMissingBean
    public MultiRegionalMongoFactory mongoDatabaseFactory(@Qualifier("regionDatabaseConfig") MultiRegionalMongoConfig multiRegionalMongoConfig) {
        final MultiRegionalMongoConfig.RegionClient regionClient = multiRegionalMongoConfig.getMultiRegionConfig().firstEntry().getValue();
        return new MultiRegionalMongoFactory(multiRegionalMongoConfig, regionClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<>();
        return new MongoCustomConversions(converterList);
    }

    @Bean("regionalMongoTemplate")
    public MongoTemplate regionalMongoTemplate(@Qualifier("regionDatabaseFactory") MultiRegionalMongoFactory mongoDatabaseFactory, MongoCustomConversions customConversions) {
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDatabaseFactory), new MongoMappingContext());
        converter.setCustomConversions(customConversions);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        converter.afterPropertiesSet();
        return new MongoTemplate(mongoDatabaseFactory, converter);
    }
}
