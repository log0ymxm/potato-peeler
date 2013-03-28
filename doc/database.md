## Migrations

Any project that involves more than a commit or two needs migrations. 

- http://stackoverflow.com/questions/131020/migrations-for-java

Never tried this in java, I'm guessing the easiest to use version wins
for this project.

## Tables

States: 50 of these things
  - id
  - abbreviation
  
Location: Scraped from school data
  - id
  - name
  - state_id

Schools: Initially populated from scraped data (probably don't need to
  try more than SLCC to start with)
  - id
  - rmp_id: RateMyProfessors ID
  - location_id (belongs to)
  
Department: Initially populated from scraped data
  - id
  - name (Chemistry, Biology, Art, Languages, Etc.)

Teachers: Initially populated from scraped data
  - id
  - rmp_id: RateMyProfessors ID
  - schools (has many, maybe belongs to)
  - first_name
  - last_name
  - department (belongs to)
  - ratings (has many)
  - hot (aggregated)
  - overall_quality (aggregated)
  - helpfulness (aggregated)
  - clarity (aggregated)
  - easiness (aggregated)
  
School_Ratings: School comments
  - id
  - rmp_id
  - school_id
  - date
  - reputation
  - opportunities
  - campus
  - food
  - social_activities
  - location
  - library
  - internet_speed
  - clubs_and_events
  - comment
  
Teacher_Ratings: Initially populated from scraped data
  - id
  - rmp_id: RateMyProfessors ID
  - easiness
  - helpfulness
  - clarity
  - rater_interest
  - comment
  - class
  - date
  
Teacher_Feedback: Scraped from RMP, Professor feedback, if it exists...
  
Class: Populated from normalized scraped ratings
  - department_id
  - level: (1100, 3150, 2550, etc)
  
Transcript: User inputted record of classes & grades, this is used to
            provide training data and predict new values
  - user_id (belongs to)  
  - is_predicted
  - date

TranscriptRecord: Represents an individual class grade in a transcript
  - transcript_id (belongs to)
  - grade
  - teacher_id (has one)
  - comment

User
  - transcripts (has many)
  - email || fb || twitter: Some unique method of login or authentication
  - password_hash (only needed if we use email auth)
  - salt (only needed if we use email auth)
