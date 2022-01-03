//
// Created by tomcooll on 02/01/2022.
//

#include "../include/encoderDecoder.h"


encoderDecoder::encoderDecoder() = default;
std::string encoderDecoder::decode(short opcode,const std::string& input) {
    if (opcode == 9){
        return "NOTIFICATION " + input;
    }
    else if (opcode == 10){
        return "ERROR " + input;
    }else{

        return "ACK " + input.substr(0,input.length()-1);
    }
}
void encoderDecoder::encodeAndSend(std::string& input,ConnectionHandler& connection) {
    std::string command = input.substr(0,input.find(' '));
    input.erase(0,input.find(' ')+1);
    std::stringstream s_input(input);
    short opcode = getOpcode(command);
    short handleCase = getCase(opcode);
    char opcodeBytes[2];
    char delimiter[1] = {0};
    char endLine[1] = {';'};
    std::string nextSubstr;
    shortToBytes(opcode,opcodeBytes);
    switch(handleCase) {
        int nextDel;
        case 1:
            connection.sendBytes(opcodeBytes, 2);
            while (s_input.good()) {
                std::getline(s_input, nextSubstr, ' ');
                connection.sendLine(nextSubstr);
                connection.sendBytes(delimiter, 1);
            }
            connection.sendBytes(endLine, 1);

        case 2:
            connection.sendBytes(opcodeBytes, 2);
            connection.sendLine(input);
            connection.sendBytes(delimiter, 1);
            connection.sendBytes(endLine, 1);

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
            connection.sendLine(input);
            connection.sendBytes(delimiter, 1);
            connection.sendBytes(endLine, 1);

        case 4:
            connection.sendBytes(opcodeBytes, 2);
            nextDel = input.find(' ');
            std::string username = input.substr(0, nextDel);
            input.erase(0, nextDel + 1);
            connection.sendLine(username);
            connection.sendBytes(delimiter, 1);
            connection.sendLine(input);
            connection.sendBytes(delimiter, 1);
            std::string dateAndTime = getDateTime();
            connection.sendLine(dateAndTime);
            connection.sendBytes(delimiter, 1);
            connection.sendBytes(endLine, 1);

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
    if (opcode == 5 || opcode == 8){
        return 2;
    }else if(opcode == 4 ){
        return 3;
    }else if(opcode == 6){
        return 4;
    }
    return 1;
}

std::string encoderDecoder::getDateTime() {
    time_t rawTime;
    struct tm* timeinfo;
    char buffer[16];

    time(&rawTime);
    timeinfo = localtime(&rawTime);
    strftime(buffer,16,"%d-%m-%Y %H:%M",timeinfo);
    return buffer;
}











int main(int argc,char *argv[]) {
    ConnectionHandler connectionHandler("10.100.102.13", 5000);
    encoderDecoder* encdec = new encoderDecoder();
    std::string input;
    std::cin >> input;
    while(input != "stop"){
        encdec->encodeAndSend(input,connectionHandler);
    }
    delete encdec;
    return 0;
}

