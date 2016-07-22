clear; javac -source 1.6 -target 1.6 Calculator.java &&
jar -cfe MMWarranty.jar Calculator *.class &&
rm *.class && java -jar MMWarranty.jar $1 $2
