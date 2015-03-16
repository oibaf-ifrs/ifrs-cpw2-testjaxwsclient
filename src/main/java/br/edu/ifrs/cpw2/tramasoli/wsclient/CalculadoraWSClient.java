/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.cpw2.tramasoli.wsclient;

import com.tramasoli.testjaxwsclient.wsclient.Calculadora;
import com.tramasoli.testjaxwsclient.wsclient.Math;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet interface para ws Calculadora.
 * A Servlet pega dinamicamente os métodos com a anotação @WebMethod para listar
 * as operações disponíveis aos clientes, não sendo necessária muita intervenção
 * quando adicionados novos métodos ao WS.
 * @author fabio
 */
public class CalculadoraWSClient extends HttpServlet {
    
    int result=0;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CalculadoraWSClient</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CalculadoraWSClient at " + request.getContextPath() + "</h1>");
            out.println("<form method=\"POST\">");
            out.println("<label for=\"op\">Operation: </label>");
            out.println("<select required name=\"op\" id=\"op\">");
            for(Method method : Math.class.getMethods()) {
                if( method.getAnnotation(javax.jws.WebMethod.class) != null )
                {
                    out.println("<option value=\""+method.getName()+"\">"+method.getName()+"</option>");
                }
            }
            out.println("</select><br>");
            out.println("<label for=\"x\">First number: </label>");
            out.println("<input required name=\"x\" id=\"x\" type=\"number\"><br>");
            out.println("<label for=\"y\">Second number: </label>");
            out.println("<input required name=\"y\" id=\"y\" type=\"number\"><br>");
            out.println("<input type=\"reset\">");
            out.println("<input type=\"submit\">");
            out.println("</form>");
            if(request.getMethod().equals("POST")) {
                out.println("<h1>Result: "+this.result+"</h1>");
            }
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    /***
     * 
     * @param num1
     * @param num2
     * @param methodName 
     * @throws java.lang.IllegalAccessException 
     * @throws java.lang.reflect.InvocationTargetException 
     */
    protected void callWS(int num1, int num2, String methodName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Calculadora wsClient = new Calculadora();
        for(Method method : Math.class.getMethods()) {
            if (method.getName().equals(methodName))
            {
                result = (int)method.invoke(wsClient.getMathPort(), num1, num2);
                break;
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            callWS(Integer.parseInt(request.getParameter("x")),Integer.parseInt(request.getParameter("y")),request.getParameter("op"));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(CalculadoraWSClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
