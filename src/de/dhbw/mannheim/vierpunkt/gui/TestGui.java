package de.dhbw.mannheim.vierpunkt.gui;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.util.Duration;
import javafx.util.StringConverter;
import java.awt.Toolkit;
/**
 *
 * @author janaschaub
 */
public class TestGui extends Application {

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
    public javafx.scene.image.Image backgroundvariable = new javafx.scene.image.Image(getClass().getResource("Sweets_transparent.png").toExternalForm());
	public javafx.scene.image.Image kuerbis = new javafx.scene.image.Image(getClass().getResource("kuerbis.png").toExternalForm()); 
    public javafx.scene.image.Image fledermaus = new javafx.scene.image.Image(getClass().getResource("fledermaus.png").toExternalForm()); 
    public javafx.scene.image.Image pizza = new javafx.scene.image.Image(getClass().getResource("pizza.png").toExternalForm());
    public javafx.scene.image.Image burger = new javafx.scene.image.Image(getClass().getResource("burger.png").toExternalForm());
    public javafx.scene.image.Image basketball = new javafx.scene.image.Image(getClass().getResource("basketball.png").toExternalForm());
    public javafx.scene.image.Image baseball = new javafx.scene.image.Image(getClass().getResource("baseball.png").toExternalForm());
    public javafx.scene.image.Image orange = new javafx.scene.image.Image(getClass().getResource("spielstein_orange.png").toExternalForm());
    public javafx.scene.image.Image gruen = new javafx.scene.image.Image(getClass().getResource("spielstein_gruen.png").toExternalForm());
    public javafx.scene.image.Image hexe = new javafx.scene.image.Image(getClass().getResource("hexe.png").toExternalForm());
    public javafx.scene.image.Image pokal = new javafx.scene.image.Image(getClass().getResource("pokal.png").toExternalForm());
    public javafx.scene.image.Image teller = new javafx.scene.image.Image(getClass().getResource("teller.png").toExternalForm());
    public javafx.scene.image.Image food = new javafx.scene.image.Image(getClass().getResource("hintergrund_food.png").toExternalForm());
    public javafx.scene.image.Image halloween = new javafx.scene.image.Image(getClass().getResource("hintergrund_halloween.png").toExternalForm());
    public javafx.scene.image.Image sport = new javafx.scene.image.Image(getClass().getResource("hintergrund_sport.png").toExternalForm());
    public javafx.scene.image.Image sweets = new javafx.scene.image.Image(getClass().getResource("Sweets_transparent.png").toExternalForm());
    
   // public javafx.scene.image.Image test = new javafx.scene.image.Image("hintergrund_halloween.png", breite, Toolkit.getDefaultToolkit().getScreenSize().height, true,true);
    
    public Color color = Color.rgb(133, 3, 118);
    
    public void setBackgroundvariable(javafx.scene.image.Image backgroundvariable) {
		this.backgroundvariable = backgroundvariable;}
    
    public void setColor(Color color) {	this.color = color;}

	public void setImage1(javafx.scene.image.Image image1) {this.image1 = image1;}

	public void setImage2(javafx.scene.image.Image image2) {this.image2 = image2;}

	public void setImage3(javafx.scene.image.Image image3) {this.image3 = image3;}
	
	public BackgroundImage backgroundImage = new BackgroundImage(backgroundvariable, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
	public Background background = new Background(backgroundImage);
	
	

	
	
	public static void main(String[] args) { launch(args);}

	/*********************************************************************************************************************
	 ******************************************* START METHODE *********************************************************
	 ********************************************************************************************************************/
	@Override
	public void start(Stage primaryStage) {

		// Grundlegende Eigenschaften der Stage
		primaryStage.setFullScreen(true); 		// automatisches Oeffnen im Fullscreen
		primaryStage.setTitle("VierPunkt");
		primaryStage.setResizable(true);
		
		

		// Layout Boxen
		VBox root = new VBox(); 				// aeusserste Box
		root.setBackground(background);
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

		// Bild der Ueberschrift einfuegen
		javafx.scene.image.Image vier_gestreift = new javafx.scene.image.Image(
				getClass().getResource("ueberschrift.jpg").toExternalForm());

		// Ansicht der Ueberschrift
		ImageView ueberschrift = new ImageView(vier_gestreift);
		ueberschrift.setId("ueberschrift");
		ueberschrift.setImage(vier_gestreift);
		ueberschrift.setFitWidth(breite / 2); // Ueberschrift soll die Haelfte
												// des Bildschirms breit sein
		ueberschrift.setFitHeight((breite / 2) / 3.33); // Aufrechterhalten des
														// Verhaeltnisses
														// (Quotient: 3.33)
		ueberschrift.setPreserveRatio(true);

		// Container, in dem die Ueberschrift platziert wird
		HBox titelbox = new HBox();
		titelbox.setId("titel");

		// Rechtecke als Platzhalter, um die Ueberschrift mittig zu platzieren
		Rectangle containerlinks = new Rectangle(breite / 4, (breite / 2) / 3.33);
		containerlinks.setId("containerklein");

		Rectangle containerrechts = new Rectangle(breite / 4, (breite / 2) / 3.33);
		containerrechts.setId("containerklein");

		// Platzhalter und Ueberschrift in den Container einfuegen
		titelbox.getChildren().addAll(containerlinks, ueberschrift, containerrechts);

		/******************************************* CONTAINERBOXEN *********************************************************/

		// Hauptcontainer erhaelt den Inhalt
		root.getChildren().addAll(menuBar, titelbox, content);

		/******************************************* LAYOUT-UEBERSICHT ******************************************************/

		/**
		 * VBox "root"
		 * ____________________________________________________________________________________________________________________
		 * | | | MenuBar "menuBar" | |
		 * ____________________________________________________________________________________________________________
		 * | | | | | | | Menu "vierpunkt" Menu "themen" Menu "hilfe" | | |
		 * |___________________________________________________________________________________________________________|
		 * | | | | HBox "titelbox" | |
		 * ____________________________________________________________________________________________________________
		 * | | | | | | | Rectangle "containerlinks" Imageview "ueberschrift"
		 * Rectangle "containerrechts" | | |
		 * |___________________________________________________________________________________________________________|
		 * | | | | HBox "content" | |
		 * ____________________________________________________________________________________________________________
		 * | | | | | | | VBox "boxlinks" VBox "boxmitte" VBox "boxrechts" | | |
		 * | ____________________________ ____________________________
		 * ____________________________________ | | | | | | | | | | | | | | | |
		 * | Rectangle "platzhalter1"| | Label "spielstand" | | | | | | | | | |
		 * Text "antwortspielstand" | | | | | | | | Gridpane "spielfeld" | |
		 * Label "satzstatus" | | | | | | | | | | Text "antwortsatzstatus" | | |
		 * | | | Imageview "cupcake" | | | | Label "spielmodi" | | | | | | | | |
		 * | Slider "spielmodus" | | | | | | Rectangle "platzhalter2"| | | |
		 * Rectangle "platzhalter0" | | | | | | | | | | Button "start" | | | | |
		 * |___________________________| |___________________________|
		 * |___________________________________| | | |
		 * |___________________________________________________________________________________________________________|
		 * |
		 * _____________________________________________________________________________________________________________________|
		 **/

		/*******************************************
		 * CONTAINERBOXEN IN CONTENT
		 ************************************************/
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
		Slider spielmodus = new Slider(0, 2, 1); // Slider geht von 0 bis 2 in
													// 1er Abstaenden
		spielmodus.setMinorTickCount(0);
		spielmodus.setMajorTickUnit(1); // Man kann nur auf den Zahlen 0, 1, 2
										// landen, nicht dazwischen
		spielmodus.setSnapToTicks(true); // Der Punkt rutzscht zur naechsten
											// Zahl
		spielmodus.setShowTickMarks(true); // Markierungen anzeigen -
		spielmodus.setShowTickLabels(true); // Beschriftungen anzeigen
		spielmodus.setOrientation(Orientation.VERTICAL); // Vertikale Anordnung,
															// standardmaessig
															// horizontal
		spielmodus.setPrefHeight(120); // bevorzugte Hoehe des Sliders
		spielmodus.setMinHeight(120); // Mindesthoehe
		
		spielmodus.setValue(2);
		

		// Methode, die die Zahlenbeschriftung durch entsprechenden Text ersetzt
		// und die Rueckgabewerte festlegt
		spielmodus.setLabelFormatter(new StringConverter<Double>() {
			@Override
			public String toString(Double n) {
				if (n == 0)
					return "manuell";
				if (n > 0 && n < 2)
					return "gegen den Computer";
				if (n == 2)
					return "automatisch";
				return "automatisch";
			}

			@Override
			public Double fromString(String x) {
				switch (x) {
				case "manuell":
					return 0d;
				case "gegen den Computer":
					return 1d;
				case "automatisch":
					return 2d;
				default:
					return 2d;
				}
			}
		});

		// Platzhalter, damit der nachfolgende Button weiter unten angeordnet
		// wird
		Rectangle platzhalter0 = new Rectangle(10, 40);
		platzhalter0.setOpacity(0);

		Button start = new Button("Spiel starten");
		

		// Einfuegen der Elemente in die rechte Box
		boxrechts.getChildren().addAll(spielstand, antwortspielstand, satzstatus, antwortsatzstatus, spielmodi,
				spielmodus, platzhalter0, start);

		// Erzeugen der mittleren Containerbox
		VBox boxmitte = new VBox();
		boxmitte.setId("boxmitte");
		boxmitte.setPrefWidth(7 * l);
		boxmitte.setPadding(new Insets(30, 0, 50, 0));
		boxmitte.setMinWidth(7 * l);

		/******* INHALTE DER MITTLEREN CONTAINERBOX *********************/
		Rectangle platzhalter1 = new Rectangle(7 * l, l);
		platzhalter1.setOpacity(0); // Platzhalter nicht sichtbar

		// Einfuegen der Elemente in die mittlere Box
		boxmitte.getChildren().add(platzhalter1);

		// Erzeugen der linken Containerbox
		VBox boxlinks = new VBox();
		boxlinks.setId("boxlinks");
		boxlinks.setPrefWidth(breite / 4);
		boxlinks.setAlignment(Pos.BOTTOM_CENTER);

		/******* INHALTE DER LINKEN CONTAINERBOX ************************/
		javafx.scene.image.Image suessigkeiten = new javafx.scene.image.Image(
				getClass().getResource("suessigkeiten.png").toExternalForm());
		ImageView cupcake = new ImageView(suessigkeiten);

		cupcake.setFitWidth(breite / 4); // Breite soll ein Viertel des Fensters
											// betragen
		cupcake.setPreserveRatio(true); // Das Verhaeltnis soll beibehalten
										// werden

		Rectangle platzhalter2 = new Rectangle(10, 70); // Platzhalter, damit
														// das Bild nicht ganz
														// am Boden sitzt
		platzhalter2.setOpacity(0);

		// Einfuegen der Elemente in die linke Box
		boxlinks.getChildren().addAll(cupcake, platzhalter2);

		/*********************************************************************************************************************
		 ******************************************* GRID FUER DAS SPIELFELD
		 * *********************************************************
		 ********************************************************************************************************************/

		// Erzeugen eines GridPanes spielfeld im uebergeordneten GridPane grid
		GridPane spielfeld = new GridPane();
		spielfeld.setId("spielfeld");
		boxmitte.getChildren().add(spielfeld);

		content.getChildren().addAll(boxlinks, boxmitte, boxrechts);

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
		
		menu13.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) { Platform.exit();}
		});
		
		menu11.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e) {
				spieler = 1;
				createGrids(spielfeld, spielmodus, start);
				}
		});
		
		menu21.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				setColor(Color.PURPLE);
				setImage1(orange);
				setImage2(gruen);
				setBackgroundvariable(sweets);
				
				backgroundImage = new BackgroundImage(backgroundvariable, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
				background = new Background(backgroundImage);
				root.setBackground(background);
				createGrids(spielfeld, spielmodus, start);
			}
		});
		
		menu22.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				setColor(Color.BLACK);
				setImage1(kuerbis);
				setImage2(fledermaus);
				
				//root.setStyle("-fx-background-image: url('hintergrund_halloween.png'); -fx-background-position: center; -fx-background-size: contain;");
				setBackgroundvariable(halloween);
				backgroundImage = new BackgroundImage(backgroundvariable, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
				background = new Background(backgroundImage);
				root.setBackground(background);
				createGrids(spielfeld, spielmodus, start);
			}
		});
		
		menu23.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				setColor(Color.BLACK);
				setImage1(pizza);
				setImage2(burger);
				setBackgroundvariable(food);
				backgroundImage = new BackgroundImage(backgroundvariable, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
				background = new Background(backgroundImage);
				root.setBackground(background);
				createGrids(spielfeld, spielmodus, start);
			}
		});
		
		menu24.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				setColor(Color.CADETBLUE);
				setImage1(basketball);
				setImage2(baseball);
				setBackgroundvariable(sport);
				backgroundImage = new BackgroundImage(backgroundvariable, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
				background = new Background(backgroundImage);
				root.setBackground(background);
				createGrids(spielfeld, spielmodus, start);
			}
		});
		
		
		
		createGrids(spielfeld, spielmodus, start); // Methodenaufruf
		
		

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		scene.getStylesheets().add(TestGui.class.getResource("Gui.css").toExternalForm());

		
		primaryStage.show();
	}

	/*********************************************************************************************************************
     *******************************************  SPIELFELD ERZEUGEN METHODE  ********************************************
     ********************************************************************************************************************/
    public void createGrids(final GridPane spielfeld, Slider spielmodus, Button start){
    	
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
            
            start.setOnMouseClicked(new EventHandler<MouseEvent>() {
    			@Override
                public void handle(MouseEvent arg0) {
					spieler = 1;
	            	createGrids(spielfeld, spielmodus, start);
	            }
    		});
            
            /*******************************************************************************************************************
             *******************************************  ANZEIGE IM SPIELFELD  ************************************************
             *******************************************************************************************************************/ 
          // if(spielmodus.getValue() == 2){}
            //if(spielmodus.getValue() == 0){
            
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
                
               // Lässt den Stein herunterfallen (Weg und Zeit)
                spielstein.setTranslateY(-(l*(anzahlzeilen+1)));
               final TranslateTransition translateTransition = new TranslateTransition(Duration.millis(300), spielstein);
            
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
                   @Override
                   public void handle(MouseEvent arg0) {
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
                   }
               });
              
               // Setzen der Spielsteine beim Klick auf die entsprechende Vorschau
               vorschauspielstein.setOnMouseClicked(new EventHandler<MouseEvent>(){
                   @Override
                   public void handle(MouseEvent arg0) {
                       if(spielstein.getTranslateY()!=0){
                           translateTransition.setToY(0);
                           translateTransition.play();
                           if(spieler==1){
                               spieler=2;
                               spielstein.setImage(image1);
                           }else{
                               spieler=1;
                               spielstein.setImage(image2);
                           }
                           int spalte = (int)spielstein.getId().charAt(10) - 48;
                           System.out.println(spalte);
                           
                       }
                   }
               });
           //}
            
            /*******************************************************************************************************************
             *******************************************  ZELLEN FUELLEN  ******************************************************
             *******************************************************************************************************************/
            // Zellen werden gefuellt
            StackPane stack = new StackPane();
            vorschauspielstein.setImage(image3);                         // Hintergrund grau
            stack.getChildren().addAll(cell, vorschauspielstein, spielstein);    // Fuellen der Zelle mit Rahmen, Vorschau oder Spielstein
            spielfeld.add(stack, anzahlspalten, anzahlzeilen); 
            }
        }
    }
    
    private void AutoSpiel(final GridPane spielfeld, int zeile, int spalte, int spieler, Button start, Slider spielmodus, Image image1, Image image2, TranslateTransition translateTransition, ImageView spielstein, Shape cell){
    	 
             start.setOnMouseClicked(new EventHandler<MouseEvent>() {
     			@Override
                 public void handle(MouseEvent arg0) {
     				for(anzahlzeilen=0;anzahlzeilen<spielfeld.getRowConstraints().size(); anzahlzeilen++){
     		            for(anzahlspalten=0; anzahlspalten<spielfeld.getColumnConstraints().size(); anzahlspalten++){
     		            	createGrids(spielfeld, spielmodus, start);
     		            }
     			}}
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
                }
         }
    	
               
            
        
    

