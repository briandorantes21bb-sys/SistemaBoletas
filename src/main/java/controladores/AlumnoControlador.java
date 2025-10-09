/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;

import java.awt.HeadlessException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelos.Alumno;
import modelos.Secretario;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;


/**
 *
 * @author Ivan De la Rosa
 */
public class AlumnoControlador 
{
    public void MostrarAlumno(Connection conexionExistente, JTable Alumnos_tabla)
    {
        //Conexion ObjConexion = new Conexion();
        DefaultTableModel MiModeloTabla = new DefaultTableModel();
        //TableRowSorter<TableModel> OrdenarTabla = new TableRowSorter();
        
        //TablaAlumnos.setRowSorter(OrdenarTabla);
        
        String SQL = "SELECT * FROM alumnos;";
        // Hello!1
        MiModeloTabla.addColumn("ID");
        MiModeloTabla.addColumn("CURP");
        MiModeloTabla.addColumn("Nombre");
        MiModeloTabla.addColumn("Apellido");
        MiModeloTabla.addColumn("Edad");
        MiModeloTabla.addColumn("Genero");
        MiModeloTabla.addColumn("Nombre Padre");
        MiModeloTabla.addColumn("Apellido Padre");
        MiModeloTabla.addColumn("Correo Padre");
        MiModeloTabla.addColumn("Telefono Padre");
        MiModeloTabla.addColumn("Grupo");
        MiModeloTabla.addColumn("Fecha Nacimiento");
       
        
        Alumnos_tabla.setModel(MiModeloTabla);
        
        String[] datos = new String[12];
        Statement st;
        
        try
        {
            st = conexionExistente.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            
            while(rs.next())
            {
                datos[0]=rs.getString(1);
                datos[1]=rs.getString(2);
                datos[2]=rs.getString(3);
                datos[3]=rs.getString(4);
                datos[4]=rs.getString(5);
                datos[5]=rs.getString(6);
                datos[6]=rs.getString(7);
                datos[7]=rs.getString(8);
                datos[8]=rs.getString(9);
                datos[9]=rs.getString(10);
                datos[10]=rs.getString(11);
                datos[11]=rs.getString(12);
                
                datos[10]=this.CambiarIDPorGrupo(conexionExistente, datos[10]);
                
                MiModeloTabla.addRow(datos);
            }
            
            Alumnos_tabla.setModel(MiModeloTabla);
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "No se pudo mostrar: " + ex.toString());
        }
    }
    
    public String CambiarIDPorGrupo(Connection conexionExistente, String idGrupo)
    {
        String cambio = "";
        
        String SQL = "SELECT id_grupo, grupo FROM grupos;";
         
        String[] datos = new String[2];
        Statement st;
         
        try
        {
            st = conexionExistente.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            
            while(rs.next())
            {
                datos[0]=rs.getString(1);
                datos[1]=rs.getString(2);
                
                if(idGrupo.equals(datos[0]))
                {
                    cambio = datos[1];
                }
            }
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "No se encontró grupo: " + ex.toString());
        }
        return cambio;
    }
    
    public int CambiarGrupoPorID(Connection conexionExistente, String grupo)
    {
        if(grupo != null && grupo.matches("\\d[A-Z]"))
        {
            int cambio = 0;
        
            String SQL = "SELECT id_grupo, grupo FROM grupos;";

            String[] datos = new String[2];
            Statement st;

            try
            {
                st = conexionExistente.createStatement();
                ResultSet rs = st.executeQuery(SQL);

                while(rs.next())
                {
                    datos[0]=rs.getString(1);
                    datos[1]=rs.getString(2);

                    if(grupo.equals(datos[1]))
                    {
                        cambio = Integer.parseInt(datos[0]);
                    }
                }
            }
            catch(SQLException ex)
            {
                JOptionPane.showMessageDialog(null, "No se encontró grupo: " + ex.toString());
            }
            return cambio;
        }
        else
        {
            //JOptionPane.showMessageDialog(null, "Para un grupo solo use combinaciones de un número y una mayúscula \nEjemplo: 1A" );
            return 0;
        }
    }
    
    public void CargarAlumnoPorCURP(Connection conexion, String curp, 
        JTextField paramID, JTextField paramCURP, JTextField paramNombre, JTextField paramApellido,
        JTextField paramFecha, JTextField paramEdad, JComboBox<String> paramGenero, 
        JTextField paramNombreP, JTextField paramApellidoP, JTextField paramCorreoP, 
        JTextField paramTelefonoP, JTextField paramGrupo) {
    
    String sql = "SELECT id_alumno, curp, nombre, apellido, fecha_nacimiento, edad, genero, " +
                 "nombre_padre, apellido_padre, correo_padre, telefono_padre, id_grupo " +
                 "FROM alumnos WHERE curp = ?";

    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setString(1, curp);
        ResultSet rs = ps.executeQuery();
          if (rs.next()) {
            // Obtener el ID del grupo desde la base de datos
            int id_grupo = rs.getInt("id_grupo");
            String grupo;

            // Convertir el id_grupo en el nombre del grupo
            switch (id_grupo) {
                case 1: grupo = "1A"; break;
                case 3: grupo = "2A"; break;
                case 5: grupo = "3A"; break;
                case 7: grupo = "4A"; break;
                case 9: grupo = "5A"; break;
                case 11: grupo = "6A"; break;
                default: grupo = "Desconocido"; break;
            }

            // Asignar los valores a los JTextField
            paramID.setText(rs.getString("id_alumno"));
            paramCURP.setText(rs.getString("curp"));
            paramNombre.setText(rs.getString("nombre"));
            paramApellido.setText(rs.getString("apellido"));
            paramFecha.setText(rs.getString("fecha_nacimiento"));
            paramEdad.setText(rs.getString("edad"));
            paramGenero.setSelectedItem(rs.getString("genero"));
            paramNombreP.setText(rs.getString("nombre_padre"));
            paramApellidoP.setText(rs.getString("apellido_padre"));
            paramCorreoP.setText(rs.getString("correo_padre"));
            paramTelefonoP.setText(rs.getString("telefono_padre"));
            paramGrupo.setText(grupo);
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró el alumno con CURP: " + curp);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al cargar los datos del alumno: " + e.getMessage());
    }
}

    
    public void SeleccionarAlumno(JTextField paramID, JTextField paramCURP, JTextField paramNombre, JTextField paramApellido,JTextField paramFecha, JTextField paramEdad, JComboBox<String> paramGenero, JTextField paramNombreP, JTextField paramApellidoP, JTextField paramCorreoP, JTextField paramTelefonoP, JTextField paramGrupo, JTable paramTabla)
    {
        try
        {
            int fila = paramTabla.getSelectedRow();
            
            if (fila >= 0)
            {
                paramID.setText(paramTabla.getValueAt(fila, 0).toString());
                paramCURP.setText(paramTabla.getValueAt(fila, 1).toString());
                paramNombre.setText(paramTabla.getValueAt(fila, 2).toString());
                paramApellido.setText(paramTabla.getValueAt(fila, 3).toString());
                paramEdad.setText(paramTabla.getValueAt(fila, 4).toString());
                paramGenero.setSelectedItem(paramTabla.getValueAt(fila, 5).toString());
                paramNombreP.setText(paramTabla.getValueAt(fila, 6).toString());
                paramApellidoP.setText(paramTabla.getValueAt(fila, 7).toString());
                paramCorreoP.setText(paramTabla.getValueAt(fila, 8).toString());
                paramTelefonoP.setText(paramTabla.getValueAt(fila, 9).toString());
                paramGrupo.setText(paramTabla.getValueAt(fila, 10).toString());
                 paramFecha.setText(paramTabla.getValueAt(fila, 11).toString());
            }
            else
            {
             //   JOptionPane.showMessageDialog(null, "Fila no seleccionada");
            }
        }
        catch(HeadlessException ex)
        {
            //JOptionPane.showMessageDialog(null, "No se pudo selecionar: " + ex.toString());
        }
    }
    
    public boolean ValidarDatos(Connection conexionExistente,
                            JTextField paramCURP,
                            JTextField paramNombre,
                            JTextField paramApellido,
                            JTextField paramFecha,
                            JTextField paramEdad,
                            JComboBox<String> paramGenero,
                            JTextField paramNombreP,
                            JTextField paramApellidoP,
                            JTextField paramCorreoP,
                            JTextField paramTelefonoP,
                             JTextField paramGrupo) 
{
        // --- 1) CURP: normalizar y validar formato ---
        String curp = paramCURP.getText();
        if (curp == null) curp = "";
        curp = curp.trim().toUpperCase().replaceAll("\\s+", "");

        if (!curp.matches("^[A-Z]{4}\\d{6}[HM][A-Z]{5}[0-9A-Z]{2}$")) {
            JOptionPane.showMessageDialog(null, "Formato de CURP inválido. Debe ser AAAA######H... (sin espacios).");
            marcarError(paramCURP, true);
            return false;
        }

        // --- 2) Normalizar nombres/apellidos (quitar acentos, collapse espacios, dejar solo letras y espacios) ---
        String nombreNorm = normalizarTexto(paramNombre.getText());
        String apellidosNorm = normalizarTexto(paramApellido.getText());

        if (nombreNorm.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre es obligatorio.");
            marcarError(paramNombre, true);
            return false;
        }
        if (apellidosNorm.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Los apellidos son obligatorios.");
            marcarError(paramApellido, true);
            return false;
        }

        // --- 3) Validaciones de formato (permitir espacios) ---
        if (!paramNombre.getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            JOptionPane.showMessageDialog(null, "Los nombres solo pueden contener letras y espacios");
            marcarError(paramNombre, true);
            return false;
        }
        if (!paramApellido.getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            JOptionPane.showMessageDialog(null, "Los apellidos solo pueden contener letras y espacios");
            marcarError(paramApellido, true);
            return false;
        }

        // --- 4) Comparar las 4 letras de la CURP con nombre y apellidos ---
        // Separar apellidos (un solo campo). Tomamos primer token como primer apellido y segundo token como segundo apellido si existe.
        String[] tokensAp = apellidosNorm.split("\\s+");
        String primerApellido = tokensAp.length > 0 ? tokensAp[0] : "";
        String segundoApellido = tokensAp.length > 1 ? tokensAp[1] : "";

        // Primera letra
        String letra1 = primerApellido.length() > 0 ? primerApellido.substring(0, 1) : "X";
        // Primera vocal interna del primer apellido (desde índice 1)
        String vocalInterna = "X";
        for (int i = 1; i < primerApellido.length(); i++) {
            char c = primerApellido.charAt(i);
            if ("AEIOU".indexOf(c) != -1) {
                vocalInterna = String.valueOf(c);
                break;
            }
        }
        // Primera letra del segundo apellido
        String letra2 = segundoApellido.isEmpty() ? "X" : segundoApellido.substring(0, 1);

        // Nombre: si el primer token es "JOSE"/"MARIA" usamos el segundo token cuando exista
        String[] tokensNombre = nombreNorm.split("\\s+");
        String nombreUsado = tokensNombre[0];
        if (tokensNombre.length > 1) {
            String primer = nombreUsado;
            if (primer.equals("JOSE") || primer.equals("J") || primer.equals("MARIA") || primer.equals("MA")) {
                nombreUsado = tokensNombre[1];
            }
        }
        String letra3 = nombreUsado.length() > 0 ? nombreUsado.substring(0, 1) : "X";

        String letrasEsperadas = (letra1 + vocalInterna + letra2 + letra3).toUpperCase();
        String letrasCurp = curp.substring(0, 4);

        if (!letrasCurp.equals(letrasEsperadas)) {
            JOptionPane.showMessageDialog(null,
                "La CURP no coincide con nombre y apellidos.\nEsperado: " + letrasEsperadas + "\nCURP: " + letrasCurp);
            marcarError(paramNombre, true);
            marcarError(paramApellido, true);
            marcarError(paramCURP, true);
            return false;
        }
        //rellenar fecha de nacimiento y edad
        String yy = curp.substring(4, 6);
        String mm = curp.substring(6, 8);
        String dd = curp.substring(8, 10);
        int fullYear;
           int idGrupoCalculado = 0;
        try {
            int year = Integer.parseInt(yy);
            int month = Integer.parseInt(mm);
            int day = Integer.parseInt(dd);
            int currentTwoDigits = LocalDate.now().getYear() % 100;
             
            fullYear = (year <= currentTwoDigits) ? (2000 + year) : (1900 + year);
            LocalDate fechaNacimiento = LocalDate.of(fullYear, month, day);
             int edadCalculada = Period.between(fechaNacimiento, LocalDate.now()).getYears();
             paramEdad.setText(String.valueOf(edadCalculada));
            
          DateTimeFormatter formatoMostrar = DateTimeFormatter.ofPattern("dd-MM-yyyy");
          paramFecha.setText(fechaNacimiento.format(formatoMostrar));


            // escribir la edad calculada en el campo paramEdad
            paramEdad.setText(String.valueOf(edadCalculada));


        
            

      
            //fullYear = (year <= currentTwoDigits) ? (2000 + year) : (1900 + year);

           

            // **********************************************************
            // ***** INICIO DE LA LÓGICA DE ASIGNACIÓN DE idGrupo *****
            // **********************************************************
            switch (edadCalculada) {
                case 6: idGrupoCalculado = 1; break;  // 1A
                case 7: idGrupoCalculado = 3; break;  // 2A
                case 8: idGrupoCalculado = 5; break;  // 3A
                case 9: idGrupoCalculado = 7; break;  // 4A
                case 10: idGrupoCalculado = 9; break; // 5A
                case 11: idGrupoCalculado = 11; break;// 6A
            }

            if (idGrupoCalculado > 0) {
                // Usamos la función que ya tienes para obtener el nombre del grupo (ej. "1A")
                String nombreGrupo = this.CambiarIDPorGrupo(conexionExistente, String.valueOf(idGrupoCalculado));
                // Actualizamos la interfaz para que el usuario lo vea
                paramGrupo.setText(nombreGrupo);
            } else {
                JOptionPane.showMessageDialog(null, "Edad no válida (" + edadCalculada + " años). El alumno no corresponde a ningún grado.");
                return false; // Falla la validación
            }
            // **********************************************************
            // ***** FIN DE LA LÓGICA DE ASIGNACIÓN DE idGrupo ******
            // **********************************************************

        } catch (NumberFormatException | DateTimeException e) {
            JOptionPane.showMessageDialog(null, "Fecha de nacimiento inválida en CURP: " + e.getMessage());
            return false; // Falla la validación
        }

        // --- 7) Género ---
    String genero = "";
    if (paramGenero.getSelectedItem() != null) {
        genero = paramGenero.getSelectedItem().toString().trim().toUpperCase();
    }

    if (!(genero.equals("M") || genero.equals("F") || genero.equals("MASCULINO") || genero.equals("FEMENINO"))) {
        JOptionPane.showMessageDialog(null, "Selecciona un género válido (Masculino o Femenino)");
        return false;
    }


            // --- 8) Datos del padre/madre (permitir espacios en nombre/apellidos) ---
            if (!(paramNombreP.getText() != null && paramNombreP.getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+"))) {
                JOptionPane.showMessageDialog(null, "El nombre del padre/madre solo puede contener letras y espacios");
                marcarError(paramNombreP, true);
                return false;
            }
            if (!(paramApellidoP.getText() != null && paramApellidoP.getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+"))) {
                JOptionPane.showMessageDialog(null, "El apellido del padre/madre solo puede contener letras y espacios");
                marcarError(paramNombreP, true);
                return false;
            }

            //Correo y teléfono padre-----------------------------------------------------------------------
            if ((paramCorreoP.getText() != null 
                    && paramCorreoP.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) 
                && (paramTelefonoP.getText() != null 
                    && paramTelefonoP.getText().matches("\\d{10}"))) 
            {
                //Validacion de duplicados
                String correo = paramCorreoP.getText();
                String telefono = paramTelefonoP.getText();
                String SQL_padre = "SELECT COUNT(*) FROM alumnos WHERE correo_padre = ? OR telefono_padre = ?;";

                try (var ps = conexionExistente.prepareStatement(SQL_padre)) 
                {
                    ps.setString(1, correo);
                    ps.setString(2, telefono);
                    try (var rs = ps.executeQuery()) 
                    {
                        if (rs.next() && rs.getInt(1) > 0) 
                        {
                           // JOptionPane.showMessageDialog(null, "No puede haber correos o teléfonos repetidos.");
                              marcarError(paramCorreoP, true);
                              marcarError(paramTelefonoP, true);
                            return false;
                        }
                    }
                } 
                catch (SQLException e) 
                {
                    JOptionPane.showMessageDialog(null, "Error al validar los datos: " + e.getMessage());
                    return false;
                }

} 
else 
{
    JOptionPane.showMessageDialog(null, "El correo o el teléfono no son válidos. El número debe tener exactamente 10 dígitos.");
    return false;
}

            // Todo validado
            return true;
        }

    // -------------------- Helper: normalizar texto (quitar acentos, collapse espacios) --------------------
    private String normalizarTexto(String s) {
        if (s == null) return "";
        String t = s.trim().toUpperCase();
        t = Normalizer.normalize(t, Normalizer.Form.NFD).replaceAll("\\p{M}", ""); // quita acentos
        t = t.replaceAll("[^A-Z\\s]", ""); // deja solo letras A-Z y espacios
        t = t.replaceAll("\\s+", " ").trim();
        return t;
    }

    // -------------------- Helper: buscar id de grupo por nombre --------------------
    public int ObtenerIdGrupoPorNombre(Connection conexion, int nombreGrupo) {
        String sql = "SELECT id_grupo FROM grupos WHERE nombre_grupo = ?;";
        try (var ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, nombreGrupo);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_grupo");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener el ID del grupo " + nombreGrupo + ": " + ex.getMessage());
        }
        return -1;
    }
   
    
    public int ObtenerGrado(Connection conexion, String idGrupo) 
    {
        String sql = "SELECT grado FROM grupos WHERE id_grupo = ?;";
        
       
        try (var ps = conexion.prepareStatement(sql)) 
        {
            ps.setString(1, idGrupo);
            try (var rs = ps.executeQuery()) 
            {
                if (rs.next()) 
                {
                    return rs.getInt("grado");
                }
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "No se encontró el ID del grupo.");
        }
        return -1; // Retorna -1 si no se encuentra el grupo.
    }
    
    public void FiltrarAlumnos(Connection conexion, 
                           javax.swing.JTable tabla_bt, 
                           javax.swing.JTextField Grupo_tb, 
                           java.awt.TextField grupo_n, 
                           java.awt.TextField nombre_maestro,
                           javax.swing.JTable materias_tb) {
    String grupoTexto = Grupo_tb.getText().trim();

   

    try {
        // Consulta SQL para obtener alumnos y datos del maestro del grupo
        String sqlAlumnos = "SELECT a.nombre, a.apellido, a.curp, g.nombre_maestro, g.apellido_maestro, g.idPaqueteMaterias " +
                            "FROM alumnos a " +
                            "INNER JOIN grupos g ON a.id_grupo = g.id_grupo " +
                            "WHERE g.grupo = ? " +
                            "ORDER BY a.apellido ASC";

        try (PreparedStatement ps = conexion.prepareStatement(sqlAlumnos)) {
            ps.setString(1, grupoTexto);
            ResultSet rs = ps.executeQuery();

            // Modelo de tabla para los alumnos
             javax.swing.table.DefaultTableModel modeloActual = (javax.swing.table.DefaultTableModel) tabla_bt.getModel();
            modeloActual.setRowCount(0);
            javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel();
            // Limpia todas las filas previas

            modelo.addColumn("No.Lista");
            modelo.addColumn("Apellidos");
            modelo.addColumn("Nombre");
            modelo.addColumn("CURP");

            int contador = 1;
            String nombreMaestro = "";
            String apellidoMaestro = "";
            int idPaqueteMaterias = -1; // ID del paquete de materias del grupo

            while (rs.next()) {
                if (nombreMaestro.isEmpty() && apellidoMaestro.isEmpty()) {
                    nombreMaestro = rs.getString("nombre_maestro");
                    apellidoMaestro = rs.getString("apellido_maestro");
                    idPaqueteMaterias = rs.getInt("idPaqueteMaterias");
                }

                modelo.addRow(new Object[]{
                    contador,
                    rs.getString("apellido"),
                    rs.getString("nombre"),
                    rs.getString("curp")
                });
                contador++;
            }

            if (modelo.getRowCount() == 0) {
                modelo.addRow(new Object[]{"-", "No se encontraron alumnos", "-", "-"});
            }

            // Asignar modelo a la tabla de alumnos
            tabla_bt.setModel(modelo);
            tabla_bt.setShowGrid(true);
            tabla_bt.setGridColor(java.awt.Color.GRAY);
            tabla_bt.setFillsViewportHeight(true);

            // Mostrar el grupo y nombre del maestro
            grupo_n.setText(grupoTexto);
            nombre_maestro.setText((nombreMaestro.isEmpty() ? "" : nombreMaestro + " " + apellidoMaestro));

            // Cargar materias en el JTable
            javax.swing.table.DefaultTableModel modeloMaterias = new javax.swing.table.DefaultTableModel();
            modeloMaterias.addColumn("MATERIAS");
            materias_tb.setOpaque(false);
            ((javax.swing.JScrollPane) materias_tb.getParent().getParent()).setOpaque(false);
            ((javax.swing.JScrollPane) materias_tb.getParent().getParent()).getViewport().setOpaque(false);
            materias_tb.setBackground(new java.awt.Color(0, 0, 0, 0)); // transparente


            if (idPaqueteMaterias != -1) {
                String sqlMaterias = "SELECT materia1, materia2, materia3, materia4, materia5, materia6, materia7, materia8 " +
                                     "FROM paquetematerias WHERE id = ?";
                try (PreparedStatement psMat = conexion.prepareStatement(sqlMaterias)) {
                    psMat.setInt(1, idPaqueteMaterias);
                    ResultSet rsMat = psMat.executeQuery();
                    if (rsMat.next()) {
                        for (int i = 1; i <= 8; i++) {
                            String materia = rsMat.getString("materia" + i);
                            if (materia != null && !materia.isEmpty()) {
                                modeloMaterias.addRow(new Object[]{materia});
                            }
                        }
                    }
                }
            }

            // Asignar modelo al JTable de materias
            materias_tb.setModel(modeloMaterias);
            materias_tb.setShowGrid(true);
            materias_tb.setGridColor(java.awt.Color.GRAY);
            materias_tb.setRowHeight(25);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al filtrar los alumnos o materias: " + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
    }
}
    
    public void FiltrarAlumno(Connection conexionExistente, JTable Alumnos_tabla, JTextField paramGrupo) {
        
        DefaultTableModel MiModeloTabla = new DefaultTableModel();
        
        MiModeloTabla.addColumn("ID");
        MiModeloTabla.addColumn("CURP");
        MiModeloTabla.addColumn("Nombre");
        MiModeloTabla.addColumn("Apellido");
        MiModeloTabla.addColumn("Edad");
        MiModeloTabla.addColumn("Genero");
        MiModeloTabla.addColumn("Nombre Padre");
        MiModeloTabla.addColumn("Apellido Padre");
        MiModeloTabla.addColumn("Correo Padre");
        MiModeloTabla.addColumn("Telefono Padre");
        MiModeloTabla.addColumn("Grupo");
        MiModeloTabla.addColumn("Fecha Nacimiento");
        
        Alumnos_tabla.setModel(MiModeloTabla);

        String SQL = "SELECT * FROM alumnos WHERE id_grupo = ?;";

        try 
        {
            int idGrupo = this.CambiarGrupoPorID(conexionExistente, paramGrupo.getText());

            try (PreparedStatement ps = conexionExistente.prepareStatement(SQL)) 
            {
                // Asigna el valor del parámetro al placeholder
                ps.setInt(1, idGrupo);
                
                try (ResultSet rs = ps.executeQuery()) 
                {
                    String[] datos = new String[12];
                    while (rs.next()) {
                        // Obtiene los datos de cada columna por su nombre, es una práctica más segura
                        // y legible que usar índices numéricos.
                        datos[0] = rs.getString("id_alumno");
                        datos[1] = rs.getString("curp");
                        datos[2] = rs.getString("nombre");
                        datos[3] = rs.getString("apellido");
                        datos[4] = rs.getString("edad");
                        datos[5] = rs.getString("genero");
                        datos[6] = rs.getString("nombre_padre");
                        datos[7] = rs.getString("apellido_padre");
                        datos[8] = rs.getString("correo_padre");
                        datos[9] = rs.getString("telefono_padre");
                        datos[10] = rs.getString("id_grupo");
                        datos[11] = rs.getString("fecha_nacimiento");

                        datos[10] = this.CambiarIDPorGrupo(conexionExistente, datos[10]);
                        
                        MiModeloTabla.addRow(datos);
                    }
                }
            }
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error SQL al filtrar alumnos: " + ex.toString());
        } 
        catch (NumberFormatException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error en el formato del grupo. " + ex.toString());
        }
    }

 public void InsertarAlumno(Connection conexionExistente,
                           JTextField paramCURP,
                           JTextField paramNombre,
                           JTextField paramApellido,
                           JTextField paramFecha,
                           JTextField paramEdad,
                           JComboBox<String> paramGenero,
                           JTextField paramNombreP,
                           JTextField paramApellidoP,
                           JTextField paramCorreoP,
                           JTextField paramTelefonoP,
                           JTextField paramGrupo)
{
   // int idGrupo; //= this.CambiarGrupoPorID(conexionExistente, paramGrupo.getText());
    //String Grupo = paramGrupo.getText();
    // Primero validamos los datos
     
    
    if (this.ValidarDatos(conexionExistente, paramCURP, paramNombre, paramApellido, paramFecha, paramEdad,  paramGenero, paramNombreP, paramApellidoP, paramCorreoP, paramTelefonoP, paramGrupo))

    {
        
        Alumno ObjAlumno = new Alumno();
        ObjAlumno.setCURP(paramCURP.getText());
        ObjAlumno.setNombre(paramNombre.getText());
        ObjAlumno.setApellido(paramApellido.getText());
        ObjAlumno.setFechaNacimiento(paramFecha.getText());
        ObjAlumno.setEdad(Integer.parseInt(paramEdad.getText()));
        ObjAlumno.setGenero(paramGenero.getSelectedItem().toString());
        ObjAlumno.setNombre_padre(paramNombreP.getText());
        ObjAlumno.setApellido_padre(paramApellidoP.getText());
        ObjAlumno.setCorreo_padre(paramCorreoP.getText());
        ObjAlumno.setTelefono_padre(paramTelefonoP.getText());

      //  ObjAlumno.setGrupo(this.CambiarGrupoPorID(conexionExistente, paramGrupo.getText()));
        int idGrupo = 0; //= this.CambiarGrupoPorID(conexionExistente, paramGrupo.getText());
        String Grupo = paramGrupo.getText();
        switch (Grupo) {
                    case "1A": idGrupo = 1; break;  // 1A
                    case "2A": idGrupo = 3; break;  // 2A
                    case "3A": idGrupo = 5; break;  // 3A
                    case "4A": idGrupo = 7; break;  // 4A
                    case "5A": idGrupo = 9; break; // 5A
                    case "6A": idGrupo = 11; break;// 6A
                }
            ObjAlumno.setGrupo(idGrupo);


        String Consulta = "INSERT INTO alumnos (curp, nombre, apellido, edad, genero, " +
                          "nombre_padre, apellido_padre, correo_padre, telefono_padre, id_grupo, fecha_nacimiento) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (CallableStatement cs = conexionExistente.prepareCall(Consulta)) {

            cs.setString(1, ObjAlumno.getCURP());
            cs.setString(2, ObjAlumno.getNombre());
            cs.setString(3, ObjAlumno.getApellido());

            // ✅ Conversión de fecha al formato PostgreSQL (YYYY-MM-DD)

            cs.setInt(4, ObjAlumno.getEdad());
            cs.setString(5, ObjAlumno.getGenero());
            cs.setString(6, ObjAlumno.getNombre_padre());
            cs.setString(7, ObjAlumno.getApellido_padre());
            cs.setString(8, ObjAlumno.getCorreo_padre());
            cs.setString(9, ObjAlumno.getTelefono_padre());
            cs.setInt(10, ObjAlumno.getGrupo());
                        try {
                DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate fechaSQL = LocalDate.parse(paramFecha.getText(), formatoEntrada);
                cs.setDate(11, java.sql.Date.valueOf(fechaSQL)); // <-- Aquí se inserta correctamente
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Formato de fecha inválido (usa dd-MM-yyyy)");
                return;
            }


            cs.execute();
            JOptionPane.showMessageDialog(null, "✅ Alumno insertado correctamente");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "❌ Error al insertar alumno: " + ex.getMessage());
        }
    }
}

    
    public void ModificarAlumno(Connection conexionExistente, 
                             JTextField paramID, JTextField paramCURP, JTextField paramNombre, JTextField paramApellido,
                             JTextField paramFecha, JTextField paramEdad, JComboBox<String> paramGenero, 
                             JTextField paramNombreP, JTextField paramApellidoP, JTextField paramCorreoP, 
                             JTextField paramTelefonoP, JTextField paramGrupo) {

    try {
        int idGrupo = Integer.parseInt(paramGrupo.getText());

        Alumno ObjAlumno = new Alumno();
        ObjAlumno.setId(Integer.parseInt(paramID.getText()));
        ObjAlumno.setCURP(paramCURP.getText());
        ObjAlumno.setNombre(paramNombre.getText());
        ObjAlumno.setApellido(paramApellido.getText());
        ObjAlumno.setFechaNacimiento(paramFecha.getText());
        ObjAlumno.setEdad(Integer.parseInt(paramEdad.getText()));
        ObjAlumno.setGenero(paramGenero.getSelectedItem().toString());
        ObjAlumno.setNombre_padre(paramNombreP.getText());
        ObjAlumno.setApellido_padre(paramApellidoP.getText());
        ObjAlumno.setCorreo_padre(paramCorreoP.getText());
        ObjAlumno.setTelefono_padre(paramTelefonoP.getText());
        ObjAlumno.setGrupo(idGrupo);

        String Consulta = "UPDATE alumnos SET curp =?, nombre =?, apellido =?, edad =?, genero =?, " +
                          "nombre_padre =?, apellido_padre =?, correo_padre =?, telefono_padre =?, " +
                          "id_grupo =?, fecha_nacimiento =? WHERE id_alumno =?;";

        CallableStatement cs = conexionExistente.prepareCall(Consulta);

        cs.setString(1, ObjAlumno.getCURP());
        cs.setString(2, ObjAlumno.getNombre());
        cs.setString(3, ObjAlumno.getApellido());
        cs.setInt(4, ObjAlumno.getEdad());
        cs.setString(5, ObjAlumno.getGenero());
        cs.setString(6, ObjAlumno.getNombre_padre());
        cs.setString(7, ObjAlumno.getApellido_padre());
        cs.setString(8, ObjAlumno.getCorreo_padre());
        cs.setString(9, ObjAlumno.getTelefono_padre());
        cs.setInt(10, ObjAlumno.getGrupo());
        cs.setString(11, ObjAlumno.getFechaNacimiento()); // ✅ FECHA AQUÍ
        cs.setInt(12, ObjAlumno.getId()); // ✅ ID AQUÍ

        int filas = cs.executeUpdate();

        if (filas > 0) {
            JOptionPane.showMessageDialog(null, "Modificación exitosa");
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró el alumno a modificar");
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al modificar: " + ex.getMessage());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Error en los datos numéricos (edad o grupo).");
    }
}

    
   public void EliminarAlumno(Connection conexionExistente, JTextField paramID) {
        Secretario ObjSecretario = new Secretario();
        ObjSecretario.setId(Integer.parseInt(paramID.getText()));

        String Consulta = "DELETE FROM alumnos WHERE id_alumno = ?;";

        int respuesta = JOptionPane.showConfirmDialog(
                null,
                "¿Deseas confirmar la eliminación?",
                "Confirmación requerida",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta == JOptionPane.OK_OPTION) {
            try (CallableStatement cs = conexionExistente.prepareCall(Consulta)) {
                cs.setInt(1, ObjSecretario.getId());
                cs.execute();

                JOptionPane.showMessageDialog(null, "✅ Alumno eliminado correctamente");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "❌ Error al eliminar: " + ex.getMessage());
            }
        }
    }


    private void marcarError(JTextField campo, boolean error) {
    if (error) {
        campo.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    } else {
        campo.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    }
}


    public void FiltrarAlumno(Connection IniciarConexion, JScrollPane jScrollPane1, JTextField Grupo_tb) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
