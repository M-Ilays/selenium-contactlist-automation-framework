@echo off
echo ===================================
echo   Running Tests with Allure
echo ===================================
echo.
echo Step 1: Running all tests...
call mvn clean test
echo.
echo Step 2: Generating Allure report...
call mvn allure:report
echo.
echo Step 3: Opening report in browser...
call mvn allure:serve
