# All Targets
all: studio


studio: bin/Customer.o bin/Workout.o
	g++ -o studio bin/Customer.o bin/Workout.o bin/Action.o

bin/Customer.o: include/Customer.h src/Customer.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Customer.o src/Customer.cpp

# Tool invocations
bin/Workout.o: include/Workout.h src/Workout.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Workout.o src/Workout.cpp

bin/Action.o: include/Action.h src/Action.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Action.o src/Action.cpp

bin/Trainer.o: src/Trainer.cpp
	@echo "compiling Trainer"
	g++ -g -Wall

#Clean the build directory
clean:
	@echo "cleaning bin"
	rm -f bin/*