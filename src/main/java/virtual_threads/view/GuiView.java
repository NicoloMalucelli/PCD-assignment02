package virtual_threads.view;

import utils.ResultObserver;
import virtual_threads.controller.Controller;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class GuiView implements ResultObserver {

    private Controller controller;
    private final JFrame frame = new JFrame();
    private final JList<AnalyzedFile> ranking = new JList<>();
    private final JList<String> distribution = new JList<>();
    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");


    public GuiView(Controller controller) {
        this.controller = controller;
        this.frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        this.frame.setSize(800, 500);

        final JPanel controlPanel = new JPanel();
        final JPanel inputPanel = new JPanel();

        final JLabel lblDirectory = new JLabel("Directory");
        final JTextField txtDirectory = new JTextField(20);
        txtDirectory.setText("C:\\Users\\nicol\\Documents\\Progetti");
        inputPanel.add(lblDirectory);
        inputPanel.add(txtDirectory);

        final JLabel lblNumOfFiles = new JLabel("Number of files to visualize");
        final JTextField txtNumOfFiles = new JTextField(3);
        txtDirectory.setSize(40, 7);
        inputPanel.add(lblNumOfFiles);
        inputPanel.add(txtNumOfFiles);

        final JLabel lblNumOfIntervals = new JLabel("Number of intervals");
        final JTextField txtNumOfIntervals = new JTextField(3);
        txtDirectory.setSize(40, 7);
        inputPanel.add(lblNumOfIntervals);
        inputPanel.add(txtNumOfIntervals);

        final JLabel lblLastInterval = new JLabel("Last interval max");
        final JTextField txtLastInterval = new JTextField(3);
        txtDirectory.setSize(40, 7);
        inputPanel.add(lblLastInterval);
        inputPanel.add(txtLastInterval);

        controlPanel.add(btnStart);

        btnStop.setEnabled(false);
        controlPanel.add(btnStop);

        btnStart.addActionListener(e -> {
            if (txtDirectory.getText().isEmpty()) {
                Toast.makeToast(frame, "directory text field is empty", new Color(255, 0, 0, 170), 3);
                return;
            }
            if (txtLastInterval.getText().isEmpty() || !Strings.isNumeric(txtLastInterval.getText()) || Integer.parseInt(txtLastInterval.getText()) <= 0) {
                Toast.makeToast(frame, "last interval empty or not numeric", new Color(255, 0, 0, 170), 3);
                return;
            }
            if (txtNumOfFiles.getText().isEmpty() || !Strings.isNumeric(txtNumOfFiles.getText()) || Integer.parseInt(txtNumOfFiles.getText()) <= 0) {
                Toast.makeToast(frame, "number of files to visualize empty or not numeric", new Color(255, 0, 0, 170), 3);
                return;
            }
            if (txtNumOfIntervals.getText().isEmpty() || !Strings.isNumeric(txtNumOfIntervals.getText()) || Integer.parseInt(txtNumOfIntervals.getText()) <= 0) {
                Toast.makeToast(frame, "number of intervals empty or not numeric", new Color(255, 0, 0, 170), 3);
                return;
            }
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);

            ranking.setModel(new DefaultListModel<>());
            distribution.setModel(new DefaultListModel<>());

            this.controller.processEvent(() -> {
                SetupInfo setupInfo = new SetupInfo(txtDirectory.getText(),
                        Integer.parseInt(txtNumOfFiles.getText()),
                        Integer.parseInt(txtNumOfIntervals.getText()),
                        Integer.parseInt(txtLastInterval.getText()));
                CompletableFuture<Void> executionEnded = new CompletableFuture<>();
                executionEnded.thenRun(() -> {
                    btnStart.setEnabled(true);
                    btnStop.setEnabled(false);
                });
                Result result = this.controller.analyzeSources(setupInfo, executionEnded);
                result.listen(this);
                //this.controller.startScan(setupInfo);
            });
        });

        btnStop.addActionListener(e -> {
            this.controller.stopExecution();
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
        });

        mainPanel.add(inputPanel);
        mainPanel.add(controlPanel);

        final JPanel showPanel = new JPanel();

        showPanel.add(new JScrollPane(distribution));

        showPanel.add(new JScrollPane(ranking));

        mainPanel.add(showPanel);

        this.frame.setContentPane(mainPanel);
        this.frame.setVisible(true);
    }

    @Override
    public void resultUpdated(Result updatedMidReport) {
        DefaultListModel<String> distributionModel = new DefaultListModel<>();
        distributionModel.addAll(updatedMidReport.getDistribution().entrySet().stream()
                .map(e -> "files of " + e.getKey() + " lines: " + e.getValue())
                .collect(Collectors.toList()));
        SwingUtilities.invokeLater(() -> {
            distribution.setModel(distributionModel);
        });

        DefaultListModel<AnalyzedFile> rankingModel = new DefaultListModel<>();
        final List<AnalyzedFile> rankingList = updatedMidReport.getRanking();
        rankingModel.addAll(rankingList);

        SwingUtilities.invokeLater(() -> {
            ranking.setModel(rankingModel);
        });
    }
}
