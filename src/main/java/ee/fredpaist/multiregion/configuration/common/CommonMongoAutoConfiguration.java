package ee.fredpaist.multiregion.configuration.common;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@AutoConfigureBefore(MongoAutoConfiguration.class)
@Configuration
@ConditionalOnProperty(prefix = "mongodb.multi", name = "common")
public class CommonMongoAutoConfiguration {

    @Bean(name = "commonMongoProperties")
    @ConfigurationProperties(prefix = "mongodb.multi.common")
    @Primary
    public MongoProperties primaryProperties() {
        return new MongoProperties();
    }

    @Primary
    @Bean
    public SimpleMongoClientDatabaseFactory primaryMongoClientDatabaseFactory(@Qualifier("commonMongoProperties") MongoProperties mongoProperties) {
        return new SimpleMongoClientDatabaseFactory(mongoProperties.getUri());
    }

    @Primary
    @Bean("commonMongoTemplate")
    public MongoTemplate commonMongoTemplate(MongoDatabaseFactory factory, MongoCustomConversions customConversions) {
        DbRefResolver primarydbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(primarydbRefResolver, new MongoMappingContext());
        mappingConverter.setCustomConversions(customConversions);
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        mappingConverter.afterPropertiesSet();
        return new MongoTemplate(factory, mappingConverter);
    }
}
