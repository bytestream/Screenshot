package uk.co.miami_nice.screenshot.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;

public class RegionSelection extends JFrame {

    public static void main(String[] args) {
        new RegionSelection();
    }

    public RegionSelection() {
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0)); // DO NOT REMOVE
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(new ContentPane());
        setBounds(getScreenViewableBounds());
        setVisible(true);
    }

    public class ContentPane extends JPanel {

        private Point mouseAnchor;
        private Point dragPoint;

        private SelectionPane selectionPane;

        public ContentPane() {
            setOpaque(false);
            setLayout(null);
            selectionPane = new SelectionPane();
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

    public class SelectionPane extends JPanel {

        private JLabel label;

        public SelectionPane() {
            setOpaque(false);
            setLayout(new BorderLayout());

            label = new JLabel("", SwingConstants.RIGHT);
            label.setOpaque(false);
            label.setBorder(new EmptyBorder(4, 4, 4, 4));
            label.setForeground(Color.DARK_GRAY);
            add(label, BorderLayout.SOUTH);

//            button.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    SwingUtilities.getWindowAncestor(SelectionPane.this).dispose();
//                }
//            });

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    label.setText("<html>" + getX() + "<br />" + getY() + "</html>");
                }
            });

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            // I've chosen NOT to fill this selection rectangle, so that
            // it now appears as if you're "cutting" away the selection
//            g2d.setColor(new Color(128, 128, 128, 64));
//            g2d.fillRect(0, 0, getWidth(), getHeight());

            float dash1[] = {10.0f};
            BasicStroke dashed =
                    new BasicStroke(3.0f,
                            BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER,
                            10.0f, dash1, 0.0f);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(dashed);
            g2d.drawRect(0, 0, getWidth() - 3, getHeight() - 3);
            g2d.dispose();
        }
    }

    public static Rectangle getScreenViewableBounds() {
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
