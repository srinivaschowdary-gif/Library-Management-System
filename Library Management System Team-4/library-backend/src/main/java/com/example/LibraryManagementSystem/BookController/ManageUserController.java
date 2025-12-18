package com.example.LibraryManagementSystem.BookController;

import com.example.LibraryManagementSystem.book.ManageUser;
import com.example.LibraryManagementSystem.payload.ManageUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Allow frontend requests; set specific origin in production
public class ManageUserController {

    private final ManageUserService userService;

    public ManageUserController(ManageUserService userService) {
        this.userService = userService;
    }

    // GET /api/users?q=search
    @GetMapping
    public List<ManageUser> list(@RequestParam(required = false) String q) {
        return userService.getAll(q);
    }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ManageUser> get(@PathVariable String id) {
        return userService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/users
    @PostMapping
    public ResponseEntity<ManageUser> create(@RequestBody ManageUser payload) {
        ManageUser created = userService.create(payload);
        return ResponseEntity.created(URI.create("/api/users/" + created.getId()))
                .body(created);
    }

    // PUT /api/users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ManageUser> update(@PathVariable String id, @RequestBody ManageUser payload) {
        try {
            ManageUser updated = userService.update(id, payload);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
