package br.com.fiap.soulcode.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

final class OracleIds {
    private OracleIds() {}

    static int proximo(Connection connection, String sequencia) throws SQLException {
        // Sequências são nomes internos fixos, nunca entradas do usuário.
        try (PreparedStatement stmt = connection.prepareStatement("SELECT " + sequencia + ".NEXTVAL FROM DUAL");
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
