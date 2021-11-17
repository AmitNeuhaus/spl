#ifndef TRAINER_H_
#define TRAINER_H_

#include <vector>
#include "Customer.h"
#include "Workout.h"

typedef std::pair<int, Workout> OrderPair;
//
class Trainer{
public:
    Trainer(int t_capacity);
    Trainer(const Trainer& _trainer);
    Trainer& operator=(Trainer& _trainer);
    Trainer(Trainer&& _trainer);
    Trainer& operator=(Trainer&& _trainer);
    ~Trainer();
    int getCapacity() const;
    void addCustomer(Customer* customer);
    void removeCustomer(int id);
    Customer* getCustomer(int id);
    std::vector<Customer*>& getCustomers();
    std::vector<OrderPair>& getOrders();
    void order(const int customer_id, const std::vector<int> workout_ids, const std::vector<Workout>& workout_options);
    void openTrainer();
    void closeTrainer();
    int getSalary();
    bool isOpen();
    bool isFull();
    std::string toString();
private:
    Customer* createCustomer(Customer *customer);
    void Copy(const Trainer& _trainer);
    void Clean();
    void Steel(Trainer &_trainer);
    int capacity;
    bool open;
    int salary;
    std::vector<Customer*> customersList;
    std::vector<OrderPair> orderList; //A list of pairs for each order for the trainer - (customer_id, Workout)


};


#endif