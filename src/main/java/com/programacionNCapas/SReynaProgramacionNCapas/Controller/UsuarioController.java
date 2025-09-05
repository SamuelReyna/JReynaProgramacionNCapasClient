package com.programacionNCapas.SReynaProgramacionNCapas.Controller;

import com.programacionNCapas.SReynaProgramacionNCapas.ML.DireccionML;
import com.programacionNCapas.SReynaProgramacionNCapas.ML.Result;
import com.programacionNCapas.SReynaProgramacionNCapas.ML.UsuarioML;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    private final String url = "http://localhost:8080/api/";

    @GetMapping
    public String Index(Model model) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<List<UsuarioML>>> responseEntity
                = restTemplate.exchange(url + "usuario",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result<List<UsuarioML>>>() {
                });

        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {

            Result result = responseEntity.getBody();
            if (result.correct) {
                model.addAttribute("usuarios", result.object);
            } else {
                model.addAttribute("usuarios", null);
            }

        }

        return "UsuarioIndex";
    }

    @GetMapping("Detalles/{IdUsuario}")
    public String UserDetail(@PathVariable int IdUsuario, Model model) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<UsuarioML>> responseEntity
                = restTemplate.exchange(url + "usuario" + "/" + IdUsuario,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result<UsuarioML>>() {
                });
        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            Result result = responseEntity.getBody();
            model.addAttribute("usuario", result.correct ? result.object : null);
        }

        return "UsuarioDetail";
    }

    @GetMapping("/Form")
    public String Form(
            @RequestParam(name = "IdUsuario", required = false, defaultValue = "0") int IdUsuario,
            @RequestParam(name = "IdDireccion", required = false, defaultValue = "0") int IdDireccion,
            @ModelAttribute("usuario") UsuarioML usuario,
            Model model) {

        RestTemplate restTemplate = new RestTemplate();
        if (IdUsuario == 0 && IdDireccion == 0) {
            //Agregar Usuario
            return "Form";
        } else if (IdUsuario != 0 && IdDireccion == -1) {
            ResponseEntity<Result<UsuarioML>> responseEntity
                    = restTemplate.exchange(url + "usuario" + "/" + IdUsuario,
                            HttpMethod.GET,
                            HttpEntity.EMPTY,
                            new ParameterizedTypeReference<Result<UsuarioML>>() {
                    });
            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                Result result = responseEntity.getBody();
                usuario = (UsuarioML) result.object;
                usuario.setIdUser(IdUsuario);
                usuario.setSexo(usuario.getSexo().trim());
                usuario.Direcciones = new ArrayList<>();
                DireccionML direccion = new DireccionML();
                direccion.setIdDireccion(IdDireccion);
                usuario.Direcciones.add((direccion));
                model.addAttribute("usuario", result.correct ? usuario : null);
            }
            //Editar Usuario
            return "Form";
        } else if (IdUsuario != 0 && IdDireccion == 0) {
            usuario.setIdUser(IdUsuario);
            //Agregar Dirección
            return "Form";
        } else if (IdUsuario != 0 && IdDireccion != 0 && IdDireccion != -1) {
            //Editar Dirección
            ResponseEntity<Result<DireccionML>> responseEntity
                    = restTemplate.exchange(url + "direccion" + "/" + IdDireccion,
                            HttpMethod.GET,
                            HttpEntity.EMPTY,
                            new ParameterizedTypeReference<Result<DireccionML>>() {
                    });
            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                Result result = responseEntity.getBody();
                DireccionML direccion = (DireccionML) result.object;
                usuario.Direcciones = new ArrayList<>();
                usuario.setIdUser(IdUsuario);
                usuario.Direcciones.add((direccion));
                model.addAttribute("usuario", result.correct ? usuario : null);
            }
            return "Form";
        }
        return "error";
    }

    @PostMapping("/add")
    public String Add(@RequestParam("imagenFile") MultipartFile imagen,
            @ModelAttribute("usuario") UsuarioML usuario
    ) {
        RestTemplate restTemplate = new RestTemplate();

        if (imagen != null && !imagen.isEmpty()) {
            try {
                String nombreArchivo = imagen.getOriginalFilename();
                if (nombreArchivo != null) {
                    String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf('.') + 1).toLowerCase();
                    if (extension.matches("jpg|jpeg|png")) {
                        String base64Img = Base64.getEncoder().encodeToString(imagen.getBytes());
                        usuario.setImg(base64Img);
                    }
                }
            } catch (Exception ex) {

            }
        }
        int idUsuario = usuario.getIdUser();
        int idDireccion = usuario.Direcciones.get(0).getIdDireccion();
        if (idUsuario == 0 && idDireccion == 0) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UsuarioML> requestEntity = new HttpEntity<>(usuario, headers);
            ResponseEntity<Result<UsuarioML>> responseEntity
                    = restTemplate.exchange(url + "usuario",
                            HttpMethod.POST,
                            requestEntity,
                            new ParameterizedTypeReference<Result<UsuarioML>>() {
                    });
            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                Result result = responseEntity.getBody();
                if (result.correct) {
                    return "redirect:/usuario";
                }
            }
        } else if (idUsuario != 0 && idDireccion == -1) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UsuarioML> requestEntity = new HttpEntity<>(usuario, headers);
            ResponseEntity<Result<UsuarioML>> responseEntity
                    = restTemplate.exchange(url + "usuario" + "/" + idUsuario,
                            HttpMethod.PUT,
                            requestEntity,
                            new ParameterizedTypeReference<Result<UsuarioML>>() {
                    });
            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                Result result = responseEntity.getBody();
                if (result.correct) {
                    return "redirect:/usuario";
                }
            }

        } else if (idUsuario != 0 && idDireccion == 0) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UsuarioML> requestEntity = new HttpEntity<>(usuario, headers);
            ResponseEntity<Result<UsuarioML>> responseEntity
                    = restTemplate.exchange(url + " direccion",
                            HttpMethod.POST,
                            requestEntity,
                            new ParameterizedTypeReference<Result<UsuarioML>>() {
                    });
            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                Result result = responseEntity.getBody();
                if (result.correct) {
                    return "redirect:/usuario/Detalles/" + idUsuario;
                }

            }
        } else if (idUsuario != 0 && idDireccion != 0 && idDireccion != -1) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UsuarioML> requestEntity = new HttpEntity<>(usuario, headers);
            ResponseEntity<Result<UsuarioML>> responseEntity
                    = restTemplate.exchange(url + " direccion/" + idDireccion,
                            HttpMethod.PUT,
                            requestEntity,
                            new ParameterizedTypeReference<Result<UsuarioML>>() {
                    });
            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                Result result = responseEntity.getBody();
                if (result.correct) {
                    return "redirect:/usuario/Detalles/" + idUsuario;
                }

            }
        }

        return "Error";
    }
    //    @PostMapping("/add")
    //    public String Add(
    //            @Valid @RequestParam("imagenFile") MultipartFile imagen,
    //            @ModelAttribute("usuario") UsuarioML usuario,
    //            BindingResult bindingResult,
    //            Model model) {
    //
    //        int idUsuario = usuario.getIdUser();
    //        int idDireccion = usuario.Direccion.getIdDireccion();
    //
    //        if (idUsuario != 0 && idDireccion == -1) {
    //            usuarioDAOJPAImplementation.Update(usuario);
    //            return "redirect:/usuario";
    //        }
    //        // Caso: actualización de dirección
    //        if (idUsuario != 0 && idDireccion != 0 && idDireccion != -2 && idDireccion != -1) {
    //            direccionDAOJPAImplementation.Update(usuario);
    //            return "redirect:/usuario/form?&IdUsuario=" + idUsuario + "&IdDireccion=0";
    //        }
    //        //Caso: crear dirección
    //        if (idUsuario != 0 && idDireccion == -2) {
    //            direccionDAOJPAImplementation.Add(usuario);
    //            return "redirect:/usuario/form?&IdUsuario=" + idUsuario + "&IdDireccion=0";
    //        }
    //        //Caso: Editar Usuario
    //
    //        // Caso: errores de validación
    //        // Procesar imagen
    //        if (imagen != null && !imagen.isEmpty()) {
    //            try {
    //                String nombreArchivo = imagen.getOriginalFilename();
    //                if (nombreArchivo != null) {
    //                    String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf('.') + 1).toLowerCase();
    //                    if (extension.matches("jpg|jpeg|png")) {
    //                        String base64Img = Base64.getEncoder().encodeToString(imagen.getBytes());
    //                        usuario.setImg(base64Img);
    //                    }
    //                }
    //            } catch (Exception ex) {
    //
    //            }
    //        }
    //
    //        // Guardar usuario
    //        if (idUsuario == 0 && idDireccion == 0) {
    //            usuarioDAOJPAImplementation.Add(usuario);
    //            return "redirect:/usuario";
    //        }
    //        return "Error";
    //    }
    //
    //    @PostMapping()
    //    public String Search(
    //            @ModelAttribute("usuario") UsuarioML usuario,
    //            Model model) {
    //        Result result = usuarioDAOImplementation.GetAll(usuario);
    //        if (result.correct) {
    //            model.addAttribute("usuarios", result.objects);
    //            model.addAttribute("Roles", rolDAOJPAImplementation.GetAll().objects);
    //        } else {
    //            model.addAttribute("usuarios", null);
    //        }
    //
    //        try {
    //            model.addAttribute("Roles", rolDAOJPAImplementation.GetAll().objects);
    //            model.addAttribute("Paises", paisDAOJPAImplementation.GetAll().objects);
    //        } catch (Exception ex) {
    //            result.ex = ex;
    //            result.errMessage = ex.getLocalizedMessage();
    //            result.correct = false;
    //            return "Exception";
    //        }
    //
    //        model.addAttribute("usuario", usuario);
    //        return "UsuarioIndex";
    //    }
    //
    //    @GetMapping("DeleteUsuario")
    //    @ResponseBody
    //    public ResponseEntity<Object> DeleteUsuario(@RequestParam int IdUser) {
    //        Result result = usuarioDAOJPAImplementation.Delete(IdUser);
    //
    //        if (result.correct) {
    //            return ResponseEntity.noContent().build(); // 204 No Content
    //        } else {
    //            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 si falla
    //        }
    //
    //    }
    //
    //    @GetMapping("/form")
    //    public String Form(
    //            @RequestParam(name = "IdUsuario", required = false, defaultValue = "0") int idUsuario,
    //            @RequestParam(name = "IdDireccion", required = false, defaultValue = "0") int idDireccion,
    //            @ModelAttribute("usuario") UsuarioML usuario,
    //            Model model
    //    ) {
    //        try {
    //            model.addAttribute("Roles", rolDAOJPAImplementation.GetAll().objects);
    //            model.addAttribute("Paises", paisDAOJPAImplementation.GetAll().objects);
    //            model.addAttribute("Colonias", coloniaDAOJPAImplementation.GetAll().objects);
    //            model.addAttribute("Estados", estadoDAOJPAImplementation.GetAll().objects);
    //            model.addAttribute("Municipios", municipioDAOJPAImplementation.GetAll().objects);
    //        } catch (Exception ex) {
    //            return "Exception";
    //        }
    //
    //        // Inicializar usuario y dirección
    //        usuario.setIdUser(idUsuario);
    //        usuario.Direccion = new DireccionML();
    //        usuario.Direccion.setIdDireccion(idDireccion);
    //
    //        // Crear Usuario
    //        if (idUsuario == 0 && idDireccion == 0) {
    //            return "Form"; // Formulario completo
    //        }
    //
    //        // Detalles de Usuario
    //        if (idUsuario != 0 && idDireccion == 0) {
    //            Result result = usuarioDAOJPAImplementation.GetOne(idUsuario);
    //            model.addAttribute("usuario", result.correct ? result.object : null);
    //            return "UsuarioDetail"; // Página de detalles
    //        }
    //
    //        // Editar Usuario
    //        if (idUsuario != 0 && idDireccion == -1) {
    //
    //            Result result = usuarioDAOJPAImplementation.GetOne(idUsuario);
    //
    //            UsuarioML usuarioML = (UsuarioML) result.object;
    //            usuarioML.setSexo(usuarioML.getSexo().trim());
    //            usuarioML.Direccion = new DireccionML();
    //            usuarioML.direcciones = new ArrayList<>();
    //            usuarioML.Direccion.setIdDireccion(-1);
    //
    //            model.addAttribute("usuario", usuarioML);
    //            return "Form"; // Solo datos de usuario para editar
    //        }
    //
    //        // Crear dirección
    //        if (idUsuario != 0 && idDireccion == -2) {
    //            return "Form"; // Solo para crear dirección
    //        }
    //
    //        // Editar dirección
    //        if (idUsuario != 0 && idDireccion > 0) {
    //            DireccionML direccionML = (DireccionML) direccionDAOJPAImplementation.GetOne(idDireccion).object;
    //
    //            usuario.setIdUser(idUsuario);
    //            usuario.Direccion = new DireccionML();
    //            usuario.Direccion = direccionML;
    //
    //            model.addAttribute("usuario", usuario);
    //            return "Form"; // Solo parte de dirección editar
    //        }
    //
    //        return "ERROR";
    //    }
    //
    //    @GetMapping("/cargamasiva")
    //    public String CargaMasiva() {
    //        return "CargaMasiva";
    //    }
    //
    //    @GetMapping("/estatus")
    //    @ResponseBody
    //    public ResponseEntity<Object> LogicalDelete(@RequestParam int IdUser) {
    //        Result result = usuarioDAOJPAImplementation.LogicalDelete(IdUser);
    //
    //        if (result.correct) {
    //            return ResponseEntity.noContent().build(); // 204 No Content
    //        } else {
    //            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 si falla
    //        }
    //
    //    }
    //
    //    @PostMapping("/cargamasiva")
    //    public String CargaMasiva(@RequestParam("archivo") MultipartFile file, Model model, HttpSession session) {
    //        List<ErrorCM> errores = new ArrayList<>();
    //        List<UsuarioML> usuarios = new ArrayList<>();
    //        String extension = file.getOriginalFilename().split("\\.")[1];
    //        Boolean valid = extension.equals("txt") || extension.equals("xlsx");
    //        if (valid) {
    //            String root = System.getProperty("user.dir");
    //            String pathfile = "/src/main/resources/files/";
    //            String upDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    //            String finalPath = root + pathfile + upDate + file.getOriginalFilename();
    //            try {
    //                file.transferTo(new File(finalPath));
    //            } catch (Exception ex) {
    //                System.out.println(ex);
    //            }
    //
    //            if (extension.equals("txt")) {
    //                usuarios = ProcesarTXT(new File(finalPath));
    //            } else if (extension.equals("xlsx")) {
    //                usuarios = ProcesarExcel(new File(finalPath));
    //            }
    //            errores = ValidarDatos(usuarios);
    //
    //            if (errores.isEmpty()) {
    //                model.addAttribute("listaErrores", errores);
    //                model.addAttribute("archivoCorrecto", true);
    //                session.setAttribute("path", finalPath);
    //
    //            } else {
    //                model.addAttribute("listaErrores", errores);
    //                model.addAttribute("archivoCorrecto", false);
    //            }
    //        } else {
    //            errores.add(new ErrorCM(1, "", "Tipo de Archvio Invalido"));
    //            model.addAttribute("listaErrores", errores);
    //            model.addAttribute("archivoCorrecto", false);
    //        }
    //
    //        return "CargaMasiva";
    //    }
    //
    //    @GetMapping("cargamasiva/procesar")
    //    public String cargaMasivaProcesar(HttpSession session) {
    //
    //        try {
    //            String path = (String) session.getAttribute("path");
    //            List<UsuarioML> usuarios;
    //
    //            if (path.split("\\.").equals("txt")) {
    //                usuarios = ProcesarTXT(new File(path));
    //            } else {
    //                usuarios = ProcesarExcel(new File(path));
    //            }
    //
    //            for (UsuarioML usuario : usuarios) {
    //                usuarioDAOImplementation.Add(usuario);
    //            }
    //            session.removeAttribute("path");
    //
    //        } catch (Exception ex) {
    //            System.out.println(ex.getLocalizedMessage());
    //        }
    //
    //        return "redirect:/usuario";
    //    }
    //
    //    private List<UsuarioML> ProcesarTXT(File file) {
    //        try {
    //            BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
    //            String linea = "";
    //            List<UsuarioML> usuarios = new ArrayList<>();
    //            while ((linea = bufferedReader.readLine()) != null) {
    //                String[] campos = linea.split("\\|");
    //                UsuarioML usuario = new UsuarioML();
    //                usuario.Rol = new RolML();
    //                usuario.Direccion = new DireccionML();
    //                usuario.Direccion.Colonia = new ColoniaML();
    //
    //                usuario.setNombreUsuario(campos[0]);
    //                usuario.setApellidoPaterno(campos[1]);
    //                usuario.setApellidoMaterno(campos[2]);
    //                usuario.setFechaNacimiento(LocalDate.parse(campos[3], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    //                usuario.setPassword(campos[4]);
    //                usuario.setSexo(campos[5]);
    //                usuario.setUsername(campos[6]);
    //                usuario.setEmail(campos[7]);
    //                usuario.setTelefono(campos[8]);
    //                usuario.setCelular(campos[9]);
    //                usuario.setCurp(campos[10]);
    //                usuario.Rol.setIdRol(Integer.parseInt(campos[11]));
    //                usuario.Direccion.setCalle(campos[12]);
    //                usuario.Direccion.setNumeroInterior(campos[13]);
    //                usuario.Direccion.setNumeroExterior(campos[14]);
    //                usuario.Direccion.Colonia.setIdColonia(Integer.parseInt(campos[15]));
    //                usuario.setImg("");
    //                usuarios.add(usuario);
    //            }
    //            return usuarios;
    //        } catch (Exception ex) {
    //            System.out.println(ex);
    //            return new ArrayList<>();
    //        }
    //
    //    }
    //
    //    private List<UsuarioML> ProcesarExcel(File file) {
    //        List<UsuarioML> usuarios = new ArrayList<>();
    //
    //        try (XSSFWorkbook workbook = new XSSFWorkbook(file)) {
    //            DataFormatter formatter = new DataFormatter();
    //            Sheet sheet = workbook.getSheetAt(0);
    //            for (Row row : sheet) {
    //                UsuarioML usuario = new UsuarioML();
    //                usuario.setNombreUsuario(row.getCell(0) != null ? row.getCell(0).toString() : "");
    //                usuario.setApellidoPaterno(row.getCell(1) != null ? row.getCell(1).toString() : "");
    //                usuario.setApellidoMaterno(row.getCell(2) != null ? row.getCell(2).toString() : "");
    //                usuario.setFechaNacimiento(
    //                        row.getCell(3).getCellType() == CellType.NUMERIC
    //                        ? row.getCell(3).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    //                        : LocalDate.parse(row.getCell(3).getStringCellValue(), DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    //                );
    //                usuario.setPassword(row.getCell(4).toString());
    //                usuario.setSexo(row.getCell(5).toString());
    //                usuario.setUsername(row.getCell(6).toString());
    //                usuario.setEmail(row.getCell(7).toString());
    //                usuario.setTelefono(formatter.formatCellValue(row.getCell(8)));
    //                usuario.setCelular(formatter.formatCellValue(row.getCell(9)));
    //                usuario.setCurp(row.getCell(10).toString());
    //                usuario.Rol = new RolML();
    //                usuario.Rol.setIdRol((int) row.getCell(11).getNumericCellValue());
    //                usuario.Direccion = new DireccionML();
    //                usuario.Direccion.setCalle(row.getCell(12).toString());
    //                usuario.Direccion.setNumeroInterior(formatter.formatCellValue(row.getCell(13)));
    //                usuario.Direccion.setNumeroExterior(formatter.formatCellValue(row.getCell(14)));
    //                usuario.Direccion.Colonia = new ColoniaML();
    //                usuario.Direccion.Colonia.setIdColonia((int) row.getCell(15).getNumericCellValue());
    //
    //                usuarios.add(usuario);
    //            }
    //
    //            return usuarios;
    //
    //        } catch (Exception ex) {
    //            System.out.println(ex);
    //            return new ArrayList<>();
    //        }
    //
    //    }
    //
    //    private List<ErrorCM> ValidarDatos(List<UsuarioML> usuarios) {
    //        List<ErrorCM> errores = new ArrayList<>();
    //        int linea = 1;
    //
    //        for (UsuarioML usuario : usuarios) {
    //            if (usuario.getNombreUsuario() == null
    //                    || "".equals(usuario.getNombreUsuario())) {
    //                errores.add(new ErrorCM(linea, "".equals(usuario.getNombreUsuario()) ? "vacio" : usuario.getNombreUsuario(), "Nombre es un campo obligatorio"));
    //            } else if (!OnlyLetters(usuario.getNombreUsuario())) {
    //                errores.add(new ErrorCM(linea, usuario.getNombreUsuario(), "Nombre no cumple con el formato requerido"));
    //            }
    //            if (usuario.getApellidoPaterno() == null
    //                    || "".equals(usuario.getApellidoPaterno())) {
    //                errores.add(new ErrorCM(linea, "".equals(usuario.getApellidoPaterno()) ? "vacio" : usuario.getApellidoPaterno(), "Apellido Paterno es un campo obligatorio"));
    //            } else if (!OnlyLetters(usuario.getApellidoPaterno())) {
    //                errores.add(new ErrorCM(linea, usuario.getApellidoPaterno(), "Apellido Paterno no cumple con el formato requerido"));
    //            }
    //            if (usuario.getApellidoMaterno() == null
    //                    || "".equals(usuario.getApellidoMaterno())) {
    //                errores.add(new ErrorCM(linea, "".equals(usuario.getApellidoMaterno()) ? "vacio" : usuario.getApellidoMaterno(), "Apellido Materno es un campo obligatorio"));
    //            } else if (!OnlyLetters(usuario.getApellidoMaterno())) {
    //                errores.add(new ErrorCM(linea, usuario.getApellidoMaterno(), "Apellido Materno no cumple con el formato requerido"));
    //            }
    //            if (usuario.getPassword() == null
    //                    || "".equals(usuario.getPassword())) {
    //                errores.add(new ErrorCM(linea, "".equals(usuario.getPassword()) ? "vacio" : "Vacio", "Contraseña es un campo obligatorio"));
    //            } else if (!validatePassword(usuario.getPassword())) {
    //                errores.add(new ErrorCM(linea, usuario.getPassword(), "Contrasena no cumple con el formato requerido"));
    //            }
    //            if (usuario.getEmail() == null
    //                    || "".equals(usuario.getEmail())) {
    //                errores.add(new ErrorCM(linea, "".equals(usuario.getEmail()) ? "vacio" : usuario.getEmail(), "Email es un campo obligatorio"));
    //            } else if (!validateEmail(usuario.getEmail())) {
    //                errores.add(new ErrorCM(linea, usuario.getEmail(), "Email no cumple con el formato requerido"));
    //            }
    //            if (usuario.getUsername() == null
    //                    || "".equals(usuario.getUsername())) {
    //                errores.add(new ErrorCM(linea, "".equals(usuario.getUsername()) ? "vacio" : usuario.getUsername(), "Username es un campo obligatorio"));
    //            } else if (!validateUsername(usuario.getUsername())) {
    //                errores.add(new ErrorCM(linea, usuario.getUsername(), "Username no cumple con el formato requerido"));
    //            }
    //
    //            linea++;
    //        }
    //        return errores;
    //    }

    static boolean OnlyLetters(String text) {
        String regexOnlyLetters = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$";
        Pattern pattern = Pattern.compile(regexOnlyLetters);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    static boolean validatePassword(String text) {
        String regexPassword = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regexPassword);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    static boolean validateEmail(String text) {
        String regexEmail = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        Pattern pattern = Pattern.compile(regexEmail);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    static boolean validateUsername(String text) {
        String regexUsername = "^(?!.*[_.]{2})[a-zA-Z0-9](?!.*[_.]{2})[a-zA-Z0-9._]{1,14}[a-zA-Z0-9]$";
        Pattern pattern = Pattern.compile(regexUsername);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
