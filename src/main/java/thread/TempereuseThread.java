package thread;

import service.TempereuseService;

import java.util.UUID;

public class TempereuseThread extends Thread {
    private final String id;
    private final UUID machineId;
    private final TempereuseService tempereuseService;

    public TempereuseThread(UUID machineId, String id, TempereuseService service) {
        this.id = id;
        this.machineId = machineId;
        this.tempereuseService = service;
    }

    public String getThreadId() { return id; }

    @Override
    public void run() {
        try {
            while (true) {
                boolean avancé = tempereuseService.avancerEtapeParMachineId(machineId);
                if (avancé) {
                    System.out.println("[TEMPEREUSE " + id + "] a avancé d'une étape.");
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
