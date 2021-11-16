
#include "../include/Action.h"
#include "../include/Trainer.h"

//Base Action
//TODO: check if needed to initial with those values
BaseAction::BaseAction(){errorMessage(""),status(NULL)};
ActionStatus BaseAction::getStatus() const {}
std::string BaseAction::getErrorMsg() const {}(){return status;}
void BaseAction::complete() {status=COMPLETED;}
void BaseAction::error(std::string errorMsg) { status=ERROR; errorMsg=errorMsg;}



//OpenTrainer
// receive customers created reference and attach them to the current trainer.
OpenTrainer::OpenTrainer(int id, std::vector<Customer *> &customersList):BaseAction(){
    trainerId = id;
    customers = customersList;
}

void OpenTrainer::act(Studio &studio) {
    Trainer* trainerRef = studio.getTrainer(trainerId);
    //TODO: check if trainer isnt open &&  and capacity is suitable  && trainer exists
    // if not call error("Workout session does not exist or is already open")

    // open trainer (set trainer session status to open)
    trainerRef -> openTrainer();
    for (int i=0; i < customers.size(); i++){
        trainerRef.addCustomer(customers[i]);
    }
    complete();
    //log action

}


//Order

Order::Order(int id) {}:BaseAction(){
    trainerId = id;
}

void Order::act(Studio &studio) {
    Trainer* trainerRef = studio.getTrainer(trainerId);
    std::vector<int> workoutIds;
    //TODO: check if trainer isnt open && trainer exists
    // if not call error("Trainer does not exist or is not open")

    // open trainer (set trainer session status to open)
    std::vector<Customer*>& customers = trainerRef -> getCustomers()
    for (int i=0;  i < customers.size(); i++){
        // todo: check how to store in var efficiently.
        virtual std::vector<int> orderedWorkouts = customers[i] -> order(studio.getWorkoutOptions());
        trainerRef -> order(customers[i]-> getId(), orderedWorkouts, studio.getWorkoutOptions());
    }
    std::vector<OrderPair> trainerSessionOrders = trainerRef -> orderList;
    for (int i =0; i<trainerSessionOrders; ++i ){
        //cout the order pair
    }
    complete();
    //log action
}