
package br.com.fiap.soulcode.controller;

import br.com.fiap.soulcode.dao.InteracaoDao;
import br.com.fiap.soulcode.model.Interacao;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interacoes")
public class InteracaoController {

    private final InteracaoDao interacaoDao = new InteracaoDao();

    @GetMapping
    public List<Interacao> listar() {
        return interacaoDao.listar();
    }

    @PostMapping
    public String inserir(@RequestBody Interacao interacao) {
        interacaoDao.inserir(interacao);
        return "Interação cadastrada com sucesso!";
    }

    @DeleteMapping("/{id}")
    public String remover(@PathVariable int id) {
        interacaoDao.remover(id);
        return "Interação removida com sucesso!";
    }
}
