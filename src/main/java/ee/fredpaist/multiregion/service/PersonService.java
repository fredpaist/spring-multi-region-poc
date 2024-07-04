package ee.fredpaist.multiregion.service;

import ee.fredpaist.multiregion.api.PersonRequest;
import ee.fredpaist.multiregion.data_accesors.common.system.ConfigurationType;
import ee.fredpaist.multiregion.data_accesors.regional.person.Person;
import ee.fredpaist.multiregion.data_accesors.regional.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final SystemConfigurationProvider systemConfigurationProvider;

    public Person createPerson(PersonRequest request) {
        validateRequest(request);

        var person = new Person()
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .setEmail(request.getEmail())
                .setPhoneNumber(request.getPhoneNumber());

        return personRepository.save(person);
    }

    public Person getPersonById(String id) {
        return personRepository.findById(id).orElse(null);
    }

    public Person updatePerson(String id, PersonRequest updatedPerson) {
        validateRequest(updatedPerson);

        return Optional.ofNullable(getPersonById(id))
                .map(person -> person
                        .setFirstName(updatedPerson.getFirstName())
                        .setLastName(updatedPerson.getLastName())
                        .setEmail(updatedPerson.getEmail())
                        .setPhoneNumber(updatedPerson.getPhoneNumber()))
                .map(personRepository::save)
                .orElseThrow(() -> new RuntimeException("Missing person with id %s".formatted(id)));
    }

    public void deletePerson(String id) {
        personRepository.deleteById(id);
    }

    private void validateRequest(PersonRequest request) {
        if (systemConfigurationProvider.getConfigurationByType(ConfigurationType.PERSON_EMAIL_MANDATORY).getAsBoolean() && Objects.isNull(request.getEmail())) {
            throw new IllegalArgumentException("Email is required");
        }

        if (systemConfigurationProvider.getConfigurationByType(ConfigurationType.PERSON_PHONE_MANDATORY).getAsBoolean() && Objects.isNull(request.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number is required");
        }
    }
}
