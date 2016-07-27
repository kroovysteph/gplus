clear; javac Calculator.java &&
jar -cfe MMWarranty.jar Calculator *.class &&
rm *.class && java -jar MMWarranty.jar $1 $2
