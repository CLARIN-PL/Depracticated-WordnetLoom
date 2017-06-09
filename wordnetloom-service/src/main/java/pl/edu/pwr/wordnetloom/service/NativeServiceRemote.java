package pl.edu.pwr.wordnetloom.service;

import javax.ejb.Remote;

@Remote
public interface NativeServiceRemote {

    void setOwner(String owner);

    String getOwner();

}
