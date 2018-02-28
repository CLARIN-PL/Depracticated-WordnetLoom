package pl.edu.pwr.wordnetloom.application.importers.lmf;

import pl.edu.pwr.wordnetloom.application.importers.lmf.model.LexicalResource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

public class LmfResourceImporter {

    public LexicalResource getResourceFromFile(String filename) throws JAXBException, XMLStreamException {

        JAXBContext jc = JAXBContext.newInstance(LexicalResource.class);

        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);

        XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(filename));

        Unmarshaller unmarshaller = jc.createUnmarshaller();

        return (LexicalResource) unmarshaller.unmarshal(xsr);

    }
}
