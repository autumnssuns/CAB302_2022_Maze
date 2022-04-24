package Views;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MazeViewOptionPartialView extends PartialView implements ActionListener {
    private final JCheckBox showCreationAnimationCheckbox, showGridCheckbox, showSolutionCheckbox;

    public MazeViewOptionPartialView(MainView view) {
        super(view);
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        setLayout(layout);

        showCreationAnimationCheckbox = new JCheckBox("Show Creation Animation");
        showCreationAnimationCheckbox.setSelected(true);
        view.setShowingAnimation(showCreationAnimationCheckbox.getModel().isSelected());
        showCreationAnimationCheckbox.addActionListener(this);

        showGridCheckbox = new JCheckBox("Show Grid");
        showGridCheckbox.setSelected(true);
        view.setShowingGrid(showGridCheckbox.getModel().isSelected());
        showGridCheckbox.addActionListener(this);

        showSolutionCheckbox = new JCheckBox("Show Solution");
        showSolutionCheckbox.setSelected(true);
        view.setShowingSolution(showSolutionCheckbox.getModel().isSelected());
        showSolutionCheckbox.addActionListener(this);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(showCreationAnimationCheckbox)
                .addComponent(showGridCheckbox)
                .addComponent(showSolutionCheckbox)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(showCreationAnimationCheckbox)
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

        if (e.getSource() == showCreationAnimationCheckbox){
            JCheckBox source = (JCheckBox) e.getSource();
            boolean state = (source.getModel().isSelected());
            view.toggleAnimation(state);
        }
    }
}
