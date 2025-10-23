package co.vinni.repository;

import co.vinni.model.Venta;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class SalesRepository {

    private final List<Venta> ventas = new CopyOnWriteArrayList<>();

    public void add(Venta v) {
        ventas.add(v);
    }

    public List<Venta> all() {
        // retornar más reciente primero
        List<Venta> copy = new ArrayList<>(ventas);
        copy.sort(Comparator.comparing(Venta::getTs).reversed());
        return copy;
    }
}
