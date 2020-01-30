package cz.zcu.viteja.upg;

import java.awt.Color;

/**
 * Tøída obsahující všechny dùležité konstanty využité v rámci tohoto projektu
 * 
 * @author Jakub Vítek - A16B0165P
 * @version 1.00.00
 *
 */
public final class Constants {

	/** Pøepíná program do debugovacího módu */
	public static boolean DEBUG = false;

	/** pro pøevod mm na m */
	public static final double mmToM = 1000.0;
	/** pro pøevod m na mm */
	public static final double mToMM = 0.001;

	/** Øetìzec reprezentující identifikátor støelce v rámci aplikace */
	public static final String SHOOTER = "shooter";
	/** Øetìzec reprezentující identifikátor cíle v rámci aplikace */
	public static final String TARGET = "target";
	/** Øetìzec reprezentující identifikátor oblasti zásahu v rámci aplikace */
	public static final String HITSPOT = "hitSpot";

	/** Barva vykreslování cíle v rámci aplikace */
	public static final Color targetColor = Color.BLUE;
	/** Barva vykreslování støelce v rámci aplikace */
	public static final Color shooterColor = Color.RED;
	/** Barva vykreslování oblasti zásahu v rámci aplikace */
	public static final Color hitspotColor = Color.ORANGE;

	/** Cesta k obrázku støelce */
	public static final String shooterImagePath = "images/shooterRed.png";
	/** Cesta k obrázku cíle */
	public static final String targetImagePath = "images/targetBlue.png";
	/** Cesta k pozadí støedu kompasu */
	public static final String compassMiddleImagePath = "images/compassMiddleBackground.png";

	/** preferovaná šíøka okna */
	public static final int preferedWindowWidth = 400;
	/** Preferovaná výška okna */
	public static final int preferedWindowHeight = 300;

}
