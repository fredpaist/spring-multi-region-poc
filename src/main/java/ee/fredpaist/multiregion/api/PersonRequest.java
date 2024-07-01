package ee.fredpaist.multiregion.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PersonRequest {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @Email
    private String email;

    private String phoneNumber;

}
