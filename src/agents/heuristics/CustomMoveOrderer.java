package hw2.agents.moveorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import hw2.agents.heuristics.CustomHeuristics;
import hw2.chess.game.Board;
import hw2.chess.game.Game;
import hw2.chess.game.move.CaptureMove;
import hw2.chess.game.move.Move;
import hw2.chess.game.move.MoveType;
import hw2.chess.game.move.PromotePawnMove;
import hw2.chess.game.piece.Pawn;
import hw2.chess.game.piece.Piece;
import hw2.chess.game.piece.PieceType;
import hw2.chess.game.player.Player;
import hw2.chess.game.player.PlayerType;
import hw2.chess.search.DFSTreeNode;
import hw2.chess.utils.Coordinate;

public class CustomMoveOrderer
{

	

	/**
	 * TODO: implement me!
	 * This method should perform move ordering. Remember, move ordering is how alpha-beta pruning gets part of its power from.
	 * You want to see nodes which are beneficial FIRST so you can prune as much as possible during the search (i.e. be faster)

	 *@param nodes. The nodes to order (these are children of a DFSTreeNode) that we are about to consider in the search.**/
	
	public static List<DFSTreeNode> order(List<DFSTreeNode> nodes)
	   {
	  
	      // by default get the CaptureMoves first
	     List<DFSTreeNode> castlemove = new LinkedList<DFSTreeNode>();
	     List<DFSTreeNode> promotemove = new LinkedList<DFSTreeNode>();
	     List<DFSTreeNode> captureNodes = new LinkedList<DFSTreeNode>();
	     List<DFSTreeNode> otherNodes = new LinkedList<DFSTreeNode>();
	     List<DFSTreeNode> enpassNodes = new LinkedList<DFSTreeNode>();
	     List<DFSTreeNode> moveNodes = new LinkedList<DFSTreeNode>();

	       for(DFSTreeNode node : nodes)
	       {
	        if(node.getMove() != null)
	        {
	         switch(node.getMove().getType())
	         {
	          case CASTLEMOVE: // priority capture move
	           castlemove.add(node);
	           break;
	          case PROMOTEPAWNMOVE: // priority capture move
	           promotemove.add(node);
	           break;
	          case CAPTUREMOVE: // priority capture move
	            captureNodes.add(node);
	            break;
	          case ENPASSANTMOVE:
	           enpassNodes.add(node);
	           break;
	          case MOVEMENTMOVE:
	           moveNodes.add(node);
	           break;
	          default:
	           otherNodes.add(node);
	           break;
	         }
	        } else
	        {
	         otherNodes.add(node);
	        }
	       }
	       captureNodes.addAll(enpassNodes);
	       captureNodes.addAll(moveNodes);
	       captureNodes.addAll(promotemove);
	       captureNodes.addAll(castlemove);
	       captureNodes.addAll(otherNodes);
	    return captureNodes;
	 }
}