
// Класс "Запись", который может хранить данные о событии
public class Note {
    private String name; // Название события
    private String dateString; // Дата проведения (в строковом виде)
    private String location; // Место проведения

    public Note(String Name,String DateString,String Location)
    {
        this.name = Name;
        this.dateString = DateString;
        this.location = Location;
    }
    private Note(){

    }
    public void setName(String Name)
    {
        this.name =Name;
    }
    public String getName()
    {
        return name;
    }

    public void setDateString(String DateString)
    {
        this.dateString =DateString;
    }
    public String getDateString()
    {
        return dateString;
    }
    public void setLocation(String Location)
    {
        this.location =Location;
    }
    public String getLocation()
    {
        return location;
    }
    public void print()
    {
        System.out.println("|"+ name +"|"+ dateString +"|"+ location +"|");
    }
}
