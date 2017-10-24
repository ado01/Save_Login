package SaveLogin;

import java.util.List;

public interface MyXmlInterface {

	public List<Login> loadXMLApi();

	public void insertTagXMLApi(Login login);

	public void deleteTagXMLApi(Login login);

	public void changeTagXMLApi(Login login);
}
