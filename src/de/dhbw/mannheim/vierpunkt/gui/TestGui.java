package de.dhbw.mannheim.vierpunkt.gui;
import javafx.animation.TranslateTransition;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.util.Duration;
import javafx.util.StringConverter;
import java.awt.Toolkit;
import java.io.File;

import de.dhbw.mannheim.vierpunkt.interfaces.ZugListener;
/**
 *
 * @author janaschaub
 */
public class TestGui implements ZugListener {

	/****** VARIABLENDEKLARATION *****/
	

	
	
	/**
	 * Gibt den aktuellen Fuellstand aller Spalten an
	 */	
	static int[] plaetzeFreiInReihe = new int[7];
	public static Stage primaryStage = new Stage();
	
	private int anzahlzeilen;
	private int anzahlspalten;
	private final int l = 70; 		// Seitenlaenge der Grids - spaeter manuelle Einstellung
	public int spieler = 1; 		// Spieler 1
	private double breite = Toolkit.getDefaultToolkit().getScreenSize().width; // Breite des Fensters in Pixeln
	
	private String names1 = "spieler1";
	private String names2 = "spieler2";
	public String getNames1() {
		return names1;
	}
	public String getNames2() {
		return names2;
	}

	
	private String schnittstelle = "pusher";
	private int zugzeit = 2000;
	
	//Erzeugen der Spielsteine
    public javafx.scene.image.Image image1 = new javafx.scene.image.Image(getClass().getResource("kuerbis.png").toExternalForm());
    public javafx.scene.image.Image getImage1() {
		return image1;
	}
	public javafx.scene.image.Image getImage2() {
		return image2;
	}

	public javafx.scene.image.Image image2 = new javafx.scene.image.Image(getClass().getResource("fledermaus.png").toExternalForm());
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
    public Color color = Color.rgb(0, 0, 0);
    public GridPane spielfeld = new GridPane();
    
    // Getter und Setter Methoden
    public void setColor(Color color) {	this.color = color;}
	public void setImage1(javafx.scene.image.Image image1) {this.image1 = image1;}
	public void setImage2(javafx.scene.image.Image image2) {this.image2 = image2;}
	public void setImage3(javafx.scene.image.Image image3) {this.image3 = image3;}
	public double getZugzeit() {return zugzeit;}
	public void setZugzeit(int zugzeit) {this.zugzeit = zugzeit;}
	public String getSchnittstelle() {return schnittstelle;}
	public void setSchnittstelle(String schnittstelle) {this.schnittstelle = schnittstelle;}
	

	/*********************************************************************************************************************
	 ******************************************* START METHODE *********************************************************
	 ********************************************************************************************************************/
	
	public void start(Stage primaryStage) {
		
		for (int i = 0; i < plaetzeFreiInReihe.length; i++){
			plaetzeFreiInReihe[i]=5;
		}
		
		
		// Grundlegende Eigenschaften der Stage
		primaryStage.setFullScreen(true); 		// automatisches Oeffnen im Fullscreen
		primaryStage.setTitle("VierPunkt");
		primaryStage.setResizable(true);

		// Layout Boxen
		VBox root = new VBox(); 				// aeusserste Box
		root.setId("root");
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
		ueberschrift.setId("ueberschrift");
		ueberschrift.setFitWidth(breite / 2); // Ueberschrift soll die Haelfte des Bildschirms breit sein
		ueberschrift.setFitHeight((breite / 2) / 3.33); // Aufrechterhalten des Verhaeltnisses (Quotient: 3.33)
		ueberschrift.setPreserveRatio(true);

		// Container, in dem die Ueberschrift platziert wird
		HBox titelbox = new HBox();
		titelbox.setId("titel");

		// Rechtecke als Platzhalter, um die Ueberschrift mittig zu platzieren
		Rectangle containerlinks = new Rectangle(breite / 4, (breite / 2) / 3.33);
		containerlinks.setId("container");
		Rectangle containerrechts = new Rectangle(breite / 4, (breite / 2) / 3.33);
		containerrechts.setId("container");

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
		
		Label spieler1 = new Label("Name Spieler 1: ");
		TextField spielername1 = new TextField();
		Label spieler2 = new Label("Name Spieler 2: ");
		TextField spielername2 = new TextField();
		names1 = spielername1.getText();
		names1 = spielername2.getText();
	
		Label spielerfarben = new Label("Spieler");
		HBox box3 = new HBox();
		
		
	
		ImageView i1 = new ImageView(getImage1());
		i1.setFitWidth(l-20);
		i1.setFitHeight(l-20);
		Label s1 = new Label(names1);
		
		ImageView i2 = new ImageView(getImage2());
		i2.setFitWidth(l-20);
		i2.setFitHeight(l-20);
		Label s2 = new Label(names2);
		Rectangle p = new Rectangle(20,20);
		p.setOpacity(0);
		Rectangle p1 = new Rectangle(20,20);
		p1.setOpacity(0);
		box3.getChildren().addAll(s1, i1, s2, i2);
		
		
		Label schnittstelle = new Label("Schnittstelle");
		CheckBox file = new CheckBox("File");
		CheckBox pusher1 = new CheckBox("Pusher");
		pusher1.setSelected(true);
		pusher1.setOnMouseClicked(new EventHandler<MouseEvent>(){
                   @Override
                   public void handle(MouseEvent arg0) {
                       if(file.isSelected()){
                    	   file.setSelected(false);   
                    	   pusher1.setSelected(true);
                       }else{pusher1.setSelected(true);}
                       setSchnittstelle("pusher");
                   }
               });
		file.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                if(pusher1.isSelected()){
             	   pusher1.setSelected(false);   
             	   file.setSelected(true);
                }else{file.setSelected(true);}
                setSchnittstelle("file");
            }
        });
		
		/*String fileString = new String();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose File");
		File selectedFile = fileChooser.showOpenDialog(primaryStage);
		 if (selectedFile != null) {
		    System.out.println("kein File ausgewählt");
		 }else{
			 fileString = selectedFile.getPath();
			 System.out.println(fileString);
		 }
		 */

		
		Slider zeit = new Slider(0, 5000, 100); 			// Slider geht von 0 bis 2 in 1er Abstaenden
		zeit.setMinorTickCount(0);
		zeit.setMajorTickUnit(100); 					// Man kann nur auf den Zahlen 0, 1, 2 landen, nicht dazwischen
		zeit.setSnapToTicks(true); 						// Der Punkt rutzscht zur naechsten Zahl
		zeit.setShowTickMarks(true); 					// Markierungen anzeigen -
		zeit.setOrientation(Orientation.HORIZONTAL); 	// Vertikale Anordnung,standardmaessig horizontal
		
		zeit.setValue(2000);								// Default Value = 2
		
		Label zeitlabel = new Label("Zugzeit");
		zeit.valueProperty().addListener(new ChangeListener<Number>() {
		    @Override
		    public void changed(ObservableValue<? extends Number> observable,
		            Number oldValue, Number newValue) {
		    	zeitlabel.setText("Zugzeit:   " + newValue + " ms");
		    	setZugzeit(newValue.intValue());
		    }
		});
		
		
		ImageView bild = new ImageView();
		bild.setId("bild");
		bild.setFitWidth(breite / 4); // Breite soll ein Viertel des Fensters betragen
		bild.setPreserveRatio(true); // Das Verhaeltnis soll beibehalten werden
		
		Rectangle platzhalter2 = new Rectangle(10, 70); // Platzhalter, damit das Bild nicht ganz am Boden sitzt
		platzhalter2.setOpacity(0);

		// Einfuegen der Elemente in die linke Box
		boxlinks.getChildren().addAll(spielerfarben, box3, p, schnittstelle, file, pusher1, p1, zeitlabel, zeit, bild, platzhalter2);

		/******* CONTAINERBOXEN EINFUEGEN ************************/
		content.getChildren().addAll(boxlinks, boxmitte, boxrechts);

		/*********************************************************************************************************************
		 ******************************************* EVENTHANDLER FUER DAS MENU *********************************************************
		 ********************************************************************************************************************/
		
		Scene scene = new Scene(root);
		
		menu13.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) { Platform.exit();}
		});
		
		menu11.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e) {
				primaryStage.setScene(scene);
				primaryStage.setFullScreen(true);
				spieler = 1;
				createGrids();
				}
		});
		
		menu21.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				primaryStage.setScene(scene);
				primaryStage.setFullScreen(true);
				setColor(Color.PURPLE);
				setImage1(orange);
				setImage2(gruen);
				i1.setImage(orange);
				i2.setImage(gruen);
				scene.getStylesheets().clear();
				scene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());
				createGrids();
			}
		});
		
		menu22.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				setColor(Color.BLACK);
				setImage1(kuerbis);
				setImage2(fledermaus);
				i1.setImage(kuerbis);
				i2.setImage(fledermaus);
				scene.getStylesheets().clear();
				scene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());
				createGrids();
			}
		});
		
		menu23.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				primaryStage.setScene(scene);
				primaryStage.setFullScreen(true);
				scene.getStylesheets().clear();
				scene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());
				setColor(Color.DARKGREEN);
				setImage1(pizza);
				setImage2(burger);
				i1.setImage(pizza);
				i2.setImage(burger);
				
				createGrids();
			}
		});
		
		menu24.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				primaryStage.setScene(scene);
				primaryStage.setFullScreen(true);
				scene.getStylesheets().clear();
				scene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());
				setColor(Color.CADETBLUE);
				setImage1(basketball);
				setImage2(baseball);
				i1.setImage(basketball);
				i2.setImage(baseball);
				
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
		
		
		Stage loginStage = new Stage();
		Button login = new Button("Spiel starten");
		FlowPane pane=new FlowPane();
		pane.setPadding(new Insets(10, 10, 10, 10));
		pane.setVgap(4);
		pane.setHgap(4);
		
		
		Stage gewinnermeldung = new Stage();
		FlowPane panegewinner = new FlowPane();
		Label gewinnernachricht = new Label();
		panegewinner.setPadding(new Insets(10, 10, 10, 10));
		panegewinner.getChildren().addAll(gewinnernachricht);
		
		Scene meldung = new Scene(panegewinner);
		gewinnermeldung.setScene(meldung);

		
		
		pane.getChildren().addAll(spieler1, spielername1, spieler2, spielername2, login);
		
		SplitPane alteSpiele = new SplitPane();
		alteSpiele.setOrientation(Orientation.VERTICAL);
		Label spieleLabel = new Label("Bisherige Spiele:");
		ScrollPane listeSpiele = new ScrollPane();
		Text beispieltext = new Text("Spiele ID 1 \n Spiele ID 2 \n Spiele ID 3 \n Spiele ID 4");
		listeSpiele.setContent(beispieltext);
		Button back = new Button("zurueck");
		
		
		alteSpiele.getItems().addAll(menuBar, spieleLabel, listeSpiele, back);
		
		
		
		
		Scene datenbank = new Scene(alteSpiele);
				alteSpiele.setId("bisherigeSpiele");
		
		primaryStage.setScene(scene);
		scene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());
		Scene scene2 = new Scene(pane, 200, 200);
		
	    loginStage.setScene(scene2);
	    loginStage.initModality(Modality.APPLICATION_MODAL);
	    loginStage.setTitle("Login");
	    loginStage.setFullScreen(false);
	    
	    Stage gamesStage = new Stage();
	    gamesStage.setScene(datenbank);
	    
	    back.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
            public void handle(MouseEvent arg0) {
				
				gamesStage.close();
				primaryStage.toFront();
				
				
            }
		});
	    
	    menu31.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e) {
				//menu31.getScene().setRoot(alteSpiele);
				
				gamesStage.showAndWait();
				gamesStage.setFullScreen(true);
				}
		});
	    
	    login.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	         public void handle(KeyEvent evt)
	         {
	              if (evt.getCode() == KeyCode.ENTER)
	            	loginStage.close();
					s1.setText(spielername1.getText());
					s2.setText(spielername2.getText());
					primaryStage.show();
	         }
	    });
	    
		login.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
            public void handle(MouseEvent arg0) {
				loginStage.close();
				s1.setText(spielername1.getText());
				s2.setText(spielername2.getText());
				primaryStage.show();
            }
		});
		
		start.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
            public void handle(MouseEvent arg0) {
				if(spielmodus.getValue()==2){
					spieler = 1;
	            	createGrids_automatisch();
				}else{
					spieler = 1;
					createGrids();}
            }
		});
		
		
		loginStage.show();
		
	}

	
	/*********************************************************************************************************************
     *******************************************  SPIELFELD ERZEUGEN METHODE  ********************************************
     ********************************************************************************************************************/
    public void gewinnermethode(Stage gewinnermeldung, Label gewinnernachricht, int spieler){
		if(spieler == 1){
			gewinnernachricht.setText("Spieler " + spieler + "hat gewonnen!");
		}else {
			if(spieler == 2){
				gewinnernachricht.setText("Spieler " + spieler + "hat gewonnen!");
			}else{
				gewinnernachricht.setText("Das spiel ist unentschieden ausgegangen.");
			}}
		gewinnermeldung.show();
		
	}
    
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
            vorschauspielstein.setFitWidth(l-10);
            vorschauspielstein.setPreserveRatio(true); 
            vorschauspielstein.setOpacity(0.5);
            
            spielstein.setTranslateY(-(l*(anzahlzeilen+1)));
            
            // Zellen werden gefuellt
            StackPane stack = new StackPane();
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
                System.out.println((int)spielstein.getId().charAt(10)-48 + " " + spieler);
                spieler=2;
            }else{
                spielstein.setImage(image2);
                System.out.println((int)spielstein.getId().charAt(10)-48 + " " + spieler);
                spieler=1;
            }
        }
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
    
    
	@Override
	public void zugGespielt(int zug)
	{
		setSpielstein(plaetzeFreiInReihe[zug], zug);
        plaetzeFreiInReihe[zug]--;
	}

 
}

               
            