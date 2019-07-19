# valueIterationAgents.py
# -----------------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


# valueIterationAgents.py
# -----------------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


import mdp, util

from learningAgents import ValueEstimationAgent
import collections

class ValueIterationAgent(ValueEstimationAgent):
    """
        * Please read learningAgents.py before reading this.*

        A ValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs value iteration
        for a given number of iterations using the supplied
        discount factor.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 100):
        """
          Your value iteration agent should take an mdp on
          construction, run the indicated number of iterations
          and then act according to the resulting policy.

          Some useful mdp methods you will use:
              mdp.getStates()
              mdp.getPossibleActions(state)
              mdp.getTransitionStatesAndProbs(state, action)
              mdp.getReward(state, action, nextState)
              mdp.isTerminal(state)
        """
        self.mdp = mdp
        self.discount = discount
        self.iterations = iterations
        self.values = util.Counter() # A Counter is a dict with default 0
        self.maxValActionDic = {}
        for i in mdp.getStates():
            self.maxValActionDic[i] = None
        self.runValueIteration()


    def runValueIteration(self):
        # Write value iteration code here
        "*** YOUR CODE HERE ***"

        # update the values in the dictionary at each iteration
        # update all states per iteration
        # for given state check all action Qvalues and update accordingly
        # for given state have a max Qvalue
        # for given state have best action
        # if the state is terminal the action and value remain same with equivalence there.
        for x in range(self.iterations):
            currDict = util.Counter()
            for state in self.mdp.getStates():
                maxValForStates = -99999999
                for action in self.mdp.getPossibleActions(state):
                    qvalue = self.computeQValueFromValues(state, action)
                    if qvalue > maxValForStates:
                        maxValForStates = qvalue
                        currDict[state] = qvalue
                        self.maxValActionDic[state] = action
            self.values = currDict
        return


    def getValue(self, state):
        """
          Return the value of the state (computed in __init__).
        """
        return self.values[state]


    def computeQValueFromValues(self, state, action):
        """
          Compute the Q-value of action in state from the
          value function stored in self.values.
        """
        "*** YOUR CODE HERE ***"
        # use the computation shown in formula
        # key value pairs
        # calculate weighted sum

        Qvalue = 0
        List = self.mdp.getTransitionStatesAndProbs(state, action)
        for nextstate, prob in List:
            Qvalue += prob * (self.mdp.getReward(state, action, nextstate) + self.discount * self.values[nextstate])
        return Qvalue

    def computeActionFromValues(self, state):
        """
          The policy is the best action in the given state
          according to the values currently stored in self.values.

          You may break ties any way you see fit.  Note that if
          there are no legal actions, which is the case at the
          terminal state, you should return None.
        """
        "*** YOUR CODE HERE ***"
        return self.maxValActionDic[state]

    def getPolicy(self, state):
        return self.computeActionFromValues(state)

    def getAction(self, state):
        "Returns the policy at the state (no exploration)."
        return self.computeActionFromValues(state)

    def getQValue(self, state, action):
        return self.computeQValueFromValues(state, action)

class AsynchronousValueIterationAgent(ValueIterationAgent):
    """
        * Please read learningAgents.py before reading this.*

        An AsynchronousValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs cyclic value iteration
        for a given number of iterations using the supplied
        discount factor.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 1000):
        """
          Your cyclic value iteration agent should take an mdp on
          construction, run the indicated number of iterations,
          and then act according to the resulting policy. Each iteration
          updates the value of only one state, which cycles through
          the states list. If the chosen state is terminal, nothing
          happens in that iteration.

          Some useful mdp methods you will use:
              mdp.getStates()
              mdp.getPossibleActions(state)
              mdp.getTransitionStatesAndProbs(state, action)
              mdp.getReward(state)
              mdp.isTerminal(state)
        """
        ValueIterationAgent.__init__(self, mdp, discount, iterations)

    def runValueIteration(self):
        states = self.mdp.getStates()
        lenght = len(states)
        for i in range(self.iterations):
            maxQvalue = -99999999
            state = states[i % lenght]
            if not self.mdp.isTerminal(state):
                for action in self.mdp.getPossibleActions(state):
                    qvalue = self.computeQValueFromValues(state, action)
                    if qvalue > maxQvalue:
                        maxQvalue = qvalue
                        self.maxValActionDic[state] = action
                self.values[state] = maxQvalue






class PrioritizedSweepingValueIterationAgent(AsynchronousValueIterationAgent):
    """
        * Please read learningAgents.py before reading this.*

        A PrioritizedSweepingValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs prioritized sweeping value iteration
        for a given number of iterations using the supplied parameters.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 100, theta = 1e-5):
        """
          Your prioritized sweeping value iteration agent should take an mdp on
          construction, run the indicated number of iterations,
          and then act according to the resulting policy.
        """
        self.theta = theta
        ValueIterationAgent.__init__(self, mdp, discount, iterations)

    def runValueIteration(self):
        "*** YOUR CODE HERE ***"
        def maxQValueFromState(state):
            maxQvalue = -99999
            for action in self.mdp.getPossibleActions(state):
                qvalue = self.computeQValueFromValues(state, action)
                if qvalue > maxQvalue:
                    maxQvalue = qvalue
                    self.maxValActionDic[state] = action
            return maxQvalue
        PredDictionary = {}

        for state in self.mdp.getStates():
            PredDictionary[state] = set()

        for s in self.mdp.getStates():
            for action in self.mdp.getPossibleActions(s):
                for nextState, prob in self.mdp.getTransitionStatesAndProbs(s, action):
                    if nextState not in PredDictionary:
                        PredDictionary[nextState] = set()
                    PredDictionary[nextState].add(s)
        priortyQ = util.PriorityQueue()


        for state in self.mdp.getStates():
            if not self.mdp.isTerminal(state):
                maxQvalue = maxQValueFromState(state)
                diff = abs(self.values[state] - maxQvalue)
                priortyQ.push(state, -diff)

        for iteration in range(self.iterations):

            if priortyQ.isEmpty():
                return

            temState = priortyQ.pop()


            if not self.mdp.isTerminal(temState):
                maxQvalue = maxQValueFromState(temState)
                self.values[temState] = maxQvalue

            for predecessor in PredDictionary[temState]:
                maxQvalue = maxQValueFromState(predecessor)
                diff = abs(self.values[predecessor] - maxQvalue)
                if diff > self.theta:
                    priortyQ.update(predecessor, -diff)






