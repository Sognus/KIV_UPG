package cz.zcu.kiv.upg;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Pomùcka pro semestrální práci z UPG ke generování jednoduchého souboru s
 * terénem.
 * <p>
 * Tøída TerrainGenerator je pouze obálkou nìkolika statických metod. Jelikož
 * jde o triviální aplikaci, nemá smysl vytváøet cokoliv složitìjšího.
 * <p>
 * Program naète parametry pøíkazové øádky, zkontroluje jejich správnost a
 * vygeneruje binární soubor s terénem. Malé terény souèasnì pro kontrolu vypíše
 * na obrazovku.
 * <p>
 * Souèasnì slouží jako pøíklad
 * <ul>
 * <li>jak má vypadat archiv s odevzdanou semestrálkou,</li>
 * <li>jak vytvoøit rùzné dávky (pro kompilaci, spuštìní apod.)</li>
 * <li>jak vypadá okomentovaný zdrojový kód,</li>
 * <li>jak øešit zpracování pøíkazové øádky,</li>
 * <li>jak manipulovat s binárním souborem.</li>
 * </ul>
 * 
 * @author KIV/UPG
 * @version 20170210
 * @since 2016
 */
public class TerrainGenerator {

	/**
	 * Program oèekává NUMBER_OF_PARAMS parametrù na pøíkazové øádce.
	 */
	public static final int NUMBER_OF_PARAMS = 11;

	/**
	 * 2D pole nadmoøských výšek; indexování: terrain[Y][X]
	 */
	public static int[][] terrain = null;

	/** delta x */
	static int deltaX;
	/** delta y */
	static int deltaY;

	/** x-ová pozice støelce */
	static int shooterX;
	/** y-ová pozice støelce */
	static int shooterY;

	/** x-ová pozice cíle */
	static int targetX;
	/** y-ová pozice cíle */
	static int targetY;

	/** jméno souboru */
	static String fName;

	/** pro pøevod mm na m */
	static final double mmToM = 1000.0;

	/**
	 * Generuje plochý terén
	 * 
	 * @param columns
	 *            poèet sloupcù (šíøka mapy)
	 * @param rows
	 *            poèet øádkù (výška mapy)
	 * @param value
	 *            (konstantní) nadmoøská výška v mm
	 * @return 2D pole nadmoøských výšek
	 */
	public static int[][] generateFlatTerrain(int columns, int rows, int value) {
		int[][] terrain = new int[rows][columns];
		int x, y;

		for (y = 0; y < rows; ++y) {
			for (x = 0; x < columns; ++x) {
				terrain[y][x] = value;
			}
		}

		return terrain;
	}

	/**
	 * Generuje terén typu "rovina naklonìná ve smìru osy x"
	 * 
	 * @param columns
	 *            poèet sloupcù (šíøka mapy)
	 * @param rows
	 *            poèet øádkù (výška mapy)
	 * @param inclination
	 *            sklon roviny (pøírùstek mezi dvìma sloupci)<br>
	 *            sklon 0 = zcela plochý terén<br>
	 *            sklon 1 = mezi dvìma sloupci je pøírùstek 1 mm<br>
	 * @return 2D pole nadmoøských výšek
	 */
	public static int[][] generateSweepTerrain(int columns, int rows, double inclination) {
		int[][] terrain = new int[rows][columns];
		int x, y;

		for (y = 0; y < rows; y++) {
			for (x = 0; x < columns; x++) {
				terrain[y][x] = (int) (x * inclination);
			}
		}

		return terrain;
	}

	/**
	 * Uloží terén do binárního souboru. Specifikace souboru viz zadání
	 * semestrální práce nebo zdrojový kód metody.
	 */
	public static void saveTerrain() {
		int rows = terrain.length;
		int columns = terrain[0].length;

		DataOutputStream fout = null;
		try {
			// Samotný zápis dat
			fout = new DataOutputStream(new FileOutputStream(fName));

			fout.writeInt(columns);
			fout.writeInt(rows);
			fout.writeInt(deltaX);
			fout.writeInt(deltaY);
			fout.writeInt(shooterX);
			fout.writeInt(shooterY);
			fout.writeInt(targetX);
			fout.writeInt(targetY);

			for (int y = 0; y < rows; ++y) {
				for (int x = 0; x < columns; ++x) {
					fout.writeInt(terrain[y][x]);
				}
			}
		}
		/*
		 * Následuje pouze zavøení souboru a ošetrení výjimek
		 */
		catch (FileNotFoundException e) {
			System.err.println("Nepovedlo se otevrit vystupni soubor.");
		} catch (IOException e) {
			System.err.println("Nepovedlo se zapsat vystupni soubor.");
		} finally {
			try {
				if (fout != null) {
					fout.close();
				}
			} catch (IOException e) {
				System.err.println("Nepovedlo se uzavrit vystupni soubor.");
			}
		}
	}

	/**
	 * Vytiskne metadata terénu, støelce a cíle
	 */
	public static void printTerrainData() {
		int rowsCount = terrain.length;
		int columnsCount = terrain[0].length;

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
	}

	/**
	 * Vytiskne vygenerovaný terén
	 */
	public static void printTerrainAltitudes() {
		int rowsCount = terrain.length;
		int columnsCount = terrain[0].length;
		final int tableColWidth = 11;

		if (rowsCount * columnsCount == 0) {
			System.out.println("TEREN NEOBSAHUJE ZADNA VYSKOVA DATA !");
		} else {
			System.out.println("Vysky jednotlivych bodu terenu v m:");
			System.out.println();

			System.out.print(String.format("           |%" + ((tableColWidth * columnsCount) / 2 + 2) + "s", "x [m]"));
			System.out.println();

			System.out.print("     y [m] |");
			for (int column = 0; column < columnsCount; column++) {
				System.out.print(String.format("%11.3f", column * deltaX / mmToM));
			}
			System.out.println();

			System.out.print("-----------+");
			for (int column = 0; column < columnsCount; column++) {
				System.out.print("-----------");
			}
			System.out.println();

			for (int row = 0; row < rowsCount; row++) {
				System.out.print(String.format("%10.3f |", row * deltaY / mmToM));
				for (int column = 0; column < columnsCount; column++) {
					System.out.print(String.format("%11.3f", terrain[row][column] / mmToM));
				}
				System.out.println();
			}
		}
	}

	/**
	 * Naètení parametrù z pøíkazové øádky, jejich kontrola a spuštení èinnosti.
	 * Jelikož je veškerá èinnost jednoduchá, nemá smysl metodu dále
	 * dekomponovat. Pokud by ale program umožòoval více vìcí, bylo by vhodné
	 * oddìlit zpracování parametrù a vykonání èinnosti.
	 * 
	 * @param args
	 *            parametry pøíkazové øádky
	 */
	public static void main(String args[]) {
		if (args.length < NUMBER_OF_PARAMS) {
			System.out.println("Generator zkusebniho terenu.");
			System.out.println(
					"Pouziti: java terrainGenerator/TerrainGenerator <typ> <f> <sirka> <vyska> <DeltaX> <DeltaY> <sX> <sY> <tX> <tY> <nazev>");
			System.out.println("<typ>     - typ terenu; r = rovny, s = sikmy");
			System.out.println("<f>       - vyska rovneho terenu [mm] / sklon sikmeho terenu [mm/sloupec]");
			System.out.println("<sirka>   - pocet sloupcu terenu");
			System.out.println("<vyska>   - pocet radek terenu");
			System.out.println("<DeltaX>  - rozestup sloupcu [mm]");
			System.out.println("<DeltaY>  - rozestup radek [mm]");
			System.out.println("<sX> <sY> - sloupec a radek, kde je umisten strelec (shooter)");
			System.out.println("<tX> <tY> - sloupec a radek, kde je umisten cil (target)");
			System.out.println("<nazev>   - nazev vystupniho souboru");
			System.out.println();
			System.out.println("Pro velmi male tereny (max. 5 radku a 5 sloupcu) se teren navic vypise na obrazovku.");
			System.out.println(
					"Priklad: java terrainGenerator/TerrainGenerator r 1   200 100   1000 1000   5 5   100 50   rovny.ter");
			System.exit(0);
		}

		int argNo = 0;
		try {
			// Ètení parametrù pøíkazové øádky
			String terrainType = args[argNo++];

			// TODO:
			// Hodnoty parametru <typ> (teï se øetezce "r" a "s" vyskytují v
			// nápovìdì výše
			// a v testech níže. Pøi rozšiøování programu to vyøešit jinak!
			if (!(terrainType.equals("r") || terrainType.equals("s"))) {
				throw new IllegalArgumentException();
			}

			double value = Double.parseDouble(args[argNo++]);
			int columns = Integer.parseInt(args[argNo++]);
			int rows = Integer.parseInt(args[argNo++]);
			deltaX = Integer.parseInt(args[argNo++]);
			deltaY = Integer.parseInt(args[argNo++]);
			shooterX = Integer.parseInt(args[argNo++]);
			shooterY = Integer.parseInt(args[argNo++]);
			targetX = Integer.parseInt(args[argNo++]);
			targetY = Integer.parseInt(args[argNo++]);
			fName = args[argNo++];

			// Vygenerování a uložení terénu
			if (terrainType.equals("r")) {
				terrain = generateFlatTerrain(columns, rows, (int) value);
			} else if (terrainType.equals("s")) {
				terrain = generateSweepTerrain(columns, rows, value);
			}

			saveTerrain();
			printTerrainData();
			if (columns <= 5 && rows <= 5) {
				System.out.println();
				printTerrainAltitudes();
			}
		} catch (IllegalArgumentException e) {
			/*
			 * výjimka IllegalArgumentException rovnež pokrývá
			 * NumberFormatException, kterou mùže generovat parseInt a
			 * parseDouble. Jediný parametr nepokrytý výjimkou je fName; na
			 * nekorektní název souboru tak zareaguje až metoda saveTerrain()
			 */
			System.err.println("Chyba v zadani parametru " + argNo);
		}
	}
}
