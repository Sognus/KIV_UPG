package cz.zcu.viteja.upg;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

/**
 * T��da reprezentuj�c� panel, kter� je sou��sti hlavn�ho okna aplikace. T��da
 * zaji�tuje vykreslov�n� komponent (st�elec, c�l, oblast z�sahu).
 * 
 * @author Jakub V�tek - A16B0165P
 * @version 1.02.00
 *
 */
public class GamePanel extends JPanel {

	/** Serial version UID */
	private static final long serialVersionUID = 1L;

	/** Rerefence na instanci reprezentuj�c� hern� t�r�n */
	public Terrain terrain;
	/** Rerefence na instanci reprezentuj�c� st�elce */
	public NamedPosition shooter;
	/** Rerefence na instanci reprezentuj�c� c�l */
	public NamedPosition target;
	/** Rerefence na instanci reprezentuj�c� oblast z�sahu */
	public NamedPosition hitSpot;

	/**
	 * Z�kladn� konstruktor t��dy GamePanel. Objekty, kter� jsou p�ed�ny jako
	 * argumenty jsou ulo�eny do prom�nn�ch instance
	 * 
	 * @param terrain
	 *            instance hern�ho ter�nu
	 * @param shooter
	 *            instance st�elce
	 * @param target
	 *            instance c�lu
	 */
	public GamePanel(Terrain terrain, NamedPosition shooter, NamedPosition target) {
		this.terrain = terrain;
		this.shooter = shooter;
		this.target = target;
	}

	/**
	 * Metoda, kter� zaji�tuje vol�n� metod kreslen� pro v�echny kl��ov�
	 * komponenty (ter�n, st�elec, c�l, oblast z�sahu)
	 * 
	 * @param g
	 *            grafick� kontext
	 */
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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

		if (hitSpot != null) {

			hitSpot.draw(g2, scale);

		}

	}

	/**
	 * Metoda vracej�c� hodnotu, kter� reprezentuje men�� z hodnot pom�ru
	 * rozm�r� okna a ter�nu. Prakticky vrac� po�et metr� na pixel. Kdy� se pak
	 * j�k�koliv velikost v metrech n�sob� touto hodnotou, dostaneme hodnotu v
	 * pixelech
	 * 
	 * @return hodnota pro p�evod metr� na pixely na z�klad� velikost� okna a
	 *         ter�nu
	 */
	public double getScale() {
		double cosiX = (this.getWidth() - 20) / this.terrain.getWidthInM();
		double cosiY = (this.getHeight() - 20) / this.terrain.getHeightInM();

		// System.out.println(cosiX + " VS " + cosiY);

		return Math.min(cosiX, cosiY);
	}

	/**
	 * Setter, kter� p�ep�e sou�asnou referenci na instanci oblasti z�sahu
	 * referenc� p�edanou jako argument funkce
	 * 
	 * @param hitSpot
	 *            nov� reference na oblast z�sahu
	 */
	public void setHitSpot(NamedPosition hitSpot) {
		this.hitSpot = hitSpot;

	}

}
