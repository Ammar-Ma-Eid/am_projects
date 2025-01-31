public class Employee extends User{
    protected String type;
    public Employee(int id, String name, String username, String password ,String file_name,String type) {
        super(id,name,username,password ,file_name );
        this.type=type;

    }

    public String getType() {
        return type;
    }
}
