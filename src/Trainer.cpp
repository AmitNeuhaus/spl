
#include "../include/Trainer.h"

//Class Trainer:

//Public Methods:
//Constructor
Trainer::Trainer(int t_capacity):capacity(t_capacity),open(false),salary(0),customersList(std::vector<Customer*>()),orderList(std::vector<OrderPair>()){}


//Add a new customer to the customers list.
void Trainer::addCustomer(Customer* customer){
    customersList.push_back(customer);
}

//Removes a customer from the customers list.
//input:customerId
void Trainer::removeCustomer(int id){
    for (std::vector<Customer*>::iterator customer = customersList.begin();customer!=customersList.end();) {
        if ((*customer)->getId() == id){
            customersList.erase(customer);
//            delete *customer;
        }else{
            ++customer;
        }
    }

    std::vector<OrderPair> newOrderList;
    for (OrderPair pair: orderList) {
        OrderPair p = pair;
        if (p.first != id){
            newOrderList.push_back(p);
        }else{
            salary = salary - p.second.getPrice();
        }
    }
    orderList.clear();
    orderList = std::move(newOrderList);
}


//Return: a reference to a list of pairs (customerId,Workout) representation of the trainers orders.
std::vector<OrderPair>& Trainer::getOrders(){
    return orderList;
}

//Submit an order to the customer based on his strategy and updates the trainers salary.
//input: Customer Id, Workouts Id List, Workout options list.
void Trainer::order(const int customer_id, const std::vector<int> workout_ids, const std::vector<Workout>& workout_options){
    for(int i : workout_ids){
        orderList.push_back(OrderPair(customer_id,workout_options[i]));
        salary = salary + workout_options[i].getPrice();
    }
}

//Change trainer status to open.
void Trainer::openTrainer(){
    open = true;
}

//Change trainer status to close, clear his customers and orders.
void Trainer::closeTrainer(){
    open = false;
    for (Customer* customer: customersList) {
        delete customer;
    }
    customersList.clear();
    orderList.clear();
}

//Return true if the trainer is fully booked by customers.
bool Trainer::isFull() {
    return capacity == int(customersList.size());
}


//Getters:
//Return: integer trainer max capacity.
int Trainer::getCapacity() const{
    return capacity;
}

//Return a pointer to a specific customer.
//input: integer Customer Id.
//return: pointer to the customer if existed, nullptr otherwise.
Customer* Trainer::getCustomer(int id){
    for (Customer* customer: customersList) {
        if (customer->getId() == id){
            return customer;
        }
    }
    return nullptr;
}

//Return: a reference to the trainers customers list.
std::vector<Customer*>& Trainer::getCustomers(){
    return customersList;
}

//Return the trainers current salary.
//return: integer capacity.
int Trainer::getSalary(){
    return salary;
}

//Return true if the trainer is already open
bool Trainer::isOpen(){
    return open;
}



//Rule of 5:

//d-tor:
Trainer::~Trainer(){
    Clean();
}


//copy c-tor
Trainer::Trainer(const Trainer& _trainer):capacity(0),open(false),salary(0),customersList(std::vector<Customer*>()),orderList(std::vector<OrderPair>()){
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
Trainer::Trainer(Trainer &&_trainer):capacity(0),open(false),salary(0),customersList(std::vector<Customer*>()),orderList(std::vector<OrderPair>()) {
    Steel(_trainer);

}

//move ass-op
Trainer& Trainer::operator=(Trainer &&_trainer) {
    Clean();
    Steel(_trainer);
    return *this;
}


//Private Methods
//Create a new copy of a trainer.
void Trainer::Copy(const Trainer& _trainer){
    capacity = _trainer.capacity;
    open = _trainer.open;
    salary = _trainer.salary;
    //todo: fix copy for costumer list vector;
    for (Customer* customer : _trainer.customersList) {Customer* newCustomer = customer->clone();
        customersList.push_back(newCustomer);
    }
    for (OrderPair i : orderList) {
        OrderPair newPair= i;
        orderList.push_back(newPair);
    }
}


//Clean all fields and heap blocks used by this trainer.
void Trainer::Clean(){
    salary = -1;
    capacity = -1;
    open = false;
    for (Customer* customer: customersList) {
        delete customer;
    }
    customersList.clear();
    orderList.clear();

}

//Transfer all heap blocks from other trainer to this trainer and copy all primitive fields.
void Trainer::Steel(Trainer &_trainer) {
    salary = _trainer.salary;
    capacity = _trainer.capacity;
    open = _trainer.open;
    customersList = std::move(_trainer.customersList);
    orderList = std::move(_trainer.orderList);
    _trainer.capacity = -1;
    _trainer.open = false;
}


//Helpers.

//Creates a copy of a customer.
//input: a reference to a customer.
//return:a pointer to new instance of customer on the heap.
Customer *Trainer::createCustomer(Customer *customer) {
    std::string customerType = customer->toString();
    if (customerType.compare("SweatyCustomer")){
        SweatyCustomer* newCustomer = new SweatyCustomer(customer->getName(),customer->getId());
        return newCustomer;
    }
    else if (customerType.compare("CheapCustomer")){
        CheapCustomer* newCustomer = new CheapCustomer(customer->getName(),customer->getId());
        return newCustomer;
    }else if (customerType.compare("HeavyMuscleCustomer")){
        HeavyMuscleCustomer* newCustomer = new HeavyMuscleCustomer(customer->getName(),customer->getId());
        return newCustomer;
    }else{
        FullBodyCustomer* newCustomer = new FullBodyCustomer(customer->getName(),customer->getId());
        return newCustomer;
    }
}









