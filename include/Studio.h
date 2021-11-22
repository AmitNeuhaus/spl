#ifndef STUDIO_H_
#define STUDIO_H_

#include <vector>
#include <string>
#include <sstream>
#include <iostream>
#include <fstream>
#include "Workout.h"
#include "Trainer.h"
#include "Action.h"


class Studio{		
public:
	Studio();
    Studio(const std::string &configFilePath);
    Studio(const Studio& studio);
    Studio& operator=(Studio& studio);
    Studio(Studio&& studio);
    Studio& operator=(Studio&& studio);
    virtual ~Studio();
    void start();
    int getNumOfTrainers() const;
    Trainer* getTrainer(int tid);
	const std::vector<BaseAction*>& getActionsLog() const; // Return a reference to the history of actions
    std::vector<Workout>& getWorkoutOptions();
    std::vector<std::string> getUserCommand();
    Customer* createCustomer(std::string name, std::string strategy, int id);
    std::vector<std::string> splitNameAndStrategy(std::string nameAndStrategy);
    std::vector<Customer *> customersStudio;
private:
    bool open;
    std::vector<Trainer*> trainers;
    std::vector<Workout> workout_options;
    std::vector<BaseAction*> actionsLog;
    int customersIdCounter = 0;
    void Clean();
    void Copy(const Studio& studio);
    void Steel(Studio& studio);
};

#endif