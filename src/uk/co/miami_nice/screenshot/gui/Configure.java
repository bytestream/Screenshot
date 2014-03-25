package uk.co.miami_nice.screenshot.gui;

import com.google.gson.Gson;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import uk.co.miami_nice.screenshot.Config;
import uk.co.miami_nice.screenshot.misc.Misc;
import uk.co.miami_nice.screenshot.net.Uploader;
import uk.co.miami_nice.screenshot.util.JavaClassFinder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Configure extends JDialog {

    private JPanel contentPane;
    private JButton buttonSave;
    private JTabbedPane tabbedPane;
    private JLabel configureLabel;
    private JLabel uploaderLabel;
    private JComboBox uploadDropdown;
    private JLabel imageFormatLabel;
    private JComboBox imageFormatDropdown;
    private JLabel configureDescLabel;
    private JLabel aboutLabel;
    private JLabel autoUploadLabel;
    private JRadioButton yesRadioButton;
    private JRadioButton noRadioButton;
    private JLabel outputLabel;
    private JTextField outputDirectory;

    public Configure() {
        $$$setupUI$$$();
        setTitle("Configure");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        setContentPane(contentPane);
        setModal(true);

        // Add image formats
        String[] unique = Misc.unique(ImageIO.getWriterFormatNames());
        for (String format : unique) {
            imageFormatDropdown.addItem(format);

            if (format.equalsIgnoreCase(Config.getImageFormat()))
                imageFormatDropdown.setSelectedItem(format);
        }

        // Add uploaders
        JavaClassFinder classFinder = new JavaClassFinder();
        List<Class<? extends Uploader>> classes = classFinder.findAllMatchingTypes(Uploader.class);
        for (Class c : classes) {
            try {
                Method m = c.getMethod("getName", null);
                String s;
                uploadDropdown.addItem(s = (String) m.invoke(c.newInstance()));

                if (s.equalsIgnoreCase(Config.getUploadMethod()))
                    uploadDropdown.setSelectedItem(s);
            } catch (Exception e) {
            }
        }

        // Set output directory
        outputDirectory.setText(Config.getOutputDirectory());

        // Set output directory
        outputDirectory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fc.showOpenDialog(new JPanel());

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    outputDirectory.setText(file.getAbsolutePath());
                } else {
                    outputDirectory.setText(System.getProperty("java.io.tmpdir"));
                }
            }
        });

        // Save the configuration
        buttonSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Config.setImageFormat((String) imageFormatDropdown.getSelectedItem());
                Config.setUploadMethod((String) uploadDropdown.getSelectedItem());
                Config.setAutoUpload(yesRadioButton.isSelected());
                Config.setOutputDirectory(outputDirectory.getText());

                // Save config
                Gson gson = new Gson();
                Collection collection = new ArrayList();
                collection.add(Config.getImageFormat());
                collection.add(Config.getUploadMethod());
                collection.add(Config.isAutoUpload());
                collection.add(Config.getOutputDirectory());
                String json = gson.toJson(collection);

                try {
                    //write converted json data to a file named "file.json"
                    FileWriter writer = new FileWriter("config.json");
                    writer.write(json);
                    writer.close();
                    JOptionPane.showMessageDialog(new JPanel(),
                            "Success", "Successfully updated configuration.", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(new JPanel(),
                            "Error", ioe.getMessage(), JOptionPane.ERROR_MESSAGE);
                    ioe.printStackTrace();
                }
            }
        });

        pack();
        setVisible(true);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        tabbedPane = new JTabbedPane();
        panel1.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(324, 200), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(7, 4, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Configure", panel2);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4), null));
        configureLabel = new JLabel();
        configureLabel.setFont(new Font(configureLabel.getFont().getName(), Font.BOLD, 18));
        configureLabel.setText("Configure");
        panel2.add(configureLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        imageFormatLabel = new JLabel();
        imageFormatLabel.setFont(new Font(imageFormatLabel.getFont().getName(), imageFormatLabel.getFont().getStyle(), imageFormatLabel.getFont().getSize()));
        imageFormatLabel.setText("Image Format:");
        panel2.add(imageFormatLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        uploaderLabel = new JLabel();
        uploaderLabel.setFont(new Font(uploaderLabel.getFont().getName(), uploaderLabel.getFont().getStyle(), uploaderLabel.getFont().getSize()));
        uploaderLabel.setText("Upload Method:");
        panel2.add(uploaderLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        imageFormatDropdown = new JComboBox();
        panel2.add(imageFormatDropdown, new GridConstraints(3, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        uploadDropdown = new JComboBox();
        panel2.add(uploadDropdown, new GridConstraints(4, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        configureDescLabel = new JLabel();
        configureDescLabel.setText("<html>Below you can configure the software to methods of your choosing, <br />such as the file format to use when saving images and where to upload images to.</html>");
        panel2.add(configureDescLabel, new GridConstraints(1, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSave = new JButton();
        buttonSave.setText("Save");
        panel2.add(buttonSave, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(6, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, new Dimension(318, 11), null, 0, false));
        autoUploadLabel = new JLabel();
        autoUploadLabel.setText("Automatically Upload:");
        panel2.add(autoUploadLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        yesRadioButton = new JRadioButton();
        yesRadioButton.setSelected(true);
        yesRadioButton.setText("Yes");
        panel2.add(yesRadioButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(98, 28), null, 0, false));
        noRadioButton = new JRadioButton();
        noRadioButton.setText("No");
        panel2.add(noRadioButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        outputLabel = new JLabel();
        outputLabel.setFont(new Font(outputLabel.getFont().getName(), outputLabel.getFont().getStyle(), outputLabel.getFont().getSize()));
        outputLabel.setText("Output Directory:");
        panel2.add(outputLabel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        outputDirectory = new JTextField();
        outputDirectory.setEditable(false);
        outputDirectory.setEnabled(true);
        panel2.add(outputDirectory, new GridConstraints(5, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("About", panel3);
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4), null));
        aboutLabel = new JLabel();
        aboutLabel.setFont(new Font(aboutLabel.getFont().getName(), Font.BOLD, 18));
        aboutLabel.setText("About");
        panel3.add(aboutLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("<html>This a multi-platform open-source screen capture application.<br />The application can be used to capture video or static images<br /> and upload these to a hosting site of your choice.<br /><br /><b>Contact:</b><br /><a href=\"mailto:kieran@miami-nice.co.uk\">kieran@miami-nice.co.uk</a><br /><a href=\"http://github.com/bytestream/Screenshot\">Source Code</a></html>");
        panel3.add(label1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(noRadioButton);
        buttonGroup.add(yesRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
