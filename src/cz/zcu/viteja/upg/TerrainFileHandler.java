package cz.zcu.viteja.upg;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * T��da jej� instance se star� o na��t�n� dat ze souboru, ve kter�m je ulo�en
 * hern� ter�n a jejich zprost�edkov�n� ostatn�m ��stem aplikace.
 * 
 * @author Jakub V�tek - A16B0165P
 * @version 1.01.00
 */
public class TerrainFileHandler {

	// Konstanty
	/** pro p�evod mm na m */
	static final double mmToM = 1000.0;

	// Instan�n� prom�nn�
	/**
	 * V�cerozm�rn� pole reprezentuj�c� nadmo�sk� v��ky v sou�adnicov�m syst�mu
	 */
	public int[][] terrain;
	/** Po�et sloupc� ter�nu */
	public int columns;
	/** Po�et ��dk� v ter�nu */
	public int rows;
	/** Rozestup mezi sloupci */
	public int deltaX;
	/** Rozestup mezi ��dky */
	public int deltaY;
	/** Sou�adnice st�elce na ose X */
	public int shooterX;
	/** Sou�adnice st�elce na ose Y */
	public int shooterY;
	/** Sou�adnice c�le na ose X */
	public int targetX;
	/** Sou�adnice c�le na ose Y */
	public int targetY;

	/**
	 * Implicitn� konstruktor (jen aby se ne�eklo)
	 */
	public TerrainFileHandler() {

	}

	/**
	 * Metoda, kter� p�e�te zadan� soubor hern�ho ter�nu a ulo�� p�e�ten� data
	 * jako sv� prom�nn�
	 * 
	 * @param fileName
	 *            n�zev/cesta k souboru
	 */
	public void loadTerFile(String fileName) {
		try {

			File file = new File(fileName);
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			DataInputStream dis = new DataInputStream(bis);

			this.columns = dis.readInt();
			this.rows = dis.readInt();
			this.deltaX = dis.readInt();
			this.deltaY = dis.readInt();
			this.shooterX = dis.readInt();
			this.shooterY = dis.readInt();
			this.targetX = dis.readInt();
			this.targetY = dis.readInt();

			this.terrain = new int[rows][columns];
			int x, y;

			for (y = 0; y < this.rows; ++y) {
				for (x = 0; x < this.columns; ++x) {
					terrain[y][x] = dis.readInt();
				}
			}

			dis.close();
			bis.close();
			fis.close();

		} catch (IOException e) {
			// Soubor nenalezen
			e.printStackTrace();
		}
	}

	/**
	 * Vyp�e nadmo�sk� v��ky aktu�ln�ho ter�nu
	 */
	public void printData() {
		int rowsCount = terrain.length;
		int columnsCount = terrain[0].length;

		System.out.println();
		System.out.println("-----Vypisuji informace o souboru s ter�nem-----");

		System.out.println(String.format("Pocet sloupcu: %d, pocet radku: %d", columnsCount, rowsCount));
		System.out.println(
				String.format("Rozestup mezi sloupci %.3f m, mezi radky %.3f m", deltaX / mmToM, deltaY / mmToM));
		System.out.println(String.format("Rozmery oblasti: sirka %.3f m, vyska %.3f m", columnsCount * deltaX / mmToM,
				rowsCount * deltaY / mmToM));
		System.out.println(String.format("Poloha strelce: sloupec %d, radek %d, tj. x = %.3f m, y = %.3f m", shooterX,
				shooterY, shooterX * deltaX / mmToM, shooterY * deltaY / mmToM));

		if (shooterX < 0 || shooterX >= columnsCount || shooterY < 0 || shooterY >= rowsCount) {
			System.out.println("STRELEC JE MIMO MAPU !");
		} else {
			System.out.println(String.format("   nadmorska vyska strelce %.3f m", terrain[shooterY][shooterX] / mmToM));
		}

		System.out.println(String.format("Poloha cile: sloupec %d, radek %d, tj. x = %.3f m, y = %.3f m", targetX,
				targetY, targetX * deltaX / mmToM, targetY * deltaY / mmToM));

		if (targetX < 0 || targetX >= columnsCount || targetY < 0 || targetY >= rowsCount) {
			System.out.println("CIL JE MIMO MAPU !");
		} else {
			System.out.println(String.format("   nadmorska vyska cile %.3f m", terrain[targetY][targetX] / mmToM));
		}

		System.out.println("-----Kon��m s v�pisem ter�n souboru-----");
		System.out.println();

	}

}
