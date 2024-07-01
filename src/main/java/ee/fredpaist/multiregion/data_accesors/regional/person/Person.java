package ee.fredpaist.multiregion.data_accesors.regional.person;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Accessors(chain = true)
@Document(collection = "person")
public class Person {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
