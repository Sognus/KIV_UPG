package cz.zcu.viteja.upg;

import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Tøída reprezentující simulovaný vítr
 * 
 * @author Jakub Vítek (A16B0165P)
 * @version 1.00.00
 *
 */
public class Wind extends Observable {

	/** Maximální absolutní hodnota, o kterou se mùže zmìnit smìr vìtru */
	private static final double MAX_AZIMUTH_SHIFT = 0.5;
	/** Maximální absolutní hodnota, o kterou se mùže zmìnit rychlost vìtru */
	private static final double MAX_VELOCITY_SHIFT = 5;

	/** Objekt ze kterého lze získávat pseudonáhodná èísla */
	private Random random;

	/** Smìr vìtru 0 až 2 PI - v radiánech */
	private double azimuth;

	/** Rychlost vìtru */
	private double velocity;

	/** Maximální možná velikost vìtru */
	private double maxVelocity;

	/**
	 * Konstruktor objektu Wind
	 * 
	 * @param maxVelocity
	 *            maximální rychlost vìtru
	 */
	public Wind(double maxVelocity) {
		this.maxVelocity = maxVelocity;

		// Vygenerování poèáteèních dat
		this.random = new Random();
		this.azimuth = 0 + (2.0 - 0) * random.nextDouble();
		this.velocity = 0 + (this.maxVelocity - 0) * random.nextDouble();
	}

	/**
	 * Vrátí x-ovou složku vektoru vìtru
	 * 
	 * @return x-ová složka vektoru vìtru
	 */
	public double slozkaX() {
		return this.velocity * Math.cos(-this.azimuth);
	}

	/**
	 * Vrátí y-ovou složku vektoru vìtru
	 * 
	 * @return y-ová složka vektoru vìtru
	 */
	public double slozkaY() {
		return this.velocity * Math.sin(-this.azimuth);
	}

	/**
	 * Vrátí z-ovou složku vektoru vìtru
	 * 
	 * @return z-ová složka vektoru vìtru
	 */
	public double slozkaZ() {
		return 0.0;
	}

	/**
	 * Vygeneruje nové parametry vìtru
	 * 
	 */
	@Deprecated
	public void generateParams() {
		double velocityDiff = 0 + (Wind.MAX_VELOCITY_SHIFT - (-1 * Wind.MAX_VELOCITY_SHIFT)) * random.nextDouble();
		this.setVelocity(this.velocity + velocityDiff);

		double azimuthDiff = 0 + (Wind.MAX_AZIMUTH_SHIFT - (-1 * Wind.MAX_AZIMUTH_SHIFT)) * random.nextDouble();
		this.setAzimuth(this.azimuth + azimuthDiff);

		this.setChanged();
		this.notifyObservers();

	}

	/**
	 * 
	 */
	public void generateParamsAnimated() {

		double velocityDiff = ThreadLocalRandom.current().nextDouble(-1 * Wind.MAX_VELOCITY_SHIFT,
				Wind.MAX_VELOCITY_SHIFT);
		this.setVelocity(this.velocity + velocityDiff);

		double azimuthDiff = ThreadLocalRandom.current().nextDouble(-1 * Wind.MAX_AZIMUTH_SHIFT,
				Wind.MAX_AZIMUTH_SHIFT);
		double newAzimunth = this.azimuth + azimuthDiff;

		boolean vetsi = newAzimunth > this.getAzimuth();
		Wind current = this;

		// Nastavení
		int msToWait = 10;
		double step = 0.01;

		new Thread(new Runnable() {
			@Override
			public void run() {
				double diff;

				try {
					if (vetsi) {
						diff = newAzimunth - current.getAzimuth();

						long startTime = System.nanoTime();
						while (diff > 0) {
							current.setAzimuth(current.getAzimuth() + step);
							diff = newAzimunth - current.getAzimuth();
							// System.out.println("DiffVetsi: " + diff);
							current.setChanged();
							current.notifyObservers();
							Thread.sleep(msToWait);

							long cTime = System.nanoTime();
							if ((cTime - startTime) >= 125000000)
								break;
						}
					} else {
						diff = current.getAzimuth() - newAzimunth;

						long startTime = System.nanoTime();
						while (diff >= 0) {
							current.setAzimuth(current.getAzimuth() - step);
							diff = current.getAzimuth() - newAzimunth;
							// System.out.println("DiffMensi: " + diff);
							current.setChanged();
							current.notifyObservers();
							Thread.sleep(msToWait);

							long cTime = System.nanoTime();
							if ((cTime - startTime) >= 125000000)
								break;
						}
					}

					current.setAzimuth(newAzimunth);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	/**
	 * Getter pro získání smìru vìtru (radiány)
	 * 
	 * @return smìr vìtru v radiánech
	 */
	public double getAzimuth() {
		return azimuth;
	}

	/**
	 * Setter pro nastavení smìru vìtru (radiány)
	 * 
	 * @param azimuth
	 *            nový smìr vìtru
	 */
	public void setAzimuth(double azimuth) {
		double azimuthFix = azimuth;

		if (azimuthFix >= 2.0)
			azimuthFix = azimuthFix % 2.0;

		this.azimuth = azimuthFix;

	}

	/**
	 * Getter pro získání rychlosti vìtru
	 * 
	 * @return rychlost vìtru
	 */
	public double getVelocity() {
		return velocity;
	}

	/**
	 * Setter pro nastavení rychlosti vìtru
	 * 
	 * @param velocity
	 *            nová rychlost vìtru
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
	 * Getter pro získání maximální velikosti vìtru
	 * 
	 * @return maximální velikost vìtru
	 */
	public double getMaxVelocity() {
		return maxVelocity;
	}

	/**
	 * Setter pro nastavení maximální velikosti vìtru
	 * 
	 * @param maxVelocity
	 *            nová maximální hodnota rychlosti vìtru
	 */
	public void setMaxVelocity(double maxVelocity) {
		this.maxVelocity = maxVelocity;
	}

}
