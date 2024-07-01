package ee.fredpaist.multiregion.configuration.regional;

import com.mongodb.client.MongoDatabase;
import ee.fredpaist.multiregion.configuration.RegionContext;
import ee.fredpaist.multiregion.exception.RegionException;
import lombok.NonNull;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.Optional;

public class MultiRegionalMongoFactory extends SimpleMongoClientDatabaseFactory {

    private final MultiRegionalMongoConfig multiRegionalMongoConfig;

    public MultiRegionalMongoFactory(final MultiRegionalMongoConfig multiRegionalMongoConfig, final MultiRegionalMongoConfig.RegionClient tenantMongoClient) {
        super(tenantMongoClient.mongoClient(), tenantMongoClient.database());
        this.multiRegionalMongoConfig = multiRegionalMongoConfig;
    }

    @Override
    public @NonNull MongoDatabase getMongoDatabase() throws DataAccessException {
        final String region = Optional.ofNullable(RegionContext.getRegionId())
                .filter(it -> !it.isEmpty())
                .orElseThrow(() -> new RegionException("No region found"));

        return Optional.ofNullable(multiRegionalMongoConfig.getMultiRegionConfig().get(region))
                .map(it -> it.mongoClient().getDatabase(it.database()))
                .orElseThrow(() -> new RegionException("Region %s is not configured".formatted(region)));
    }

    @Override
    public void destroy() {
        multiRegionalMongoConfig.getMultiRegionConfig().values().forEach(mongo -> mongo.mongoClient().close());
    }
}
