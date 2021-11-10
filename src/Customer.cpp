#include "../include/Customer.h"

#include <iostream>
using namespace std;



//Customer ---------------------------------
Customer::Customer(std::string c_name, int c_id):name(c_name),id(c_id){};

//order and toString are virtual therefore i don't need to implement them here
std::string Customer::getName() const {
    return name;
}
int Customer::getId() const {
    return id;
}





// SweatyCustomer ------------------------
SweatyCustomer::SweatyCustomer(std::string name, int id):Customer(name,id){};
std::vector<int> SweatyCustomer::order(const std::vector<Workout> &workout_options){
    for(std::vector<Workout>::size_type i = 0; i != workout_options.size(); i++){
        std::cout << workout_options[i].getName()<< std::endl;
    }
};

std::string SweatyCustomer::toString() const {
    return "SweatyCustomer";
}

//CheapCustomer --------------------------
//CheapCustomer::CheapCustomer(std::string name, int id):Customer(name,id){};
//
////HeavyMuscleCustomer --------------------
//HeavyMuscleCustomer::HeavyMuscleCustomer(std::string name, int id):Customer(name,id){};
//
////FullBodyCustomer -----------------------
//FullBodyCustomer::FullBodyCustomer(std::string name, int id):Customer(name,id){};




int main(int argc, char** argv) {
    SweatyCustomer amit = SweatyCustomer("amit", 4);
    Workout running = Workout( 1, "running",  15, ANAEROBIC);

    //this is a workouts pointer
    std::vector<Workout> *workouts = new vector<Workout>{running};

    //passing the workouts object itself, the function gets it by reference.
    amit.order(*workouts);
    return 3;
}

