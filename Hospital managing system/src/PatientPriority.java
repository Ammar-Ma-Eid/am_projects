public class PatientPriority {
    private String email;
    private String name;
    private int injuryLevel;

    public PatientPriority(String email, String name, int injuryLevel) {
        this.email = email;
        this.name = name;
        this.injuryLevel = injuryLevel;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public int getInjuryLevel() {
        return injuryLevel;
    }

    @Override
    public String toString() {
        return name + "," + email + "," + injuryLevel;
    }
}
