package com.faforever.client.query;

import com.github.rutledgepaulv.qbuilders.builders.QBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.properties.concrete.StringProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Getter
@Setter
public class BinaryFilterController extends FilterNodeController {

  public GridPane binaryFilter;
  public Label title;
  public CheckBox firstCheckBox;
  public CheckBox secondCheckBox;
  private String propertyName;
  private String firstValue;
  private String secondValue;

  public void setOptions(String firstLabel, String firstValue, String secondLabel, String secondValue) {
    this.firstValue = firstValue;
    this.secondValue = secondValue;
    firstCheckBox.setText(firstLabel);
    secondCheckBox.setText(secondLabel);
  }

  @Override
  public Optional<List<Condition>> getCondition() {
    QBuilder qBuilder = new QBuilder<>();
    StringProperty property = qBuilder.string(propertyName);
    ArrayList<String> values = new ArrayList<>();
    if (firstCheckBox.isSelected()) {
      values.add(firstValue);
    }
    if (secondCheckBox.isSelected()) {
      values.add(secondValue);
    }
    if (values.size() == 1) {
      return Optional.of(Collections.singletonList(property.in(values.toArray())));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void addQueryListener(InvalidationListener queryListener) {
    firstCheckBox.selectedProperty().addListener(queryListener);
    secondCheckBox.selectedProperty().addListener(queryListener);
  }

  @Override
  public void clear() {
    firstCheckBox.setSelected(true);
    secondCheckBox.setSelected(true);
  }

  @Override
  public void setTitle(String title) {
    this.title.setText(title + ":");
  }

  @Override
  public Node getRoot() {
    return binaryFilter;
  }

  public BooleanProperty firstSelectedProperty() {
    return this.firstCheckBox.selectedProperty();
  }

  public void setFirstSelected(boolean isSelected) {
    firstCheckBox.setSelected(isSelected);
  }

  public BooleanProperty secondSelectedProperty() {
    return this.secondCheckBox.selectedProperty();
  }

  public void setSecondSelected(boolean isSelected) {
    secondCheckBox.setSelected(isSelected);
  }

}
