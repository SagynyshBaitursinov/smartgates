# smartgates
####### A play framework 1.x application. The app makes it easier to process requests to visit closed institutions. Users submit their requests to visit the place. Administrators decide whether to invite the user or not. Users get email notifications about the decision.
####### In order to submit the request go to "/"
####### In order to see the admin panel go to "/administration/login"

### To run
``sh
play start --%prod
``
###### Postgresql database "smartgates" has to be created before running the app

### Todo:
- QR code has to be sent to user, if his/her request is accepted. That QR code has to be verified by security guard.
- Make an API and external application for QR code verification
