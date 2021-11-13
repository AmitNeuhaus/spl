
#include "../include/Action.h"

//TODO: check if needed to initial with those values
BaseAction::BaseAction(){errorMessage(""),status(NULL)};
ActionStatus BaseAction::getStatus() const {}
std::string BaseAction::getErrorMsg() const {}(){return status;}
void BaseAction::complete() {status=COMPLETED;}
void BaseAction::error(std::string errorMsg) { status=ERROR; errorMsg=errorMsg;}