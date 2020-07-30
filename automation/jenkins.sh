#!/bin/sh
#CONSTANTS HERE
PROJECT_DIR="$( cd ../"$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
OUTPUT_DIR="${PROJECT_DIR}/automation/jenkins"
BRANCH="develop"

#BUILD TYPE
BUILD_TYPE="debug"
if [ -z "$1" ]
then
      BUILD_TYPE=debug
else
      BUILD_TYPE=$1
fi

#Enter project directory
cd $PROJECT_DIR

#print functions
print_red(){
    printf "\e[1;31m$1\e[0m"
}
print_green(){
    printf "\e[1;32m$1\e[0m"
}
print_yellow(){
    printf "\e[1;33m$1\e[0m"
}
print_blue(){
    printf "\e[1;34m$1\e[0m"
}

print_blue "\nBuilding from $BRANCH branch...\n"

#Start Clean Process
print_blue "\nStarting"
print_blue "\nCleaning...\n"
./gradlew clean

print_blue "\ncleanBuildCache...\n"
./gradlew cleanBuildCache

print_blue "\nBuilding...\n"
./gradlew build

if [ "$BUILD_TYPE" == "debug" ]; then
  	print_blue "\nassembleDebug...\n"
	./gradlew assembleDebug
elif [ "$BUILD_TYPE" == "release" ]; then
	print_blue "\nassembleRelease...\n"
	./gradlew assembleRelease
else
	print_red "Invalid Build Type!!!"
	exit
fi

#Clean output folder
print_blue "\nDeleting old artifacts in output folder...\n"
rm -v "$OUTPUT_DIR/*"

#Copy APK to output folder
mv "$PROJECT_DIR"/app/build/outputs/aar/app-release.aar "$OUTPUT_DIR/app-$BUILD_TYPE.aar"

print_blue "\nMoving AAR to output directory Done\n"
print_blue "\nOUTPUT DIR : $OUTPUT_DIR\n"