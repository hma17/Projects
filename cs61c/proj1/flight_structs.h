/*
 * CS61C Summer 2019
 * Name: Mohammad Loqman
 * Login: cs61c-ade
 */

#ifndef FLIGHT_STRUCTS_H
#define FLIGHT_STRUCTS_H

#include "timeHM.h"

typedef struct flightSys flightSys_t;
typedef struct airport airport_t;
typedef struct flight flight_t;
typedef struct flightNode flightNode_t;
typedef struct airportNode airportNode_t;


struct airportNode {
	//  a linked list of aiports
	airport_t *curr;
	struct airportNode *next;
	struct airportNode *prev;
};
struct flightNode {
	// a linked list is flights
	flight_t *curr;
	struct flightNode *next;
	struct flightNode *prev;
};

struct flightSys {
	airportNode_t *list;
  // Place the members you think are necessary for the flightSys struct here.
};

struct airport {
  // Place the members you think are necessary for the airport struct here.
	char *name;
	flightNode_t *list;

};

struct flight {
  // Place the members you think are necessary for the flight struct here.
	airport_t* destination;
	timeHM_t* departTime;
	timeHM_t* arrivalTime;
	int costofFlight;
};

#endif

