package org.revo.auth.Controller;

import org.revo.auth.Service.BaseClientService;
import org.revo.auth.Service.UserService;
import org.revo.core.base.Domain.BaseClient;
import org.revo.core.base.Domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by ashraf on 10/04/17.
 */
@Controller
@SessionAttributes(types = AuthorizationRequest.class)
public class MainController {
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private BaseClientService baseClientService;

    @RequestMapping("/oauth/confirm_access")
    public ModelAndView getAccessConfirmation(@ModelAttribute AuthorizationRequest clientAuth, Principal user) {
        return new ModelAndView("access_confirmation").addObject("auth_request", clientAuth)
                .addObject("client", clientDetailsService.loadClientByClientId(clientAuth.getClientId()));
    }

    @GetMapping("signup")
    public String modelAndView(User user) {
        return "signup";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = {"/app"})
    public ModelAndView app(@AuthenticationPrincipal User user) {
        System.out.println(baseClientService.findAll(user.getId()).size());
        return new ModelAndView("app")
                .addObject("app", new BaseClient().init()).addObject("apps", baseClientService.findAll(user.getId()));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("app")
    public String app(@Valid BaseClient baseClient, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "app";
        baseClientService.save(baseClient);
        return "redirect:/app";
    }

    @GetMapping(value = {"/", "/home"})
    public ModelAndView home(@AuthenticationPrincipal User user) {
        return new ModelAndView("home").addObject("user", user);
    }

    @GetMapping(value = {"/403"})
    public ModelAndView e403() {
        return new ModelAndView("403.html");
    }

    @PostMapping("signup")
    public String modelAndView(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "signup";
        userService.encodeThenSave(user);
        return "redirect:/done?message=fake+message+'+please+check+you+email+to+activate+your+account+'";
    }

    @GetMapping("activate/{id}")
    public String modelAndView(@PathVariable String id) {
        userService.activate(id);
        return "redirect:/done?message=successfully+activate+your+account+you+are+welcome";
    }

    @GetMapping(value = "/user")
    @ResponseBody
    public Object getExtraInfo(OAuth2Authentication auth) {
        return tokenStore.readAccessToken(((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue()).getAdditionalInformation();
    }

    @Autowired
    private TokenStore tokenStore;
}
