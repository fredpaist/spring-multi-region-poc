package ee.fredpaist.multiregion.data_accesors.common.system;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SystemConfigurationRepository extends MongoRepository<SystemConfiguration, String> {
}
