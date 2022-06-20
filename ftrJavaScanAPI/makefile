CXX=gcc

CFLAGS += -fPIC -O3 -I./ftrScanAPI/ -I/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/include -I/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/include/darwin/

LDFLAGS += -L/Users/user/Desktop/ScanAPI_Java_Sample_01082011/ftrJavaScanAPI/  -lScanAPI -lstdc++

FTRJAVASCANAPISRC = ftrJavaScanAPI/ftrJavaScanAPI.cpp

FTRJAVASCANAPIOBJ = ftrJavaScanAPI.o 

FTRJAVASCANAPILIB = libftrJavaScanAPI.dylib


all: $(FTRJAVASCANAPILIB)
	    
$(FTRJAVASCANAPIOBJ): $(FTRJAVASCANAPISRC)
	$(CXX) -c  $(CFLAGS) $(FTRJAVASCANAPISRC)

$(FTRJAVASCANAPILIB):  $(FTRJAVASCANAPIOBJ)
	$(CXX) $(FTRJAVASCANAPIOBJ) $(LDFLAGS) -dynamiclib -fPIC -o $(FTRJAVASCANAPILIB) 


clean:
	rm -f *.o
	rm -f $(FTRJAVASCANAPILIB)
