import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PlayStack extends Stack {

	private int indexFirstFaceUp;
	private int faceDownHeight;

	protected int moveHeight;

	// oh god i am so sorry for doing this

	public PlayStack(List<Card> cardsList, int locX, int locY) {
		super(cardsList, locX, locY);
		update();
	}

	public boolean isLegal() {
		if (!cards.isEmpty()) {
			for (int i = indexFirstFaceUp; i < cards.size() - 1; i++) {
				Card c1 = cards.get(i);
				Card c2 = cards.get(i + 1);
				boolean orderCorrect = c1.pointValue() == c2.pointValue() + 1;
				boolean colorsCorrect = ((c1.isRed() && !c2.isRed()) || (!c1.isRed() && c2.isRed()));
				if (!(orderCorrect && colorsCorrect)) {
					return false;
				}
			}
		}
		return true;
	}

	public void draw(Graphics g) {
		drawFaceDown(g);
		drawFaceUp(g);
	}

	private void findIndexFirstFaceUp() {
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).isFaceUp()) {
				indexFirstFaceUp = i;
				return;
			}
		}
	}

	public void addCards(List<Card> cardsToAdd) {
		for (Card c : cardsToAdd) {
			cards.add(c);
		}
		calculateInitialMoveCoords();
	}

	private void calculateInitialMoveCoords() {
		moveX = x;
		moveY = y + (indexFirstSelected * cardSpacing) - faceDownHeight;
	}

	private void calculateDrawLocations() {
		findIndexFirstFaceUp();
		faceDownHeight = (cardSpacing / 2) * indexFirstFaceUp;
	}

	private void drawFaceDown(Graphics g) {
		calculateDrawLocations();
		for (int i = 0; i < indexFirstFaceUp; i++) {
			cards.get(i).draw(g, x, y + (cardSpacing / 2) * i);
		}
	}

	private void drawFaceUp(Graphics g) {
		for (int i = indexFirstFaceUp; i < indexFirstSelected; i++) {
			cards.get(i).draw(g, x, y + faceDownHeight + cardSpacing * (i - indexFirstFaceUp));
		}
	}

	public void drawMovingCards(Graphics g) {
		for (int i = indexFirstSelected; i < cards.size(); i++) {
			cards.get(i).draw(g, moveX, moveY + cardSpacing * (i - indexFirstSelected));
		}
	}

	@Override
	public boolean clickWithinBounds(MouseEvent click) {
		int clickX = click.getX();
		int clickY = click.getY();
		// Should it be < or <=?
		// System.out.println("FDH:" +faceDownHeight);
		// System.out.println((y+faceDownHeight)+" | "+clickY+" | "+(y +
		// cardSpacing * cards.size() - faceDownHeight + cardHeight));
		if (x <= clickX && clickX <= (x + cardWidth) && y + faceDownHeight <= clickY
				&& clickY <= (y + cardSpacing * cards.size() - faceDownHeight + cardHeight)) {
			// System.out.println("WITHIN BOUNDS");
			return true;
		}
		return false;
	}

	@Override
	public void pressAction(MouseEvent press) {
		isSelected = true;
		for (int i = cards.size() - 1; i >= 0; i--) {
			System.out.println(press.getY() + "|" + (i * cardSpacing - faceDownHeight));
			if (press.getY() > y + i * cardSpacing - faceDownHeight) {
				indexFirstSelected = i;
				System.out.println("SELECTED: " + indexFirstSelected);
				break;
			}
		}
		calculateInitialMoveCoords();
	}

	@Override
	public List<Card> successReleaseAction() {
		List<Card> cardsToWithdraw = new ArrayList<>();
		while (indexFirstSelected < cards.size()) {
			cardsToWithdraw.add(cards.remove(indexFirstSelected));
		}
		indexFirstSelected = cards.size();

		isSelected = false;
		return cardsToWithdraw;
	}

	public void failReleaseAction() {
		// animationSlide(50,100,3000);
		indexFirstSelected = cards.size();
		isSelected = false;
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
			int gapY = pressY - (y + (indexFirstSelected * cardSpacing) - faceDownHeight);

			moveX = drag.getX() - gapX;
			moveY = drag.getY() - gapY;
		}
	}

	public void update() {
		findIndexFirstFaceUp();
		indexFirstSelected = cards.size();
		calculateInitialMoveCoords();
		if (cards.size() > 0) {
			cards.get(cards.size() - 1).setFaceUp();
		}
	}

	public void addMoveX(int addition) {
		moveX += addition;
		;
	}

	public void addMoveY(int addition) {
		moveY += addition;
	}

	public void setMoveX(int value) {
		moveX = value;
	}

	public void setMoveY(int value) {
		moveY = value;
	}

	public int getFaceDownHeight() {
		return faceDownHeight;
	}
}
