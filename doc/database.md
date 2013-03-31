## Setting up the Database

NOTE: we probably don't need to use the root user. I just did that
because it was easy

    # Create a local database with user root & blank password
    mvn db-migration:create
    mvn db-migration:migrate
    
## Migrations

https://code.google.com/p/c5-db-migration/

New maven commands & stuff

    mvn db-migration:create    # Create a new, empty database
    mvn db-migration:migrate   # Apply all pending migrations
    mvn db-migration:reset     # Drop the existing database, create a new one, and apply all pending migrations
    mvn db-migration:new       # Create a new, empty migration script
    mvn db-migration:check     # Check for pending migrations, fail the build if the db is not up to date

## Useful queries

    -- Informative list of schools
    SELECT schools.name, schools.rmp_id, locations.name AS location_name, states.name AS state_name FROM schools 
    JOIN locations ON schools.location_id = locations.id
    JOIN states ON locations.state_id = states.id;
    
    -- Get db sizes
    SELECT table_schema "DB Name", 
    Round(Sum(data_length + index_length) / 1024 / 1024, 1) "DB Size in MB" 
    FROM information_schema.tables 
    GROUP BY table_schema; 

