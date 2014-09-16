@echo off
setlocal

set SERVICE_NAME=humanresourceswebapi
for %%I in ("%~dp0.") do set ES_HOME=%%~dpfI
set ES_LIB=%ES_HOME%\lib
set PRUNSRV=%ES_HOME%\webapi\%SERVICE_NAME%w

"%PRUNSRV%" //DS//%SERVICE_NAME%