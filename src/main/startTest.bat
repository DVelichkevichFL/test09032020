@echo off

rem
rem Test application for parsing strings, which contain encoded hierarchy of string groups
rem
rem %JAVA_HOME% - Java home path is required to run the application!
rem
rem The string to parse can be specified as the first command line argument (OPTIONAL)
rem
setlocal

if exist "%JAVA_HOME%/bin" goto javaHomeFound

echo "<JAVA_HOME>/bin" is not found! Execution impossible
goto scriptEnd

:javaHomeFound
%JAVA_HOME%/bin/java -jar Test-0.0.1-SNAPSHOT.jar %1

:scriptEnd
echo.
pause
