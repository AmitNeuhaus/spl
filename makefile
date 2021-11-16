all: test

test:bin/Trainer.o bin/Workout.o bin/Customer.o bin/Studio.o
	g++ -o bin/test bin/Trainer.o bin/Workout.o bin/Customer.o bin/Studio.o
	@echo "Ready"

bin/Studio.o: src/Studio.o
	@echo "compiling Studio"
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Studio.o src/Studio.cpp


bin/Trainer.o: src/Trainer.cpp
	@echo "compiling Trainer"
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Trainer.o src/Trainer.cpp

bin/Workout.o: src/Workout.cpp
	@echo "compiling Workout"
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Workout.o src/Workout.cpp

bin/Customer.o: src/Customer.cpp
	@echo "compiling Workout"
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Customer.o src/Customer.cpp

clean:
	@echo "cleaning bin"
	rm -f bin/*