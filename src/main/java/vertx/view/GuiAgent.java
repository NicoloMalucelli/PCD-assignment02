package vertx.view;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import utils.*;
import vertx.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class GuiAgent extends AbstractVerticle {

    private Controller controller;
    private Result result;
    private final JFrame frame = new JFrame();
    private final JList<AnalyzedFile> ranking = new JList<>();
    private final JList<String> distribution = new JList<>();
    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        EventBus eb = vertx.eventBus();

        eb.consumer("computation-ended", message -> {
            this.btnStart.setEnabled(true);
            this.btnStop.setEnabled(false);
        });

        eb.consumer("mid-report", message -> {
            DefaultListModel<String> distributionModel = new DefaultListModel<>();
            distributionModel.addAll(result.getDistribution().entrySet().stream()
                    .map(e -> "files of " + e.getKey() + " lines: " + e.getValue())
                    .collect(Collectors.toList()));
            SwingUtilities.invokeLater(() -> {
                distribution.setModel(distributionModel);
            });

            DefaultListModel<AnalyzedFile> rankingModel = new DefaultListModel<>();
            final List<AnalyzedFile> rankingList = result.getRanking();
            rankingModel.addAll(rankingList);

            SwingUtilities.invokeLater(() -> {
                ranking.setModel(rankingModel);
            });
        });
        super.start(startPromise);
    }

    public GuiAgent(Controller controller) {
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
                result = this.controller.analyzeSources(new SetupInfo(
                        txtDirectory.getText(),
                        Integer.parseInt(txtNumOfFiles.getText()),
                        Integer.parseInt(txtNumOfIntervals.getText()),
                        Integer.parseInt(txtLastInterval.getText())), vertx);
            });
        });

        btnStop.addActionListener(e -> {
            /*
            System.out.println("B" + this.controller.getRootId());
            vertx.undeploy(this.controller.getRootId()).onComplete(res -> {
                if (res.succeeded()) {
                    System.out.println("Undeployed ok");
                } else {
                    System.out.println("C" + this.controller.getRootId());
                    System.out.println(res.cause().getMessage());
                    System.out.println("Undeploy failed!");
                }
            });
             */
            vertx.eventBus().publish("stop", "");
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
}
