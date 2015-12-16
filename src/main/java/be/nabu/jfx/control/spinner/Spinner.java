package be.nabu.jfx.control.spinner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.util.StringConverter;

abstract public class Spinner<T> extends Control {

	public interface ValueChanger<T> {
		public T increment(T currentValue);
		public T decrement(T currentValue);
	}
	
	public enum Alignment {
		VERTICAL, HORIZONTAL, RIGHT, LEFT
	}
	
	private ObjectProperty<T> value = new SimpleObjectProperty<T>(this, "value");
	private ObjectProperty<Boolean> editable = new SimpleObjectProperty<Boolean>(this, "editable", true);

	private Alignment alignment;
	private String externalForm;
	
	public Spinner(Alignment alignment) {
		this.alignment = alignment;
		getStyleClass().add("jfx-spinner");
		// default height of a text field is 20
		setPrefHeight(getAlignment() == Alignment.VERTICAL ? 50 : 25);
		setMaxHeight(getAlignment() == Alignment.VERTICAL ? 50 : 25);
	}

	public ObjectProperty<T> valueProperty() {
		return value;
	}
	
	public ObjectProperty<Boolean> editableProperty() {
		return editable;
	}
	
	@Override
	public String getUserAgentStylesheet() {
		if (externalForm == null) {
			externalForm = Spinner.class.getClassLoader().getResource("jfx-spinner.css").toExternalForm();
		}
		return externalForm;
	}
		
	public Alignment getAlignment() {
		return alignment;
	}

	abstract public ValueChanger<T> getValueChanger();
	abstract public StringConverter<T> getConverter();
}
