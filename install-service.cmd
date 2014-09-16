@echo on
setlocal

set SERVICE_NAME=humanresourceswebapi
for %%I in ("%~dp0.") do set ES_HOME=%%~dpfI
set ES_LIB=%ES_HOME%\lib
set PRUNSRV=%ES_HOME%\webapi\%SERVICE_NAME%w

rem Initial memory pool size in MB.
set JVM_MS=256
rem Maximum memory pool size in MB.
set JVM_MX=1024
rem Thread stack size in KB.
set JVM_SS=256

rem Other options.
rem NB the pound (#) and semicolon (;) are separator characters.

rem Force the JVM to use IPv4 stack
rem JVM_OPTIONS=%JVM_OPTIONS% -Djava.net.preferIPv4Stack=true

REM Enable aggressive optimizations in the JVM
REM    - Disabled by default as it might cause the JVM to crash
REM set JVM_OPTIONS=%JVM_OPTIONS% -XX:+AggressiveOpts

REM Enable reference compression, reducing memory overhead on 64bit JVMs
REM    - Disabled by default as it is not stable for Sun JVM before 6u19
REM set JVM_OPTIONS=%JVM_OPTIONS% -XX:+UseCompressedOops

set JVM_OPTIONS=%JVM_OPTIONS% -XX:+UseParNewGC
set JVM_OPTIONS=%JVM_OPTIONS% -XX:+UseConcMarkSweepGC

set JVM_OPTIONS=%JVM_OPTIONS% -XX:CMSInitiatingOccupancyFraction=75
set JVM_OPTIONS=%JVM_OPTIONS% -XX:+UseCMSInitiatingOccupancyOnly

REM When running under Java 7
REM JVM_OPTIONS=%JVM_OPTIONS% -XX:+UseCondCardMark

REM GC logging options -- uncomment to enable
REM JVM_OPTIONS=%JVM_OPTIONS% -XX:+PrintGCDetails
REM JVM_OPTIONS=%JVM_OPTIONS% -XX:+PrintGCTimeStamps
REM JVM_OPTIONS=%JVM_OPTIONS% -XX:+PrintClassHistogram
REM JVM_OPTIONS=%JVM_OPTIONS% -XX:+PrintTenuringDistribution
REM JVM_OPTIONS=%JVM_OPTIONS% -XX:+PrintGCApplicationStoppedTime
REM JVM_OPTIONS=%JVM_OPTIONS% -Xloggc:/var/log/elasticsearch/gc.log

REM Causes the JVM to dump its heap on OutOfMemory.
set JVM_OPTIONS=%JVM_OPTIONS% -XX:+HeapDumpOnOutOfMemoryError
REM The path to the heap dump location, note directory must exists and have enough
REM space for a full heap dump.
REM JVM_OPTIONS=%JVM_OPTIONS% -XX:HeapDumpPath=$ES_HOME/logs/heapdump.hprof

REM Disables explicit GC
set JAVA_OPTS=%JAVA_OPTS% -XX:+DisableExplicitGC

set JVM_CLASSPATH=%ES_HOME%\webapi\%SERVICE_NAME%.jar

set JVM=auto
if exist "%ES_HOME%\jre\bin\server\jvm.dll" set JVM=%ES_HOME%\jre\bin\server\jvm.dll
if exist "%ES_HOME%\jre\bin\client\jvm.dll" set JVM=%ES_HOME%\jre\bin\client\jvm.dll

"%PRUNSRV%" //DS//%SERVICE_NAME%

::"%PRUNSRV%" //IS//%SERVICE_NAME%

"%PRUNSRV%" //IS//%SERVICE_NAME% ^
  --Jvm "%JVM%" ^
  --DisplayName "SDB Human Resources Training" ^
  --StdOutput auto ^
  --StdError auto ^
  --LogPath "%ES_HOME%\logs" ^
  --StartPath "%ES_HOME%" ^
  --StartMode=jvm --StartClass=spray.examples.Boot --StartMethod=main ^
  --StopMode=jvm --StopClass=spray.examples.Boot --StopMethod=close ^
  --Classpath "%JVM_CLASSPATH%" ^
  --JvmMs %JVM_MS% ^
  --JvmMx %JVM_MX% ^
  --JvmSs %JVM_SS%

rem These settings are saved in the Windows Registry at:
rem
rem    HKEY_LOCAL_MACHINE\SOFTWARE\Apache Software Foundation\Procrun 2.0\elasticsearch
rem
rem OR, on windows 64-bit procrun always uses the 32-bit registry at:
rem
rem    HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\Apache Software Foundation\Procrun 2.0\elasticsearch
rem
rem See http://commons.apache.org/daemon/procrun.html