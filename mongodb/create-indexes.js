const db = db.getSiblingDB("test_db");
const collections = db.getCollectionNames();
const collectionExists = (name) =>  collections.includes(name);

// person
if (collectionExists("person")) {
    db.person.createIndex( {
        "firstName": 1,
        "lastName": 1
    }, { name: "person_fullname_index" } )

    db.person.createIndex( {
        "email": 1,
    }, { name: "person_email_index" } )
}
