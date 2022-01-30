@echo off
SETLOCAL EnableDelayedExpansion
set HMCL_UPDATE_ARG="-Dhmcl.update_source.override="
set HMCL_UPDATE_JSON=https://gitee.com/Glavo/HMCL-Update/raw/main/update/dev.json
set HMCL_UPDATE_JAVA_OPTION=%HMCL_UPDATE_ARG%%HMCL_UPDATE_JSON%
if "%JAVA_TOOL_OPTIONS%JUST_TEST"=="JUST_TEST" (
    SetX JAVA_TOOL_OPTIONS %HMCL_UPDATE_JAVA_OPTION%
) else (
    for /f "usebackq tokens=2,*" %%A in (`reg query HKCU\Environment /v JAVA_TOOL_OPTIONS`) do set _TEMP_JAVA_TOOL_OPTIONS=%%B
    if "!_TEMP_JAVA_TOOL_OPTIONS!JUST_TEST"=="JUST_TEST" (
        choice /c:yn /m "You have set the environment variable JAVA_TOOL_OPTIONS for machine, so we don't have permission to modify it. Do you want to open the system properties page? (Y/N)"
        if !errorlevel!==1 SystemPropertiesAdvanced
    ) ELSE (
        powershell -Command "SetX JAVA_TOOL_OPTIONS $(If ($env:_TEMP_JAVA_TOOL_OPTIONS -Match '(?<=\s|^^)-Dhmcl\.update_source\.override=\S*') {$env:_TEMP_JAVA_TOOL_OPTIONS -replace '(?<=\s|^^)-Dhmcl\.update_source\.override=\S*', $env:HMCL_UPDATE_JAVA_OPTION} Else {\"$env:_TEMP_JAVA_TOOL_OPTIONS $env:HMCL_UPDATE_JAVA_OPTION\"})"
    )
)
