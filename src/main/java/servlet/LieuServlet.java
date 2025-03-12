package servlet;

import dao.LieuDAO;
import model.LieuModel;

//import jakarta.servlet.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@WebServlet("/LieuServlet")
public class LieuServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
    private LieuDAO lieuDAO;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "/deleteLieu":
                	deleteLieu(request, response);
                    break;
                case "/showFormLieu":
                    showEditForm(request, response);
                    break;
                default:
                	listAllLieux(request, response);
                    break;
            }
        } 
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "/addLieu":
                	addLieu(request, response);
                    break;
                case "/updateLieu":
                	updateLieu(request, response);
                    break;
            }
        } 

    }


    // 🔹 Afficher la liste des lieux
    private void listAllLieux(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<LieuModel> lieux = lieuDAO.getAllLieux();

        // Vérification en console
        if (lieux == null) {
            System.out.println("⚠ ERREUR: La liste des lieux est NULL !");
            lieux = new ArrayList<>(); // Évite NullPointerException
        } 
        else {
            System.out.println("✅ Nombre de lieux récupérés : " + lieux.size());
            for (LieuModel lieu : lieux) {
                System.out.println(" - " + lieu.getCodelieu() + " | " + lieu.getDesignation() + " | " + lieu.getProvince());
            }
        }

        request.setAttribute("lieux", lieux);
        request.getRequestDispatcher("lieu.jsp").forward(request, response);
    }
        
        
     // 🔹 Ajouter un lieu
        private void addLieu(HttpServletRequest request, HttpServletResponse response) throws IOException {
            String designation = request.getParameter("designation");
            String province = request.getParameter("province");

            LieuModel lieu = new LieuModel(designation, province);
            lieuDAO.saveLieu(lieu);

            response.sendRedirect("listLieux");
        }

        // 🔹 Afficher le formulaire de modification
        private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            Long codelieu = Long.parseLong(request.getParameter("codelieu"));
            LieuModel lieu = lieuDAO.getLieuById(codelieu);

            request.setAttribute("lieu", lieu);
            request.getRequestDispatcher("lieu-form.jsp").forward(request, response);
        }

        // 🔹 Modifier un lieu
        private void updateLieu(HttpServletRequest request, HttpServletResponse response) throws IOException {
            Long codelieu = Long.parseLong(request.getParameter("codelieu"));
            String designation = request.getParameter("designation");
            String province = request.getParameter("province");

            LieuModel lieu = lieuDAO.getLieuById(codelieu);
            lieu.setDesignation(designation);
            lieu.setProvince(province);

            lieuDAO.updateLieu(lieu);
            response.sendRedirect("listLieux");
        }

        // 🔹 Supprimer un lieu
        private void deleteLieu(HttpServletRequest request, HttpServletResponse response) throws IOException {
            Long codelieu = Long.parseLong(request.getParameter("codelieu"));
            lieuDAO.deleteLieu(codelieu);
            response.sendRedirect("listLieux");
        }
	
}
