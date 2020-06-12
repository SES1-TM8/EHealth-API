# EHealth API
This github repository contains the code the EHealth API written for the SES1A subject at the University of Technology Sydney. 

## Installation
To instal this project you must clone this repository. Once doing so you must then import into Eclipse Enterprise Edition as an "Existing Maven Project". Then you must run the project using the ```spring-boot:run``` goal. Additionally you must add environment parameters for Google Firestore and Amazon S3. These parameters are defined in the table below

Parameter Name | Content
-------------- | -------
awsAccessKeyProperty | This contains the access key for Amazon S3
awsSecretKeyProperty | This contains the secret key for Amazon S3
EHEALTH_DATABASE_PASSWORD | This is the password for the database (defaults to nothing)
EHEALTH_DATABASE_URI | This the url for the database (defaults to ```localhost:3306```)
googleApiSecretProperty | The JSON file content provided by Google Firebase (contains the secret key for Google Firestore)

Before running you must have an already running DB instance that contains a database named ```ehealth```.
