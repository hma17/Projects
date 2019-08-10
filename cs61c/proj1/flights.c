/*
 * CS61C Summer 2019
 * Name: Mohammad Loqman
 * Login: cs61c-ade
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "flights.h"
#include "flight_structs.h"
#include "timeHM.h"


/*
 *  This should be called if memory allocation failed.
 */
static void allocation_failed(void) {
  fprintf(stderr, "Out of memory.\n");
  exit(EXIT_FAILURE);
}


/*
 *  Creates and initializes a flight system, which stores the flight schedules of several airports.
 *  Returns a pointer to the system created.
 */
flightSys_t* createSystem(void) {
  // Replace this line with your code
  flightSys_t *sys = (flightSys_t*) malloc(sizeof(flightSys_t));
  if(sys == NULL) {
    allocation_failed();
  }
  sys->list = (airportNode_t*) malloc(sizeof(airportNode_t));
  if(sys->list == NULL) {
    allocation_failed();
  }
  sys->list->next = sys->list;
  sys->list->prev = sys->list;
  sys->list->curr = NULL;
  return sys;
}
/*
 *   Given a destination airport, departure and arrival times, and a cost, return a pointer to new flight_t.
 *
 *   Note that this pointer must be available to use even after this function returns.
 *   (What does this mean in terms of how this pointer should be instantiated?)
 *   Additionally you CANNOT assume that the `departure` and `arrival` pointers will persist after this function completes.
 *   (What does this mean about copying dep and arr?)
 */

flight_t* createFlight(airport_t* destination, timeHM_t* departure, timeHM_t* arrival, int cost) {
  // Replace this line with your code
  //set up the destination at the right location
	if (destination == NULL || departure == NULL || arrival == NULL || cost < 0 || isAfter(departure, arrival) == true) {
		return NULL;
	}
  flight_t* flight = (flight_t*) malloc(sizeof(flight_t));
  if(flight == NULL) {
    allocation_failed();
  }
  flight->destination = destination;
  flight->departTime = (timeHM_t*)malloc(sizeof(timeHM_t));
  if(flight->departTime == NULL) {
    allocation_failed();
  }
  // fix theis
  flight->departTime->hours = departure->hours;
  flight->departTime->minutes = departure->minutes;
  flight->arrivalTime = (timeHM_t*) malloc(sizeof(timeHM_t));
  if(flight->arrivalTime == NULL) {
    allocation_failed();
  }
  flight->arrivalTime->hours = arrival->hours;
  flight->arrivalTime->minutes = arrival->minutes;
  flight->costofFlight = cost;
  return flight;
}

/*
 *  Frees all memory associated with this system; that's all memory you dynamically allocated in your code.
 */
void deleteSystem(flightSys_t* system) {
  // Replace this line with your code
  //fix this
	if (system == NULL || system->list == NULL) {
		return;
	}
  airportNode_t* currAirportNode = system->list->next;
  airportNode_t* nextAirportNode = system->list->next->next;
  
  airport_t* currAirport = currAirportNode->curr;

    if (currAirport == NULL) {
  	free(currAirport);
  	free(currAirportNode);
  	free(system);
  	return;
  }


  flightNode_t* currFlightNode = currAirport->list->next;
  flightNode_t* nextFlightNode = currAirport->list->next->next;

  flight_t* currFlight = currFlightNode->curr;

  while(currAirport != NULL) {
    while(currFlight != NULL) {
      deleteFlight(currFlight);
      // free(currFlight);
      free(currFlightNode);
      currFlightNode = nextFlightNode;
      currFlight = currFlightNode->curr;
      nextFlightNode = nextFlightNode->next;
    }
    free(currAirportNode->curr->name);
    free(currAirportNode->curr->list);
    free(currAirportNode->curr);
    free(currAirportNode);
    currAirportNode = nextAirportNode;
    currAirport = currAirportNode->curr;
    nextAirportNode = nextAirportNode->next;
  }
  free(currAirportNode);
  free(system);
}

/*
 *  Frees all memory allocated for a single flight. You may or may not decide
 *  to use this in delete system but you must implement it.
 */
void deleteFlight(flight_t* flight) {
  // Replace this line with your code 
	if (flight == NULL) {
		return;
	}
  free(flight->departTime);
  free(flight->arrivalTime);
  free(flight);
}


/*
 *  Adds a airport with the given name to the system. You must copy the string and store it.
 *  Do not store `name` (the pointer) as the contents it point to may change.
 */
void addAirport(flightSys_t* system, char* name) {

  //set up memory for new airportNode
	if(system == NULL || name == NULL || system->list == NULL || strspn(name, " \r\n\t") == strlen(name)) {
		return;
	}
	if(getAirport(system, name)) {
		return;
	}

  airportNode_t *airportNode = (airportNode_t*) malloc(sizeof(airportNode_t));
  if(airportNode == NULL) {
  	return allocation_failed();
  }
  airportNode->next = airportNode;
  airportNode->prev = airportNode;
  airportNode->curr = (airport_t*) malloc(sizeof(airport_t));
  if(airportNode->curr == NULL) {
  	return allocation_failed();
  }
  airportNode->curr->name = (char*) malloc(sizeof(char) * (strlen(name) + 1));
// need to setup the list as a sentinel linked list of flights.
  if(airportNode->curr->name == NULL) {
  	return allocation_failed();
  }
  strcpy(airportNode->curr->name, name);
  airportNode->curr->list = (flightNode_t*) malloc(sizeof(flightNode_t));
  if(airportNode->curr->list == NULL) {
  	return allocation_failed();
  }
  flightNode_t* sentinel_flight = airportNode->curr->list;
  sentinel_flight->next = sentinel_flight;
  sentinel_flight->prev = sentinel_flight;
  sentinel_flight->curr = NULL;

  // airportNode_t* ptr = system->list;
  // while(ptr->next != NULL) {
  // 	ptr = ptr->next;
  // }
  // ptr->next = airportNode;
  // airportNode->next = NULL; 
  airportNode->prev = system->list->prev;
  airportNode->next = system->list;
  system->list->prev->next = airportNode;
  system->list->prev = airportNode;
}

/*
 *  Returns a pointer to the airport with the given name.
 *  If the airport doesn't exist, return NULL.
 */
airport_t* getAirport(flightSys_t* system, char* name) {
  // Replace this line with your code
	if(system == NULL || name == NULL) {
		return NULL;
	}
  airport_t* currPtr = system->list->next->curr;
  airportNode_t* nextPtr = system->list->next->next;
  
  if(currPtr == NULL) {
    return NULL;
  }

  while (currPtr != NULL) {
    if (strcmp(currPtr->name, name) == 0) {
      return currPtr;
    } else {
      currPtr = nextPtr->curr;
      nextPtr = nextPtr->next;
    }
  }
  return currPtr;
}


/*
 *  Print each airport name in the order they were added through addAirport, one on each line.
 *  Make sure to end with a new line. You should compare your output with the correct output
 *  in `flights.out` to make sure your formatting is correct.
 */

void printAirports(flightSys_t* system) {
  // Replace this line with your code
	if (system == NULL) {
		return;
	}
  airport_t* currPtr = system->list->next->curr;
  airportNode_t* nextPtr = system->list->next->next;
  if (currPtr == NULL)
  {
    return;
  }

  while (currPtr != NULL) {
    printf("%s\n", currPtr->name);
    currPtr = nextPtr->curr;
    nextPtr = nextPtr->next;
  }
}


/*
 *  Adds a flight to source's schedule, stating a flight will leave to destination at departure time and arrive at arrival time.
 */
void addFlight(airport_t* source, airport_t* destination, timeHM_t* departure, timeHM_t* arrival, int cost) {
  if (source == NULL || destination == NULL || departure == NULL || arrival  == NULL|| cost < 0)  {
  	return;
  }
  // || isAfter(departure, arrival) == true
  flight_t* flight = createFlight(destination, departure, arrival, cost);
  flightNode_t* Node = (flightNode_t*) malloc(sizeof(flightNode_t));
  if(Node == NULL) {
    allocation_failed();
  }
  Node->next = Node;
  Node->prev = Node;

  Node->curr = flight;

  Node->next = source->list;
  Node->prev = source->list->prev;
  source->list->prev->next = Node;
  source->list->prev = Node; 
}


/*
 *  Prints the schedule of flights of the given airport.
 *
 *  Prints the airport name on the first line, then prints a schedule entry on each
 *  line that follows, with the format: "destination_name departure_time arrival_time $cost_of_flight".
 *
 *  You should use `printTime()` (look in `timeHM.h`) to print times, and the order should be the same as
 *  the order they were added in through addFlight. Make sure to end with a new line.
 *  You should compare your output with the correct output in flights.out to make sure your formatting is correct.
 */
void printSchedule(airport_t* airport) {
  // Replace this line with your code
	if (airport == NULL || airport->name == NULL) {
    return;
}
flightNode_t* currFlightNode = airport->list->next;
	char* nam = airport->name;
	printf("%s\n",nam);
while(currFlightNode->curr != NULL) {
	flight_t* currFlight = currFlightNode->curr;
	printf("%s", currFlight->destination->name);
	printf(" ");
	printTime(currFlight->departTime);
	printf(" ");
	printTime(currFlight->arrivalTime);
	printf(" ");
	printf("%s","$" );
	printf("%d\n", currFlight->costofFlight);
	currFlightNode = currFlightNode->next; 
}


}


/*
 *   Given a source and destination airport, and the time now, finds the next flight to take based on the following rules:
 *   1) Finds the earliest arriving flight from source to destination that departs after now.
 *   2) If there are multiple earliest flights, take the one that costs the least.
 *
 *   If a flight is found, you should store the flight's departure time, arrival time, and cost in the `departure`, `arrival`,
 *   and `cost` params and return true. Otherwise, return false.
 *
 *   Please use the functions `isAfter()` and `isEqual()` from `timeHM.h` when comparing two timeHM_t objects and compare
 *   the airport names to compare airports, not the pointers.
 */
bool getNextFlight(airport_t* source, airport_t* destination, timeHM_t* now, timeHM_t* departure, timeHM_t* arrival,
                   int* cost) {
  // Replace this lin with your code
	if (destination == NULL || source == NULL || now == NULL){
		return false;
	}	
	char* destination_name = destination->name;
	flight_t* flight_to_take = NULL;
	timeHM_t* best_Time = NULL;
	flightNode_t* currFlightNode = source->list->next;
	while (currFlightNode->curr) {
		if(isAfter(currFlightNode->curr->departTime, now)) {
			if (strcmp(destination_name, currFlightNode->curr->destination->name) == 0) {
				if(flight_to_take == NULL) {
					flight_to_take = currFlightNode->curr;
					best_Time = currFlightNode->curr->departTime;
				}
				else if (isAfter(best_Time, currFlightNode->curr->departTime)) {
					flight_to_take = currFlightNode->curr;
					best_Time = currFlightNode->curr->departTime;
				}
				else if(isEqual(best_Time, currFlightNode->curr->departTime)) {
					if(currFlightNode->curr->costofFlight < flight_to_take->costofFlight) {
						flight_to_take = currFlightNode->curr;}
					}
				}

			}
			currFlightNode = currFlightNode->next;
		}
	if (flight_to_take != NULL) {
	departure->hours = flight_to_take->departTime->hours;
	departure->minutes = flight_to_take->departTime->minutes;
	arrival->hours = flight_to_take->arrivalTime->hours;
	arrival->minutes = flight_to_take->arrivalTime->minutes;
	*cost = flight_to_take->costofFlight;
	return true;
}
  return false;
}

/*
 *  Given a list of flight_t pointers (`flight_list`) and a list of destination airport names (`airport_name_list`),
 *  first confirm that it is indeed possible to take these sequences of flights, (i.e. be sure that the i+1th flight departs
 *  after or at the same time as the ith flight arrives) (HINT: use the `isAfter()` and `isEqual()` functions).
 *  Then confirm that the list of destination airport names match the actual destination airport names of the provided flight_t structs.
 *
 *  `size` tells you the number of flights and destination airport names to consider. Be sure to extensively test for errors.
 *  As one example, if you encounter NULL's for any values that you might expect to be non-NULL return -1, but test for other possible errors too.
 *
 *  Return from this function the total cost of taking these sequence of flights.
 *  If it is impossible to take these sequence of flights,
 *  if the list of destination airport names doesn't match the actual destination airport names provided in the flight_t struct's,
 *  or if you run into any errors mentioned previously or any other errors, return -1.
 */
int validateFlightPath(flight_t** flight_list, char** airport_name_list, int size) {
  // Replace this line with your code]
	if (flight_list == NULL || airport_name_list == NULL || size < 0) {
		return -1;
	}
	int total_cost = 0;
	flight_t* currFlight; 
	flight_t* nextFlight;
	int i = 1;
	if(flight_list == NULL || airport_name_list ==  NULL || size == 0) {
		return -1;
	}
	for (i = 1; i < size; i++) {
		currFlight = flight_list[i-1];
		nextFlight = flight_list[i];
		total_cost = total_cost + currFlight->costofFlight;
		if (isAfter(nextFlight->departTime, currFlight->departTime) || isEqual(nextFlight->departTime, currFlight->departTime)) {
			if (strcmp(currFlight->destination->name, airport_name_list[i-1]) == 0) {
				total_cost = total_cost + nextFlight->costofFlight;
			}
		} else {
			return -1;}
	} if (total_cost != -1) {
		return total_cost;
	}
	return -1;
}

