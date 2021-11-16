//
// Created by tomcooll on 08/11/2021.
//
#include "Trainer.h"
#include <iostream>

//Constructor
Trainer::Trainer(int t_capacity):capacity(t_capacity),open(false),customersList(std::vector<Customer*>()),orderList(std::vector<OrderPair>()){}

//Public methods:
int Trainer::getCapacity() const{
    return capacity;
}

void Trainer::addCustomer(Customer* customer){
    customersList.push_back(customer);
}

void Trainer::removeCustomer(int id){
    for (std::vector<Customer*>::iterator customer = customersList.begin();customer!=customersList.end();) {
        if ((*customer)->getId() == id){
            customersList.erase(customer);
        }else{
            ++customer;
        }
    }


}

Customer* Trainer::getCustomer(int id){
    for (Customer* customer: customersList) {
        if (customer->getId() == id){
            return customer;
        }
    }
    return nullptr;
}

std::vector<Customer*>& Trainer::getCustomers(){
    return customersList;
}

std::vector<OrderPair>& Trainer::getOrders(){
    return orderList;
}

void Trainer::order(const int customer_id, const std::vector<int> workout_ids, const std::vector<Workout>& workout_options){
    for(int i : workout_ids){
        orderList.push_back(OrderPair(customer_id,workout_options[i]));
    }
}

void Trainer::openTrainer(){
    open = true;
}

void Trainer::closeTrainer(){
    open = false;
}

int Trainer::getSalary(){
    int salary = 0;
    for(const OrderPair& pair : orderList){
        const int workoutPrice = pair.second.getPrice();
        salary = salary + workoutPrice;
    }
    return salary;
}

bool Trainer::isOpen(){
    return open;
}




//Rule of 5:

//d-tor:
Trainer::~Trainer(){
    Clean();
}


//copy c-tor
Trainer::Trainer(const Trainer& _trainer){
    Copy(_trainer);
}


//ass-op
Trainer& Trainer::operator=(Trainer& _trainer){
    if(this != &_trainer){
        Clean();
        Copy(_trainer);
    }
    return (*this);
}
//move c-tor
Trainer::Trainer(Trainer &&_trainer) {
    Steel(_trainer);

}

//move ass-op
Trainer& Trainer::operator=(Trainer &&_trainer) {
    Clean();
    Steel(_trainer);
    return *this;
}

//private methods

void Trainer::Copy(const Trainer& _trainer){
    capacity = _trainer.getCapacity();
    open = _trainer.open;
    //todo: fix copy for costumer list vector;
    customersList = _trainer.customersList;
    for (OrderPair i : orderList) {
        OrderPair newPair= i;
        orderList.push_back(newPair);
    }
}


void Trainer::Clean(){

    capacity = -1;
    open = false;
    //todo: delete each object in costumer list;
    customersList.clear();
    orderList.clear();

}

void Trainer::Steel(Trainer &_trainer) {
    capacity = _trainer.capacity;
    open = _trainer.open;
    customersList = std::move(_trainer.customersList);
    orderList = std::move(_trainer.orderList);
    _trainer.capacity = -1;
    _trainer.open = false;
}

std::string Trainer::toString() {
    return std::to_string(capacity);
}

bool Trainer::isFull() {
    return capacity = customersList.size();
}



////TO DELETE:
//int main(int argc,char** argv){
//    Trainer* T1 = new Trainer(7);
//    Customer* c1 = new SweatyCustomer("amit", 4);
//    std::cout << "print capacity of trainer should be 7"<<std::endl;
//    std::cout << T1->getCapacity()<<std::endl;
//    T1->addCustomer(c1);
//    std::cout << "print customer name should be amit"<<std::endl;
//    std::cout <<"Customer name is: "<< T1->getCustomer(4)->getName()<<std::endl;
//    T1->removeCustomer(4);
//    std::cout << "removed customer and for vector.empty should be true"<<std::endl;
//    if(T1->getCustomers().empty()){
//        std::cout << "true"<<std::endl;
//
//    }else{
//        std::cout <<"false" <<std::endl;
//
//    }
//
//    delete(T1);
//    delete(c1);
//}








