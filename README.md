# Camunda-ext

Repo with helper classes for BPMN development.
Included into [Latera Camunda docker image](https://hub.docker.com/r/latera/camunda).

## Download

See [Releases](https://github.com/latera/camunda-ext/releases) for .jar files and [Dockerhub](https://hub.docker.com/r/latera/camunda) for docker images.

## Documentation

See [Docs page](https://latera.github.io/camunda-ext/)

## Test Cases

See [Test reports page](ttps://latera.github.io/camunda-ext/test-reports)

## How to build

### Install [SDKman](https://sdkman.io/install)

```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

### Install Java 8+, Groovy and Maven

```bash
sdk list java
sdk install java 8.0.232.hs-adpt #latest Java 8 version from previous command

sdk list groovy
sdk install groovy 2.4.16

sdk list maven
sdk install maven 3.6.0
```

### Clone this repo

```bash
git clone https://github.com/latera/camunda-ext.git
cd camunda-ext
```

### Build

```bash
./build.sh
```

See target/*.jar files

### Test

```bash
./test.sh
```

## How to run

```bash
cp .env.sample .env
docker-compose up -d
```

Then open http://localhost:8080/camunda with `user:changeme` credentials.

## How to build with SSO

Do the same steps like without SSO to install build tools (sdkman, java, groovy, maven).

After that:
```bash
mvn clean install -f ./pom-sso.xml
mvn dependency:copy-dependencies -Dcamunda-ext-1.6.version=1.6 -f ./sso/libs/third-party-libs/pom.xm
mvn dependency:copy-dependencies -Dcamunda-ext-1.6.version=1.6 -f ./sso/libs/webapp-libs/pom.xml
mvn dependency:copy-dependencies -Dcamunda-ext-1.6.version=1.6 -f ./sso/libs/engine-rest-libs/pom.xml

docker build -f Dockerfile.new_camunda_sso -t new_camunda_sso .
```

Keycloak must be configured according to the tutorial: https://github.com/camunda-community-hub/camunda-platform-7-keycloak#prerequisites-in-your-keycloak-realm

In `sso/conf/bpm-platform.xml` set actual settings in KeycloakIdentityProviderPlugin properties (keycloakIssuerUrl, keycloakAdminUrl, clientId, clientSecret).
