//
// Created by tomcooll on 02/01/2022.
//

#include "../include/encoderDecoder.h"
encoderDecoder::encoderDecoder() = default;

std::string encoderDecoder::decode(short opcode,std::string input) {
    if (opcode == 9){
        return "NOTIFICATION " + input;
    }
    else if (opcode == 10){
        return "ERROR " + input;
    }else{

        return "ACK " + input.substr(0,input.length()-1);
    }
}
std::string encoderDecoder::encode(std::string input) {
    std::string output;
    std::stringstream s_stream(input);
    std::string substr;
    bool isFirst = true;
    while(s_stream.good()){
        std::getline(s_stream,substr,' ');
        if (isFirst){
            output += substr;
            isFirst = false;
        }else{
            output += " " + substr;
        }
    }
    return output+';';

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






//int main(int argc,char *argv[]) {
//    std::cout << "started testing this shit" <<std::endl;
//    encoderDecoder *encDec;
//    encDec =new encoderDecoder();
//     std::cout<<encDec->encode("REGISTER tom 12345 20-10-1995")<<std::endl;
//    delete(encDec);
//    return 0;
//}

