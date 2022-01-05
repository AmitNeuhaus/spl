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
#include <thread>
#include "connectionHandler.h"


class encoderDecoder {

public:
    static std::string decode(ConnectionHandler &connection);
    static void encodeAndSend(std::string& input,ConnectionHandler &connection);
    explicit encoderDecoder();
    static short getOpcode(std::string command);
    static void shortToBytes(short num, char* bytesArr);
    static short bytesToShort(char* bytesArr);
    static void readSocket(ConnectionHandler &connection);


private:
    static short getCase(short opcode);
    static int getNumber(int numberOfBytes,ConnectionHandler &connection);
    static std::string getDateTime();
    static std::string getNotificationString(ConnectionHandler &connection);
    static std::string getStatAckString(int commandOpcode, ConnectionHandler &connection);
    static std::string getFollowAckString(ConnectionHandler &connection);


};


#endif //CLIENT_ENCODERDECODER_H
