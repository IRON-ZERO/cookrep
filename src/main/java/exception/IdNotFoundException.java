package exception;

public class IdNotFoundException extends RuntimeException{
    public IdNotFoundException(String id){
        super("id: "+id+" not found");
    }
}
