//
// Created by tomcooll on 02/01/2022.
//

#include <fstream>
#include "../include/encoderDecoder.h"


encoderDecoder::encoderDecoder() = default;


std::string encoderDecoder::decode(ConnectionHandler &connection) {
    std::string result = "Unknown Opcode";
    int opcode = getNumber(2,connection);
    if(opcode == 10){ //ACK
        int commandOpcode = getNumber(2,connection);
        if (commandOpcode == 4){ //Follow
            result = getFollowAckString(connection);

        }else if(commandOpcode == 8 ||  commandOpcode == 7) { //Stat/Logsatat
            result = getStatAckString(commandOpcode,connection);
        }else{ //default case
            result = "ACK "+std::to_string(commandOpcode);
        }


    }else if(opcode == 11){ // ERROR
        int commandOpcode = getNumber(2,connection);
        result = &"ERROR "[commandOpcode] ;

    }else if(opcode == 9) { //NOTIFICATION
        result = getNotificationString(connection);
    }
    char end[1];
    connection.getBytes(end,1);
    return result;
}


void encoderDecoder::encodeAndSend(std::string& input,ConnectionHandler& connection) {
    std::string command = input.substr(0,input.find(' '));
    input.erase(0,input.find(' ')+1);
    std::stringstream s_input(input);
    short opcode = getOpcode(command);
    short handleCase = getCase(opcode);
    char opcodeBytes[2];
    char endLine[1] = {';'};
    std::string nextSubstr;
    shortToBytes(opcode,opcodeBytes);
    std::string username;
    int nextDel;
    std::string dateAndTime;
    switch(handleCase) {

        case 1:
            connection.sendBytes(opcodeBytes, 2);
            while (s_input.good()) {
                std::getline(s_input, nextSubstr, ' ');
                connection.sendSubstr(nextSubstr);
            }
            connection.sendBytes(endLine, 1);
            break;
        case 2:
            connection.sendBytes(opcodeBytes, 2);
            connection.sendSubstr(input);
            connection.sendBytes(endLine, 1);
            break;

        case 3:
            connection.sendBytes(opcodeBytes, 2);
            nextDel = input.find(' ');
            nextSubstr = input.substr(0, nextDel);
            input.erase(0, nextDel + 1);
            char follow[1];
            if (nextSubstr == "1") {
                follow[0] = '1';

            } else {
                follow[0] = '0';
            }
            connection.sendBytes(follow, 1);
            connection.sendSubstr(input);
            connection.sendBytes(endLine, 1);
            break;

        case 4:
            connection.sendBytes(opcodeBytes, 2);
            nextDel = input.find(' ');
            username = input.substr(0, nextDel);
            input.erase(0, nextDel + 1);
            connection.sendSubstr(username);
            connection.sendSubstr(input);
            dateAndTime = getDateTime();
            connection.sendSubstr(dateAndTime);
            connection.sendBytes(endLine, 1);
            break;

        case 5:
            connection.sendBytes(opcodeBytes, 2);
            connection.sendBytes(endLine, 1);
            break;


    }

}
short encoderDecoder::getOpcode(std::string command) {
    if (command.compare("REGISTER") == 0) {
        return 1;
    }else if (command.compare("LOGIN") == 0){
        return 2;
    }else if(command.compare("LOGOUT")== 0){
        return 3;
    }else if (command.compare("FOLLOW") == 0){
        return 4;
    }else if(command.compare("POST")== 0){
        return 5;
    }else if (command.compare("PM") == 0){
        return 6;
    }else if(command.compare("LOGSTAT")== 0){
        return 7;
    }else if (command.compare("STAT") == 0){
        return 8;
    }else if(command.compare("NOTIFICATION")== 0){
        return 9;
    }else if (command.compare("ACK") == 0){
        return 10;
    }else if(command.compare("ERROR")== 0){
        return 11;
    }else if(command.compare("BLOCK")== 0){
        return 12;
    }else {
        return 13;
    }
}

void encoderDecoder::shortToBytes(short num, char *bytesArr) {
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

short encoderDecoder::bytesToShort(char *bytesArr) {
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

short encoderDecoder::getCase(short opcode) {
    if (opcode == 5 || opcode == 8){ //POST AND STAT
        return 2;
    }else if(opcode == 4 ){ //FOLLOW
        return 3;
    }else if(opcode == 6){ //PM
        return 4;
    }else if(opcode == 3 || opcode == 7){ //LOGOUT AND STAT
        return 5;
    }
    return 1;
}

std::string encoderDecoder::getDateTime() {
    time_t rawTime;
    struct tm* timeinfo;
    char buffer[50];

    time(&rawTime);
    timeinfo = localtime(&rawTime);
    strftime(buffer,50,"%d-%m-%Y %H:%M",timeinfo);
    return buffer;
}

int encoderDecoder::getNumber(int numberOfBytes,ConnectionHandler &connection) {
    int result;
    char bytes[numberOfBytes];
    if (numberOfBytes == 2){
        connection.getBytes(bytes,numberOfBytes);
        result = bytesToShort(bytes);
        return result;
    }else{
        return connection.getBytes(bytes,numberOfBytes);
    }
}

std::string encoderDecoder::getNotificationString(ConnectionHandler &connection){
    int notificationType = getNumber(1, connection);
    std::string postingUser;
    connection.getSubstr(postingUser);
    std::string content;
    connection.getSubstr(content);
    return &"NOTIFICATION " [notificationType] +postingUser + " " + content;

}

std::string encoderDecoder::getStatAckString(int commandOpcode, ConnectionHandler &connection) {
    int age = getNumber(2,connection);
    int numOfPosts = getNumber(2,connection);
    int numOfFollowers = getNumber(2,connection);
    int numOfFollowing = getNumber(2,connection);
    return "ACK " + std::to_string(commandOpcode) + " " + std::to_string(age) + " " + std::to_string(numOfPosts) + " " + std::to_string(numOfFollowers) + " "+std::to_string(numOfFollowing);
}

std::string encoderDecoder::getFollowAckString(ConnectionHandler &connection){
    std::string userName;
    connection.getSubstr(userName);
    return "ACK "+ std::to_string(4) + " " + userName;
}

//TODO remove before submission
int main(int argc,char *argv[]) {
    ConnectionHandler connectionHandler("10.100.102.4", 5000);
    if(!connectionHandler.connect()){
        std::cout <<"fuck tou didnt connect"<< std::endl;
    }
    auto* encdec = new encoderDecoder();
    std::string input;
    bool stop =false;
    while(!stop){
        std::getline(std::cin, input);
        if(input == "file"){
            std::string filePath;
            std::getline(std::cin,filePath);
            std::ifstream file(filePath);
            std::string line;
            do{
                std::getline(std::cin,input);
                std::getline(file,line);
                std::cout << line << std::endl;
                encdec->encodeAndSend(line,connectionHandler);
                std::string serverResponse=encdec->decode(connectionHandler);
                std::cout <<serverResponse <<std::endl;
            }while(input.empty());
        }
        encdec->encodeAndSend(input,connectionHandler);
        std::string serverResponse=encdec->decode(connectionHandler);
        std::cout <<serverResponse <<std::endl;
        if(serverResponse == "ACK 3"){
            stop = true;
        }
    }


    delete encdec;
    return 0;
}

