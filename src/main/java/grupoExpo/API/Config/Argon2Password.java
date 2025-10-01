package grupoExpo.API.Config;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Service;

@Service
public class Argon2Password {

    //Configuración recomendada para Argon2Id
    private static final int iteraciones = 10;

    private static final int memory = 32768;

    private static final int paralelismo = 2;

    //Crear una instancia de argon2Id
    private Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    public String EncryptPassword(String password){
        return argon2.hash(iteraciones, memory, paralelismo, password);
    }

    public boolean VerifyPassword(String passwordBD, String password){
        return argon2.verify(passwordBD, password);
    }
}
