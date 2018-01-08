package services;

class Kunde{
    private String id;
    private String navn;

    public Kunde() {
    }

    public Kunde(String navn, String id) {

        this.id = id;
        this.navn = navn;
    }

    public String getId() {
        return id;
    }

    public void setId(String kundeId) {
        this.id = kundeId;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }
}