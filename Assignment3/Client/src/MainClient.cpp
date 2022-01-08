//
// Created by AMIT Neuhaus on 06/01/2022.
//
#include "MainClient.h"
#include "../include/MainClient.h"
#include <connectionHandler.h>

#include <iostream>
int main (int argc, char *argv[]) {
    std::string ip = argv[1];
    int port = std::stoi(argv[2]);
    std::cout <<"Started the program"<< std::endl;
    MainClient::run(ip,port);
}

MainClient::MainClient(std::string ip,int port): connection(ip, port), encdec() {
    if(!connection.connect()){
        std::cout <<"fuck tou didnt connect"<< std::endl;
    }

}

void MainClient::run(std::string ip,int port) {
    MainClient client(ip,port);
    std::thread t1(&MainClient::userInput,&client);
    client.workWithServer();
}

void MainClient::userInput() {
    bool stop = false;
    std::string input;
    while(!stop){
        std::getline(std::cin, input);
        encdec.encodeAndSend(input,connection);
        if(input == "LOGOUT")
            stop = true;

    }

}

void MainClient::workWithServer() {
    bool stop = false;
    while(!stop){
        std::string serverResponse = encdec.decode(connection);
        std::cout << serverResponse << std::endl;
        if(serverResponse == "ACK 3"){stop = true;}
    }
    std::cout<<"terminated output thread"<<std::endl;
}
