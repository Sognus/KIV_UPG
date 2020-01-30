package cz.zcu.viteja.upg;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

/**
 * Tøída reprezentující panel, který je souèásti hlavního okna aplikace. Tøída
 * zajištuje vykreslování komponent (støelec, cíl, oblast zásahu).
 * 
 * @author Jakub Vítek - A16B0165P
 * @version 1.02.00
 *
 */
public class GamePanel extends JPanel {

	/** Serial version UID */
	private static final long serialVersionUID = 1L;

	/** Rerefence na instanci reprezentující herní térén */
	public Terrain terrain;
	/** Rerefence na instanci reprezentující støelce */
	public NamedPosition shooter;
	/** Rerefence na instanci reprezentující cíl */
	public NamedPosition target;
	/** Rerefence na instanci reprezentující oblast zásahu */
	public NamedPosition hitSpot;
	/** Reference na trajektorii */
	public Trajectory trajectory;

	/**
	 * Základní konstruktor tøídy GamePanel. Objekty, které jsou pøedány jako
	 * argumenty jsou uloženy do promìnných instance
	 * 
	 * @param terrain
	 *            instance herního terénu
	 * @param shooter
	 *            instance støelce
	 * @param target
	 *            instance cílu
	 */
	public GamePanel(Terrain terrain, NamedPosition shooter, NamedPosition target) {
		this.terrain = terrain;
		this.shooter = shooter;
		this.target = target;
	}

	/**
	 * Metoda, která zajištuje volání metod kreslení pro všechny klíèové
	 * komponenty (terén, støelec, cíl, oblast zásahu)
	 * 
	 * @param g
	 *            grafický kontext
	 */
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHints(hints);

		g2.translate(10, 10);

		double scale = this.getScale();

		if (terrain != null) {

			terrain.draw(g2, scale);
			g2.setClip(new Rectangle2D.Double(0, 0, terrain.getWidthInM() * scale, terrain.getHeightInM() * scale));
		}

		if (shooter != null) {

			shooter.draw(g2, scale);

		}

		if (target != null) {

			target.draw(g2, scale);

		}

		if (trajectory != null) {
			trajectory.draw(g2, scale);
		}

		if (hitSpot != null) {

			hitSpot.draw(g2, scale);

		}

	}

	/**
	 * Metoda vracející hodnotu, která reprezentuje menší z hodnot pomìru
	 * rozmìrù okna a terénu. Prakticky vrací poèet metrù na pixel. Když se pak
	 * jákákoliv velikost v metrech násobí touto hodnotou, dostaneme hodnotu v
	 * pixelech
	 * 
	 * @return hodnota pro pøevod metrù na pixely na základì velikostí okna a
	 *         terénu
	 */
	public double getScale() {
		double cosiX = (this.getWidth() - 20) / this.terrain.getWidthInM();
		double cosiY = (this.getHeight() - 20) / this.terrain.getHeightInM();

		// System.out.println(cosiX + " VS " + cosiY);

		return Math.min(cosiX, cosiY);
	}

	/**
	 * Setter, který pøepíše souèasnou referenci na instanci oblasti zásahu
	 * referencí pøedanou jako argument funkce
	 * 
	 * @param hitSpot
	 *            nová reference na oblast zásahu
	 */
	public void setHitSpot(NamedPosition hitSpot) {
		this.hitSpot = hitSpot;

	}

}
