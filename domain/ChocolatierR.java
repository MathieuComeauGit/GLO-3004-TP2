package domain;
import domain.enums.*;
import java.util.UUID;

public class ChocolatierR {
    private final UUID id;
    private final Provenance provenance;
    private boolean enRupture;
    private EtapeChocolatier etape;
    private Integer mouleuseUtiliseeId;
    public ChocolatierR(Provenance provenance) {
        this.id = UUID.randomUUID();
        this.provenance = provenance;
        this.etape = EtapeChocolatier.AUCUNE;
        this.enRupture = false;
    }

    public UUID getId() {
        return id;
    }

    public Provenance getProvenance() {
        return provenance;
    }

    public boolean isEnRupture() {
        return enRupture;
    }

    public void setEnRupture(boolean rupture) {
        this.enRupture = rupture;
    }

    public EtapeChocolatier getEtape() {
        return etape;
    }

    public void setEtape(EtapeChocolatier etape) {
        this.etape = etape;
    }

    public Integer getMouleuseUtiliseeId() {
        return mouleuseUtiliseeId;
    }

    public void setMouleuseUtiliseeId(Integer mouleuseUtiliseeId) {
        this.mouleuseUtiliseeId = mouleuseUtiliseeId;
    }
}
