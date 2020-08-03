package in.ontimetravel.ontimetravel;


public class SuggestGetSet {

    String location;
    public SuggestGetSet( String location){

        this.setName(location);
    }

    public String getName() {
        return location;
    }

    public void setName(String location) {
        this.location = location;
    }

}