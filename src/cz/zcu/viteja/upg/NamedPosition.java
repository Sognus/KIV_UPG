package cz.zcu.viteja.upg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

public class NamedPosition {

	// Konstanty
	/** pro p�evod mm na m */
	static final double mmToM = 1000.0;
	/** pro p�evod m na mm */
	static final double mToMM = 0.001;

	// Instan�n� prom�nn�
	public double x;
	public double y;

	public String positionType;
	public Color color;
	public double size;

	public NamedPosition(double x, double y, String positionType, Color color, double size) {
		this.x = x;
		this.y = y;
		this.positionType = positionType;
		this.color = color;
		this.size = size;
	}

	/**
	 * Vypo��t� vzd�lenost mezi dv�mi pojmenovan�mi pozicemi a vrat� v�slednou
	 * hodnotu jako primitivn� typ double
	 * 
	 * @param position
	 *            pojmenovan� pozice, ke kter� se bude po��tat vzd�lenost od
	 *            sou�asn� pozice
	 * @return vzd�lenost mezi sou�asnou a c�lovou pozic� (v metrech)
	 */
	public double getDistance(NamedPosition position) {
		return Math.sqrt(Math.pow(position.x - this.x, 2) + Math.pow(position.y - this.y, 2));
	}

	public void draw(Graphics2D g2, double scale) {

		AffineTransform transform = g2.getTransform();
		g2.translate(10, 10);

		switch (this.positionType) {
		case Constants.SHOOTER:
		case Constants.TARGET:
			this.drawTargetShooter(g2, scale);
			break;
		case Constants.HITSPOT:
			this.drawHitspot(g2, scale);
			break;

		}

		g2.transform(transform);

	}

	private void drawHitspot(Graphics2D g2, double scale) {
		// TODO Auto-generated method stub

	}

	private void drawTargetShooter(Graphics2D g2, double scale) {
		double positionX = this.x * scale;
		double positionY = this.y * scale;
		double offset = this.size / 2;

		if (this.positionType.equals(Constants.SHOOTER)) {
			System.out.println("X/Y: " + x + "/" + y);
		}

		g2.setColor(this.color);
		g2.draw(new Line2D.Double(positionX - offset, positionY, positionX + offset, positionY));
		g2.draw(new Line2D.Double(positionX, positionY - offset, positionX, positionY + offset));

	}

}
