package de.dhbw.mannheim.vierpunkt.interfaces;

import java.awt.Toolkit;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.google.gson.Gson;
import com.pusher.client.AuthorizationFailureException;
import com.pusher.client.Authorizer;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PrivateChannel;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import de.dhbw.mannheim.vierpunkt.logic.*;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import de.dhbw.mannheim.vierpunkt.gui.*;

public class PusherInterface extends Application 
{
	/**
	 * App-ID der Pusher Instanz des Clients
	 */
	private static String MyAppID = "255967";
	
	/**
	 * App-Key der Pusher-Instanz des Clients
	 */
	private static String MyAppKey = "61783ef3dd40e1b399b2";
	
	/**
	 * App-Secret der Pusher-Instanz des Clients
	 */
	private static String MyAppSecret = "66b722950915220b298c";
	
	/**
	 * Channel-Name des Kommunikationskanals
	 */
	private static String ChannelName = "private-channel";
	
	
	
	
	/* Code f�r GUI*/
	TestGui gui;
	String[] args;
	
	/****** VARIABLENDEKLARATION *****/
	private int anzahlzeilen;
	private int anzahlspalten;
	private final int l = 70; 		// Seitenlaenge der Grids - spaeter manuelle Einstellung
	public int spieler = 1; 		// Spieler 1
	private double breite = Toolkit.getDefaultToolkit().getScreenSize().width; // Breite des Fensters in Pixeln
	
	// Erzeugen der Spielsteine
    public javafx.scene.image.Image image1 = new javafx.scene.image.Image(getClass().getResource("spielstein_orange.png").toExternalForm());
    public javafx.scene.image.Image image2 = new javafx.scene.image.Image(getClass().getResource("spielstein_gruen.png").toExternalForm());
    public javafx.scene.image.Image image3 = new javafx.scene.image.Image(getClass().getResource("spielstein_grau.png").toExternalForm());
	public javafx.scene.image.Image kuerbis = new javafx.scene.image.Image(getClass().getResource("kuerbis.png").toExternalForm()); 
    public javafx.scene.image.Image fledermaus = new javafx.scene.image.Image(getClass().getResource("fledermaus.png").toExternalForm()); 
    public javafx.scene.image.Image pizza = new javafx.scene.image.Image(getClass().getResource("pizza.png").toExternalForm());
    public javafx.scene.image.Image burger = new javafx.scene.image.Image(getClass().getResource("burger.png").toExternalForm());
    public javafx.scene.image.Image basketball = new javafx.scene.image.Image(getClass().getResource("basketball.png").toExternalForm());
    public javafx.scene.image.Image baseball = new javafx.scene.image.Image(getClass().getResource("baseball.png").toExternalForm());
    public javafx.scene.image.Image orange = new javafx.scene.image.Image(getClass().getResource("spielstein_orange.png").toExternalForm());
    public javafx.scene.image.Image gruen = new javafx.scene.image.Image(getClass().getResource("spielstein_gruen.png").toExternalForm());
    
    // Variable fuer die Farbe des Spielfelds
    public Color color = Color.rgb(133, 3, 118);
    public GridPane spielfeld = new GridPane();
    
    // Setter Methoden
    public void setColor(Color color) {	this.color = color;}
	public void setImage1(javafx.scene.image.Image image1) {this.image1 = image1;}
	public void setImage2(javafx.scene.image.Image image2) {this.image2 = image2;}
	public void setImage3(javafx.scene.image.Image image3) {this.image3 = image3;}
	




	
	public void start(Stage primaryStage) throws Exception 
	{
        /**
         * CurRow gibt den F�llstand der aktuellen Spalte an	
         */
		int[] CurRow = new int[7];
		for (int i = 0; i < CurRow.length; i++){
			CurRow[i]=5;
		}
		
		 GameLogic game = new GameLogic();
		
		// Das Pusher-Objekt wird mit dem App-Key des Testaccounts initialisiert
		PusherOptions options = new PusherOptions();
		//options.setCluster("EU");
		options.setAuthorizer(new Authorizer() {
			
			/**
			 * Nutzt die Client API von Pusher um eine Authentifizierung zu ermoeglichen.
			 * 
			 * Es wird ein Hash ueber die Socket-ID des Clients und den Namen des Channels gebildet,
			 * dieser wird zusammen mit dem gehashten App-Secret des Clients uebertragen.
			 * 
			 * Die Daten werden vom Server mit der Agents.json-Datei verglichen um die Authentifizierung zu ermoeglichen.
			 */
			public String authorize(String channel, String socketID)throws AuthorizationFailureException
			{
				com.pusher.rest.Pusher pusher = new com.pusher.rest.Pusher(MyAppID, MyAppKey, MyAppSecret);
				
				String signature = pusher.authenticate(socketID, channel);
				System.out.println(signature);
				return signature;
			}});

		com.pusher.client.Pusher pusher = new com.pusher.client.Pusher (MyAppKey, options);
		
		/**
		 * Das Pusher Client-Objekt wird mit dem Server verbunden.
		 * 
		 * Die Veraenderung des Verbindungsstatus wird in der Konsole ausgegeben.
		 */
		pusher.connect(new ConnectionEventListener() {
		    @Override
		    public void onConnectionStateChange(ConnectionStateChange change) {
		        System.out.println("Der Verbindungsstatus hat sich von " + change.getPreviousState() +
		                           " zu " + change.getCurrentState() + " geaendert.");
		    }

		    @Override
		    public void onError(String message, String code, Exception e) {
		        System.out.println("Es gab ein Problem beim Verbindungsaufbau.");
		        System.out.println(message);
		        System.out.println("Code:" + code);
		        System.out.println("Exception: " + e);
		    }
		}, ConnectionState.ALL);
	
		// Der Pusher wartet auf dem vorgegebenen Channel
				PrivateChannel channel = pusher.subscribePrivate(ChannelName);

				// Auf das "MoveToAgent"-Event wird reagiert, indem die empfangenen Daten in der Konsole ausgegeben werden
				channel.bind("MoveToAgent", new PrivateChannelEventListener() {
				    public void onEvent(String channel1, String event, String data) {
				    	// Zug des Gegners aus Nachricht von Server erhalten
				        System.out.println("Empfangene Daten: " + data);
				        int zug = getGegnerzug(data);				        
				        System.out.println("Pusher empf�ngt Spalte: "+ zug);
				        
				        // Zug des Gegners wird
				        game.setChip(zug, 1);
				        
				        // Spielstein wird in der GUI eingeworfen
				        setSpielstein(CurRow[zug], zug);
				        CurRow[zug]--;
				        
				       

				        
				        
				        
				        if (data.contains("true")){
				        	// der Move wird von der Logik berechnet
				        	int move = game.playerTurn();
				        	channel.trigger("client-event", "{\"move\": \"" + move + "\"}");
				        	setSpielstein(CurRow[move], move);
				        	CurRow[move]--;
				        }
				        
				        
				    }

					@Override
					public void onSubscriptionSucceeded(String channel)
					{
						System.out.println("Client wurde im Channel private-channel angemeldet");
					}
					
					@Override
					public void onAuthenticationFailure(String msg, Exception e)
					{
						System.out.println("Client konnte nicht im Channel private-channel angemeldet werden");
						System.out.println("Grund: "+ msg);
						System.out.println("Exception: " + e);
					}
				});
				
				// Grundlegende Eigenschaften der Stage
				//primaryStage.setFullScreen(true); 		// automatisches Oeffnen im Fullscreen
				primaryStage.setTitle("VierPunkt");
				primaryStage.setResizable(true);


				// Layout Boxen
				VBox root = new VBox(); 				// aeusserste Box
				root.setId("root_sweets");
				HBox content = new HBox();
				content.setPrefWidth(breite); 			// content ueber gesamte Bildschirmbreite
				content.setAlignment(Pos.TOP_CENTER); 	// alle Inhalte werden mittig ausgerichtet

				/********************************************** MENUBAR ******************************************************/
				// MenuBar Hauptkategorien
				final Menu vierpunkt = new Menu("VierPunkt");
				final Menu themen = new Menu("Themen");
				final Menu hilfe = new Menu("Hilfe");

				// Menubar, Hauptkategorien setzen
				MenuBar menuBar = new MenuBar();
				menuBar.setId("menu");
				menuBar.getMenus().addAll(vierpunkt, themen, hilfe);

				// Unterkategorien fuer "vierpunkt"
				MenuItem menu11 = new MenuItem("neues Spiel");
				MenuItem menu12 = new MenuItem("Spielanleitung");
				MenuItem menu13 = new MenuItem("Spiel beenden");
				MenuItem menu14 = new MenuItem("Impressum");
				vierpunkt.getItems().addAll(menu11, menu12, menu13, menu14);
				
				// Unterkategorien fuer "themen"
				MenuItem menu21 = new MenuItem("Suessigkeiten");
				MenuItem menu22 = new MenuItem("Halloween");
				MenuItem menu23 = new MenuItem("Food");
				MenuItem menu24 = new MenuItem("Sports");
				themen.getItems().addAll(menu21, menu22, menu23, menu24);

				// Unterkategorien fuer "hilfe"
				MenuItem menu31 = new MenuItem("zu Google");
				MenuItem menu32 = new MenuItem("Spielanleitung");
				hilfe.getItems().addAll(menu31, menu32);

				/******************************************* UEBERSCHRIFT *********************************************************/

				// Ansicht der Ueberschrift
				ImageView ueberschrift = new ImageView();
				ueberschrift.setId("ueberschrift_sweets");
				ueberschrift.setFitWidth(breite / 2); // Ueberschrift soll die Haelfte des Bildschirms breit sein
				ueberschrift.setFitHeight((breite / 2) / 3.33); // Aufrechterhalten des Verhaeltnisses (Quotient: 3.33)
				ueberschrift.setPreserveRatio(true);

				// Container, in dem die Ueberschrift platziert wird
				HBox titelbox = new HBox();
				titelbox.setId("titel");

				// Rechtecke als Platzhalter, um die Ueberschrift mittig zu platzieren
				Rectangle containerlinks = new Rectangle(breite / 4, (breite / 2) / 3.33);
				containerlinks.setId("container_weiss");
				Rectangle containerrechts = new Rectangle(breite / 4, (breite / 2) / 3.33);
				containerrechts.setId("container_weiss");

				// Platzhalter und Ueberschrift in den Container einfuegen
				titelbox.getChildren().addAll(containerlinks, ueberschrift, containerrechts);

				/******************************************* CONTAINERBOXEN *********************************************************/

				// Hauptcontainer erhaelt den Inhalt
				root.getChildren().addAll(menuBar, titelbox, content);

				/******************************************* LAYOUT-UEBERSICHT ******************************************************/


				/******************************************** CONTAINERBOXEN IN CONTENT ************************************************/
				// Erzeugen der rechten Containerbox
				VBox boxrechts = new VBox();
				boxrechts.setPrefWidth(breite / 4);
				boxrechts.setSpacing(10);
				boxrechts.setPadding(new Insets(50, 0, 0, 50));

				/******* INHALTE DER RECHTEN CONTAINERBOX *********************/

				Label spielstand = new Label("Spielstand: ");
				Text antwortspielstand = new Text("1 : 1");
				spielstand.setPadding(new Insets(20, 0, 0, 0));

				Label satzstatus = new Label("Satzstatus:");
				Text antwortsatzstatus = new Text("warten auf den Gegner");
				satzstatus.setPadding(new Insets(20, 0, 0, 0));

				Label spielmodi = new Label("Spielmodus:");
				spielmodi.setPadding(new Insets(20, 0, 0, 0));

				// Slider, um einfach den Spielmodus einstellen zu koennen
				Slider spielmodus = new Slider(0, 2, 1); 			// Slider geht von 0 bis 2 in 1er Abstaenden
				spielmodus.setMinorTickCount(0);
				spielmodus.setMajorTickUnit(1); 					// Man kann nur auf den Zahlen 0, 1, 2 landen, nicht dazwischen
				spielmodus.setSnapToTicks(true); 					// Der Punkt rutzscht zur naechsten Zahl
				spielmodus.setShowTickMarks(true); 					// Markierungen anzeigen -
				spielmodus.setShowTickLabels(true); 				// Beschriftungen anzeigen
				spielmodus.setOrientation(Orientation.VERTICAL); 	// Vertikale Anordnung,standardmaessig horizontal
				spielmodus.setPrefHeight(120); 						// bevorzugte Hoehe des Sliders
				spielmodus.setMinHeight(120); 						// Mindesthoehe
				spielmodus.setValue(2);								// Default Value = 2
				

				// Methode, die die Zahlenbeschriftung durch entsprechenden Text ersetzt und die Rueckgabewerte festlegt
				spielmodus.setLabelFormatter(new StringConverter<Double>() {
					@Override
					public String toString(Double n) {
						if (n == 0)return "manuell";
						if (n > 0 && n < 2)return "gegen den Computer";
						if (n == 2)return "automatisch";
						return "automatisch";
					}
					@Override
					public Double fromString(String x) {
						switch (x) {
						case "manuell":return 0d;
						case "gegen den Computer":return 1d;
						case "automatisch":return 2d;
						default:return 2d;
						}
					}
				});
				
				
				spielmodus.valueProperty().addListener(new ChangeListener<Number>() {
				    @Override
				    public void changed(ObservableValue<? extends Number> observable,
				            Number oldValue, Number newValue) {
				    	if(newValue.intValue() == 2){
				    		createGrids_automatisch();
				    	}
				    	if(newValue.intValue() == 0){
				    		createGrids();
				    	}
				    }
				});
				
				
				
				
				// Platzhalter, damit der nachfolgende Button weiter unten angeordnet wird
				Rectangle platzhalter0 = new Rectangle(10, 40);
				platzhalter0.setOpacity(0);

				Button start = new Button("Spiel starten");
				start.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
		            public void handle(MouseEvent arg0) {
						spieler = 1;
		            	createGrids();
		            }
				});

				// Einfuegen der Elemente in die rechte Box
				boxrechts.getChildren().addAll(spielstand, antwortspielstand, satzstatus, antwortsatzstatus, spielmodi, spielmodus, platzhalter0, start);

				// Erzeugen der mittleren Containerbox
				VBox boxmitte = new VBox();
				boxmitte.setId("boxmitte");
				boxmitte.setPrefWidth(7 * l);
				boxmitte.setPadding(new Insets(30, 0, 50, 0));
				boxmitte.setMinWidth(7 * l);

				/******* INHALTE DER MITTLEREN CONTAINERBOX *********************/
				Rectangle platzhalter1 = new Rectangle(7 * l, l);
				platzhalter1.setOpacity(0); // Platzhalter nicht sichtbar
				
				/*********************************************************************************************************************
				 ******************************************* GRID FUER DAS SPIELFELD *********************************************************
				 ********************************************************************************************************************/
				
				// Erzeugen eines GridPanes spielfeld im uebergeordneten GridPane grid
				spielfeld.setId("spielfeld");
				
				
				// Erzeugen der Spalten (7)
				spielfeld.getColumnConstraints().addAll(new ColumnConstraints(l, l, Double.MAX_VALUE),
						new ColumnConstraints(l, l, Double.MAX_VALUE), new ColumnConstraints(l, l, Double.MAX_VALUE),
						new ColumnConstraints(l, l, Double.MAX_VALUE), new ColumnConstraints(l, l, Double.MAX_VALUE),
						new ColumnConstraints(l, l, Double.MAX_VALUE), new ColumnConstraints(l, l, Double.MAX_VALUE));
				// Erzeugen der Zeilen (6)
				spielfeld.getRowConstraints().addAll(new RowConstraints(l, l, Double.MAX_VALUE),
						new RowConstraints(l, l, Double.MAX_VALUE), new RowConstraints(l, l, Double.MAX_VALUE),
						new RowConstraints(l, l, Double.MAX_VALUE), new RowConstraints(l, l, Double.MAX_VALUE),
						new RowConstraints(l, l, Double.MAX_VALUE));
						

				// Einfuegen der Elemente in die mittlere Box
				boxmitte.getChildren().addAll(platzhalter1, spielfeld);

				// Erzeugen der linken Containerbox
				VBox boxlinks = new VBox();
				boxlinks.setId("boxlinks");
				boxlinks.setPrefWidth(breite / 4);
				boxlinks.setAlignment(Pos.BOTTOM_CENTER);

				/******* INHALTE DER LINKEN CONTAINERBOX ************************/
				
				ImageView bild = new ImageView();
				bild.setId("bild_sweets");
				bild.setFitWidth(breite / 4); // Breite soll ein Viertel des Fensters betragen
				bild.setPreserveRatio(true); // Das Verhaeltnis soll beibehalten werden
				
				Rectangle platzhalter2 = new Rectangle(10, 70); // Platzhalter, damit das Bild nicht ganz am Boden sitzt
				platzhalter2.setOpacity(0);

				// Einfuegen der Elemente in die linke Box
				boxlinks.getChildren().addAll(bild, platzhalter2);

				/******* CONTAINERBOXEN EINFUEGEN ************************/
				content.getChildren().addAll(boxlinks, boxmitte, boxrechts);

				/*********************************************************************************************************************
				 ******************************************* EVENTHANDLER FUER DAS MENU *********************************************************
				 ********************************************************************************************************************/
				
				menu13.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) { Platform.exit();}
				});
				
				menu11.setOnAction(new EventHandler<ActionEvent>(){
					@Override public void handle(ActionEvent e) {
						spieler = 1;
						createGrids();
						}
				});
				
				menu21.setOnAction(new EventHandler<ActionEvent>(){
					@Override public void handle(ActionEvent e){
						setColor(Color.PURPLE);
						setImage1(orange);
						setImage2(gruen);
						root.setId("root_sweets");
						containerrechts.setId("container_weiss");
						containerlinks.setId("container_weiss");
						ueberschrift.setId("ueberschrift_sweets");
						bild.setId("bild_sweets");
						createGrids();
					}
				});
				
				menu22.setOnAction(new EventHandler<ActionEvent>(){
					@Override public void handle(ActionEvent e){
						setColor(Color.BLACK);
						setImage1(kuerbis);
						setImage2(fledermaus);
						root.setId("root_halloween");
						containerrechts.setId("container_schwarz");
						containerlinks.setId("container_schwarz");
						ueberschrift.setId("ueberschrift_halloween");
						bild.setId("bild_halloween");
						createGrids();
					}
				});
				
				menu23.setOnAction(new EventHandler<ActionEvent>(){
					@Override public void handle(ActionEvent e){
						setColor(Color.DARKGREEN);
						setImage1(pizza);
						setImage2(burger);
						root.setId("root_food");
						containerrechts.setId("container_weiss");
						containerlinks.setId("container_weiss");
						ueberschrift.setId("ueberschrift_food");
						bild.setId("bild_food");
						createGrids();
					}
				});
				
				menu24.setOnAction(new EventHandler<ActionEvent>(){
					@Override public void handle(ActionEvent e){
						setColor(Color.CADETBLUE);
						setImage1(basketball);
						setImage2(baseball);
						root.setId("root_sport");
						containerrechts.setId("container_weiss");
						containerlinks.setId("container_weiss");
						ueberschrift.setId("ueberschrift_sport");
						bild.setId("bild_sport");
						createGrids();
					}
				});
				
				/******* METHODENAUFRUF ************************/
				// manuell
				if(spielmodus.getValue() == 0 ){
					createGrids();
				}
				
				// automatisch
				if(spielmodus.getValue() == 2){
					createGrids_automatisch();
				}
				
				
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				scene.getStylesheets().add(TestGui.class.getResource("Gui.css").toExternalForm());

				primaryStage.show();
				
			}

			/*********************************************************************************************************************
		     *******************************************  SPIELFELD ERZEUGEN METHODE  ********************************************
		     ********************************************************************************************************************/
		    public void createGrids_automatisch(){
		    	spielfeld.getChildren().clear();
		        for(anzahlzeilen=0;anzahlzeilen<spielfeld.getRowConstraints().size(); anzahlzeilen++){
		            for(anzahlspalten=0; anzahlspalten<spielfeld.getColumnConstraints().size(); anzahlspalten++){
		            
		            // Darstellung des Rahmens/ der Zellen    
		            Rectangle rect = new Rectangle(l,l);
		            Circle circ = new Circle((l/2)-5);
		            circ.centerXProperty().set(l/2);
		            circ.centerYProperty().set(l/2);
		            Shape cell = Path.subtract(rect, circ);
		            cell.setId("cell");
		            cell.setFill(color);
		            cell.setStroke(color.darker());
		            
		            /*******************************************************************************************************************
		             *******************************************  SPIELSTEINE  *********************************************************
		             *******************************************************************************************************************/            
		            
		            // Ansicht der Spielsteine
		            ImageView spielstein = new ImageView(image1);
		            spielstein.setImage(image1);
		            spielstein.setId("spielstein" + anzahlspalten);
		            spielstein.setFitWidth(l-10);
		            spielstein.setPreserveRatio(true);  
		            
		            // Vorschau der Spielsteine
		            ImageView vorschauspielstein = new ImageView(image3);
		            vorschauspielstein.setImage(image1);
		            vorschauspielstein.setFitWidth(l-10);
		            vorschauspielstein.setPreserveRatio(true); 
		            vorschauspielstein.setOpacity(0.5);
		            
		            spielstein.setTranslateY(-(l*(anzahlzeilen+1)));
		            
		            // Zellen werden gefuellt
		            StackPane stack = new StackPane();
		            vorschauspielstein.setImage(image3);                         		// Hintergrund grau
		            stack.getChildren().addAll(cell, vorschauspielstein, spielstein);   // Fuellen der Zelle mit Rahmen, Vorschau oder Spielstein
		            spielfeld.add(stack, anzahlspalten, anzahlzeilen); 
		            }
		         }
		        
		        
		        
		        
		    }
			
			
			
			public void createGrids(){
		    	spielfeld.getChildren().clear();
		        for(anzahlzeilen=0;anzahlzeilen<spielfeld.getRowConstraints().size(); anzahlzeilen++){
		            for(anzahlspalten=0; anzahlspalten<spielfeld.getColumnConstraints().size(); anzahlspalten++){
		            
		            // Darstellung des Rahmens/ der Zellen    
		            Rectangle rect = new Rectangle(l,l);
		            Circle circ = new Circle((l/2)-5);
		            circ.centerXProperty().set(l/2);
		            circ.centerYProperty().set(l/2);
		            Shape cell = Path.subtract(rect, circ);
		            cell.setId("cell");
		            cell.setFill(color);
		            cell.setStroke(color.darker());
		            
		            /*******************************************************************************************************************
		             *******************************************  SPIELSTEINE  *********************************************************
		             *******************************************************************************************************************/            
		            
		            // Ansicht der Spielsteine
		            ImageView spielstein = new ImageView(image1);
		            spielstein.setImage(image1);
		            spielstein.setId("spielstein" + anzahlspalten);
		            spielstein.setFitWidth(l-10);
		            spielstein.setPreserveRatio(true);  
		            
		            // Vorschau der Spielsteine
		            ImageView vorschauspielstein = new ImageView(image3);
		            vorschauspielstein.setImage(image1);
		            vorschauspielstein.setFitWidth(l-10);
		            vorschauspielstein.setPreserveRatio(true); 
		            vorschauspielstein.setOpacity(0.5);
		            
		            /*******************************************************************************************************************
		             *******************************************  ANZEIGE IM SPIELFELD  ************************************************
		             *******************************************************************************************************************/ 
		            
		        	// Methode zur Vorschau - jeweiliger Spielstein wird angezeigt
		               vorschauspielstein.setOnMouseEntered(new EventHandler<MouseEvent>(){
		                   @Override
		                   public void handle(MouseEvent arg0) {
		                       if(spieler==1){vorschauspielstein.setImage(image1);      
		                       }else{vorschauspielstein.setImage(image2);}
		                   }
		               });
		               
		               // Beim Verlassen des Vorschau-Spielsteins erscheint wieder der graue Hintergrund
		                vorschauspielstein.setOnMouseExited(new EventHandler<MouseEvent>(){
		                   @Override
		                   public void handle(MouseEvent arg0) {
		                       vorschauspielstein.setImage(image3);
		                   }
		               });
		                
		               spielstein.setTranslateY(-(l*(anzahlzeilen+1)));
		            
		               // Spielsteine oberhalb des Spielfelds - Anzeige der Vorschau
		               spielstein.setOnMouseEntered(new EventHandler<MouseEvent>(){
		               @Override
		                   public void handle(MouseEvent arg0) {
		                       vorschauspielstein.setImage(image3);
		                       if(spieler==1){ vorschauspielstein.setImage(image1);
		                       }else{vorschauspielstein.setImage(image2); }
		                   }
		               });
		               
		               // Beim Verlassen des Spielsteins erscheint wieder der graue Hintergrund
		              spielstein.setOnMouseExited(new EventHandler<MouseEvent>(){
		                   @Override
		                   public void handle(MouseEvent arg0) {
		                        vorschauspielstein.setImage(image3);
		                   }
		               });
		              
		              // Setzen der Spielsteine
		              spielstein.setOnMouseClicked(new EventHandler<MouseEvent>(){
		                   @Override public void handle(MouseEvent arg0) { spielsteinAnzeigen(spielstein);}
		               });
		              
		               // Setzen der Spielsteine beim Klick auf die entsprechende Vorschau
		               vorschauspielstein.setOnMouseClicked(new EventHandler<MouseEvent>(){
		                   @Override public void handle(MouseEvent arg0) {spielsteinAnzeigen(spielstein);}
		               });
		           
		            /*******************************************************************************************************************
		             *******************************************  ZELLEN FUELLEN  ******************************************************
		             *******************************************************************************************************************/
		            // Zellen werden gefuellt
		            StackPane stack = new StackPane();
		            vorschauspielstein.setImage(image3);                         		// Hintergrund grau
		            stack.getChildren().addAll(cell, vorschauspielstein, spielstein);   // Fuellen der Zelle mit Rahmen, Vorschau oder Spielstein
		            spielfeld.add(stack, anzahlspalten, anzahlzeilen); 
		            }
		        }
		    }
		    public void setSpielstein(int zeile, int spalte){
		    	spielsteinAnzeigen(getImageView((getNodeByRowColumnIndex(zeile, spalte, spielfeld))));
		    }
		    
		    public void spielsteinAnzeigen(ImageView spielstein){
		    	if(spielstein.getTranslateY()!=0){ 
		    		spielstein.setTranslateY(-(l*(anzahlzeilen+1)));
		            final TranslateTransition translateTransition = new TranslateTransition(Duration.millis(300), spielstein);
		            translateTransition.setToY(0);				//Runterfallen der Steine
		            translateTransition.play();
		            if(spieler==1){
		                spielstein.setImage(image1);
		                spieler=2;
		            }else{
		                spielstein.setImage(image2);
		                spieler=1;
		            }
		        }System.out.println((int)spielstein.getId().charAt(10)-48);
		    }
		    
		    public StackPane getNodeByRowColumnIndex (final int row, final int column, GridPane spielfeld) {
		        for (Node node : spielfeld.getChildren()) {
		            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
		                return (StackPane) node;
		            }
		        } return null;
		    }
		    
		    public ImageView getImageView (StackPane stack) {
		        ObservableList<Node> list = stack.getChildren();
		        
		        System.out.println(list.get(2));
		        return (ImageView)list.get(2);
		    }
		    
		    private void AutoSpiel(int zeile, int spalte, int spieler, Button start, Slider spielmodus, Image image1, Image image2, TranslateTransition translateTransition, ImageView spielstein, Shape cell){
		    	 
		    	start.setOnMouseClicked(new EventHandler<MouseEvent>() {
		 			@Override
		             public void handle(MouseEvent arg0) {
		 				for(anzahlzeilen=0;anzahlzeilen<spielfeld.getRowConstraints().size(); anzahlzeilen++){
		 		            for(anzahlspalten=0; anzahlspalten<spielfeld.getColumnConstraints().size(); anzahlspalten++){
		 		            	createGrids();
		 		            }
		 				}
		 			}
		 		});
		         
		        if(spielstein.getTranslateY()!=0){              //Runterfallen der Steine
		            translateTransition.setToY(0);
		            translateTransition.play();
		            if(spieler==1){
		                spielstein.setImage(image1);
		                spieler=2;
		            }else{
		                spielstein.setImage(image2);
		                spieler=1;
		            }
		        } 
		            
		         System.out.println(spielstein.getId().charAt(10));
		       
			     // Zellen werden gefuellt
			     StackPane stack = new StackPane();
			     stack.getChildren().addAll(cell, spielstein);    // Fuellen der Zelle mit Rahmen, Vorschau oder Spielstein
			     spielfeld.add(stack, anzahlspalten, anzahlzeilen); 
		     
		while (true){}
	}
	
	/**
	 * Das Pusher-Objekt baut eine Verbindung zu dem privaten Kommunikationskanal auf.
	 * 
	 * In dieser Methode wird sowohl der Name des Channels, als auch der Eventtyp benoetigt,
	 * auf den reagiert werden soll.
	 * @param pusher
	 */
	public static void verbindungsaufbau_Kanal(Pusher pusher){
		// Der Pusher wartet auf dem vorgegebenen Channel
		Channel channel = pusher.subscribePrivate(ChannelName);

		// Auf das "MoveToAgent"-Event wird reagiert, indem die empfangenen Daten in der Konsole ausgegeben werden
		channel.bind("MoveToAgent", new PrivateChannelEventListener() {
		    public void onEvent(String channel, String event, String data) {
		        System.out.println("Empfangene Daten: " + data);
		    }

			@Override
			public void onSubscriptionSucceeded(String channel)
			{
				System.out.println("Client wurde im Channel private-channel angemeldet");
			}
			
			@Override
			public void onAuthenticationFailure(String msg, Exception e)
			{
				System.out.println("Client konnte nicht im Channel private-channel angemeldet werden");
				System.out.println("Grund: "+ msg);
				System.out.println("Exception " + e);
			}
		});
		
	}

	public static int getGegnerzug (String data){
		
		int zug;
		int stelle = ordinalIndexOf(data, "#", 1);
		
		try{
		zug = Integer.parseInt(String.valueOf(data.charAt(stelle+2)));
		} catch (Exception e) {zug = -1;}
		
		return zug;
		}
	
	
	public static int ordinalIndexOf(String str, String s, int n) {
	    int pos = str.indexOf(s, 0);
	    while (n-- > 0 && pos != -1)
	        pos = str.indexOf(s, pos+1);
	    return pos;
	}
	
	public static ServerEvent entpacken(String data){
		Gson gson = new Gson();
		ServerEvent se = gson.fromJson(data, ServerEvent.class);
		return se;
	}
	
	public static void main(String[] args) throws InterruptedException, InstantiationException, IllegalAccessException {
		launch(args);
	}
}

class ServerEvent {
	public static boolean freigabe = true;
	public static String status = "Satz spielen";
	public static int spalte = 0;
	public static String sieger = null;
	
	public static int getGegnerzugfromJSON(){
		return spalte;
	}

}

/*
/**
 * Die zur Authentifizierung noetigen Daten werden in dem von Pusher benoetigten Format aufbereitet.
 * 
 * Hierzu werden das App-Secret, sowie der Channel und die Socket-ID in einem definierten Vorgang gehasht.
 * @param MyAppSecret
 * @param AuthString
 * @return Key
 * @throws NoSuchAlgorithmException
 * @throws InvalidKeyException
 
public static String getHash (String MyAppSecret, String AuthString) throws NoSuchAlgorithmException, InvalidKeyException{
	Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
	
	SecretKeySpec secretKey = new SecretKeySpec(MyAppSecret.getBytes(), "HmacSHA256");
	sha256_HMAC.init(secretKey);
	byte[] hash = sha256_HMAC.doFinal(AuthString.getBytes());
	// wenn es nicht funktioniert, Methode zur Konvertierung in lesbare Zeichen einfuegen
	String check = Hex.encodeHexString(hash);
	
	return new String(check);
}
*/

