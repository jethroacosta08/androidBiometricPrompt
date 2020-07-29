#BUILD VARIANT
BUILD_VARIANT="debug"
if [ -z "$1" ]
then
      BUILD_VARIANT=debug
else
      BUILD_VARIANT=$1
fi

printf $BUILD_VARIANT