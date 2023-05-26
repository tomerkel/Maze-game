package algorithms.search;

import algorithms.mazeGenerators.Position;

/**
 * this class will turn a position to AState
 */
public class MazeState extends AState {
    private Position StatePos;
    public MazeState(Position pos) {
        super(pos.toString());
        StatePos = pos;
    }

    public Position getStatePos() {
        return StatePos;
    }
}
