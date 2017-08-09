import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.Timer;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class KlondikeBoard {

	// Maybe later implement code to ensure playable/solvable Game

	boolean gameFinished;

	FullDeckStack fullDeck;

	public List<Stack> stacks;

	public Stack holder = new FullDeckStack(new ArrayList<Card>());

	private MouseEvent initialClick;

	private Image victoryImage = openImagePath("res/images/victory.png");

	private Image gameOverImage = openImagePath("res/images/gameOver.png");
	
	private Image restartImage = openImagePath("res/images/restart.png");

	private Timer soundTimer;
	private int timesSoFar;

	private Timer songTimer;

	public KlondikeBoard() {
		setSizes();
		createStacks();
		playSong();

	}

	private void setSizes() {
		Card.setWidthAndHeight(98, 133);
	}

	private void playSong() {
		AudioStream bgSong = null;
		try {
			URL url = getClass().getResource("res/sounds/bgSong.wav");
			bgSong = new AudioStream(url.openStream());
		} catch (Exception e) {
			System.out.println("Problem opening a sound");
			e.printStackTrace();
		}
		AudioPlayer.player.start(bgSong);
		if (songTimer == null) {
			songTimer = new Timer(190 * 1000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					playSong();
				}
			});
			songTimer.start();
		}
	}

	private void playCardSlideSound() {
		AudioStream cardSlideSound = null;
		try {
			URL url = getClass().getResource("res/sounds/cardSlide2.wav");
			cardSlideSound = new AudioStream(url.openStream());
		} catch (Exception e) {
			System.out.println("Problem opening a sound");
			e.printStackTrace();
		}
		AudioPlayer.player.start(cardSlideSound);
	}

	private void playCardPlaceSound() {
		AudioStream cardPlaceSound = null;
		try {
			URL url = getClass().getResource("res/sounds/cardPlace1.wav");
			cardPlaceSound = new AudioStream(url.openStream());
		} catch (Exception e) {
			System.out.println("Problem opening a sound");
			e.printStackTrace();
		}
		AudioPlayer.player.start(cardPlaceSound);
	}

	private void playMultipleCardSlideSounds(int times, int duration) {
		timesSoFar = -1;
		soundTimer = new Timer(duration / times, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (timesSoFar >= times) {
					soundTimer.stop();
				} else {
					playCardPlaceSound();
					timesSoFar++;
				}
			}
		});
		soundTimer.start();
	}

	private void createStacks() {
		/*
		 * 0-6 PlayStacks 7-10 GoalStacks 11 DeckStacks 12 DrawnStacks
		 */

		/*
		 * SOLITAIRE FORMATTING (O = faceUp, X = faceDown
		 * 
		 * XXXX OOOO OOOO OOOO OOOO OOOO OOOO OOOO OOOO OOOO XXXX OOOO OOOO OOOO
		 * OOOO OOOO OOOO OOOO OOOO OOOO XXXX OOOO OOOO OOOO OOOO OOOO OOOO OOOO
		 * OOOO OOOO +----+ +----+ OOOO +----+ OOOO |OOOO| OOOO |OOOO| |OOOO|
		 * OOOO +----+ OOOO OOOO
		 * 
		 * OOOO OOOO OOOO
		 * 
		 */

		stacks = new ArrayList<>();
		fullDeck = new FullDeckStack();

		for (int i = 1; i <= 7; i++) {
			stacks.add(new PlayStack(fullDeck.dealRandomCardList(i),
					Card.getCardWidth() / 2 + (Card.getCardWidth() + 10) * i, (int) (Card.getCardHeight() / 1.2)));
		}
		for (int i = 0; i < 4; i++) {
			stacks.add(new GoalStack(new ArrayList<Card>(),
					(int) (Card.getCardWidth() * 1.8) + 7 * (Card.getCardWidth() + 10),
					Card.getCardHeight() / 2 + (Card.getCardHeight() + 10) * i));
		}
		stacks.add(new DeckStack(fullDeck.dealRandomCardList(24), (int) (Card.getCardWidth() / 2.9),
				Card.getCardHeight() / 2));
		stacks.add(new DrawnStack(fullDeck.dealRandomCardList(0), (int) (Card.getCardWidth() / 2.9),
				(int) (Card.getCardHeight() * 1.5 + 10)));
	}

	public void pressedAt(MouseEvent press) {
		if (!gameFinished) {
			initialClick = press;
			for (Stack s : stacks) {
				if (s.clickWithinBounds(press)) {
					s.pressAction(press);
				}
			}
		}
		if (30<=press.getX()&&press.getX()<=70) {
			if (630<=press.getY()&&press.getY()<=670) {
				createStacks();
			}
		}
	}

	public void releasedAt(MouseEvent release) {
		// pls forgive me for this mess

		if (!gameFinished) {
			int selectedIndex = -1;
			for (int i = 0; i < stacks.size(); i++) {
				if (stacks.get(i).getIsSelected()) {
					selectedIndex = i;
					break;
				}
			}

			if (selectedIndex == -1) {
				return;
			}
			for (int i = 0; i < stacks.size(); i++) {
				// if this is a click on the deckStack
				if (stacks.get(i).clickWithinBounds(release) && i == selectedIndex && i == 11) {
					// in most cases, deck has Cards
					// stacks.get(i).setIsHighlighted(true);
					if (!stacks.get(11).cards.isEmpty()) {
						// moves top card from deck to Drawn
						stacks.get(12).addCard(stacks.get(11).removeCard(stacks.get(11).cards.size() - 1));
						if (!stacks.get(12).isLegal()) {
							holder.addCard(stacks.get(12).removeCard(0));
							stacks.get(12).moveY -= stacks.get(12).cardSpacing;
						}
						playCardSlideSound();
					}
					// sometimes, deck does not have cards
					else {
						// if drawn has 3 cards
						if (stacks.get(12).cards.size() >= 3) {
							int size = stacks.get(12).cards.size();
							for (int j = 0; j < size; j++) {
								holder.addCard(stacks.get(12).removeCard(0));
							}
							playMultipleCardSlideSounds(3, 250);
						}
						// if drawn does not have 3 cards and holder has some
						// cards
						else if (holder.cards.size() > 0) {
							System.out.println("HERE");
							int size = stacks.get(12).cards.size();
							for (int j = 0; j < size; j++) {
								holder.addCard(stacks.get(12).removeCard(0));
							}
							int hSize = holder.cards.size();
							for (int j = 0; j < hSize; j++) {
								stacks.get(11).addCard(holder.removeCard(holder.cards.size() - 1));
							}
							int timesToPlay = (int) (Math.sqrt(stacks.get(11).cards.size())) + 2;
							playMultipleCardSlideSounds(timesToPlay, timesToPlay * 40);
						}
						// if drawn does not have 3 cards and holder has none
						else {
							System.out.println("HERE");
							int size = stacks.get(12).cards.size();
							for (int j = 0; j < size; j++) {
								holder.addCard(stacks.get(12).removeCard(0));
							}
							int hSize = holder.cards.size();
							for (int j = 0; j < hSize; j++) {
								stacks.get(11).addCard(holder.removeCard(holder.cards.size() - 1));
							}
						}
					}
					System.out.println("HOLDER: " + holder.cards.size() + " DECK: " + stacks.get(11).cards.size()
							+ " DRAWN: " + stacks.get(12).cards.size());
				}

				if (stacks.get(i).clickWithinBounds(release) && i != selectedIndex && i != 11 && i != 12) {
					System.out.println("SUCCESS RELEASE");
					// transfer
					if (i < 7 && stacks.get(i).cards.size() == 0) {
						if (selectedIndex == 12 && stacks.get(selectedIndex).cards
								.get(stacks.get(selectedIndex).cards.size() - 1).pointValue() == 13) {
							System.out.println("KING");
							stacks.get(i).addCards(stacks.get(selectedIndex).successReleaseAction());
							if (!stacks.get(i).isLegal()) {
								System.out.println("ILLEGAL MOVE");
								stacks.get(selectedIndex).addCards(stacks.get(i).successReleaseAction());
							} else {
								playCardSlideSound();
								stacks.get(i).update();
								stacks.get(selectedIndex).update();
							}
						} else if (stacks.get(selectedIndex).cards.get(stacks.get(selectedIndex).indexFirstSelected)
								.pointValue() == 13) {
							System.out.println("KING");
							stacks.get(i).addCards(stacks.get(selectedIndex).successReleaseAction());
							if (!stacks.get(i).isLegal()) {
								System.out.println("ILLEGAL MOVE");
								stacks.get(selectedIndex).addCards(stacks.get(i).successReleaseAction());
							} else {
								playCardSlideSound();
								stacks.get(i).update();
								stacks.get(selectedIndex).update();
							}
						}
					} else {
						stacks.get(i).addCards(stacks.get(selectedIndex).successReleaseAction());
						if (!stacks.get(i).isLegal()) {
							System.out.println("ILLEGAL MOVE");
							stacks.get(selectedIndex).addCards(stacks.get(i).successReleaseAction());
						} else {
							playCardSlideSound();
							stacks.get(i).update();
							stacks.get(selectedIndex).update();
						}
					}
					break;
				}
			}

			if (checkForVictory()) {
				playVictorySound();
			}
			if (checkForGameOver()) {
				playGameOverSound();
			}

			if (selectedIndex != -1) {
				System.out.println("FAIL RELEASE");
				stacks.get(selectedIndex).failReleaseAction();
				return;
			}
		}

	}

	public void draggedAt(MouseEvent drag) {
		if (!gameFinished) {
			for (Stack s : stacks) {
				s.dragAction(drag, initialClick);
			}
		}
	}

	public void draw(Graphics g) {
		for (Stack s : stacks) {
			s.draw(g);
		}
		for (Stack s : stacks) {
			s.drawMovingCards(g);
		}

		if (checkForVictory()) {
			drawVictoryScreen(g);
		}

		if (checkForGameOver()) {
			drawGameOverScreen(g);
		}
		
		drawRestartButton(g);
	}

	public boolean checkForVictory() {
		for (int i = 7; i < 11; i++) {
			if (stacks.get(i).cards.size() < 13) {
				return false;
			}
		}
		gameFinished = true;
		return true;
	}

	public boolean checkForGameOver() {
		return false;
	}

	public void drawVictoryScreen(Graphics g) {
		g.drawImage(victoryImage, 300, 300, 450, 104, null);
	}

	public void drawGameOverScreen(Graphics g) {
		g.drawImage(gameOverImage, 260, 100, 524, 499, null);
	}
	
	public void drawRestartButton(Graphics g) {
		g.drawImage(restartImage, 30, 630, 40, 40, null);
	}

	public void playVictorySound() {
		AudioStream victorySound = null;
		try {
			URL url = getClass().getResource("res/sounds/victorySound.wav");
			victorySound = new AudioStream(url.openStream());
		} catch (Exception e) {
			System.out.println("Problem opening a sound");
			e.printStackTrace();
		}
		AudioPlayer.player.start(victorySound);

	}

	public void playGameOverSound() {
		AudioStream victorySound = null;
		try {
			URL url = getClass().getResource("res/sounds/gameOverSound.wav");
			victorySound = new AudioStream(url.openStream());
		} catch (Exception e) {
			System.out.println("Problem opening a sound");
			e.printStackTrace();
		}
		AudioPlayer.player.start(victorySound);

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
}
