package loginexceptions;

public class EmptyUsernamePasswordException extends Exception{
    public EmptyUsernamePasswordException(String message){
        super(message);
    }
}