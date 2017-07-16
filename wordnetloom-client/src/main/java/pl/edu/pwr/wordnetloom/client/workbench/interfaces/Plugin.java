package pl.edu.pwr.wordnetloom.client.workbench.interfaces;

/**
 * Interfejs zapewniający dostęp funkcji oferowanych przez plugin
 *
 * @author <a href="mailto:lukasz.jastrzebski@pwr.wroc.pl">Lukasz
 * Jastrzebski</a>
 * @version CVS $Id$
 */
public interface Plugin {

    /**
     * Wywoływana celem zainstalowania danego pluginu w środowisku
     *
     * @param workbench - środowisko pracy
     */
    void install(Workbench workbench);

}
