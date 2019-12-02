javac -d ./build ./src/*.java || exit;
cd ./build;
jar cf ../shared.jar ./shared/ || exit; 
