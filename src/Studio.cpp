//
// Created by AMIT Neuhaus on 08/11/2021.
//

#include "../include/Studio.h"


Studio::Studio():open(false),trainers(std::vector<Trainer*>()), workout_options(std::vector<Workout>()),actionsLog(std::vector<BaseAction*>()) {}

Studio::Studio(const std::string &configFilePath):open(false),trainers(std::vector<Trainer*>()), workout_options(std::vector<Workout>()),actionsLog(std::vector<BaseAction*>())  {
    std::ifstream file(configFilePath);
    std::string line;
    std::string substr;
    int lineCounter = 0;
    int w_id = 0;
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
                    Workout newWorkout = Workout(w_id,name,price,type);
                    workout_options.push_back(newWorkout);
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
    open = true;



    std::ifstream file("../textinput.txt");
    bool fileinput=false;
    while (open) {
        std::vector<std::string> command;
        // commands from file---------------------
        if(fileinput){
            std::string line;
            std::getline(file, line);
            std::stringstream commandStream(line);
            std::vector<std::string> out;
            std::string s;
            while (std::getline(commandStream, s, ' ')) {
                out.push_back(s);
            }
            command = out;
        }else {
            command = getUserCommand();
        }
        std::string action = command[0];

        if(action=="file"){
            fileinput = true;
        }

        // commands from file---------------------

//        std::vector<std::string> command = getUserCommand();
//        std::string action = command[0];
                if (action == "open") {
                    int trainerId = std::stoi( command[1] );
                    // open a customer list on stack
                    std::vector<Customer *> customersList = std::vector<Customer *>();
                    int amountOfCustomers = command.size() - 2 ;// number of parameters excluding action and trainer Id (divide 2 because of workout types)
                    Trainer* trainerRef = getTrainer(trainerId);
                    int maxCustomers = -1;
                    if (trainerRef != nullptr) {
                        int trainerCapacity = trainerRef->getCapacity();
                        maxCustomers = trainerCapacity < amountOfCustomers ? trainerCapacity : amountOfCustomers;
                    }
                    for (int i = 0; i < amountOfCustomers; i++) {
                        std::vector<std::string> nameAndStrategy = splitNameAndStrategy(command[i + 2]);
                        std::string name = nameAndStrategy[0];
                        std::string strategy = nameAndStrategy[1];
                        Customer *newCustomer;
                        if(maxCustomers<0 || i>=maxCustomers){
                            newCustomer = createCustomer(name, strategy, -1);
                        }else{
                            newCustomer = createCustomer(name, strategy, customersIdCounter);
                            customersIdCounter++;
                        }
                        customersList.push_back(newCustomer);
                    }
                    OpenTrainer *openTrainerInstance = new OpenTrainer(trainerId, customersList);
                    openTrainerInstance -> act(*this);
                    //TODO: think if need to check that act completed successfully.
                    actionsLog.push_back(openTrainerInstance);
                }
                else if (action == "order" ) {
                    int trainerId = std::stoi( command[1] );
                    Order *orderInstance = new Order(trainerId);
                    orderInstance->act(*this);
                    //TODO: think if need to check that act completed successfully.
                    actionsLog.push_back(orderInstance);
                }
                else if (action == "move") {
                    int originTrainerId = std::stoi( command[1] );
                    int destinationTrainerId = std::stoi( command[2] );
                    int customerId = std::stoi( command[3] );
                    MoveCustomer *moveCustomerInstance = new MoveCustomer(originTrainerId,destinationTrainerId,customerId);
                    moveCustomerInstance->act(*this);
                    actionsLog.push_back(moveCustomerInstance);
                }
                else if (action == "close") {
                    int trainerId = std::stoi( command[1] );
                    Close *closeInstance = new Close(trainerId);
                    closeInstance->act(*this);
                    actionsLog.push_back(closeInstance);
                }
                else if (action == "closeall") {
                    CloseAll *closeAllInstance = new CloseAll();
                    closeAllInstance->act(*this);
                    actionsLog.push_back(closeAllInstance);
                    open = false;
                }
                else if( action == "workout_options"){
                    PrintWorkoutOptions *printWorkoutOptionsInstance = new PrintWorkoutOptions();
                    printWorkoutOptionsInstance->act(*this);
                    actionsLog.push_back(printWorkoutOptionsInstance);
                }
                else if (action == "status") {
                    int trainerId = std::stoi( command[1] );
                    PrintTrainerStatus *printTrainerStatusInstance = new PrintTrainerStatus(trainerId);
                    printTrainerStatusInstance->act(*this);
                    actionsLog.push_back(printTrainerStatusInstance);
                }
                else if (action == "log") {
                    PrintActionsLog *printActionsLogInstance = new PrintActionsLog();
                    printActionsLogInstance->act(*this);
                    actionsLog.push_back(printActionsLogInstance);
                }
                else if (action == "backup") {
                    BackupStudio *backupStudioInstance = new BackupStudio();
                    backupStudioInstance->act(*this);
                    actionsLog.push_back(backupStudioInstance);
                }
                else if (action == "restore") {
                    RestoreStudio *restoreStudioInstance = new RestoreStudio();
                    restoreStudioInstance->act(*this);
                    actionsLog.push_back(restoreStudioInstance);
                }
                else {
                    std::cout << "unknown action" + action << std::endl;
                }
            }
        }



int Studio::getNumOfTrainers() const {
    return trainers.size();
}


Trainer *Studio::getTrainer(int tid) {
    if (tid>int(trainers.size()-1)){
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

//Rule of 5:

//copy c-tor:
Studio::Studio(const Studio &studio):open(false),trainers(std::vector<Trainer*>()), workout_options(std::vector<Workout>()),actionsLog(std::vector<BaseAction*>())  {
    Copy(studio);
}

Studio &Studio::operator=(Studio &studio) {
    if (this!=&studio){
        Clean();
        Copy(studio);
    }
    return (*this);
}

Studio::Studio(Studio &&studio):open(false),trainers(std::vector<Trainer*>()), workout_options(std::vector<Workout>()),actionsLog(std::vector<BaseAction*>())  {
    Steel(studio);
}

Studio &Studio::operator=(Studio &&studio) {
    if (this!=&studio){
        Clean();
        Steel(studio);
    }
    return (*this);
}

Studio::~Studio() {
    Clean();
}

void Studio::Clean() {
    for(Trainer* trainer : trainers){
        delete trainer;
    }
    for(BaseAction* action : actionsLog){
        delete action;
    }
    trainers.clear();
    workout_options.clear();
    actionsLog.clear();
}

void Studio::Copy(const Studio& studio) {
    customersIdCounter = studio.customersIdCounter;
    for(Trainer* trainer : studio.trainers){
        Trainer* copyTrainer = new Trainer(*trainer);
        trainers.push_back(copyTrainer);
    }
    for(Workout workout : studio.workout_options){
        Workout copyWorkout  =  Workout(workout.getId(),workout.getName(),workout.getPrice(),workout.getType());
        workout_options.push_back(copyWorkout);
    }
    for(BaseAction* action : studio.actionsLog){
        BaseAction* copyAction = action->clone();
        actionsLog.push_back(copyAction);
    }
}

void Studio::Steel(Studio &studio) {
    trainers=std::move(studio.trainers);
    studio.trainers.clear();
    workout_options = std::move(studio.workout_options);
    studio.workout_options.clear();
    actionsLog = std::move(studio.actionsLog);
    studio.actionsLog.clear();
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

std::vector<std::string> Studio::splitNameAndStrategy(std::string nameAndStrategy){
    std::stringstream commandStream(nameAndStrategy);
    std::vector<std::string> out;
    std::string s;
    while (std::getline(commandStream, s, ',')) {
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

