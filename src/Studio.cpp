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
        std::string action = "Hey";
        while (studioIsOpen) {
                if (action == "open") {
                    // TODO: parse customers from command ,and trainer id
                    // create new customers instances list
                    int parsedId = 7;
                    std::vector<Customer *> customersList = std::vector<Customer *>();
                    OpenTrainer *openTrainerInstance = new OpenTrainer(parsedId, customersList);
                    openTrainerInstance -> act(*this);
                    //TODO: think if need to check that act completed successfully.
                    delete openTrainerInstance;
                }
                else if (action == "order" ) {
                    //parse Trainer ID
                    int parsedId = 7;
                    Order *orderInstance = new Order(parsedId);
                    orderInstance->act(*this);
                    //TODO: think if need to check that act completed successfully.
                    delete orderInstance;
                }
                else if (action == "closeAll") {
                    studioIsOpen = false;
                }
                else {
                }
            }
        }




int Studio::getNumOfTrainers() const {
    return trainers.size();
}

Trainer *Studio::getTrainer(int tid) {
    if (tid>trainers.size()-1){
        return nullptr;
    }
    Trainer* t = trainers[tid];
    return t;
}

const std::vector<BaseAction *> &Studio::getActionsLog() const {
    return actionsLog;
}

std::vector<Workout> &Studio::getWorkoutOptions() {
    return workout_options;
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

