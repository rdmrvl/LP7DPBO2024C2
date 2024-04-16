import java.awt.*;

public class Pipe {
    private int posX;
    private int posY;
    private int widht;
    private int height;
    private Image image;
    private int velocityX;
    boolean passed = false;

    public Pipe(int posX, int posY, int widht, int height, Image image) {
        this.posX = posX;
        this.posY = posY;
        this.widht = widht;
        this.height = height;
        this.image = image;

        this.velocityX = -3;
        this.passed = false;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getWidht() {
        return widht;
    }

    public void setWidht(int widht) {
        this.widht = widht;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}