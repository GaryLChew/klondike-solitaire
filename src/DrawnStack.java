import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DrawnStack extends DeckStack {
	
	public DrawnStack(List<Card> cardsList, int locX, int locY) {
		super(cardsList, locX, locY);
		moveX = x;
		moveY = y + cardSpacing * (cards.size() - 1);
	}

	@Override
	public boolean isLegal() {
		if (cards.size() > 3) {
			return false;
		}
		return true;

	}

	@Override
	public boolean clickWithinBounds(MouseEvent click) {

		int clickX = click.getX();
		int clickY = click.getY();
		if (clickX <= x + Card.getCardWidth() && clickY <= y + Card.getCardHeight()+cardSpacing*(cards.size()-1) && clickX >= x
				&& clickY >= y+cardSpacing*(cards.size()-1)) {

			return true;
		}
		return false;
	}

	@Override
	public void pressAction(MouseEvent press) {
		isSelected = true;
	}

	@Override
	public List<Card> successReleaseAction() {
		isSelected = false;

		List<Card> lastCard = new ArrayList<>();
		lastCard.add(cards.remove(cards.size() - 1));
		moveY = y + cardSpacing * (cards.size() - 1);
		return lastCard;
	}

	@Override
	public void failReleaseAction() {
		isSelected = false;
		// set moveX and moveY to its original values
		moveX = x;
		moveY = y + cardSpacing * (cards.size() - 1);
	}

	@Override
	public void update() {
	}

	@Override
	public void dragAction(MouseEvent drag, MouseEvent initialPress) {
		if (clickWithinBounds(initialPress)&&isSelected) {
			int pressX = initialPress.getX();
			int pressY = initialPress.getY();
			int gapX = pressX - x;
			int gapY = pressY - (y + cardSpacing * (cards.size() - 1));

			moveX = drag.getX() - gapX;
			moveY = drag.getY() - gapY;
		}
	}

	@Override
	public void addCard(Card cardToAdd) {
		cardToAdd.setFaceUp();
		cards.add(cardToAdd);
		moveY = y + cardSpacing * (cards.size() - 1);
	}

	@Override
	public void addCards(List<Card> cardsToAdd) {
		for (Card c : cardsToAdd) {
			c.setFaceUp();
			cards.add(c);
			// KEEP IN MIND THIS ONLY EVER ADDS ONE CARD. ONE CARD.
		}
		moveY = y + cardSpacing * (cards.size() - 1);
//		System.out.println("new moveY: " + moveY);
		// set moveX and moveY here, AND in successReleaseAction, when you
		// remove a card
	}

	@Override
	public void draw(Graphics g) {
		// this is already working properly, leave it alone
		if (cards.size() > 0) {
			for (int i = 0; i < cards.size() - 1; i++) {
				cards.get(i).draw(g, x, y + cardSpacing * i);
			}
		}
		if (cards.size() > 0&&!isSelected) {
			cards.get(cards.size() - 1).draw(g, moveX, moveY);
		}
	}

	@Override
	public void drawMovingCards(Graphics g) {
		// you need to set moveX and moveY to their proper values in a different
		// method
		if (cards.size() > 0&&isSelected) {
			cards.get(cards.size() - 1).draw(g, moveX, moveY);
		}
	}
}
