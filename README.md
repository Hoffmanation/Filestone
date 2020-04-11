# Readme file
development version of paygilant application
## Environment
 - Ubuntu 18.04
 
## Requirements
- nodejs
- docker
- for development only - locally installed postgres client - `sudo apt install postgresql-client-common`

## Directories
- _docker - running docker with command line
    - db/postgress - folder that holds docker mapped database
- client - vuejs client application
- server - nodejs/express server application
    - controllers - modules for each route controller
    - db - module for connection to postgress
    - helpers - helper modules
    - middleware - modules for route middleware
    - migrations - used to make db structure
    - models - modules&function to get data from postgres tables
    - router - router/routes module definitions
    - seeds - seeding demo data, users session risk_events

## JSON Configuration File / jsonFile.json in server root folder
 - If there is property in fields it will be handled, else ignored. So if there is no configuration everything will be displayed as is.
 - Administrators see all fields
 - Empty objects are being deleted, unless handled in jsonFile.json
 - Example:
 1. Response from database:
     ```
     {
         score:123
         amount:456
         paymentMethod:'asdf'
         address:{}
     }
  2. Handling with json:
      ```
     {
     "fields": {
         "score": {
           "label": "score renamed",
           "analyst": true, //show to analyst
           "developer": false //hide to developer
         },
         "amount": {
             "label": "Amount",
             "analyst": false,
             "developer": true
           }
     }
  3. Response for analyst will be:
     ```
     {
         "score renamed":"123"
         "paymentMethod":"asdf"
     }
     
  

## Modify Database query
 - <a href="http://knexjs.org/">Knex</a> library is responsible for communication with database. 
 - Folder /server/models is responsible for querying database with functions.
Example: `knex(table).select(...)`
 - Getting all risk events with `/server/models/riskEvents.js` method `getRiskEvents`.
Select `knex(table).select('severity', 'score', 'timestamp', 'sessionid', 'requestid', 'userid', 'checkpoint', 'amount', 'currency', 'destination', 'status');`
We are creating query with knex queryBuilder. For example take a look at jsdoc for `getRiskEvents`
 - Getting single risk event by requestId `/server/models/riskEvents.js` method `getRiskEventsByRequestId`, select `knex.select('json').table(table)`

## Running up Environment without docker
- server
    - `cd server`
    - `npm run dev` or
    - `npm run start`
- client
    - `cd client`
    - `npm run serve` or 
    - `npm run build` 

#### .env file example
    #postgres db settings
    DATABASE_HOST=127.0.0.1
    DATABASE_USER=postgres
    DATABASE_PASSWORD=postgres
    DATABASE_NAME=postgres
    DATABASE_SEARCH_PATH=public
    #env development | production
    NODE_ENV=development
    #app port
    PORT=3000
    #used for generating jwt token
    SECRET=s0m3su3rs3cr3tp4ssw0rd
    #JWT token expiry, can be 7d, 60 or whatever in seconds
    TOKEN_EXPIRES_IN=1d
    #used for cors policy
    FRONT_APP_DOMAIN=http://localhost:8080
    #root of the server
    JSON_CONFIGURATION_FILE_PATH='../jsonFile.json'
    
    ####EMAIL CONFIGURATION
    EMAIL_FROM='someEmail@gmail.com'
    # Gmail SMTP server Configuration
    GMAIL_SERVICE_NAME=gmail
    GMAIL_SERVICE_HOST=smtp.google.com
    GMAIL_SERVICE_SECURE=false
    GMAIL_SERVICE_PORT=587
    GMAIL_USER_NAME=someEmail@gmail.com
    GMAIL_USER_PASSWORD=someEmailGmailPassword
    
    # SMTP service configuration
    SMTP_SERVICE_HOST=
    SMTP_SERVICE_SECURE=
    SMTP_SERVICE_PORT=
    SMTP_USER_NAME=
    SMTP_USER_PASSWORD=
    
### Docker Environment
We can run application via two scripts for development purposes.
- in _docker folder ./start.sh - starts docker without docker-compose, configuration is in command line parameters
- in root of the project ./start.sh - starts docker containers with docker-compose, configuration is in docker-compose.yaml file in root of the project.
- there is Dockerfile in server/ and client/ dir that is used with docker-compose process


### Some useful commands for development speedup
#### postgres commands
    \q | Exit psql connection
    \c | Connect to a new database
    \dt | List all tables
    \du | List all roles
    \list | List databases
    \d table_name | describes table_name
##### running commands into docker container
    docker exec -it docker-container-name some-command-line
##### example: connecting to `pg-db-server` docker instance 1
    docker exec -it pg-db-server psql -h localhost -U postgres -d postgres
##### example2: connecting to docker container as root user:
    docker exec -u root -t -i pg-node-server /bin/bash
##### example3: running up docker with file docker-compose.yaml, building it, recreating it, and getting docker up 
    docker-compose -f docker-compose.yaml up -d --force-recreate --build
##### example4: listing docker containers:
    docker ps
