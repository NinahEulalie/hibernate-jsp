package servlet;

import dao.EmployeDAO;
import model.EmployeModel;

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
@WebServlet("/EmployeServlet")
public class EmployeServlet extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
    private EmployeDAO employeDAO = new EmployeDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
        	listAllEmployes(request, response);
        } else {
            switch (action) {
                case "/deleteEmploye":
                    deleteEmploye(request, response);
                    break;
                case "/showFormEmploye":
                    showEditForm(request, response);
                    break;
                default:
                	listAllEmployes(request, response);
                    break;
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "/addEmploye":
                    addEmploye(request, response);
                    break;
                case "/updateEmploye":
                    updateEmploye(request, response);
                    break;
            }
        }
    }

    // 🔹 Afficher la liste des employés
    private void listAllEmployes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<EmployeModel> employes = employeDAO.getAllEmployes();

        // Vérification en console
        if (employes == null) {
            System.out.println("⚠ ERREUR: La liste des employés est NULL !");
            employes = new ArrayList<>(); // Évite NullPointerException
        } else {
            System.out.println("✅ Nombre d'employés récupérés : " + employes.size());
            for (EmployeModel emp : employes) {
                System.out.println(" - " + emp.getCodeemp() + " | " + emp.getNom() + " | " + emp.getPrenom() + " | " + emp.getPoste());
            }
        }

        request.setAttribute("employes", employes);
        request.getRequestDispatcher("employe.jsp").forward(request, response);
    }
    
    // 🔹 Ajouter un employé
    private void addEmploye(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String poste = request.getParameter("poste");

        EmployeModel employe = new EmployeModel(nom, prenom, poste);
        employeDAO.saveEmploye(employe);

        response.sendRedirect("listEmployes");
    }

    // 🔹 Afficher le formulaire de modification
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long codeemp = Long.parseLong(request.getParameter("codeemp"));
        EmployeModel employe = employeDAO.getEmployeById(codeemp);

        request.setAttribute("employe", employe);
        request.getRequestDispatcher("employe-form.jsp").forward(request, response);
    }

    // 🔹 Modifier un employé
    private void updateEmploye(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long codeemp = Long.parseLong(request.getParameter("codeemp"));
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String poste = request.getParameter("poste");

        EmployeModel employe = employeDAO.getEmployeById(codeemp);
        employe.setNom(nom);
        employe.setPrenom(prenom);
        employe.setPoste(poste);

        employeDAO.updateEmploye(employe);
        response.sendRedirect("listEmployes");
    }

    // 🔹 Supprimer un employé
    private void deleteEmploye(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long codeemp = Long.parseLong(request.getParameter("codeemp"));
        employeDAO.deleteEmploye(codeemp);
        response.sendRedirect("listEmployes");
    }
}
