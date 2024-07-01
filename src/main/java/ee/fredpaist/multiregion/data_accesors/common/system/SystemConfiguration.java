package ee.fredpaist.multiregion.data_accesors.common.system;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Accessors(chain = true)
@Document(collection = "systemConfiguration")
public class SystemConfiguration {

    @Id
    private String id;
    private String name;
    private String version;
    private String value;
}
