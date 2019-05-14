package model;

/**
 * Created by rames on 05-05-2019.
 */
public class TicketDetails {
    private String ticketCategory;
    private String price;
    private String totalPrice;
    private int fromAge;
    private int noOfTotalTickets;
    private int toAge;

    public String getTicketCategory() {
        return ticketCategory;
    }

    public void setTicketCategory(String ticketCategory) {
        this.ticketCategory = ticketCategory;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getFromAge() {
        return fromAge;
    }

    public void setFromAge(int fromAge) {
        this.fromAge = fromAge;
    }

    public int getNoOfTotalTickets() {
        return noOfTotalTickets;
    }

    public void setNoOfTotalTickets(int noOfTotalTickets) {
        this.noOfTotalTickets = noOfTotalTickets;
    }

    public int getToAge() {
        return toAge;
    }

    public void setToAge(int toAge) {
        this.toAge = toAge;
    }
}
