@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM Apache Maven Wrapper startup batch script, version 3.3.2

@echo off
@if "%MAVEN_BATCH_ECHO%" == "on" echo %MAVEN_BATCH_ECHO%

if "%HOME%" == "" (set "HOME=%HOMEDRIVE%%HOMEPATH%")

@REM Execute pre-RC file if present
if not "%MAVEN_SKIP_RC%" == "" goto skipRcFile
if exist "%USERPROFILE%\mavenrc_pre.bat" call "%USERPROFILE%\mavenrc_pre.bat" %*
if exist "%USERPROFILE%\mavenrc_pre.cmd" call "%USERPROFILE%\mavenrc_pre.cmd" %*
:skipRcFile

@REM Find the project base dir (directory containing .mvn folder)
set EXEC_DIR=%CD%
set WDIR=%EXEC_DIR%

:findBaseDir
IF EXIST "%WDIR%\.mvn" goto baseDirFound
cd ..
IF "%WDIR%"=="%CD%" goto baseDirNotFound
set "WDIR=%CD%"
goto findBaseDir

:baseDirFound
set MAVEN_PROJECTBASEDIR=%WDIR%
cd "%EXEC_DIR%"
goto endDetectBaseDir

:baseDirNotFound
set MAVEN_PROJECTBASEDIR=%EXEC_DIR%
cd "%EXEC_DIR%"

:endDetectBaseDir

@REM Download maven-wrapper.jar if not present
if exist "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" goto runWithWrapperJar

set "DOWNLOAD_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"

for /f "usebackq tokens=1,2 delims==" %%A IN ("%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties") DO (
    if "%%A"=="wrapperUrl" set "DOWNLOAD_URL=%%B"
)

@echo Downloading Maven Wrapper JAR from: %DOWNLOAD_URL%
powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object System.Net.WebClient).DownloadFile('%DOWNLOAD_URL%', '%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar')"
if ERRORLEVEL 1 (
    echo Failed to download maven-wrapper.jar
    goto error
)

:runWithWrapperJar
cd /D "%MAVEN_PROJECTBASEDIR%"

@REM Locate Java
if not "%JAVA_HOME%" == "" goto OkJHome
for %%i in (java.exe) do set "JAVACMD=%%~$PATH:i"
if not "%JAVACMD%" == "" goto runMaven
goto NoJavaHome

:OkJHome
if not exist "%JAVA_HOME%\bin\java.exe" goto NoJavaHome
set "JAVACMD=%JAVA_HOME%\bin\java.exe"
goto runMaven

:NoJavaHome
echo.
echo Error: JAVA_HOME is not defined correctly or java.exe not found on PATH.
echo.
goto error

:runMaven
"%JAVACMD%" ^
  %MAVEN_OPTS% ^
  -classpath "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" ^
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
  org.apache.maven.wrapper.MavenWrapperMain %*

if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

if not "%MAVEN_SKIP_RC%" == "" goto skipRcPost
if exist "%USERPROFILE%\mavenrc_post.bat" call "%USERPROFILE%\mavenrc_post.bat"
if exist "%USERPROFILE%\mavenrc_post.cmd" call "%USERPROFILE%\mavenrc_post.cmd"
:skipRcPost

if "%MAVEN_BATCH_PAUSE%" == "on" pause
if "%MAVEN_TERMINATE_CMD%" == "on" exit %ERROR_CODE%

cmd /C exit /B %ERROR_CODE%
