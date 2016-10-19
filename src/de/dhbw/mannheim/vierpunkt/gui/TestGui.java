package de.dhbw.mannheim.vierpunkt.gui;
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
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
	
	//Erzeugen der Spielsteine
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
		MenuItem menu31 = new MenuItem("letztes Spiel nachvollziehen");
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
		VBox boxrechts2 = new VBox();
		boxrechts2.setPrefWidth(breite / 4);
		boxrechts2.setSpacing(10);
		boxrechts2.setPadding(new Insets(50, 0, 0, 50));
		
		Label sieger = new Label("Sieger:");
		Text antwortsieger = new Text("Spieler 1");
		boxrechts2.getChildren().addAll(sieger, antwortsieger);
		

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
		boxlinks.setAlignment(Pos.BOTTOM_LEFT);

		/******* INHALTE DER LINKEN CONTAINERBOX ************************/
		
		Label schnittstelle = new Label("Schnittstelle");
		CheckBox file = new CheckBox("File");
		CheckBox pusher = new CheckBox("Pusher");
		pusher.setSelected(true);
		pusher.setOnMouseClicked(new EventHandler<MouseEvent>(){
                   @Override
                   public void handle(MouseEvent arg0) {
                       if(file.isSelected()){
                    	   file.setSelected(false);   
                    	   pusher.setSelected(true);
                       }else{pusher.setSelected(true);}
                   }
               });
		file.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                if(pusher.isSelected()){
             	   pusher.setSelected(false);   
             	   file.setSelected(true);
                }else{file.setSelected(true);}
            }
        });
		
		Slider zeit = new Slider(0.5, 5, 0.1); 			// Slider geht von 0 bis 2 in 1er Abstaenden
		zeit.setMinorTickCount(0);
		zeit.setMajorTickUnit(0.1); 					// Man kann nur auf den Zahlen 0, 1, 2 landen, nicht dazwischen
		zeit.setSnapToTicks(true); 						// Der Punkt rutzscht zur naechsten Zahl
		zeit.setShowTickMarks(true); 					// Markierungen anzeigen -
		zeit.setOrientation(Orientation.HORIZONTAL); 	// Vertikale Anordnung,standardmaessig horizontal
		
		zeit.setValue(2);								// Default Value = 2
		
		Label zugzeit = new Label("Zugzeit");
		zeit.valueProperty().addListener(new ChangeListener<Number>() {
		    @Override
		    public void changed(ObservableValue<? extends Number> observable,
		            Number oldValue, Number newValue) {
		    	 NumberFormat numberFormat = new DecimalFormat("0.0");
		    	    numberFormat.setRoundingMode(RoundingMode.DOWN);
		    	zugzeit.setText("Zugzeit:   " + numberFormat.format(newValue));
		    }
		});
		
		ImageView bild = new ImageView();
		bild.setId("bild_sweets");
		bild.setFitWidth(breite / 4); // Breite soll ein Viertel des Fensters betragen
		bild.setPreserveRatio(true); // Das Verhaeltnis soll beibehalten werden
		
		Rectangle platzhalter2 = new Rectangle(10, 70); // Platzhalter, damit das Bild nicht ganz am Boden sitzt
		platzhalter2.setOpacity(0);

		// Einfuegen der Elemente in die linke Box
		boxlinks.getChildren().addAll(schnittstelle, file, pusher, zugzeit, zeit, bild, platzhalter2);

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
		
		
		Stage newStage = new Stage();
		
		Button login = new Button("Spiel starten");
		FlowPane pane=new FlowPane();
		pane.getChildren().add(login);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		scene.getStylesheets().add(TestGui.class.getResource("Gui.css").toExternalForm());
		Scene scene2 = new Scene(pane, 200, 100);
		
	    newStage.setScene(scene2);
	    newStage.initModality(Modality.APPLICATION_MODAL);
	    newStage.setTitle("Login");
	    newStage.setFullScreen(false);
		login.setOnMouseClicked(new EventHandler<MouseEvent>(){
					@Override
		            public void handle(MouseEvent arg0) {
						newStage.close();
						primaryStage.show();
		            }
				});
		newStage.show();
		
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
    	spielsteinAnzeigen(getImageView((getNodeByRowColumnIndex(zeile, spalte))));
    }
    
    public void spielsteinAnzeigen(ImageView spielstein){
    	if(spielstein.getTranslateY()!=0){ 
    		//spielstein.setTranslateY(-(l*(anzahlzeilen+1)));
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
    
    public StackPane getNodeByRowColumnIndex (final int row, final int column) {
        for (Node node : spielfeld.getChildren()) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return (StackPane) node;
            }
        } return null;
    }
    
    public ImageView getImageView (StackPane stack) {
        ObservableList<Node> list = stack.getChildren();
        return (ImageView)list.get(2);
    }
}
    	
               
            
        
    

