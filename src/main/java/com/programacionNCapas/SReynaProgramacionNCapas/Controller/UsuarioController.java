package com.programacionNCapas.SReynaProgramacionNCapas.Controller;

import com.programacionNCapas.SReynaProgramacionNCapas.ML.DireccionML;
import com.programacionNCapas.SReynaProgramacionNCapas.ML.ErrorCM;
import com.programacionNCapas.SReynaProgramacionNCapas.ML.Result;
import com.programacionNCapas.SReynaProgramacionNCapas.ML.UsuarioML;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
            List<UsuarioML> listUsuarios = (List<UsuarioML>) result.object;

            if (result.correct) {
                model.addAttribute("usuarios", listUsuarios);
            } else {
                model.addAttribute("usuarios", null);
            }

        }

        return "UsuarioIndex";
    }

    @PostMapping("/search")
    public String Search(Model model, @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellidoPa,
            @RequestParam(required = false) String apellidoMa,
            @RequestParam(required = false, defaultValue = "0") String rol) {

        RestTemplate restTemplate = new RestTemplate();
        nombre = (nombre == null) ? "" : nombre.trim();
        apellidoPa = (apellidoPa == null) ? "" : apellidoPa.trim();
        apellidoMa = (apellidoMa == null) ? "" : apellidoMa.trim();
        ResponseEntity<Result<List<UsuarioML>>> responseEntity
                = restTemplate.exchange(url + "usuario",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result<List<UsuarioML>>>() {
                });

        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {

            Result result = responseEntity.getBody();
            List<UsuarioML> listUsuarios = (List<UsuarioML>) result.object;

            String filtroNombre = (nombre == null) ? "" : nombre.trim();
            String filtroApellidoPa = (apellidoPa == null) ? "" : apellidoPa.trim();
            String filtroApellidoMa = (apellidoMa == null) ? "" : apellidoMa.trim();
            String filtroRol = (rol == null) ? "0" : rol.trim();

            List<UsuarioML> filtrados = listUsuarios.stream()
                    .filter(u -> filtroNombre.isEmpty() || u.getNombreUsuario().toLowerCase().contains(filtroNombre.toLowerCase()))
                    .filter(u -> filtroApellidoPa.isEmpty() || u.getApellidoPaterno().toLowerCase().contains(filtroApellidoPa.toLowerCase()))
                    .filter(u -> filtroApellidoMa.isEmpty() || u.getApellidoMaterno().toLowerCase().contains(filtroApellidoMa.toLowerCase()))
                    .filter(u -> filtroRol.equals("0") || String.valueOf(u.getRol().getIdRol()).equals(filtroRol))
                    .toList();

            if (result.correct) {
                model.addAttribute("usuarios", filtrados);
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

    @GetMapping("/cargamasiva")
    public String CargaMasiva() {
        return "CargaMasiva";
    }

    @GetMapping("cargamasiva/procesar")
    public String cargaMasivaProcesar(HttpSession session) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String key = (String) session.getAttribute("key");
        Map<String, String> body = new HashMap<>();
        body.put("key", key);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Result> responseEntity
                = restTemplate.exchange(
                        url + "usuario/cargamasiva/procesar",
                        HttpMethod.POST,
                        requestEntity,
                        new ParameterizedTypeReference<Result>() {
                });
        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            Result result = responseEntity.getBody();
            if (result.correct) {
                return "redirect:/usuario";
            }
        }
        return "Error";
    }

    @PostMapping("/cargamasiva")
    public String CargaMasiva(@RequestParam("archivo") MultipartFile file, Model model,
            HttpSession session) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename(); // el nombre original del archivo
            }
        };
        body.add("archivo", fileAsResource);

        // Crear la petición
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Hacer el request con RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result> responseEntity = restTemplate.exchange(
                url + "/usuario/cargamasiva",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Result>() {
        });
        Result result = responseEntity.getBody();

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String key = (String) result.object;
            session.setAttribute("key", key);
            List<ErrorCM> erroresList = new ArrayList<>();
            model.addAttribute("listaErrores", erroresList);
            model.addAttribute("archivoCorrecto", true);

        } else if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED) {
            List<ErrorCM> erroresList = (List<ErrorCM>) result.object;
            model.addAttribute("listaErrores", erroresList);
            model.addAttribute("archivoCorrecto", false);
        } else {
            return "Error";
        }
        return "CargaMasiva";
    }

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
}
