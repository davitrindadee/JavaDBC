
package br.com.fiap.soulcode.controller;

import br.com.fiap.soulcode.dao.MissaoDao;
import br.com.fiap.soulcode.model.Missao;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missoes")
public class MissaoController {

    private final MissaoDao missaoDao = new MissaoDao();

    @GetMapping
    public List<Missao> listar() {
        return missaoDao.listar();
    }

    @PostMapping
    public String inserir(@RequestBody Missao missao) {
        missaoDao.inserir(missao);
        return "Missão cadastrada com sucesso!";
    }

    @PutMapping("/{id}")
    public String atualizar(
            @PathVariable int id,
            @RequestBody Missao missao) {

        missao.setId(id);
        missaoDao.atualizar(missao);

        return "Missão atualizada com sucesso!";
    }

    @DeleteMapping("/{id}")
    public String remover(@PathVariable int id) {
        missaoDao.remover(id);
        return "Missão removida com sucesso!";
    }
}
