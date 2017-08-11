import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.Timer;


public class KlondikePanel extends JPanel {

	Dimension dim = new Dimension(1060,700);
	Color backGround = new Color(0, 220, 235);
	KlondikeBoard board = new KlondikeBoard();
	
	Timer repaintTimer;

	
	public KlondikePanel() {
		this.setPreferredSize(dim);
		this.setBackground(backGround);
		setUpMouseListeners();
		createRepaintTimer();
	}
	
	private void createRepaintTimer() {
		repaintTimer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		repaintTimer.start();
		
	}
	
	
	
	private void setUpMouseListeners() {
		setUpML();
		setUPMML();
	}



	private void setUPMML() {
		// MouseMotionListener allows for listening for dragging
		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent drag) {
//				System.out.println("Dragged at X:" + drag.getX()+", Y:"+drag.getY());
				board.draggedAt(drag);
				repaint();
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				
			}
			
		});
	}



	private void setUpML() {
		// MouseListeners allow for listening for clicks
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent press) {
//				System.out.println("Pressed at X:" + press.getX()+", Y:"+press.getY());
				board.pressedAt(press);
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent release) {
				// TODO Auto-generated method stub
//				System.out.println("Released at X:" + release.getX()+", Y:"+release.getY());
				board.releasedAt(release);
				repaint();
			}
			
		});
		this.requestFocusInWindow();
	}



	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		board.draw(g);;
	}
	
}
