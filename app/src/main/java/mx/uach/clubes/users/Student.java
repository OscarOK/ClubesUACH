package mx.uach.clubes.users;

import com.google.firebase.auth.FirebaseUser;

import mx.uach.clubes.Utils.StringUtils;

public class Student {
    private String UID;
    private String firstName;
    private String lastName;
    private String email;
    private String enrollment;

    public Student() {
    }

    // Constructor for new users
    public Student(FirebaseUser user) throws NullPointerException {
        this.UID = user.getUid();
        this.email = user.getEmail();
        this.getName(user.getDisplayName());
    }

    public Student(String UID, String firstName, String lastName, String email, String enrollment) {
        this.UID = UID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.enrollment = enrollment;
    }

    private void getName(String displayName) {
        String[] names = StringUtils.SentenceCase(displayName).split(" ");
        StringBuilder builder = new StringBuilder();

        try {
            this.firstName = names[0];

            for (int i = 1; i < names.length; i++) {
                builder.append(names[i]).append(" ");
            }

            this.lastName = builder.toString().trim();
        } catch (IndexOutOfBoundsException e) {
            this.firstName = "";
            this.lastName = "";
        }
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }
}
