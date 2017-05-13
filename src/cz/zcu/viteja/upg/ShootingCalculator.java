package cz.zcu.viteja.upg;

import java.awt.Color;

/**
 * T��da, kter� na z�klad� referenc� na st�elce a c�l a hodnot azimutu a
 * vzd�lenosti je schopn� vypo��tat sou�adnice z�sahu a vytvo�it z nich
 * NamedPosition objekt oblasti z�sahu
 * 
 * @author Jakub V�tek - A16B0165P
 * @version 1.00.00
 */
public class ShootingCalculator {

	/** gx */
	public double gx;
	/** gy */
	public double gy;
	/** gz */
	public double gz;
	/** b */
	public double b;
	/** deltaT */
	public double deltaT;
	/** Reference na instanci st�elce */
	public NamedPosition shooter;
	/** Reference na instanci c�le */
	public NamedPosition target;
	/** Reference na trajektorii */
	public Trajectory trajectory;

	/**
	 * Reference na instanci oblasti z�sahu. Nejprve je null, vytvo�� se po
	 * zavol�n� metody shoot
	 */
	private NamedPosition hitSpot;

	/**
	 * Konstruktor t��dy {@link ShootingCalculator}, kter� ulo�� sv� argumenty
	 * jako koresponduj�c� prom�nn� instance
	 * 
	 * @param shooter
	 *            reference na st�elce
	 * @param target
	 *            reference na c�l
	 */
	public ShootingCalculator(NamedPosition shooter, NamedPosition target) {
		this.shooter = shooter;
		this.target = target;
		this.hitSpot = null;
		this.trajectory = null;

		this.gx = 0.0;
		this.gy = 0.0;
		this.gz = 10;
		this.b = 0.05;
		this.deltaT = 0.01;
	}

	/**
	 * St�elec vyst�el�
	 * 
	 * @param azimuth
	 *            sm�r st�elby
	 * @param distance
	 *            vzd�lenost st�elby
	 */
	@Deprecated
	public void shoot(double azimuth, double distance) {
		// St�elec i target jsou zad�ny v metrech
		// Posun v metrech

		// P�evod stupn� 0 a� -180 na 180 - -uhel
		double radians = azimuth * (Math.PI / 180);

		double posunX = Math.cos(radians) * distance;
		double posunY = Math.sin(radians) * distance;

		// System.out.println(
		// String.format("sX + posuX = %.5f + %.5f = %.5f)", this.shooter.x,
		// posunX, this.shooter.x + posunX));

		this.hitSpot = new NamedPosition(this.shooter.x + posunX, this.shooter.y - posunY, Constants.HITSPOT,
				Constants.hitspotColor, 60);

	}

	/**
	 * St�elec vyst�el�
	 * 
	 * @param azimuth
	 *            sm�r
	 * @param elevation
	 *            v��ka
	 * @param startVelocity
	 *            po��te�n� rychlost
	 */
	public void shoot(double azimuth, double elevation, double startVelocity) {
		double shooterVyska = Game.terrain.getAltitudeInM(shooter.x, shooter.y);
		this.trajectory = new Trajectory(shooter.x, shooter.y, shooterVyska);

		// I have no idea
		double firstPosX = shooter.x;
		double firstPosY = shooter.y;
		double firstPosZ = shooterVyska;

		// Bullshit matika
		double firstShootX = startVelocity * Math.cos(Math.toRadians(elevation)) * Math.cos(Math.toRadians(-azimuth));
		double firstShootY = startVelocity * Math.cos(Math.toRadians(elevation)) * Math.sin(Math.toRadians(-azimuth));
		double firstShootZ = startVelocity * Math.sin(Math.toRadians(elevation));

		double lastShootVelocityX = firstShootX;
		double lastShootVelocityY = firstShootY;
		double lastShootVelocityZ = firstShootZ;

		double lastPositionX = firstPosX;
		double lastPositionY = firstPosY;
		double lastPositionZ = firstPosZ;

		double preLastPositionX = firstPosX;
		double preLastPositionY = firstPosY;
		double preLastPositionZ = firstPosZ;

		// A� se d�je co se d�je, po�k�me na Metod�je
		while (Game.terrain.getAltitudeInM(lastPositionX, lastPositionY) <= (lastPositionZ)) {
			// Umim matiku jako motyku
			double newShootX = lastShootVelocityX + 0.0 * this.gx * deltaT
					+ (Game.wind.slozkaX() - lastShootVelocityX) * b * deltaT;
			double newShootY = lastShootVelocityY + 0.0 * this.gy * deltaT
					+ (Game.wind.slozkaY() - lastShootVelocityY) * b * deltaT;
			double newShootZ = lastShootVelocityZ - 1.0 * this.gz * deltaT
					+ (Game.wind.slozkaZ() - lastShootVelocityZ) * b * deltaT;

			double newPosX = lastPositionX + newShootX * deltaT;
			double newPosY = lastPositionY + newShootY * deltaT;
			double newPosZ = lastPositionZ + newShootZ * deltaT;

			preLastPositionX = lastPositionX;
			preLastPositionY = lastPositionY;
			preLastPositionZ = lastPositionZ;

			trajectory.add(newPosX, newPosY, newPosZ);

			lastShootVelocityX = newShootX;
			lastShootVelocityY = newShootY;
			lastShootVelocityZ = newShootZ;

			lastPositionX = newPosX;
			lastPositionY = newPosY;
			lastPositionZ = newPosZ;

		}

		double horniX = preLastPositionX;
		double horniY = preLastPositionY;
		double horniZ = preLastPositionZ;

		double dolniX = lastPositionX;
		double dolniY = lastPositionY;
		double dolniZ = lastPositionZ;

		double bX = (horniX + dolniX) / 2.0;
		double bY = (horniY + dolniY) / 2.0;
		double bZ = (horniZ + dolniZ) / 2.0;

		double vyska = Game.terrain.getAltitudeInM(bX, bY);
		int zastaveni = 0;

		// No no no. We should go to the armory. That's where Zadorojny is
		// likely to be.
		while (vyska != bZ) {
			if (Math.abs((vyska - bZ)) < 0.001)
				break;
			if (zastaveni == 200)
				break;
			if (vyska > bZ) {
				dolniX = bX;
				dolniY = bY;
				dolniZ = bZ;

				bX = (horniX + bX) / 2.0;
				bY = (horniY + bY) / 2.0;
				bZ = (horniZ + bZ) / 2.0;
			} else {
				horniX = bX;
				horniY = bY;
				horniZ = bZ;

				bX = (bX + dolniX) / 2.0;
				bY = (bY + dolniY) / 2.0;
				bZ = (bZ + dolniZ) / 2.0;

			}
			vyska = Game.terrain.getAltitudeInM(bX, bY);
			zastaveni++;
		}

		// Pokud tohle n�kdo �te, obdivuju, �e V�m to nevyp�lilo o�i a nezni�ilo
		// zdravej rozum, m� toti� jo.
		this.hitSpot = new NamedPosition(lastPositionX, lastPositionY, Constants.HITSPOT, Color.ORANGE, 10);

		Game.trajectory = trajectory;
		Game.gamePanel.trajectory = trajectory;

	}

	public double getGravityConstant() {
		return gz;
	}

	/**
	 * Metoda, kter� vrac� pravdivnost� hodnotu o tom, zda byl treferen c�l.
	 * Pokud jsme je�t� nevyst�elili, nem�me ��dnou instanci oblasti z�sahu,
	 * tud� jsme nemohli trefit (nevyst�elili jsme). Pokud byla st�elba
	 * provedena, ov��� se zda je c�l um�st�n uvnit� oblasti z�sahu.
	 * 
	 * @return byl zasa�en c�l?
	 */
	public boolean testTargetHit() {
		if (this.hitSpot.equals(null)) {
			return false;
		}

		return (this.hitSpot.getDistance(target) <= 30);

	}

	/**
	 * Getter, vr�t� aktu�ln� referenci na objekt oblasti z�sahu
	 * 
	 * @return oblast z�sahu
	 */
	public NamedPosition getHitSpot() {
		return this.hitSpot;
	}

}
