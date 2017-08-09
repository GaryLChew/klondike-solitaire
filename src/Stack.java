import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

//make abstract
public abstract class Stack {
	protected List<Card> cards = new ArrayList<>();

	// the X and Y coordinates of the entire stack of cards
	protected int x, y;

	// the X and Y coordinates of the top left corner of the moving card/cards
	protected int moveX, moveY;
	protected double preciseX, preciseY;

	public int indexFirstSelected;
	
//	private boolean isHighlighted;

	protected static int cardWidth, cardHeight;

	// used only for DrawnStack and PlayStack
	protected int cardSpacing;

	// When this card is pressed by the mouse, isSelected should be made true
	// When the mouse is released, isSelected should revert back to false
	protected boolean isSelected;

	public Stack() {
		cardWidth = Card.getCardWidth();
		cardHeight = Card.getCardHeight();
		cardSpacing = cardHeight / 5;
	}

	public Stack(List<Card> cardsList, int locX, int locY) {
		cards = cardsList;
		cardWidth = Card.getCardWidth();
		cardHeight = Card.getCardHeight();
		cardSpacing = cardHeight / 5;

		x = locX;
		y = locY;
	}

	public void addCard(Card cardToAdd) {
		cards.add(cardToAdd);
	}

	public void addCards(List<Card> cardsToAdd) {
		for (Card c : cardsToAdd) {
			c.setFaceUp();
			cards.add(c);
		}
	}

	public Card removeCard(int index) {
		return cards.remove(index);
	}

	protected void animationSlide(int destinationX, int destinationY, int animationTime) {
		// animationTime is in terms of ticks (1/1000th of a second)
		// new Timer(t, new ActionListener()) runs once every t/100 seconds
		// possibly make global preciseX and Y variables
		double xStep = ((destinationX - moveX) / 100);
		double yStep = ((destinationY - moveY) / 100);

		preciseX = moveX;
		preciseY = moveY;

		Timer animationTimer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				preciseX += xStep;
				preciseY += yStep;

				moveX = (int) preciseX;
				moveY = (int) preciseY;
				if (moveX > destinationX) {
					moveX = destinationX;
				}
				if (moveY > destinationY) {
					moveY = destinationY;
				}
				System.out.println("Timer actionPerformed");
				// every .1 seconds, change preciseX and preciseY
				// then x1 = y1?, does this work? don't think so
				System.out.println(moveX + ", " + moveY);
				System.out.println(xStep + "|" + yStep);
			}
		});
		animationTimer.start();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int xCoord) {
		x = xCoord;
	}

	public void setY(int yCoord) {
		y = yCoord;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

	// checks if List cards is legal
	public abstract boolean isLegal();

	public abstract boolean clickWithinBounds(MouseEvent click);

	public abstract void pressAction(MouseEvent press);

	public abstract List<Card> successReleaseAction();

	public abstract void failReleaseAction();

	public abstract void update();

	public abstract void dragAction(MouseEvent drag, MouseEvent initialPress);

	// Called in another class
	public abstract void draw(Graphics g);

	// Called in another class
	public abstract void drawMovingCards(Graphics g);

	public void print() {
		for (Card c : cards) {
			System.out.println(c);
		}
	}
	
//	public boolean getIsHighlighted() {
//		return isHighlighted;
//	}
//
//	
//	public void setIsHighlighted(boolean b) {
//		isHighlighted = b;
//	}
}
