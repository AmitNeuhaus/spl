//
// Created by tomcooll on 02/01/2022.
//

#ifndef CLIENT_ENCODERDECODER_H
#define CLIENT_ENCODERDECODER_H
#include <string>
#include <vector>
#include <iostream>
#include <sstream>


class encoderDecoder {
public:
    static std::string decode(short opcode,std::string input);
    static std::string encode(std::string input);
    encoderDecoder();
    static short getOpcode(std::string command);
    static void shortToBytes(short num, char* bytesArr);
    static short bytesToShort(char* bytesArr);
};


#endif //CLIENT_ENCODERDECODER_H
