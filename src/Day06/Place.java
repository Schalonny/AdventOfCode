package Day06;


public class Place {
     int x;
     int y;
     int area;
     int index;

    public Place(int x, int y, int index) {
        this.x = x;
        this.y = y;
        this.index = index;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "(" + x +
                ", " + y +
                "), area=" + area;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return index == place.index;
    }

    @Override
    public int hashCode() {
        return index;
    }

    public Place(int index) {
        this.index = index;
    }
}
