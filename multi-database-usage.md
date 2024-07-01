## Usagedata Multidatabase usage with MongoDB
### Overview
This Client (configuration) demonstrates the usage of multiple databases separated by country.
The idea draws inspiration from the multi-tenancy architecture, where each tenant (in this case, a country) has its own isolated database.
Additionally, there's a mix where certain entities are shared and stored in a common database accessible to all countries.

### Motivation
The motivation behind this stems from the need to manage data for different countries efficiently while still maintaining isolation and scalability.
By segregating data based on country, we ensure data privacy and compliance with country specific business needs.
Furthermore, having a common database for shared entities optimizes data access and reduces redundancy.


### Features
- Multi-Database Support: Each country has its own database instance, ensuring data isolation and compliance.
- Common Database for Shared Entities: Certain entities shared among countries are stored in a common database, facilitating centralized management and reducing redundancy.
- Scalability: The architecture allows to add new databases for additional countries without affecting existing ones.


### Usage
#### yml configuration
If multi database is not needed, just disable it and can use the default mongodb uri properties
```properties
spring.data.mongodb.uri=${DATABASE_URI}
```

To use multi-database setup you need to enable it by setting configuration
```YML
mongodb:
  multi:
    enabled: true
```
Provide common database properties, If common is not needed, leave common properties empty, then this will be disabled.
```YML
mongodb:
  multi:
    common:
      uri: <common-database-uri>
```

Add country based mongodb properties with ease
```YML
mongodb:
  multi:
    regions:
      - region: <country-key>
        properties:
          uri: <country-database-uri>
          database: <database-name>
```

#### Code configuration
We need to define which entities/repositories are for regional usage and which are shared.
For that need to create 2 Spring `@Configuration` files

1. Register common entities/repositories
```Java
@EnableMongoRepositories(
        basePackages = {"com.example"}, // packages where are common entities/repositories located
        mongoTemplateRef = "commonMongoTemplate") // This refers to a common mongo template which is used to access common DB
@Configuration
@ConditionalOnProperty(value = "mongodb.multi.enabled", havingValue = "true") // if you disable the multi setup, your application would still work.
public class CommonMongoConfiguration {}
```
2. Register regionalized entities/repositories
```Java
@EnableMongoRepositories(
        basePackages = {"com.example"}, // packages where are common entities/repositories located
        mongoTemplateRef = "regionalMongoTemplate") // This refers to a regional mongo template which is used to access country specific DB
@Configuration
@ConditionalOnProperty(value = "mongodb.multi.enabled", havingValue = "true") // if you disable the multi setup, your application would still work.
public class RegionalMongoConfiguration {}
```
3. Register mongo custom converters
```Java
@Bean
public MongoCustomConversions mongoCustomConversions() {
    List<Converter<?, ?>> converterList = new ArrayList<>();
    converterList.add(new CustomConverter.toValueConverter());
    converterList.add(new CustomConverter.fromValueConverter());
    return new MongoCustomConversions(converterList);
}
```

#### Using correct database
- Common database: Inject MongoTemplate with the bean name `commonMongoTemplate`
- Country database: Inject MongoTemplate with the bean name `regionalMongoTemplate`

example
```Java
public class SomeComponent{
    
    public SomeComponent(@Qualifier("commonMongoTemplate") MongoTemplate dataSource) {
        // now you can use common datasource
    }
}
```
PS! already registered repositories use correct datasource/template - this is just an example if want to use raw mongoTemplate.

#### Switching regions
To switch between regions and get data from correct DB, we need to set the region before accessing data.
`RegionContext` is holding the key for which country/region is currently selected. This key is for local Thread so every new request/thread can work on its own region.
To switch regions/countries just need to set 
```Java
RegionContext.setRegionId("LV");
```
where the key is equal to the configured region key in the properties


## Open Questions
1. what is the best way to set regions:
   2. set it with the incoming request/started process
   3. by repository query (this needs some spring boot Aspect version to wrap those)
2. What about the changelog, supporting current changelog system is not so easy, should we convert it to js script like we have for indexes? 