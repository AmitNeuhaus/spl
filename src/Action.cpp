
#include "../include/Action.h"
#include "../include/Trainer.h"
#include "../include/Studio.h"


//Base Action
//TODO: check if needed to initial with those values
BaseAction::BaseAction(){}

ActionStatus BaseAction::getStatus() const {return status;}
std::string BaseAction::getErrorMsg() const {return errorMsg;}
void BaseAction::complete() {status=COMPLETED;}
void BaseAction::error(std::string errorMsg_in) {
    status=ERROR;
    errorMsg=errorMsg_in;
    std::cout << "Error: " + errorMsg_in << std::endl;
}



//OpenTrainer
// receive customers created reference and attach them to the current trainer.
OpenTrainer::OpenTrainer(int id, std::vector<Customer *> &customersList):BaseAction(), trainerId(id), customers(customersList){}

void OpenTrainer::act(Studio &studio) {
    Trainer* trainerRef = studio.getTrainer(trainerId);
    //TODO: check if trainer isn't open &&  and capacity is suitable  && trainer exists
    // if not call error("Workout session does not exist or is already open")
    int trainerCapacity = trainerRef -> getCapacity();
    // open trainer (set trainer session status to open)
    trainerRef -> openTrainer();
    int maxCustomers = trainerCapacity < customers.size()? trainerCapacity: customers.size();
    for (int i=0; i < maxCustomers; i++){
        trainerRef -> addCustomer(customers[i]);
    }
    complete();
}

std::string OpenTrainer::toString() const {
    std::string output= "open " + std::to_string(trainerId);
    for (int i=0; i<customers.size(); i++){
        output = output + std::string(" ") + customers[i] -> toString() + std::string(" ");
    }
    if (getStatus() == COMPLETED) {
        output = output + std::string("Completed");
    }
    else if(getStatus() == ERROR){
        output = output + getErrorMsg();
    }
    return output;
}

//Order

Order::Order(int id):BaseAction(), trainerId(id){}

void Order::act(Studio &studio) {
    Trainer* trainerRef = studio.getTrainer(trainerId);
    //TODO: check if trainer isnt open && trainer exists
    // if not call error("Trainer does not exist or is not open")

    // open trainer (set trainer session status to open)
    trainerRef->openTrainer();
    std::vector<Customer*>& customers = trainerRef -> getCustomers();
    for (int i=0;  i < customers.size(); i++){
        // todo: check how to store in var efficiently.
        std::vector<int> orderedWorkouts = customers[i] -> order(studio.getWorkoutOptions());
        trainerRef -> order(customers[i]-> getId(), orderedWorkouts, studio.getWorkoutOptions());
    }
    std::vector<OrderPair> trainerSessionOrders = trainerRef -> getOrders();
    for (int i =0; i<trainerSessionOrders.size(); ++i ){
        //cout the order pair
    }
    complete();
}

std::string Order::toString() const {
    std::string output= "order " + std::to_string(trainerId) +std::string(" ");
    if (getStatus() == COMPLETED) {
        output = output + std::string("Completed");
    }
    else if (getStatus() == ERROR){
        output = output + getErrorMsg();
    }
    return output;
}


//PrintActionsLog

PrintActionsLog::PrintActionsLog() {}

void PrintActionsLog::act(Studio &studio) {
    for (int i = 0; i < studio.getActionsLog().size(); i++) {
        std::cout << studio.getActionsLog()[i]->toString() << std::endl;
    }
}

std::string PrintActionsLog::toString() const {
    return std::string("log");
}
