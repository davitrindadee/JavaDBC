package br.com.fiap.soulcode.dao;

import br.com.fiap.soulcode.connection.ConnectionFactory;
import br.com.fiap.soulcode.model.Usuario;
import java.sql.*;
import java.util.*;

public class UsuarioDao {
    // true quando as colunas NR_IDADE, NM_CIDADE e DS_INTERESSE existem (ver config/MigracaoUsuario)
    private static volatile boolean colunasExtras = false;

    public static void setColunasExtras(boolean disponiveis) {
        colunasExtras = disponiveis;
    }

    public void inserir(Usuario usuario) {
        String sql = colunasExtras
                ? "INSERT INTO T_SU_USUARIO (ID_USUARIO, NM_USUARIO, DS_EMAIL, DS_SENHA, NR_IDADE, NM_CIDADE, DS_INTERESSE, DT_CADASTRO, ST_USUARIO) VALUES (?, ?, ?, ?, ?, ?, ?, SYSDATE, 'A')"
                : "INSERT INTO T_SU_USUARIO (ID_USUARIO, NM_USUARIO, DS_EMAIL, DS_SENHA, DT_CADASTRO, ST_USUARIO) VALUES (?, ?, ?, ?, SYSDATE, 'A')";
        try (Connection c = ConnectionFactory.abrirConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            int id = OracleIds.proximo(c, "SEQ_SU_USUARIO");
            ps.setInt(1, id);
            ps.setString(2, usuario.getNome());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getSenhaHash());
            if (colunasExtras) {
                ps.setInt(5, usuario.getIdade());
                ps.setString(6, usuario.getCidade());
                ps.setString(7, usuario.getInteresse());
            }
            ps.executeUpdate();
            usuario.setId(id);
        } catch (SQLException e) { throw new IllegalStateException("Não foi possível cadastrar o usuário.", e); }
    }

    public List<Usuario> listar() {
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection c = ConnectionFactory.abrirConexao(); PreparedStatement ps = c.prepareStatement(
                "SELECT u.*, NVL(p.QT_PONTOS_ATUAIS, 0) AS PONTOS FROM T_SU_USUARIO u LEFT JOIN T_SU_PONTUACAO p ON p.ID_USUARIO=u.ID_USUARIO ORDER BY u.ID_USUARIO");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) usuarios.add(mapear(rs));
            return usuarios;
        } catch (SQLException e) { throw new IllegalStateException("Não foi possível listar os usuários.", e); }
    }

    public Usuario buscarPorId(int id) {
        try (Connection c = ConnectionFactory.abrirConexao(); PreparedStatement ps = c.prepareStatement(
                "SELECT u.*, NVL(p.QT_PONTOS_ATUAIS, 0) AS PONTOS FROM T_SU_USUARIO u LEFT JOIN T_SU_PONTUACAO p ON p.ID_USUARIO=u.ID_USUARIO WHERE u.ID_USUARIO=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapear(rs) : null; }
        } catch (SQLException e) { throw new IllegalStateException("Não foi possível buscar o usuário.", e); }
    }

    public void atualizar(Usuario usuario) {
        String sql = colunasExtras
                ? "UPDATE T_SU_USUARIO SET NM_USUARIO=?, DS_EMAIL=?, NR_IDADE=?, NM_CIDADE=?, DS_INTERESSE=? WHERE ID_USUARIO=?"
                : "UPDATE T_SU_USUARIO SET NM_USUARIO=?, DS_EMAIL=? WHERE ID_USUARIO=?";
        try (Connection c = ConnectionFactory.abrirConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, usuario.getNome()); ps.setString(2, usuario.getEmail());
            if (colunasExtras) {
                ps.setInt(3, usuario.getIdade()); ps.setString(4, usuario.getCidade()); ps.setString(5, usuario.getInteresse());
                ps.setInt(6, usuario.getId());
            } else {
                ps.setInt(3, usuario.getId());
            }
            if (ps.executeUpdate() == 0) throw new IllegalStateException("Nenhum usuário foi atualizado.");
        } catch (SQLException e) { throw new IllegalStateException("Não foi possível atualizar o usuário.", e); }
    }

    public void remover(int id) {
        try (Connection c = ConnectionFactory.abrirConexao(); PreparedStatement ps = c.prepareStatement(
                "DELETE FROM T_SU_USUARIO WHERE ID_USUARIO=?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) throw new IllegalStateException("Nenhum usuário foi removido.");
        } catch (SQLException e) { throw new IllegalStateException("Não foi possível remover o usuário (verifique vínculos como avatar e pontuação).", e); }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        // Idade, cidade e interesse só existem depois da MigracaoUsuario; antes disso voltam vazios.
        int idade = colunasExtras ? rs.getInt("NR_IDADE") : 0;
        String cidade = colunasExtras ? rs.getString("NM_CIDADE") : null;
        String interesse = colunasExtras ? rs.getString("DS_INTERESSE") : null;
        return new Usuario(rs.getInt("ID_USUARIO"), rs.getString("NM_USUARIO"), rs.getString("DS_SENHA"),
                rs.getString("DS_EMAIL"), idade, cidade == null ? "" : cidade, interesse == null ? "" : interesse,
                rs.getInt("PONTOS"));
    }
}
