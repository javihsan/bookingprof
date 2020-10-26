SET PATH=C:\GCloudSDK\google-cloud-sdk\bin;C:\openjdk_1.8.0\bin;%PATH%;
mvn clean package -P prod
gcloud config set project dilosohairapp
gcloud app deploy .\target\dilosohairapp\WEB-INF\appengine-web.xml --version=r8-0-7 --no-stop-previous-version --no-promote

