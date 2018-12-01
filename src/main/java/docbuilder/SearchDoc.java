package docbuilder;

import org.apache.solr.client.solrj.beans.Field;

import java.util.List;
import java.util.Set;

public class SearchDoc {

    @Field
    private String imageStatus;

    @Field
    private String image;

    @Field
    private String brand;

    @Field
    private String description;

    @Field
    private String id;

    @Field
    private String name;

    @Field
    private String url;

    @Field
    private String primaryVertical;

    @Field
    private List<String> primaryCategory;

    @Field
    private Set<String> categories;

    @Field
    private String itemnumber;

    @Field
    private String partnumber;

    @Field
    private String itemCondition;

    @Field
    private String price;

    @Field
    private int daysOnline;

    @Field
    private String instock;

    @Field
    private  String weight;

    @Field
    private  String height;


    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrimaryVertical() {
        return primaryVertical;
    }

    public void setPrimaryVertical(String primaryVertical) {
        this.primaryVertical = primaryVertical;
    }

    public List<String> getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(List<String> primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public String getItemnumber() {
        return itemnumber;
    }

    public void setItemnumber(String itemnumber) {
        this.itemnumber = itemnumber;
    }

    public String getPartnumber() {
        return partnumber;
    }

    public void setPartnumber(String partnumber) {
        this.partnumber = partnumber;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getDaysOnline() {
        return daysOnline;
    }

    public void setDaysOnline(int daysOnline) {
        this.daysOnline = daysOnline;
    }

    public String getInstock() {
        return instock;
    }

    public void setInstock(String instock) {
        this.instock = instock;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}