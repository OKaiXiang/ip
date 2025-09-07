import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import okuke.OKuke;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private OKuke okuke;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/userImage.jpg"));
    private Image OKukeImage = new Image(this.getClass().getResourceAsStream("/images/OKukeImage.jpg"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Duke instance */
    public void setOKuke(OKuke o) {
        okuke = o;
        dialogContainer.getChildren().add(DialogBox.getOKukeDialog(
                "Hello! I'm OKuke.\nWhat can I do for you?", OKukeImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input == null || input.isBlank()) return;

        String response = okuke.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),   // right side
                DialogBox.getOKukeDialog(response, OKukeImage) // left side (flipped)
        );
        userInput.clear();
    }
}
