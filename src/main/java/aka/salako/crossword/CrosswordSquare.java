    package aka.salako.crossword;

    import javafx.animation.ScaleTransition;
    import javafx.beans.value.ChangeListener;
    import javafx.beans.value.ObservableValue;
    import javafx.geometry.Insets;
    import javafx.geometry.Pos;
    import javafx.scene.Cursor;
    import javafx.scene.control.Label;
    import javafx.scene.input.KeyEvent;
    import javafx.scene.layout.GridPane;
    import javafx.scene.text.Font;
    import javafx.scene.text.FontWeight;
    import javafx.util.Duration;

    import java.util.Objects;

    public class CrosswordSquare extends Label {

         String lastDirection = "RIGHT"; // Initialiser la dernière direction par défaut

        private final Crossword crossword;
        private char solution;
        private char proposition;
        private String horizontalDefinition;
        private String verticalDefinition;
        private boolean isBlackSquare;

        private int row;
        private int col;

        public CrosswordSquare(Crossword crossword, char solution, char proposition, String horizontalDefinition, String verticalDefinition, boolean isBlackSquare) {
            this.crossword = crossword;
            this.solution = solution;
            this.proposition = proposition;
            this.horizontalDefinition = horizontalDefinition;
            this.verticalDefinition = verticalDefinition;
            this.isBlackSquare = isBlackSquare;

            initializeSquare();

            textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.isEmpty()) {
                        // Créer une transition de mise à l'échelle
                        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), CrosswordSquare.this);
                        scaleTransition.setToX(1.5); // Facteur d'agrandissement horizontal
                        scaleTransition.setToY(1.5); // Facteur d'agrandissement vertical
                        scaleTransition.setAutoReverse(true); // Animation en va-et-vient
                        scaleTransition.setCycleCount(2); // Nombre de fois où l'animation se répète

                        // Démarrer l'animation
                        scaleTransition.play();
                    }
                }
            });
        }


        private void initializeSquare() {
            // Chargement de ma feuille de Style CSS externe
            String cssFile = Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm();
            this.getStylesheets().add(cssFile);

            this.getStyleClass().add("crossword-square");
            if (isBlackSquare) {
                this.getStyleClass().add("black");
            }

            this.setCursor(Cursor.HAND);
            this.setPrefWidth(99.9);
            this.setPrefHeight(99.9);
            this.setAlignment(Pos.CENTER);
            this.setFont(Font.font("System", FontWeight.BOLD, 30));
            this.setPadding(new Insets(0, 0, 0, 0));

            // Ajouter un écouteur d'événements pour obtenir le focus lorsque la case est cliquée
            setOnMouseClicked(event -> {
                if (!this.isBlackSquare) {
                    requestFocus();
                }
            });

            // Ajouter un écouteur d'événements pour les touches du clavier
            setOnKeyPressed(this::handleKeyPressed);

        }

        private void handleKeyPressed(KeyEvent event) {
            switch (event.getCode()) {
                case BACK_SPACE:
                    // Effacer le texte de la case actuelle
                    setText("");
                    // Déplacer vers la case précédente en fonction du dernier mouvement effectué
                    moveToPreviousSquare();
                    break;
                case RIGHT:
                    moveRight();
                    lastDirection = "RIGHT"; // Mettre à jour la dernière direction
                    System.out.println(lastDirection);
                    break;
                case DOWN:
                    moveDown();
                    lastDirection = "DOWN";  // Mettre à jour la dernière direction
                    System.out.println(lastDirection);
                    break;
                case UP:
                    moveUp();
                    lastDirection = "DOWN"; // Mettre à jour la dernière direction
                    System.out.println(lastDirection);
                    break;
                case LEFT:
                    moveLeft();
                    lastDirection = "RIGHT"; // Mettre à jour la dernière direction
                    System.out.println(lastDirection);
                    break;
                default:
                    if (event.getCode().isLetterKey()) {
                        char typedLetter = event.getText().charAt(0); // Obtenir le caractère tapé
                        if (!event.getText().isEmpty() && event.getText().length() == 1) {
                            setText(String.valueOf(typedLetter).toUpperCase());
                            this.setProposition(typedLetter);
                            moveNext(); // Avancer vers la case suivante
                        }
                    }
                    break;
            }
        }

        private void moveToPreviousSquare() {
            switch (lastDirection) {
                case "RIGHT":
                    moveLeft();
                    break;
                case "DOWN":
                    moveUp();
                    break;
                default:
                    break;
            }
        }





        private void moveRight() {
            GridPane boardGrid = (GridPane) getParent();
            int nextColumnIndex = getColumn() + 1;
            int nextRowIndex = getRow();

            // Tant que la prochaine case est une case noire et que nous ne sommes pas à la fin de la grille
            while (nextColumnIndex < crossword.getWidth() && crossword.getCell(nextRowIndex + 1, nextColumnIndex + 1).isBlackSquare()) {
                nextColumnIndex++;
            }

            // Si nous ne sommes pas à la fin de la grille, déplacer le focus
            if (nextColumnIndex < crossword.getWidth()) {
                CrosswordSquare nextSquare = (CrosswordSquare) boardGrid.getChildren().get(nextColumnIndex + nextRowIndex * crossword.getWidth());
                nextSquare.requestFocus();
            }
        }

        private void moveDown() {
            GridPane boardGrid = (GridPane) getParent();
            int nextColumnIndex = getColumn();
            int nextRowIndex = getRow() + 1;

            // Tant que la prochaine case est une case noire et que nous ne sommes pas à la fin de la grille
            while (nextRowIndex < crossword.getHeight() && crossword.getCell(nextRowIndex + 1, nextColumnIndex + 1).isBlackSquare()) {
                nextRowIndex++;
            }

            // Si nous ne sommes pas à la fin de la grille, déplacer le focus
            if (nextRowIndex < crossword.getHeight()) {
                CrosswordSquare nextSquare = (CrosswordSquare) boardGrid.getChildren().get(nextColumnIndex + nextRowIndex * crossword.getWidth());
                nextSquare.requestFocus();
            }
        }

        private void moveUp() {
            GridPane boardGrid = (GridPane) getParent();
            int nextColumnIndex = getColumn();
            int nextRowIndex = getRow() - 1;

            // Tant que la case précédente est une case noire et que nous ne sommes pas au début de la grille
            while (nextRowIndex >= 0 && crossword.getCell(nextRowIndex + 1, nextColumnIndex + 1).isBlackSquare()) {
                nextRowIndex--;
            }

            // Si nous ne sommes pas au début de la grille, déplacer le focus
            if (nextRowIndex >= 0) {
                CrosswordSquare nextSquare = (CrosswordSquare) boardGrid.getChildren().get(nextColumnIndex + nextRowIndex * crossword.getWidth());
                nextSquare.requestFocus();
            }
        }

        private void moveLeft() {
            GridPane boardGrid = (GridPane) getParent();
            int nextColumnIndex = getColumn() - 1;
            int nextRowIndex = getRow();

            // Tant que la case précédente est une case noire et que nous ne sommes pas au début de la grille
            while (nextColumnIndex >= 0 && crossword.getCell(nextRowIndex + 1, nextColumnIndex + 1).isBlackSquare()) {
                nextColumnIndex--;
            }

            // Si nous ne sommes pas au début de la grille, déplacer le focus
            if (nextColumnIndex >= 0) {
                CrosswordSquare nextSquare = (CrosswordSquare) boardGrid.getChildren().get(nextColumnIndex + nextRowIndex * crossword.getWidth());
                nextSquare.requestFocus();
            }
        }


        private int getRow() {
            return GridPane.getRowIndex(this);
        }

        private int getColumn() {
            return GridPane.getColumnIndex(this);
        }

        private void moveNext() {
            int currentRow = getRow();
            int currentColumn = getColumn();

            int nextRow;
            int nextColumn;

            switch (lastDirection) {
                case "RIGHT":
                    nextRow = currentRow;
                    nextColumn = currentColumn + 1;
                    break;
                case "DOWN":
                    nextRow = currentRow + 1;
                    nextColumn = currentColumn;
                    break;
                default:
                    return;
            }

            // Vérifie si la case suivante est valide et n'est pas une case noire
            if (nextRow >= 0 && nextRow < crossword.getHeight() &&
                    nextColumn >= 0 && nextColumn < crossword.getWidth() &&
                    !crossword.getCell(nextRow + 1, nextColumn + 1).isBlackSquare()) {

                GridPane boardGrid = (GridPane) getParent();
                CrosswordSquare nextSquare = (CrosswordSquare) boardGrid.getChildren().get(nextColumn + nextRow * crossword.getWidth());
                nextSquare.requestFocus();
            }
        }

        public char getSolution() {
            return solution;
        }

        public void setSolution(char solution) {
            this.solution = solution;
        }

        public char getProposition() {
            return proposition;
        }

        public void setProposition(char proposition) {
            this.proposition = proposition;
        }

        public String getHorizontalDefinition() {
            return horizontalDefinition;
        }

        public void setHorizontalDefinition(String horizontalDefinition) {
            this.horizontalDefinition = horizontalDefinition;
        }

        public String getVerticalDefinition() {
            return verticalDefinition;
        }

        public void setVerticalDefinition(String verticalDefinition) {
            this.verticalDefinition = verticalDefinition;
        }

        public boolean isBlackSquare() {
            return this.getSolution() == ' ';
        }
    }
