REM Navigate to your project directory
cd RiverMonitoringService
REM Use Gradle to compile the project
call .\gradlew build
REM Use Gradle to compile the project
start "" ".\src\main\resources\static\index.html"
.\gradlew run