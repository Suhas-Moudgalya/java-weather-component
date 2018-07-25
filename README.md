# Java-Weather-Component
Gets weather data from weather API and stores in MySQL Database.

# Steps to be followed
1. Required JARs are present in jars folder in the same project add them as external libraries in libraries tab of Java Build Path in eclipse.
2. Signup to openweathermap and generate api-key.Use that api-key inside JSONMySQLComponent.java file. 
3. Create a database called "mysqlapp" in mysql. then Create a table called "weather" with three columns namely "city", "zipcode" and "temparature". Enter these credentials ininsert_into_db function inside JSONMySQLComponent.java file.

