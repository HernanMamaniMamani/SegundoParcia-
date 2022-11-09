package com.emergentes.controlador;

import com.emergentes.modelo.Producto;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet{
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        HttpSession ses = request.getSession();
        
        if (ses.getAttribute("listaper") == null) {
            ArrayList<Producto> listaaux = new ArrayList<Producto>();
            ses.setAttribute("listaper", listaaux);
        }
        
        ArrayList<Producto> lista =  (ArrayList<Producto>) ses.getAttribute("listaper");
        
        String op = request.getParameter("op");
        String opcion = (op != null) ? op: "view";  
        
        Producto prod1 = new Producto();
        int id, pos;
        
        switch (opcion) {
            case "nuevo":
                request.setAttribute("miProducto", prod1);
                request.getRequestDispatcher("editar.jsp").forward(request, response);
                break;
                
            case "editar":
                id = Integer.parseInt(request.getParameter("id"));
                pos = buscarIndice(request,id);
                prod1 = lista.get(pos);
                request.setAttribute("miProducto", prod1);
                request.getRequestDispatcher("editar.jsp").forward(request, response);
                break;
              
            case "eliminar":
                id = Integer.parseInt(request.getParameter("id"));
                pos = buscarIndice(request,id);
                lista.remove(pos);
                ses.setAttribute("listaper", lista);
                response.sendRedirect("index.jsp");
                break;
                
            case "view":
                response.sendRedirect("index.jsp");
                break;
        }
                
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession ses = request.getSession();
        ArrayList<Producto> lista = (ArrayList<Producto>) ses.getAttribute("listaper");
        
        Producto prod1 = new Producto();
        prod1.setId(Integer.parseInt(request.getParameter("id")));
        prod1.setDescripcion(request.getParameter("descripcion"));
        prod1.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        prod1.setPrecio(Double.parseDouble(request.getParameter("precio")));
        prod1.setCategoria((request.getParameter("categoria")));
        
        int idt = prod1.getId();
        if (idt == 0) {
            int ultID = ultimoId(request);
            prod1.setId(ultID);
            lista.add(prod1);
        }else{
            //Edicion
            lista.set(buscarIndice(request,idt),prod1);
        }
        ses.setAttribute("listaper", lista);
        response.sendRedirect("index.jsp");
    }

    private int buscarIndice(HttpServletRequest request, int id){
        HttpSession ses = request.getSession();
        ArrayList<Producto> lista = (ArrayList<Producto>) ses.getAttribute("listaper");
        int i=0;
        if (!lista.isEmpty()) {
            while(i < lista.size()){
                if (lista.get(i).getId() == id) {
                    break;
                }else{
                    i++;
                }
            }      
        }
        return i;
    }
    
    public int ultimoId(HttpServletRequest request){
        HttpSession ses = request.getSession();
        ArrayList<Producto> lista = (ArrayList<Producto>) ses.getAttribute("listaper");
        
        int idaux = 0;
        
        for (Producto item : lista) {
            idaux = item.getId();
        }
        return idaux + 1;
    }
    
}
