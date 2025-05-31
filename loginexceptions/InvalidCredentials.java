package loginexceptions;

public class InvalidCredentials extends Exception{
    public InvalidCredentials(String message){
        super(message);
    }
}