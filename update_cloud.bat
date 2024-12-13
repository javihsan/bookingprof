SET PATH=C:\Users\fjsanchez\AppData\Local\Google\Cloud SDK\google-cloud-sdk\bin;%PATH%;

gcloud config set project dilosohairapp
mvn package appengine:deploy -P prod

gcloud beta app migrate-config datastore-indexes-xml-to-yaml src\main\webapp\WEB-INF\datastore-indexes.xml
mvn clean package appengine:deployIndex

gcloud app deploy .\target\dilosohairapp\WEB-INF\cron.yaml

Si falla el índice en el deploy a Pro, lanzar primero el deployIndex 

