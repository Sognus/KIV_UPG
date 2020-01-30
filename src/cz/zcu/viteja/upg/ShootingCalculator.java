package cz.zcu.viteja.upg;

import java.awt.Color;

/**
 * Tøída, která na základì referencí na støelce a cíl a hodnot azimutu a
 * vzdálenosti je schopná vypoèítat souøadnice zásahu a vytvoøit z nich
 * NamedPosition objekt oblasti zásahu
 * 
 * @author Jakub Vítek - A16B0165P
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
	/** Reference na instanci støelce */
	public NamedPosition shooter;
	/** Reference na instanci cíle */
	public NamedPosition target;
	/** Reference na trajektorii */
	public Trajectory trajectory;

	/**
	 * Reference na instanci oblasti zásahu. Nejprve je null, vytvoøí se po
	 * zavolání metody shoot
	 */
	private NamedPosition hitSpot;

	/**
	 * Konstruktor tøídy {@link ShootingCalculator}, kterı uloí své argumenty
	 * jako korespondující promìnné instance
	 * 
	 * @param shooter
	 *            reference na støelce
	 * @param target
	 *            reference na cíl
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
	 * Støelec vystøelí
	 * 
	 * @param azimuth
	 *            smìr støelby
	 * @param distance
	 *            vzdálenost støelby
	 */
	@Deprecated
	public void shoot(double azimuth, double distance) {
		// Støelec i target jsou zadány v metrech
		// Posun v metrech

		// Pøevod stupnì 0 a -180 na 180 - -uhel
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
	 * Støelec vystøelí
	 * 
	 * @param azimuth
	 *            smìr
	 * @param elevation
	 *            vıška
	 * @param startVelocity
	 *            poèáteèní rychlost
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

		// A se dìje co se dìje, poèkáme na Metodìje
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

		// Pokud tohle nìkdo ète, obdivuju, e Vám to nevypálilo oèi a neznièilo
		// zdravej rozum, mì toti jo.
		this.hitSpot = new NamedPosition(lastPositionX, lastPositionY, Constants.HITSPOT, Color.ORANGE, 10);

		Game.trajectory = trajectory;
		Game.gamePanel.trajectory = trajectory;

	}

	public double getGravityConstant() {
		return gz;
	}

	/**
	 * Metoda, která vrací pravdivností hodnotu o tom, zda byl treferen cíl.
	 * Pokud jsme ještì nevystøelili, nemáme ádnou instanci oblasti zásahu,
	 * tudí jsme nemohli trefit (nevystøelili jsme). Pokud byla støelba
	 * provedena, ovìøí se zda je cíl umístìn uvnitø oblasti zásahu.
	 * 
	 * @return byl zasaen cíl?
	 */
	public boolean testTargetHit() {
		if (this.hitSpot.equals(null)) {
			return false;
		}

		return (this.hitSpot.getDistance(target) <= 30);

	}

	/**
	 * Getter, vrátí aktuální referenci na objekt oblasti zásahu
	 * 
	 * @return oblast zásahu
	 */
	public NamedPosition getHitSpot() {
		return this.hitSpot;
	}

	public double getAltitude(NamedPosition namedPosition, double elevation, double startV, double distance) {
		double y = 0;				
		double v = startV;		
		double g = this.gz;		
		double y0 = Game.terrain.getAltitudeInM(namedPosition.x, namedPosition.y);
		double x = distance;		// distance
		double theta = Math.toRadians(elevation);	
		double xtanth = x*Math.tan(theta);			
		double vcosth = v*Math.cos(theta);		
		y = y0 + xtanth - ( (g*x*x) / (2*vcosth*vcosth) );
		return y;
	}

}
