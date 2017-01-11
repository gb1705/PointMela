package cme.pointmila.com;

/**
 * Created by amoln on 07-10-2016.
 */
public class MedicalCouncil {
    private String name;
    private String shortcode;
    private String Id;
    public MedicalCouncil(String name,String Id,String shortcode)
    {
        this.name = name;
        this.Id = Id;
        this.shortcode = shortcode;

    }

    public String getId()
    {
        return Id;
    }
    public void setId(String Id)
    {
        this.Id = Id;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getShortcode()
    {
        return shortcode;
    }
    public void setShortcode(String shortcode)
    {
        this.shortcode = shortcode;
    }
}
