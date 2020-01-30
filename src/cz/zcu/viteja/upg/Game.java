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
 * Hlavní vstupní tøída aplikace, která zajištuje základní úkony, kterımi jsou
 * napøíklad: naètení herní mapy ze souboru, získání a udrení všech dùleitıch
 * instancí na èásti aplikace.
 * 
 * @author Jakub Vítek - A16B0165P
 * @version 1.05.00
 *
 */
public class Game {

	// Konstanty
	/** konstanta pro pøevod mm na m */
	static final double mmToM = 1000.0;
	/** konstanta pro pøevod m na mm */
	static final double mToMM = 0.001;

	// Tøídní atributy
	/** Reference na instanci, která zajišuje naèítání souboru terénu */
	public static TerrainFileHandler terrainFile;
	/** Reference na instanci, která reprezentuje naètenı herní terén */
	public static Terrain terrain;

	/**
	 * Reference na instanci, která reprezentuje pozici a další vlastnosti
	 * støelce
	 */
	public static NamedPosition shooter;
	/**
	 * Reference na instanci, která reprezentuje pozici a další vlastnosti cíle
	 */
	public static NamedPosition target;
	/**
	 * Reference na instanci, která na základì vstupních dat dokáe vypoèítat
	 * dopad støely a ovìøit zda støela zasáhla èi minula
	 */
	public static ShootingCalculator shootingCalculator;

	/** Reference na instanci okna aplikace */
	public static JFrame frame;
	public static JFrame dependencyGraphFrame;
	/**
	 * Reference na instanci herního panelu, kterı je vykreslován v oknì - terén
	 */
	public static GamePanel gamePanel;
	/** Reference na instanci panelu pro vykreslení smìru a intenzity vìtru */
	public static CompassPanel compassPanel;
	/** Reference na instanci vìtru */
	public static Wind wind;
	/** Reference na trajektorii */
	public static Trajectory trajectory;

	/**
	 * Reference na vstupní parametry, která je pouita, aby bylo moné k
	 * parametrùm z console pøistupovat odkudkoliv ze tøídy ani by bylo nutné
	 * pøedávat hodnoty jako argument metod
	 */
	private static String[] startArgs;
	
	public  static boolean graphMainLoopRunning;
	public static JFrame terrainProfileGraphFrame;

	/**
	 * Hlavní metoda aplikace
	 * 
	 * @param args
	 *            vstupní parametry aplikace
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// Uloit vstupní parametry tak, aby bylo moné pøistupovat k nim v celé
		// tøídì
		startArgs = args;

		// Kontrola jestli je soubor zadán jako parametr z konzole
		String fileName = (args.length < 4) ? "rovny1metr_1km_x_1km.ter" : args[3];

		// Zjistí souèasné umístìní pracovního adresáøe
		File relative = new File(Game.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		// Zjistí absolutní cestu k náhradnímu souboru
		String absolute = relative.getParentFile().getAbsolutePath() + "\rovny1metr_1km_x_1km.ter";

		// Ovìøí zda soubor existuje
		String filePath = new File(fileName).exists() ? fileName : absolute;

		// Ovìøí znovu, zda soubor existuje, pojistka pro pøípad, e by
		// neexistoval ani náhradní soubor s terénem, aplikace se pak ukonèí
		File f = new File(filePath);
		if (!f.exists()) {
			System.out.println("-----CHYBA APLIKACE----");
			System.out.println("Aplikace nìkolikrát ovìøila, zda má k dispozici zadanı soubor s terénem.");
			System.out.println("Poadovanı soubor i všechny záloní soubory pravdìpodobnì neexistují.");
			System.out
					.println("Pokud tuto aplikaci spouštíte v adresáøové struktuøe odevzdávané pro pøedmìt KIV/UPG, ");
			System.out.println("nahrajte soubor rovny1metr_1km_x_1km.ter do koøene sloky");
			System.out.println(
					"Pokud spouštíte aplikaci jinak, zadejte v 3. parametru pøi spouštìní cestu k platnému souboru s terénem.");

			System.out.println();
			System.out.println("APLIKACE BUDE UKONÈENA");
			System.out.println("----------");

			System.out.println("Ukonèení po stisku libovolné klávesy...");
			System.in.read();
			System.exit(-1);

		}

		// Naètení souboru
		loadTerrain(filePath);

		// Získání všech nutnıch instancí objektù, nastavení nutnıch promìnnıch
		initData();

		// Vytvoøení okna
		makeWindow();
		
		// Rozhodnutí mezi vykreslením grafù a hlavním cyklem
		graphsOrGame();

		// Hlavní herní cyklus
		//gameMainLoop();
	}

	private static void graphsOrGame() {
		
		// Nekoneènı cyklus
		boolean running = true;
		
		System.out.println("-----Vítejte na støelnici-----");
		
		while(running)
		{
			System.out.println("MENU:");
			System.out.println("[0] Hrát");
			System.out.println("[1] Zobrazit graf závislosti vstupních parametrù na vıslednou vzdálenost støely");
			System.out.println("[2] Zobrazit graf profilu terénu");
			System.out.println("[3] Ukonèit aplikaci");
			
			System.out.println();
			System.out.print("Jaká je vaše volba: ");
			
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
					// TODO: vytvoøit vykreslování grafù
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
					System.out.println("*****UKONÈUJI APLIKACI*****");
					running = false;
					break;
				default:
					System.out.println("-----Neplatná volba, budete navrácen/a do menu-----");
					System.out.println();
					System.out.println();
					break;
				}
				
			} catch (Exception e) {
				System.out.println("Pøi ètení vstupu z console nastala chyba!");
				e.printStackTrace();
			}
			
			System.out.println();
			
		}
		
	}

	/**
	 * Získá všechny nutné dùleité instance, bez kterıch by aplikace nemohla
	 * pracovat a uloí je do korespondujících statickıch atributù
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
	 * Metoda, která se stará o funkènost hlavního cyklu aplikace. Metoda se
	 * pokusí získat hodnoty konzole parametrù, pokud je nenajde, zeptá se na
	 * všechny potøebné hodnoty uivatele. Hlavní cyklus bìí tak dlouho, dokud
	 * se uivatel nerozhodne ukonèit aplikaci
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

					System.out.print("Zadejte elevaci støelby: ");
					elevace = Double.valueOf(br.readLine());

					System.out.print("Zadejte rychlost støely: ");
					rychlost = Double.valueOf(br.readLine());

				}

				// shootingCalculator.shoot(azimuth, distance);
				shootingCalculator.shoot(azimuth, elevace, rychlost);

				gamePanel.setHitSpot(shootingCalculator.getHitSpot());

				System.out.println();
				if (shootingCalculator.testTargetHit()) {
					System.out.println("ZÁSAH! Cíl byl znièen!");
				}

				else {
					System.out.println("VEDLE! Cíl nebyl zasaen!");
				}

				frame.setVisible(true);

				startArgs = new String[0];
				frame.repaint();

				// Zeptat se znova na hraní

				System.out.println();
				System.out.print("Návrat do herního menu? <ano/ne>: ");
				String hrat = br.readLine();

				if (hrat.toLowerCase().equals("a") || hrat.toLowerCase().equals("ano")
						|| hrat.toLowerCase().equals("true")) {
					System.out.println("-----Návrat do hlavního menu-----");

					frame.setVisible(false);
					frame.dispose();
					break;
				}

				// wind.generateParams();
				wind.generateParamsAnimated();

				System.out.println("----------");

			} catch (NumberFormatException | IOException e) {
				System.out.println("Nepodaøil se pøeèíst vstup nebo zadanı vstup nebyl platnım èíslem");
				e.printStackTrace();
				break;
			}
		}

	}

	/**
	 * Na základì vstupního parametru, kterı reprezentuje jméno/cestu souboru,
	 * ve kterém je v domluveném formátu uloen herní terén, naète všechny
	 * potøebné informace ze souboru a vypíše základní informace o nìm.
	 * 
	 * @param filename
	 *            jméno/cesta k souboru herního terénu
	 */
	public static void loadTerrain(String filename) {
		terrainFile = new TerrainFileHandler();
		terrainFile.loadTerFile(filename);
		terrainFile.printData();
	}

	/**
	 * Vytvoøí nové okno aplikace, do kterého pøidá panel, ve kterém lze
	 * vykreslovat všechny potøebné komponenty.
	 */
	public static void makeWindow() {
		frame = new JFrame();

		// Nastavení panelù
		gamePanel = new GamePanel(terrain, shooter, target);
		gamePanel.trajectory = trajectory;
		gamePanel.setSize(Constants.preferedWindowWidth + 20, Constants.preferedWindowHeight + 20);

		compassPanel = new CompassPanel(wind);
		wind.addObserver((Observer) compassPanel);

		// Nastavení layoutù
		frame.setLayout(new GridLayout());
		frame.add(gamePanel);
		frame.add(compassPanel);

		// Zobrazení a interakce
		frame.setTitle("Støelec - Herní okno | J. Vítek | A16B0165P");
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setSize(Constants.preferedWindowWidth, Constants.preferedWindowHeight);
	}
}
