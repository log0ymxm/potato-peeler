potato-peeler
=============

Java JSF Web App that uses MySQL and what not

Run Local
=========

    mvn package
    java -jar target/dependency/webapp-runner.jar target/*.war --port 8081
    
Deployment to Heroku
====================

Installing Heroku

    # Install ruby & rubygems (DIY)...
    gem install heroku
    heroku login
    
Deploying

    git push heroku master
    
