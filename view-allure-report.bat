@echo off
echo ===================================
echo   Opening Allure Report
echo ===================================
echo.
echo Starting Allure server...
echo The report will open in your default browser automatically.
echo.
echo Press Ctrl+C to stop the server when you're done viewing the report.
echo.
mvn allure:serve
