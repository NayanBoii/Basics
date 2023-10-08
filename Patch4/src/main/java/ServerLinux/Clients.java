package ServerLinux;

import java.net.InetAddress;

public class Clients {
    private InetAddress inetAddress; // This is for the inet address
    private String side; // This is for the side of the client
    private boolean allowed = false;  // This is to check if the client is allowed

    public Clients(InetAddress inetAddress, String side, boolean allowed){
        this.inetAddress = inetAddress;
        this.allowed = allowed;
        this.side = side;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }
}
