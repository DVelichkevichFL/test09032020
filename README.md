# Test String Parsing Application Documentation
Test application to parse strings, which contain encoded hierarchy of string groups
### Preconditions
Make sure, that the **'JAVA_HOME'** path variable is pointing to a valid **Java 1.8** compatible **Java SDK**
### Build Instructions
#### Build Using The Command Line
**_Currently only Windows platform is supported!_**

It is possible to build an executable JAR, using the supplied batch scripts, located in **'&lt;TEST_ROOT&gt;/src/main'**.
Navigate to **'&lt;TEST_ROOT&gt;/src/main'** and use the following command to compile the sources and build the JAR:
```
buildTest.bat
```
#### Build With Maven
**Maven 3.6.1** is required to build the application.
Use the following command to build an executable JAR:
```
mvn clean install
```
### Configuration
There is an **'application.properties'** classpath resource. By default, it contains the following property:
```
parsing.groups.organizing.strategy=sorted
```

This property determines, whether tokens in the generated output should be **'sorted'** or **'plain'**.

To change the sorting strategy, it is necessary to create an **'application.properties'** file in the same folder, as the **'startTest.bat'** script.
This file should contain the following property to sort tokens:
```
parsing.groups.organizing.strategy=sorted
```
And the following property to generate plain output:
```
parsing.groups.organizing.strategy=plain
```
### Execution Instructions
There is already a built executable JAR and **'startTest.bat'** in **'&lt;TEST_ROOT&gt;/built/Test'**!
There is also **'application.properties'** with **'plain'** sorting strategy configured in **'&lt;TEST_ROOT&gt;/built'**.

Navigate to **'&lt;TEST_ROOT&gt;/target'** folder. There you will find the following artifacts:
1. 'startTest.bat';
2. 'Test-[VERSION].jar'.

Run the **'startTest.bat'** batch script to start the test application. In this case, it will ask to enter a string to parse. Press enter to accept the default string.

The script also accepts a string to parse as a command line argument:
```
startTest.bat "(id,created,employee(id,firstname,employeeType(id), lastname),location)"
```
**_The result output will not be generated, if the string to parse contains error(s)!_**
## Assumptions
1. The following characters are not part of string group names (tokens): '"', ',', '(', ')', whitespaces;
2. Groups of tokens are enclosed between '(' and ')' characters;
3. A token may have a complex structure, e. g.: 'token1(token2(token3(token4(token5))))';
4. The entered string to parse may contain errors. Basic validation and reporting of errors is required. Application execution for corrupted data is impossible;
5. Tokens (including complex tokens (see point **4.**)) are separated by ',' character;
6. The root group may be not enclosed between the '(' and ')' characters;
7. It should be possible to enter any string to parse, using the console or command line argument;
8. The approach to generate the output should be selectable, since generating the output in alphabetical order should be a bonus feature
