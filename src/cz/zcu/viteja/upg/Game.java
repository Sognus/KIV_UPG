package cz.zcu.viteja.upg;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;

/**
 * Hlavn� vstupn� t��da aplikace, kter� zaji�tuje z�kladn� �kony, kter�mi jsou
 * nap��klad: na�ten� hern� mapy ze souboru, z�sk�n� a udr�en� v�ech d�le�it�ch
 * instanc� na ��sti aplikace.
 * 
 * @author Jakub V�tek - A16B0165P
 * @version 1.05.00
 *
 */
public class Game {

	// Konstanty
	/** konstanta pro p�evod mm na m */
	static final double mmToM = 1000.0;
	/** konstanta pro p�evod m na mm */
	static final double mToMM = 0.001;

	// T��dn� atributy
	/** Reference na instanci, kter� zaji��uje na��t�n� souboru ter�nu */
	public static TerrainFileHandler terrainFile;
	/** Reference na instanci, kter� reprezentuje na�ten� hern� ter�n */
	public static Terrain terrain;

	/**
	 * Reference na instanci, kter� reprezentuje pozici a dal�� vlastnosti
	 * st�elce
	 */
	public static NamedPosition shooter;
	/**
	 * Reference na instanci, kter� reprezentuje pozici a dal�� vlastnosti c�le
	 */
	public static NamedPosition target;
	/**
	 * Reference na instanci, kter� na z�klad� vstupn�ch dat dok�e vypo��tat
	 * dopad st�ely a ov��it zda st�ela zas�hla �i minula
	 */
	public static ShootingCalculator shootingCalculator;

	/** Reference na instanci okna aplikace */
	public static JFrame frame;
	/** Reference na instanci hern�ho panelu, kter� je vykreslov�n v okn� */
	public static GamePanel gamePanel;

	/**
	 * Reference na vstupn� parametry, kter� je pou�ita, aby bylo mo�n� k
	 * parametr�m z console p�istupovat odkudkoliv ze t��dy ani� by bylo nutn�
	 * p�ed�vat hodnoty jako argument metod
	 */
	private static String[] startArgs;

	/**
	 * Hlavn� metoda aplikace
	 * 
	 * @param args
	 *            vstupn� parametry aplikace
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// Ulo�it vstupn� parametry tak, aby bylo mo�n� p�istupovat k nim v cel�
		// t��d�
		startArgs = args;

		// Kontrola jestli je soubor zad�n jako parametr z konzole
		String fileName = (args.length < 3) ? "rovny1metr_1km_x_1km.ter" : args[2];

		// Zjist� sou�asn� um�st�n� pracovn�ho adres��e
		File relative = new File(Game.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		// Zjist� absolutn� cestu k n�hradn�mu souboru
		String absolute = relative.getParentFile().getAbsolutePath() + "\rovny1metr_1km_x_1km.ter";

		// Ov��� zda soubor existuje
		String filePath = new File(fileName).exists() ? fileName : absolute;

		// Ov��� znovu, zda soubor existuje, pojistka pro p��pad, �e by
		// neexistoval ani n�hradn� soubor s ter�nem, aplikace se pak ukon��
		File f = new File(filePath);
		if (!f.exists()) {
			System.out.println("-----CHYBA APLIKACE----");
			System.out.println("Aplikace n�kolikr�t ov��ila, zda m� k dispozici zadan� soubor s ter�nem.");
			System.out.println("Po�adovan� soubor i v�echny z�lo�n� soubory pravd�podobn� neexistuj�.");
			System.out
					.println("Pokud tuto aplikaci spou�t�te v adres��ov� struktu�e odevzd�van� pro p�edm�t KIV/UPG, ");
			System.out.println("nahrajte soubor rovny1metr_1km_x_1km.ter do ko�ene slo�ky");
			System.out.println(
					"Pokud spou�t�te aplikaci jinak, zadejte v 3. parametru p�i spou�t�n� cestu k platn�mu souboru s ter�nem.");

			System.out.println();
			System.out.println("APLIKACE BUDE UKON�ENA");
			System.out.println("----------");

			System.out.println("Ukon�en� po stisku libovoln� kl�vesy...");
			System.in.read();
			System.exit(-1);

		}

		// Na�ten� souboru
		loadTerrain(filePath);

		// Z�sk�n� v�ech nutn�ch instanc� objekt�, nastaven� nutn�ch prom�nn�ch
		initData();

		// Vytvo�en� okna
		makeWindow();

		// Hlavn� hern� cyklus
		gameMainLoop();
	}

	/**
	 * Z�sk� v�echny nutn� d�le�it� instance, bez kter�ch by aplikace nemohla
	 * pracovat a ulo�� je do koresponduj�c�ch statick�ch atribut�
	 */
	public static void initData() {
		shooter = new NamedPosition(terrainFile.shooterX * terrainFile.deltaX / Constants.mmToM,
				terrainFile.shooterY * terrainFile.deltaY / Constants.mmToM, Constants.SHOOTER, Constants.shooterColor,
				20.0);
		target = new NamedPosition(terrainFile.targetX * terrainFile.deltaX / Constants.mmToM,
				terrainFile.targetY * terrainFile.deltaY / Constants.mmToM, Constants.TARGET, Constants.targetColor,
				10.0);

		terrain = new Terrain(terrainFile.terrain, terrainFile.deltaX, terrainFile.deltaY, terrainFile.rows,
				terrainFile.columns);

		shootingCalculator = new ShootingCalculator(shooter, target);
	}

	/**
	 * Metoda, kter� se star� o funk�nost hlavn�ho cyklu aplikace. Metoda se
	 * pokus� z�skat hodnoty konzole parametr�, pokud je nenajde, zept� se na
	 * v�echny pot�ebn� hodnoty u�ivatele. Hlavn� cyklus b�� tak dlouho, dokud
	 * se u�ivatel nerozhodne ukon�it aplikaci
	 */
	public static void gameMainLoop() {

		System.out.println("-----HRA-----");

		while (true) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				double azimuth;
				double distance;
				if (startArgs.length >= 2 && !startArgs.equals(null)) {
					azimuth = Double.valueOf(startArgs[0]);
					distance = Double.valueOf(startArgs[1]);
				} else {

					System.out.print("Zadejte azimut: ");
					azimuth = Double.valueOf(br.readLine());

					System.out.print("Zadejte vzd�lenost st�elby: ");
					distance = Double.valueOf(br.readLine());
				}

				shootingCalculator.shoot(azimuth, distance);

				gamePanel.setHitSpot(shootingCalculator.getHitSpot());

				System.out.println();
				if (shootingCalculator.testTargetHit()) {
					System.out.println("Z�SAH! C�l byl zni�en!");
				}

				else {
					System.out.println("VEDLE! C�l nebyl zasa�en!");
				}

				frame.setVisible(true);

				startArgs = new String[0];
				frame.repaint();

				// Zeptat se znova na hran�

				System.out.println();
				System.out.print("Hr�t znovu? <ano/ne>: ");
				String hrat = br.readLine();

				if (!hrat.toLowerCase().equals("a") && !hrat.toLowerCase().equals("ano")
						&& !hrat.toLowerCase().equals("true")) {
					System.out.println("-----Hra byla ukon�ena-----");

					frame.setVisible(false);
					frame.dispose();
					break;
				}

				System.out.println("----------");

			} catch (NumberFormatException | IOException e) {
				System.out.println("Nepoda�il se p�e��st vstup nebo zadan� vstup nebyl platn�m ��slem");
				e.printStackTrace();
				break;
			}
		}

	}

	/**
	 * Na z�klad� vstupn�ho parametru, kter� reprezentuje jm�no/cestu souboru,
	 * ve kter�m je v domluven�m form�tu ulo�en hern� ter�n, na�te v�echny
	 * pot�ebn� informace ze souboru a vyp�e z�kladn� informace o n�m.
	 * 
	 * @param filename
	 *            jm�no/cesta k souboru hern�ho ter�nu
	 */
	public static void loadTerrain(String filename) {
		terrainFile = new TerrainFileHandler();
		terrainFile.loadTerFile(filename);
		terrainFile.printData();
	}

	/**
	 * Vytvo�� nov� okno aplikace, do kter�ho p�id� panel, ve kter�m lze
	 * vykreslovat v�echny pot�ebn� komponenty.
	 */
	public static void makeWindow() {
		frame = new JFrame();
		gamePanel = new GamePanel(terrain, shooter, target);

		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.setLayout(new BorderLayout());
		gamePanel.setSize(Constants.preferedWindowWidth + 20, Constants.preferedWindowHeight + 20);
		frame.add(gamePanel, BorderLayout.CENTER);

		frame.setTitle("Prototyp 1 | J. V�tek | A16B0165P");
		frame.pack();

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setSize(Constants.preferedWindowWidth, Constants.preferedWindowHeight);
	}
}
