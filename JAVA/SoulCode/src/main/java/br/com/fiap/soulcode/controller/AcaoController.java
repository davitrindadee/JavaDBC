
package br.com.fiap.soulcode.controller;

import br.com.fiap.soulcode.dao.AcaoDao;
import br.com.fiap.soulcode.model.Acao;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acoes")
public class AcaoController {

    private final AcaoDao acaoDao = new AcaoDao();

    @GetMapping
    public List<Acao> listar() {
        return acaoDao.listar();
    }

    @PostMapping
    public String inserir(@RequestBody Acao acao) {
        acaoDao.inserir(acao);
        return "Tipo de ação cadastrado com sucesso!";
    }

    @PutMapping("/{id}")
    public String atualizar(
            @PathVariable int id,
            @RequestBody Acao acao) {

        acao.setId(id);
        acaoDao.atualizar(acao);

        return "Tipo de ação atualizado com sucesso!";
    }

    @DeleteMapping("/{id}")
    public String remover(@PathVariable int id) {
        acaoDao.remover(id);
        return "Tipo de ação removido com sucesso!";
    }
}
