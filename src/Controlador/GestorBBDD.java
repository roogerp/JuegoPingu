package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Modelo.Partida;
import Modelo.Pinguino;
import Modelo.*;

/*
 * Clase encargada de gestionar toda la conexión
 * entre el juego y la base de datos Oracle.
 */

public class GestorBBDD {

	private final String url, user, pass;
	private Connection con;

	public boolean validarUsuario(String usuario, String pass) {

	    // Encripta la contraseña antes de compararla
	    String passEncriptada = encriptar(pass); 
	    
	    String sql = "SELECT * FROM USUARIOS WHERE username = ? AND password = ?";

	    try (PreparedStatement ps = con.prepareStatement(sql)) {

	        // Evita SQL Injection usando PreparedStatement
	        ps.setString(1, usuario);
	        ps.setString(2, passEncriptada);

	        ResultSet rs = ps.executeQuery();
	        
	        // Devuelve true si el usuario existe
	        return rs.next();

	    } catch (SQLException e) {

	        System.err.println("Error al validar usuario: " + e.getMessage());
	        return false;
	    }
	}

	
	public GestorBBDD() {

		// Datos de conexión a Oracle
		this.url = "jdbc:oracle:thin:@192.168.3.26:1521/XEPDB2";
		this.user = "DM1_2526_GRUP05";
		this.pass = "AGRUP05";
	}

	public boolean conectar() {

		// Abre conexión con Oracle
		try {

			con = DriverManager.getConnection(url, user, pass);
			System.out.println("Conexión exitosa a Oracle");

			return true;

		} catch (SQLException e) {

			System.err.println("Error de conexión: " + e.getMessage());
			return false;
		}
	}

	public void desconectar() {

		// Cierra la conexión de forma segura
		try {

			if (con != null && !con.isClosed()) con.close();

		} catch (SQLException e) {

			System.err.println("Error al cerrar: " + e.getMessage());
		}
	}

	// Guarda toda la información de la partida
	public int guardarBBDD(Partida p) {

	    int idPartida = -1;

	    // Convierte el tablero en texto para guardarlo
	    StringBuilder estado = new StringBuilder();

	    for (Casilla c : p.getTablero().getCasillas()) {

	        if (c instanceof Oso) estado.append("O");
	        else if (c instanceof Agujero) estado.append("A");
	        else if (c instanceof Trineo) estado.append("T");
	        else if (c instanceof Interrogante) estado.append("I");
	        else estado.append("N"); 

	        estado.append(",");
	    }

	    String sql = "INSERT INTO PARTIDAS (id, tamanyo_tablero, estado_tablero, turno_actual) VALUES (SEQ_PARTIDAS.NEXTVAL, ?, ?, ?)";

	    try (PreparedStatement psP = con.prepareStatement(sql, new String[] {"ID"})) {

	        psP.setInt(1, p.getTablero().getTamaño());
	        psP.setString(2, estado.toString());
	        psP.setInt(3, 0);

	        psP.executeUpdate();

	        ResultSet rs = psP.getGeneratedKeys();

	        // Obtiene el ID generado automáticamente
	        if (rs.next()) idPartida = rs.getInt(1);

	        // Guarda todos los jugadores
	        for (Pinguino pg : p.getJugadores()) {
	            guardarPinguino(pg, idPartida);
	        }

	        System.out.println("Partida guardada exitosamente.");

	    } catch (SQLException e) {

	        System.err.println("Error al guardar en la BD: " + e.getMessage());
	    }

	    return idPartida;
	}

	private void guardarPinguino(Pinguino pg, int idPartida) throws SQLException {

		// Guarda cada jugador en la base de datos
		String sql = "INSERT INTO PINGUINOS (id, id_partida, nombre, color, posicion, peces, bolas_nieve) VALUES (SEQ_PINGUINOS.NEXTVAL, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement ps = con.prepareStatement(sql)) {

		    ps.setInt(1, idPartida);
		    ps.setString(2, pg.getNombre());
		    ps.setString(3, pg.getColor());
		    ps.setInt(4, pg.getPosicion());
		    ps.setInt(5, pg.getInventario().getPeces());
		    ps.setInt(6, pg.getInventario().getBolas());

		    ps.executeUpdate();
		}
	}

	// Carga una partida desde la base de datos
	public Partida cargarBBDD(int id) {

	    try {

	        PreparedStatement ps = con.prepareStatement("SELECT * FROM PARTIDAS WHERE id = ?");
	        ps.setInt(1, id);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {

	            // Recupera el estado del tablero
	            String tableroLimpio = rs.getString("estado_tablero");

	            if (tableroLimpio == null) tableroLimpio = "";

	            String[] tiposCasillas = tableroLimpio.split(",");

	            Partida partida = new Partida(tiposCasillas.length);

	            // Reconstruye el tablero
	            partida.getTablero().getCasillas().clear();

	            for (int i = 0; i < tiposCasillas.length; i++) {

	                String tipo = tiposCasillas[i].trim();

	                if (tipo.isEmpty()) continue;

	                switch (tipo) {

	                    case "O": partida.getTablero().getCasillas().add(new Oso(i)); break;
	                    case "A": partida.getTablero().getCasillas().add(new Agujero(i)); break;
	                    case "T": partida.getTablero().getCasillas().add(new Trineo(i)); break;
	                    case "I": partida.getTablero().getCasillas().add(new Interrogante(i)); break;

	                    default: partida.getTablero().getCasillas().add(new Nieve(i)); break;
	                }
	            }

	            // Carga todos los jugadores
	            for (Pinguino pg : cargarPinguinos(id)) {
	                partida.añadirJugador(pg);
	            }

	            return partida;
	        }

	    } catch (SQLException e) {

	        System.err.println("Error al cargar partida: " + e.getMessage());
	    }

	    return null;
	}

	private ArrayList<Pinguino> cargarPinguinos(int idPartida) throws SQLException {

	    ArrayList<Pinguino> lista = new ArrayList<>();

	    String sql = "SELECT * FROM PINGUINOS WHERE id_partida = ? ORDER BY id";

	    try (PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, idPartida);

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {

	            // Reconstruye cada pingüino guardado
	            String nombreReal = rs.getString("nombre");

	            Pinguino p = new Pinguino(nombreReal, rs.getString("color"));

	            p.setPosicion(rs.getInt("posicion"));
	            p.getInventario().setPeces(rs.getInt("peces"));
	            p.getInventario().setBolas(rs.getInt("bolas_nieve"));

	            lista.add(p);
	        }
	    }

	    return lista;
	}

	// Encriptación básica usando Base64
	private String encriptar(String datos) {

	    if (datos == null) return "";

	    return java.util.Base64.getEncoder().encodeToString(datos.getBytes());
	}

	public boolean registrarUsuario(String usuario, String pass) {

	    // Encripta la contraseña antes de guardar
	    String passEncriptada = encriptar(pass);

	    String sql = "INSERT INTO USUARIOS (username, password) VALUES (?, ?)";

	    try (PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, usuario);
	        ps.setString(2, passEncriptada);

	        ps.executeUpdate();

	        System.out.println("Usuario " + usuario + " registrado con éxito.");

	        return true;

	    } catch (SQLException e) {

	        System.err.println("Error al registrar: " + e.getMessage());

	        return false;
	    }
	}

	private String desencriptar(String datos) {

	    if (datos == null || datos.isEmpty()) return "";

	    byte[] decodedBytes = java.util.Base64.getDecoder().decode(datos);

	    return new String(decodedBytes);
	}
}