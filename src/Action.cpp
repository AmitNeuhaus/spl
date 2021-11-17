
#include "../include/Action.h"
#include "../include/Trainer.h"
#include "../include/Studio.h"
#include <utility>


//Base Action
//TODO: check if needed to initial with those values
BaseAction::BaseAction(){}

ActionStatus BaseAction::getStatus() const {return status;}
std::string BaseAction::getErrorMsg() const {return errorMsg;}
void BaseAction::complete() {status=COMPLETED;}
void BaseAction::error(std::string errorMsg_in) { status=ERROR; errorMsg=errorMsg_in;}



//OpenTrainer
// receive customers created reference and attach them to the current trainer.
OpenTrainer::OpenTrainer(int id, std::vector<Customer *> &customersList):BaseAction(), trainerId(id), customers(customersList){}

void OpenTrainer::act(Studio &studio) {
    Trainer* trainerRef = studio.getTrainer(2);
    //TODO: check if trainer isn't open &&  and capacity is suitable  && trainer exists
    // if not call error("Workout session does not exist or is already open")

    // open trainer (set trainer session status to open)
    trainerRef -> openTrainer();
    for (int i=0; i < customers.size(); i++){
        trainerRef -> addCustomer(customers[i]);
    }
    complete();
    //log action

}
std::string OpenTrainer::toString() const { return "dfsfs";}


//Order class:

Order::Order(int id):BaseAction(), trainerId(id){}

void Order::act(Studio &studio) {
    Trainer* trainerRef = studio.getTrainer(trainerId);
    std::vector<int> workoutIds;
    //TODO: check if trainer isnt open && trainer exists
    // if not call error("Trainer does not exist or is not open")

    // open trainer (set trainer session status to open)
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
std::string Order::toString() const {return "sdsda";}



//Move Customer class:
MoveCustomer::MoveCustomer(int src, int dst, int customerId):srcTrainer(src),dstTrainer(dst),id(customerId) {}

void MoveCustomer::act(Studio &studio) {
    Trainer* srcTrainerRef = studio.getTrainer(srcTrainer);
    Trainer* dstTrainerRef = studio.getTrainer(dstTrainer);
    if (canMove(srcTrainerRef,dstTrainerRef,id)){
        Customer* customer = srcTrainerRef->getCustomer(id);
        //remove customer from src trainer:
        srcTrainerRef->removeCustomer(id);
        std::vector<OrderPair>* srcOrderList = &(srcTrainerRef->getOrders());
        for (std::vector<std::pair<int,Workout>>::iterator it = srcOrderList->begin();it<srcOrderList->end();++it) {
           if(it->first == id){
                srcOrderList->erase(it);  //// this is the line that cause the ERROR, good luck she is a bitch
           }
        }
        //add customer to new trainer:
        dstTrainerRef->addCustomer(customer);
        dstTrainerRef->order(customer->getId(),customer->order(studio.getWorkoutOptions()),studio.getWorkoutOptions());
        complete();
    }
    else{
        error( "Cannot move customer");
    }


}


std::string MoveCustomer::toString() const {
    std::string actionString = "MoveCustomer" + std::to_string(srcTrainer) + ", " + std::to_string(dstTrainer) + ", " + std::to_string(id)+", " + std::to_string(getStatus());
    return actionString;
}

bool MoveCustomer::canMove(Trainer* t1, Trainer* t2, int cId) {
    return (t1!= nullptr  && t2!= nullptr && t1->isOpen() && t2->isOpen() && !t2->isFull() && t1->getCustomer(cId) != nullptr);
}


//Close Class:
Close::Close(int id):trainerId(id) {}

void Close::act(Studio &studio) {
    Trainer* trainer = studio.getTrainer(trainerId);
    if (trainer!= nullptr && trainer->isOpen()){
        trainer->closeTrainer();
        std::cout << "Trainer " + std::to_string(trainerId) + "closed. Salary " + std::to_string(trainer->getSalary()) +"NIS"<<std::endl;
    }else{
        error("Trainer does not exist or is not open");
    }

}

std::string Close::toString() const {
    std::string actionString = "Close, "  + std::to_string(trainerId) + ", " + std::to_string(getStatus());
    return actionString;
}

