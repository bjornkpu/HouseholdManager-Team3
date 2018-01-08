package minpakke;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Path("/kunder/")
public class KundeService {
    private static Map<String,Kunde> kunder = new HashMap<>();
    @GET
    @Path("/{kundeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Kunde getKunde(@PathParam("kundeId") String kundeId) {
        if(kunder.get(kundeId)!=null){
            return kunder.get(kundeId);
        }else {
            throw new javax.ws.rs.NotFoundException();
        }

    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Kunde> getKunder() {
        return kunder.values();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addKunde(Kunde kunde) {
        kunder.put(kunde.getId(), kunde);
    }

    @DELETE
    @Path("/{kundeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteKunde(@PathParam("kundeId") String kundeId){
        kunder.remove(kundeId);
    }

    @PUT
    @Path("/{kundeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void updateKunde(Kunde kunde){
        if(kunder.get(kunde.getId())!=null){
            kunder.put(kunde.getId(),kunde);
        }else {
            throw new javax.ws.rs.NotFoundException();

        }
    }
}