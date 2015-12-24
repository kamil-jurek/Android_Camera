package camera.cam;

public class BasePointer extends Pointer {
    private float size;
    public BasePointer(float x1, float y1, float x2, float y2, DisplayAct _activity, int _type, Observer obs, int baseType) {
        super(x1, y1, x2, y2, _activity, _type, obs);

        switch(baseType) {
            case 0 : size = 8.56F;
                break;
            case 1 : size = 5.398F;
                break;
            case 2 : size = 29.7F;
                break;
            case 3 : size = 21.0F;
                break;
        }

    }

    public float getBaseSize() {
        return this.size;
    }
}
