package ee.fredpaist.multiregion.configuration.regional;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mongodb.multi")
public class MultiRegionalMongoProperties {

    private boolean enabled;
    private List<RegionProperties> regions;
    private MongoProperties common;

    @Getter
    @Setter
    public static class RegionProperties {
        private String region;
        private MongoProperties properties;
    }
}
