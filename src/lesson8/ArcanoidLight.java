package lesson8;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

public class ArcanoidLight extends JComponent {
	private static final long 	serialVersionUID = 7340923076782301730L;
	
	private static final float	GF_WIDTH = 80;
	private static final float	GF_HEIGHT = 60;
	private static final float	BRICK_WIDTH = 2;
	private static final float	BRICK_HEIGHT = 1;
	private static final float	BAR_WIDTH = 1;
	private static final float	BAR_HEIGHT = 5;
	private static final float	BALL_DIAMETER = 1;
	private static final int	TICK_GRAIN = 20;

	private static final Color	BRICK_COLOR = Color.RED;
	private static final Color	BRICK_CONTOUR_COLOR = Color.DARK_GRAY;
	private static final Color	BAR_COLOR = Color.WHITE;
	private static final Color	BAR_CONTOUR_COLOR = Color.CYAN;
	private static final Color	BALL_COLOR = Color.GREEN;
	private static final Color	BALL_CONTOUR_COLOR = Color.DARK_GRAY;

	enum AutomatState {
		STOPPED,
		STARTED,
		PAUSED
	}
	
	enum AutomatEvents {
		START_PRESSED,
		PAUSE_PRESSED,
		ABORT_GAME
	}
	
	private final BufferedImage		image;
	private final ScoreAndControl	sac = new ScoreAndControl();
	private final Timer				t = new Timer(true);
	
	private volatile float 			barYCenter = GF_HEIGHT / 2; 
	private volatile float			ballXCenter = 2*BRICK_WIDTH+BALL_DIAMETER/2; 
	private volatile float			ballYCenter = GF_HEIGHT / 2;
	private volatile float			deltaX = 0.2f;
	private volatile float			deltaY = 0.2f;
	private int						currentScore = 0;
	
	private AutomatState 			currentState = AutomatState.STOPPED;
	private TimerTask				tt;
	
	public ArcanoidLight() throws IOException {
		this.image = ImageIO.read(ArcanoidLight.class.getResource("background.jpg"));	

		setFocusable(true);
		requestFocusInWindow();
		setLayout(null);
		add(sac);
		sac.setLocation(0,0);
		sac.setVisible(true);
		
		getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"start");
		getActionMap().put("start", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sac.startAndStop.doClick();
			}
		});
		getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),"pause");
		getActionMap().put("pause", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sac.pauseAndResume.doClick();
			}
		});
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				moveBar(e.getWheelRotation());
			}
		});
		addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent e) {}
			@Override public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					moveBar(-1);
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP) {
					moveBar(1);
				}
			}
		});
	}


	//
	//	Часть отрисовки игрового поля
	//
	
	@Override
	public void paintComponent(final Graphics g) {	// <2>
		final Graphics2D		g2d = (Graphics2D)g;
		final AffineTransform	oldAt = g2d.getTransform();
		final AffineTransform	at = pickCoordinates(g2d);

		g2d.setTransform(at);
		fillBackground(g2d);

		fillWall(g2d,0,0,GF_WIDTH-BRICK_WIDTH,BRICK_HEIGHT);
		fillWall(g2d,0,GF_HEIGHT-2*BRICK_HEIGHT,GF_WIDTH-BRICK_WIDTH,GF_HEIGHT);
		fillWall(g2d,0,BRICK_HEIGHT,BRICK_WIDTH,GF_HEIGHT-3*BRICK_HEIGHT);
		
		fillBar(g2d,GF_WIDTH-BAR_WIDTH,barYCenter-BAR_HEIGHT/2,GF_WIDTH,barYCenter+BAR_HEIGHT/2);
		fillBall(g2d,ballXCenter,ballYCenter);
		
		g2d.setTransform(oldAt);
	}

	private static void fillBall(final Graphics2D g2d, final float x, final float y) {
		final Color		oldColor = g2d.getColor();
		final Stroke	oldStroke = g2d.getStroke();
		final Ellipse2D	ellipse = new Ellipse2D.Float(x-BALL_DIAMETER/2,y-BALL_DIAMETER/2,BALL_DIAMETER,BALL_DIAMETER);

		g2d.setStroke(new BasicStroke(0.1f));
		g2d.setColor(BALL_COLOR);
		g2d.fill(ellipse);
		g2d.setColor(BALL_CONTOUR_COLOR);
		g2d.draw(ellipse);
		g2d.setColor(oldColor);
		g2d.setStroke(oldStroke);
	}

	private static void fillWall(final Graphics2D g2d, final float xStart, final float yStart, final float xEnd, final float yEnd) {
		final Color		oldColor = g2d.getColor();
		final Stroke	oldStroke = g2d.getStroke();
		
		g2d.setStroke(new BasicStroke(0.1f));
		for (float y = yStart; y <= yEnd; y += BRICK_HEIGHT) {
			for (float x = xStart; x <= xEnd; x += BRICK_WIDTH) {
				final GeneralPath		path = new GeneralPath();
				
				path.moveTo(0,0);
				path.lineTo(BRICK_WIDTH,0);
				path.lineTo(BRICK_WIDTH,BRICK_HEIGHT);
				path.lineTo(0,BRICK_HEIGHT);
				path.closePath();
				
				final AffineTransform	at = new AffineTransform();
				
				at.translate(x,y);
				path.transform(at);
				g2d.setColor(BRICK_COLOR);
				g2d.fill(path);
				g2d.setColor(BRICK_CONTOUR_COLOR);
				g2d.draw(path);
			}
		}
		g2d.setColor(oldColor);
		g2d.setStroke(oldStroke);
	}

	private static void fillBar(final Graphics2D g2d, final float xStart, final float yStart, final float xEnd, final float yEnd) {
		final Color				oldColor = g2d.getColor();
		final Stroke			oldStroke = g2d.getStroke();
		final GeneralPath		path = new GeneralPath();
		
		g2d.setStroke(new BasicStroke(0.1f));
				
		path.moveTo(xStart,yStart);
		path.lineTo(xStart,yEnd);
		path.lineTo(xEnd,yEnd);
		path.lineTo(xEnd,yStart);
		path.closePath();
				
		g2d.setColor(BAR_COLOR);
		g2d.fill(path);
		g2d.setColor(BAR_CONTOUR_COLOR);
		g2d.draw(path);
			
		g2d.setColor(oldColor);
		g2d.setStroke(oldStroke);
	}
	
	private void fillBackground(final Graphics2D g2d) {
		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 5; x++) {
				final AffineTransform	at = new AffineTransform();
				
				at.translate(0.2*GF_WIDTH*x,0.2*GF_HEIGHT*y);
				at.scale(0.2*GF_WIDTH/image.getWidth(),0.2*GF_HEIGHT/image.getHeight());
				g2d.drawImage(image,at,null);				
			}
		}
	}
	
	private AffineTransform pickCoordinates(final Graphics2D g2d) {	// <3>
		final Dimension		screenSize = this.getSize();
		final AffineTransform	result = new AffineTransform();

		result.scale(screenSize.getWidth()/GF_WIDTH, -screenSize.getHeight()/GF_HEIGHT);
		result.translate(0, -GF_HEIGHT);
		return result;
	}

	//
	//	Часть работы игрового автомата
	//
	
	private void automat(final AutomatEvents event) {
		System.err.println("State="+currentState+", term="+event);
		switch (currentState) {
			case PAUSED:
				switch (event) {
					case ABORT_GAME:
						break;
					case PAUSE_PRESSED:
						sac.setState(ScoreState.SS_STARTED);
						currentState = AutomatState.STARTED;
						tt = new TimerTask() {
							@Override
							public void run() {
								tick();
								repaint();
							}
						};
						t.schedule(tt,TICK_GRAIN,TICK_GRAIN);
						break;
					case START_PRESSED:
						tt.cancel();
						sac.setState(ScoreState.SS_STOPPED);
						currentState = AutomatState.STOPPED;
						break;
					default:
						break;
				}
				break;
			case STARTED:
				switch (event) {
					case ABORT_GAME:
						tt.cancel();
						sac.setState(ScoreState.SS_STOPPED);
						sac.setScore(++currentScore);
						currentState = AutomatState.STOPPED;
						break;
					case PAUSE_PRESSED:
						tt.cancel();
						sac.setState(ScoreState.SS_PAUSED);
						currentState = AutomatState.PAUSED;
						break;
					case START_PRESSED:
						tt.cancel();
						sac.setState(ScoreState.SS_STOPPED);
						currentState = AutomatState.STOPPED;
						break;
					default:
						break;
				}
				break;
			case STOPPED:
				switch (event) {
					case ABORT_GAME:
						break;
					case PAUSE_PRESSED:
						break;
					case START_PRESSED:
						currentState = AutomatState.STARTED;
						barYCenter = GF_HEIGHT / 2; 
						ballXCenter = 2*BRICK_WIDTH+BALL_DIAMETER/2; 
						ballYCenter = GF_HEIGHT / 2;
						deltaX = 0.2f;
						deltaY = 0.2f;
						sac.setState(ScoreState.SS_STARTED);
						tt = new TimerTask() {
							@Override
							public void run() {
								tick();
								repaint();
							}
						};
						t.schedule(tt,TICK_GRAIN,TICK_GRAIN);
						break;
					default:
						break;
				}
				break;
			default:
				break;
		}
	}
	
	private void tick() {
		if (deltaY < 0 && ballYCenter <= 2 * BRICK_HEIGHT - BALL_DIAMETER/2) {
			deltaY = -deltaY;
		}
		else if (deltaY > 0 && ballYCenter >= GF_HEIGHT - (2 * BRICK_HEIGHT - BALL_DIAMETER/2)) {
			deltaY = -deltaY;
		}
		if (deltaX < 0 && ballXCenter <= 2 * BRICK_WIDTH - BALL_DIAMETER/2) {
			deltaX = -deltaX;
		}
		else if (deltaX > 0 && ballXCenter >= GF_WIDTH - BAR_WIDTH) {
			if (ballYCenter >= barYCenter - BAR_HEIGHT/2 && ballYCenter <= barYCenter + BAR_HEIGHT/2) {
				deltaX = -deltaX;
			}
			else {
				automat(AutomatEvents.ABORT_GAME);
			}
		}
		ballXCenter += deltaX;
		ballYCenter += deltaY;
		repaint();
	}

	private void moveBar(final int wheelRotation) {
		if (wheelRotation < 0) {
			barYCenter = Math.max(barYCenter+wheelRotation,2*BRICK_HEIGHT+BAR_HEIGHT/2);
		}
		else {
			barYCenter = Math.min(barYCenter+wheelRotation,GF_HEIGHT-(2*BRICK_HEIGHT+BAR_HEIGHT/2));
		}
		repaint();
	}
	
	//
	//	Запуск приложения 
	//
	
	public static void main(String[] args) throws IOException {
		final JFrame	frame = new JFrame("Test ");
		
		frame.getContentPane().add(new ArcanoidLight(),BorderLayout.CENTER);
		frame.setPreferredSize(new Dimension(800,600));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	enum ScoreState {
		SS_STOPPED,
		SS_STARTED,
		SS_PAUSED,
	}
	
	class ScoreAndControl extends JPanel {
		private static final int	IMAGE_CELL = 24;
		private static final int	START_X = 1;
		private static final int	START_Y = 0;
		private static final int	STOP_X = 0;
		private static final int	STOP_Y = 1;
		private static final int	PAUSE_X = 1;
		private static final int	PAUSE_Y = 1;
		private static final int	CONTINUE_X = 2;
		private static final int	CONTINUE_Y = 1;
		
		private final BufferedImage	image; 
		private final JTextField	score = new JTextField();
		private final JButton		startAndStop = new JButton();
		private final JButton		pauseAndResume = new JButton();
		private ScoreState			currentState; 
		
		ScoreAndControl() throws IOException {
			image = ImageIO.read(ArcanoidLight.class.getResource("icon-set-player.png")); 
			
			score.setFont(new Font("Courier new",Font.ITALIC,36));
			score.setBackground(Color.BLACK);
			score.setForeground(Color.GREEN);
			score.setHorizontalAlignment(JTextField.RIGHT);
			score.setColumns(3);
			add(score);
			
			add(startAndStop);
			startAndStop.addActionListener((e)->{automat(AutomatEvents.START_PRESSED);});
			
			add(pauseAndResume);
			pauseAndResume.addActionListener((e)->{automat(AutomatEvents.PAUSE_PRESSED);});
			
			setState(ScoreState.SS_STOPPED);
			setScore(0);
			setSize(200,60);
		}
		
		public void setScore(final int scoreValue) {
			score.setText(""+scoreValue);
		}
		
		public void setState(final ScoreState newState) {
			switch (currentState = newState) {
				case SS_PAUSED:
					pauseAndResume.setIcon(new ImageIcon(image.getSubimage(CONTINUE_X*IMAGE_CELL,CONTINUE_Y*IMAGE_CELL,IMAGE_CELL-1,IMAGE_CELL-1)));
					break;
				case SS_STARTED:
					startAndStop.setIcon(new ImageIcon(image.getSubimage(STOP_X*IMAGE_CELL,STOP_Y*IMAGE_CELL,IMAGE_CELL-1,IMAGE_CELL-1)));
					pauseAndResume.setIcon(new ImageIcon(image.getSubimage(PAUSE_X*IMAGE_CELL,PAUSE_Y*IMAGE_CELL,IMAGE_CELL-1,IMAGE_CELL-1)));
					pauseAndResume.setEnabled(true);
					break;
				case SS_STOPPED:
					startAndStop.setIcon(new ImageIcon(image.getSubimage(START_X*IMAGE_CELL,START_Y*IMAGE_CELL,IMAGE_CELL-1,IMAGE_CELL-1)));
					pauseAndResume.setIcon(new ImageIcon(image.getSubimage(PAUSE_X*IMAGE_CELL,PAUSE_Y*IMAGE_CELL,IMAGE_CELL-1,IMAGE_CELL-1)));
					pauseAndResume.setEnabled(false);
					break;
				default:
					break;
			}
		}
	}
}
