= README =

In this directory, place the file named "google-service-account-secret.json" that will be used to access the Google spreadsheet used for shared tasks.
To obtain that file:
* Log-in to https://console.developers.google.com/
* Create a project
* Create a "service account"
* Download its key file in the JSON format. 

The contents of this file should look like this:
{
  "type": "service_account",
  "project_id": "<PROJECT ID>",
  "private_key_id": "<PRIVATE KEY ID>",
  "private_key": "<PRIVATE KEY>",
  "client_email": "<SERVICE ACCOUNT EMAIL>",
  "client_id": "<SERVICE ACCOUNT CLIENT ID>",
  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
  "token_uri": "https://oauth2.googleapis.com/token",
  "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
  "client_x509_cert_url": "<URL>"
}