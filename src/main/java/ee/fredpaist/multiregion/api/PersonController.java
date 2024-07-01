package ee.fredpaist.multiregion.api;

import ee.fredpaist.multiregion.data_accesors.regional.person.Person;
import ee.fredpaist.multiregion.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("person")
public class PersonController {

    private final PersonService personService;

    @PostMapping
    public ResponseEntity<Person> createPerson(@Valid @RequestBody PersonRequest request) {
        Person createdPerson = personService.createPerson(request);
        return ResponseEntity.ok(createdPerson);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable String id) {
        Person person = personService.getPersonById(id);
        return ResponseEntity.ok(person);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable String id, @Valid @RequestBody PersonRequest request) {
        Person updatedPerson = personService.updatePerson(id, request);
        return ResponseEntity.ok(updatedPerson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable String id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

}
