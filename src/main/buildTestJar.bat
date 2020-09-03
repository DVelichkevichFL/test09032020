@echo off

rem
rem Script to build the Test application for parsing strings
rem
rem %JAVA_HOME% - Java home path is required to run the application!
rem
setlocal

if exist "%JAVA_HOME%/bin" goto javaHomeFound

echo "<JAVA_HOME>/bin" is not found! Execution impossible
goto scriptEnd

:javaHomeFound
%JAVA_HOME%/bin/jar cm ./META-INF/MANIFEST.MF com messages.properties application.properties > ../../target/Test-0.0.1-SNAPSHOT.jar

:scriptEnd
echo.
pause
