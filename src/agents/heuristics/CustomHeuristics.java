package hw2.agents.heuristics;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import edu.cwru.sepia.util.Direction;
import hw2.agents.heuristics.DefaultHeuristics.DefensiveHeuristics;
import hw2.agents.heuristics.DefaultHeuristics.OffensiveHeuristics;
import hw2.chess.game.Board;
import hw2.chess.game.Game;
import hw2.chess.game.move.Move;
import hw2.chess.game.move.MoveType;
import hw2.chess.game.move.PromotePawnMove;
import hw2.chess.game.piece.Piece;
import hw2.chess.game.piece.PieceType;
import hw2.chess.game.player.Player;
import hw2.chess.game.player.PlayerType;
import hw2.chess.search.DFSTreeNode;
import hw2.chess.utils.Coordinate;
import hw2.chess.utils.Pair;
import hw2.chess.game.move.CaptureMove;

public class CustomHeuristics
{
	private Stack<Pair<Move, Game>> gameStateHistory;
	private static int bishoppos[][] =
		  {
		   {-5, -5, -5, -5, -5, -5, -5, -5},
		      {-5, 10,  5,  8,  8,  5, 10, -5},
		      {-5,  5,  3,  8,  8,  3,  5, -5},
		      {-5,  3, 10,  3,  3, 10,  3, -5},
		      {-5,  3, 10,  3,  3, 10,  3, -5},
		      {-5,  5,  3,  8,  8,  3,  5, -5},
		      {-5, 10,  5,  8,  8,  5, 10, -5},
		      {-5, -5, -5, -5, -5, -5, -5, -5}
		  };
		  private static int knightpos[][] =
		  {
		   {-10, -5, -5, -5, -5, -5, -5,-10},
		   { -8,  0,  0,  3,  3,  0,  0, -8},
		   { -8,  0, 10,  8,  8, 10,  0, -8},
		   { -8,  0,  8, 10, 10,  8,  0, -8},
		   { -8,  0,  8, 10, 10,  8,  0, -8},
		   { -8,  0, 10,  8,  8, 10,  0, -8},
		   { -8,  0,  0,  3,  3,  0,  0, -8},
		   {-10, -5, -5, -5, -5, -5, -5,-10}
		  };
		  
		  private static int pawnposBlack[][] =
		  {
		   {0,  0,  0,  0,  0,  0,  0,  0},
		      {0,  0,  0, -5, -5,  0,  0,  0},
		      {0,  2,  3,  4,  4,  3,  2,  0},
		      {0,  4,  6, 10, 10,  6,  4,  0},
		      {0,  6,  9, 10, 10,  9,  6,  0},
		      {4,  8, 12, 16, 16, 12,  8,  4},
		      {5, 10, 15, 20, 20, 15, 10,  5},
		      {0,  0,  0,  0,  0,  0,  0,  0}
		  };
		  private static int rookpos[][] =
		   {
		       {0, 0, 0, 0, 0, 0, 0, 0},
		       {5, 10, 10, 10, 10, 10, 10, 5},
		       {-5, 0, 0, 0, 0, 0, 0, -5},
		       {-5, 0, 0, 0, 0, 0, 0, -5},
		       {-5, 0, 0, 0, 0, 0, 0, -5},
		       {-5, 0, 0, 0, 0, 0, 0, -5},
		       {-5, 0, 0, 0, 0, 0, 0, -5},
		       {0, 0, 0, 5, 5, 0, 0, 0}
		   };
		  private static int queenpos[][] =
		   {
		       {-5, -5, -5, -5, -5, -5, -5, -5},
		       {-5, 5, 5, 5, 5, 5, 5, -5},
		       {-5, 0, 5, 5, 5, 5, 0, -5},
		       {0, 0, 5, 5, 5, 5, 0, -5},
		       {0, 0, 5, 5, 5, 5, 0, -5},
		       {0, 0, 5, 5, 5, 5, 0, -5},
		       {-5, 0, 0, 0, 0, 0, 0, -5},
		       {-5, -5, -5, -5, -5, -5, -5, -5}
		   };
		  private static int kingpos[][] =
		   {
		       {0, 5, 5, 5, 5, 5, 5, 0},
		       {5, 10, 10, 10, 10, 10, 10, 5},
		       {0, 5, 5, 5, 5, 5, 5, 0},
		       {0, 5, 5, 5, 5, 5, 5, 0},
		       {0, 5, 5, 5, 5, 5, 5, 0},
		       {0, 5, 5, 5, 5, 5, 5, 0},
		       {5, 10, 10, 10, 10, 10, 10, 5},
		       {0, 5, 5, 5, 5, 5, 5, 0}
		   };

		  
		  // I created this, should be just a flipped
		  // version of the white array
		  private static int pawnposWhite[][] =
		  {
		   {0,  0,  0,  0,  0,  0,  0,  0},
		   {5, 10, 15, 20, 20, 15, 10,  5},
		   {4,  8, 12, 16, 16, 12,  8,  4},
		   {0,  6,  9, 10, 10,  9,  6,  0},
		   {0,  4,  6, 10, 10,  6,  4,  0},
		   {0,  2,  3,  4,  4,  3,  2,  0},
		   {0,  0,  0, -5, -5,  0,  0,  0},
		   {0,  0,  0,  0,  0,  0,  0,  0}
		   };

 /**
  * TODO: implement me! The heuristics that I wrote are useful, but not very good for a good chessbot.
  * Please use this class to add your heuristics here! I recommend taking a look at the ones I provided for you
  * in DefaultHeuristics.java (which is in the same directory as this file)
  */

 
	/**
	 * Get the max player from a node
	 * @param node
	 * @return
	 */
	public static Player getMaxPlayer(DFSTreeNode node)
	{
		return node.getMaxPlayer();
	}
 
	/**
	 * Get the min player from a node
	 * @param node
	 * @return
	 */
	public static Player getMinPlayer(DFSTreeNode node)
	{
		return CustomHeuristics.getMaxPlayer(node).equals(node.getGame().getCurrentPlayer()) ? node.getGame().getOtherPlayer() : node.getGame().getCurrentPlayer();
	}
	
 public static class OffensiveHeuristics extends Object
 {


	public static int getNumberOfPiecesMaxPlayerIsThreatening(DFSTreeNode node)
	{
		int pointWecanGet = 0;
		int numPiecesMaxPlayerIsThreatening = 0;
		for(Piece piece : node.getGame().getBoard().getPieces(DefaultHeuristics.getMaxPlayer(node)))
		{
			numPiecesMaxPlayerIsThreatening += piece.getAllCaptureMoves(node.getGame()).size();
	         List<Move> all_piece_capMove= piece.getAllCaptureMoves(node.getGame());
	         for(Move capMove: all_piece_capMove) {
	            if(capMove.getType().equals(MoveType.CAPTUREMOVE)){
	            	int targetID = ((CaptureMove)capMove).getTargetPieceID();
	                Board board = node.getGame().getBoard();
	                PieceType tagType = board.getPiece(DefaultHeuristics.getMinPlayer(node),targetID).getType();
	                if  (tagType == null) System.out.print("tag Piece not found (getNumberOfPiecesMaxPlayerIsThreatening): bug");
	                int tempWin = Piece.getPointValue(tagType);
	                // update point we might lose if a more import piece are threat
	                pointWecanGet += tempWin;
	              }
	           }
		}
		return numPiecesMaxPlayerIsThreatening + pointWecanGet;
	}
	
	public static int MaxPlayerboardPostionPoints(DFSTreeNode node) {
	     int score = 0;
	     Player maxPlayer = CustomHeuristics.getMaxPlayer(node);
	     // add position score for the pawns
	     for (Piece pawnPiece : node.getGame().getBoard().getPieces(maxPlayer, PieceType.PAWN)) { 
	    	 //I chance current player to max player
	         Coordinate pawnPosition = node.getGame().getCurrentPosition(pawnPiece);
	         if (maxPlayer.getPlayerType() == PlayerType.WHITE) {
	             score += pawnposWhite[pawnPosition.getXPosition()-1][pawnPosition.getYPosition()-1];
	            // System.out.println("score: "+"x coordinate: "+pawnPosition.getXPosition()+"y coordinate: "+pawnPosition.getYPosition()+"corr value: "+pawnposWhite[pawnPosition.getXPosition()][pawnPosition.getYPosition()]);
	         } else {
	             score += pawnposBlack[pawnPosition.getXPosition()-1][pawnPosition.getYPosition()-1];
	         }
	     }
	     
	     // add position score for king
	     Piece kingPiece = node.getGame().getBoard().getPieces(maxPlayer, PieceType.KING).iterator().next();
	     Coordinate kingPosition = node.getGame().getCurrentPosition(kingPiece);
	     score += kingpos[kingPosition.getXPosition()-1][kingPosition.getYPosition()-1];

	     // add position score for knights
	     for (Piece knightPiece : node.getGame().getBoard().getPieces(maxPlayer, PieceType.KNIGHT)) {
	         Coordinate knightPosition = node.getGame().getCurrentPosition(knightPiece);
	         score += knightpos[knightPosition.getXPosition()-1][knightPosition.getYPosition()-1];
	     }

	     // add position score for bishops
	     for (Piece bishopPiece : node.getGame().getBoard().getPieces(maxPlayer, PieceType.BISHOP)) {
	         Coordinate bishopPosition = node.getGame().getCurrentPosition(bishopPiece);
	         score += bishoppos[bishopPosition.getXPosition()-1][bishopPosition.getYPosition()-1];
	     }

	     // add position score for rooks
	     for (Piece rookPiece : node.getGame().getBoard().getPieces(maxPlayer, PieceType.ROOK)) {
	         Coordinate rookPosition = node.getGame().getCurrentPosition(rookPiece);
	         score += rookpos[rookPosition.getXPosition()-1][rookPosition.getYPosition()-1];
	     }

	     // add position score for queens
	     for (Piece queenPiece : node.getGame().getBoard().getPieces(maxPlayer, PieceType.QUEEN)) {
	         Coordinate queenPosition = node.getGame().getCurrentPosition(queenPiece);
	         score += queenpos[queenPosition.getXPosition()-1][queenPosition.getYPosition()-1];
	     }

	     return score;
	 }
	
	public static double mobilityScore(DFSTreeNode node) {double spaceAdvantageValue = 0;

	// Calculate the total number of  moves available for each player
	 int maxPlayerMoves = 0;
	 int minPlayerMoves=0;
	 int mobilityScore=0;
	 for(Piece piece : node.getGame().getBoard().getPieces(node.getMaxPlayer())) {
	  maxPlayerMoves  += piece.getAllMoves(node.getGame()).size();
	 
	   }
	 for (Piece piece : node.getGame().getBoard().getPieces(node.getMaxPlayer())) {
	  minPlayerMoves  += piece.getAllMoves(node.getGame()).size();
	 }

	// Calculate mobility score by mobilityScore = mobilityWt * (wMobility-bMobility)
	 mobilityScore = maxPlayerMoves-minPlayerMoves;

	return 0.1*mobilityScore;
	}
  
 }

 public static class DefensiveHeuristics extends Object
 {

		public static int getNumberOfMaxPlayersAlivePieces(DFSTreeNode node)
		{
			int numMaxPlayersPiecesAlive = 0;
			for(PieceType pieceType : PieceType.values())
			{
				numMaxPlayersPiecesAlive += node.getGame().getNumberOfAlivePieces(DefaultHeuristics.getMaxPlayer(node), pieceType);
			}
			return numMaxPlayersPiecesAlive;
		}

		public static int getNumberOfMinPlayersAlivePieces(DFSTreeNode node)
		{
			int numMaxPlayersPiecesAlive = 0;
			for(PieceType pieceType : PieceType.values())
			{
				numMaxPlayersPiecesAlive += node.getGame().getNumberOfAlivePieces(DefaultHeuristics.getMinPlayer(node), pieceType);
			}
			return numMaxPlayersPiecesAlive;
		}

//TODO: if getNumberofEnemyAlivePieces > getNumberOfAlivePieces, make the reward of capture move higher	 
	 
//TODO: if enemy weight more, eat, unless pawn or king in danger
	 
  
  
//TODO: protect King add own piece around king & let king move a safer pos // 2 step no enemy pawn
	public static int getClampedPieceValueTotalSurroundingMaxPlayersKing(DFSTreeNode node)
	{
		// what is the state of the pieces next to the king? add up the values of the neighboring pieces
		// positive value for friendly pieces and negative value for enemy pieces (will clamp at 0)
		int maxPlayerKingSurroundingPiecesValueTotal = 0;

		Piece kingPiece = node.getGame().getBoard().getPieces(DefaultHeuristics.getMaxPlayer(node), PieceType.KING).iterator().next();
		Coordinate kingPosition = node.getGame().getCurrentPosition(kingPiece);
		for(Direction direction : Direction.values())
		{
			Coordinate neightborPosition = kingPosition.getNeighbor(direction);
			if(node.getGame().getBoard().isInbounds(neightborPosition) && node.getGame().getBoard().isPositionOccupied(neightborPosition))
			{
				Piece piece = node.getGame().getBoard().getPieceAtPosition(neightborPosition);
				int pieceValue = Piece.getPointValue(piece.getType());
				if(piece != null && kingPiece.isEnemyPiece(piece))
				{
					maxPlayerKingSurroundingPiecesValueTotal -= pieceValue;
				} else if(piece != null && !kingPiece.isEnemyPiece(piece))
				{
					maxPlayerKingSurroundingPiecesValueTotal += pieceValue;
				}
			}
		}
		// kingSurroundingPiecesValueTotal cannot be < 0 b/c the utility of losing a game is 0, so all of our utility values should be at least 0
		maxPlayerKingSurroundingPiecesValueTotal = Math.max(maxPlayerKingSurroundingPiecesValueTotal, 0);
		return maxPlayerKingSurroundingPiecesValueTotal;
	}


	public static int getNumberOfPiecesThreateningMaxPlayer(DFSTreeNode node)
	{
		// how many pieces are threatening us? And add weight to which maxplayer piece are being threaten
		int pointWeMightLose = 0;
		int numPiecesThreateningMaxPlayer = 0;
		for(Piece piece : node.getGame().getBoard().getPieces(DefaultHeuristics.getMinPlayer(node)))
		{
			numPiecesThreateningMaxPlayer += piece.getAllCaptureMoves(node.getGame()).size();
			List<Move> all_piece_capMove = piece.getAllCaptureMoves(node.getGame()); //getting all capture move min player can done
			for(Move capMove: all_piece_capMove) { //for every capmove, accumulate the maxplayer threaten val
	           if(capMove.getType().equals(MoveType.CAPTUREMOVE)){
	        	   int targetID = ((CaptureMove)capMove).getTargetPieceID();
	               Board board = node.getGame().getBoard();
	               PieceType tagType = board.getPiece(DefaultHeuristics.getMaxPlayer(node),targetID).getType();
	               // Error Debug
	               if  (tagType == null) System.out.print("tag Piece not found (getNumberOfPiecesThreateningUs): bug");
	               int tempLose = Piece.getPointValue(tagType);
	               // update point we might lose if a more import piece are threat
	               pointWeMightLose += tempLose;
	            }       
			}
		}
		return numPiecesThreateningMaxPlayer+pointWeMightLose;
	}
 }
 	
 	// Need to add/Delete something
	public static double getOffensiveMaxPlayerHeuristicValue(DFSTreeNode node)
	{
		// remember the action has already taken affect at this point, so capture moves have already resolved
		// and the targeted piece will not exist inside the game anymore.
		// however this value was recorded in the amount of points that the player has earned in this node
		double damageDealtInThisNode = node.getGame().getBoard().getPointsEarned(DefaultHeuristics.getMaxPlayer(node));
		double positionPoints = OffensiveHeuristics.MaxPlayerboardPostionPoints(node);
		double spaceAdvantagValue=CustomHeuristics. getSpaceAdvantageValue(node);
		
		switch(node.getMove().getType())
		{
		case PROMOTEPAWNMOVE:
			PromotePawnMove promoteMove = (PromotePawnMove)node.getMove();
			damageDealtInThisNode += Piece.getPointValue(promoteMove.getPromotedPieceType());
			break;
		default:
			break;
		}
		// offense can typically include the number of pieces that our pieces are currently threatening
		int numPiecesWeAreThreatening = OffensiveHeuristics.getNumberOfPiecesMaxPlayerIsThreatening(node);

		return damageDealtInThisNode + numPiecesWeAreThreatening + positionPoints + spaceAdvantagValue;
	}
	public static double getDefensiveMaxPlayerHeuristicValue(DFSTreeNode node)
	{
		// how many pieces exist on our team?
		int numPiecesAlive = DefensiveHeuristics.getNumberOfMaxPlayersAlivePieces(node);

		// what is the state of the pieces next to the king? add up the values of the neighboring pieces
		// positive value for friendly pieces and negative value for enemy pieces (will clamp at 0)
		int kingSurroundingPiecesValueTotal = DefensiveHeuristics.getClampedPieceValueTotalSurroundingMaxPlayersKing(node);
		//TODO: decide if needed
//		  double numEnemyPiecesAlive=CustomHeuristics.getNumberOfEnemyAlivePieces (node); 
		
		// how many pieces are threatening us?
		int numPiecesThreateningUs = DefensiveHeuristics.getNumberOfPiecesThreateningMaxPlayer(node);
		
		double getCheckMate=CustomHeuristics. getCheckMate(node);
		
		// WE don't want to be threatening so minus, but should check if it's neg, if it change to 0
		double res = numPiecesAlive + kingSurroundingPiecesValueTotal - numPiecesThreateningUs + getCheckMate;
		res = Math.max(res,0);
		return res;
	}
	public static double getNonlinearPieceCombinationMaxPlayerHeuristicValue(DFSTreeNode node)
	{ 
		// both bishops are worth more together than a single bishop alone
		// same with knights...we want to encourage keeping pairs of elements
		double multiPieceValueTotal = 0.0;

		double exponent = 1.5; // f(numberOfKnights) = (numberOfKnights)^exponent

		// go over all the piece types that have more than one copy in the game (including pawn promotion)
		for(PieceType pieceType : new PieceType[] {PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK, PieceType.QUEEN})
		{
			multiPieceValueTotal += Math.pow(node.getGame().getNumberOfAlivePieces(DefaultHeuristics.getMaxPlayer(node), pieceType), exponent);
		}
		double materialValue=CustomHeuristics.getMaxMaterialVal(node);
		
		return multiPieceValueTotal + materialValue;
	}
	

	


// Existing code for the class
	public static int bishopHelper(DFSTreeNode node, Player thisPlayer) {
		   
	    int bishopPairBonus = 0;
	    int bishopVsKnightValue=0;
	    int alivePiece = 0;
	    if(thisPlayer.equals(CustomHeuristics.getMaxPlayer(node))) {
	     alivePiece = DefensiveHeuristics.getNumberOfMaxPlayersAlivePieces(node);
	    }else {
	     alivePiece = DefensiveHeuristics.getNumberOfMinPlayersAlivePieces(node);
	    }
	    // Check for bishop pair bonus by searching for bishop pair
	    if (node.getGame().getBoard().getPieces(thisPlayer, PieceType.BISHOP).size() == 2) {
	     // if more than half of all pieces 
	     if(alivePiece>8) bishopPairBonus+= 0.3;
	     //if less
	     else bishopPairBonus += 0.7;
	    }
	    // Evaluate bishop vs knight value
	    if (node.getGame().getBoard().getPieces(DefaultHeuristics.getMaxPlayer(node), PieceType.KNIGHT).size() == 1) {
	        bishopVsKnightValue += .1;
	    }
	     return bishopPairBonus + bishopVsKnightValue;
	 }

	public static double getMaxMaterialVal(DFSTreeNode node) {
	 int pieceDiffAccum = 0;
	   
	 for(PieceType pieceType : PieceType.values())
	 {
	  int maxPnum = node.getGame().getNumberOfAlivePieces(DefaultHeuristics.getMaxPlayer(node), pieceType);
	  int minPnum = node.getGame().getNumberOfAlivePieces(DefaultHeuristics.getMinPlayer(node), pieceType);

	  int typeVal = Piece.getPointValue(pieceType);
	  // calculate the difference
	  
	  pieceDiffAccum += typeVal * (maxPnum - minPnum);
	 }
	 ///////////////other///////////////////
	 int pawnMaterial = pawnHelper(node);
	 //Movability
	 double mobilityScore = OffensiveHeuristics.mobilityScore(node);
	 //penalize queen for early moves and development
	 double queenScore=queenHelper(node);
	 // TODO: test if need calculate enemy bishop pair
	 double bishopScore = CustomHeuristics.bishopHelper(node, CustomHeuristics.getMaxPlayer(node)) 
	   - 0.5 * CustomHeuristics.bishopHelper(node, CustomHeuristics.getMinPlayer(node));
	 return pieceDiffAccum + 0.5* pawnMaterial + mobilityScore + bishopScore+queenScore;
	}
	
	public static int queenHelper(DFSTreeNode node) {
		int numPiecesAlive = DefensiveHeuristics.getNumberOfMaxPlayersAlivePieces(node);;
		int y_distance=0;
		int x_distance=0;
	    int numPlayersThreshold = 15;
	    Piece queen = node.getGame().getBoard().getPieces(DefaultHeuristics.getMaxPlayer(node), PieceType.QUEEN).iterator().next();
	    Coordinate queenPosition = node.getGame().getCurrentPosition(queen);
	    // check whether queen still exists
	    if (queen == null) {
	        return 0;
	    }
	   
        //calculate distance from queen's initial position
        if(node.getMaxPlayer().getPlayerType() == PlayerType.BLACK){
        	y_distance=Math.abs(queenPosition.getYPosition()-1);
        	x_distance=Math.abs(queenPosition.getXPosition()-4);
        }else {
        	y_distance=Math.abs(queenPosition.getYPosition()-8);
        	x_distance=Math.abs(queenPosition.getXPosition()-4);
        	}
        //if the number of remaining pieces is greater than or equal to a threshold value we will penalize the queen for early moves
        if (numPiecesAlive >= numPlayersThreshold) {
               return -(int) (0.5 * (y_distance + x_distance));
           } else {
               return 0;
           }
        
	 
	     
	    
	}



public static double getMaxPlayerHeuristicValue(DFSTreeNode node)
{
	double offenseHeuristicValue = DefaultHeuristics.getOffensiveMaxPlayerHeuristicValue(node);
	double defenseHeuristicValue = DefaultHeuristics.getDefensiveMaxPlayerHeuristicValue(node);
	double nonlinearHeuristicValue = DefaultHeuristics.getNonlinearPieceCombinationMaxPlayerHeuristicValue(node);

	return offenseHeuristicValue + defenseHeuristicValue + nonlinearHeuristicValue;
}
@SuppressWarnings("unused")
public static int pawnHelper(DFSTreeNode node) {
	Iterator<Piece> pawnPiece = node.getGame().getBoard().getPieces(DefaultHeuristics.getMaxPlayer(node), PieceType.PAWN).iterator();
	int isolateNum = 0;
	int blockNum = 0;
	int doubled = 0;
	int promotionBonus = 0;
	while (pawnPiece.hasNext()) {
		Piece pawn = pawnPiece.next();
		Coordinate pawnPosition = node.getGame().getCurrentPosition(pawn);
		// accumulate nearby friend piece
		int friendPiece = 0;
		for(Direction direction : Direction.values()) {
			Coordinate neightborPosition = pawnPosition.getNeighbor(direction);
			if(node.getGame().getBoard().isInbounds(neightborPosition) && node.getGame().getBoard().isPositionOccupied(neightborPosition))
			{
				Piece neigborPiece = node.getGame().getBoard().getPieceAtPosition(neightborPosition);
				int pieceValue = Piece.getPointValue(neigborPiece.getType());
				if(neigborPiece != null) {
					if(neigborPiece != null && pawn.isEnemyPiece(neigborPiece))// is nearby piece is enemy
					{
					// if my forward direction, block ++
						// white player
						if((direction == Direction.NORTH) && (node.getMaxPlayer().getPlayerType() == PlayerType.WHITE)) blockNum += 1;
						// black player
						if((direction == Direction.SOUTH) && (node.getMaxPlayer().getPlayerType() == PlayerType.BLACK)) blockNum += 1;
					} else if(neigborPiece != null && !pawn.isEnemyPiece(neigborPiece))// if nearby piece is friend
					{
						friendPiece += 1;
						if((direction == Direction.NORTH) && (node.getMaxPlayer().getPlayerType() == PlayerType.WHITE)) {
							blockNum += 1;
							if(neigborPiece.getType() == PieceType.PAWN) doubled += 1;
						}
						// black player
						if((direction == Direction.SOUTH) && (node.getMaxPlayer().getPlayerType() == PlayerType.BLACK)) {
							blockNum += 1;
							if(neigborPiece.getType() == PieceType.PAWN) doubled += 1;
						}
					}
				}else {
					//if neigborPiece is null do nothing
					
				}

		        // promotion bonus for pawns that have reached the end of the board
		        if ((node.getMaxPlayer().getPlayerType() == PlayerType.WHITE && pawnPosition.getYPosition() == 8) ||
		            (node.getMaxPlayer().getPlayerType() == PlayerType.BLACK && pawnPosition.getYPosition() == 1)) {
		            promotionBonus += 8;
		        }
			}
		}
		// if no frienpiece nearby
		if(friendPiece == 0) isolateNum +=1;
	}
	return isolateNum+blockNum+doubled+promotionBonus;
}

 public static double getSpaceAdvantageValue(DFSTreeNode node) {
     double spaceAdvantageValue = 0;

     // Calculate the total number of squares and moves controlled by each player
     int playerMovesControlled = 0;
     int otherPlayerMovesControlled = 0;
     for(Piece piece : node.getGame().getBoard().getPieces(node.getGame().getOtherPlayer())) {
      otherPlayerMovesControlled  += piece.getAllMoves(node.getGame()).size();
      
        }
     for (Piece piece : node.getGame().getBoard().getPieces(node.getGame().getCurrentPlayer())) {
      playerMovesControlled  += piece.getAllMoves(node.getGame()).size();
     }

     // Calculate the space and move advantage as the difference between our player and the opponenet
     spaceAdvantageValue = playerMovesControlled - otherPlayerMovesControlled;

     return spaceAdvantageValue;
 }
 
 //if next checkmate, run away
 
 public static double getCheckMate(DFSTreeNode node) {
     double score = 0;
     if(node.getGame().isInCheckmate())
  {
   // who is in check?
   if(node.getGame().isInCheck(node.getGame().getCurrentPlayer()))
   {
    // loss
    score-=10000;
   } else
   {
    // win
    score+=10000;
   }
  } 
     return score;
 }
 public static int getNumberOfEnemyAlivePieces(DFSTreeNode node) // control number count checked
    {
     int numEenemyPiecesAlive = 0;
              for(PieceType pieceType : PieceType.values()) {
               numEenemyPiecesAlive += node.getGame().getNumberOfAlivePieces(node.getGame().getOtherPlayer(), pieceType)*2;
              }
              return numEenemyPiecesAlive;
    }


 

}
