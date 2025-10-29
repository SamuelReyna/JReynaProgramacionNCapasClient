package com.programacionNCapas.SReynaProgramacionNCapas.Controller;

import com.programacionNCapas.SReynaProgramacionNCapas.ML.Password;
import com.programacionNCapas.SReynaProgramacionNCapas.ML.Result;
import com.programacionNCapas.SReynaProgramacionNCapas.ML.UsuarioML;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final String url = "http://localhost:8080/auth/";

    @GetMapping("/login")
    public String Auth() {
        return "login";
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "forgotPassword";
    }

    @PostMapping("/sendEmail")
    public String SendEmail(RedirectAttributes redirectAttributes, @RequestParam("email") String email) {
        if (!email.isEmpty()) {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> responseEntity
                    = restTemplate.exchange(url + "sendEmail?email=" + email,
                            HttpMethod.POST,
                            HttpEntity.EMPTY,
                            String.class);

            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                redirectAttributes.addFlashAttribute("message", "Correo enviado éxitosamente");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Correo no enviado");
            }
        }

        return "redirect:/forgotPassword";
    }

    @GetMapping("/changePassword")
    public String changePassword(@RequestParam("token") String token, Model model, @ModelAttribute Password password) {
        model.addAttribute("token", token);
        return "changePassword";
    }

    @PostMapping("/changePassword")
    public String RecoveryPassword(RedirectAttributes redirectAttributes, @ModelAttribute("password") Password password, @RequestParam("token") String token) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Password> requestEntity = new HttpEntity<>(password, headers);
        ResponseEntity<Result> responseEntity
                = restTemplate.exchange(url + "changePass?token=" + token,
                        HttpMethod.PATCH,
                        requestEntity,
                        new ParameterizedTypeReference<Result>() {
                });

        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            redirectAttributes.addFlashAttribute("message", "Contraseña cambiada exitosamente");
        }

        return "redirect:/login";
    }

    @GetMapping("/verify")
    public String Verify() {
        return "verify";
    }

    @PostMapping("/login")
    public String Login(@ModelAttribute("usuario") UsuarioML usuario, HttpSession http) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UsuarioML> requestEntity = new HttpEntity<>(usuario, headers);

        ResponseEntity<Map<String, Object>> responseEntity
                = restTemplate.exchange(url + "login",
                        HttpMethod.POST,
                        requestEntity,
                        new ParameterizedTypeReference<Map<String, Object>>() {
                }
                );
        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            http.setAttribute("token", responseEntity.getBody().get("token"));

            HttpHeaders headers1 = new HttpHeaders();

            HttpEntity<String> entity = new HttpEntity<>(headers1);
            headers1.setContentType(MediaType.APPLICATION_JSON);

            headers1.set("Authorization", "Bearer " + http.getAttribute("token"));

            ResponseEntity<Map<String, Object>> tokenDecode
                    = restTemplate.exchange(url + "decode",
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            http.setAttribute("role", (String) tokenDecode.getBody().get("role"));
        }

        return "redirect:/usuario";
    }

    @GetMapping("/logout")
    public String Logout(HttpSession session) {
        session.removeAttribute("token");
        session.removeAttribute("role");
        return "redirect:/login";
    }
}
