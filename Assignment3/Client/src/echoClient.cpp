#include <stdlib.h>
#include <connectionHandler.h>
#include "../include/encoderDecoder.h"

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main (int argc, char *argv[]) {
    encoderDecoder encDec = encoderDecoder(<#initializer#>, <#initializer#>);
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect() ) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
	
	//From here we will see the rest of the ehco client implementation:
    while (1) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
		std::string line(buf);
        std::string opcodeString = line.substr(0,line.find(" "));
        short opcode = encDec.getOpcode(opcodeString);
        char byte[2];
        encoderDecoder::shortToBytes(opcode,byte);
        line = encDec.encode(line.erase(0,line.find(" ")+1));
		int len=line.length();
        if (!connectionHandler.sendBytes(byte,2)  || !connectionHandler.sendLine(line)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
		// connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
        std::cout << "Sent " << len+1 << " bytes to server" << std::endl;

 
        // We can use one of three options to read data from the server:
        // 1. Read a fixed number of characters
        // 2. Read a line (up to the newline character using the getline() buffered reader
        // 3. Read up to the null character
        std::string answer;
        char bytes[2];
        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
        if (!connectionHandler.getBytes(bytes,2)|| !connectionHandler.getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        short newOpcode = encoderDecoder::bytesToShort(bytes);
        std::string serverResponse = encoderDecoder::decode(newOpcode,answer);
        std::cout << "Reply: " << serverResponse << "||" << std::endl << std::endl;
		len=serverResponse.length();
		// A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
		// we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
        serverResponse.resize(len-1);
        std::cout << "Reply: " << serverResponse <<"||"<<  std::endl << std::endl;
        if (answer == "bye") {
            std::cout << "Exiting...\n" << std::endl;
            break;
        }
    }
    return 0;
}
