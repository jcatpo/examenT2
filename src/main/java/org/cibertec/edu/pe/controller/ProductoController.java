package org.cibertec.edu.pe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.cibertec.edu.pe.interfaceService.IDetalleService;
import org.cibertec.edu.pe.interfaceService.IProductoService;
import org.cibertec.edu.pe.interfaces.IDetalleRepository;
import org.cibertec.edu.pe.interfaces.IProductoRepository;
import org.cibertec.edu.pe.interfaces.IVentaRepository;
import org.cibertec.edu.pe.modelo.Detalle;
import org.cibertec.edu.pe.modelo.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"carrito", "total", "precioenvio", "descuento", "subtotal"})
public class ProductoController {
	@Autowired
	private IProductoRepository productoRepository;
	@Autowired
	private IVentaRepository ventaRepository;
	@Autowired
	private IDetalleRepository detalleRepository;
	@Autowired
	private IProductoService productoService;
	@Autowired
	private IDetalleService detalleService;
	
	@GetMapping("/index")
	public String listado(Model model) {
		List<Producto> lista = new ArrayList<>();
		lista = productoRepository.findAll();
		model.addAttribute("productos", lista);
		return "index";
	}
	
	@GetMapping("/agregar/{idProducto}")
    public String agregar(Model model,
                          @PathVariable(name = "idProducto", required = true) int idProducto,
                          @ModelAttribute("carrito") List<Detalle> carrito,
                          @ModelAttribute("subtotal") double subtotal,                          
                          @ModelAttribute("precioenvio") double precioenvio,
                          @ModelAttribute("descuento") double descuento,
                          @ModelAttribute("total") double total) {				
		Producto producto = productoRepository.findById(idProducto).orElse(null);
	    // Buscar si el producto ya está en el carrito
	    Detalle detalleExistente = carrito.stream()
	                                      .filter(detalle -> detalle.getProducto().getIdProducto() == idProducto)
	                                      .findFirst()
	                                      .orElse(null);
	    if (detalleExistente != null) {
	        // El producto ya está en el carrito, incrementar la cantidad
	        detalleExistente.setCantidad(detalleExistente.getCantidad() + 1);
	        detalleExistente.setSubtotal(producto.getPrecio() * detalleExistente.getCantidad());
	    } else {
	        // Crear un nuevo detalle con cantidad 1
	        Detalle nuevoDetalle = new Detalle();
	        nuevoDetalle.setProducto(producto);
	        nuevoDetalle.setCantidad(1);
	        nuevoDetalle.setSubtotal(producto.getPrecio() * nuevoDetalle.getCantidad());
	        // Guardar el nuevo detalle en la base de datos
	        detalleService.guardar(nuevoDetalle);
	        carrito.add(nuevoDetalle);
	    }
	    // Actualizar subtotal, descuento, y total directamente
	    subtotal = carrito.stream().mapToDouble(detalle -> detalle.getSubtotal()).sum();
	    descuento = subtotal * 0.05;
	    total = subtotal + precioenvio - descuento;  // Actualizar el total directamente
	    // Actualizar atributos del modelo
	    model.addAttribute("carrito", carrito);
	    model.addAttribute("subtotal", subtotal);
	    model.addAttribute("precioenvio", precioenvio);
	    model.addAttribute("descuento", descuento);
	    model.addAttribute("total", total);	       
        return "redirect:/index";
    }

    // Método para calcular el total incluyendo precio de envío y descuento
    private double calcularTotal(List<Detalle> carrito, double precioenvio, double descuento) {
        double subtotal = carrito.stream().mapToDouble(detalle -> detalle.getSubtotal()).sum();
        return subtotal + precioenvio - descuento;
    }
	
	@GetMapping("/carrito")
	public String carrito() {
		return "carrito";
	}
	
	@GetMapping("/pagar")
	public String pagar(Model model,
						@ModelAttribute("carrito") List<Detalle> carrito,
			            @ModelAttribute("subtotal") double subtotal,                          
			            @ModelAttribute("precioenvio") double precioenvio,
			            @ModelAttribute("descuento") double descuento,
			            @ModelAttribute("total") double total) {
	    // Codigo para pagar		
	    return "pagar";
	}	
	
	@PostMapping("/actualizarCarrito")
	public String actualizarCarrito(@ModelAttribute("carrito") List<Detalle> carrito) {
	    // Recalcular subtotal, descuento y total
	    double precioenvio = obtenerPrecioEnvio(); 
	    double descuento = calcularDescuento(carrito);
	    double subtotal = carrito.stream().mapToDouble(Detalle::getSubtotal).sum();
	    double total = calcularTotal(carrito, precioenvio, descuento);	         
		detalleService.actualizarDetalles(carrito);
	    return "carrito";
	}
	
	@GetMapping("/eliminar/{idDetalle}")
	public String eliminarProducto(@PathVariable(name = "idDetalle") int idDetalle,
	                               Model model) {
	    // Eliminar el detalle del carrito
	    detalleService.Suprimir(idDetalle);
	    // Recuperar el carrito actualizado
	    List<Detalle> carrito = detalleService.listado();
	    // Actualizar la información del producto en cada detalle
	    for (Detalle detalle : carrito) {
	        detalle.setProducto(productoService.buscar(detalle.getProducto().getIdProducto()).orElse(null));
	    }
	    // Calcular subtotal, descuento y total
	    double precioenvio = obtenerPrecioEnvio(); 
	    double descuento = calcularDescuento(carrito);
	    double subtotal = carrito.stream().mapToDouble(Detalle::getSubtotal).sum();
	    double total = calcularTotal(carrito, precioenvio, descuento);

	    // Actualizar atributos del modelo
	    model.addAttribute("carrito", carrito);
	    model.addAttribute("subtotal", subtotal);
	    model.addAttribute("precioenvio", precioenvio);
	    model.addAttribute("descuento", descuento);
	    model.addAttribute("total", total);

	    // Redirige a la página del carrito
	    return "redirect:/carrito";
	}

	// Método para obtener el precio de envío (implementa tu lógica)
	private double obtenerPrecioEnvio() {	   
	    return 10.0; 
	}

	// Método para calcular el descuento
	private double calcularDescuento(List<Detalle> carrito) {
	    double subtotal = carrito.stream().mapToDouble(Detalle::getSubtotal).sum();
	    return subtotal * 0.05;
	}	
	
	// Inicializacion de variable de la sesion
	@ModelAttribute("carrito")
	public List<Detalle> getCarrito() {
		return new ArrayList<Detalle>();
	}
	
	@ModelAttribute("total")
	public double getTotal() {
		return 0.0;
	}	
	
	@ModelAttribute("subtotal")
	public double getsubTotal() {
		return 0.0;
	}	
	
	@ModelAttribute("precioenvio")
	public double getPrecioEnvio() {			
		return 10.0;
	}
	
	@ModelAttribute("descuento")
	public double getDescuento() {
		return 0.00;
	}
}
