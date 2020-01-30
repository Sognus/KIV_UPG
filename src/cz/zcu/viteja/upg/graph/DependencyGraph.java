package cz.zcu.viteja.upg.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import cz.zcu.viteja.upg.Constants;
import cz.zcu.viteja.upg.Game;

public class DependencyGraph extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	
	public static JFrame dependencyGraphFrame = null;
	public static DependencyGraph instance = null;
	
	private JFreeChart graph;
	private ChartPanel graphPanel;
	
	public boolean running = true;
	
	private double azimuth = 0;
	private double elevation = 10;
	private double velocity = 200;
	
	public DependencyGraph()
	{
		azimuth = 0;
		elevation = 45;
		velocity = 200;	
		
		this.createGraph();
		
		instance = this;
	}
	
	public DependencyGraph(double azimuth, double elevation, double velocity)
	{
		this.azimuth = azimuth;
		this.elevation = elevation;
		this.velocity = velocity;
		
		this.createGraph();
		
		instance = this;
		
		
	}
	
	public DependencyGraph(double elevationInput)
	{
		azimuth = 0;
		elevation = elevationInput;
		velocity = 200;
		
		this.createGraph();
		
		instance = this;
		
	}
	
	private void createGraph()
	{
		// Pøíprava panelu
		XYSeries data = getGraphData();
		XYSeriesCollection ds = new XYSeriesCollection();
		ds.addSeries(data);
		graph = ChartFactory.createXYLineChart(
				"Graf dostøelu","Poèáteèní rychlost [m/s]", "Dostøel [m]", 
				ds, PlotOrientation.VERTICAL,
				false, true, false);
		XYPlot plot = (XYPlot) graph.getPlot();
        plot.setAxisOffset(new RectangleInsets(0, 0, 0, 0));
        
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        range.setRange(-10.0, 18000.0);
        
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.black);
        plot.setForegroundAlpha(0.9f);
        
        // Panel
        graphPanel = new ChartPanel(graph);
	}
	
	
	public JFrame  makeWindow()
	{
		dependencyGraphFrame = new JFrame();
		
		// Nastavení grafu
		dependencyGraphFrame.setLayout(new BorderLayout());
		dependencyGraphFrame.add(graphPanel, BorderLayout.CENTER);

		
		
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
		
		//controlPane.add(labelAzimuth, c);
		c.gridx = 1; 
		//controlPane.add(textAreaAzimuth, c);
		
		// TextArea elevation + label
		c.gridx = 0;
		c.gridy = 0;
		Label labelElevation = new Label("Elevation: ");
		JTextField textAreaElevation = new JTextField(5);
		textAreaElevation.setPreferredSize(new Dimension(75, textAreaElevation.getPreferredSize().height));
		
		controlPane.add(labelElevation, c);
		c.gridx = 1; 
		controlPane.add(textAreaElevation, c);

		// TextArea velocity + label
		c.gridx = 0;
		c.gridy = 0;
		Label labelVelocity = new Label("Velocity: ");
		JTextField textAreaVelocity = new JTextField(5);
		textAreaVelocity.setPreferredSize(new Dimension(75, textAreaVelocity.getPreferredSize().height));
	
		//controlPane.add(labelVelocity, c);
		c.gridx = 1; 
		//controlPane.add(textAreaVelocity, c);
		
		// Filler
		JLabel filler = new JLabel(" ");
		c.gridx = 0;
		c.gridy = 1;
		
		controlPane.add(filler, c);
		
		// Buttons
		JButton buttonChange = new JButton("Pøekreslit graf");
		buttonChange.setAlignmentX(CENTER_ALIGNMENT);

		
		buttonChange.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  if(textAreaElevation.getText().length() > 0)
			  {
				  try
				  {
					 dependencyGraphFrame.remove(graphPanel);
					 
					 elevation = Double.parseDouble(textAreaElevation.getText());
					 
					 createGraph();
					 
					graphPanel = new ChartPanel(graph);
					 
					 dependencyGraphFrame.add(graphPanel, BorderLayout.CENTER);
					 
					 dependencyGraphFrame.revalidate();
					 dependencyGraphFrame.repaint();

				  }
				  catch(Exception exp)
				  {
					
					  exp.printStackTrace();
				  }
			  }
		  }
		});
		
		
		c.gridy = 2;
		c.fill = 1;
		
		controlPane.add(buttonChange, c);

		// Další filler
		// Filler
		JLabel filler2 = new JLabel(" ");
		c.gridx = 0;
		c.gridy = 3;
		
		controlPane.add(filler2, c);		
		
		// A button
		JButton buttonExit = new JButton("Návrat do menu");
		
		buttonExit.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  Game.dependencyGraphFrame.dispose();
		  }
		});
		
		
		buttonExit.setBackground(Color.red);
		buttonExit.setForeground(Color.WHITE);
		buttonExit.setAlignmentX(CENTER_ALIGNMENT);
		c.gridy = 4;
		c.fill = 1;
		c.anchor = GridBagConstraints.EAST;
		
		controlPane.add(buttonExit, c);
		
		
		dependencyGraphFrame.add(controlPane, BorderLayout.EAST );
		
		
		
		// Zobrazení a interakce
		dependencyGraphFrame.pack();
		dependencyGraphFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dependencyGraphFrame.setLocationRelativeTo(null);
		Dimension d = new Dimension(Constants.preferedWindowWidth, Constants.preferedWindowHeight);
		dependencyGraphFrame.setPreferredSize(d);
		
		dependencyGraphFrame.setTitle("Støelec - Graf dostøelu | J. Vítek | A16B0165P");
		
		return dependencyGraphFrame;
	}
	
	public XYSeries getGraphData() {
		XYSeries data = new XYSeries("Vzdálenost");
		
		double graphVelocity = (velocity*2.0)/100.0;
		double graphElevation = elevation;
		
		// Ošetøení vstupu
		graphVelocity = graphVelocity < 0.1 ? 0.1 : graphVelocity;
		graphElevation = graphElevation < 0.1 ? 0.1 : graphElevation;
		
		// Generování dat
		for(int i = 0; i<100; i++) {
			// Výpoèet dat
			double distance = 0;		
			double velicity = graphVelocity*i;
			double gravity = Game.shootingCalculator.getGravityConstant();
			double kappa = elevation;
			distance = ((velicity*velicity)/gravity)*Math.sin(Math.toRadians(2*kappa));
			
			double range = distance;
			
			// Pøidání dat do grafu
			//data.add(range, i*graphVelocity);
			
			data.add(i*graphVelocity, Math.abs(range));
		}		
		return data;
	}	
}
