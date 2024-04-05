@echo off
TITLE onesignal-socket-layer

if exist venv\ (
  echo Virtual environment exists
  ) else (
  echo Creating virtual environment...
  python -m venv venv
  %~dp0venv\Scripts\pip install -r requirements.txt
)

cls
%~dp0venv\Scripts\python.exe main.py
pause
