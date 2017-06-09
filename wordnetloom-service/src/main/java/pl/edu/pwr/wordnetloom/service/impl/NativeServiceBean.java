package pl.edu.pwr.wordnetloom.service.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;
import pl.edu.pwr.wordnetloom.service.NativeServiceRemote;

@Stateless
public class NativeServiceBean implements NativeServiceRemote {

    @EJB
    private DAOLocal local;

    @Override
    public void setOwner(String owner) {
        if (owner == null) {
            owner = "";
        }

        owner = owner.replaceAll("\"", "").replaceAll("'", "").replaceAll("--", "");
        local.getEM().createNativeQuery("SET @owner=\"" + owner + "\"").executeUpdate();
    }

    @Override
    public String getOwner() {
        Object o = local.getEM().createNativeQuery("SELECT @owner").getSingleResult();
        if (o != null) {
            return o.toString();
        }
        return null;
    }

}
