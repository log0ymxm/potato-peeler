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
    
    -- Ratings with useful info
    SELECT schools.name, teachers.last_name, teachers.first_name, teacher_ratings.rmp_id, 
      teacher_ratings.easiness, teacher_ratings.helpfulness, teacher_ratings.clarity, teacher_ratings.rater_interest, 
      teacher_ratings.comment, teacher_ratings.date, classes.level, departments.name
    FROM teacher_ratings
      JOIN teachers ON teacher_ratings.teacher_id = teachers.id
      JOIN schools ON teachers.school_id = schools.id
      JOIN classes ON teacher_ratings.class_id = classes.id
      JOIN departments ON classes.department_id = departments.id
    WHERE
      (
        schools.name like "California State Polytechnic University"
        OR 
        schools.name like "California Polytechnic State University"
      )
    ORDER BY teachers.last_name

    -- The absurd join
    SELECT teacher_ratings.comment, schools.name, teachers.last_name, teachers.first_name, teacher_ratings.rmp_id, 
      teacher_ratings.easiness, teacher_ratings.helpfulness, teacher_ratings.clarity, teacher_ratings.rater_interest, 
      teacher_ratings.date, classes.level, departments.name, 
      school_ratings.date AS school_rating_date, school_ratings.school_reputation,
      school_ratings.career_opportunities, school_ratings.campus_grounds,
      school_ratings.quality_of_food, school_ratings.social_activities, 
      school_ratings.campus_location, school_ratings.condition_of_library,
      school_ratings.internet_speed, school_ratings.clubs_and_events,
      school_ratings.comment AS school_rating_comment, school_ratings.school_happiness
    FROM teacher_ratings
      JOIN teachers ON teacher_ratings.teacher_id = teachers.id
      JOIN schools ON teachers.school_id = schools.id
      JOIN classes ON teacher_ratings.class_id = classes.id
      JOIN departments ON classes.department_id = departments.id
      JOIN school_ratings ON schools.id = school_ratings.school_id

    -- People who state what grade they got, could be used to manually
    -- build a training set. Also accounting for bad grammar :D
    SELECT inferred_grade, comment, classes.level, easiness, helpfulness, clarity, rater_interest, date
    FROM teacher_ratings
    JOIN classes ON classes.id = teacher_ratings.class_id
    WHERE
    (comment like "%i got an A%"
    OR comment like "%i got a A%"
    OR comment like "%i got a B%"
    OR comment like "%i got a C%"
    OR comment like "%i got a D%"
    OR comment like "%i got an F%"
    OR comment like "%i got a F%")
    AND inferred_grade IS NULL

    
