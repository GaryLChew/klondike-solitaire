import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class DeckStack extends Stack {

	Image emptyImage = null;

	public DeckStack(List<Card> cardsList, int locX, int locY) {
		super(cardsList, locX, locY);
		moveX = x;
		moveY = y;
	}

	@Override
	public boolean isLegal() {
		if (cards.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean clickWithinBounds(MouseEvent click) {
		int clickX = click.getX();
		int clickY = click.getY();
		if (clickX <= x + Card.getCardWidth() && clickY <= y + Card.getCardHeight() && clickX >= x && clickY >= y) {

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
		List<Card> newCard = new ArrayList<>();
		if (cards.size() > 0) {
			newCard.add(cards.remove(0));
			newCard.get(0).setFaceUp();
		}
		return newCard;
	}

	@Override
	public void failReleaseAction() {
		isSelected = false;
	}

	@Override
	public void addCard(Card cardToAdd) {
		cardToAdd.setFaceDown();
		cards.add(cardToAdd);
	}

	@Override
	public void addCards(List<Card> cardsToAdd) {
		for (int i = 0; i < cardsToAdd.size(); i++) {
			cardsToAdd.get(i).setFaceDown();
			cards.add(cardsToAdd.get(i));
		}
	}

	@Override
	public void update() {
	}

	@Override
	public void dragAction(MouseEvent drag, MouseEvent initialPress) {
	}

	@Override
	public void draw(Graphics g) {
		if (cards.size() > 0) {
			Card a = cards.get(cards.size() - 1);
			a.draw(g, moveX, moveY);
		} else {
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
			g.drawImage(emptyImage, x, y, Card.getCardWidth(), Card.getCardHeight(), null);
		}
	}

	@Override
	public void drawMovingCards(Graphics g) {

	}

}
