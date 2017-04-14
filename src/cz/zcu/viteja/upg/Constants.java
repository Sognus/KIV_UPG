package cz.zcu.viteja.upg;

import java.awt.Color;

/**
 * T��da obsahuj�c� v�echny d�le�it� konstanty vyu�it� v r�mci tohoto projektu
 * 
 * @author Jakub V�tek - A16B0165P
 * @version 1.00.00
 *
 */
public final class Constants {

	/** P�ep�n� program do debugovac�ho m�du */
	public static boolean DEBUG = false;

	/** pro p�evod mm na m */
	public static final double mmToM = 1000.0;
	/** pro p�evod m na mm */
	public static final double mToMM = 0.001;

	/** �et�zec reprezentuj�c� identifik�tor st�elce v r�mci aplikace */
	public static final String SHOOTER = "shooter";
	/** �et�zec reprezentuj�c� identifik�tor c�le v r�mci aplikace */
	public static final String TARGET = "target";
	/** �et�zec reprezentuj�c� identifik�tor oblasti z�sahu v r�mci aplikace */
	public static final String HITSPOT = "hitSpot";

	/** Barva vykreslov�n� c�le v r�mci aplikace */
	public static final Color targetColor = Color.BLUE;
	/** Barva vykreslov�n� st�elce v r�mci aplikace */
	public static final Color shooterColor = Color.RED;
	/** Barva vykreslov�n� oblasti z�sahu v r�mci aplikace */
	public static final Color hitspotColor = Color.ORANGE;

	public static final String shooterImagePath = "images/shooterRed.png";
	public static final String targetImagePath = "images/targetBlue.png";

	public static final int preferedWindowWidth = 400;
	public static final int preferedWindowHeight = 300;

}
