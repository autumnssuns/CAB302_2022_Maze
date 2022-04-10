package Views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPartialView extends JPanel implements ActionListener {
    private static class MazeSizeSpinnerModel extends SpinnerNumberModel{
        MazeSizeSpinnerModel(){
            super(1, 1, 100, 1);
        }
    }

    private final JSpinner rowsInput, colsInput;
    private final JButton createButton, deleteButton, exportButton;
    private final JCheckBox showGridCheckbox;
    private final MainView view;

    public MenuPartialView(MainView container){
        this.view = container;
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        setLayout(layout);

        JLabel label = new JLabel("Models.Maze Size");
        rowsInput = new JSpinner(new MazeSizeSpinnerModel());
        colsInput = new JSpinner(new MazeSizeSpinnerModel());
        createButton = new JButton("Create");
        createButton.addActionListener(this);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this);
        deleteButton.setEnabled(false);

        exportButton = new JButton("Export");
        exportButton.addActionListener(this);
        exportButton.setEnabled(false);

        showGridCheckbox = new JCheckBox("Show Grid");
        showGridCheckbox.setSelected(true);
        showGridCheckbox.setEnabled(false);
        showGridCheckbox.addActionListener(e -> {
            JCheckBox source = (JCheckBox) e.getSource();
            boolean state = (source.getModel().isSelected());
            view.toggleMazeGrid(state);
        });

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(label)
                        .addComponent(showGridCheckbox))
                .addComponent(rowsInput)
                .addComponent(colsInput)
                .addGroup(layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(createButton)
                                .addComponent(deleteButton))
                        .addComponent(exportButton)));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(label)
                        .addComponent(rowsInput)
                        .addComponent(colsInput)
                        .addComponent(createButton)
                        .addComponent(deleteButton))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(showGridCheckbox)
                        .addComponent(exportButton)));
    }

//    public Views.MenuPartialView(Views.View container){
//        this.container = container;
//        setLayout(new FlowLayout());
//
//        JLabel label = new JLabel("Models.Maze Size");
//        rowsInput = new JSpinner(new MazeSizeSpinnerModel());
//        colsInput = new JSpinner(new MazeSizeSpinnerModel());
//        createButton = new JButton("Create");
//        createButton.addActionListener(this);
//
//        deleteButton = new JButton("Delete");
//        deleteButton.addActionListener(this);
//        deleteButton.setEnabled(false);
//
//        add(label);
//        add(rowsInput);
//        add(colsInput);
//        add(createButton);
//        add(deleteButton);
//    }

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
                view.requestFocus();
                view.toggleMazeGrid(showGridCheckbox.getModel().isSelected());
                deleteButton.setEnabled(true);
                createButton.setEnabled(false);
                exportButton.setEnabled(true);
                showGridCheckbox.setEnabled(true);
            }
            case "Delete" -> {
                view.clearMazeView();
                deleteButton.setEnabled(false);
                createButton.setEnabled(true);
                exportButton.setEnabled(false);
                showGridCheckbox.setEnabled(false);
            }
            case "Export" -> {

            }
        }
    }
}
