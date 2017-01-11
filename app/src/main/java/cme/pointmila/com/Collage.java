package cme.pointmila.com;

/**
 * Created by amoln on 13-10-2016.
 */
public class Collage
{
    private String name;
    private String Id;
    public Collage(String name,String Id)
    {
        this.name = name;
        this.Id = Id;

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
}
