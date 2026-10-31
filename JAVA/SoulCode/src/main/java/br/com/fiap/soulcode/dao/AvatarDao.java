package br.com.fiap.soulcode.dao;

import br.com.fiap.soulcode.connection.ConnectionFactory;
import br.com.fiap.soulcode.model.Avatar;
import br.com.fiap.soulcode.model.Usuario;
import java.sql.*;
import java.util.*;

public class AvatarDao {
    public void inserir(Avatar avatar) {
        String sql = "INSERT INTO T_SU_AVATAR (ID_AVATAR, ID_USUARIO, NM_AVATAR, NR_NIVEL, QT_EXPERIENCIA) VALUES (?, ?, ?, ?, 0)";
        try (Connection c = ConnectionFactory.abrirConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            int id = OracleIds.proximo(c, "SEQ_SU_AVATAR");
            ps.setInt(1,id); ps.setInt(2,avatar.getUsuario().getId()); ps.setString(3,avatar.getNome()); ps.setInt(4,avatar.getNivel());
            ps.executeUpdate(); avatar.setId(id);
        } catch (SQLException e) { throw new IllegalStateException("Não foi possível inserir o avatar.", e); }
    }

    public List<Avatar> listar() {
        List<Avatar> resultado = new ArrayList<>();
        String sql = "SELECT a.ID_AVATAR, a.NM_AVATAR, a.NR_NIVEL, u.ID_USUARIO, u.NM_USUARIO FROM T_SU_AVATAR a JOIN T_SU_USUARIO u ON a.ID_USUARIO=u.ID_USUARIO ORDER BY a.ID_AVATAR";
        try (Connection c = ConnectionFactory.abrirConexao(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario(rs.getInt("ID_USUARIO"),rs.getString("NM_USUARIO"),"","",0,"","",0);
                resultado.add(new Avatar(rs.getInt("ID_AVATAR"),u,rs.getString("NM_AVATAR"),rs.getInt("NR_NIVEL")));
            }
            return resultado;
        } catch (SQLException e) { throw new IllegalStateException("Não foi possível listar os avatares.", e); }
    }

    public void atualizar(Avatar avatar) {
        try (Connection c = ConnectionFactory.abrirConexao(); PreparedStatement ps = c.prepareStatement(
                "UPDATE T_SU_AVATAR SET NM_AVATAR=?, NR_NIVEL=? WHERE ID_AVATAR=?")) {
            ps.setString(1,avatar.getNome()); ps.setInt(2,avatar.getNivel()); ps.setInt(3,avatar.getId()); ps.executeUpdate();
        } catch (SQLException e) { throw new IllegalStateException("Não foi possível atualizar o avatar.", e); }
    }

    public void remover(int id) {
        try (Connection c = ConnectionFactory.abrirConexao(); PreparedStatement ps = c.prepareStatement(
                "DELETE FROM T_SU_AVATAR WHERE ID_AVATAR=?")) {
            ps.setInt(1,id); ps.executeUpdate();
        } catch (SQLException e) { throw new IllegalStateException("Não foi possível remover o avatar.", e); }
    }
}
