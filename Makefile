clean:
	./gradlew clean

build:
	./gradlew clean build

install:
	./gradlew clean install

start:
	./gradlew bootRun --args='--spring.profiles.active=development'

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=production'

run-dist:
	./build/install/app/bin/app

start-dist:
	./build/install/app/bin/app

run:
	./gradlew bootRun

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

lint:
	./gradlew checkstyleMain checkstyleTest

update-deps:
	./gradlew useLatestVersions

stage:
	./gradlew stage

swagger:
	gradle clean generateOpenApiDocs

.PHONY: build

