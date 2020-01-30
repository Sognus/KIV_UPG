package cz.zcu.viteja.upg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * T��da reprezentuj�c� hern� ter�n. Tato t��da n�m poskytuje p��stup k
 * informac�m jako je nap��klad zji�t�n�, jak� je nadmo�sk� v��ka na dan�ch
 * sou�adnic�ch ter�nu a dal��..
 * 
 * @author Jakub V�tek - A16B0165P
 * @version 1.02.00
 *
 */
public class Terrain {

	/** Hern� ter�n reprezentovan� jako v�cerozm�n� pole nadmo�sk�ch v��ek */
	public int terrain[][];
	/** Rozestup mezi sloupci v milimetrech */
	public int deltaXInMM;
	/** Rozestup mezi ��deky v milimetrech */
	public int deltaYInMM;

	/** Po�et sloupc� v ter�nu */
	private int columnCount;
	/** Po�et ��dk� v ter�nu */
	private int rowCount;

	/** Bitmapa vytvo�en� dle nadmo�sk� v��ky */
	private BufferedImage terrainImage;

	/**
	 * Pomocn� reference na instanci, kter� zaji�tuje n���t�n� ter�nu ze souboru
	 */
	private TerrainFileHandler fHandler;

	/**
	 * Z�kladn� konstruktor, kter� vstupn� argumenty ulo�� do jejich spr�vn�ch
	 * instanc�.
	 * 
	 * @param terrain
	 *            v�cerozm�rn� pole s nadmo�sk�mi v��kami
	 * @param deltaXInMM
	 *            rozestup mezi sloupci v milimetrech
	 * @param deltaYInMM
	 *            rozestup mezi ��dky v milimetrech
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
	 * Roz���en� konstruktor, kter� nav�c oproti p�vodn�mu konstruktoru jako
	 * parametry po�aduje po�et ��dk� a sloupc� v ter�nu. Byl vyu��v�n hlavn�
	 * pro testovac� ��ely
	 * 
	 * @param terrain
	 *            v�cerozm�rn� pole s nadmo�sk�mi v��kami
	 * @param deltaXInMM
	 *            rozestup mezi sloupci v milimetrech
	 * @param deltaYInMM
	 *            rozestup mezi ��dky v milimetrech
	 * @param rowCount
	 *            po�et ��dk� v ter�nu
	 * @param columnCount
	 *            po�et sloupc� v ter�nu
	 */
	public Terrain(int[][] terrain, int deltaXInMM, int deltaYInMM, int rowCount, int columnCount) {
		this(terrain, deltaXInMM, deltaYInMM);
		this.columnCount = columnCount;
		this.rowCount = rowCount;

		this.makeImage();

	}

	/**
	 * Z�sk� nadmo�skou v��ku na sou�adnich v ter�nu u�en�ch vstupn�mi argumenty
	 * t�to metody.
	 * 
	 * @param x
	 *            sou�adnice na ose X
	 * @param y
	 *            sou�adnice na ose Y
	 * @return nadmo�sk� v��ka v bode [x, y]
	 */
	public double getAltitudeInM(double x, double y) {
		// Z�sk�m sou�adnice v milimetrech
		int mmX = (int) (x * Constants.mToMM);
		int mmY = (int) (y * Constants.mToMM);

		// Vr�t�m nadmo�skou v��ku v metrech
		return (terrain[mmX][mmY] / Constants.mmToM);
	}

	/**
	 * Vykreslov�n� ter�nu. Na z�klad� pou�it� prom�nn� p�ev�d�j�c� metry na
	 * pixely vykresl� v okn� b�lou barvou hern� ter�n p�izp�spoben� aktu�ln�
	 * v��ce a ���ce okna. Na hranic�ch ter�nu je �ernou barvou vykreslena
	 * hranice.
	 * 
	 * @param g2
	 *            grafick� kontext
	 * @param scale
	 *            prom�nn� d�ky kter� lze p�ev�d�t metry na pixely
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
	 * Vr�t� ���ku aktu�ln�ho ter�nu v metrech
	 * 
	 * @return aktu�ln� ���ka ter�nu v metrech
	 */
	public double getWidthInM() {
		return (columnCount * deltaXInMM / 1000.0);

	}

	/**
	 * Vr�t� v��ku aktu�ln�ho ter�nu v metrech
	 * 
	 * @return aktu�ln� v��ka ter�nu v metrech
	 */
	public double getHeightInM() {
		return (rowCount * deltaYInMM / 1000.0);

	}

	/**
	 * Vr�t� aktu�ln� ���ku hern�ho ter�nu v pixelech
	 * 
	 * @return ���ka ter�nu v pixelech
	 */
	public double getWidthInPixels() {
		return this.getWidthInM() * Game.gamePanel.getScale();
	}

	/**
	 * Vr�t� aktu�ln� ���ku hern�ho ter�nu v pixelech
	 * 
	 * @return ���ka ter�nu v pixelech
	 */
	public double getHeightInPixels() {
		return this.getHeightInM() * Game.gamePanel.getScale();
	}

	/**
	 * Na z�klad� aktu�ln�ho na�ten�ho ter�nu vytvo�� bitmapu, reprezentuj�c�
	 * ter�n. Pokud bitmapa ji� existuje, bude vr�cena existuj�c� bitmapa.
	 * 
	 * @return bitmapa ter�nu
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

		// Ter�n je rovn�
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
