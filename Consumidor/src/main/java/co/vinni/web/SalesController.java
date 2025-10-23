package co.vinni.web;

import co.vinni.repository.SalesRepository;
import co.vinni.model.Venta;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SalesController {

    private final SalesRepository repo;

    public SalesController(SalesRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Venta> all() {
        return repo.all();
    }
}
