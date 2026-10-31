package br.com.fiap.soulcode.test;

import br.com.fiap.soulcode.connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.ResultSet;

public class TesteConexao {
    public static void main(String[] args) {
        try (Connection c = ConnectionFactory.abrirConexao();
             ResultSet rs = c.createStatement().executeQuery("SELECT 1 FROM DUAL")) {
            if (rs.next()) System.out.println("Oracle conectado com sucesso!");
        } catch (Exception e) {
            System.err.println("Falha ao conectar ao Oracle: " + e.getMessage());
        }
    }
}
