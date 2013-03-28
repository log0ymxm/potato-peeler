potato-peeler
=============

Java JSF Web App that uses MySQL and what not

Run Local
=========

Heroku uses the procfile to run our app, we might as well use the same
tool. Install `foreman` with ruby gems & this should be easy.

Setup

    gem install foreman

Running

    # build the app
    mvn package
    
    # start a web process
    foreman start web
    
    # or manually with
    java -jar target/dependency/webapp-runner.jar target/*.war --port 8081

    
Deployment to Heroku
====================

Installing Heroku

    # Install ruby & rubygems (DIY)...
    gem install heroku
    heroku login
    
Deploying

    git push heroku master
    
