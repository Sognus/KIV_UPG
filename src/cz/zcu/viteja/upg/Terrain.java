package cz.zcu.viteja.upg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Tøída reprezentující herní terén. Tato tøída nám poskytuje pøístup k
 * informacím jako je napøíklad zjištìní, jaká je nadmoøská výška na daných
 * souøadnicích terénu a další..
 * 
 * @author Jakub Vítek - A16B0165P
 * @version 1.02.00
 *
 */
public class Terrain {

	/** Herní terén reprezentovaný jako vícerozmìné pole nadmoøských výšek */
	public int terrain[][];
	/** Rozestup mezi sloupci v milimetrech */
	public int deltaXInMM;
	/** Rozestup mezi øádeky v milimetrech */
	public int deltaYInMM;

	/** Poèet sloupcù v terénu */
	private int columnCount;
	/** Poèet øádkù v terénu */
	private int rowCount;

	/** Bitmapa vytvoøená dle nadmoøské výšky */
	private BufferedImage terrainImage;

	/**
	 * Pomocná reference na instanci, která zajištuje náèítání terénu ze souboru
	 */
	private TerrainFileHandler fHandler;

	/**
	 * Základní konstruktor, který vstupní argumenty uloží do jejich správných
	 * instancí.
	 * 
	 * @param terrain
	 *            vícerozmìrné pole s nadmoøskými výškami
	 * @param deltaXInMM
	 *            rozestup mezi sloupci v milimetrech
	 * @param deltaYInMM
	 *            rozestup mezi øádky v milimetrech
	 */
	public Terrain(int[][] terrain, int deltaXInMM, int deltaYInMM) {
		this.terrain = terrain;
		this.deltaXInMM = deltaXInMM;
		this.deltaYInMM = deltaYInMM;
		this.rowCount = this.terrain.length;
		this.columnCount = this.terrain[0].length;

		this.makeImage();
	}

	/**
	 * Rozšíøený konstruktor, který navíc oproti pùvodnímu konstruktoru jako
	 * parametry požaduje poèet øádkù a sloupcù v terénu. Byl využíván hlavnì
	 * pro testovací úèely
	 * 
	 * @param terrain
	 *            vícerozmìrné pole s nadmoøskými výškami
	 * @param deltaXInMM
	 *            rozestup mezi sloupci v milimetrech
	 * @param deltaYInMM
	 *            rozestup mezi øádky v milimetrech
	 * @param rowCount
	 *            poèet øádkù v terénu
	 * @param columnCount
	 *            poèet sloupcù v terénu
	 */
	public Terrain(int[][] terrain, int deltaXInMM, int deltaYInMM, int rowCount, int columnCount) {
		this(terrain, deltaXInMM, deltaYInMM);
		this.columnCount = columnCount;
		this.rowCount = rowCount;

		this.makeImage();

	}

	/**
	 * Získá nadmoøskou výšku na souøadnich v terénu uèených vstupními argumenty
	 * této metody.
	 * 
	 * @param x
	 *            souøadnice na ose X
	 * @param y
	 *            souøadnice na ose Y
	 * @return nadmoøská výška v bode [x, y]
	 */
	public double getAltitudeInM(double x, double y) {
		// Získám souøadnice v milimetrech
		int mmX = (int) (x * Constants.mToMM);
		int mmY = (int) (y * Constants.mToMM);

		// Vrátím nadmoøskou výšku v metrech
		return (terrain[mmX][mmY] / Constants.mmToM);
	}

	/**
	 * Vykreslování terénu. Na základì použití promìnné pøevádìjící metry na
	 * pixely vykreslí v oknì bílou barvou herní terén pøizpùspobený aktuální
	 * výšce a šíøce okna. Na hranicích terénu je èernou barvou vykreslena
	 * hranice.
	 * 
	 * @param g2
	 *            grafický kontext
	 * @param scale
	 *            promìnná díky které lze pøevádìt metry na pixely
	 */
	public void draw(Graphics2D g2, double scale) {

		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, (int) (getWidthInM() * scale), (int) (getHeightInM() * scale));

		g2.drawImage(this.terrainImage, 0, 0, (int) (getWidthInM() * scale), (int) (getHeightInM() * scale), null);
		// g2.drawImage(this.terrainImage, 0, 0, null);

		// g2.setColor(Color.black);
		// g2.drawRect(0, 0, (int) (getWidthInM() * scale), (int)
		// (getHeightInM() * scale));

	}

	/**
	 * Vrátí šíøku aktuálního terénu v metrech
	 * 
	 * @return aktuální šíøka terénu v metrech
	 */
	public double getWidthInM() {
		return (columnCount * deltaXInMM / 1000.0);

	}

	/**
	 * Vrátí výšku aktuálního terénu v metrech
	 * 
	 * @return aktuální výška terénu v metrech
	 */
	public double getHeightInM() {
		return (rowCount * deltaYInMM / 1000.0);

	}

	/**
	 * Vrátí aktuální šíøku herního terénu v pixelech
	 * 
	 * @return šíøka terénu v pixelech
	 */
	public double getWidthInPixels() {
		return this.getWidthInM() * Game.gamePanel.getScale();
	}

	/**
	 * Vrátí aktuální šíøku herního terénu v pixelech
	 * 
	 * @return šíøka terénu v pixelech
	 */
	public double getHeightInPixels() {
		return this.getHeightInM() * Game.gamePanel.getScale();
	}

	/**
	 * Na základì aktuálního naèteného terénu vytvoøí bitmapu, reprezentující
	 * terén. Pokud bitmapa již existuje, bude vrácena existující bitmapa.
	 * 
	 * @return bitmapa terénu
	 */
	public BufferedImage makeImage() {
		if (this.terrainImage != null) {
			return this.terrainImage;
		}

		this.terrainImage = new BufferedImage(columnCount, rowCount, BufferedImage.TYPE_INT_RGB);
		Graphics2D imgGraphics = (Graphics2D) terrainImage.createGraphics();

		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;

		for (int y = 0; y < rowCount; y++) {
			for (int x = 0; x < columnCount; x++) {
				int val = this.terrain[y][x];

				if (val > max) {
					max = val;
				}

				if (val < min) {
					min = val;
				}
			}
		}

		// min = 999;

		// Terén je rovný
		if (max == min) {
			imgGraphics.setColor(Color.gray);
			imgGraphics.fillRect(0, 0, columnCount, rowCount);
		} else {
			for (int y = 0; y < rowCount; y++) {
				for (int x = 0; x < columnCount; x++) {
					int val = this.terrain[y][x];
					double step = (max - min) / 256;
					int rgb = (int) (val / step);

					// Korekce
					rgb = rgb > 255 ? 255 : rgb;
					rgb = rgb < 0 ? 0 : rgb;

					Color color = new Color(rgb, rgb, rgb, 1);
					terrainImage.setRGB(x, y, color.getRGB());

				}
			}
		}

		File outputfile = new File("image.jpg");
		try {
			ImageIO.write(terrainImage, "jpg", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return terrainImage;

	}

	public boolean isPointInVisibleTerrain(double x, double y) {
		if(x >= 0 && x <= getWidthInM() && y >= 0 && y <= getHeightInM()) {
			return true;
		
		}
		return false;
	}
}
