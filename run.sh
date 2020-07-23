cd src
find -name "*.java" > sources.txt

sources=$(<sources.txt)
javac -d ../bin $sources
cd ..

while true; do
    read -p "Какое из заданий запустить? (1/2/3) (0 - выход)  " yn
    case $yn in
        [1]* ) java -cp bin Task1.Main; break;;
        [2]* ) java -cp bin Task2.Main; break;;
        [3]* ) java -cp bin Task3.Main; break;;
        [0]* ) exit;;
        * ) echo "Please answer yes or no.";;
    esac
done

rm src/sources.txt
rm -rf bin