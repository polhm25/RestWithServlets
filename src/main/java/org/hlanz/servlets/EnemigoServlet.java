package org.hlanz.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/enemigoservlet/*")
public class EnemigoServlet extends HttpServlet {
    private EnemigoService service = new EnemigoService();

    // GET - Obtener todos o uno por ID
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /enemigoservlet/ - Obtener todos
                String json = service.obtenerTodos();
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(json);

            } else {
                // GET /enemigoservlet/{id} - Obtener uno por ID
                String id = pathInfo.substring(1);
                String json = service.obtenerPorId(id);

                if (json.contains("\"error\"")) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                } else {
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
                resp.getWriter().write(json);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID inválido\"}");
        }
    }

    // POST - Crear nuevo enemigo
    /*
    Ejemplo de JSON para crear enemigo:
    {
      "nombre": "Nuevo Enemigo",
      "genero": "Masculino",
      "pais": "España",
      "afiliacion": "Ninguna",
      "activo": true
    }
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String json = leerBody(req);
            String resultado = service.crear(json);

            if (resultado.contains("\"error\"")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                resp.setStatus(HttpServletResponse.SC_CREATED);
            }
            resp.getWriter().write(resultado);

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Datos inválidos\"}");
        }
    }

    // PUT - Actualizar enemigo existente
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID requerido\"}");
            return;
        }

        try {
            String id = pathInfo.substring(1);
            String json = leerBody(req);
            String resultado = service.actualizar(id, json);

            if (resultado.contains("\"error\"")) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } else {
                resp.setStatus(HttpServletResponse.SC_OK);
            }
            resp.getWriter().write(resultado);

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Datos inválidos\"}");
        }
    }

    // DELETE - Eliminar enemigo
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID requerido\"}");
            return;
        }

        try {
            String id = pathInfo.substring(1);
            String resultado = service.eliminar(id);

            if (resultado.contains("\"error\"")) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(resultado);
            } else {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID inválido\"}");
        }
    }

    // Método auxiliar para leer el body de la petición
    private String leerBody(HttpServletRequest req) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }
}
