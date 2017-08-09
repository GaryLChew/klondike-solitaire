import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class GoalStack extends Stack {
	private static Image emptyImage;

	private int previousSize;

	public GoalStack(List<Card> cardsList, int locX, int locY) {
		super(cardsList, locX, locY);
		moveX = x;
		moveY = y;
		for (Card c : cards) {
			c.setFaceUp();
		}
	}

	@Override
	public boolean isLegal() {
		if (cards.size() >= 2) {
			Card c1 = cards.get(cards.size() - 1);
			Card c2 = cards.get(cards.size() - 2);
			boolean orderCorrect = (c2.pointValue() + 1 == c1.pointValue());
			boolean suitsCorrect = (c1.suit() == c2.suit());
			if ((orderCorrect && suitsCorrect)) {
				return true;
			}
			// return false;

		} else if (cards.get(0).pointValue() == 1 && cards.size() == 1)
			return true;
		return false;
	}

	@Override
	public boolean clickWithinBounds(MouseEvent click) {
		int clickX = click.getX();
		int clickY = click.getY();
		if (x <= clickX && clickX <= (x + cardWidth) && clickY <= (y + cardHeight) && clickY >= y) {
			return true;
		}
		return false;
	}

	@Override
	public void pressAction(MouseEvent press) {
		isSelected = true;
	}

	@Override
	public void dragAction(MouseEvent drag, MouseEvent initialPress) {
		// instead clickWithinBounds(initialPress)? or (drag)?
		// perhaps setFaceUpVariablesToNormal is unnecessary or needs to be
		// changed
		if (clickWithinBounds(initialPress)) {
			int pressX = initialPress.getX();
			int pressY = initialPress.getY();
			int gapX = pressX - x;
			int gapY = pressY - y;

			moveX = drag.getX() - gapX;
			moveY = drag.getY() - gapY;
		}
	}

	@Override
	public void draw(Graphics g) {
		if (emptyImage == null) {
			String filePath = "res/images/cards/cardBack_blue3.png";
			try {
				URL url = getClass().getResource(filePath);
				System.out.println(filePath);
				emptyImage = ImageIO.read(url);
			} catch (IOException e) {
				System.out.println("Problem opening the image at " + filePath);
				e.printStackTrace();
			}

		}
		if (cards.size() <= 1) {
			g.drawImage(emptyImage, x, y, Card.getCardWidth(), Card.getCardHeight(), null);

		} else if (cards.size() >= 2) {
			cards.get(cards.size() - 2).draw(g, x, y);
		}
		if (cards.size() != 0 && !isSelected) {
			cards.get(cards.size() - 1).draw(g, moveX, moveY);
		}
	}

	@Override
	public void drawMovingCards(Graphics g) {
		if (cards.size() != 0 && isSelected) {
			cards.get(cards.size() - 1).draw(g, moveX, moveY);
		}

	}

	@Override
	public List<Card> successReleaseAction() {
		List<Card> cardsToRemove = new ArrayList<Card>();
		if (isLegal()) {
			System.out.println("REMOVED LAST");
			//FIX
			cardsToRemove.add(cards.remove(cards.size()-1));
		} else {
			int s = cards.size();
			for (int i = previousSize; i < s; i++) {
				System.out.println(previousSize+"||"+cards.size());
				System.out.println("I: "+i);
				cardsToRemove.add(cards.remove(previousSize));
			}
		}
		return cardsToRemove;
	}

	@Override
	public void failReleaseAction() {
		moveX = x;
		moveY = y;
		isSelected = false;
	}
	
	@Override
	public void addCards(List<Card> cardsToAdd) {
		previousSize = cards.size();
		for (Card c:cardsToAdd) {	
			cards.add(c);
		}
	}

	@Override
	public void update() {
	}

}