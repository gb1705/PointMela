package cme.pointmila.com;

/**
 * Created by amoln on 12-10-2016.
 */
public class City
{
    private String name;
    private int Id;
    public City(String name,int Id)
    {
        this.name = name;
        this.Id = Id;

    }

    public int getId()
    {
        return Id;
    }
    public void setId(int Id)
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
