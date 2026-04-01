package customer_function;

public class ServiceItem {
    private String serviceItemID;
    private String servicePriceID;
    private String category;
    private String duration;
    private String description;

    public ServiceItem(String serviceItemID, String servicePriceID,
                       String category, String duration, String description) {
        this.serviceItemID  = serviceItemID;
        this.servicePriceID = servicePriceID;
        this.category       = category;
        this.duration       = duration;
        this.description    = description;
    }

    public String getServiceItemID()  { return serviceItemID; }
    public String getServicePriceID() { return servicePriceID; }
    public String getCategory()       { return category; }
    public String getDuration()       { return duration; }
    public String getDescription()    { return description; }
}