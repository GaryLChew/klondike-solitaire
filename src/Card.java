import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Card {

	private int pointValue;

	private String suit;

	private String rank;

	private Image img;

	private static int width;

	private static int height;

	private boolean isFaceUp;

	public Card(int cardPointValue, String cardSuit, boolean cardIsFaceUp) {
		pointValue = cardPointValue;
		suit = cardSuit;
		isFaceUp = cardIsFaceUp;
		if (2 <= pointValue && pointValue <= 10)
			rank = "" + pointValue;
		else if (pointValue == 1)
			rank = "A";
		else if (pointValue == 11)
			rank = "J";
		else if (pointValue == 12)
			rank = "Q";
		else if (pointValue == 13)
			rank = "K";
		openImage();
	}

	public String suit() {
		return suit;
	}

	public int pointValue() {
		return pointValue;
	}

	public boolean isRed() {
		if (suit.equals("Diamonds") || suit.equals("Hearts"))
			return true;
		return false;
	}

	public boolean isFaceUp() {
		return isFaceUp;
	}

	public void setFaceDown() {
		isFaceUp = false;
		openImage();
	}
	
	public void setFaceUp() {
		isFaceUp = true;
		openImage();
	}

	public void draw(Graphics g, int x, int y) {
		g.drawImage(img, x, y, width, height, null);
	}

	public void openImage() {
		if (isFaceUp()) {
			img = openImagePath("res/images/cards/card" + suit + rank + ".png");
		}
		else {
			img = openImagePath("res/images/cards/cardBack_blue4.png");
		}
	}

	public Image openImagePath(String filePath) {
		Image img = null;
		if (filePath.substring(filePath.length() - 4).equals(".gif")) {
			try {
				URL url = getClass().getResource(filePath);
				img = new ImageIcon(url).getImage();
			} catch (Exception e) {
				System.out.println("Problem opening the image at " + filePath);
				e.printStackTrace();
			}
		} else {
			try {
				URL url = getClass().getResource(filePath);
				img = ImageIO.read(url);
			} catch (IOException e) {
				System.out.println("Problem opening the image at " + filePath);
				e.printStackTrace();
			}
		}
		return img;
	}
	
	public static void setWidthAndHeight(int w, int h) {
		width = w;
		height = h;
	}
	
	public static int getCardWidth() {
		return width;
	}

	public static int getCardHeight() {
		return height;
	}
	
	@Override
	public String toString() {
		return rank + " of " + suit + " (point value = " + pointValue + ")";
	}
}
