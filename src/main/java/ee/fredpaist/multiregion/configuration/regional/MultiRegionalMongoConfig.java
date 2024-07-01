package ee.fredpaist.multiregion.configuration.regional;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import ee.fredpaist.multiregion.exception.RegionException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.TreeMap;

@RequiredArgsConstructor
public class MultiRegionalMongoConfig {

    public record RegionClient(MongoClient mongoClient, String database) {}

    @Getter
    private TreeMap<String, RegionClient> multiRegionConfig;
    private final MultiRegionalMongoProperties multiRegionMongoProperties;

    @PostConstruct
    public void multiTenantMongoConfig() {
        final List<MultiRegionalMongoProperties.RegionProperties> multiTenantList = multiRegionMongoProperties.getRegions();
        multiRegionConfig = new TreeMap<>();

        for (final MultiRegionalMongoProperties.RegionProperties multiTenant : multiTenantList) {
            final String connectionUri = multiTenant.getProperties().getUri();
            final String host = multiTenant.getProperties().getHost();
            final Integer port = multiTenant.getProperties().getPort();
            MongoClient client;

            if (connectionUri != null) {
                client = MongoClients.create(connectionUri);
            } else if (host != null && port != null) {
                final String connection = "mongodb://" + host + ":" + port + "/";
                client = MongoClients.create(connection);
            } else {
                throw new RegionException("Could not setup region database %s. At-least one of the config properties is required [uri | host & port]".formatted(multiTenant.getRegion()));
            }
            final String database = multiTenant.getProperties().getDatabase();
            final RegionClient regionClient = new RegionClient(client, database);
            this.multiRegionConfig.put(multiTenant.getRegion(), regionClient);
        }
    }

    @PreDestroy
    public void destroy() {
        multiRegionConfig.values().forEach(mongo -> mongo.mongoClient().close());
    }
}
