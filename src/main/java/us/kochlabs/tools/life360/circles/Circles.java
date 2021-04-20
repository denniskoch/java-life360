package us.kochlabs.tools.life360.circles;

public class Circles {

    public Circle[] circles;

    public int Count() {
        return  this.circles.length;
    }

    public Circle getCircle(int index) {
        return circles[index];
    }

    public String getCircleId(int index) {
        return circles[index].id;
    }
}
