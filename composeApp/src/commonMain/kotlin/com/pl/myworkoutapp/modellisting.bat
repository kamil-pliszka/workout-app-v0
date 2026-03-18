@echo off

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

dir /S/B > dir.txt