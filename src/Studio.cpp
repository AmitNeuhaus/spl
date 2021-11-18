//
// Created by AMIT Neuhaus on 08/11/2021.
//

#include "../include/Studio.h"


Studio::Studio() = default ;

Studio::Studio(const std::string &configFilePath) {
    std::ifstream file(configFilePath);
    std::string line;
    std::string substr;
    int lineCounter = 0;
    if (file.is_open()){
        while(getline(file,line)){
            if (line[0] != '#'){
                std::stringstream s_line(line);
                if (lineCounter == 0){
                    lineCounter++;
                }

                else if (lineCounter == 1){
                    while (s_line.good()){
                        std::getline(s_line,substr,',');
                        if (substr[0]==' '){
                            substr = substr.substr(1,substr.length()-1);
                        }
                        int capacity = stoi(substr);
                        Trainer* newTrainer = new Trainer(capacity);
                        trainers.push_back(newTrainer);
                    }
                    lineCounter++;
                }
                else{
                    std::string name;
                    WorkoutType type;
                    int price;
                    int w_id = 0;
                    int counter=0;
                    while (s_line.good()){
                        std::getline(s_line,substr,',');
                        if (substr[0]==' '){
                            substr = substr.substr(1,substr.length()-1);
                        }
                        if (counter == 0){
                            name = substr;
                            counter++;
                        }
                        else if (counter == 1){
                            if (substr == "Anaerobic"){
                                type = ANAEROBIC;
                            }
                            else if(substr == "Mixed"){
                                type = MIXED;
                            }
                            else if(substr == "Cardio"){
                                type = CARDIO;
                            }
                            counter++;
                        }
                        else{
                            price = std::stoi(substr);
                        }

                    }
                    Workout* newWorkout = new Workout(w_id,name,price,type);
                    workout_options.push_back(*newWorkout);
                    counter = 0;
                    w_id++;
                }
            }
        }
    }

    else{
        std::cout<< "ERROR: file did not open"<<std::endl;
    }


    file.close();

}

void Studio::start() {
        // TODO: parsing the first word in the command line  == action
        std::cout << "Studio is now open!" << std::endl;
        bool studioIsOpen = true;


    while (studioIsOpen) {
        std::vector<std::string> command = getUserCommand();
        std::string action = command[0];
                if (action == "open") {
                    int trainerId = std::stoi( command[1] );
                    // open a customer list on stack
                    std::vector<Customer *> customersList = std::vector<Customer *>();
                    int amountOfCustomers = command.size() - 2 ;// number of parameters excluding action and trainer Id (divide 2 because of workout types)
                    int customerId = 0;
                    for (int i=0; i < amountOfCustomers; i=i+2){
                        customersList.push_back(createCustomer(command[i+2], command[i+3], customerId));
                        customerId++;
                    }
                    OpenTrainer *openTrainerInstance = new OpenTrainer(trainerId, customersList);
                    openTrainerInstance -> act(*this);
                    //TODO: think if need to check that act completed successfully.
                    delete openTrainerInstance;
                }
                else if (action == "order" ) {
                    int trainerId = std::stoi( command[1] );
                    Order *orderInstance = new Order(trainerId);
                    orderInstance->act(*this);
                    //TODO: think if need to check that act completed successfully.
                    delete orderInstance;
                }
                else if (action == "closeAll") {
                    studioIsOpen = false;
                }
                else {
                    std::cout << action << std::endl;
                }
            }
        }




int Studio::getNumOfTrainers() const {
    return trainers.size();
}

Trainer *Studio::getTrainer(int tid) {
    Trainer* t = trainers[tid];
    return t;
}

const std::vector<BaseAction *> &Studio::getActionsLog() const {
    return actionsLog;
}

std::vector<Workout> &Studio::getWorkoutOptions() {
    return workout_options;
}


//std::vector<std::string>& getUserCommand(){

// helpers --------------------------------------

Customer* Studio::createCustomer(std::string name, std::string strategy, int id){
    if(strategy == "swt"){
        return new SweatyCustomer(name, id);
    }else if (strategy =="chp"){
        return new CheapCustomer(name, id);
    }else if (strategy =="mcl"){
        return new HeavyMuscleCustomer(name, id);
    } else {
        return  new FullBodyCustomer(name, id);
    }
}


std::vector<std::string> Studio::getUserCommand(){
    std::string command;
    std::getline(std::cin, command);
    std::stringstream commandStream(command);
    std::vector<std::string> out;
    std::string s;
    while (std::getline(commandStream, s, ' ')) {
        out.push_back(s);
    }
    return out;
}

//int main(int argc,char** argv){
//    std::string fileName;
//    std::cout<<"----enter file name----: "<<std::endl;
//    std::cin>>fileName;
//    Studio s(fileName);
//    std::cout<<"------number of trainers in studio-------"<<std::endl;
//    std::cout<<s.getNumOfTrainers()<<std::endl;
//    std::cout<<"------print all workouts in the studio-------"<<std::endl;
//    for (Workout i : s.getWorkoutOptions()) {
//        std::cout<<i.toString()<<std::endl;
//    }
//
//    std::cout<<"-----print trainers id and capacity"<<std::endl;
//    for(int i =0;i<s.getNumOfTrainers();i++){
//        std::cout << std::to_string(i) << ": " <<s.getTrainer(i)->toString() <<std::endl;
//    }
//
//}

