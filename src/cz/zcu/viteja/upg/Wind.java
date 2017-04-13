package cz.zcu.viteja.upg;

import java.util.Random;

/**
 * T��da reprezentuj�c� simulovan� v�tr
 * 
 * @author Jakub V�tek (A16B0165P)
 * @version 1.00.00
 *
 */
public class Wind {

	/** Maxim�ln� absolutn� hodnota, o kterou se m��e zm�nit sm�r v�tru */
	private static final double MAX_AZIMUTH_SHIFT = 5;
	/** Maxim�ln� absolutn� hodnota, o kterou se m��e zm�nit rychlost v�tru */
	private static final double MAX_VELOCITY_SHIFT = 5;

	/** Objekt ze kter�ho lze z�sk�vat pseudon�hodn� ��sla */
	private Random random;

	/** Sm�r v�tru 0 a� 2 PI - v radi�nech */
	private double azimuth;

	/** Rychlost v�tru */
	private double velocity;

	/** Maxim�ln� mo�n� velikost v�tru */
	private double maxVelocity;

	/**
	 * Konstruktor objektu Wind
	 * 
	 * @param maxVelocity
	 *            maxim�ln� rychlost v�tru
	 */
	public Wind(double maxVelocity) {
		this.maxVelocity = maxVelocity;

		// Vygenerov�n� po��te�n�ch dat
		this.random = new Random();
		this.azimuth = 0 + (2.0 - 0) * random.nextDouble();
		this.velocity = 0 + (this.maxVelocity - 0) * random.nextDouble();
	}

	/**
	 * Vygeneruje nov� parametry v�tru
	 * 
	 */
	public void generateParams() {
		double velocityDiff = 0 + (Wind.MAX_VELOCITY_SHIFT - (-1 * Wind.MAX_VELOCITY_SHIFT)) * random.nextDouble();
		this.setVelocity(this.velocity + velocityDiff);

		double azimuthDiff = 0 + (Wind.MAX_AZIMUTH_SHIFT - (-1 * Wind.MAX_AZIMUTH_SHIFT)) * random.nextDouble();
		this.setAzimuth(this.azimuth + azimuthDiff);
	}

	/**
	 * Getter pro z�sk�n� sm�ru v�tru (radi�ny)
	 * 
	 * @return sm�r v�tru v radi�nech
	 */
	public double getAzimuth() {
		return azimuth;
	}

	/**
	 * Setter pro nastaven� sm�ru v�tru (radi�ny)
	 * 
	 * @param azimuth
	 *            nov� sm�r v�tru
	 */
	public void setAzimuth(double azimuth) {
		double azimuthFix = azimuth;

		if (azimuthFix >= 2.0)
			azimuthFix = azimuthFix % 2.0;

		this.azimuth = azimuthFix;
	}

	/**
	 * Getter pro z�sk�n� rychlosti v�tru
	 * 
	 * @return rychlost v�tru
	 */
	public double getVelocity() {
		return velocity;
	}

	/**
	 * Setter pro nastaven� rychlosti v�tru
	 * 
	 * @param velocity
	 *            nov� rychlost v�tru
	 */
	public void setVelocity(double velocity) {
		double velocityFix = velocity;

		if (velocityFix > maxVelocity)
			velocityFix = maxVelocity;

		if (velocityFix < 0)
			velocityFix = 0;

		this.velocity = velocityFix;
	}

	/**
	 * Getter pro z�sk�n� maxim�ln� velikosti v�tru
	 * 
	 * @return maxim�ln� velikost v�tru
	 */
	public double getMaxVelocity() {
		return maxVelocity;
	}

	/**
	 * Setter pro nastaven� maxim�ln� velikosti v�tru
	 * 
	 * @param maxVelocity
	 *            nov� maxim�ln� hodnota rychlosti v�tru
	 */
	public void setMaxVelocity(double maxVelocity) {
		this.maxVelocity = maxVelocity;
	}

}
