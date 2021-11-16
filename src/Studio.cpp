//
// Created by AMIT Neuhaus on 08/11/2021.
//
# include "../include/Studio.h"
# include "../include/Action.h"

void Studio::start() {
    // TODO: parsing the first word in the command line  == action
    std::cin << "Studio is now open!" << std::cout
    bool studioIsOpen = true;
    while (studioIsOpen) {
        switch (action) {
            case "open":
                // TODO: parse customers from command ,and trainer id
                // create new customers instances list
                OpenTrainer *openTrainerInstance = new OpenTrainer(id, customerList);
                openTrainerInstance->act(this)
                //TODO: think if need to check that act completed successfully.
                delete openTrainerInstance;
            case "order":
                //parse Trainer ID
                Order *orderInstance = new Order(trainerId);
                orderInstance->act(this);
                //TODO: think if need to check that act completed successfully.
                delete orderInstance;
            case "closeAll":
                studioIsOpen = false;

            default:
                std::cin << "No action recognized" << std::cout
        }
    }
}