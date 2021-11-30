
#include "../include/Workout.h"

//Class Workout:
//Public Methods:

//Constructor:
Workout::Workout(int w_id, std::string w_name, int w_price, WorkoutType w_type):id(w_id),name(w_name),price(w_price),type(w_type){};

//Getters: return Id/Name/Price/Type:
int Workout::getId() const {
    return id;
}

std::string Workout::getName() const {
    return name;
}

int Workout::getPrice() const {
    return price;
}

WorkoutType Workout::getType() const {
    return type;
}
//Return a String representation of the Workout instance.
std::string Workout::toString() {
    std::string ans = name+ ", " +std::to_string(type)+ ", "+ std::to_string(price);
    return ans;
}

