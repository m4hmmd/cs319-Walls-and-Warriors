import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MyComponents extends JComponent {

    private Model model;
    private CardLayout cardLayout;
    private JPanel card;
    int levelNo;

    private int pWidth = getWidth();
    private int pHeight = getHeight();

    public MyComponents(Model model, CardLayout cardLayout, JPanel card, int levelNo) {
        this.model = model;
        this.cardLayout = cardLayout;
        this.card = card;
        this.levelNo = levelNo;

        MyCircleMouseListener listener = new MyCircleMouseListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.gray);

        if (pWidth != getWidth() || pHeight != getHeight()) {
            centerTheGame(getWidth(), getHeight());
        }

        int fontSize = 20;
        g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
        g.setColor(Color.gray.darker());
        g.drawString("LEVEL " + levelNo, getWidth() / 2 - 45, 50);
        drawGrid(g);
        drawSoldiers(g);
        model.getCastle().draw(g, model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(), model.getSquareWidth());
        drawWalls(g);
        drawWallOptions(g);

        pWidth = getWidth();
        pHeight = getHeight();
    }

    private void centerTheGame(int width, int height) {
        int gameWidth = model.getSquareWidth() * model.getMapWidth();
        int gameHeight = (model.getSquareHeight()) * (model.getMapLength() + 2);

        model.setInitialXShift((width - gameWidth) / 2);

        if ((height - gameHeight) / 2 > model.getBarShift()) {
            model.setInitialYShift((height - gameHeight) / 2);
        } else {
            model.setInitialYShift(model.getBarShift());
        }

        model.setSizesOfObjects();
        model.rearrangeWalls();
    }

    void drawGrid(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(model.initialXShift + model.squareWidth - model.lineWidth / 2, model.initialYShift - model.lineWidth / 2, model.squareWidth * (model.mapWidth - 2) + model.lineWidth, model.squareHeight * model.mapLength + model.lineWidth);
        g.fillRect(model.initialXShift - model.lineWidth / 2, model.initialYShift + model.squareHeight - model.lineWidth / 2, model.squareWidth * model.mapWidth + model.lineWidth, model.squareHeight * (model.mapLength - 2) + model.lineWidth);

        for (int i = 0; i < model.mapWidth; i++) {
            for (int j = 0; j < model.mapLength; j++) {
                if (i == 0 && j == 0) {
                } else if (i == model.mapWidth - 1 && j == 0) {
                } else if (i == 0 && j == model.mapLength - 1) {
                } else if (i == model.mapWidth - 1 && j == model.mapLength - 1) {
                } else {
                    g.setColor(Color.gray.brighter());
                    g.fillRect(model.initialXShift + model.squareWidth * i + model.lineWidth / 2, model.initialYShift + model.squareHeight * j + model.lineWidth / 2, model.squareWidth - model.lineWidth + 1, model.squareHeight - model.lineWidth + 1);
                }
            }
        }
    }

    void drawWallOptions(Graphics g) {
        g.setColor(Color.green);
        for (int i = 0; i < model.getWalls().length; i++) {
            Rectangle r = model.getWalls()[i].wallContainer;
            g.drawRect(r.x, r.y, r.width, r.height);
        }
    }

    void drawSoldiers(Graphics g) {
        for (int i = 0; i < model.getSoldiers().length; i++) {
            model.getSoldiers()[i].draw(g, model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(), model.getSquareWidth());
        }
    }

    void drawWalls(Graphics g) {
        for (int i = 0; i < model.getWalls().length; i++) {
            model.getWalls()[i].draw(g, model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(), model.getSquareWidth());
        }
    }

    class MyCircleMouseListener implements MouseListener, MouseMotionListener {

        Wall toBeDragged;
        int indexOfGreenSquare;
        int wallStartX;
        int wallStartY;
        int clickedX;
        int clickedY;
        boolean wasInvisible;

        public void mouseClicked(MouseEvent e) {
            int i = 0;
            for (Wall wall : model.getWalls()) {
                Rectangle r = wall.wallContainer;
                if (r.contains(e.getX(), e.getY()) && !model.getWalls()[i].visible) {
                    model.getWalls()[i].turnLeft();
                    model.getWalls()[i].setRectangles();
                }
                i++;
            }

            repaint();
        }

        public void mousePressed(MouseEvent e) {
            clickedX = e.getX();
            clickedY = e.getY();
            boolean isWall = false;
            for (Wall w : model.getWalls()) {
                if (w.visible && w.contains(e.getX(), e.getY())) {
                    toBeDragged = w;
                    model.removeFromLines(toBeDragged);
                    wallStartX = toBeDragged.xCoor;
                    wallStartY = toBeDragged.yCoor;
                    isWall = true;
                    break;
                }
            }

            if (!isWall) {
                for (int i = 0; i < model.getWalls().length; i++) {

                    if (model.getWalls()[i].wallContainer.contains(clickedX, clickedY) && !model.getWalls()[i].visible) {
                        model.getWalls()[i].appear();
                        toBeDragged = model.getWalls()[i];
                        indexOfGreenSquare = i;
                        wallStartX = (int) model.getWalls()[i].wallContainer.getCenterX();
                        wallStartY = (int) model.getWalls()[i].wallContainer.getCenterY();
                        wasInvisible = true;
                    }
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int a = 0;
            if (toBeDragged != null) {
                toBeDragged.xCoor = wallStartX + e.getX() - clickedX;
                toBeDragged.yCoor = wallStartY + e.getY() - clickedY;
            }
            repaint();
        }

        public void mouseReleased(MouseEvent e) {
            if (toBeDragged != null) {
                toBeDragged.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(), model.getSquareWidth());

                if (model.outOfScreen(toBeDragged)) {
                    toBeDragged.remove();
                } else {
                    if (!model.onAvailablePlace(toBeDragged)) {
                        if (wasInvisible) {
                            toBeDragged.remove();
                        } else {
                            toBeDragged.xCoor = wallStartX;
                            toBeDragged.yCoor = wallStartY;
                        }
                    } else {
                        toBeDragged.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(), model.getSquareWidth());
                        model.addToLines(toBeDragged);
                        SoundManager.wallPlaced();

                        if (model.isGameFinished()) {
                            repaint();
                            
                            SoundManager.gameWon();

                            Object[] options = {"Return Home", "Next Level"};

                            int n = JOptionPane.showOptionDialog(null,
                                    "You have completed this level!",
                                    "Congratulations!!",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    options,
                                    options[1]);
                            
                            GameView.lastCompletedLevel++;
                            GameView.levelButtons[levelNo].setEnabled(true);
                            if (n == JOptionPane.YES_OPTION) {
                                cardLayout.show(card, "Game Menu");
                                model.reset();
                            } else if (n == JOptionPane.NO_OPTION) {
                                cardLayout.show(card, "Level " + (levelNo + 1));
                                model.reset();
                            }
                        }
                    }
                }
            }
            wasInvisible = false;
            toBeDragged = null;
            repaint();
        }

        public void mouseExited(MouseEvent e) {

        }

        public void mouseMoved(MouseEvent e) {

        }

        public void mouseEntered(MouseEvent e) {

        }
    }
}
