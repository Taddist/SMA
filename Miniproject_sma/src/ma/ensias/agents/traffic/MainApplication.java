package ma.ensias.agents.traffic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ma.ensias.agents.env.JadeContainer;
import ma.ensias.agents.env.MainContainer;
import ma.ensias.classes.RoadPoint;

public class MainApplication extends Application {
	
	ContainerController mainContainer,container ;
	RoadPoint roadPoint = new RoadPoint();
	
	public StackPane stackPane = new StackPane();
	
	
	public static final double calmDriverCost = 1;
	public static final double crazyDriverCost = 200;
	
	public double calmDriverNumber=7;
	public double crazyDriverNumber=3;
	
	public double vehicleAgentNumber=crazyDriverNumber+calmDriverNumber;

	int j=0;
	public ArrayList<Shape> agentShapeList;
	int MAX_FEU=roadPoint.getSignalLocList().size();
	int k=0;
	
	AgentController vehicleAgent,signalTrafficAgent;
	ArrayList<AgentController> AllAgents = new ArrayList<AgentController>();
	ArrayList<AgentController> WaitingAgents = new ArrayList<AgentController>();
	public static void main(String[] args) {
		launch(MainApplication.class);
	}



	public List<String> getLocalAgents(int xmin, int xmax, int ymin, int ymax) {

		List<String> localAgentList = new ArrayList<String>();
		for (Node node : stackPane.getChildren()) {
			if (node.getTranslateX() > xmin && node.getTranslateX() < xmax && node.getTranslateY() > ymin
					&& node.getTranslateY() < ymax) {
				if (node.getUserData() != null) {
					localAgentList.add(node.getUserData().toString());
					localAgentList.add(node.toString());
				}
			}
		}
		return localAgentList;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		 mainContainer = new MainContainer().getContainer(); // démarre main-container
		 //startcontainer();
		 container = new JadeContainer().getContainer(); //démarre un container
		 
		 for (int i=1;i<6;i++) {
				AgentController roadAgent;
				roadAgent = container.createNewAgent("roadAgent"+i, "ma.ensias.agents.traffic.RoadAgent", new Object[] {this});
				roadAgent.start();

			}
		agentShapeList = new ArrayList<Shape>();
		 
		
		Image image = new Image("/image/city.jpg");
		ImageView imageView = new ImageView(image);

		stackPane.getChildren().add(imageView);
		imageView.toBack();
		
		BorderPane root = new BorderPane();
		MenuBar menuBar = new MenuBar();
	    menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
	    root.setTop(menuBar);

	    // File menu - new, save, exit
	    Menu AddMenu = new Menu("Ajouter");
	    MenuItem calmeMenuItem = new MenuItem("Conducteur calme");
	    MenuItem fouMenuItem = new MenuItem("Conducteur fou");
	    MenuItem feuMenuItem = new MenuItem("Feu de circulation");
	 
	    AddMenu.getItems().addAll(calmeMenuItem, fouMenuItem,feuMenuItem);

	    Menu ActionMenu = new Menu("actions");
	    CheckMenuItem startMenuItem = new CheckMenuItem("start");
	    startMenuItem.setSelected(false);
	    ActionMenu.getItems().add(startMenuItem);

	    CheckMenuItem pauseMenuItem = new CheckMenuItem("Pause");
	    pauseMenuItem.setSelected(false);
	    ActionMenu.getItems().add(pauseMenuItem);
	    
	    CheckMenuItem stopMenuItem = new CheckMenuItem("stop");
	    stopMenuItem.setSelected(false);
	    ActionMenu.getItems().add(stopMenuItem);

	    Menu fileMenu = new Menu("Fichier");
	    MenuItem exitMenuItem = new MenuItem("Sortir");
	    fileMenu.getItems().add(exitMenuItem);
	    	
	    // Set Accelerator for Exit MenuItem.
	    exitMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
	     
	    // When user click on the Exit item.
	    exitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
	     
	        @Override
	        public void handle(ActionEvent event) {
	            System.exit(0);
	        }
	    });
	    
	    MainApplication v = this;
	    calmeMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
	    // When user click on the Exit item.
	    calmeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
	     
	        @Override
	        public void handle(ActionEvent event) {
	        	System.out.println("Ajout conducteur calme");
				try {
					vehicleAgent = container.createNewAgent("vehicleAgent"+j , "ma.ensias.agents.traffic.VehicleAgent", new Object[] { v ,calmDriverCost});
					j++;
					WaitingAgents.add(vehicleAgent);
					
				} catch (StaleProxyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	        }
	    });
	    
	    
	    
	    fouMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+F"));
	    // When user click on the Exit item.
	    fouMenuItem.setOnAction(new EventHandler<ActionEvent>() {
	     
	        @Override
	        public void handle(ActionEvent event) {
	        	System.out.println("Ajout conducteur fou");
				try {
					vehicleAgent = container.createNewAgent("vehicleAgent"+j , "ma.ensias.agents.traffic.VehicleAgent", new Object[] { v ,crazyDriverCost});
					j++;
					WaitingAgents.add(vehicleAgent);
				} catch (StaleProxyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	        }
	    });
	    
	    feuMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
	    // When user click on the Exit item.
	    feuMenuItem.setOnAction(new EventHandler<ActionEvent>() {
	     
	        @Override
	        public void handle(ActionEvent event) {
	        	System.out.println("Ajout feu de circulation");
	        	if(k<MAX_FEU) {
	        		
	        		RoadPoint.SignalLoc signalInfo = roadPoint.getSignalLocList().get(k);
		        	
					try {
						signalTrafficAgent = container.createNewAgent("signalTrafficAgent" + k, "ma.ensias.agents.traffic.SignalTrafficAgent",
								new Object[] { v, signalInfo });
						k++;
						WaitingAgents.add(signalTrafficAgent);
					} catch (StaleProxyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	        		
	        	}
				
	        }
	    });
	    
	    
	    startMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
	    startMenuItem.setOnAction(new EventHandler<ActionEvent>() {
	     
	        @Override
	        public void handle(ActionEvent event) {
	        	
	        	if(startMenuItem.isSelected()) {
	    	        pauseMenuItem.setSelected(false);
	    	        stopMenuItem.setSelected(false);
	    	        	for (AgentController ag: WaitingAgents) {
	    	        		try {
								ag.start();
							} catch (StaleProxyException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	    	        }
	    	        	AllAgents.addAll(WaitingAgents);
	    	        	WaitingAgents.clear(); 
	        }
	        	
	        	else {
	        		pauseMenuItem.setSelected(true);
	    	        stopMenuItem.setSelected(false);
	    	        for (AgentController ag: AllAgents) {
    	        		try {
							ag.suspend();
						} catch (StaleProxyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    	        }
	        		
	        	}
	        	
	        	
	    }});
	    
	    pauseMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
	    pauseMenuItem.setOnAction(new EventHandler<ActionEvent>() {
	     
	        @Override
	        public void handle(ActionEvent event) {
	        	
	        	if(pauseMenuItem.isSelected()) {
	    	        startMenuItem.setSelected(false);
	    	        stopMenuItem.setSelected(false);
	    	        	for (AgentController ag: AllAgents) {
	    	        		try {
								ag.suspend();
							} catch (StaleProxyException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	    	        }
 
	        }
	        	else {
	        		
		    	        startMenuItem.setSelected(true);
		    	        stopMenuItem.setSelected(false);
		    	        	for (AgentController ag: AllAgents) {
		    	        		try {
									ag.activate();
								} catch (StaleProxyException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		    	        }
	 
		        
	        	}
	    }});
	    
	    stopMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
	    stopMenuItem.setOnAction(new EventHandler<ActionEvent>() {
	     
	        @Override
	        public void handle(ActionEvent event) {
	        	
	        	if(stopMenuItem.isSelected()) {
	    	        startMenuItem.setSelected(false);
	    	        pauseMenuItem.setSelected(false);
	    	        	for (AgentController ag: AllAgents) {
	    	        		try {
								ag.kill();
							} catch (StaleProxyException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	    	        }
 
	        }
	        	
	    }});
	    
	    
	    menuBar.getMenus().addAll(AddMenu, ActionMenu, fileMenu);
	    
		VBox vbox = new VBox();
		vbox.getChildren().add(root);
		vbox.getChildren().add(stackPane);
		Scene scene = new Scene(vbox);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Simulation de trafic sur un réseau routier simple");
		
		

		primaryStage.setResizable(false);

		primaryStage.sizeToScene();

		primaryStage.show();
		
		
	    

	    

	   
	}
}
