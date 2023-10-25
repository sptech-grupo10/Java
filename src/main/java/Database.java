import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Database {
    static String username;
    public static boolean autenticarLogin(String email, String senha) {
        try {
            File xmlFile = new File("C:\\Users\\KAUANMATHEUSBARROSDE\\IdeaProjects\\system-login\\src\\main\\java\\users.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList userList = doc.getElementsByTagName("user");

            for (int i = 0; i < userList.getLength(); i++) {
                Element userElement = (Element) userList.item(i);
                String emaildb = userElement.getElementsByTagName("email").item(0).getTextContent();
                String senhadb = userElement.getElementsByTagName("password").item(0).getTextContent();
                if (emaildb.equals(email) && senhadb.equals(senha)) {
                    username = userElement.getElementsByTagName("username").item(0).getTextContent();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getUsername(){
        return username;
    }
}
