PROJECT_NAME=stats-api
VERSION = 1.0

.PHONY: run-app
run-app: build
	docker run -d -p 6000:8080 en-two-six/$(PROJECT_NAME):latest

.PHONY: build
build: install
	docker build -f Dockerfile -t en-two-six/$(PROJECT_NAME):latest --build-arg JAR_FILE=target/$(PROJECT_NAME)-$(VERSION).jar .

package:
	mvn clean package

install:
	mvn clean install

