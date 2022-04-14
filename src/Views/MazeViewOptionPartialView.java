package Views;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MazeViewOptionPartialView extends PartialView implements ActionListener {
    private final JCheckBox showGridCheckbox, showSolutionCheckbox;

    public MazeViewOptionPartialView(MainView view) {
        super(view);
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        setLayout(layout);

        showGridCheckbox = new JCheckBox("Show Grid");
        showGridCheckbox.setSelected(true);
        view.setShowingGrid(showGridCheckbox.getModel().isSelected());
        showGridCheckbox.addActionListener(this);

        showSolutionCheckbox = new JCheckBox("Show Solution");
        showSolutionCheckbox.setSelected(true);
        view.setShowingSolution(showSolutionCheckbox.getModel().isSelected());
        showSolutionCheckbox.addActionListener(this);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(showGridCheckbox)
                .addComponent(showSolutionCheckbox)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(showGridCheckbox)
                .addComponent(showSolutionCheckbox)
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == showGridCheckbox){
            JCheckBox source = (JCheckBox) e.getSource();
            boolean state = (source.getModel().isSelected());
            view.toggleMazeGrid(state);
        }

        if (e.getSource() == showSolutionCheckbox){
            JCheckBox source = (JCheckBox) e.getSource();
            boolean state = (source.getModel().isSelected());
            view.toggleMazeSolution(state);
        }
    }
}
