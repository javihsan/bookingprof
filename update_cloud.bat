SET PATH=C:\GCloudSDK\google-cloud-sdk\bin;C:\openjdk_1.8.0\bin;%PATH%;

gcloud config set project dilosohairapp
mvn package appengine:deploy -P prod

gcloud beta app migrate-config datastore-indexes-xml-to-yaml src\main\webapp\WEB-INF\datastore-indexes.xml
mvn clean package appengine:deployIndex

gcloud app deploy .\target\dilosohairapp\WEB-INF\cron.yaml

Indexado?
Task
TaskClass
RepeatClient
ProductClass
Product