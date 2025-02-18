@echo on
cd %~dp0
cd ../../../../
mvn mybatis-generator:generate && pause
