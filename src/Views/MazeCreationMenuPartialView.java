package Views;

import Controllers.MazeCreationController;
import Generators.GeneratorFactory;
import Models.Maze;
import Models.MazeDataModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MazeCreationMenuPartialView extends PartialView implements ActionListener {
    private static class MazeSizeSpinnerModel extends SpinnerNumberModel{
        MazeSizeSpinnerModel(){
            super(10, 1, 100, 1);
        }
    }

    private final JSpinner rowsInput, colsInput;
    private final JButton createButton, saveButton, exportButton;
    private final JComboBox algorithmSelectionComboBox;
    private final PromptTextField mazeNameTextField, authornameTextField, seedTextField;
    private final PromptTextArea descriptionTextArea;

    public void loadMaze(MazeDataModel mazeModel){
        rowsInput.setValue(mazeModel.rowsCount());
        colsInput.setValue(mazeModel.colsCount());
        algorithmSelectionComboBox.setSelectedIndex(mazeModel.algorithm());
        seedTextField.writeText(String.valueOf(mazeModel.seed()));
        mazeNameTextField.writeText(mazeModel.name());
        authornameTextField.writeText(mazeModel.author());
        descriptionTextArea.writeText(mazeModel.description());
        view.requestFocus();
        saveButton.setEnabled(true);
//                createButton.setEnabled(false);
        createButton.setText("Delete");
        exportButton.setEnabled(true);

        Maze maze = mazeModel.unpack();
        maze.setLocked(true);
        createMaze(maze);
    }

    public MazeCreationMenuPartialView(MainView container){
        super(container);
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        setLayout(layout);

        JLabel sizeLabel = new JLabel("Maze Size");
        JLabel algorithmLabel = new JLabel("Algorithm");
        JLabel detailsLabel = new JLabel("Details");
        JLabel seedLabel = new JLabel("Seed");

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

        seedTextField = new PromptTextField("Seed");

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
                                .addComponent(seedLabel)
                                .addComponent(detailsLabel)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(rowsInput)
                                        .addComponent(colsInput)
                                )
                                .addComponent(algorithmSelectionComboBox)
                                .addComponent(seedTextField)
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
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(seedLabel)
                        .addComponent(seedTextField)
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

    private void createMaze(Maze maze){
        view.addMazeView(maze);
        long seed;
        if (seedTextField.getText().isEmpty()){
            seed = new Random().nextLong();
            seedTextField.setPromptText(String.valueOf(seed));
        } else seed = Long.parseLong(seedTextField.getText());

        view.setMazeGenerator(algorithmSelectionComboBox.getSelectedIndex(), seed);
        view.renderMazeView();
        view.requestFocus();
        saveButton.setEnabled(true);
//                createButton.setEnabled(false);
        createButton.setText("Delete");
        exportButton.setEnabled(true);
//                showGridCheckbox.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton sourceButton = (JButton) e.getSource();
        switch (sourceButton.getText()) {
            case "Create" -> {
//                Main.createMazeView(getRows(), getColumns());
                Maze maze = new Maze(getRows(), getColumns());
                createMaze(maze);
            }
            case "Delete" -> {
                view.clearMazeView();
                saveButton.setEnabled(false);
//                createButton.setEnabled(true);
                createButton.setText("Create");
                exportButton.setEnabled(false);
//                showGridCheckbox.setEnabled(false);
            }
            case "Save" -> {
                String name = mazeNameTextField.getText();
                String author = authornameTextField.getText();
                String description = descriptionTextArea.getText();

                Maze maze = view.getMaze();
                MazeCreationController.addMaze(name, author, description, maze);
                view.reloadSavedMazes();
            }
        }
    }
}
