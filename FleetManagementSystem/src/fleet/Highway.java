package fleet;

public class Highway {
    private int totalDistance = 0;
    
    private boolean synchronizedMode = false;

    public void setSynchronizedMode(boolean enable) {
        this.synchronizedMode = enable;
    }

    public int getTotalDistance() {

        return totalDistance;

    }

    public void reset() {
        totalDistance = 0;

    }

    public void addDistance(int distance) {
        if (synchronizedMode) {
            synchronized (this) {
                totalDistance += distance;
            }
        } else {
            int temp = totalDistance;
            try { 
                Thread.sleep(50); 
            } catch (InterruptedException e) {}
            totalDistance = temp + distance;
        }

    }



}