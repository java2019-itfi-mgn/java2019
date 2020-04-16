package lesson7;

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
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class Asterisks extends JComponent {
	private static final long  serialVersionUID = 4066983457124156696L; // <1> 
	private static final String		HELLO_WORLD = "Hello, world!";
	private static final double		ASTERISK_CYRCLE_RADIUS = 0.4;
	private static final double		ASTERISK_SIZE = 0.05;
	private static final double		LETTER_SIZE = 0.075;
	private static final double		WINDOW_SIZE = 1;
	private static final float		LINE_WIDTH = 0.01f;
	private static final double[][]	NODES = new double[][]{
								new double[]{1.0, 0.0},
								new double[]{0.25, 0.25},
								new double[]{0.0, 1.0},
								new double[]{-0.25, 0.25},
								new double[]{-1.0, 0.0},
								new double[]{-0.25, -0.25},
								new double[]{0.0, -1.0},
								new double[]{0.25, -0.25},
								new double[]{1.0, 0.0}
							};
	
	@Override
	public void paintComponent(final Graphics g) {	// <2>
		final Graphics2D		g2d = (Graphics2D)g;
		final AffineTransform	oldAt = g2d.getTransform();
		final AffineTransform	at = pickCoordinates(g2d);
		final int				stringSize = HELLO_WORLD.length();

		g2d.setTransform(at);
		fillBackground(g2d);
		
		for (int index = 0; index < stringSize; index++) {
			paintAsterisk(g2d, ASTERISK_CYRCLE_RADIUS,
			 index * 180 / stringSize, ASTERISK_SIZE);
		}

		for (int index = 0; index < stringSize; index++) {
			paintLetter(g2d, ASTERISK_CYRCLE_RADIUS, 
			180 + index * 180 / stringSize, LETTER_SIZE
			, HELLO_WORLD.charAt(index));
		}
		g2d.setTransform(oldAt);
	}

	private AffineTransform pickCoordinates(final Graphics2D g2d) {	// <3>
		final Dimension		screenSize = this.getSize();
		final AffineTransform	result = new AffineTransform();

		result.scale(screenSize.getWidth()/WINDOW_SIZE
				, -screenSize.getHeight()/WINDOW_SIZE);
		result.translate(WINDOW_SIZE/2, -WINDOW_SIZE/2);
		return result;
	}
	
	private void fillBackground(final Graphics2D g2d) {		// <4>
		final RadialGradientPaint rgp = new RadialGradientPaint(0.0f, 0.0f
			, (float)(0.75f*WINDOW_SIZE)
			, new float[]{0.0f, 1.0f}
			, new Color[]{Color.YELLOW, Color.BLACK});
		final Rectangle2D.Double	r2d = new Rectangle2D.Double(
			-WINDOW_SIZE/2,-WINDOW_SIZE/2
			,WINDOW_SIZE, WINDOW_SIZE);
		final Paint			oldPaint = g2d.getPaint();
		
		g2d.setPaint(rgp);
		g2d.fill(r2d);
		g2d.setPaint(oldPaint);
	}

	private void paintAsterisk(final Graphics2D g2d, final double radius
			, final double angle, final double scale) { // <5>
		final GeneralPath		aster = new GeneralPath();
		final Color			oldColor = g2d.getColor();
		final Stroke		oldStroke = g2d.getStroke();
		final AffineTransform	at = new AffineTransform();
		
		aster.moveTo(NODES[0][0], NODES[0][1]);
		for (int index = 1; index < NODES.length; index++) {
			aster.lineTo(NODES[index][0], NODES[index][1]);
		}
		aster.closePath();
		
		at.translate(radius*Math.cos(Math.PI*angle/180)
				, radius*Math.sin(Math.PI*angle/180));
		at.scale(scale,scale);
		at.rotate(Math.PI*angle/180);
		aster.transform(at);
		
		g2d.setColor(Color.RED);
		g2d.setStroke(new BasicStroke(LINE_WIDTH));
		g2d.draw(aster);
		g2d.setColor(Color.WHITE);
		g2d.fill(aster);
		
		g2d.setColor(oldColor);
		g2d.setStroke(oldStroke);
	}
	
	private void paintLetter(final Graphics2D g2d, final double radius
		, final double angle, final double scale, final char symbol) {// <6>
		final Color			oldColor = g2d.getColor();
		final Font			oldFont = g2d.getFont();
		final AffineTransform	oldAt = g2d.getTransform();
		final AffineTransform	at = new AffineTransform(oldAt);
		
		at.translate(radius*Math.cos(Math.PI*angle/180)
			, radius*Math.sin(Math.PI*angle/180));
		at.rotate(Math.PI*(angle + 90)/180);
		at.scale(scale,scale);

		g2d.setFont(new Font("Arial",Font.BOLD,1));
		g2d.setColor(Color.CYAN);
		g2d.setTransform(at);
		g2d.drawString(new String(new char[]{symbol}),0,0);
		
		g2d.setColor(oldColor);
		g2d.setFont(oldFont);
		g2d.setTransform(oldAt);
	}

	public static void main(String[] args) {
		final JFrame	frame = new JFrame("Test ");
		
		frame.setPreferredSize(new Dimension(200,200));
		frame.getContentPane().add(new Asterisks(),BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}