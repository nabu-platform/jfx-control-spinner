/*
* Copyright (C) 2015 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

package be.tmp;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import be.nabu.jfx.control.spinner.DoubleSpinner;
import be.nabu.jfx.control.spinner.Spinner.Alignment;

public class Test extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
//		DatePicker picker = new DatePicker();
		VBox vbox = new VBox();
		final DoubleSpinner spinner = new DoubleSpinner(Alignment.VERTICAL);
		spinner.valueProperty().setValue(0d);
		spinner.editableProperty().setValue(true);
		spinner.setFormat("00");
		spinner.setMin(0d);
		spinner.setMax(59d);
		spinner.setMaxWidth(25);
//		spinner.setMaxHeight(25);
		
		final TextField wtf = new TextField("wtf");
		vbox.getChildren().add(spinner);
		vbox.getChildren().add(new Button("test"));
		vbox.getChildren().add(wtf);
		root.setCenter(vbox);
		Button button = new Button("Show");
		button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				wtf.setText("" + wtf.getHeight());
			}
			
		});
		root.setBottom(button);
		Scene scene = new Scene(root, 500, 500);
		primaryStage.setTitle("Test");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String...args) {
		launch(args);
	}
}
