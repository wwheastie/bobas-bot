## Boba's Bot Instructions

### How to build new jar file with gradlew

1. Update the jar version in `build.gradle` and in `bootJar` update the `archiveVersion`
2. Run the command `./gradlew clean bootJar`
3. Grab the latest version you created under `/build/libs/bobas-bot-*.*.*.jar`

### How to start application

1. Run the command `./gradlew clean bootJar`
2. Then run `java -jar build/libs/bobas-bot-*.*.*.jar` (should be the only jar in that directory)