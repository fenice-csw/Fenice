D:
cd D:\Progetti\e-Prot\build\eprot\download
C:\Programmi\Java\jdk1.4.2_05\bin\jar cvf barcode-print-applet.jar -C ../WEB-INF/classes it/finsiel/siged/mvc/presentation/applet/BarcodePrintApplet.class
C:\Programmi\Java\jdk1.4.2_05\bin\keytool -genkey -keystore eprot -alias EPROT -keypass siged1
C:\Programmi\Java\jdk1.4.2_05\bin\jarsigner -keystore eprot -storepass siged1 -keypass siged1 barcode-print-applet.jar eprot
C:\Programmi\Java\jdk1.4.2_05\bin\jarsigner -keystore eprot -storepass siged1 -keypass siged1 barcode4j.jar eprot
pause
