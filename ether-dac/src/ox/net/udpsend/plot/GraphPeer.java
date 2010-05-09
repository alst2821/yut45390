package ox.net.udpsend.plot;


import java.awt.CardLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Color;

/**
 * GraphPeer is a panel with a button and a text field to display a value in the
 * label and select the corresponding graph in the text field
 *
 * @author stcrm
 * @version $Revision: 1.10 $ $Date: 2008-03-17 10:41:03 $
 */
public class GraphPeer extends JPanel implements ActionListener {

    protected Container graphContainer;

    protected String graphKey;

    protected JTextField valueField;

    protected JButton button;

    /**
     * create a GraphPeer button textfield pair for the graph inserted in the
     * graphContainer as identified by its graphKey. The name is displayed in
     * the button. This class expects the container to have a CardLayout as
     * LayoutManager or else the button functionality will not be added.
     *
     * @param graphContainer
     *            contains the graph
     * @param name
     * @param graphKey
     *            identifier to the graphContainers CardLayout manager. It is a
     *            string from "0" to "11"
     * @param panel
     *            TODO
     */
    public GraphPeer(Container graphContainer, String name, String graphKey) {

        this.graphContainer = graphContainer;
        setName(name);
        this.graphKey = graphKey;

        button = new JButton(name);
        // button.setSize(140,26);
        valueField = new JTextField(10);

        setLayout(new GridBagLayout());
        GridBagConstraints con1 = new GridBagConstraints();
        con1.gridx = 0;
        con1.gridy = 0;
        con1.weightx = 1;
        con1.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints con2 = new GridBagConstraints();
        con2.gridx = 1;
        con2.gridy = 0;
        con2.weightx = 0.0;
        con2.fill = GridBagConstraints.HORIZONTAL;
        add(button, con1);
        add(valueField, con2);

        LayoutManager manager = graphContainer.getLayout();

        if (manager instanceof CardLayout) {
            button.setActionCommand(graphKey);
            button.addActionListener(this);
        }
    }

    /* for use of SevaGraphPeer onnly */
    protected GraphPeer() {
    }

    public void actionPerformed(ActionEvent e) {
        selectGraph(e.getActionCommand());
    }

    /**
     * selects a graph page to display, based on whether the corresponding
     * channel is calibrated or not.
     *
     * @param key
     */
    public void selectGraph(String key) {
        CardLayout dispManager = ((CardLayout) graphContainer.getLayout());
        dispManager.show(graphContainer, key);
    }

    public void setText(String text) {
        if (valueField == null) {
            return;
        }
        valueField.setText(text);
    }

    public void setBackgroundValueField(Color bgcolor) {
        if (valueField == null) {
            return;
        }
        valueField.setBackground(bgcolor);
    }

    public void setForegroundValueField(Color fgcolor) {
        if (valueField == null) {
            return;
        }
        valueField.setForeground(fgcolor);
    }

    public void setVisibleValueField(boolean vs) {
        if (valueField == null) {
            return;
        }
        valueField.setVisible(vs);
    }

    public static void main(String[] args){
        JFrame testFrame = new JFrame();
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new FlowLayout());
        contentPane.add(new GraphPeer(contentPane,
                "Validated mixture density [kg/m3]", "key1"));
        contentPane.add(new GraphPeer(contentPane,
                "Validated liqmassflow [kg/s]", "key2"));
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testFrame.setContentPane(contentPane);
        testFrame.pack();
        testFrame.setVisible(true);
    }
}
