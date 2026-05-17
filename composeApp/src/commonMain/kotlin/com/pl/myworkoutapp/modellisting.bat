@echo off
setlocal enabledelayedexpansion

echo. > model.txt
for /r "domain" %%f in (*.kt) do (
    echo ==== %%f ==== >> model.txt
    type "%%f" >> model.txt
    echo. >> model.txt
)

echo. > ui.txt
for /r "ui" %%f in (*.kt) do (
    echo ==== %%f ==== >> ui.txt
    type "%%f" >> ui.txt
    echo. >> ui.txt
)

echo. > navi.txt
for /r "ui/navigation" %%f in (*.kt) do (
    echo ==== %%f ==== >> navi.txt
    type "%%f" >> navi.txt
    echo. >> navi.txt
)

echo. > execution.txt
for /r "ui/execution" %%f in (*.kt) do (
    echo ==== %%f ==== >> execution.txt
    type "%%f" >> execution.txt
    echo. >> execution.txt
)


echo. > workouts.txt
for /r "ui/workouts" %%f in (*.kt) do (
    echo ==== %%f ==== >> workouts.txt
    type "%%f" >> workouts.txt
    echo. >> workouts.txt
)

echo. > workouts_details.txt
for /r "ui/workouts/details" %%f in (*.kt) do (
    echo ==== %%f ==== >> workouts_details.txt
    type "%%f" >> workouts_details.txt
    echo. >> workouts_details.txt
)


echo. > workouts_flat.txt
for %%f in ("ui/workouts\*.kt") do (
    set "name=%%~nxf"
    if /i not "!name:~0,7!"=="Circuit" (
        if /i not "!name:~0,8!"=="Workouts" (
            if /i "!name:~0,7!"=="Workout" (
                    if /i not "!name:~-9!"=="Screen.kt" (
                    echo ==== %%f ==== >> workouts_flat.txt
                    type "%%f" >> workouts_flat.txt
                    echo. >> workouts_flat.txt
                )
            )
        )
    )
)

dir /S/B > dir.txt