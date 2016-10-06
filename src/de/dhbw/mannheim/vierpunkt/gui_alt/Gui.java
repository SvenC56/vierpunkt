package de.dhbw.mannheim.vierpunkt.gui_alt;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import static javafx.scene.paint.Color.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.scene.control.Slider;

/**
 *
 * @author janaschaub
 */
public class Gui extends Application {
   
    private int anzahlzeilen;                         
    private int anzahlspalten;                          
    private final int l = 70;               // Seitenlaenge der Grids - spaeter manuelle Einstellung
    private final Color rot = RED;          // Spielerfarbe 1
    private final Color gruen = GREEN;      // Spielerfarbe 2
    private SimpleObjectProperty<Color> spielerfarbe = new SimpleObjectProperty<Color>(rot);
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        // automatisches Oeffnen im Fullscreen
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("VierPunkt");
        primaryStage.setResizable(true);
        
        // Erzeugen der aeußeren GridPane
        final GridPane grid = new GridPane();
        grid.setId("grid");
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(15);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setMinWidth((7*l));
        column2.setMaxWidth(7*l);
        ColumnConstraints column3 = new ColumnConstraints();
        column1.setPercentWidth(15);
        grid.getColumnConstraints().addAll(column1, column2, column3); 
        
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(20);
        RowConstraints row2 = new RowConstraints();
        row2.setMinHeight(7*l);
        row2.setMaxHeight(7*l);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(10);
        grid.getRowConstraints().addAll(row1, row2, row3); 
        
        // Erzeugen der Ueberschrift 
        
        javafx.scene.image.Image ueberschrift = new javafx.scene.image.Image(getClass().getResource("ueberschrift.jpeg").toExternalForm());
        
        // Ansicht der Ueberschrift
        ImageView vier = new ImageView(ueberschrift);
        vier.setImage(ueberschrift);
        grid.add(vier, 1, 0, 3, 1);
        vier.setFitWidth(800);
        vier.setFitHeight(150);
        vier.setPreserveRatio(true);
        vier.setId("ueberschrift");
        
        /** Uebersicht der aeußeren GridPane
         * 	_____________________________________
         *	|			|			|			|
         *	|	0,0		|	1,0		|	2,0		|
         *	|___________|___________|___________|
         *	|			|			|			|
         * 	|			|			|			|
         * 	|	0,1		|	1,1		|	2,1		|
         * 	|			|			|			|
         * 	|___________|___________|___________|
         * 	|			|			|			|
         * 	|	0,2		|	1,2		|	2,2		|
         * 	|___________|___________|___________|
         * 
         * 	_____________________________________
         *	|			|			|			|
         *	|	0,0		|	1,0		|	2,0		|
         *	|___________|___________|___________|
         *	|			|			|			|
         * 	|			| boxmitte	|			|
         * 	|  boxlinks	| spielfeld	| boxrechts	|
         * 	|			|			|			|
         * 	|___________|___________|___________|
         * 	|			|			|			|
         * 	|	0,2		|	1,2		|	2,2		|
         * 	|___________|___________|___________|
         **/
        
        // Erzeugen von Boxen zur besseren Anzeige der einzelnen Elemente
        VBox boxrechts = new VBox();
        boxrechts.setId("box");
        Label satzstatus = new Label("Satzstatus: warten auf den Gegner");
        Label spielstand = new Label ("Spielstand: 1 zu 1");
        Label spielmodi = new Label ("Spielmodus:");
        Button start = new Button("Spiel starten");
        Slider spielmodus = new Slider(0, 2, 1);
        spielmodus.setMin(0);
        spielmodus.setMax(2);
        spielmodus.setValue(1);
        spielmodus.setMinorTickCount(0);
        spielmodus.setMajorTickUnit(1);
        spielmodus.setSnapToTicks(true);
        spielmodus.setShowTickMarks(true);
        spielmodus.setShowTickLabels(true);
        spielmodus.setOrientation(Orientation.VERTICAL);
        
        spielmodus.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double n) {
                if (n == 0) return "manuell";
                if (n > 0 && n < 2) return "gegen den Computer";
                if (n == 2) return "automatisch";

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

        spielmodus.setPrefHeight(120);
        boxrechts.getChildren().addAll(spielstand, satzstatus, spielmodi, spielmodus, start);
        boxrechts.setMinWidth(200);
        grid.add(boxrechts, 2, 1);
        
        VBox boxmitte = new VBox();
        boxmitte.setId("boxmitte");
        Rectangle platzhalter = new Rectangle(7*l,l);
        platzhalter.setOpacity(0);
        grid.add(boxmitte, 1, 1);
        boxmitte.getChildren().add(platzhalter);
        
        VBox boxlinks = new VBox();
        boxlinks.setId("boxlinks");
        grid.add(boxlinks, 0, 1);
        boxlinks.setMinWidth(200);
        
        // Erzeugen eines GridPanes spielfeld im uebergeordneten GridPane grid
        GridPane spielfeld = new GridPane();
        spielfeld.setId("spielfeld");
        boxmitte.getChildren().add(spielfeld);
        //grid.add(spielfeld, 1, 1);
        
        
        
        // Erzeugen der Spalten (7)
        spielfeld.getColumnConstraints().addAll(
                new ColumnConstraints(l,l,Double.MAX_VALUE),
                new ColumnConstraints(l,l,Double.MAX_VALUE), 
                new ColumnConstraints(l,l,Double.MAX_VALUE),
                new ColumnConstraints(l,l,Double.MAX_VALUE), 
                new ColumnConstraints(l,l,Double.MAX_VALUE),
                new ColumnConstraints(l,l,Double.MAX_VALUE),
                new ColumnConstraints(l,l,Double.MAX_VALUE));
        // Erzeugen der Zeilen (6)
        spielfeld.getRowConstraints().addAll(
                new RowConstraints(l,l,Double.MAX_VALUE), 
                new RowConstraints(l,l,Double.MAX_VALUE),
                new RowConstraints(l,l,Double.MAX_VALUE), 
                new RowConstraints(l,l,Double.MAX_VALUE),
                new RowConstraints(l,l,Double.MAX_VALUE),
                new RowConstraints(l,l,Double.MAX_VALUE));
         
        createGrids(spielfeld); // Methodenaufruf
        
        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(Gui.class.getResource("Gui.css").toExternalForm());

        primaryStage.show();
    }

    private void createGrids(final GridPane spielfeld){
       // spielfeld.getChildren().clear();
        for(anzahlzeilen=0;anzahlzeilen<spielfeld.getRowConstraints().size(); anzahlzeilen++){
            for(anzahlspalten=0; anzahlspalten<spielfeld.getColumnConstraints().size(); anzahlspalten++){
            
            // Darstellung des Rahmens/ der Zellen    
            Rectangle rect = new Rectangle(l,l);
            Circle circ = new Circle((l/2)-5);
            circ.centerXProperty().set(l/2);
            circ.centerYProperty().set(l/2);
            Shape cell = Path.subtract(rect, circ);
            cell.setId("cell");
            /*DropShadow effect = new DropShadow();
            effect.setSpread(.2);
            effect.setRadius(25);
            effect.setColor(BLACK);
            cell.setEffect(effect);*/
             
            // Erzeugen der Spielsteine
            javafx.scene.image.Image image1 = new javafx.scene.image.Image(getClass().getResource("spielstein-rot.png").toExternalForm());
            javafx.scene.image.Image image2 = new javafx.scene.image.Image(getClass().getResource("spielstein_gruen.png").toExternalForm());
            javafx.scene.image.Image image3 = new javafx.scene.image.Image(getClass().getResource("spielstein-grau.png").toExternalForm());
            
            // Ansicht der Spielsteine
            ImageView spielstein = new ImageView(image1);
            spielstein.setImage(image1);
            spielstein.setFitWidth(l-10);
            spielstein.setPreserveRatio(true);  
            
            // Vorschau der Spielsteine
            ImageView vorschauspielstein = new ImageView(image3);
            vorschauspielstein.setImage(image1);
            vorschauspielstein.setFitWidth(l-10);
            vorschauspielstein.setPreserveRatio(true); 
            vorschauspielstein.setOpacity(0.5);
            
            // Methode zur Vorschau - jeweiliger Spielstein wird angezeigt
            vorschauspielstein.setOnMouseEntered(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent arg0) {
                    if(spielerfarbe.get()==rot){
                        vorschauspielstein.setImage(image1);      
                    }else{
                        vorschauspielstein.setImage(image2);
                    }
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
                    if(spielerfarbe.get()==rot){
                        vorschauspielstein.setImage(image1);
                    }else{
                        vorschauspielstein.setImage(image2);
                    }
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
                        if(spielerfarbe.get()==rot){
                            spielstein.setImage(image1);
                            spielerfarbe.set(gruen);
                        }else{
                            spielstein.setImage(image2);
                            spielerfarbe.set(rot);
                        }
                    }
                }
            });
           
            // Setzen der Spielsteine beim Klick auf die entsprechende Vorschau
            vorschauspielstein.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent arg0) {
                    if(spielstein.getTranslateY()!=0){
                        translateTransition.setToY(0);
                        translateTransition.play();
                        if(spielerfarbe.get()==rot){
                            spielerfarbe.set(gruen);
                            spielstein.setImage(image1);
                        }else{
                            spielerfarbe.set(rot);
                            spielstein.setImage(image2);
                        }
                    }
                }
            });
            
            // Zellen werden gefuellt
            StackPane stack = new StackPane();
            vorschauspielstein.setImage(image3);                         // Hintergrund grau
            stack.getChildren().addAll(cell, vorschauspielstein, spielstein);    // Fuellen der Zelle mit Rahmen, Vorschau oder Spielstein
            spielfeld.add(stack, anzahlspalten, anzahlzeilen); 
            }
        }
    }
}
