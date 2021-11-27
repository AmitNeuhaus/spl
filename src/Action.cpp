
#include "../include/Action.h"
#include "../include/Trainer.h"
#include "../include/Studio.h"

extern Studio* backup;

//Base Action
BaseAction::BaseAction():errorMsg(""),status(ERROR){}

BaseAction::~BaseAction() {}

ActionStatus BaseAction::getStatus() const {return status;}
std::string BaseAction::getErrorMsg() const {return errorMsg;}
void BaseAction::complete() {status=COMPLETED;}
void BaseAction::error(std::string errorMsg_in) {
    status=ERROR;
    errorMsg="Error: " + errorMsg_in;
    std::cout <<  errorMsg_in << std::endl;
}

void BaseAction::setStatus(ActionStatus newStatus) {
    status = newStatus;
}

std::string BaseAction::convertStatus() const {
    std::string output;
    if (getStatus() == COMPLETED) {
        output =  std::string(" Completed");
    }
    else if(getStatus() == ERROR){
        output = " " + getErrorMsg();
    }
    return output;
}


//OpenTrainer
// receive customers created reference and attach them to the current trainer.
OpenTrainer::OpenTrainer(int id, std::vector<Customer *> &customersList):BaseAction(), trainerId(id), customers(customersList),rep(""){}

OpenTrainer::~OpenTrainer(){
    customers.clear();
}

void OpenTrainer::act(Studio &studio) {
    Trainer* trainerRef = studio.getTrainer(trainerId);
    if (trainerRef != nullptr && !(trainerRef -> isOpen())){
    trainerRef -> openTrainer();
    for (int i=0; i < int(customers.size()); i++){
        rep+= customers[i]->toString()+" ";
        trainerRef -> addCustomer(customers[i]);
    }
    complete();
    }
    else{
        error("Trainer does not exist or is already open");
    }
}

std::string OpenTrainer::toString() const {
    std::string output= "open " + std::to_string(trainerId) +" "+ rep + convertStatus();
    return output;
}

OpenTrainer* OpenTrainer::clone(){
    OpenTrainer* newOpenTrainer = new OpenTrainer(trainerId,customers);
    newOpenTrainer-> setStatus(getStatus());
    newOpenTrainer -> rep = rep;
    return newOpenTrainer;
}
//Order

Order::Order(int id):BaseAction(), trainerId(id){}

Order::~Order(){}

void Order::act(Studio &studio) {
    Trainer* trainerRef = studio.getTrainer(trainerId);
    if (trainerRef != nullptr && trainerRef -> isOpen()){
        std::vector<Customer *> &customers = trainerRef->getCustomers();
        for (int i = 0; i < int(customers.size()); i++) {
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
    std::string output= "order " + std::to_string(trainerId) +convertStatus();
    return output;
}

Order* Order::clone(){
    Order* newOrder = new Order(trainerId);
    newOrder->setStatus(getStatus());
    return newOrder;
}
//PrintActionsLog

PrintActionsLog::PrintActionsLog() {}

PrintActionsLog::~PrintActionsLog() {}

void PrintActionsLog::act(Studio &studio) {
    for (int i = 0; i < int(studio.getActionsLog().size()); i++) {
        std::cout << studio.getActionsLog()[i]->toString() << std::endl;
    }
}

std::string PrintActionsLog::toString() const {
    return std::string("log Completed");
}

PrintActionsLog* PrintActionsLog::clone() {
    PrintActionsLog* newPrintActionsLog = new PrintActionsLog();
    newPrintActionsLog->setStatus(getStatus());
    return newPrintActionsLog;
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
        if (srcTrainerRef->getCustomers().empty()){
            srcTrainerRef->closeTrainer();
        }

        complete();
    }
    else{
        error( "Cannot move customer");
    }
}


std::string MoveCustomer::toString() const {
    std::string actionString = "MoveCustomer " + std::to_string(srcTrainer) + ", " + std::to_string(dstTrainer) + ", " + std::to_string(id) + convertStatus();
    return actionString;

}

MoveCustomer* MoveCustomer::clone() {
    MoveCustomer* newMoveCustomer =  new MoveCustomer(srcTrainer,dstTrainer,id);
    newMoveCustomer->setStatus(getStatus());
    return newMoveCustomer;
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
    std::string actionString = "Close, "  + std::to_string(trainerId) +  convertStatus() ;
    return actionString;
}

Close* Close::clone(){
    Close* newClose =  new Close(trainerId);
    newClose->setStatus(getStatus());
    return newClose;
}


//Close All class:
CloseAll::CloseAll() {}

CloseAll::~CloseAll(){}

void CloseAll::act(Studio &studio) {
    for(int i = 0 ; i<studio.getNumOfTrainers();++i){
        if (studio.getTrainer(i)->isOpen()){
            Close* closeTrainer= new Close(i);
            closeTrainer->act(studio);
            delete closeTrainer;
        }
    }
    complete();
}

std::string CloseAll::toString() const {
    std::string actionString = "CloseAll,"   + convertStatus();
    return actionString;
}
CloseAll* CloseAll::clone(){
    CloseAll* newCloseAll = new CloseAll();
    newCloseAll->setStatus(getStatus());
    return newCloseAll;
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
    std::string actionString = "PrintWorkoutOptions,"   + convertStatus();
    return actionString;
}

PrintWorkoutOptions* PrintWorkoutOptions::clone(){
    PrintWorkoutOptions* newPrintWorkoutOptions = new PrintWorkoutOptions();
    newPrintWorkoutOptions->setStatus(getStatus());
    return newPrintWorkoutOptions;
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
    std::string actionString = "PrintTrainerStatus, "   +std::to_string(trainerId) + convertStatus();
    return actionString;
}

PrintTrainerStatus* PrintTrainerStatus::clone(){
    PrintTrainerStatus* newPrintTrainerStatus=  new PrintTrainerStatus(trainerId);
    newPrintTrainerStatus->setStatus(getStatus());
    return newPrintTrainerStatus;
}

//BackUp Action class:
BackupStudio::BackupStudio() {}

void BackupStudio::act(Studio &studio) {
    backup = new Studio(studio);
    complete();
}

BackupStudio::~BackupStudio() {}

std::string BackupStudio::toString() const {
    std::string actionString = "BackUp,"   +convertStatus();
    return actionString;
}

BackupStudio* BackupStudio::clone(){
    BackupStudio* newBackupStudio = new BackupStudio();
    newBackupStudio->setStatus(getStatus());
    return newBackupStudio;
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
    std::string actionString = "RestoreStudio,"   + convertStatus();
    return actionString;
}

RestoreStudio* RestoreStudio::clone(){
    RestoreStudio* newRestoreStudio = new RestoreStudio();
    newRestoreStudio ->setStatus(getStatus());
    return newRestoreStudio;
}


