package sk.posam.domain;

public enum Department {
    OCNE("Ocne"),
    CHIRURGIA("Chirurgia"),
    KOZNE("Kozne"),
    PSYCHIATRIA("Psychiatria"),
    KARDIOLOGIA("kardiologia"),
    ORTOPEDIA("Ortopedia");


    private String depCode;

    Department(String depCode) {
        this.depCode = depCode;
    }

    public String getDepCode() {
        return this.depCode;
    }
}
