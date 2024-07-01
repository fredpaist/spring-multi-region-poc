# MongoDB Schema and Indexes script README

## Overview

This README provides instructions on how to use the MongoDB shell script files to setup schema and indexes with the `mongosh` command-line interface.

## Prerequisites

Before using the MongoDB shell script, ensure that you have the following prerequisites installed:

- **MongoDB Shell (`mongosh`):** Make sure you have `mongosh` installed on your machine. You can download it from the official [MongoDB Download Center](https://www.mongodb.com/try/download/shell).

## Usage

Available operations:
* Create schema and indexes
* Create indexes only

### Create Schema

Follow these steps to create collection and indexes with the script

```bash
mongosh mongodb://localhost:27018/test_db --file build-schema.js --eval 'var region="LT"'
```
* Connection String: replace `mongodb://localhost:27018/usagedata` with the correct connection string to wanted database
* Country selection: `--eval 'var region="LT"'` - replace country key with the correct one - currently supported `COMMON, REGION_1, REGION_2`


Follow these steps to create indexes only
```bash
mongosh mongodb://localhost:27018/test_db --file create-indexes.js
```
* Connection String: replace `mongodb://localhost:27018/usagedata` with the correct connection string to wanted database