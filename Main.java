import com.nijoat.backend.BackendServer;
import com.nijoat.frontend.FrontendServer;

import java.io.IOException;

public class Main {
    public static void main (String[] args) {
        Thread backendThread = new Thread(() -> {
            System.out.println("Starting backend server...");
            BackendServer.main(new String[]{});
        });
        backendThread.start();

        Thread frontendThread = new Thread(() -> {
            System.out.println("Starting frontend server...");
            FrontendServer.main(new String[]{});
        });
        frontendThread.start();

        try {
            backendThread.join();
            frontendThread.join();
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted: " + e.getMessage());
        }
        System.out.println("Both servers started successfully!");
    }

}
