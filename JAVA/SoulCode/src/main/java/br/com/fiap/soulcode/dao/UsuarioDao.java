package br.com.fiap.soulcode.dao;

import br.com.fiap.soulcode.connection.ConnectionFactory;
import br.com.fiap.soulcode.model.Usuario;
import java.sql.*;
import java.util.*;

public class UsuarioDao {
    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO T_SU_USUARIO (ID_USUARIO, NM_USUARIO, DS_EMAIL, DS_SENHA, DT_CADASTRO, ST_USUARIO) VALUES (?, ?, ?, ?, SYSDATE, 'A')";
        try (Connection c = ConnectionFactory.abrirConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            int id = OracleIds.proximo(c, "SEQ_SU_USUARIO");
            ps.setInt(1, id);
            ps.setString(2, usuario.getNome());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getSenhaHash());
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
        try (Connection c = ConnectionFactory.abrirConexao(); PreparedStatement ps = c.prepareStatement(
                "UPDATE T_SU_USUARIO SET NM_USUARIO=?, DS_EMAIL=? WHERE ID_USUARIO=?")) {
            ps.setString(1, usuario.getNome()); ps.setString(2, usuario.getEmail()); ps.setInt(3, usuario.getId());
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
        // Idade, cidade e interesse não existem no DDL recebido.
        return new Usuario(rs.getInt("ID_USUARIO"), rs.getString("NM_USUARIO"), rs.getString("DS_SENHA"),
                rs.getString("DS_EMAIL"), 0, "", "", rs.getInt("PONTOS"));
    }
}
