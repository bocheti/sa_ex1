System Architecture exercise 1 by Salvador Espinosa

Instructions on how to use program (Program.java):


Compilation:

javac Program.java


Send a text:

java Program send localhost 5000 "Hello, this is a test message!"


Receive a text:

java Program recv 5000


Send a file:

java Program sendf localhost 5000 /path/to/your/file.txt


Receive a file:

java Program recvf 5000
