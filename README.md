## Boba's Bot Instructions

### Build

1. Update the jar version in `build.gradle` and in `bootJar` update the `archiveVersion`
2. Run the command `./gradlew clean bootJar`
3. The latest version you created is under `/build/libs/bobas-bot-*.*.*.jar`

### Run

1. Run the command `./gradlew clean bootJar`
2. Then run `java -jar build/libs/bobas-bot-*.*.*.jar` (should be the only jar in that directory)
