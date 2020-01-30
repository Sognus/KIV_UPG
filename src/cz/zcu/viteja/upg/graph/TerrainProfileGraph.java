package cz.zcu.viteja.upg.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import cz.zcu.viteja.upg.Constants;
import cz.zcu.viteja.upg.Game;
import cz.zcu.viteja.upg.other.Vector3;

public class TerrainProfileGraph extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public static JFrame terrainProfileGraph = null;
	public static TerrainProfileGraph instance = null;
	
	private double azimuth = 0;
	private double elevation = 10;
	private double velocity = 200;
	
	private JFreeChart graph;
	private ChartPanel graphPanel;


	private ArrayList<Vector3> terrainPoints;
	
	public TerrainProfileGraph()
	{
		this.createGraph();
	}
	
	public TerrainProfileGraph(double azimuth, double elevation, double velocity)
	{
		this.azimuth = azimuth;
		this.elevation = elevation;
		this.velocity = velocity;
		
		this.createGraph();
	}
	
	public JFrame makeWindow()
	{
		terrainProfileGraph = new JFrame();
		
		// Nastavení grafu
		terrainProfileGraph.setLayout(new BorderLayout());
		terrainProfileGraph.add(graphPanel, BorderLayout.CENTER);
		
		// Vytvoøení ovládání
		JPanel controlPane = new JPanel();
		
		Dimension randomName = new Dimension(100, graphPanel.getHeight());
		controlPane.setSize(randomName);
		controlPane.setBackground(Color.WHITE);
		controlPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		GridBagLayout verticalMagic = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		
		controlPane.setLayout(verticalMagic);
		
		
		// TextArea azimuth + label
		c.gridx = 0;
		c.gridy = 0;
		Label labelAzimuth = new Label("Azimuth: ");
		JTextField textAreaAzimuth = new JTextField(5);
		textAreaAzimuth.setPreferredSize(new Dimension(75, textAreaAzimuth.getPreferredSize().height));
		
		controlPane.add(labelAzimuth, c);
		c.gridx = 1; 
		controlPane.add(textAreaAzimuth, c);
		
		// TextArea elevation + label
		c.gridx = 0;
		c.gridy = 1;
		Label labelElevation = new Label("Elevation: ");
		JTextField textAreaElevation = new JTextField(5);
		textAreaElevation.setPreferredSize(new Dimension(75, textAreaElevation.getPreferredSize().height));
		
		controlPane.add(labelElevation, c);
		c.gridx = 1; 
		controlPane.add(textAreaElevation, c);

		// TextArea velocity + label
		c.gridx = 0;
		c.gridy = 2;
		Label labelVelocity = new Label("Velocity: ");
		JTextField textAreaVelocity = new JTextField(5);
		textAreaVelocity.setPreferredSize(new Dimension(75, textAreaVelocity.getPreferredSize().height));
	
		controlPane.add(labelVelocity, c);
		c.gridx = 1; 
		controlPane.add(textAreaVelocity, c);
		
		// Filler
		JLabel filler = new JLabel(" ");
		c.gridx = 0;
		c.gridy = 3;
		
		controlPane.add(filler, c);
		
		// Buttons
		JButton buttonChange = new JButton("Aktualizovat graf");
		buttonChange.setAlignmentX(CENTER_ALIGNMENT);

		
		buttonChange.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  if(textAreaElevation.getText().length() > 0 && textAreaAzimuth.getText().length() > 0 && textAreaVelocity.getText().length() > 0)
			  {
				  try
				  {
					 terrainProfileGraph.remove(graphPanel);
					 
					 elevation = Double.parseDouble(textAreaElevation.getText());
					 azimuth = Double.parseDouble(textAreaAzimuth.getText());
					 velocity = Double.parseDouble(textAreaVelocity.getText());
					 
					 createGraph();
					 
					graphPanel = new ChartPanel(graph);
					 
					terrainProfileGraph.add(graphPanel, BorderLayout.CENTER);
					 
					terrainProfileGraph.revalidate();
					terrainProfileGraph.repaint();

				  }
				  catch(Exception exp)
				  {
					
					  exp.printStackTrace();
				  }
			  }
		  }
		});
		
		
		c.gridy = 4;
		c.fill = 1;
		
		controlPane.add(buttonChange, c);

		// Další filler
		// Filler
		JLabel filler2 = new JLabel(" ");
		c.gridx = 0;
		c.gridy = 5;
		
		controlPane.add(filler2, c);		
		
		// A button
		JButton buttonExit = new JButton("Návrat do menu");
		
		buttonExit.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  Game.terrainProfileGraphFrame.dispose();
		  }
		});
		
		
		buttonExit.setBackground(Color.red);
		buttonExit.setForeground(Color.WHITE);
		buttonExit.setAlignmentX(CENTER_ALIGNMENT);
		c.gridy = 6;
		c.fill = 1;
		c.anchor = GridBagConstraints.EAST;
		
		controlPane.add(buttonExit, c);
		
		
		terrainProfileGraph.add(controlPane, BorderLayout.EAST );
		
		
		// Nastavení okna
		terrainProfileGraph.pack();
		terrainProfileGraph.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		terrainProfileGraph.setLocationRelativeTo(null);
		Dimension d = new Dimension(Constants.preferedWindowWidth, Constants.preferedWindowHeight);
		terrainProfileGraph.setPreferredSize(d);
		
		terrainProfileGraph.setTitle("Støelec - Profil terénu | J. Vítek | A16B0165P");
		
		return terrainProfileGraph;
	}
	
	private void createGraph()
	{
		generateTerrainPoints();
		XYSeries data1 = generateTerrainData();
		XYSeries data2 = generateTrajectoryData();
		XYSeriesCollection ds = new XYSeriesCollection();
		ds.addSeries(data1);
		ds.addSeries(data2);
		graph = ChartFactory.createXYLineChart("Terrain Profile",
				"Distance [m]", "Altitude [m]",
				ds, PlotOrientation.VERTICAL,
				false, true, false);
		XYPlot plot = (XYPlot) graph.getPlot();
		
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
		
        XYAreaRenderer renderer = new XYAreaRenderer();
        plot.setRenderer(0, renderer);
        renderer.setSeriesPaint(1, new GradientPaint(0.0f, 0.0f, new Color(255, 0, 0), 0.0f, 0.0f, new Color(255, 171, 174)));  
        renderer.setOutline(true);
        renderer.setSeriesOutlinePaint(1, Color.BLACK);
       
        renderer.setSeriesPaint(0, new GradientPaint(0.0f, 0.0f, new Color(15, 90, 15), 0.0f, 0.0f, new Color(0, 255, 0)));  
        renderer.setSeriesOutlinePaint(0, Color.BLACK);
        
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.black);
        plot.setForegroundAlpha(1.0f);
        
        graphPanel = new ChartPanel(graph);
	}
	
	public void generateTerrainPoints() {
		terrainPoints = new ArrayList<Vector3>();
		
		double size = Game.terrain.getWidthInM();
		double distance = size/200;
		double x = Game.shooter.x;
		double y = Game.shooter.y;
		double z = Game.terrain.getAltitudeInM(x, y);
		terrainPoints.add(new Vector3(x,y,z));
		
		while(Game.terrain.isPointInVisibleTerrain(x, y)) {
			x += distance * Math.cos(Math.toRadians(-azimuth));
			y += distance * Math.sin(Math.toRadians(-azimuth));
			if(Game.terrain.isPointInVisibleTerrain(x, y)) {
				try{
					z = Game.terrain.getAltitudeInM(x, y);
					terrainPoints.add(new Vector3(x,y,z));
				} catch(Exception e) {;
					break;
				}
			}
		}
	}
	
	public XYSeries generateTerrainData() {
		XYSeries data = new XYSeries("Terrain");

		double size = Game.terrain.getWidthInM();
		double distance = size/200.0;
		
		for(int i = 0; i<terrainPoints.size(); i++) {
			Vector3 vector = terrainPoints.get(i);
			data.add((i*distance),vector.z);
		}

		return data;
	}
	
	public XYSeries generateTrajectoryData() {
		XYSeries data = new XYSeries("Trajectory");
	
		if(terrainPoints != null && !terrainPoints.isEmpty()) {
			for(int i = 0; i<terrainPoints.size(); i++) {
				Vector3 vector = terrainPoints.get(i);
				double distance = vector.getDistance(Game.shooter);
				double altitude = Game.shootingCalculator.getAltitude(Game.shooter, elevation, velocity, distance);
				if(altitude < vector.z) {
					if(altitude > 0) {
						data.add(distance,altitude);
					} else {
						data.add(distance,0);
					}
					break;
				}
				data.add(distance,altitude);
			}
		} else {
			data.add(0,0);
		}

		return data;
	}
}
