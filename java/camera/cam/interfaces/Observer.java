package camera.cam.interfaces;

public interface Observer {
    void register(Subject o);
    void unregister(Subject o);
    void update(int type, double x1, double y1, double x2, double y2);
}
