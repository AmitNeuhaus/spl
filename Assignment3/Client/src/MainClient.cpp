//
// Created by AMIT Neuhaus on 06/01/2022.
//
#include "MainClient.h"
#include "../include/MainClient.h"
#include <connectionHandler.h>

#include <iostream>
int main (int argc, char *argv[]) {
    std::cout <<"Started the program"<< std::endl;
    MainClient::run();
}

MainClient::MainClient(): connection("127.0.0.1", 3222), encoderDecoder() {
    if(!connection.connect()){
        std::cout <<"fuck tou didnt connect"<< std::endl;
    }

}

void MainClient::run() {
    MainClient client;
    std::thread t1(&MainClient::userInput,&client);
    client.workWithServer();
}

void MainClient::userInput() {
    bool stop = false;
    std::string input;
    while(!stop){
        std::getline(std::cin, input);
        encoderDecoder::encodeAndSend(input,connection);
        if(input == "LOGOUT")
            stop = true;

    }

}

void MainClient::workWithServer() {
    bool stop = false;
    while(!stop){
        std::string serverResponse = encoderDecoder.decode(connection);
        std::cout << serverResponse << std::endl;
        if(serverResponse == "ACK 3"){stop = true;}
    }
    std::cout<<"terminated output thread"<<std::endl;
}
