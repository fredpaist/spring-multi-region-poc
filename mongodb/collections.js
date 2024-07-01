const authorizationDetails = async (db) => {
    await db.createCollection("authorizationDetails", {
        validator: {
            $jsonSchema: {
                bsonType: "object",
                required: ["username", "department", "role"],
                properties: {
                    id: { bsonType: "string" },
                    username: { bsonType: "string" },
                    department: { bsonType: "string" },
                    role: { bsonType: "string" }
                }
            }
        }
    });

    await db.authorizationDetails.insertMany([
        {
            role: "ADMIN",
            username: "ingestion_system",
            department: "usagedata_system"
        },
        {
            role: "ADMIN",
            username: "usagedata_admin",
            department: "usagedata_system"
        }
    ])
}

const createPerson = async (db) => {
    await db.createCollection("person", {
        validator: {
            $jsonSchema: {
                bsonType: "object",
                required: ["firstName", "lastName"],
                properties: {
                    id: { bsonType: "string" },
                    firstName: { bsonType: "string" },
                    lastName: { bsonType: "string" },
                    email: { bsonType: "string" },
                    phoneNumber: { bsonType: "string" }
                }
            }
        }
    });
}


const systemClassifier = async (db) => {
    await db.createCollection("systemClassifier", {
        validator: {
            $jsonSchema: {
                bsonType: "object",
                required: ["classification", "value"],
                properties: {
                    id: { bsonType: "string" },
                    type: { bsonType: "string", description: "configuration type." },
                    value: {
                        bsonType: ["string", "object", "boolean", "null"]
                    }
                }
            }
        }
    });
}

module.exports = {
    authorizationDetails,
    person: createPerson,
    systemClassifier
};
