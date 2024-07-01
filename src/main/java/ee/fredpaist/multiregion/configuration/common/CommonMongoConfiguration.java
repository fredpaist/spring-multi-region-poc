package ee.fredpaist.multiregion.configuration.common;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(
        basePackages = {"ee.fredpaist.multiregion.data_accesors.common"},
        mongoTemplateRef = "commonMongoTemplate")
@Configuration
@ConditionalOnProperty(prefix = "mongodb.multi", name = "common")
public class CommonMongoConfiguration {
}
