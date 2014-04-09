package gui;
import game.TextOutput;
import gui.PlayerDisplayInfo.PlayerType;
import gui.IconProvider.IconType;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.DimensionUIResource;

import state.Point;


public class MainWindow extends JFrame {
	GUI gui;
	
	MapPanel guiMap;
	PlayerDisplay detectiveDisplay;
	PlayerDisplay mrXDisplay;
	JButton initialiseButton;
	
	private final static int SIDE_BAR_WIDTH = 280; 
	
	
	
	/**
	 * Constructor for the main window
	 * @param parent
	 * @param mapFilename
	 */
	public MainWindow(GUI parent, String mapFilename)
	{
		gui = parent;
		// load the map
		guiMap = new MapPanel(mapFilename);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	public void setPossibleMoveMarker(Point location, Color color)
	{
		guiMap.setHoverMarker(color, location, 155);
		guiMap.revalidate();
		guiMap.repaint();
		//guiMap.removeHoverMarker();
	}
	
	
	/**
	 * Setup function for the main parts of the gui
	 */
	public void setUp()
	{
		// set up the main layout
		Container frame = this.getContentPane();
		
		frame.add(guiMap, BorderLayout.CENTER);
		frame.add(basicPlayerBox(), BorderLayout.LINE_START);
		frame.add(buttonBox(), BorderLayout.SOUTH);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Scotland Yard");
		pack();
		setLocationByPlatform(true);
		
	}
	
	public void setUpInitialiseCallback()
	{
		initialiseButton.addMouseListener(new InitialiseGameListener(gui));
		initialiseButton.setEnabled(true);
	}
	
	
	public void setUpMapCallbacks()
	{
		guiMap.addMouseMotionListener(new MapEventListener(gui));
		guiMap.addMouseListener(new MapEventListener(gui));
	}
	
	private JPanel basicPlayerBox()
	{
		
		JPanel container = new JPanel();
		container.setBackground(new Color(100,100,100));
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));	
		container.add(createSeperator(3));
		container.add(createTitle("Detectives", IconType.Detective));
		//container.add(createSeperator(3));
		
		
		
		container.setPreferredSize(new Dimension(SIDE_BAR_WIDTH, (int) guiMap.getMapDimensions().getHeight()));
		
		detectiveDisplay = new PlayerDisplay();
		detectiveDisplay.setMaximumSize(new Dimension(SIDE_BAR_WIDTH, 500));
		detectiveDisplay.setUp();
		
		container.add(detectiveDisplay);
		container.add(createSeperator(3));
		container.add(createTitle("Mr X", IconType.Criminal));
		//container.add(createSeperator(3));
		
		mrXDisplay = new PlayerDisplay();
		mrXDisplay.setPreferredSize(new Dimension(SIDE_BAR_WIDTH, 300));
		mrXDisplay.setUp();
		
		container.add(mrXDisplay);
		return container;
	}
	
	private JPanel createSeperator(int height)
	{
		JPanel sep = new JPanel();
		sep.setMaximumSize(new Dimension(SIDE_BAR_WIDTH, height));
		//sep.setBackground(new Color(220,220,220));
		return sep;
	}

	
	private Box createTitle(String text, IconType type)
	{
		// create the container
		Box titleContainer = Box.createHorizontalBox();
		titleContainer.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		titleContainer.setPreferredSize(new Dimension(SIDE_BAR_WIDTH, 50));
		titleContainer.setBackground(new Color(100,100,100));
		
		// create the label
		JLabel detectiveTitle = new JLabel("<html><font size=6 family='Monaco'><strong>"+text+"</strong></font></html>");
		detectiveTitle.setAlignmentX(JLabel.LEFT);
		detectiveTitle.setIcon(IconProvider.getIcon(type, 40, 40));
		detectiveTitle.setForeground(new Color(230, 230, 230));
		
		
		titleContainer.add(detectiveTitle);
		titleContainer.add(Box.createHorizontalGlue());
		
		
		return titleContainer;
	}
	
	private JPanel buttonBox()
	{
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,4));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		buttonPanel.add(Box.createHorizontalGlue());
			
		initialiseButton = new JButton("New Game");
		initialiseButton.setEnabled(false);
		
		
		JButton loadGameButton = new JButton("Load Game");
		JButton saveGameButton = new JButton("Save Game");
		
		buttonPanel.add(initialiseButton);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(loadGameButton);
		buttonPanel.add(saveGameButton);
		
		return buttonPanel;
		
	}
	
	public void setPlayers(List<PlayerDisplayInfo> detectives, List<PlayerDisplayInfo> mrXs)
	{
		for(PlayerDisplayInfo det : detectives)
		{
			
			detectiveDisplay.addPlayer(det);
			guiMap.addPlayer(det);
		}
		
		for(PlayerDisplayInfo x : mrXs)
		{
			mrXDisplay.addPlayer(x);
			guiMap.addPlayer(x);
		}
		
		//detectiveDisplay.redo();
		//mrXDisplay.redo();
	}
	
	public void updatePlayerVisualisation(PlayerDisplayInfo player)
	{
		updatePlayerPanel(player);
		updateMapDisplay(player);
		revalidate();
		repaint();
	}
	
	public void updateMapDisplay(PlayerDisplayInfo player)
	{
		guiMap.revalidate();
		guiMap.repaint();
	}
	
	public void updatePlayerPanel(PlayerDisplayInfo player)
	{
		mrXDisplay.update();
		detectiveDisplay.update();

		
		/*
		if(!display.hasPlayer(player.getId()))
		{
			display.addPlayer(player);
		}
		else
		{
			
			display.updatePlayer(player);
		}
		*/
		
		
		
		//detectiveDisplay.redo();
		//mrXDisplay.redo();
		
	}
	
	
	public void clearPlayers()
	{
		detectiveDisplay.clear();
		mrXDisplay.clear();
		guiMap.clearMarkers();
		guiMap.removeHoverMarker();
	}
	

	
}
