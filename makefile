# All Targets
all: studio

studio: bin/Customer.o bin/Workout.o bin/Action.o bin/Trainer.o bin/Studio.o bin/main.o
	g++ -o studio bin/Customer.o bin/Workout.o bin/Action.o bin/Trainer.o bin/Studio.o bin/main.o

bin/main.o: src/main.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o  bin/main.o src/main.cpp

bin/Customer.o: include/Customer.h src/Customer.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Customer.o src/Customer.cpp

# Tool invocations
bin/Workout.o: include/Workout.h src/Workout.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Workout.o src/Workout.cpp

bin/Action.o: include/Action.h src/Action.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Action.o src/Action.cpp


bin/Studio.o: include/Studio.h src/Studio.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Studio.o src/Studio.cpp

bin/Trainer.o: src/Trainer.cpp
	@echo "compiling Trainer"
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Trainer.o src/Trainer.cpp

#Clean the build directory
clean:
	@echo "cleaning bin"
	rm -f bin/*