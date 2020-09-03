@echo off

rem
rem Script to compile all classes of the Test application for parsing strings
rem
rem %JAVA_HOME% - Java home path is required to run the application!
rem
setlocal

if exist "%JAVA_HOME%/bin" goto javaHomeFound

echo "<JAVA_HOME>/bin" is not found! Execution impossible
goto scriptEnd

:javaHomeFound
rem
rem Clear 'target' folder
rem
rmdir /s /q ..\..\target

mkdir ..\..\target\classes
mkdir ..\..\target\classes\META-INF

rem
rem Copying 'startTest.bat' and all resources to 'target/classes'
rem
xcopy startTest.bat ..\..\target
xcopy .\resources\*.properties ..\..\target\classes
xcopy .\resources\META-INF ..\..\target\classes\META-INF

rem
rem Collect all source files
rem
dir /s /B *.java > sources.txt

rem
rem Compile all sources
rem
%JAVA_HOME%/bin/javac -d ../../target/classes @sources.txt

rem
rem Delete the file with list of sources to avoid the error
rem
del /f sources.txt

rem
rem Build a Test application JAR
rem
cd ..\..\target\classes
..\..\src\main\buildTestJar.bat

:scriptEnd
echo.
pause
