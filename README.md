# smartgates

###### A play framework 1.x application. The app makes it easier to process requests to visit closed institutions. Users submit their requests to visit the place. Administrators decide whether to invite the user or not. Users get email notifications about the decision.
###### In order to submit the request go to "/"
###### In order to see the admin panel go to "/administration"

### To run
``sh
play start --%prod
``
###### Postgresql database "smartgates" has to be created before running the app
###### play is a Java framework, needs to be downloaded (version 1.x)
###### Folder where generated QRcodes are going to be stored "/opt/smartgates" should exist and the user should have permissions to write and read in that directory

### Todo:
- The external mobile application that verifies QRcodes needs to be developed
- Make an API for QR code verification
