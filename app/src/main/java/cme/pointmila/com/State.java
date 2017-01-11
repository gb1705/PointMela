package cme.pointmila.com;

/**
 * Created by amoln on 08-10-2016.
 */
public class State
{
    private String name;
    private int Id;
    public State(String name,int Id)
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
