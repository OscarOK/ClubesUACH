package mx.uach.clubes.Posts;

public class AuthorPost extends Post implements Comparable<AuthorPost> {
    private String clubName;
    private String imgClub;

    public AuthorPost() {
        super();
    }

    public AuthorPost(String clubName) {
        super();
        this.clubName = clubName;
    }

    public String getImgClub() {
        return imgClub;
    }

    public void setImgClub(String imgClub) {
        this.imgClub = imgClub;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    @Override
    public int compareTo(AuthorPost o) {
        if (o == null) {
            return 0;
        }

        return -1 * this.getDate().compareTo(o.getDate());
    }
}
