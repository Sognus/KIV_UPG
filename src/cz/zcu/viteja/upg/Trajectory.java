package cz.zcu.viteja.upg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.LinkedList;

/**
 * T��da reprezentuj�c� implementaci trajektorie st�ely
 * 
 * @author Jakub V�tek A16B0165P
 * @version 1.00.00
 *
 */
public class Trajectory {

	/** X-ov� slo�ka sou�adnice */
	private double x;
	/** Y-ov� slo�ka sou�adnice */
	private double y;
	/** Z-ov� slo�ka sou�adnice */
	private double z;

	/** Seznam slo�ek sou�adnic ve tvaru: x, y, z, x, y, z, x, .. */
	private LinkedList<Double> trajectory;

	/**
	 * Konstruktor t��dy {@link Trajectory} vytvo�� nov� LinkedList, do kter�ho
	 * je mo�n� ukl�dat jednotliv� slo�ky bodu
	 * 
	 * Plat�, �e jeden bod m� t�i slo�ky
	 * 
	 * @param x
	 *            X-ov� slo�ka po��te�n� sou�adnice
	 * @param y
	 *            Y-ov� slo�ka po��te�n� sou�adnice
	 * @param z
	 *            Z-ov� slo�ka po��te�n� sou�adnice
	 */
	public Trajectory(double x, double y, double z) {

		this.trajectory = new LinkedList<Double>();
		this.add(x, y, z);
	}

	/**
	 * Vykresl� trajektorii st�ely
	 * 
	 * @param g2
	 *            Grafick� kontext
	 * @param scale
	 *            �k�lov�n� dle velikosti okna
	 */
	public void draw(Graphics2D g2, double scale) {

		GeneralPath path = new GeneralPath();

		path.moveTo(this.getX(0) * scale, this.getY(0) * scale);

		for (int i = 0; i < this.size(); i++) {
			double x = this.getX(i) * scale;
			double y = this.getY(i) * scale;
			double z = this.getZ(i) * scale;

			path.lineTo(x, y);

		}

		g2.setColor(Color.YELLOW);
		g2.draw(path);

	}

	/**
	 * Ulo�� nov� bod
	 * 
	 * @param x
	 *            X-ov� slo�ka bodu
	 * @param y
	 *            Y-ov� slo�ka bodu
	 * @param z
	 *            Z-ov� slo�ka bodu
	 */
	public void add(double x, double y, double z) {
		this.trajectory.add(x);
		this.trajectory.add(y);
		this.trajectory.add(z);
	}

	/**
	 * Vr�t� po�et ulo�en�ch bod�, pokud n�jak� slo�ky chyb�, nebudou po��t�ny
	 * jako cel� bod
	 * 
	 * @return po�et cel�ch bod� (1 bod = 3 slo�ky = 3 indexy v Listu)
	 */
	public int size() {

		return (int) Math.floor(this.trajectory.size() / 3);

	}

	/**
	 * Vr�t� x-ovou slo�ku dan�ho bodu.
	 * 
	 * Pro index 0 vr�t� x-ovou slo�ku 1. bodu,
	 * 
	 * Pro index 1 vr�t� x-ovou slo�ku 2. bodu,
	 * 
	 * a tak d�le
	 * 
	 * @param index
	 *            po�ad� bodu v listu
	 * @return x-ov� slo�ka (index + 1)-ho bodu
	 */
	public double getX(int index) {
		int position = 0 + index * 3;
		return this.trajectory.get(position);
	}

	/**
	 * Vr�t� y-ovou slo�ku dan�ho bodu.
	 * 
	 * Pro index 0 vr�t� y-ovou slo�ku 1. bodu,
	 * 
	 * Pro index 1 vr�t� y-ovou slo�ku 2. bodu,
	 * 
	 * a tak d�le
	 * 
	 * @param index
	 *            po�ad� bodu v listu
	 * @return y-ov� slo�ka (index + 1)-ho bodu
	 */
	public double getY(int index) {
		int position = 1 + index * 3;
		return this.trajectory.get(position);
	}

	/**
	 * Vr�t� z-ovou slo�ku dan�ho bodu.
	 * 
	 * Pro index 0 vr�t� z-ovou slo�ku 1. bodu,
	 * 
	 * Pro index 1 vr�t� z-ovou slo�ku 2. bodu,
	 * 
	 * a tak d�le
	 * 
	 * @param index
	 *            po�ad� bodu v listu
	 * @return z-ov� slo�ka (index + 1)-ho bodu
	 */
	public double getZ(int index) {
		int position = 1 + index * 3;
		return this.trajectory.get(position);
	}

}
