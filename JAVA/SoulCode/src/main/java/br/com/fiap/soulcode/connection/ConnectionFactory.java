package br.com.fiap.soulcode.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionFactory {
    private ConnectionFactory() {}

    // Conexão Oracle da FIAP (SID: orcl).
    private static final String URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl";
    // Preencha apenas em sua cópia local. NÃO envie suas credenciais ao GitHub.
    private static final String USUARIO = "rm571926";
    private static final String SENHA = "300108";

    public static Connection abrirConexao() throws SQLException {
        if (USUARIO.equals("SEU_RM") || SENHA.equals("SUA_SENHA")) {
            throw new SQLException("Preencha USUARIO e SENHA em ConnectionFactory.java antes de executar.");
        }
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
