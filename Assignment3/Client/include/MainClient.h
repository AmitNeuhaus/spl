//
// Created by AMIT Neuhaus on 06/01/2022.
//

#ifndef CLIENT_MAINCLIENT_H
#define CLIENT_MAINCLIENT_H
#include <connectionHandler.h>
#include <encoderDecoder.h>


class MainClient {
private:
    ConnectionHandler connection;
    encoderDecoder encdec;

public:
    MainClient(std::string ip, int port);
    void userInput();
    void workWithServer();
    static void run(std::string ip, int port);
};


#endif //CLIENT_MAINCLIENT_H
