
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
    //log action

}
std::string OpenTrainer::toString() const { return "dfsfs";}



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
    //log action
}

std::string Order::toString() const {return "sdsda";}