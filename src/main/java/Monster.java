import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

import java.util.Random;

public class Monster extends Element {

    public Monster(int x, int y) {
        super(x, y);
    }

    public void draw(TextGraphics graphics) {
        graphics.setForegroundColor(TextColor.Factory.fromString("#8B0000"));
        graphics.enableModifiers(SGR.BORDERED);
        graphics.putString(new TerminalPosition(position.getX(), position.getY()), "M");
    }

    public Position move() {
        Random random = new Random();
        int nextX = position.getX() + random.nextInt(2) * (-1) + random.nextInt(2); //Randomly decide between moving left, right or not moving
        int nextY = position.getY() + random.nextInt(2) * (-1) + random.nextInt(2);
        return new Position(nextX, nextY);
    }
}
