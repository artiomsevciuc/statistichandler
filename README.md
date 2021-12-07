# statistichandler
A simple Java Servlet example which contains how to use thread pool and timer
The application was build against Java 1.8 and newer. Test were performed on the Apache Tomcat apache-tomcat-8.5.32 container, should work on older and newer version, the only requirement is that the Tomcat has to run also on Java 1.8

Configurations:
1. /statisticshandler/src/main/webapp/META-INF/context.xml - this file contains database configurations for oracle, if you have another one you can take a look here https://tomcat.apache.org/tomcat-8.0-doc/jndi-datasource-examples-howto.html, you will find configurations examples for other databases.
2. ~/apache-tomcat-8.5.32/conf/RequestProcesssor.properties - it is optional file which has to be created and has to contain following properties:
      numberOfProcessingThreads=3
      numberOfSecondsToWaitBeforeFlush=30
      delayMinutesBetweenDatabaseFlush=5
      maximumThreadPoolSize=10
      keepAliveThreadPoolMinutes=10
As it was said they are optional ones as the StatisticsHandlerProperties enum contains default values for them
3. Please create database schema, oracle example is in the /statisticshandler/src/main/webapp/META-INF/DDL.sql file

When configuration steps where done please check if maven is installed, if not, then install it. 
Type "mvn clean install" command in the command line on the root folder of the project, that command will create the statisticshandler.war
Place statisticshandler.war int the Tomcat container in the ~/apache-tomcat-8.5.32/webapps
Start tomcat from command line by going to cmd to ~/apache-tomcat-8.5.32/bin using startup.bat command

There are three links which are accessible from browser, SOAP UI, Postman
POST http://localhost:8080/statisticshandler/handleStatistics
MediaType "applicatin/json"
Content
{
	"customerID": 1,
	"tagID": 2,
	"userID": "aaaaaaaa-bbbb-cccc-1111-222222222222",
	"remoteIP": "123.234.56.78",
	"timestamp": 1500000000
}
GET http://localhost:8080/statisticshandler/getStatisticsForCustomer?customer=Big News Media Corp&statisticForDay=2021-12-05T17:06:46.798Z
GET http://localhost:8080/statisticshandler/getStatisticsForADay?statisticForDay=2021-12-05T17:06:46.798Z
Time format is a strict one, please keep the one from the example
