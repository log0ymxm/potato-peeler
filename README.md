potato-peeler
=============

Java JSF Web App that uses MySQL and what not

Run Local
=========

    mvn package
    PORT=9900 java -cp target/classes:"target/dependency/*" HelloWorld
    
Deployment to Heroku
====================

Installing Heroku

    # Install ruby & rubygems (DIY)...
    gem install heroku
    heroku login
    
Deploying

    git push heroku master
    
