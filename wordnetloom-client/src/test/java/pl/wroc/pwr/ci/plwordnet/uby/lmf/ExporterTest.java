package pl.wroc.pwr.ci.plwordnet.uby.lmf;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.junit.Ignore;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.client.utils.RemoteUtils;

public class ExporterTest {

    @Test
    @Ignore
    public void remoteLmfServiceTest() throws InterruptedException {

        Future<String> isComplete = RemoteUtils.lmfExportRemote.startExport(LexiconManager.getInstance().getFullLexicons(), true);
        Date start = new Date();
        System.out.println(start);
        while (RemoteUtils.lmfExportRemote.isExportRunning()) {
            System.out.println(RemoteUtils.lmfExportRemote.getProggress());
            System.out.println(RemoteUtils.lmfExportRemote.getMessages());
        }
        try {
            if (isComplete.isDone()) {
                System.out.println(isComplete.get().toString());
            }
            Date end = new Date();
            System.out.println(end);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void remoteTest() {
    }

    @Test
    @Ignore
    public void remoteImport() {
    }
}
