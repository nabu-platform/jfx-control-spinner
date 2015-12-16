package be.nabu.jfx.control.spinner;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import be.nabu.jfx.control.spinner.Spinner.Alignment;


public class SpinnerSkin<T> extends SkinBase<Spinner<T>> {

	private Button btnIncrease, btnDecrease;
	private TextField txtValue;
	private VBox buttons;
	private Pane content;
	
	public SpinnerSkin(Spinner<T> spinner) {
		super(spinner);
		initialize();
	}

	private void initialize() {
		btnIncrease = new Button();	// large: 25B2, small: 25B4, white: 25B3
		btnIncrease.getStyleClass().add("jfx-spinner-button");
		btnIncrease.getStyleClass().add("jfx-spinner-button-increase-" + getSkinnable().getAlignment().name().toLowerCase());
		
		btnDecrease = new Button();	// large: 25BC, small: 25BE, white: 25BD
		btnDecrease.getStyleClass().add("jfx-spinner-button");
		btnDecrease.getStyleClass().add("jfx-spinner-button-decrease-" + getSkinnable().getAlignment().name().toLowerCase());
		
		txtValue = new TextField();
		txtValue.getStyleClass().add("jfx-spinner-text");
	
		if (getSkinnable().getAlignment() == Alignment.HORIZONTAL) {
			content = new HBox();
			content.getChildren().addAll(
				btnDecrease,
				txtValue,
				btnIncrease
			);
			btnDecrease.prefHeightProperty().bind(content.heightProperty());
			btnIncrease.prefHeightProperty().bind(content.heightProperty());
			txtValue.prefHeightProperty().bind(content.heightProperty());
		}
		else if (getSkinnable().getAlignment() == Alignment.VERTICAL) {
			content = new VBox();
			content.getChildren().addAll(
				btnIncrease,
				txtValue,
				btnDecrease
			);
			txtValue.prefHeightProperty().bind(content.heightProperty().divide(2));
			btnIncrease.prefHeightProperty().bind(content.heightProperty().divide(4));
			btnDecrease.prefHeightProperty().bind(content.heightProperty().divide(4));
		}
		else {
			content = new HBox();
			buttons = new VBox();
			buttons.getChildren().addAll(
				btnIncrease,
				btnDecrease	
			);
			if (getSkinnable().getAlignment() == Alignment.LEFT) {
				content.getChildren().addAll(
					buttons,
					txtValue
				);
			}
			else {
				content.getChildren().addAll(
					txtValue,
					buttons
				);
			}
			btnIncrease.prefHeightProperty().bind(content.heightProperty().divide(2));
			btnDecrease.prefHeightProperty().bind(content.heightProperty().divide(2));
			buttons.prefHeightProperty().bind(content.heightProperty());
			txtValue.prefHeightProperty().bind(content.heightProperty());
			buttons.maxHeightProperty().bind(buttons.prefHeightProperty());
		}
		btnDecrease.maxHeightProperty().bind(btnDecrease.prefHeightProperty());
		btnIncrease.maxHeightProperty().bind(btnIncrease.prefHeightProperty());
		txtValue.maxHeightProperty().bind(txtValue.prefHeightProperty());
		btnDecrease.minHeightProperty().bind(btnDecrease.prefHeightProperty());
		btnIncrease.minHeightProperty().bind(btnIncrease.prefHeightProperty());
		txtValue.minHeightProperty().bind(txtValue.prefHeightProperty());
		
		Pane test = new Pane();
		test.getChildren().add(content);
		getChildren().add(test);
		
		getSkinnable().widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
				double width = newValue.doubleValue();
				switch (getSkinnable().getAlignment()) {
					case VERTICAL:
						btnIncrease.setPrefWidth(width);
						btnDecrease.setPrefWidth(width);
						txtValue.setPrefWidth(width);
					break;
					case LEFT:
					case RIGHT:
						// additional five is margin
						txtValue.setPrefWidth(width - 20);
						btnIncrease.setPrefWidth(20);
						btnDecrease.setPrefWidth(20);
					break;
					case HORIZONTAL:
						txtValue.setPrefWidth(width - 30);
						btnIncrease.setPrefWidth(15);
						btnDecrease.setPrefWidth(15);
					break;
				}
			}
		});
		
		getSkinnable().heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
				double height = newValue.doubleValue();
				content.setMaxHeight(height);
				content.setPrefHeight(height);
				content.setMinHeight(height);
			}
		});
		
		txtValue.editableProperty().bind(getSkinnable().editableProperty());
		
		getSkinnable().valueProperty().addListener(new ChangeListener<T>() {
			@Override
			public void changed(ObservableValue<? extends T> arg0, T arg1, T arg2) {
				try {
					txtValue.setText(getSkinnable().getConverter().toString(arg2));
				}
				catch (Exception e) {
					// revert
					getSkinnable().valueProperty().setValue(arg1);
				}
			}
		});
		txtValue.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				// make sure it is valid, otherwise revert it
				try {
					getSkinnable().getConverter().fromString(arg2);
				}
				catch (IllegalArgumentException e) {
					// revert
					txtValue.setText(arg1);
					// move one back, the caret will have moved
					txtValue.positionCaret(txtValue.getCaretPosition() - 1);
				}				
			}
		});
		// update the value if focus is lost
		txtValue.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if (!arg2)
					updateValue();
				else
					txtValue.selectAll();
			}
		});
		
		// also update the value if you hit enter
		txtValue.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					updateValue();
					txtValue.selectAll();
				}
			}
		});
		txtValue.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.UP) {
					increment();
					txtValue.selectAll();
					event.consume();
				}
				else if (event.getCode() == KeyCode.DOWN) {
					decrement();
					txtValue.selectAll();
					event.consume();
				}
			}
		});
		
		btnIncrease.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				increment();
			}
		});
		btnDecrease.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				decrement();
			}
		});
		
		// initial value
		if (getSkinnable().valueProperty().getValue() != null)
			txtValue.setText(getSkinnable().getConverter().toString(getSkinnable().valueProperty().getValue()));
	}
	
	private void updateValue() {
		getSkinnable().valueProperty().setValue(getSkinnable().getConverter().fromString(txtValue.getText()));
	}
	
	private void increment() {
		getSkinnable().valueProperty().setValue(
				getSkinnable().getValueChanger().increment(getSkinnable().valueProperty().getValue()));
	}
	private void decrement() {
		getSkinnable().valueProperty().setValue(
				getSkinnable().getValueChanger().decrement(getSkinnable().valueProperty().getValue()));
	}
}
