# All Targets
all: studio


studio: bin/Customer.o bin/Workout.o
	g++ -o studio bin/Customer.o bin/Workout.o


bin/Customer.o: include/Customer.h src/Customer.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Customer.o src/Customer.cpp

# Tool invocations
bin/Workout.o: include/Workout.h src/Workout.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Workout.o src/Workout.cpp


## This will be the final make file ------------------------
#studio: bin/main.o bin/Action.o bin/Customer.o bin/Studio.o bin/Trainer.o bin/Workout.o
#	g++ -o studio bin/main.o bin/Action.o bin/Customer.o bin/Studio.o bin/Trainer.o bin/Workout.o
##
#bin/main.o: src/main.cpp
#	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/main.o src/main.cpp
#
#bin/Action.o: include/Action.h
#	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Action.o src/Action.cpp
#
#bin/Customer.o: include/Customer.h
#	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Customer.o src/Customer.cpp
#
#bin/Studio.o: include/Studio.h
#	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Studio.o src/Studio.cpp
#
#bin/Trainer.o: include/Trainer.h
#	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Trainer.o src/Trainer.cpp
#
#bin/Workout.o: include/Workout.h
#	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Workout.o src/Workout.cpp

#Clean the build directory
clean:
	rm -f bin/*