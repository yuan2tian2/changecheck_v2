@echo off
REM mvn dependency:copy-dependencies -DoutputDirectory=lib
SET CLASS=target/changecheck-1.0-SNAPSHOT.jar;lib/log4j-api-2.11.0.jar;lib/log4j-core-2.11.0.jar;lib/log4j-slf4j-impl-2.11.0.jar;lib/slf4j-api-1.7.25.jar;lib/commons-logging-1.2.jar;lib/jcl-over-slf4j-1.7.25.jar;lib/javax.mail-api-1.6.1.jar;lib/activation-1.1.jar;lib/javax.mail-1.6.1.jar
java -classpath target;%CLASS% com.haradatakahiko.security.App
pause
