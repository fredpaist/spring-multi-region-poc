const collections = require('./collections');
const supportedRegions = ['COMMON', 'REGION_1', 'REGION_2']
console.log("Setting up schema for " + region)

if (region.length == 0 || !supportedRegions.includes(region)) {
    throw new Error('No Region or unsupported Region selected');
}

const db = db.getSiblingDB("test_db");

const run = async () => {
    if (region === "COMMON") {
        await collections.authorizationDetails(db)
        await collections.systemClassifier(db)
    }

    if (region === "REGION_1") {
        await collections.person(db)
    }

    if (region === "REGION_2") {
        await collections.person(db)
    }
}

run();