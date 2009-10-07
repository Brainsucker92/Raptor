/**
 * New BSD License
 * http://www.opensource.org/licenses/bsd-license.php
 * Copyright (c) 2009, RaptorProject (http://code.google.com/p/raptor-chess-interface/)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the RaptorProject nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package raptor.game;

import java.util.ArrayList;

import raptor.game.pgn.Arrow;
import raptor.game.pgn.Comment;
import raptor.game.pgn.Highlight;
import raptor.game.pgn.MoveAnnotation;
import raptor.game.pgn.Nag;
import raptor.game.pgn.RemainingClockTime;
import raptor.game.pgn.SublineNode;
import raptor.game.util.GameUtils;

public class Move implements GameConstants {
	public static final int KINGSIDE_CASTLING_CHARACTERISTIC = 1;
	public static final int QUEENSIDE_CASTLING_CHARACTERISTIC = 2;
	public static final int DOUBLE_PAWN_PUSH_CHARACTERISTIC = 4;
	public static final int PROMOTION_CHARACTERISTIC = 8;
	public static final int EN_PASSANT_CHARACTERISTIC = 16;
	public static final int DROP_CHARACTERISTIC = 32;

	// Bytes are used because they take up less space than ints and there is no
	// need for the extra space.
	protected byte from = EMPTY_SQUARE;
	protected byte to = EMPTY_SQUARE;
	protected byte piece;
	protected byte capture;
	protected byte piecePromotedTo;
	protected byte color;
	protected byte epSquare = EMPTY_SQUARE;
	protected byte castlingType;
	protected byte moveCharacteristic;
	protected byte lastCastleState;
	protected byte previous50MoveCount;

	/**
	 * May or may not be used.
	 */
	protected int fullMoveCount;

	/**
	 * May or may not be used.
	 */
	protected int halfMoveCount;

	/**
	 * May or may not be used. It is obviously not suitable to use this for a
	 * chess engine. That is why it starts out null.
	 */
	protected ArrayList<MoveAnnotation> annotations = null;

	/**
	 * May or may not be used.
	 */
	protected String san;

	/**
	 * Constructor for drop moves.
	 */
	public Move(int to, int piece, int color) {
		this.to = (byte) to;
		this.piece = (byte) piece;
		moveCharacteristic = DROP_CHARACTERISTIC;
		this.color = (byte) color;
	}

	public Move(int from, int to, int piece, int color, int capture) {
		super();

		this.piece = (byte) piece;
		this.color = (byte) color;
		this.capture = (byte) capture;
		this.from = (byte) from;
		this.to = (byte) to;
	}

	public Move(int from, int to, int piece, int color, int capture,
			int moveCharacteristic) {
		super();
		this.piece = (byte) piece;
		this.color = (byte) color;
		this.capture = (byte) capture;
		this.from = (byte) from;
		this.to = (byte) to;
		this.moveCharacteristic = (byte) moveCharacteristic;
	}

	public Move(int from, int to, int piece, int color, int capture,
			int piecePromotedTo, int epSquare, int moveCharacteristic) {
		super();
		this.piece = (byte) piece;
		this.color = (byte) color;
		this.capture = (byte) capture;
		this.from = (byte) from;
		this.to = (byte) to;
		this.piecePromotedTo = (byte) piecePromotedTo;
		this.epSquare = (byte) epSquare;
		this.moveCharacteristic = (byte) moveCharacteristic;
	}

	public void addAnnotation(MoveAnnotation annotation) {
		if (annotations == null) {
			annotations = new ArrayList<MoveAnnotation>(5);
		}
		annotations.add(annotation);
	}

	public MoveAnnotation[] getAnnotations() {
		if (annotations == null) {
			return new MoveAnnotation[0];
		}

		return annotations.toArray(new MoveAnnotation[0]);
	}

	public Arrow[] getArrows() {
		if (annotations == null) {
			return new Arrow[0];
		}

		ArrayList<Arrow> result = new ArrayList<Arrow>(3);
		for (MoveAnnotation annotation : annotations) {
			if (annotation instanceof Arrow) {
				result.add((Arrow) annotation);
			}
		}
		return result.toArray(new Arrow[0]);
	}

	/**
	 * Returns the capture without the promote mask.
	 */
	public int getCapture() {
		return capture & NOT_PROMOTED_MASK;
	}

	public int getCaptureColor() {
		return GameUtils.getOppositeColor(getColor());
	}

	/**
	 * Returns the capture with the promote mask.
	 */
	public int getCaptureWithPromoteMask() {
		return capture;
	}

	public int getColor() {
		return color;
	}

	public Comment[] getComments() {
		if (annotations == null) {
			return new Comment[0];
		}

		ArrayList<Comment> result = new ArrayList<Comment>(3);
		for (MoveAnnotation annotation : annotations) {
			if (annotation instanceof Comment) {
				result.add((Comment) annotation);
			}
		}
		return result.toArray(new Comment[0]);
	}

	public int getEpSquare() {
		return epSquare;
	}

	public int getFrom() {
		return from;
	}

	public int getFullMoveCount() {
		return fullMoveCount;
	}

	public int getHalfMoveCount() {
		return halfMoveCount;
	}

	public Highlight[] getHighlights() {
		if (annotations == null) {
			return new Highlight[0];
		}

		ArrayList<Highlight> result = new ArrayList<Highlight>(3);
		for (MoveAnnotation annotation : annotations) {
			if (annotation instanceof Highlight) {
				result.add((Highlight) annotation);
			}
		}
		return result.toArray(new Highlight[0]);
	}

	public String getLan() {
		return isCastleKSide() ? "O-O" : isCastleQSide() ? "O-O-O"
				: isDrop() ? COLOR_PIECE_TO_CHAR[color].charAt(getPiece())
						+ "@" + SQUARE_TO_FILE_SAN.charAt(getTo())
						+ SQUARE_TO_RANK_SAN.charAt(getTo()) : ""
						+ SQUARE_TO_FILE_SAN.charAt(getFrom())
						+ SQUARE_TO_RANK_SAN.charAt(getFrom())
						+ "-"
						+ SQUARE_TO_FILE_SAN.charAt(getTo())
						+ SQUARE_TO_RANK_SAN.charAt(getTo())
						+ (isPromotion() ? "="
								+ PIECE_TO_SAN.charAt(piecePromotedTo
										& NOT_PROMOTED_MASK) : "");
	}

	public int getLastCastleState() {
		return lastCastleState;
	}

	public int getMoveCharacteristic() {
		return moveCharacteristic;
	}

	public Nag[] getNags() {
		if (annotations == null) {
			return new Nag[0];
		}

		ArrayList<Nag> result = new ArrayList<Nag>(3);
		for (MoveAnnotation annotation : annotations) {
			if (annotation instanceof Nag) {
				result.add((Nag) annotation);
			}
		}
		return result.toArray(new Nag[0]);
	}

	public int getNumAnnotations() {
		return annotations.size();
	}

	public int getNumAnnotationsExcludingSublines() {
		int result = 0;

		for (MoveAnnotation annotation : annotations) {
			if (!(annotation instanceof SublineNode)) {
				result++;
			}
		}
		return result;
	}

	public int getPiece() {
		return piece;
	}

	public int getPiecePromotedTo() {
		return piecePromotedTo;
	}

	public int getPrevious50MoveCount() {
		return previous50MoveCount;
	}

	public RemainingClockTime[] getRemainingClockTimes() {
		if (annotations == null) {
			return new RemainingClockTime[0];
		}

		ArrayList<RemainingClockTime> result = new ArrayList<RemainingClockTime>(
				3);
		for (MoveAnnotation annotation : annotations) {
			if (annotation instanceof RemainingClockTime) {
				result.add((RemainingClockTime) annotation);
			}
		}
		return result.toArray(new RemainingClockTime[0]);
	}

	public String getSan() {
		return san;
	}

	public SublineNode[] getSublines() {
		if (annotations == null) {
			return new SublineNode[0];
		}

		ArrayList<SublineNode> result = new ArrayList<SublineNode>(3);
		for (MoveAnnotation annotation : annotations) {
			if (annotation instanceof SublineNode) {
				result.add((SublineNode) annotation);
			}
		}
		return result.toArray(new SublineNode[0]);
	}

	public int getTo() {
		return to;
	}

	public boolean hasNag() {
		if (annotations == null) {
			return false;
		}

		boolean result = false;
		for (MoveAnnotation annotation : annotations) {
			if (annotation instanceof Nag) {
				result = true;
				break;
			}
		}
		return result;
	}

	public boolean hasSubline() {
		if (annotations == null) {
			return false;
		}

		boolean result = false;
		for (MoveAnnotation annotation : annotations) {
			if (annotation instanceof SublineNode) {
				result = true;
				break;
			}
		}
		return result;
	}

	public boolean isCapture() {
		return getCapture() != GameConstants.EMPTY;
	}

	public boolean isCastleKSide() {
		return (moveCharacteristic & KINGSIDE_CASTLING_CHARACTERISTIC) != 0;
	}

	public boolean isCastleQSide() {
		return (moveCharacteristic & QUEENSIDE_CASTLING_CHARACTERISTIC) != 0;
	}

	public boolean isDrop() {
		return (moveCharacteristic & DROP_CHARACTERISTIC) != 0;
	}

	public boolean isEnPassant() {
		return (moveCharacteristic & EN_PASSANT_CHARACTERISTIC) != 0;
	}

	public boolean isPromotion() {
		return piecePromotedTo != GameConstants.EMPTY;
	}

	public boolean isWhitesMove() {
		return color == GameConstants.WHITE;
	}

	public void removeAnnotation(MoveAnnotation annotation) {
		if (annotations == null) {
			return;
		}
		annotations.remove(annotation);
	}

	public void setCapture(int capture) {
		this.capture = (byte) capture;
	}

	public void setColor(int color) {
		this.color = (byte) color;
	}

	public void setEpSquare(int epSquare) {
		this.epSquare = (byte) epSquare;
	}

	public void setFrom(int from) {
		this.from = (byte) from;
	}

	public void setFullMoveCount(int fullMoveCount) {
		this.fullMoveCount = fullMoveCount;
	}

	public void setHalfMoveCount(int halfMoveCount) {
		this.halfMoveCount = halfMoveCount;
	}

	public void setLastCastleState(int lastCastleState) {
		this.lastCastleState = (byte) lastCastleState;
	}

	public void setMoveCharacteristic(int moveCharacteristic) {
		this.moveCharacteristic = (byte) moveCharacteristic;
	}

	public void setPiece(int piece) {
		this.piece = (byte) piece;
	}

	public void setPiecePromotedTo(int piecePromotedTo) {
		this.piecePromotedTo = (byte) piecePromotedTo;
	}

	public void setPrevious50MoveCount(int previous50MoveCount) {
		this.previous50MoveCount = (byte) previous50MoveCount;
	}

	public void setSan(String san) {
		this.san = san;
	}

	public void setTo(int to) {
		this.to = (byte) to;
	}

	@Override
	public String toString() {
		return getSan() != null ? getSan() : getLan();
	}

}
