clear; jdk1.6.0_45/bin/javac Calculator.java &&
jar -cfe MMWarranty.jar Calculator *.class &&
rm *.class && java -jar MMWarranty.jar $1 $2
