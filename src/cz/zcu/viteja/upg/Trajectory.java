package cz.zcu.viteja.upg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.LinkedList;

/**
 * Tøída reprezentující implementaci trajektorie støely
 * 
 * @author Jakub Vítek A16B0165P
 * @version 1.00.00
 *
 */
public class Trajectory {

	/** X-ová složka souøadnice */
	private double x;
	/** Y-ová složka souøadnice */
	private double y;
	/** Z-ová složka souøadnice */
	private double z;

	/** Seznam složek souøadnic ve tvaru: x, y, z, x, y, z, x, .. */
	private LinkedList<Double> trajectory;

	/**
	 * Konstruktor tøídy {@link Trajectory} vytvoøí nový LinkedList, do kterého
	 * je možné ukládat jednotlivé složky bodu
	 * 
	 * Platí, že jeden bod má tøi složky
	 * 
	 * @param x
	 *            X-ová složka poèáteèní souøadnice
	 * @param y
	 *            Y-ová složka poèáteèní souøadnice
	 * @param z
	 *            Z-ová složka poèáteèní souøadnice
	 */
	public Trajectory(double x, double y, double z) {

		this.trajectory = new LinkedList<Double>();
		this.add(x, y, z);
	}

	/**
	 * Vykreslí trajektorii støely
	 * 
	 * @param g2
	 *            Grafický kontext
	 * @param scale
	 *            škálování dle velikosti okna
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
	 * Uloží nový bod
	 * 
	 * @param x
	 *            X-ová složka bodu
	 * @param y
	 *            Y-ová složka bodu
	 * @param z
	 *            Z-ová složka bodu
	 */
	public void add(double x, double y, double z) {
		this.trajectory.add(x);
		this.trajectory.add(y);
		this.trajectory.add(z);
	}

	/**
	 * Vrátí poèet uložených bodù, pokud nìjaké složky chybí, nebudou poèítány
	 * jako celý bod
	 * 
	 * @return poèet celých bodù (1 bod = 3 složky = 3 indexy v Listu)
	 */
	public int size() {

		return (int) Math.floor(this.trajectory.size() / 3);

	}

	/**
	 * Vrátí x-ovou složku daného bodu.
	 * 
	 * Pro index 0 vrátí x-ovou složku 1. bodu,
	 * 
	 * Pro index 1 vrátí x-ovou složku 2. bodu,
	 * 
	 * a tak dále
	 * 
	 * @param index
	 *            poøadí bodu v listu
	 * @return x-ová složka (index + 1)-ho bodu
	 */
	public double getX(int index) {
		int position = 0 + index * 3;
		return this.trajectory.get(position);
	}

	/**
	 * Vrátí y-ovou složku daného bodu.
	 * 
	 * Pro index 0 vrátí y-ovou složku 1. bodu,
	 * 
	 * Pro index 1 vrátí y-ovou složku 2. bodu,
	 * 
	 * a tak dále
	 * 
	 * @param index
	 *            poøadí bodu v listu
	 * @return y-ová složka (index + 1)-ho bodu
	 */
	public double getY(int index) {
		int position = 1 + index * 3;
		return this.trajectory.get(position);
	}

	/**
	 * Vrátí z-ovou složku daného bodu.
	 * 
	 * Pro index 0 vrátí z-ovou složku 1. bodu,
	 * 
	 * Pro index 1 vrátí z-ovou složku 2. bodu,
	 * 
	 * a tak dále
	 * 
	 * @param index
	 *            poøadí bodu v listu
	 * @return z-ová složka (index + 1)-ho bodu
	 */
	public double getZ(int index) {
		int position = 1 + index * 3;
		return this.trajectory.get(position);
	}

}
