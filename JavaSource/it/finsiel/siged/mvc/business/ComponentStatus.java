package it.finsiel.siged.mvc.business;


public interface ComponentStatus {

    public static int STATUS_OK = 1;

    public static int STATUS_ERROR = 2;

    public int getStatus();

    public void setStatus(int s);

}
