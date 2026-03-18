package com.shopbazar.shopbazar.controller;
import com.shopbazar.shopbazar.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Role", description = "Endpoints for managing user roles and permissions (Administrative)")
public class RoleController {

    private final RoleService roleService;
}
