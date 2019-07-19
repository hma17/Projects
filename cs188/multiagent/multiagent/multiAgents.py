# multiAgents.py
# --------------
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


from util import manhattanDistance
from game import Directions
import random, util

from game import Agent

class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """


    def getAction(self, gameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newGhostPositions = successorGameState.getGhostPositions()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        "*** YOUR CODE HERE ***"

        #print(successorGameState.getScore())
        DistbwfoodPac = 0.6
        DistbwGhostPac = 0.6
        for foodpos in newFood.asList():
            DistbwfoodPac += 1/(((foodpos[0] - newPos[0]) ** 2 + (foodpos[1] - newPos[1]) ** 2 ) ** 0.5)
        if DistbwfoodPac < 1:
            DistbwfoodPac = 1/DistbwfoodPac
        for ghostPosition in newGhostPositions:
            DistbwGhostPac += (((ghostPosition[0] - newPos[0]) ** 2 + (ghostPosition[1] - newPos[1]) ** 2 ) ** 0.5)
        print(DistbwfoodPac)
        print(DistbwGhostPac)
        return (DistbwGhostPac) + (DistbwfoodPac) + successorGameState.getScore() + newScaredTimes[0]**5


def scoreEvaluationFunction(currentGameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """

    def getAction(self, gameState):
        """
        Returns the minimax action from the current gameState using self.depth
        and self.evaluationFunction.

        Here are some method calls that might be useful when implementing minimax.

        gameState.getLegalActions(agentIndex):
        Returns a list of legal actions for an agent
        agentIndex=0 means Pacman, ghosts are >= 1

        gameState.generateSuccessor(agentIndex, action):
        Returns the successor game state after an agent takes an action

        gameState.getNumAgents():
        Returns the total number of agents in the game

        gameState.isWin():
        Returns whether or not the game state is a winning state

        gameState.isLose():
        Returns whether or not the game state is a losing state
        """
        "*** YOUR CODE HERE ***"
        endLevel = gameState.getNumAgents() * self.depth

        """General Max Function"""
        def maxAgent(curr_game_state, level):
            macho_eval_num = -1E5
            macho_action = []
            for action in curr_game_state.getLegalActions(0):
                nextState = curr_game_state.generateSuccessor(0, action)

                if nextState.isWin() or nextState.isLose():
                    curr_eval_num = self.evaluationFunction(nextState)
                else:
                    poop, curr_eval_num = minimax(nextState, level + 1)#recursive call to minagent from max agent

                if curr_eval_num > macho_eval_num:
                    macho_eval_num = curr_eval_num
                    macho_action = action

            return (macho_action, macho_eval_num)

        def minAgent(curr_game_state, level):
            micho_eval_num = 1E5
            micho_action = []
            ghost_num = level % curr_game_state.getNumAgents()
            if level == endLevel - 1:
                for action in curr_game_state.getLegalActions(ghost_num):
                    next_state = curr_game_state.generateSuccessor(ghost_num, action)

                    curr_eval_num = self.evaluationFunction(next_state)

                    if curr_eval_num < micho_eval_num:
                        micho_eval_num = curr_eval_num
                        micho_action = action

                return (micho_action, micho_eval_num)
            else:
                for action in curr_game_state.getLegalActions(ghost_num):
                    next_state = curr_game_state.generateSuccessor(ghost_num, action)

                    if next_state.isWin() or next_state.isLose():
                        curr_eval_num = self.evaluationFunction(next_state)
                    else:
                        poop, curr_eval_num = minimax(next_state, level + 1)
                    if curr_eval_num < micho_eval_num:
                        micho_eval_num = curr_eval_num
                        micho_action = action

                return (micho_action, micho_eval_num)


        def minimax(State, level):
            agentIndex = level % State.getNumAgents()
            if level >= endLevel:
                print("Error Here")
                return None
            elif (agentIndex == 0):
                return maxAgent(State, level)
            else:
                return minAgent(State, level)

        action, Num = minimax(gameState, 0)
        return action





class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """

        "*** YOUR CODE HERE ***"
        endLevel = gameState.getNumAgents() * self.depth

        def maxAgent(curr_game_state, level, alpha, beta):
                macho_eval_num = -1E5
                macho_action = []
                for action in curr_game_state.getLegalActions(0):
                    nextState = curr_game_state.generateSuccessor(0, action)

                    if nextState.isWin() or nextState.isLose():
                        curr_eval_num = self.evaluationFunction(nextState)
                    else:
                        poop, curr_eval_num = aflaateb(nextState, level + 1, alpha, beta)#recursive call to minagent from max agent

                    if curr_eval_num > macho_eval_num:
                        macho_eval_num = curr_eval_num
                        macho_action = action

                    if macho_eval_num > beta:
                        return (macho_action, macho_eval_num)
                    alpha = max(alpha, macho_eval_num)
                return (macho_action, macho_eval_num)

        def minAgent(curr_game_state, level, alpha, beta):
            micho_eval_num = 1E5
            micho_action = []
            ghost_num = level % curr_game_state.getNumAgents()
            if level == endLevel - 1:
                for action in curr_game_state.getLegalActions(ghost_num):
                    next_state = curr_game_state.generateSuccessor(ghost_num, action)

                    curr_eval_num = self.evaluationFunction(next_state)

                    if curr_eval_num < micho_eval_num:
                        micho_eval_num = curr_eval_num
                        micho_action = action
                    if micho_eval_num < alpha:
                        return (micho_action, micho_eval_num)
                    beta = min(beta, micho_eval_num)

                return (micho_action, micho_eval_num)
            else:
                for action in curr_game_state.getLegalActions(ghost_num):
                    next_state = curr_game_state.generateSuccessor(ghost_num, action)

                    if next_state.isWin() or next_state.isLose():
                        curr_eval_num = self.evaluationFunction(next_state)
                    else:
                        poop, curr_eval_num = aflaateb(next_state, level + 1, alpha, beta)
                    if curr_eval_num < micho_eval_num:
                        micho_eval_num = curr_eval_num
                        micho_action = action
                    if micho_eval_num < alpha:
                        return (micho_action, micho_eval_num)
                    beta = min(beta, micho_eval_num)

                return (micho_action, micho_eval_num)


        def aflaateb(State, level, alpha, beta):
            agentIndex = level % State.getNumAgents()
            if level >= endLevel:
                print("Error Here")
                return None
            elif (agentIndex == 0):
                return maxAgent(State, level, alpha, beta)
            else:
                return minAgent(State, level, alpha, beta)

        action, Num = aflaateb(gameState, 0, float("-inf"), float("inf"))
        return action

class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState):
        """
        Returns the expectimax action using self.depth and self.evaluationFunction

        All ghosts should be modeled as choosing uniformly at random from their
        legal moves.
        """
        "*** YOUR CODE HERE ***"
        endLevel = gameState.getNumAgents() * self.depth
        probability = 1/gameState.getNumAgents()

        """General Max Function"""
        def maxAgent(curr_game_state, level):
            macho_eval_num = -1E5
            macho_action = []
            for action in curr_game_state.getLegalActions(0):
                nextState = curr_game_state.generateSuccessor(0, action)

                if nextState.isWin() or nextState.isLose():
                    curr_eval_num = self.evaluationFunction(nextState)
                else:
                    poop, curr_eval_num = expmax(nextState, level + 1)#recursive call to minagent from max agent

                if curr_eval_num > macho_eval_num:
                    macho_eval_num = curr_eval_num
                    macho_action = action

            return (macho_action, macho_eval_num)

        def ExpAgent(curr_game_state, level):
            sum_eval_num = 0
            ghost_num = level % curr_game_state.getNumAgents()
            totalListlength = len(curr_game_state.getLegalActions(ghost_num))
            if level == endLevel - 1:
                for action in curr_game_state.getLegalActions(ghost_num):
                    next_state = curr_game_state.generateSuccessor(ghost_num, action)

                    sum_eval_num += self.evaluationFunction(next_state)


                return ('',sum_eval_num/totalListlength)
            else:
                for action in curr_game_state.getLegalActions(ghost_num):
                    next_state = curr_game_state.generateSuccessor(ghost_num, action)

                    if next_state.isWin() or next_state.isLose():
                        sum_eval_num += self.evaluationFunction(next_state)
                    else:
                        poop, exp_eval_num = expmax(next_state, level + 1)
                        sum_eval_num += exp_eval_num
                return ('', sum_eval_num/totalListlength)

        def expmax(State, level):
            agentIndex = level % State.getNumAgents()
            if level >= endLevel:
                print("Error Here")
                return None
            elif (agentIndex == 0):
                return maxAgent(State, level)
            else:
                return ExpAgent(State, level)

        action, Num = expmax(gameState, 0)
        return action



def betterEvaluationFunction(currentGameState):
    """
    Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
    evaluation function (question 5).

    DESCRIPTION: <write something here so we know what you did>
    """
    "*** YOUR CODE HERE ***"
    util.raiseNotDefined()

# Abbreviation
better = betterEvaluationFunction
