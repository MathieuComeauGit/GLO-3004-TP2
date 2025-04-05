package thread;

import service.MouleuseService;

import java.util.UUID;

public class MouleuseThread extends Thread {
    private final String id;
    private final UUID machineId;
    private final MouleuseService mouleuseService;

    public MouleuseThread(UUID machineId, String id, MouleuseService service) {
        this.id = id;
        this.machineId = machineId;
        this.mouleuseService = service;
    }

    public String getThreadId() { return id; }

    @Override
    public void run() {
        try {
            while (true) {
                boolean avancé = mouleuseService.avancerEtapeParMachineId(machineId);
                if (avancé) {
                    System.out.println("[MOULEUSE " + id + "] a avancé d'une étape.");
                    Thread.sleep(1000);
                } else {
                    Thread.sleep(500); // Attend un peu si rien à faire
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
