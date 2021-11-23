
#include "../include/Action.h"
#include "../include/Trainer.h"
#include "../include/Studio.h"

extern Studio* backup;

//Base Action
//TODO: check if needed to initial with those values
BaseAction::BaseAction(){}

BaseAction::~BaseAction() {}

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
OpenTrainer::OpenTrainer(int id, std::vector<Customer *> &customersList):BaseAction(), trainerId(id), customers(customersList),rep(""){}

OpenTrainer::~OpenTrainer(){
    customers.clear();
}

void OpenTrainer::act(Studio &studio) {
    for(Customer* customer: customers){
        rep+= customer->toString()+" ";
    }
    Trainer* trainerRef = studio.getTrainer(trainerId);
    if (trainerRef != nullptr && !(trainerRef -> isOpen())){
    int trainerCapacity = trainerRef -> getCapacity();
    trainerRef -> openTrainer();
    int maxCustomers = trainerCapacity < customers.size()? trainerCapacity: customers.size();
    for (int i=0; i < maxCustomers; i++){
        trainerRef -> addCustomer(customers[i]);
    }
    complete();
    }
    else{
        error("Workout session does not exist or is already open");
    }
}

std::string OpenTrainer::toString() const {
    std::string output= "open " + std::to_string(trainerId) +" "+ rep;
    if (getStatus() == COMPLETED) {
        output = output + std::string("Completed");
    }
    else if(getStatus() == ERROR){
        output = output + "Error: " + getErrorMsg();
    }
    return output;
}

OpenTrainer* OpenTrainer::clone(){
    return new OpenTrainer(trainerId,customers);
}
//Order

Order::Order(int id):BaseAction(), trainerId(id){}

Order::~Order(){}

void Order::act(Studio &studio) {
    Trainer* trainerRef = studio.getTrainer(trainerId);
    if (trainerRef != nullptr && trainerRef -> isOpen()){
        std::vector<Customer *> &customers = trainerRef->getCustomers();
        for (int i = 0; i < customers.size(); i++) {
            // todo: check how to store in var efficiently.
            std::vector<int> orderedWorkouts = customers[i]->order(studio.getWorkoutOptions());
            trainerRef->order(customers[i]->getId(), orderedWorkouts, studio.getWorkoutOptions());
            for(int workoutId : orderedWorkouts){
                std::cout << customers[i]->getName() + " Is Doing " + studio.getWorkoutOptions()[workoutId].getName()<<std::endl;
            }
        }

        complete();
    }else{
        error("Trainer does not exist or is not open");
    }
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

Order* Order::clone(){
    return new Order(trainerId);
}
//PrintActionsLog

PrintActionsLog::PrintActionsLog() {}

PrintActionsLog::~PrintActionsLog() {}

void PrintActionsLog::act(Studio &studio) {
    for (int i = 0; i < studio.getActionsLog().size(); i++) {
        std::cout << studio.getActionsLog()[i]->toString() << std::endl;
    }
}

std::string PrintActionsLog::toString() const {
    return std::string("log Completed");
}

PrintActionsLog* PrintActionsLog::clone() {
    return new PrintActionsLog();
}


//Move Customer class:
MoveCustomer::MoveCustomer(int src, int dst, int customerId):srcTrainer(src),dstTrainer(dst),id(customerId) {}

MoveCustomer::~MoveCustomer() {}

void MoveCustomer::act(Studio &studio) {
    Trainer* srcTrainerRef = studio.getTrainer(srcTrainer);
    Trainer* dstTrainerRef = studio.getTrainer(dstTrainer);
    if (canMove(srcTrainerRef,dstTrainerRef,id)){
        Customer* customer = srcTrainerRef->getCustomer(id);
        //add customer to new trainer:
        dstTrainerRef->addCustomer(customer);
        dstTrainerRef->order(customer->getId(),customer->order(studio.getWorkoutOptions()),studio.getWorkoutOptions());
        //remove customer from src trainer:
        srcTrainerRef->removeCustomer(id);

        complete();
    }
    else{
        error( "Cannot move customer");
    }
}


std::string MoveCustomer::toString() const {
    std::string actionString = "MoveCustomer " + std::to_string(srcTrainer) + ", " + std::to_string(dstTrainer) + ", " + std::to_string(id)+", " + std::to_string(getStatus());
    return actionString;
}

MoveCustomer* MoveCustomer::clone() {
    return new MoveCustomer(srcTrainer,dstTrainer,id);
}

bool MoveCustomer::canMove(Trainer* t1, Trainer* t2, int cId) {
    return (t1!= nullptr  && t2!= nullptr && t1->isOpen() && t2->isOpen() && !t2->isFull() && t1->getCustomer(cId) != nullptr);
}


//Close Class:
Close::Close(int id):trainerId(id) {}

Close::~Close(){}

void Close::act(Studio &studio) {
    Trainer* trainer = studio.getTrainer(trainerId);
    if (trainer!= nullptr && trainer->isOpen()){
        trainer->closeTrainer();
        std::cout << "Trainer " + std::to_string(trainerId) + " closed. Salary " + std::to_string(trainer->getSalary()) +"NIS"<<std::endl;
        complete();
    }else{
        error("Trainer does not exist or is not open");
    }

}

std::string Close::toString() const {
    std::string actionString = "Close, "  + std::to_string(trainerId) + ", " + std::to_string(getStatus()) ;
    return actionString;
}

Close* Close::clone(){
    return new Close(trainerId);
}


//Close All class:
CloseAll::CloseAll() {}

CloseAll::~CloseAll(){}

void CloseAll::act(Studio &studio) {
    for(int i = 0 ; i<studio.getNumOfTrainers()-1;++i){
        if (studio.getTrainer(i)->isOpen()){
            Close* closeTrainer= new Close(i);
            closeTrainer->act(studio);
            delete closeTrainer;
        }
    }
    complete();
}

std::string CloseAll::toString() const {
    std::string actionString = "CloseAll, "   + std::to_string(getStatus());
    return actionString;
}
CloseAll* CloseAll::clone(){
    return new CloseAll();
}


//Print Workout Options class:
PrintWorkoutOptions::PrintWorkoutOptions() {}

PrintWorkoutOptions::~PrintWorkoutOptions() {}

void PrintWorkoutOptions::act(Studio &studio) {
    std::vector<Workout> workouts = studio.getWorkoutOptions();
    for(Workout workout : workouts){
        std::cout << workout.toString() << std::endl;
    }
    complete();
}

std::string PrintWorkoutOptions::toString() const {
    std::string actionString = "PrintWorkoutOptions, "   + std::to_string(getStatus());
    return actionString;
}

PrintWorkoutOptions* PrintWorkoutOptions::clone(){
    return new PrintWorkoutOptions();
}

//Print Trainer Status class:
PrintTrainerStatus::PrintTrainerStatus(int id):trainerId(id) {}

PrintTrainerStatus::~PrintTrainerStatus(){}

void PrintTrainerStatus::act(Studio &studio) {
    Trainer* trainer = studio.getTrainer(trainerId);
    if (trainer->isOpen()){
        std::cout << "Trainer " + std::to_string(trainerId) +" status: open"<< std::endl;
        std::cout << "Customers:" << std::endl;
        for(Customer* customer:trainer->getCustomers()){
            std::cout << std::to_string(customer->getId()) << " " << customer->getName() << std::endl;
        }
        std::cout << "Orders:" << std::endl;
        for(OrderPair pair : trainer->getOrders()){
            std::cout << pair.second.getName() << " "<<std::to_string(pair.second.getPrice())<<" "<< std::to_string(pair.first)<<std::endl;
        }
        std::cout << "Current Trainer's Salary: "<< std::to_string(trainer->getSalary())<<std::endl;
    }else{
        std::cout << "Trainer " + std::to_string(trainerId) +" status: close"<< std::endl;
    }
    complete();
}

std::string PrintTrainerStatus::toString() const {
    std::string actionString = "PrintTrainerStatus, "   +std::to_string(trainerId)+", " +std::to_string(getStatus());
    return actionString;
}

PrintTrainerStatus* PrintTrainerStatus::clone(){
    return new PrintTrainerStatus(trainerId);
}

//BackUp Action class:
BackupStudio::BackupStudio() {}

void BackupStudio::act(Studio &studio) {
    backup = new Studio(studio);
    complete();
}

BackupStudio::~BackupStudio() {}

std::string BackupStudio::toString() const {
    std::string actionString = "BackUp, "   + std::to_string(getStatus());
    return actionString;
}

BackupStudio* BackupStudio::clone(){
    return new BackupStudio();
}


//Restore Studio Action class
RestoreStudio::RestoreStudio() {}

RestoreStudio::~RestoreStudio() {}

void RestoreStudio::act(Studio &studio) {
    if (backup  != nullptr){
        studio = *backup;
        complete();
    }

}

std::string RestoreStudio::toString() const {
    std::string actionString = "RestoreStudio, "   + std::to_string(getStatus());
    return actionString;
}

RestoreStudio* RestoreStudio::clone(){
    return new RestoreStudio();
}


