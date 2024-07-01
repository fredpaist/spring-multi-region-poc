package ee.fredpaist.multiregion.data_accesors.regional.person;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, String> {
}
