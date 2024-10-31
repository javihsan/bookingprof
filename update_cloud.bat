SET PATH=C:\GCloudSDK\google-cloud-sdk\bin;C:\openjdk_1.8.0\bin;%PATH%;
gcloud config set project dilosohairapp
mvn clean package -P prod
gcloud app deploy .\target\dilosohairapp\WEB-INF\appengine-web.xml --version=r11-0-0 --no-stop-previous-version --no-promote
gcloud app deploy .\target\dilosohairapp\WEB-INF\cron.yaml

new java17:
gcloud config set project dilosohairapp
mvn package appengine:deploy -P prod

