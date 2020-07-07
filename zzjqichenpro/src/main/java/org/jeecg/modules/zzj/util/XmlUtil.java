package org.jeecg.modules.zzj.util;

import lombok.experimental.SuperBuilder;
import org.apache.poi.ss.formula.functions.T;

import javax.xml.bind.*;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlUtil {
    //xml转换为JavaBena
    @SuppressWarnings("unchecked")
    public static <T> T XmlToBean(String xmlPath,Class<T> load) throws Exception {
        JAXBContext context=JAXBContext.newInstance(load);
        Unmarshaller unmarshaller =context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new StringReader(xmlPath));
    }
    /**
     * JavaBean转换成xml
     * @param obj
     * @param encoding
     * @return
     */
    public static String convertToXml(Object obj, String encoding) {
        String result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
