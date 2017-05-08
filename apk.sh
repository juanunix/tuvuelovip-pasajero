#! /usr/bin

./gradlew assembleDebug
FILE_NAME="app/build/outputs/apk/app-debug.apk"
#NOW=$(date +"%d-%m-%Y-%p")
NOW=$(date +"%d-%m-%Y")
BRANCH=$(git symbolic-ref --short HEAD)
NEW_FILE_NAME="/tmp/charter-pasajero-apk"-$BRANCH-$NOW.apk
cp "$FILE_NAME" "$NEW_FILE_NAME"
chmod 400 private-key-for-scp
scp -P 5388 -i private-key-for-scp "$NEW_FILE_NAME" pruebas-eme@162.252.57.227:/home/pruebas-eme/
