package uk.co.miami_nice.screenshot.gui;

import uk.co.miami_nice.screenshot.CaptureType;
import uk.co.miami_nice.screenshot.net.UploadManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;

public class RegionSelection extends JFrame {

    public RegionSelection(CaptureType type) {
        // We don't want the min,max, etc
        setUndecorated(true);
        // Transparent
        setBackground(new Color(0, 0, 0, 0)); // DO NOT REMOVE
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new ContentPane(type));

        // Set icon
        setIconImage(new ImageIcon("img/icon.png").getImage());

        // Set cursor
        getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

        // When the escape key is pressed exit
        ActionListener escListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };
        getRootPane().registerKeyboardAction(
                escListener,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        setBounds(getScreenViewableBounds());
        setVisible(true);
    }

    private class ContentPane extends JPanel {

        private Point mouseAnchor;
        private Point dragPoint;

        private SelectionPane selectionPane;

        public ContentPane(final CaptureType type) {
            setOpaque(false);
            setLayout(null);
            selectionPane = new SelectionPane(type);
            add(selectionPane);

            MouseAdapter adapter = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    mouseAnchor = e.getPoint();
                    dragPoint = null;
                    selectionPane.setLocation(mouseAnchor);
                    selectionPane.setSize(0, 0);
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    dragPoint = e.getPoint();
                    int width = dragPoint.x - mouseAnchor.x;
                    int height = dragPoint.y - mouseAnchor.y;

                    int x = mouseAnchor.x;
                    int y = mouseAnchor.y;

                    if (width < 0) {
                        x = dragPoint.x;
                        width *= -1;
                    }
                    if (height < 0) {
                        y = dragPoint.y;
                        height *= -1;
                    }
                    selectionPane.setBounds(x, y, width, height);
                    selectionPane.revalidate();
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    switch (type) {
                        case VIDEO:
                            UploadManager.capture(selectionPane.getBounds(), CaptureType.VIDEO);
                            break;
                        default:
                            getRootPane().setVisible(false);
                            EventQueue.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    UploadManager.capture(selectionPane.getBounds(), CaptureType.IMAGE);
                                }
                            });
                            dispose();
                    }
                }
            };
            addMouseListener(adapter);
            addMouseMotionListener(adapter);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();

            Rectangle bounds = new Rectangle(0, 0, getWidth(), getHeight());
            Area area = new Area(bounds);
            area.subtract(new Area(selectionPane.getBounds()));

            g2d.setColor(new Color(0, 0, 0, 1));
            g2d.fill(area);

        }
    }

    private class SelectionPane extends JPanel {

        private JLabel label;

        private int prevX = 0, prevY = 0;

        public SelectionPane(CaptureType type) {
            setOpaque(false);
            setLayout(new BorderLayout());

            label = new JLabel("", SwingConstants.RIGHT);
            label.setOpaque(false);
            label.setBorder(new EmptyBorder(4, 4, 4, 4));
            label.setForeground(Color.DARK_GRAY);
            add(label, BorderLayout.SOUTH);

            if (type == CaptureType.VIDEO) {
                JButton stop = new JButton("Stop");
                stop.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        UploadManager.getVideoWorker().cancel(true);
                    }
                });
                add(stop, BorderLayout.SOUTH);
            }

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    int x = (prevX == getX()) ? getWidth() : getX();
                    int y = (prevY == getY()) ? getHeight() : getY();
                    label.setText("<html>" + x + "<br />" + y + "</html>");

                    // Store previous X/Y values
                    prevX = getX();
                    prevY = getY();
                }
            });

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            float dash1[] = {10.0f};
            BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setStroke(dashed);
            g2d.drawRect(0, 0, getWidth() - 3, getHeight() - 3);
            g2d.setBackground(Color.GRAY);
            g2d.dispose();
        }
    }

    private static Rectangle getScreenViewableBounds() {
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        int minx = Integer.MAX_VALUE;
        int miny = Integer.MAX_VALUE;
        int maxx = Integer.MIN_VALUE;
        int maxy = Integer.MIN_VALUE;
        for (GraphicsDevice device : devices) {
            for (GraphicsConfiguration config : device.getConfigurations()) {
                Rectangle bounds = config.getBounds();
                minx = Math.min(minx, bounds.x);
                miny = Math.min(miny, bounds.y);
                maxx = Math.max(maxx, bounds.x + bounds.width);
                maxy = Math.max(maxy, bounds.y + bounds.height);
            }
        }
        return new Rectangle(new Point(minx, miny), new Dimension(maxx - minx, maxy - miny));
    }

}
