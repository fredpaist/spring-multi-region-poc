package ee.fredpaist.multiregion.service;

import ee.fredpaist.multiregion.api.PersonRequest;
import ee.fredpaist.multiregion.data_accesors.regional.person.Person;
import ee.fredpaist.multiregion.data_accesors.regional.person.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    private PersonService personService;

    @BeforeEach
    public void setUp() {
        personService = new PersonService(personRepository);
    }

    @Test
    void test_create_person_with_valid_data() {
        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);

        PersonRequest request = new PersonRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setPhoneNumber("+1234567890");

        when(personRepository.save(personArgumentCaptor.capture())).thenReturn(new Person());

        personService.createPerson(request);

        var person = personArgumentCaptor.getValue();
        assertNotNull(person);
        assertEquals("John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals("john.doe@example.com", person.getEmail());
        assertEquals("+1234567890", person.getPhoneNumber());
    }

    @Test
    void test_create_person_with_mandatory_fields() {
        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);

        PersonRequest request = new PersonRequest();
        request.setFirstName("Jane");
        request.setLastName("Doe");

        when(personRepository.save(personArgumentCaptor.capture())).thenReturn(new Person());

        personService.createPerson(request);

        var person = personArgumentCaptor.getValue();
        assertNotNull(person);
        assertEquals("Jane", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertNull(person.getEmail());
        assertNull(person.getPhoneNumber());
    }

    @Test
    void test_create_person_with_null_optional_fields() {
        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);

        PersonRequest request = new PersonRequest();
        request.setFirstName("Alice");
        request.setLastName("Smith");
        request.setEmail(null);
        request.setPhoneNumber(null);

        when(personRepository.save(personArgumentCaptor.capture())).thenReturn(new Person());

        personService.createPerson(request);

        var createdPerson = personArgumentCaptor.getValue();
        assertNotNull(createdPerson);
        assertEquals("Alice", createdPerson.getFirstName());
        assertEquals("Smith", createdPerson.getLastName());
        assertNull(createdPerson.getEmail());
        assertNull(createdPerson.getPhoneNumber());
    }

    @Test
    void test_returns_person_object_when_valid_id_provided() {
        String validId = "123";
        Person expectedPerson = new Person().setId(validId).setFirstName("John").setLastName("Doe").setEmail("john.doe@example.com").setPhoneNumber("1234567890");
        when(personRepository.findById(validId)).thenReturn(Optional.of(expectedPerson));
        Person actualPerson = personService.getPersonById(validId);
        assertNotNull(actualPerson);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    void test_handles_non_existent_id_gracefully_by_returning_null() {
        String nonExistentId = "non-existent-id";
        when(personRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        Person actualPerson = personService.getPersonById(nonExistentId);
        assertNull(actualPerson);
    }

    @Test
    void test_update_person_successfully() {
        String id = "123";
        Person existingPerson = new Person().setId(id).setFirstName("John").setLastName("Doe").setEmail("john.doe@example.com").setPhoneNumber("1234567890");
        PersonRequest updatedRequest = new PersonRequest().setFirstName("Jane").setLastName("Smith").setEmail("jane.smith@example.com").setPhoneNumber("0987654321");

        when(personRepository.findById(id)).thenReturn(Optional.of(existingPerson));
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Person updatedPerson = personService.updatePerson(id, updatedRequest);

        assertNotNull(updatedPerson);
        assertEquals("Jane", updatedPerson.getFirstName());
        assertEquals("Smith", updatedPerson.getLastName());
        assertEquals("jane.smith@example.com", updatedPerson.getEmail());
        assertEquals("0987654321", updatedPerson.getPhoneNumber());
    }

    @Test
    void test_update_nonexistent_person() {
        String id = "123";
        PersonRequest updatedRequest = new PersonRequest().setFirstName("Jane").setLastName("Smith").setEmail("jane.smith@example.com").setPhoneNumber("0987654321");

        when(personRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            personService.updatePerson(id, updatedRequest);
        });

        assertEquals("Missing person with id 123", exception.getMessage());
    }

    @Test
    void test_delete_existing_person_by_valid_id() {
        String validId = "123";

        doNothing().when(personRepository).deleteById(validId);

        personService.deletePerson(validId);

        verify(personRepository, times(1)).deleteById(validId);
    }
}