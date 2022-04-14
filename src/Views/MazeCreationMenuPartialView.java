package Views;

import Generators.GeneratorFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MazeCreationMenuPartialView extends PartialView implements ActionListener {
    private static class MazeSizeSpinnerModel extends SpinnerNumberModel{
        MazeSizeSpinnerModel(){
            super(10, 1, 100, 1);
        }
    }

    private final JSpinner rowsInput, colsInput;
    private final JButton createButton, saveButton, exportButton;
    private final JComboBox algorithmSelectionComboBox;
    private final JTextField mazeNameTextField, authornameTextField;
    private final JTextArea descriptionTextArea;

    public MazeCreationMenuPartialView(MainView container){
        super(container);
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        setLayout(layout);

        JLabel sizeLabel = new JLabel("Maze Size");
        JLabel algorithmLabel = new JLabel("Algorithm");
        JLabel detailsLabel = new JLabel("Details");

        rowsInput = new JSpinner(new MazeSizeSpinnerModel());
        rowsInput.setValue(10);
        colsInput = new JSpinner(new MazeSizeSpinnerModel());
        colsInput.setValue(10);
        createButton = new JButton("Create");
        createButton.addActionListener(this);

        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        saveButton.setEnabled(false);

        exportButton = new JButton("Export");
        exportButton.addActionListener(this);
        exportButton.setEnabled(false);

        algorithmSelectionComboBox = new JComboBox<>(GeneratorFactory.ALGORITHMS);
        algorithmSelectionComboBox.setSelectedIndex(0);

        mazeNameTextField = new PromptTextField("Maze Name");
        authornameTextField = new PromptTextField("Author Name");
        descriptionTextArea = new PromptTextArea("Description");
        descriptionTextArea.setLineWrap(true);

        layout.linkSize(SwingConstants.VERTICAL, mazeNameTextField, authornameTextField, algorithmSelectionComboBox);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(sizeLabel)
                                .addComponent(algorithmLabel)
                                .addComponent(detailsLabel)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(rowsInput)
                                        .addComponent(colsInput)
                                )
                                .addComponent(algorithmSelectionComboBox)
                        )
                )
                .addComponent(mazeNameTextField)
                .addComponent(authornameTextField)
                .addComponent(descriptionTextArea, 100, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(exportButton)
                        .addComponent(saveButton)
                        .addComponent(createButton)
                )
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(sizeLabel)
                        .addComponent(rowsInput)
                        .addComponent(colsInput)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(algorithmLabel)
                        .addComponent(algorithmSelectionComboBox)
                )
                .addComponent(detailsLabel)
                .addComponent(mazeNameTextField)
                .addComponent(authornameTextField)
                .addComponent(descriptionTextArea, 100, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(createButton)
                        .addComponent(saveButton)
                        .addComponent(exportButton)
                )
        );

        setMinimumSize(new Dimension(authornameTextField.getMinimumSize().width,0));
    }

    public int getRows(){
        return (int) rowsInput.getValue();
    }

    public int getColumns(){
        return (int) colsInput.getValue();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JButton sourceButton = (JButton) e.getSource();
        switch (sourceButton.getText()) {
            case "Create" -> {
//                Main.createMazeView(getRows(), getColumns());
                view.addMazeView(getRows(), getColumns());
                view.setMazeGenerator(algorithmSelectionComboBox.getSelectedIndex());
                view.renderMazeView();
                view.requestFocus();
                saveButton.setEnabled(true);
//                createButton.setEnabled(false);
                createButton.setText("Delete");
                exportButton.setEnabled(true);
//                showGridCheckbox.setEnabled(true);
            }
            case "Delete" -> {
                view.clearMazeView();
                saveButton.setEnabled(false);
//                createButton.setEnabled(true);
                createButton.setText("Create");
                exportButton.setEnabled(false);
//                showGridCheckbox.setEnabled(false);
            }
            case "Export" -> {

            }
        }
    }
}