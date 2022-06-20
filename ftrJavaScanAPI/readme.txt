Readme for ftrScanAPI Java demo program

1. ftrJavaScanAPI: the JNI (Java Native Interface) library for ftrScanAPI library. Source code is provided.

	A. Windows: VS2005 solution
	B. Linux: gcc make ( read the makefile for details )


2. ftrScanApiEx_Java: Netbeans project with source code.

	To run the program, it is necessary to install the Java Runtime Environment (JRE):

	A. Windows: need to copy the ftrScanAPI.dll and ftrJavaScanAPI.dll to %SYSTEM32%

		java -jar ftrScanApiEx_Java.jar

	B. Linux: need to copy the libScanAPI.so and libftrJavaScanAPI.so to /usr/lib/
		sudo java -jar ftrScanApiEx_Java.jar


		


