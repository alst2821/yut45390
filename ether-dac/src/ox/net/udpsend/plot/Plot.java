package ox.net.udpsend.plot;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import emJava20.emDisplays.emTimeGraph;

import ox.net.udpsend.Packet;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class Plot extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JPanel exitButtonPanel = null;

    private JButton exitButton = null;

    private JPanel jPanel1 = null;

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getExitButtonPanel() {
        if (exitButtonPanel == null) {
            exitButtonPanel = new JPanel();
            exitButtonPanel.setLayout(new FlowLayout());
            exitButtonPanel.add(getExitButton(), null);
        }
        return exitButtonPanel;
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private JButton getExitButton() {
        if (exitButton == null) {
            exitButton = new JButton();
            exitButton.setText("Exit");
            exitButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent event){
                    stopProcessing();
                    try {
                        Thread.sleep(500);
                    }
                    catch (InterruptedException exc){
                        exc.printStackTrace();
                    }
                    System.exit(0);
                }
            });
        }
        return exitButton;
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            GridBagConstraints seqPaneConstraints = new GridBagConstraints();
            seqPaneConstraints.gridx = 0;
            seqPaneConstraints.gridy = 1;
            seqPaneConstraints.fill = GridBagConstraints.HORIZONTAL;
            GridBagConstraints graphPaneConstraints = new GridBagConstraints();
            graphPaneConstraints.gridx = 0;
            graphPaneConstraints.fill = GridBagConstraints.BOTH;
            graphPaneConstraints.gridy = 0;
            GridBagConstraints buttonPaneConstraints = new GridBagConstraints();
            buttonPaneConstraints.fill = GridBagConstraints.HORIZONTAL;
            buttonPaneConstraints.gridx = 0;
            buttonPaneConstraints.gridy = 2;
            buttonPaneConstraints.weightx = 1.0;
            jPanel1 = new JPanel();
            jPanel1.setLayout(new GridBagLayout());

            jPanel1.setPreferredSize(new Dimension(180, 199));
            jPanel1.add(getGraphPane(), graphPaneConstraints);
            jPanel1.add(getSequencePane(), seqPaneConstraints);
            jPanel1.add(getButtonPane(), buttonPaneConstraints);

        }
        return jPanel1;
    }

    private boolean loopForever = true;
    private void stopProcessing(){
        loopForever = false;
    }

    /**
     *
     */
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            stopProcessing();
            try {
            Thread.sleep(500);
            }
            catch (InterruptedException exc){
                exc.printStackTrace();
            }
        }
        super.processWindowEvent(e);
    }

    public void updatePlots(Packet p){
        for (int i = 0; i < Packet.VAL24_COUNT; i++) {
            float sample = p.getCurrent24Sample(i, 100.0f);
            emTimeGraph[] tg = getTimeGraph();
            tg[i].setCurrentSample(0, sample);
            GraphPeer[] gp = getGraphPeer();
            gp[i].setText(String.valueOf(sample));
        }
        getSequence().setText(String.valueOf(p.getSequence()));
    }

    public static final int SINK_PORT = ox.net.udpsend.simulator.Sink.SINK_PORT;

    private JPanel graphPane = null;

    private JPanel buttonPane = null;

    private emTimeGraph[] timeGraph = null;

    private static final int VAL24_COUNT = Packet.VAL24_COUNT;
    private GraphPeer[] graphPeer = null;

    private JPanel sequencePane = null;

    private JLabel jLabel = null;

    private JTextField sequence = null;

    /**
     * This method initializes graphPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getGraphPane() {
        if (graphPane == null) {
            graphPane = new JPanel();
            graphPane.setLayout(new CardLayout());
            emTimeGraph[] tg = getTimeGraph();
            for (int i = 0; i < tg.length; i++) {
                graphPane.add(tg[i], tg[i].getName());
            }
        }
        return graphPane;
    }

    /**
     * This method initializes buttonPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getButtonPane() {
        if (buttonPane == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 1;
            gridBagConstraints3.gridy = 0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 0;
            buttonPane = new JPanel();

            buttonPane.setLayout(new GridLayout(9,4,5,2));
//             32 buttons arranged in rows of four
            GraphPeer[] gp = getGraphPeer();
            for (int i = 0; i< gp.length; i++) {
                buttonPane.add(gp[i]);
            }
        }
        return buttonPane;
    }

    /**
     * This method initializes emTimeGraph
     *
     * @return emJava20.emDisplays.emTimeGraph
     */
    private emTimeGraph[] getTimeGraph() {
        if (timeGraph == null) {
            timeGraph = new emTimeGraph[VAL24_COUNT];
            for (int i = 0; i < VAL24_COUNT; i++){
                String label = "Value " + String.valueOf(i+1) + "/24";
                String key = "graph-key-" + String.valueOf(i+1);
                timeGraph[i] = new emTimeGraph();
                timeGraph[i].setAutoSequence(true);
                timeGraph[i].setLabelVisible(true);
                timeGraph[i].setLabel(label);
                timeGraph[i].setYMax(100.0f);
                timeGraph[i].setYMin(0.0f);

                timeGraph[i].setNumberGraphs(1);
                timeGraph[i].setGraphVisible(0, true);
                timeGraph[i].setGraphColor(0, Color.red);

                timeGraph[i].setTickVisible(true);
                timeGraph[i].setYTickFrequency(20);
                timeGraph[i].setName(key);
            }

        }
        return timeGraph;
    }

    /**
     * This method initializes graphPeer1
     *
     * @return ox.net.udpsend.plot.GraphPeer
     */
    private GraphPeer[] getGraphPeer() {
        if (graphPeer == null) {
            graphPeer = new GraphPeer[VAL24_COUNT];
            for (int i = 0; i < VAL24_COUNT; i++){
                String name = "Value " + String.valueOf(i + 1);
                String key = "graph-key-" + String.valueOf(i+1);
                graphPeer[i] = new GraphPeer(getGraphPane(), name, key);
            }
        }
        return graphPeer;
    }

    /**
     * This method initializes sequencePane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getSequencePane() {
        if (sequencePane == null) {
            jLabel = new JLabel();
            jLabel.setText("Sequence number:");
            sequencePane = new JPanel();
            sequencePane.setLayout(new FlowLayout());
            sequencePane.add(jLabel);
            sequencePane.add(getSequence());
        }
        return sequencePane;
    }

    /**
     * This method initializes sequence
     *
     * @return javax.swing.JTextField
     */
    private JTextField getSequence() {
        if (sequence == null) {
            sequence = new JTextField();
            sequence.setPreferredSize(new Dimension(80, 20));
        }
        return sequence;
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {

        final Plot thePlot = new Plot();

        final Runnable sink = new Runnable(){
            public void run(){

                try {
                DatagramSocket sock = new DatagramSocket(SINK_PORT);
                byte[] data = new byte[100];
                DatagramPacket pack = new DatagramPacket(data, data.length);
                System.out.println("Sink thread processing started.");
                while (thePlot.loopForever) {
                    sock.receive(pack);
                    byte[] recvdata = pack.getData();
                    Packet pp = new Packet(recvdata);
                    thePlot.updatePlots(pp);
                }
                System.out.println("Sink thread processing stopped.");
                }
                catch (IOException exc) {
                    exc.printStackTrace();
                }

            }
        };

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                thePlot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                thePlot.setVisible(true);
                Thread sinkThread = new Thread(sink);
                sinkThread.start();
            }
        });

    }

    /**
     * This is the default constructor
     */
    public Plot() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setSize(988, 538);
        this.setPreferredSize(new Dimension(988, 538));
        this.setContentPane(getJContentPane());
        this.setTitle("Packets on port " + SINK_PORT);
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getExitButtonPanel(), BorderLayout.SOUTH);
            jContentPane.add(getJPanel1(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
