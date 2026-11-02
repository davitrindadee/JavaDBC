package br.com.fiap.soulcode.config;

import br.com.fiap.soulcode.connection.ConnectionFactory;
import br.com.fiap.soulcode.dao.UsuarioDao;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Ao iniciar a API, cria em T_SU_USUARIO as colunas de idade, cidade e interesse caso ainda não existam.
// Se não for possível (ex.: sem permissão), a API continua funcionando sem esses campos.
@Component
public class MigracaoUsuario {

    private static final String[][] COLUNAS = {
            {"NR_IDADE", "NUMBER(3)"},
            {"NM_CIDADE", "VARCHAR2(80)"},
            {"DS_INTERESSE", "VARCHAR2(30)"}
    };

    @EventListener(ApplicationReadyEvent.class)
    public void criarColunasExtras() {
        try (Connection conexao = ConnectionFactory.abrirConexao()) {
            for (String[] coluna : COLUNAS) {
                if (!colunaExiste(conexao, coluna[0])) {
                    // Nomes e tipos são fixos no código, nunca vêm do usuário.
                    try (Statement stmt = conexao.createStatement()) {
                        stmt.execute("ALTER TABLE T_SU_USUARIO ADD (" + coluna[0] + " " + coluna[1] + ")");
                    }
                    System.out.println("Coluna " + coluna[0] + " criada em T_SU_USUARIO.");
                }
            }
            UsuarioDao.setColunasExtras(true);
        } catch (SQLException e) {
            UsuarioDao.setColunasExtras(false);
            System.err.println("Aviso: idade, cidade e interesse não serão salvos (" + e.getMessage() + ")");
        }
    }

    private boolean colunaExiste(Connection conexao, String coluna) throws SQLException {
        try (PreparedStatement stmt = conexao.prepareStatement(
                "SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'T_SU_USUARIO' AND COLUMN_NAME = ?")) {
            stmt.setString(1, coluna);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }
}
