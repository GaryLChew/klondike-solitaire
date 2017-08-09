import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class FullDeckStack extends Stack {
	
	public FullDeckStack() {
		//Creates in-order deck of 52 cards
		for (int i = 1;i<=13;i++) {
			cards.add(new Card(i,"Spades",false));
			cards.add(new Card(i,"Clubs",false));
			cards.add(new Card(i,"Diamonds",false));
			cards.add(new Card(i,"Hearts",false));
		}
	}
	
	public FullDeckStack(List<Card> cardsList) {
		cards = cardsList;
	}
	
	public Card dealRandomCard() {
		return cards.remove((int) (Math.random() * cards.size()));
	}

	//LATER CHANGE TO USE INSERT/TRANSFER CARDS
	public List<Card> dealRandomCardList(int numCards) {
		List<Card> randomCards = new ArrayList<>();
		for (int i = 0; i < numCards; i++) {
			randomCards.add(dealRandomCard());
		}
		return randomCards;
	}

	@Override
	public void pressAction(MouseEvent press) {
	}

	@Override
	public void dragAction(MouseEvent drag, MouseEvent initialClick) {
	}

	@Override
	public boolean isLegal() {
		
		return false;
		
	}

	@Override
	public boolean clickWithinBounds(MouseEvent click) {
		return false;
	}

	@Override
	public List<Card> successReleaseAction() {
		
		return null;
		
	}

	@Override
	public void failReleaseAction() {
	}

	@Override
	public void update() {
	}

	@Override
	public void draw(Graphics g) {
	}

	@Override
	public void drawMovingCards(Graphics g) {
	}
}
