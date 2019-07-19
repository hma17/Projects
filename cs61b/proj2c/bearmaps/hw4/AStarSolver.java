package bearmaps.hw4;

import bearmaps.proj2ab.DoubleMapPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.*;


public class AStarSolver<Vertex> implements ShortestPathsSolver {
    Map<Vertex, Double> distTo = new HashMap<Vertex, Double>();
    Map<Vertex, Vertex> edgeTo = new HashMap<Vertex, Vertex>();
    DoubleMapPQ<Vertex> priorityQueue = new DoubleMapPQ<Vertex>();
    private SolverOutcome outcome;
    private double solutionWeight = 0.0;
    private List<Vertex> solution;
    private double timeSpent;
    int counter;





    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();
        //List<Vertex> solution2 = new ArrayList<Vertex>();
        Double dist = 0.0;
        distTo.put(start, dist);
        edgeTo.put(start, start);
        Double currPriority = distTo.get(start) + input.estimatedDistanceToGoal(start, end);
        priorityQueue.add(start, currPriority);

        if (priorityQueue.size() != 0 && sw.elapsedTime() < timeout) {
            while (priorityQueue.size() >= 1 && sw.elapsedTime() < timeout
                    && !priorityQueue.getSmallest().equals(end)) {
                Vertex currVertex = priorityQueue.removeSmallest();
                counter++;
                List<WeightedEdge<Vertex>> edgesFromCurr = input.neighbors(currVertex);
                for (WeightedEdge<Vertex> edge : edgesFromCurr) {
                    relax(edge, input, end);
                }
            }
            if (priorityQueue.getSmallest().equals(end)) {
                solutionWeight = distTo.get(end);
                List<Vertex> s = new ArrayList<Vertex>();
                solution = reverseHelper(start, end, s);
                Collections.reverse(solution);

                outcome = SolverOutcome.SOLVED;
                timeSpent = sw.elapsedTime();

                return;
            }
            outcome = SolverOutcome.UNSOLVABLE;
            timeSpent = sw.elapsedTime();
        }
    }

    void relax(WeightedEdge<Vertex> edge, AStarGraph<Vertex> input, Vertex goal) {
        Vertex vertexP = edge.from();
        Vertex vertexQ = edge.to();
        double weight = edge.weight();
        if (!distTo.containsKey(vertexQ)) {
            distTo.put(vertexQ, Double.POSITIVE_INFINITY);
            edgeTo.put(vertexQ, vertexP);
        }
        if (distTo.get(vertexP) + weight < distTo.get(vertexQ)) {
            Double newValue = distTo.get(vertexP) + weight;
            distTo.replace(vertexQ, newValue);
            edgeTo.replace(vertexQ, vertexP);
            Double newPriority = distTo.get(vertexQ)
                    + input.estimatedDistanceToGoal(vertexQ, goal);
            if (priorityQueue.contains(vertexQ)) {
                priorityQueue.changePriority(vertexQ, newPriority);
            } else {
                priorityQueue.add(vertexQ, newPriority);
            }
        }
    }

    private List<Vertex> reverseHelper(Vertex startNode, Vertex tail, List<Vertex> solution2) {
        if (startNode.equals(tail)) {
            solution2.add(startNode);
            return solution2;
        }
        solution2.add(tail);
        Vertex head2 = edgeTo.get(tail);
        return reverseHelper(startNode, head2, solution2);
    }

    @Override
    public SolverOutcome outcome() {
        return outcome;
    }

    @Override
    public List solution() {
        return solution;
    }

    @Override
    public double solutionWeight() {
        return solutionWeight;
    }

    @Override
    public int numStatesExplored() {
        return counter;
    }

    @Override
    public double explorationTime() {
        return timeSpent;
    }
}
