
package br.com.fiap.soulcode.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionFactory {

    private ConnectionFactory() {}

    private static final String URL =
            System.getenv().getOrDefault(
                    "ORACLE_URL",
                    "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl"
            );

    private static final String USUARIO = System.getenv("ORACLE_USER");
    private static final String SENHA = System.getenv("ORACLE_PASSWORD");

    public static Connection abrirConexao() throws SQLException {

        if (USUARIO == null || SENHA == null ||
                USUARIO.isBlank() || SENHA.isBlank()) {
            throw new SQLException("Configure as credenciais do Oracle.");
        }

        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
