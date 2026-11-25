package es.pixelvault.tienda.service;

import es.pixelvault.tienda.dto.StockDto;
import es.pixelvault.tienda.dto.VideojuegoCrearDto;
import es.pixelvault.tienda.dto.VideojuegoDetalleDto;
import es.pixelvault.tienda.dto.VideojuegoFiltroDto;
import es.pixelvault.tienda.dto.VideojuegoResumenDto;
import es.pixelvault.tienda.exception.ConflictoException;
import es.pixelvault.tienda.exception.PeticionInvalidaException;
import es.pixelvault.tienda.exception.RecursoNoEncontradoException;
import es.pixelvault.tienda.model.Direccion;
import es.pixelvault.tienda.model.Genero;
import es.pixelvault.tienda.model.OrdenProducto;
import es.pixelvault.tienda.model.Plataforma;
import es.pixelvault.tienda.model.Videojuego;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/*
 * Catálogo de videojuegos en memoria. Depende de GeneroService (validar y mapear categorías)
 * y de BusquedaService (filtros). Aplica la ordenación de los productos.
 */
@Service
public class VideojuegoService {

    private static final Set<Integer> PEGI_VALIDOS = Set.of(3, 7, 12, 16, 18);
    private static final String IMAGEN = "https://cdn.cloudflare.steamstatic.com/steam/apps/%d/header.jpg";

    private final GeneroService generoService;
    private final BusquedaService busquedaService;

    private final Map<Long, Videojuego> videojuegos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    public VideojuegoService(GeneroService generoService, BusquedaService busquedaService) {
        this.generoService = generoService;
        this.busquedaService = busquedaService;
    }

    // ---------------------------------------------------------------- datos de ejemplo

    /*
     * 13 videojuegos de ejemplo: uno sin stock (Hades), uno con stock bajo (Slay the Spire),
     * uno retirado (Dark Souls III) y varios destacados. Los precios son ficticios.
     * Los géneros 1..4 vienen del contrato con GeneroService.
     */
    @PostConstruct
    void cargarDatosEjemplo() {
        ejemplo("ELDEN-RING-PC", "Elden Ring", "Explora las Tierras Intermedias en un enorme RPG de acción.",
                "59.99", 25, 1245620, true, true, 2L, Plataforma.PC, 16, 13);
        ejemplo("WITCHER3-PC", "The Witcher 3: Wild Hunt", "Caza monstruos como Geralt de Rivia en un mundo abierto lleno de historias.",
                "29.99", 40, 292030, true, true, 2L, Plataforma.PC, 18, 12);
        ejemplo("BG3-PC", "Baldur's Gate 3", "Reúne a tu grupo y vive una aventura de rol por turnos sin precedentes.",
                "59.99", 50, 1086940, true, true, 2L, Plataforma.PC, 18, 11);
        ejemplo("CYBERPUNK-PS5", "Cyberpunk 2077", "Un RPG de acción en la megalópolis de Night City.",
                "39.99", 18, 1091500, true, true, 1L, Plataforma.PS5, 18, 10);
        ejemplo("RDR2-XBOX", "Red Dead Redemption 2", "La épica historia de Arthur Morgan y la banda de Van der Linde.",
                "44.99", 12, 1174180, true, false, 1L, Plataforma.XBOX, 18, 9);
        ejemplo("DOOM-ETERNAL-PC", "DOOM Eternal", "Acción frenética para acabar con las hordas del infierno.",
                "19.99", 30, 782330, true, false, 1L, Plataforma.PC, 18, 8);
        ejemplo("TERRARIA-PC", "Terraria", "Excava, construye, lucha y explora en un mundo 2D infinito.",
                "9.99", 100, 105600, true, false, 1L, Plataforma.PC, 12, 7);
        ejemplo("HOLLOW-KNIGHT-SW", "Hollow Knight", "Un metroidvania de ensueño en el reino de Hallownest.",
                "14.99", 60, 367520, true, false, 3L, Plataforma.SWITCH, 7, 6);
        ejemplo("CELESTE-SW", "Celeste", "Escala la montaña Celeste en este plataformas exigente y emotivo.",
                "19.99", 35, 504230, true, false, 3L, Plataforma.SWITCH, 7, 5);
        ejemplo("HADES-SW", "Hades", "Escapa del inframundo en un roguelike de acción con mucha narrativa.",
                "24.99", 0, 1145360, true, true, 3L, Plataforma.SWITCH, 12, 4);   // SIN STOCK
        ejemplo("STARDEW-SW", "Stardew Valley", "Hereda la granja de tu abuelo y empieza una nueva vida en el campo.",
                "13.99", 80, 413150, true, false, 4L, Plataforma.SWITCH, 3, 3);
        ejemplo("SLAY-SPIRE-PC", "Slay the Spire", "Cartas y roguelike: construye tu mazo y asciende por la torre.",
                "22.99", 4, 646570, true, false, 4L, Plataforma.PC, 12, 2);        // STOCK BAJO
        ejemplo("DS3-PC", "Dark Souls III", "Último capítulo de la saga de acción y rol más exigente.",
                "49.99", 10, 374320, false, false, 2L, Plataforma.PC, 16, 1);      // RETIRADO
    }

    private void ejemplo(String sku, String nombre, String descripcion, String precio, int stock,
                         int steamAppId, boolean activo, boolean destacado, Long generoId,
                         Plataforma plataforma, int pegi, int diasDeAntiguedad) {
        Videojuego v = new Videojuego();
        v.setId(secuencia.incrementAndGet());
        v.setSku(sku);
        v.setNombre(nombre);
        v.setDescripcion(descripcion);
        v.setPrecio(new BigDecimal(precio));
        v.setStock(stock);
        v.setImagenUrl(String.format(IMAGEN, steamAppId));
        v.setActivo(activo);
        v.setDestacado(destacado);
        v.setFechaAlta(LocalDateTime.now().minusDays(diasDeAntiguedad));
        v.setGenero(generoService.obtenerEntidad(generoId));
        v.setPlataforma(plataforma);
        v.setPegi(pegi);
        videojuegos.put(v.getId(), v);
    }

    // ---------------------------------------------------------------- consultas públicas (solo activos)

    public List<VideojuegoResumenDto> listar(VideojuegoFiltroDto filtro) {
        List<Videojuego> activos = videojuegos.values().stream().filter(Videojuego::isActivo).toList();
        return busquedaService.filtrar(activos, filtro).stream()
                .sorted(comparador(filtro == null ? null : filtro.getOrden(),
                        filtro == null ? null : filtro.getDireccion()))
                .map(this::aResumen)
                .toList();
    }

    public List<VideojuegoResumenDto> destacados() {
        return videojuegos.values().stream()
                .filter(Videojuego::isActivo)
                .filter(Videojuego::isDestacado)
                .sorted(Comparator.comparing(Videojuego::getId))
                .map(this::aResumen)
                .toList();
    }

    public VideojuegoDetalleDto detalle(Long id) {
        Videojuego v = obtenerEntidad(id);
        if (!v.isActivo()) {
            throw new RecursoNoEncontradoException("No existe ningún videojuego con id " + id);
        }
        return aDetalle(v);
    }

    public VideojuegoDetalleDto porSku(String sku) {
        String buscado = sku == null ? "" : sku.trim().toUpperCase();
        return videojuegos.values().stream()
                .filter(Videojuego::isActivo)
                .filter(v -> v.getSku().equals(buscado))
                .findFirst()
                .map(this::aDetalle)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe ningún videojuego con SKU " + buscado));
    }

    // ---------------------------------------------------------------- administración

    public List<VideojuegoDetalleDto> listarAdmin() {
        return videojuegos.values().stream()
                .sorted(Comparator.comparing(Videojuego::getId))
                .map(this::aDetalle)
                .toList();
    }

    public List<VideojuegoDetalleDto> stockBajo(int umbral) {
        if (umbral < 0) {
            throw new PeticionInvalidaException("El umbral no puede ser negativo");
        }
        return videojuegos.values().stream()
                .filter(Videojuego::isActivo)
                .filter(v -> v.getStock() <= umbral)
                .sorted(Comparator.comparing(Videojuego::getStock).thenComparing(Videojuego::getId))
                .map(this::aDetalle)
                .toList();
    }

    public synchronized VideojuegoDetalleDto crear(VideojuegoCrearDto dto) {
        validar(dto);
        String sku = dto.getSku().trim().toUpperCase();
        comprobarSkuLibre(sku, null);

        Videojuego v = new Videojuego();
        v.setId(secuencia.incrementAndGet());
        v.setActivo(true);
        v.setFechaAlta(LocalDateTime.now());
        copiarDatos(v, dto, sku);
        videojuegos.put(v.getId(), v);
        return aDetalle(v);
    }

    public synchronized VideojuegoDetalleDto modificar(Long id, VideojuegoCrearDto dto) {
        Videojuego v = obtenerEntidad(id);
        validar(dto);
        String sku = dto.getSku().trim().toUpperCase();
        comprobarSkuLibre(sku, id);
        copiarDatos(v, dto, sku);
        return aDetalle(v);
    }

    /** DELETE no borra: marca el videojuego como retirado para no romper los pedidos que lo contienen. */
    public synchronized void retirar(Long id) {
        obtenerEntidad(id).setActivo(false);
    }

    public synchronized VideojuegoDetalleDto reactivar(Long id) {
        Videojuego v = obtenerEntidad(id);
        v.setActivo(true);
        return aDetalle(v);
    }

    public synchronized VideojuegoDetalleDto ajustarStock(Long id, StockDto dto) {
        Videojuego v = obtenerEntidad(id);
        if (dto == null || dto.getCantidad() == null || dto.getCantidad() == 0) {
            throw new PeticionInvalidaException("Indica una cantidad distinta de 0 (positiva suma, negativa resta)");
        }
        int nuevoStock = v.getStock() + dto.getCantidad();
        if (nuevoStock < 0) {
            throw new ConflictoException("El stock no puede quedar en negativo: hay " + v.getStock()
                    + " unidad(es) y se intentan restar " + (-dto.getCantidad()));
        }
        v.setStock(nuevoStock);
        return aDetalle(v);
    }

    // ---------------------------------------------------------------- contrato con otros servicios

    /** Devuelve la entidad (aunque esté retirada) o lanza 404. La usan carrito y pedidos. */
    public Videojuego obtenerEntidad(Long id) {
        Videojuego v = id == null ? null : videojuegos.get(id);
        if (v == null) {
            throw new RecursoNoEncontradoException("No existe ningún videojuego con id " + id);
        }
        return v;
    }

    /** Cuenta TODOS los videojuegos del género, también los retirados. */
    public long contarPorGenero(Long generoId) {
        return videojuegos.values().stream()
                .filter(v -> v.getGenero().getId().equals(generoId))
                .count();
    }

    /** Resta unidades. PedidoService ya ha comprobado antes que hay stock suficiente. */
    public void descontarStock(Videojuego v, int cantidad) {
        v.setStock(v.getStock() - cantidad);
    }

    public void reponerStock(Videojuego v, int cantidad) {
        v.setStock(v.getStock() + cantidad);
    }

    // ---------------------------------------------------------------- privados

    private void validar(VideojuegoCrearDto dto) {
        if (dto == null) {
            throw new PeticionInvalidaException("Faltan los datos del videojuego");
        }
        if (dto.getSku() == null || dto.getSku().isBlank()) {
            throw new PeticionInvalidaException("El SKU es obligatorio");
        }
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new PeticionInvalidaException("El nombre es obligatorio");
        }
        if (dto.getPrecio() == null || dto.getPrecio().signum() <= 0) {
            throw new PeticionInvalidaException("El precio debe ser mayor que 0");
        }
        if (dto.getStock() == null || dto.getStock() < 0) {
            throw new PeticionInvalidaException("El stock no puede ser negativo");
        }
        if (dto.getCategoriaId() == null || !generoService.existe(dto.getCategoriaId())) {
            throw new PeticionInvalidaException("La categoría indicada no existe");
        }
        if (dto.getPlataforma() == null) {
            throw new PeticionInvalidaException("La plataforma es obligatoria (PC, PS5, XBOX o SWITCH)");
        }
        if (dto.getPegi() == null || !PEGI_VALIDOS.contains(dto.getPegi())) {
            throw new PeticionInvalidaException("El PEGI debe ser 3, 7, 12, 16 o 18");
        }
    }

    private void comprobarSkuLibre(String sku, Long idPropio) {
        boolean repetido = videojuegos.values().stream()
                .anyMatch(v -> v.getSku().equals(sku) && !v.getId().equals(idPropio));
        if (repetido) {
            throw new ConflictoException("Ya existe un videojuego con el SKU " + sku);
        }
    }

    private void copiarDatos(Videojuego v, VideojuegoCrearDto dto, String sku) {
        Genero genero = generoService.obtenerEntidad(dto.getCategoriaId());
        v.setSku(sku);
        v.setNombre(dto.getNombre().trim());
        v.setDescripcion(dto.getDescripcion() == null ? null : dto.getDescripcion().trim());
        v.setPrecio(dto.getPrecio().setScale(2, RoundingMode.HALF_UP));
        v.setStock(dto.getStock());
        v.setImagenUrl(dto.getImagenUrl());
        v.setDestacado(Boolean.TRUE.equals(dto.getDestacado()));
        v.setGenero(genero);
        v.setPlataforma(dto.getPlataforma());
        v.setPegi(dto.getPegi());
    }

    /** Por defecto NOMBRE ascendente; NOVEDAD sin dirección indicada se ordena de más nuevo a más antiguo. */
    private Comparator<Videojuego> comparador(OrdenProducto orden, Direccion direccion) {
        OrdenProducto criterio = orden == null ? OrdenProducto.NOMBRE : orden;
        Comparator<Videojuego> comparador = switch (criterio) {
            case NOMBRE -> Comparator.comparing((Videojuego v) -> BusquedaService.normalizar(v.getNombre()));
            case PRECIO -> Comparator.comparing((Videojuego v) -> v.getPrecio());
            case NOVEDAD -> Comparator.comparing((Videojuego v) -> v.getFechaAlta());
        };
        boolean descendente = direccion == Direccion.DESC
                || (direccion == null && criterio == OrdenProducto.NOVEDAD);
        return descendente ? comparador.reversed() : comparador;
    }

    private VideojuegoResumenDto aResumen(Videojuego v) {
        VideojuegoResumenDto dto = new VideojuegoResumenDto();
        dto.setId(v.getId());
        dto.setSku(v.getSku());
        dto.setNombre(v.getNombre());
        dto.setPrecio(v.getPrecio());
        dto.setImagenUrl(v.getImagenUrl());
        dto.setDisponible(v.getStock() > 0);
        dto.setDestacado(v.isDestacado());
        dto.setCategoriaNombre(v.getGenero().getNombre());
        dto.setPlataforma(v.getPlataforma());
        return dto;
    }

    private VideojuegoDetalleDto aDetalle(Videojuego v) {
        VideojuegoDetalleDto dto = new VideojuegoDetalleDto();
        dto.setId(v.getId());
        dto.setSku(v.getSku());
        dto.setNombre(v.getNombre());
        dto.setDescripcion(v.getDescripcion());
        dto.setPrecio(v.getPrecio());
        dto.setStock(v.getStock());
        dto.setImagenUrl(v.getImagenUrl());
        dto.setActivo(v.isActivo());
        dto.setDestacado(v.isDestacado());
        dto.setFechaAlta(v.getFechaAlta());
        dto.setCategoria(generoService.aDto(v.getGenero()));
        dto.setPlataforma(v.getPlataforma());
        dto.setPegi(v.getPegi());
        return dto;
    }
}
