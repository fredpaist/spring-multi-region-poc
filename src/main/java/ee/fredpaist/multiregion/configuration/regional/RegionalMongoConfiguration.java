package ee.fredpaist.multiregion.configuration.regional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(
        basePackages = {"ee.fredpaist.multiregion.data_accesors.regional"},
        mongoTemplateRef = "regionalMongoTemplate")
@Configuration
@ConditionalOnProperty(value = "mongodb.multi.enabled", havingValue = "true")
public class RegionalMongoConfiguration {
}
