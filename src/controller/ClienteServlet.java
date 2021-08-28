package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Cliente;
import persistence.ClienteDao;
import persistence.IClienteDao;

@WebServlet("/cliente")
public class ClienteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ClienteServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String saida = "";
		String erro = "";
		Cliente cli = new Cliente();

		IClienteDao cDao = null;
		try {
			cDao = new ClienteDao();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		String cmd = request.getParameter("button");
//		System.out.println(cmd);
		
		if (cmd.contains("Cadastrar")) {
			//TODO trocar o valida campos de boolean para Cliente
			cli = validaCampos(request, cmd);
			if (cli != null) {
				try {
					saida = cDao.insereCliente(cli);
					cli = null;
				} catch (SQLException e) {
					erro = e.getMessage();
				}
			} else {
				erro = "Preencha os campos";
			}
		}
		if (cmd.contains("Atualizar")) {
			cli = validaCampos(request, cmd);
			if (cli != null) {
				try {
					saida = cDao.atualizaCliente(cli);
					cli = null;
				} catch (SQLException e) {
					erro = e.getMessage();
				}
			} else {
				erro = "Preencha os campos";
			}
		}
		if (cmd.contains("Exclui")) {
			cli = validaCampos(request, cmd);
			if (cli != null) {
				try {
					saida = cDao.excluiCliente(cli);
					cli = null;
				} catch (SQLException e) {
					erro = e.getMessage();
				}
			} else {
				erro = "Preencha os campos";
			}
		}
		if (cmd.contains("Buscar")) {
			if (!request.getParameter("cpf").trim().isEmpty()) {
				try {
					cli.setCpf(request.getParameter("cpf"));
					cli = cDao.consultaCliente(cli);
				} catch (SQLException e) {
					erro = e.getMessage();
				}
			} else {
				erro = "Preencha os campos";
			}
		}
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		request.setAttribute("cliente", cli);
		request.setAttribute("saida", saida);
		request.setAttribute("erro", erro);
		rd.forward(request, response);
	}

	private Cliente validaCampos(HttpServletRequest request, String cmd) {
		if (cmd.contains("Cadastrar") || cmd.contains("Atualizar")) {
			if (request.getParameter("cpf").trim().isEmpty() ||
				request.getParameter("nome").trim().isEmpty()||
				request.getParameter("logradouro").trim().isEmpty()
				) {
				return null;
			} else {
				Cliente cli = new Cliente();
				cli.setCpf(request.getParameter("cpf").trim());
				cli.setNome(request.getParameter("nome").trim());
				cli.setLogradouro(request.getParameter("logradouro").trim());
				cli.setNumero(Integer.parseInt(request.getParameter("numero").trim()));
				return cli;
			}
		}
		if (cmd.contains("Excluir")) {
			if (request.getParameter("cpf").trim().isEmpty()) {
				return null;
			} else {
				Cliente cli = new Cliente();
				cli.setCpf(request.getParameter("cpf").trim());
				cli.setNome(request.getParameter("nome").trim());
				cli.setLogradouro(request.getParameter("logradouro").trim());
				cli.setNumero(Integer.parseInt(request.getParameter("numero").trim()));
				return cli;
			}
		}
		return null;
	}

}
