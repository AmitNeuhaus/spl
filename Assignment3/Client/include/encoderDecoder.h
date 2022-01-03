//
// Created by tomcooll on 02/01/2022.
//

#ifndef CLIENT_ENCODERDECODER_H
#define CLIENT_ENCODERDECODER_H
#include <string>
#include <vector>
#include <iostream>
#include <sstream>
#include <ctime>
#include "connectionHandler.h"


class encoderDecoder {

public:
    static std::string decode(short opcode,const std::string& input);
    void encodeAndSend(std::string& input,ConnectionHandler &connection);
    explicit encoderDecoder();
    static short getOpcode(std::string command);
    static void shortToBytes(short num, char* bytesArr);
    static short bytesToShort(char* bytesArr);

private:
    short getCase(short opcode);
    std::string getDateTime();

};


#endif //CLIENT_ENCODERDECODER_H
