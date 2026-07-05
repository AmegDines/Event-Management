@echo off
echo Compiling Java source files...
if not exist bin mkdir bin

javac -d bin -cp "lib\*" src\com\eventmanager\*.java src\com\eventmanager\model\*.java src\com\eventmanager\dao\*.java src\com\eventmanager\database\*.java src\com\eventmanager\view\*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

echo Running application...
java -cp "bin;lib\*" com.eventmanager.App
