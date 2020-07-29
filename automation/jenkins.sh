#!/bin/sh
#CONSTANTS HERE
PROJECT_DIR="$( cd ../"$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
OUTPUT_DIR="${PROJECT_DIR}/automation/jenkins"

#BUILD VARIANT
BUILD_VARIANT="debug"
if [ -z "$1" ]
then
      BUILD_VARIANT=debug
else
      BUILD_VARIANT=$1
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

#Start Clean Process
print_blue "\nStarting"
print_blue "\nCleaning...\n"
./gradlew clean

print_blue "\ncleanBuildCache...\n"
./gradlew cleanBuildCache

print_blue "\nBuilding...\n"
./gradlew build

if [ "$BUILD_VARIANT" == "debug" ]; then
  	print_blue "\nassembleDebug...\n"
	./gradlew assembleDebug
elif [ "$BUILD_VARIANT" == "release" ]; then
	print_blue "\nassembleRelease...\n"
	./gradlew assembleRelease
else
	print_red "Invalid Build Variant!!!"
	exit
fi

#Copy APK to output folder
if [ "$BUILD_VARIANT" == "debug" ]; then
  mv "$PROJECT_DIR"/app/build/outputs/aar/app-debug.aar  "$OUTPUT_DIR/app-debug.aar"
elif [ "$BUILD_VARIANT" == "release" ]; then
	mv "$PROJECT_DIR"/app/build/outputs/aar/app-release.aar "$OUTPUT_DIR/app-debug.aar"
fi

print_blue "\nMoving AAR to output directory Done\n"
print_blue "\nOUTPUT DIR : $OUTPUT_DIR\n"