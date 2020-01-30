package cz.zcu.viteja.upg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * Tøída, jejíž instance reprezentují pozici a další dùležité vlastnosti všech
 * klíèových pozicovatelných objektù (støelec, cíl, oblast zásahu).
 * 
 * @author Jakub Vítek A16B0165P
 * @version 1.02.00
 *
 */
public class NamedPosition {

	// Konstanty
	/** pro pøevod mm na m */
	static final double mmToM = 1000.0;
	/** pro pøevod m na mm */
	static final double mToMM = 0.001;

	// Instanèní promìnné
	/** Souøadnice objektu na ose X (v metrech) */
	public double x;
	/** Souøadnice objektu na ose Y (v metrech) */
	public double y;

	/** Naètený obrázek pro vykreslování */
	private BufferedImage image;

	/**
	 * Øetìzec reprezentující typ objektu (zda se jedná o cíl, støelce nebo
	 * oblast zásahu
	 */
	public String positionType;
	/** Barva, která se využívá pøi vykreslování objektu */
	public Color color;
	/**
	 * Velikost objektu (pro støelce a cíl = velikost úseèky, pro oblast zásahu
	 * = prùmìr kruhu
	 */
	public double size;

	/**
	 * Konstruktor tøídy NamedPosition, který uloží vstupní parametry do jejich
	 * korespondujících promìnných
	 * 
	 * @param x
	 *            souøadnice na ose X (v metrech)
	 * @param y
	 *            souøadnice na ose Y (v metrech)
	 * @param positionType
	 *            typ objektu (støelec, cíl nebo oblast zásahu)
	 * @param color
	 *            Barva použitá pøi vykreslování objektu
	 * @param size
	 *            Velikost objektu (pro cíl a støelce velikost úseèky, pro
	 *            oblast zásahu prùmìr kruhu)
	 */
	public NamedPosition(double x, double y, String positionType, Color color, double size) {
		this.x = x;
		this.y = y;
		this.positionType = positionType;
		this.color = color;
		this.size = size;

		this.loadImage();
	}

	/**
	 * Vypoèítá vzdálenost mezi dvìmi pojmenovanými pozicemi a vratí výslednou
	 * hodnotu jako primitivní typ double
	 * 
	 * @param position
	 *            pojmenovaná pozice, ke které se bude poèítat vzdálenost od
	 *            souèasné pozice
	 * @return vzdálenost mezi souèasnou a cílovou pozicí (v metrech)
	 */
	public double getDistance(NamedPosition position) {
		return Math.sqrt(Math.pow(position.x - this.x, 2) + Math.pow(position.y - this.y, 2));
	}

	/**
	 * 
	 * Metoda, která se na základì vnitøního stavu objektu rothodne, zda se je
	 * objekt støelcem/cílem nebo oblastí zásahu, na základì toho pak zavolá
	 * korespondující vykreslovací metodu
	 * 
	 * @param g2
	 *            grafický kontext
	 * @param scale
	 *            promìnná díky které lze pøevést metry na pixely
	 */
	public void draw(Graphics2D g2, double scale) {

		switch (this.positionType) {
		case Constants.SHOOTER:
		case Constants.TARGET:
			this.drawShooterTarget(g2, scale);
			break;
		case Constants.HITSPOT:
			this.drawHitspot(g2, scale);
			break;

		}

	}

	/**
	 * Metoda která vykresluje oblast zásahu
	 * 
	 * @param g2
	 *            grafický kontext
	 * @param scale
	 *            promìnná díky které lze pøevést metry na pixely
	 */
	private void drawHitspot(Graphics2D g2, double scale) {

		// Pozice pro vykreslení -> size je prùmìr!!!;
		double positionDrawX = (this.x - (this.size / 2)) * scale;
		double positionDrawY = (this.y - (this.size / 2)) * scale;

		g2.setColor(this.color);
		g2.fill(new Ellipse2D.Double(positionDrawX, positionDrawY, this.size * scale, this.size * scale));
	}

	/**
	 * Metoda, která vykresluje pozici støelce a cíle (vykreslení probíhá
	 * stejnì, mìní se jen barva, která je uložena jako promìnná souèasné
	 * instance.
	 * 
	 * @deprecated Metoda již dále v aplikaci není využita, její deklarace však
	 *             zùstává z dùvodu možné potøeby kompatibility
	 * 
	 * @param g2
	 *            grafický kontext
	 * @param scale
	 *            promìnná díky které lze pøevést metry na pixely
	 */
	@Deprecated
	private void drawTargetShooter(Graphics2D g2, double scale) {

		double positionX = this.x * scale;
		double positionY = this.y * scale;
		double offset = this.size / 2;

		g2.setColor(this.color);
		g2.draw(new Line2D.Double(positionX - offset, positionY, positionX + offset, positionY));
		g2.draw(new Line2D.Double(positionX, positionY - offset, positionX, positionY + offset));

		/*
		 * g2.setColor(color); int xs = (int) (x * scale); int ys = (int) (y *
		 * scale);
		 * 
		 * g2.drawLine(xs, ys, xs + 5, ys); g2.drawLine(xs, ys, xs, ys + 5);
		 * g2.drawLine(xs, ys, xs - 5, ys); g2.drawLine(xs, ys, xs, ys - 5);
		 */
	}

	/**
	 * Metoda, která vykresluje pozici støelce a cíle (vykreslení probíhá
	 * stejnì, mìní se jen barva, která je uložena jako promìnná souèasné
	 * instance.
	 * 
	 * @param g2
	 *            grafický kontext
	 * @param scale
	 *            promìnná díky které lze pøevést metry na pixely
	 */
	private void drawShooterTarget(Graphics2D g2, double scale) {
		double positionX = this.x * scale;
		double positionY = this.y * scale;
		double offset = this.size / 2;

		// Vykreslení obrázku

		if (this.image != null) {
			g2.drawImage(image, (int) (positionX - (this.size / 2)), (int) (positionY - (this.size / 2)),
					(int) (this.size), (int) (this.size), null);
		}

		// Vykreslení debug køížku

		if (Constants.DEBUG == true) {
			g2.setColor(this.color);
			g2.draw(new Line2D.Double(positionX - offset, positionY, positionX + offset, positionY));
			g2.draw(new Line2D.Double(positionX, positionY - offset, positionX, positionY + offset));
		}

	}

	/**
	 * Naète obrázek (piktogram) ze souboru na základì typu pozice
	 */
	private void loadImage() {
		if (this.positionType.equals(Constants.HITSPOT)) {
			return;
		}

		String filePath = "";

		switch (this.positionType) {
		case Constants.SHOOTER:
			filePath = Constants.shooterImagePath;
			break;
		case Constants.TARGET:
			filePath = Constants.targetImagePath;
			break;
		default:
			filePath = "";
			break;
		}

		if (filePath.equals("")) {
			return;
		}

		try {
			File f = new File(filePath);
			this.image = ImageIO.read(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
