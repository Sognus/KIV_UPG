package cz.zcu.viteja.upg;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observer;

import javax.swing.JFrame;

import cz.zcu.viteja.upg.graph.DependencyGraph;
import cz.zcu.viteja.upg.graph.TerrainProfileGraph;

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
	public static JFrame dependencyGraphFrame;
	/**
	 * Reference na instanci hern�ho panelu, kter� je vykreslov�n v okn� - ter�n
	 */
	public static GamePanel gamePanel;
	/** Reference na instanci panelu pro vykreslen� sm�ru a intenzity v�tru */
	public static CompassPanel compassPanel;
	/** Reference na instanci v�tru */
	public static Wind wind;
	/** Reference na trajektorii */
	public static Trajectory trajectory;

	/**
	 * Reference na vstupn� parametry, kter� je pou�ita, aby bylo mo�n� k
	 * parametr�m z console p�istupovat odkudkoliv ze t��dy ani� by bylo nutn�
	 * p�ed�vat hodnoty jako argument metod
	 */
	private static String[] startArgs;
	
	public  static boolean graphMainLoopRunning;
	public static JFrame terrainProfileGraphFrame;

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
		String fileName = (args.length < 4) ? "rovny1metr_1km_x_1km.ter" : args[3];

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
		
		// Rozhodnut� mezi vykreslen�m graf� a hlavn�m cyklem
		graphsOrGame();

		// Hlavn� hern� cyklus
		//gameMainLoop();
	}

	private static void graphsOrGame() {
		
		// Nekone�n� cyklus
		boolean running = true;
		
		System.out.println("-----V�tejte na st�elnici-----");
		
		while(running)
		{
			System.out.println("MENU:");
			System.out.println("[0] Hr�t");
			System.out.println("[1] Zobrazit graf z�vislosti vstupn�ch parametr� na v�slednou vzd�lenost st�ely");
			System.out.println("[2] Zobrazit graf profilu ter�nu");
			System.out.println("[3] Ukon�it aplikaci");
			
			System.out.println();
			System.out.print("Jak� je va�e volba: ");
			
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String odpoved = br.readLine();
				
				switch (odpoved) {
				case "0":
					if(dependencyGraphFrame != null)
					{
						dependencyGraphFrame.dispose();
					}
					
					if(terrainProfileGraphFrame != null)
					{
						terrainProfileGraphFrame.dispose();
					}
					
					System.out.println();
					frame.setVisible(true);
					gameMainLoop();
					break;
				case "1":
					// TODO: vytvo�it vykreslov�n� graf�
					if(dependencyGraphFrame != null)
					{
						dependencyGraphFrame.dispose();
					}
					
					if(terrainProfileGraphFrame != null)
					{
						terrainProfileGraphFrame.dispose();
					}
					
					
					frame.setVisible(false);
					DependencyGraph dg = new DependencyGraph();
					dependencyGraphFrame = dg.makeWindow();
					dependencyGraphFrame.setVisible(true);
					
					break;
				case "2":
					if(dependencyGraphFrame != null)
					{
						dependencyGraphFrame.dispose();
					}
					
					if(terrainProfileGraphFrame != null)
					{
						terrainProfileGraphFrame.dispose();
					}
					
					frame.setVisible(false);
					TerrainProfileGraph tpg = new TerrainProfileGraph();
					terrainProfileGraphFrame = tpg.makeWindow();
					terrainProfileGraphFrame.setVisible(true);
					break;
				case "3":
					System.out.println("*****UKON�UJI APLIKACI*****");
					running = false;
					break;
				default:
					System.out.println("-----Neplatn� volba, budete navr�cen/a do menu-----");
					System.out.println();
					System.out.println();
					break;
				}
				
			} catch (Exception e) {
				System.out.println("P�i �ten� vstupu z console nastala chyba!");
				e.printStackTrace();
			}
			
			System.out.println();
			
		}
		
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
				20.0);

		terrain = new Terrain(terrainFile.terrain, terrainFile.deltaX, terrainFile.deltaY, terrainFile.rows,
				terrainFile.columns);

		shootingCalculator = new ShootingCalculator(shooter, target);

		wind = new Wind(100);
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
				double elevace;
				double rychlost;
				if (startArgs.length >= 3 && !startArgs.equals(null)) {
					azimuth = Double.valueOf(startArgs[0]);
					elevace = Double.valueOf(startArgs[1]);
					rychlost = Double.valueOf(startArgs[2]);
				} else {

					System.out.print("Zadejte azimut: ");
					azimuth = Double.valueOf(br.readLine());

					System.out.print("Zadejte elevaci st�elby: ");
					elevace = Double.valueOf(br.readLine());

					System.out.print("Zadejte rychlost st�ely: ");
					rychlost = Double.valueOf(br.readLine());

				}

				// shootingCalculator.shoot(azimuth, distance);
				shootingCalculator.shoot(azimuth, elevace, rychlost);

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
				System.out.print("N�vrat do hern�ho menu? <ano/ne>: ");
				String hrat = br.readLine();

				if (hrat.toLowerCase().equals("a") || hrat.toLowerCase().equals("ano")
						|| hrat.toLowerCase().equals("true")) {
					System.out.println("-----N�vrat do hlavn�ho menu-----");

					frame.setVisible(false);
					frame.dispose();
					break;
				}

				// wind.generateParams();
				wind.generateParamsAnimated();

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

		// Nastaven� panel�
		gamePanel = new GamePanel(terrain, shooter, target);
		gamePanel.trajectory = trajectory;
		gamePanel.setSize(Constants.preferedWindowWidth + 20, Constants.preferedWindowHeight + 20);

		compassPanel = new CompassPanel(wind);
		wind.addObserver((Observer) compassPanel);

		// Nastaven� layout�
		frame.setLayout(new GridLayout());
		frame.add(gamePanel);
		frame.add(compassPanel);

		// Zobrazen� a interakce
		frame.setTitle("St�elec - Hern� okno | J. V�tek | A16B0165P");
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setSize(Constants.preferedWindowWidth, Constants.preferedWindowHeight);
	}
}
