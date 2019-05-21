package mx.uach.clubes.users;

import com.google.firebase.auth.FirebaseUser;

public class Coordinator extends Student {
    private String telNumber;

    public Coordinator(FirebaseUser user) {
        super(user);
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }
}
