package cz.zcu.viteja.upg;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Panel pro vykreslování kompasu, který ukazuje smìr a velikost vìtru
 * 
 * @author Jakub Vítek A16B0165P
 * @version 1.00.00
 *
 */
public class CompassPanel extends JPanel implements Observer {

	/** Reference na instanci vìtru */
	public Wind wind;

	/**
	 * Konstruktor panelu
	 * 
	 * @param wind
	 *            instance vìtru
	 */
	public CompassPanel(Wind wind) {
		this.wind = wind;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		double stredX = Game.gamePanel.terrain.getWidthInPixels() / 2;
		double stredY = Game.gamePanel.terrain.getHeightInPixels() / 2;
		double prumer = Game.gamePanel.terrain.getHeightInPixels() / 100 * 70;

		Graphics2D g2 = (Graphics2D) g;
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHints(hints);

		g2.translate(10, 10);

		if (wind != null) {
			// Vykreslení elipsy
			g2.draw(new Line2D.Double(stredX - 5, stredY, stredX + 5, stredY));
			g2.draw(new Line2D.Double(stredX, stredY + 5, stredX, stredY - 5));
			g2.setColor(Color.WHITE);
			g2.fill(new Ellipse2D.Double(stredX - prumer / 2, stredY - prumer / 2, prumer, prumer));
			g2.setColor(Color.GRAY);
			g2.draw(new Ellipse2D.Double(stredX - prumer / 2, stredY - prumer / 2, prumer, prumer));

			// Nastavení nového fontu
			g2.setColor(Color.BLACK);
			Font font = new Font("Courier", Font.TRUETYPE_FONT, 28);
			g2.setFont(font);

			// Výpoèet rozmìrù textu na základì fontu
			FontMetrics fm = g2.getFontMetrics();
			int stringHeight = fm.getHeight();
			int stringWidthW = fm.stringWidth("W");
			int stringWidthN = fm.stringWidth("N");
			int stringWidthS = fm.stringWidth("S");

			// Vykreslení ukazatelù svìtových smìrù
			g2.drawString("E", (float) (stredX + prumer / 2) + 5, (float) (stredY + 1 * stringHeight / 4));
			g2.drawString("W", (float) (stredX - prumer / 2) - 5 - stringWidthW,
					(float) (stredY + 1 * stringHeight / 4));
			g2.drawString("N", (float) (stredX - stringWidthN / 2), (float) (stredY - prumer / 2 - 5));
			g2.drawString("S", (float) (stredX - stringWidthS / 2),
					(float) (stredY + prumer / 2 + 5 + stringHeight / 2));

			// Vykreslení støelky kompasu
			AffineTransform at = g2.getTransform();

			g2.translate(stredX, stredY);
			g2.rotate(-wind.getAzimuth());

			double strelkaPolomer = Game.gamePanel.terrain.getHeightInPixels() / 100 * 5;

			Path2D strelkaCervena = new Path2D.Double();
			strelkaCervena.moveTo(0, 0);
			strelkaCervena.lineTo(0, 0 + strelkaPolomer);
			strelkaCervena.lineTo(0 + prumer / 2, 0);
			strelkaCervena.lineTo(0, 0 - strelkaPolomer);
			strelkaCervena.closePath();

			Path2D strelkaModra = new Path2D.Double();
			strelkaModra.moveTo(0, 0);
			strelkaModra.lineTo(0, 0 + strelkaPolomer);
			strelkaModra.lineTo(0 - prumer / 2.5, 0);
			strelkaModra.lineTo(0, 0 - strelkaPolomer);
			strelkaModra.closePath();

			g2.setColor(Color.RED);
			g2.fill(strelkaCervena);
			g2.setColor(new Color(0, 102, 204, 255));
			g2.fill(strelkaModra);

			try {
				File f = new File(Constants.compassMiddleImagePath);
				BufferedImage img = ImageIO.read(f);

				Ellipse2D el = new Ellipse2D.Double(0 - strelkaPolomer + 3, 0 - strelkaPolomer + 3,
						strelkaPolomer * 2 - 6, strelkaPolomer * 2 - 6);
				g2.setClip(el);
				g2.drawImage(img, (int) (-1 * strelkaPolomer * 10), (int) (-1 * strelkaPolomer * 10),
						(int) (strelkaPolomer * 20), (int) (strelkaPolomer * 20), null);

			} catch (Exception e) {
				g2.setColor(Color.WHITE);
				g2.fill(new Ellipse2D.Double(0 - strelkaPolomer + 3, 0 - strelkaPolomer + 3, strelkaPolomer * 2 - 6,
						strelkaPolomer * 2 - 6));
			}
			g2.setClip(null);

			g2.setColor(Color.GRAY);
			Stroke dotted = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 1, 2 },
					0);
			g2.setStroke(dotted);
			g2.draw(new Line2D.Double(0, 0 + strelkaPolomer - 3, 0, 0 - strelkaPolomer + 3));
			g2.draw(new Line2D.Double(0 + strelkaPolomer - 3, 0, 0 - strelkaPolomer + 3, 0));

			g2.setTransform(at);

			// Vykreslení textu rychlosti
			font = new Font("Courier", Font.TRUETYPE_FONT, 20);
			g2.setFont(font);
			g2.setColor(Color.GREEN.darker().darker());

			// Výpoèet rozmìrù textu na základì fontu
			fm = g2.getFontMetrics();
			String text = String.format("%.0f km/h ", wind.getVelocity());
			int textWidth = fm.stringWidth(text);

			if (fm.getHeight() > (strelkaPolomer * 2 - 10) || textWidth > (strelkaPolomer * 2 - 10)) {
				g2.drawString(text, (float) (stredX - textWidth / 2),
						(float) (stredY + prumer / 2 + 5 + stringHeight + 5));
			} else {

				g2.drawString(text, (float) (stredX - textWidth / 2), (float) (stredY + fm.getHeight() / 4));
			}

			// Vykreslení pomocných èar
			g2.setColor(Color.LIGHT_GRAY);
			Stroke dotted0 = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 1, 2 },
					0);
			g2.setStroke(dotted0);

			g2.draw(new Line2D.Double(stredX, stredY, (float) (stredX + prumer / 2) + 5,
					(float) (stredY + 0 * stringHeight / 2)));

			g2.draw(new Line2D.Double(stredX, stredY, (float) (stredX - prumer / 2) - 5,
					(float) (stredY + 0 * stringHeight / 2)));

			g2.draw(new Line2D.Double(stredX, stredY, stredX, stredY - prumer / 2 - 5));

			g2.draw(new Line2D.Double(stredX, stredY, stredX, stredY + prumer / 2 + 5));

		}
	}

	/**
	 * Pokud je zmìnìn jakýkoliv parametr v instanci vìtru, panel bude
	 * pøekreslen
	 */
	@Override
	public void update(Observable o, Object arg) {
		this.repaint();

	}

}
