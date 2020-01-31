package put.ai.games.naiveplayer;

import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

import java.util.List;


public class TurboUltraPremiumPlayerPro extends Player {
	
    public static void main(String[] args) {}
    

    @Override
    public String getName() {
        return "Mikolaj Szymczak 136814 Jan Smielowski 136815";
    }


    @Override
    public Move nextMove(Board board) {
        int playerMoves = board.getMovesFor(getColor()).size();
        int opponentMoves = board.getMovesFor(getOpponent(getColor())).size();
        int totalMoves = playerMoves;
        int maxDepth;

        if( opponentMoves == 1 || playerMoves == 1)
            return findBest(board, 1);
        else {
            for(maxDepth = 0; getTime() > totalMoves; ++maxDepth) {
                if(maxDepth % 2 == 0)
                    totalMoves = totalMoves * opponentMoves;
                else
                    totalMoves = totalMoves * playerMoves;
            }

            return findBest(board, maxDepth);
        }
    }


    private Move findBest(Board board, int maxDepth) {
        List<Move> moveList = board.getMovesFor(getColor());
        Move bestMove = moveList.get(0);
        int moveValue;
        int best = Integer.MIN_VALUE;
        
        for (Move move : moveList) {
            if (getTime() < 2)
                return bestMove;

            board.doMove(move);
            moveValue = runMinimax(board, 0, false, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
            board.undoMove(move);

            if (moveValue > best) {
                bestMove = move;
                best = moveValue;
            }
        }

        return bestMove;
    }


    private int runMinimax(Board board, int depth, boolean isMe, int maxDepth, int alpha, int beta)
    {
        Color player = getColor();
        Color opponent = getOpponent(getColor());
        List<Move> moveList;   
        int playerMoves = board.getMovesFor(player).size();
        int opponentMoves = board.getMovesFor(opponent).size();
        int score;


        if(getTime() < 2 || depth == maxDepth || !(board.getWinner(player) == null) || playerMoves == 0 || opponentMoves == 0)
            return rateMove(board);

        if(isMe)
            moveList = board.getMovesFor(player);
        else
            moveList = board.getMovesFor(opponent);

        for (Move move: moveList) {
            if (getTime() < 2)
                return rateMove(board);

            board.doMove(move);

            if(isMe) {
                score = runMinimax(board, depth + 1, !isMe, maxDepth, alpha, beta);
                if (score > alpha)
                    alpha = score;
            } else {
                score = runMinimax(board, depth + 1, isMe, maxDepth, alpha, beta);
                if (score < beta)
                    beta = score;
            }

            board.undoMove(move);
            
            if (alpha >= beta)
                return beta;
        }

        return rateMove(board);
    }

    public int rateMove(Board board) {
        Color player = getColor();
        int n = board.getSize();
        int utility = 0;
        int streak = 0;
        	
		for(int i = 0; i < n; i++)	
		{
			for(int j = 0; j < n - 1; j++)	
			{
				if(board.getState(i, j) == player && board.getState(i, j + 1) == player)
				{
					utility += streak + 1;
					streak++;
				}
				else
                    streak = 0;
			}
		}
		
		for(int i = 0; i < n; i++)	
		{
			for(int j = 0; j < n - 1; j++)	
			{
                if(board.getState(j, i) == player && board.getState(j + 1, i) == player)
				{
					utility += streak + 1;
					streak++;
				}
				else
                    streak = 0;
			}
		}
		
		for(int i = 0; i < n - 1; i++)
		{
            if(board.getState(i, i) == player && board.getState(i + 1, i + 1) == player)
			{
				utility += streak + 1;
				streak++;
			}
			else
                streak = 0;
		}
		
		for(int i = 0; i < n - 1; i++)
		{
            if(board.getState(i, 5 - i) == player && board.getState(i + 1, 4 - i) == player)
			{
				utility += streak + 1;
				streak++;
			}
			else
                streak = 0;
		}
		
		return utility;
    }
}
