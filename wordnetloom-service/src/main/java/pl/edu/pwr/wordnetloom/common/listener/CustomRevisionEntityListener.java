package pl.edu.pwr.wordnetloom.common.listener;

import org.hibernate.envers.RevisionListener;
import pl.edu.pwr.wordnetloom.common.model.CustomRevisionEntity;

import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CustomRevisionEntityListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity customRevisionEntity =
                (CustomRevisionEntity) revisionEntity;

        try {

            InitialContext ic = new InitialContext();
            SessionContext sctx = (SessionContext) ic.lookup("java:comp/EJBContext");
            customRevisionEntity.setUsername(sctx.getCallerPrincipal().getName());

        } catch (NamingException ex) {
            throw new IllegalStateException(ex);
        }

    }
}
