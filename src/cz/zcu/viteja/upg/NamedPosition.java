package cz.zcu.viteja.upg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/**
 * T��da, jej� instance reprezentuj� pozici a dal�� d�le�it� vlastnosti v�ech
 * kl��ov�ch pozicovateln�ch objekt� (st�elec, c�l, oblast z�sahu).
 * 
 * @author Jakub V�tek A16B0165P
 * @version 1.01.00
 *
 */
public class NamedPosition {

	// Konstanty
	/** pro p�evod mm na m */
	static final double mmToM = 1000.0;
	/** pro p�evod m na mm */
	static final double mToMM = 0.001;

	// Instan�n� prom�nn�
	/** Sou�adnice objektu na ose X (v metrech) */
	public double x;
	/** Sou�adnice objektu na ose Y (v metrech) */
	public double y;

	/**
	 * �et�zec reprezentuj�c� typ objektu (zda se jedn� o c�l, st�elce nebo
	 * oblast z�sahu
	 */
	public String positionType;
	/** Barva, kter� se vyu��v� p�i vykreslov�n� objektu */
	public Color color;
	/**
	 * Velikost objektu (pro st�elce a c�l = velikost �se�ky, pro oblast z�sahu
	 * = pr�m�r kruhu
	 */
	public double size;

	/**
	 * Konstruktor t��dy NamedPosition, kter� ulo�� vstupn� parametry do jejich
	 * koresponduj�c�ch prom�nn�ch
	 * 
	 * @param x
	 *            sou�adnice na ose X (v metrech)
	 * @param y
	 *            sou�adnice na ose Y (v metrech)
	 * @param positionType
	 *            typ objektu (st�elec, c�l nebo oblast z�sahu)
	 * @param color
	 *            Barva pou�it� p�i vykreslov�n� objektu
	 * @param size
	 *            Velikost objektu (pro c�l a st�elce velikost �se�ky, pro
	 *            oblast z�sahu pr�m�r kruhu)
	 */
	public NamedPosition(double x, double y, String positionType, Color color, double size) {
		this.x = x;
		this.y = y;
		this.positionType = positionType;
		this.color = color;
		this.size = size;
	}

	/**
	 * Vypo��t� vzd�lenost mezi dv�mi pojmenovan�mi pozicemi a vrat� v�slednou
	 * hodnotu jako primitivn� typ double
	 * 
	 * @param position
	 *            pojmenovan� pozice, ke kter� se bude po��tat vzd�lenost od
	 *            sou�asn� pozice
	 * @return vzd�lenost mezi sou�asnou a c�lovou pozic� (v metrech)
	 */
	public double getDistance(NamedPosition position) {
		return Math.sqrt(Math.pow(position.x - this.x, 2) + Math.pow(position.y - this.y, 2));
	}

	/**
	 * 
	 * Metoda, kter� se na z�klad� vnit�n�ho stavu objektu rothodne, zda se je
	 * objekt st�elcem/c�lem nebo oblast� z�sahu, na z�klad� toho pak zavol�
	 * koresponduj�c� vykreslovac� metodu
	 * 
	 * @param g2
	 *            grafick� kontext
	 * @param scale
	 *            prom�nn� d�ky kter� lze p�ev�st metry na pixely
	 */
	public void draw(Graphics2D g2, double scale) {

		switch (this.positionType) {
		case Constants.SHOOTER:
		case Constants.TARGET:
			this.drawTargetShooter(g2, scale);
			break;
		case Constants.HITSPOT:
			this.drawHitspot(g2, scale);
			break;

		}

	}

	/**
	 * Metoda kter� vykresluje oblast z�sahu
	 * 
	 * @param g2
	 *            grafick� kontext
	 * @param scale
	 *            prom�nn� d�ky kter� lze p�ev�st metry na pixely
	 */
	private void drawHitspot(Graphics2D g2, double scale) {

		// Pozice pro vykreslen� -> size je pr�m�r!!!;
		double positionDrawX = (this.x - (this.size / 2)) * scale;
		double positionDrawY = (this.y - (this.size / 2)) * scale;

		g2.setColor(this.color);
		g2.fill(new Ellipse2D.Double(positionDrawX, positionDrawY, this.size * scale, this.size * scale));
	}

	/**
	 * Metoda, kter� vykresluje pozici st�elce a c�le (vykreslen� prob�h�
	 * stejn�, m�n� se jen barva, kter� je ulo�ena jako prom�nn� sou�asn�
	 * instance.
	 * 
	 * @param g2
	 *            grafick� kontext
	 * @param scale
	 *            prom�nn� d�ky kter� lze p�ev�st metry na pixely
	 */
	private void drawTargetShooter(Graphics2D g2, double scale) {

		double positionX = this.x * scale;
		double positionY = this.y * scale;
		double offset = this.size / 2;

		g2.setColor(this.color);
		g2.draw(new Line2D.Double(positionX - offset, positionY, positionX + offset, positionY));
		g2.draw(new Line2D.Double(positionX, positionY - offset, positionX, positionY + offset));

		/*
		 * g2.setColor(color); int xs = (int) (x * scale); int ys = (int) (y *
		 * scale);
		 * 
		 * g2.drawLine(xs, ys, xs + 5, ys); g2.drawLine(xs, ys, xs, ys + 5);
		 * g2.drawLine(xs, ys, xs - 5, ys); g2.drawLine(xs, ys, xs, ys - 5);
		 */
	}

}
